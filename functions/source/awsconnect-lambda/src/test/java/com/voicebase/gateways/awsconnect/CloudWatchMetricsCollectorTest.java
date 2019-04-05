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

import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Test;

public class CloudWatchMetricsCollectorTest {

  @Test
  public void testCounts() throws InterruptedException {
    Date start = new Date();
    Thread.sleep(10);
    CloudWatchMetricsCollector collector =
        new CloudWatchMetricsCollector(null, "voicebase:test", 40, 0);
    collector.addCount("TestCount1", 1);
    Thread.sleep(10);
    collector.addCount("TestCount2", 20);
    Thread.sleep(10);
    collector.addCount("TestCount3", 48, "dim1", "dim1Value");
    Thread.sleep(10);
    collector.addCount("TestCount4", 44, "dim1", "dim1Value", "dim2", "dim2Value");
    List<MetricDatum> buffer = collector.readFromBuffer(4);
    Assert.assertEquals(buffer.size(), 4);
    Thread.sleep(10);
    Date end = new Date();

    MetricDatum datum;

    datum = buffer.get(0);
    Assert.assertEquals(StandardUnit.Count.name(), datum.getUnit());
    Assert.assertEquals("TestCount1", datum.getMetricName());
    Assert.assertEquals(1d, datum.getValue(), 0.01d);
    Assert.assertTrue(datum.getDimensions() == null || datum.getDimensions().isEmpty());
    Date metricDate1 = datum.getTimestamp();
    Assert.assertTrue(metricDate1.after(start));
    Assert.assertTrue(metricDate1.before(end));
    datum = buffer.get(1);
    Assert.assertEquals("TestCount2", datum.getMetricName());
    Assert.assertEquals(20d, datum.getValue(), 0.01d);
    Assert.assertTrue(datum.getDimensions() == null || datum.getDimensions().isEmpty());
    datum = buffer.get(2);
    Assert.assertEquals("TestCount3", datum.getMetricName());
    Assert.assertEquals(48d, datum.getValue(), 0.01d);
    Assert.assertEquals(1, datum.getDimensions().size());
    Assert.assertEquals("dim1", datum.getDimensions().get(0).getName());
    Assert.assertEquals("dim1Value", datum.getDimensions().get(0).getValue());
    datum = buffer.get(3);
    Assert.assertEquals("TestCount4", datum.getMetricName());
    Assert.assertEquals(44d, datum.getValue(), 0.01d);
    Assert.assertEquals(2, datum.getDimensions().size());
    Assert.assertEquals("dim1", datum.getDimensions().get(0).getName());
    Assert.assertEquals("dim1Value", datum.getDimensions().get(0).getValue());
    Assert.assertEquals("dim2", datum.getDimensions().get(1).getName());
    Assert.assertEquals("dim2Value", datum.getDimensions().get(1).getValue());
    Date metricDate4 = datum.getTimestamp();
    Assert.assertTrue(metricDate1.before(metricDate4));
  }

  @Test
  public void testTimingUnits() {
    CloudWatchMetricsCollector collector =
        new CloudWatchMetricsCollector(null, "voicebase:test", 40, 0);
    collector.addTiming("TestTiming1", 10, TimeUnit.NANOSECONDS);
    collector.addTiming("TestTiming1", 20, TimeUnit.MICROSECONDS);
    collector.addTiming("TestTiming1", 30, TimeUnit.MILLISECONDS);
    collector.addTiming("TestTiming1", 40, TimeUnit.SECONDS);
    collector.addTiming("TestTiming1", 50.7, TimeUnit.MINUTES);
    collector.addTiming("TestTiming1", 60, TimeUnit.HOURS);
    collector.addTiming("TestTiming1", 70, TimeUnit.DAYS);

    List<MetricDatum> buffer = collector.readFromBuffer(7);

    MetricDatum datum;

    datum = buffer.get(0);
    Assert.assertEquals(StandardUnit.Microseconds.name(), datum.getUnit());
    Assert.assertEquals(10d / 1000, datum.getValue(), 0.0001d);
    datum = buffer.get(1);
    Assert.assertEquals(StandardUnit.Microseconds.name(), datum.getUnit());
    Assert.assertEquals(20d, datum.getValue(), 0.0001d);
    datum = buffer.get(2);
    Assert.assertEquals(StandardUnit.Milliseconds.name(), datum.getUnit());
    Assert.assertEquals(30d, datum.getValue(), 0.0001d);
    datum = buffer.get(3);
    Assert.assertEquals(StandardUnit.Seconds.name(), datum.getUnit());
    Assert.assertEquals(40d, datum.getValue(), 0.0001d);
    datum = buffer.get(4);
    Assert.assertEquals(StandardUnit.Seconds.name(), datum.getUnit());
    Assert.assertEquals(50.7d * 60, datum.getValue(), 0.0001d);
    datum = buffer.get(5);
    Assert.assertEquals(StandardUnit.Seconds.name(), datum.getUnit());
    Assert.assertEquals(60d * 3600, datum.getValue(), 0.0001d);
    datum = buffer.get(6);
    Assert.assertEquals(StandardUnit.Seconds.name(), datum.getUnit());
    Assert.assertEquals(70d * 86400, datum.getValue(), 0.0001d);
  }
}
