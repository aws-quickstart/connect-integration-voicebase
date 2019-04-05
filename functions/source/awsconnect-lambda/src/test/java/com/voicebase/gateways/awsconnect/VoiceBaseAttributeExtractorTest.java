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

import org.junit.Assert;
import org.junit.Test;

/** @author Volker Kueffel <volker@voicebase.com> */
public class VoiceBaseAttributeExtractorTest {

  @Test
  public void testVoiceBaseAttributeNames() {
    Assert.assertEquals(
        "voicebase_enabled", VoiceBaseAttributeExtractor.getVoicebaseAttributeName("enabled"));
    Assert.assertEquals(
        "voicebase_knowledge_discovery_enabled",
        VoiceBaseAttributeExtractor.getVoicebaseAttributeName("knowledge", "discovery", "enabled"));
  }

  @Test
  public void testVoiceBaseXAttributeNames() {
    Assert.assertEquals(
        "x-voicebase_enabled", VoiceBaseAttributeExtractor.getVoicebaseXAttributeName("enabled"));
    Assert.assertEquals(
        "x-voicebase_knowledge_discovery_enabled",
        VoiceBaseAttributeExtractor.getVoicebaseXAttributeName(
            "knowledge", "discovery", "enabled"));
  }
}
