/**
 * Copyright 2016-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. Licensed under the
 * Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.voicebase.gateways.awsconnect;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Volker Kueffel <volker@voicebase.com> */
public class CloudWatchMetricsCollector implements MetricsCollector {

  public static final String DEFAULT_NAMESPACE = "voicebase";
  public static final int DEFAULT_BUFFER_SIZE = 20;
  public static final long DEFAULT_BUFFER_FLUSH_INTERVAL = 5000;

  private static final Logger LOGGER = LoggerFactory.getLogger(CloudWatchMetricsCollector.class);

  private static final int AWS_LIMIT_DIMENSIONS = 10;
  private static final int AWS_LIMIT_METRICS = 20;

  private final AmazonCloudWatch cloudWatchClient;
  private final String namespace;
  private final int maxBufferSize;
  private final BlockingDeque<MetricDatum> buffer;
  private final ScheduledExecutorService executor;
  private boolean publishIfBufferFull = false;

  /**
   * Create collector with default name space, buffer size and flush interval using the default AWS
   * credentials provider chain.
   *
   * @see #DEFAULT_NAMESPACE
   * @see #DEFAULT_BUFFER_SIZE
   * @see #DEFAULT_BUFFER_FLUSH_INTERVAL
   */
  public CloudWatchMetricsCollector() {
    this(null, DEFAULT_NAMESPACE, DEFAULT_BUFFER_SIZE, DEFAULT_BUFFER_FLUSH_INTERVAL);
  }

  /**
   * Create collector with default name space, buffer size and flush interval using the given static
   * AWS credentials.
   *
   * @param awsCredentials AWS credentials
   * @see #DEFAULT_NAMESPACE
   * @see #DEFAULT_BUFFER_SIZE
   * @see #DEFAULT_BUFFER_FLUSH_INTERVAL
   */
  public CloudWatchMetricsCollector(AWSCredentials awsCredentials) {
    this(
        awsCredentials != null ? new AWSStaticCredentialsProvider(awsCredentials) : null,
        DEFAULT_NAMESPACE,
        DEFAULT_BUFFER_SIZE,
        DEFAULT_BUFFER_FLUSH_INTERVAL);
  }

  /**
   * Create collector using given parameters and the default AWS credentials provider chain.
   *
   * @param namespace metric name space
   * @param bufferSize size of metric buffer, if buffer size exceeds this value new metrics are
   *     discarded
   * @param flushInterval interval in ms in which metrics are flushed to AWS CloudWatch.
   */
  public CloudWatchMetricsCollector(String namespace, int bufferSize, long flushInterval) {
    this(null, namespace, bufferSize, flushInterval);
  }

