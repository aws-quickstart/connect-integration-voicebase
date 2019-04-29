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
package com.voicebase.gateways.awsconnect.forward;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.voicebase.gateways.awsconnect.AmazonConnect;
import com.voicebase.sdk.v3.MediaProcessingRequest;
import com.voicebase.v3client.datamodel.VbConfiguration;
import com.voicebase.v3client.datamodel.VbMetricGroupConfiguration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/** @author Volker Kueffel <volker@voicebase.com> */
public class MediaProcessingRequestBuilderTest {

  private static ObjectMapper OM;

  @BeforeClass
  public static void setupTests() {
    OM = new ObjectMapper();
    OM.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);
    OM.enable(SerializationFeature.INDENT_OUTPUT);
  }

  private static CallbackProvider callbackProvider() {
    CallbackProvider cbp = new CallbackProvider();
    cbp.setCallbackMethod("POST");
    cbp.setCallbackUrl("http://example.com");
    cbp.setIncludes(new String[] {});
    return cbp;
  }

  private static Map<String, Object> awsConfigStub() {
    HashMap<String, Object> awsAttr = new HashMap<>();
    awsAttr.put(AmazonConnect.CTR_NODE_CONTACT_ID, (Object) "externalId");
    HashMap<String, String> vbAttr = new HashMap<>();
    awsAttr.put(AmazonConnect.CTR_NODE_ATTRIBUTES, vbAttr);
    return awsAttr;
  }

  private static Map<String, Object> getVbAttributes(Map<String, Object> awsInput) {
    return AmazonConnect.getAttributes(awsInput);
  }

  private static MediaProcessingRequestBuilder requestBuilderStub() {
    MediaProcessingRequestBuilder builder =
        new MediaProcessingRequestBuilder()
            .withCallbackProvider(callbackProvider())
            .withConfigureSpeakers(true)
            .withPredictionsEnabled(true)
            .withKnowledgeDiscoveryEnabled(true)
            .withAdvancedPunctuationEnabled(true)
            .withAwsInputData(awsConfigStub())
            .withLeftSpeakerName("left")
            .withRightSpeakerName("right")
            .withCategorizationFeatureEnabled(true)
            .withIndexingFeatureEnabled(true);
    return builder;
  }

  @Test
  public void testAdvancedFeatures() throws IOException {
    MediaProcessingRequestBuilder builder = requestBuilderStub();

    MediaProcessingRequest req = builder.build();

    Assert.assertTrue(
        req.getConfiguration()
            .getSpeechModel()
            .getFeatures()
            .contains(MediaProcessingRequestBuilder.SPEECH_FEATURE_ADVANCED_PUNCTUATION));
    verifyJSONConfiguration("advanced-features.json", req.getConfiguration());
  }

  void verifyJSONConfiguration(String resource, VbConfiguration vbConfiguration)
      throws IOException {

    Assert.assertEquals(
        IOUtils.resourceToString(
            resource, Charsets.UTF_8, MediaProcessingRequestBuilderTest.class.getClassLoader()),
        OM.writeValueAsString(vbConfiguration));
  }

  @Test
  public void testLanguageExtensions() throws IOException {
    MediaProcessingRequestBuilder builder = requestBuilderStub();
    Map<String, Object> vbAttr = getVbAttributes(builder.getAwsInputData());

    vbAttr.put("voicebase_languageExtensions", "voicemail");

    MediaProcessingRequest req = builder.build();

    Assert.assertTrue(
        req.getConfiguration().getSpeechModel().getExtensions().contains("voicemail"));
    verifyJSONConfiguration("language-extensions.json", req.getConfiguration());
  }

  @Test
  public void testVoiceFeatures() throws IOException {
    MediaProcessingRequestBuilder builder = requestBuilderStub();
    Map<String, Object> vbAttr = getVbAttributes(builder.getAwsInputData());

    vbAttr.put("voicebase_classifier_names", "blah");

    MediaProcessingRequest req = builder.build();

    Assert.assertTrue(
        req.getConfiguration()
            .getSpeechModel()
            .getFeatures()
            .contains(MediaProcessingRequestBuilder.SPEECH_FEATURE_VOICE));
    verifyJSONConfiguration("voice-features.json", req.getConfiguration());
  }

  @Test
  public void testAnalyticIndexing() throws IOException {
    MediaProcessingRequestBuilder builder = requestBuilderStub();
    Map<String, Object> vbAttr = getVbAttributes(builder.getAwsInputData());

    vbAttr.put("voicebase_enableAnalyticIndexing", "1");

    MediaProcessingRequest req = builder.build();

    Assert.assertTrue(req.getConfiguration().getPublish().isEnableAnalyticIndexing());
    verifyJSONConfiguration("analytic-indexing.json", req.getConfiguration());
  }

  @Test
  public void testEnableAllCategories() throws IOException {
    MediaProcessingRequestBuilder builder = requestBuilderStub();
    Map<String, Object> vbAttr = getVbAttributes(builder.getAwsInputData());

    vbAttr.put("voicebase_enableAllCategories", "1");

    MediaProcessingRequest req = builder.build();

    Assert.assertEquals(1, req.getConfiguration().getCategories().size());
    Assert.assertTrue(req.getConfiguration().getCategories().get(0).isAllCategories());
    verifyJSONConfiguration("all-categories.json", req.getConfiguration());
  }

  @Test
  public void testSpecificCategories() throws IOException {
    MediaProcessingRequestBuilder builder = requestBuilderStub();
    Map<String, Object> vbAttr = getVbAttributes(builder.getAwsInputData());

    vbAttr.put("voicebase_categoryNames", "alfa, gamma, beta");

    MediaProcessingRequest req = builder.build();

    Assert.assertEquals(3, req.getConfiguration().getCategories().size());
    Assert.assertEquals("alfa", req.getConfiguration().getCategories().get(0).getCategoryName());
    Assert.assertEquals("gamma", req.getConfiguration().getCategories().get(1).getCategoryName());
    Assert.assertEquals("beta", req.getConfiguration().getCategories().get(2).getCategoryName());
    verifyJSONConfiguration("specific-categories.json", req.getConfiguration());
  }

  @Test
  public void testMetricsConfig() throws IOException {
    MediaProcessingRequestBuilder builder = requestBuilderStub();
    Map<String, Object> vbAttr = getVbAttributes(builder.getAwsInputData());

    vbAttr.put("voicebase_metrics_groups", "overtalk,sentiment,talk-style-tone-and-volume");

    MediaProcessingRequest req = builder.build();

    List<VbMetricGroupConfiguration> metrics = req.getConfiguration().getMetrics();
    Assert.assertNotNull(metrics);
    Assert.assertEquals(3, metrics.size());

    ArrayList<String> groups = new ArrayList<>();
    for (VbMetricGroupConfiguration metric : metrics) {
      groups.add(metric.getMetricGroupName());
    }

    Assert.assertTrue(groups.contains("overtalk"));
    Assert.assertTrue(groups.contains("sentiment"));
    Assert.assertTrue(groups.contains("talk-style-tone-and-volume"));
    verifyJSONConfiguration("metrics.json", req.getConfiguration());
  }

  @Test
  public void testDetectorConfig() throws IOException {
    // test if detectors are configured
    MediaProcessingRequestBuilder builder = requestBuilderStub();
    Map<String, Object> vbAttr = getVbAttributes(builder.getAwsInputData());

    vbAttr.put("voicebase_pciRedaction", "1");
    vbAttr.put("voicebase_numberRedaction", "1");

    MediaProcessingRequest req = builder.build();

    verifyJSONConfiguration("detectors.json", req.getConfiguration());

    // test if no detectors are configured
    builder = requestBuilderStub();
    vbAttr = getVbAttributes(builder.getAwsInputData());

    vbAttr.put("voicebase_pciRedaction", "0");
    vbAttr.put("voicebase_numberRedaction", "0");

    req = builder.build();
    Assert.assertNull(req.getConfiguration().getPrediction().getDetectors());
  }

  @Test
  public void testCallbackConfiguration() {
    MediaProcessingRequestBuilder builder;
    MediaProcessingRequest req;

    builder = requestBuilderStub();
    req = builder.build();
    Assert.assertNotNull(req.getConfiguration().getPublish().getCallbacks());

    builder = requestBuilderStub();
    builder.setCallbackProvider(null);
    req = builder.build();
    Assert.assertNull(req.getConfiguration().getPublish().getCallbacks());

    CallbackProvider cbp = callbackProvider();
    cbp.setCallbackUrl(null);
    cbp.setAdditionalCallbackUrls(null);
    builder = requestBuilderStub();
    builder.setCallbackProvider(cbp);
    req = builder.build();
    Assert.assertNull(req.getConfiguration().getPublish().getCallbacks());
  }
}
