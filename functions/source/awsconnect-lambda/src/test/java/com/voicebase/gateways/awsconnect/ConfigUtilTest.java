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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class ConfigUtilTest {

  @Test
  public void testGetStringListSetting_NonExistingKey() {
    List<String> defaultList = Arrays.asList("a", "b", "c");
    Map<String, String> env = new HashMap<>();
    List<String> result = ConfigUtil.getStringListSetting(env, "mykey", defaultList);
    assertEquals(3, result.size());
  }

  @Test
  public void testGetStringListSetting_KeyExistsButEmpty() {
    List<String> defaultList = Arrays.asList("a", "b", "c");
    Map<String, String> env = new HashMap<>();
    env.put("mykey", "");
    List<String> result = ConfigUtil.getStringListSetting(env, "mykey", defaultList);
    assertEquals(3, result.size());
  }

  @Test
  public void testGetStringListSetting_KeyExistsButJustCommas() {
    List<String> defaultList = Arrays.asList("a", "b", "c");
    Map<String, String> env = new HashMap<>();
    env.put("mykey", ",");
    List<String> result = ConfigUtil.getStringListSetting(env, "mykey", defaultList);
    assertEquals(0, result.size());
  }

  @Test
  public void testGetStringListSetting_KeyExists_SingleValue() {
    List<String> defaultList = Arrays.asList("a", "b", "c");
    Map<String, String> env = new HashMap<>();
    env.put("mykey", "d");
    List<String> result = ConfigUtil.getStringListSetting(env, "mykey", defaultList);
    assertEquals(1, result.size());
    assertEquals("d", result.get(0));
  }

  @Test
  public void testGetStringListSetting_KeyExists_MultipleValues() {
    List<String> defaultList = Arrays.asList("a", "b", "c");
    Map<String, String> env = new HashMap<>();
    env.put("mykey", "d, e");
    List<String> result = ConfigUtil.getStringListSetting(env, "mykey", defaultList);
    assertEquals(2, result.size());
    assertEquals("d", result.get(0));
    assertEquals("e", result.get(1));
  }

  @Test
  public void testGetStringListSetting_KeyExists_ExtraComma() {
    List<String> defaultList = Arrays.asList("a", "b", "c");
    Map<String, String> env = new HashMap<>();
    env.put("mykey", ",,d, e,");
    List<String> result = ConfigUtil.getStringListSetting(env, "mykey", defaultList);
    assertEquals(2, result.size());
    assertEquals("d", result.get(0));
    assertEquals("e", result.get(1));
  }

  @Test
  public void testGetStringListSetting_KeyExists_ExtraSpaces() {
    List<String> defaultList = Arrays.asList("a", "b", "c");
    Map<String, String> env = new HashMap<>();
    env.put("mykey", " d , e , f ");
    List<String> result = ConfigUtil.getStringListSetting(env, "mykey", defaultList);
    assertEquals(3, result.size());
    assertEquals("d", result.get(0));
    assertEquals("e", result.get(1));
    assertEquals("f", result.get(2));
  }
}