  /**
   * Create a new collector.
   *
   * @param credentialsProvider AWS credentials provider to use for authentication/authorization.
   * @param namespace metric name space
   * @param bufferSize size of metric buffer, if buffer size exceeds this value new metrics are
   *     discarded
   * @param flushInterval interval in ms in which metrics are flushed to AWS CloudWatch.
   */
  public CloudWatchMetricsCollector(
      AWSCredentialsProvider credentialsProvider,
      String namespace,
      int bufferSize,
      long flushInterval) {
    AmazonCloudWatchClientBuilder builder = AmazonCloudWatchClientBuilder.standard();
    if (credentialsProvider != null) {
      builder.withCredentials(credentialsProvider);
    }
    cloudWatchClient = builder.build();
    maxBufferSize = bufferSize > 0 ? bufferSize : 0;
    this.namespace = namespace;

    if (maxBufferSize > 0) {
      LOGGER.info(
          "Metrics collection starting up, buffer size: {}, flush interval: {}",
          bufferSize,
          flushInterval);
      buffer = new LinkedBlockingDeque<>(maxBufferSize);
      if (flushInterval > 0) {
        executor = new ScheduledThreadPoolExecutor(1, new ThreadPoolExecutor.DiscardPolicy());
        executor.scheduleAtFixedRate(
            this::flush, flushInterval, flushInterval, TimeUnit.MILLISECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
      } else {
        executor = null;
      }
    } else {
      buffer = null;
      executor = null;
    }
  }

  public boolean isPublishIfBufferFull() {
    return publishIfBufferFull;
  }

  public void setPublishIfBufferFull(boolean publishIfBufferFull) {
    this.publishIfBufferFull = publishIfBufferFull;
  }

  public MetricsCollector withPublishIfBufferFull(boolean publishIfBufferFull) {
    setPublishIfBufferFull(publishIfBufferFull);
    return this;
  }

  private void shutdown() {
    executor.shutdown();
    flush();
  }

  @Override
  public void addCount(String metricName, Number value) {
    doAddCount(metricName, value, null);
  }

  @Override
  public void addCount(String metricName, Number value, String... dimensions) {
    doAddCount(metricName, value, getDimensionsFromArray(dimensions));
  }

  @Override
  public void addCount(String metricName, Number value, Map<String, String> dimensions) {
    doAddCount(metricName, value, getDimensionsFromMap(dimensions));
  }

  private void doAddCount(String metricName, Number value, List<Dimension> dimensions) {
    if (metricName == null || value == null) {
      return;
    }

    MetricDatum datum =
        new MetricDatum()
            .withUnit(StandardUnit.Count)
            .withMetricName(metricName)
            .withValue(value.doubleValue())
            .withTimestamp(new Date())
            .withDimensions(dimensions);

    addMetric(datum);
  }

  @Override
  public void addTiming(String metricName, Number value, TimeUnit timeUnit) {
    doAddTiming(metricName, value, timeUnit, null);
  }

  @Override
  public void addTiming(String metricName, Number value, TimeUnit timeUnit, String... dimensions) {
    doAddTiming(metricName, value, timeUnit, getDimensionsFromArray(dimensions));
  }

  @Override
  public void addTiming(
      String metricName, Number value, TimeUnit timeUnit, Map<String, String> dimensions) {
    doAddTiming(metricName, value, timeUnit, getDimensionsFromMap(dimensions));
  }

  private void doAddTiming(
      String metricName, Number value, TimeUnit timeUnit, List<Dimension> dimensions) {
    if (metricName == null || value == null || timeUnit == null) {
      return;
    }

    StandardUnit unit = StandardUnit.Seconds;
    Double metricValue = value.doubleValue();

    switch (timeUnit) {
      case DAYS:
        metricValue = 86400 * value.doubleValue();
        break;
      case HOURS:
        metricValue = 3600 * value.doubleValue();
        break;
      case MINUTES:
        metricValue = 60 * value.doubleValue();
        break;
      case MILLISECONDS:
        unit = StandardUnit.Milliseconds;
        break;
      case MICROSECONDS:
        unit = StandardUnit.Microseconds;
        break;
      case NANOSECONDS:
        unit = StandardUnit.Microseconds;
        metricValue = value.doubleValue() / 1000;
        break;
      default:
        break;
    }

    MetricDatum datum =
        new MetricDatum()
            .withUnit(unit)
            .withMetricName(metricName)
            .withValue(metricValue.doubleValue())
            .withTimestamp(new Date())
            .withDimensions(dimensions);

    addMetric(datum);
  }

  /** Add metric to buffer, discard metric if there is no buffer or it is full. */
  private void addMetric(MetricDatum metric) {
    if (metric == null) {
      return;
    }
    if (buffer != null) {
      boolean accepted = buffer.offerLast(metric);
      if (!accepted) {
        // send out one batch if buffer is full
        publish(AWS_LIMIT_METRICS);
        // give it another try
        buffer.offerLast(metric);
      }
    } else {
      // publish synchronously
      publishBatch(Collections.singletonList(metric));
    }
  }

  /**
   * Create metric dimensions.
   *
   * <p>Discards map entries where either key or value is blank. Also enforces the AWS limit for
   * metric dimensions by dropping all dimensions once the limit is reached.
   *
   * @param dimensions as map
   * @return metric dimensions
   * @see #AWS_LIMIT_DIMENSIONS
   */
  List<Dimension> getDimensionsFromMap(Map<String, String> dimensions) {
    List<Dimension> dims = null;
    if (dimensions != null && !dimensions.isEmpty()) {
      dims = new ArrayList<>();
      for (Entry<String, String> dimension : dimensions.entrySet()) {
        if (!StringUtils.isBlank(dimension.getKey())
            && !StringUtils.isBlank(dimension.getValue())) {
          dims.add(new Dimension().withName(dimension.getKey()).withValue(dimension.getValue()));
        }
        if (dims.size() == AWS_LIMIT_DIMENSIONS) {
          break;
        }
      }
    }
    return dims;
  }

  List<Dimension> getDimensionsFromArray(String[] dimensions) {
    List<Dimension> dims = null;
    if (dimensions != null && dimensions.length > 1) {
      dims = new LinkedList<>();
      for (int i = 0; i < dimensions.length - 1; i = i + 2) {
        String key = dimensions[i];
        String value = dimensions[i + 1];
        if (!StringUtils.isBlank(key) && !StringUtils.isBlank(value)) {
          dims.add(new Dimension().withName(key).withValue(value));
        }
        if (dims.size() == AWS_LIMIT_DIMENSIONS) {
          break;
        }
      }
    }
    return dims;
  }

  @Override
  public void flush() {
    publish(buffer.size());
  }

  private void publish(int recordsToSend) {
    if (buffer.isEmpty()) {
      return;
    }

    LOGGER.info("Sending {} metrics to CloudWatch...", recordsToSend);
    List<MetricDatum> data;
    do {
      data = readFromBuffer(AWS_LIMIT_METRICS);
      if (!data.isEmpty()) {
        publishBatch(data);
        recordsToSend -= data.size();
      }
    } while (recordsToSend > 0 && !data.isEmpty());
  }

  private void publishBatch(List<MetricDatum> partition) {
    PutMetricDataRequest request = new PutMetricDataRequest();
    request.setNamespace(namespace);
    request.setMetricData(partition);
    try {
      cloudWatchClient.putMetricData(request);
    } catch (Exception e) {
      LOGGER.warn("Unable to push metrics to CloudWatch: {}", e.getMessage());
      // request failed, put everything back into the buffer
      buffer.addAll(partition);
    }
  }

  synchronized List<MetricDatum> readFromBuffer(int recordsToRead) {
    LinkedList<MetricDatum> data = new LinkedList<>();
    MetricDatum datum;
    while ((datum = buffer.pollFirst()) != null && data.size() < recordsToRead) {
      data.add(datum);
    }
    return data;
  }
}
