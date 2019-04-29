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
package com.voicebase.sdk.v3;

import com.voicebase.sdk.util.ApiException;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

/** @author Volker Kueffel <volker@voicebase.com> */
public class VoiceBaseClientTest {

  @Test
  public void testRetryableExceptions() {
    VoiceBaseClient client = new VoiceBaseClient();

    Assert.assertTrue("IOException should be retryable", client.isRetryable(new IOException()));
    Assert.assertTrue(
        "Random RuntimeException should be retryable", client.isRetryable(new RuntimeException()));
    Assert.assertTrue(
        "ApiException with status code 500 should be retryable",
        client.isRetryable(new ApiException().withStatusCode(500)));
    Assert.assertFalse(
        "ApiException with status code 400 should not be retryable",
        client.isRetryable(new ApiException().withStatusCode(400)));
    Assert.assertFalse(
        "IllegalArgumentException should not be retryable",
        client.isRetryable(new IllegalArgumentException()));
  }
}
