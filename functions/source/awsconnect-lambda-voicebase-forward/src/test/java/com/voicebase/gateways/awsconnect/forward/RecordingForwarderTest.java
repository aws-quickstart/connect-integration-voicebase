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

import static com.voicebase.gateways.awsconnect.VoiceBaseAttributeExtractor.getVoicebaseAttributeName;
import static org.junit.Assert.assertFalse;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.voicebase.gateways.awsconnect.AmazonConnect;
import com.voicebase.gateways.awsconnect.lambda.Lambda;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class RecordingForwarderTest {

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

  @Test
  public void testIfVoiceBaseEnableFlowVariableIsHonored() {
    RecordingForwarder forwarder = new RecordingForwarder();
    String flowVariable = getVoicebaseAttributeName(Lambda.VB_ATTR_ENABLE);

    Map<String, Object> awsAttr = awsConfigStub();
    Assert.assertTrue(
        "Should forward request if respective flow variable isn't set",
        forwarder.shouldProcess(awsAttr));

    awsAttr = awsConfigStub();
    Map<String, Object> vbAttr = getVbAttributes(awsAttr);
    vbAttr.put(flowVariable, "1");
    Assert.assertTrue(
        "Should forward request if respective flow variable is set to 1",
        forwarder.shouldProcess(awsAttr));

    awsAttr = awsConfigStub();
    vbAttr = getVbAttributes(awsAttr);
    vbAttr.put(flowVariable, "0");

    Assert.assertFalse(
        "Should not forward request if respective flow variable is set to 0",
        forwarder.shouldProcess(awsAttr));
  }

  @Test
  public void testVerifyAudioAvailability() {
    Map<String, Object> ctrAsMap = awsConfigStub();
    Map<String, Object> attr = AmazonConnect.getAttributes(ctrAsMap);
    attr.put("x-voicebase_timesToFailAudioExists", 3);
    RecordingForwarder forwarder = new RecordingForwarder();

    assertFalse(forwarder.verifyAudioAvailability(ctrAsMap, "alfa", "anything"));
    forwarder.setRedeliveryCount(ctrAsMap, 1);
    assertFalse(forwarder.verifyAudioAvailability(ctrAsMap, "alfa", "anything"));
    forwarder.setRedeliveryCount(ctrAsMap, 2);
    assertFalse(forwarder.verifyAudioAvailability(ctrAsMap, "alfa", "anything"));

    forwarder.setRedeliveryCount(ctrAsMap, 4);
    try {
      forwarder.verifyAudioAvailability(ctrAsMap, "alfa", "anything");
    } catch (AmazonS3Exception se) {

    }
  }
}
