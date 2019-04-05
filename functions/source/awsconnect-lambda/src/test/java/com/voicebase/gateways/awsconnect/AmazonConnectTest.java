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

import com.voicebase.gateways.awsconnect.lambda.Lambda;
import com.voicebase.sdk.util.IOUtil;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/** @author Volker Kueffel <volker@voicebase.com> */
public class AmazonConnectTest {

  private static String CTR_STRING;

  @BeforeClass
  public static void setupTests() throws Exception {
    CTR_STRING =
        IOUtil.readToString(
            AmazonConnectTest.class.getClassLoader().getResourceAsStream("ctr.json"));
  }

  /** Get a "fresh" CTR instance. */
  private Map<String, Object> ctr() throws Exception {
    return BeanFactory.newObjectMapper().readValue(CTR_STRING, Lambda.MSG_JAVA_TYPE);
  }

  @Test
  public void testInstanceId() throws Exception {
    Assert.assertEquals("38f09398-0db8-4368-a844-6cedc0c18362", AmazonConnect.getInstanceId(ctr()));
  }

  @Test
  public void testContactId() throws Exception {
    Assert.assertEquals("3aec0dc9-2817-4b95-8b72-492c8f972493", AmazonConnect.getContactId(ctr()));
  }

  @Test
  public void testRecordingLocation() throws Exception {
    Assert.assertEquals(
        "awsconnect-bucket/connect/my-connect-instance/CallRecordings/2019/01/02/3aec0dc9-2817-4b95-8b72-492c8f972493_20190102T00:07_UTC.wav",
        AmazonConnect.getRecordingLocation(ctr()));
  }

  @Test
  public void testGetAttributes() throws Exception {
    Map<String, Object> attributes = AmazonConnect.getAttributes(ctr());

    Assert.assertNotNull(attributes);
    Assert.assertEquals(2, attributes.size());
    Assert.assertEquals("1", attributes.get("voicebase_enabled").toString());
    Assert.assertEquals("testValue1", attributes.get("testAttribute1").toString());

    Assert.assertNull(AmazonConnect.getAttributes(new HashMap<>()));
  }

  @Test
  public void testAddAttributes() throws Exception {
    Map<String, Object> ctr = ctr();

    AmazonConnect.setAttribute(ctr, "newAttribute", "true");
    AmazonConnect.setAttribute(ctr, "anotherAttribute", "someValue");

    Map<String, Object> newAttributes = AmazonConnect.getAttributes(ctr);
    Assert.assertNotNull(newAttributes);
    Assert.assertEquals(4, newAttributes.size());
    Assert.assertEquals("1", newAttributes.get("voicebase_enabled").toString());
    Assert.assertEquals("testValue1", newAttributes.get("testAttribute1").toString());
    Assert.assertEquals("true", newAttributes.get("newAttribute").toString());
    Assert.assertEquals("someValue", newAttributes.get("anotherAttribute").toString());
  }

  @Test
  public void testCreateAttributes() throws Exception {
    Map<String, Object> ctr = new HashMap<>();

    AmazonConnect.setAttribute(ctr, "newAttribute", "true");
    AmazonConnect.setAttribute(ctr, "anotherAttribute", "someValue");

    Map<String, Object> newAttributes = AmazonConnect.getAttributes(ctr);
    Assert.assertNotNull(newAttributes);
    Assert.assertEquals(2, newAttributes.size());
    Assert.assertEquals("true", newAttributes.get("newAttribute").toString());
    Assert.assertEquals("someValue", newAttributes.get("anotherAttribute").toString());
  }
}
