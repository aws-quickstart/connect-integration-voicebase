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
package com.voicebase.gateways.awsconnect.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voicebase.gateways.awsconnect.BeanFactory;
import com.voicebase.gateways.awsconnect.lambda.Lambda;
import com.voicebase.v3client.datamodel.VbCallbackFormatEnum;
import com.voicebase.v3client.datamodel.VbMedia;
import com.voicebase.v3client.datamodel.VbTranscript;
import com.voicebase.v3client.datamodel.VbTranscriptFormat;
import java.util.Collections;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/** @author Volker Kueffel <volker@voicebase.com> */
public class TranscriptionForwarderTest {

  private static ObjectMapper OM = BeanFactory.objectMapper();

  private VbTranscriptFormat transcriptFormat(VbCallbackFormatEnum format, int textLength) {
    VbTranscriptFormat f1 = new VbTranscriptFormat();
    f1.setFormat(format.getValue());
    f1.setData(RandomStringUtils.randomAlphanumeric(textLength));
    return f1;
  }

  private boolean containsFormat(VbMedia media, VbCallbackFormatEnum format) {
    if (media.getTranscript() == null || media.getTranscript().getAlternateFormats() == null) {
      return false;
    }

    for (VbTranscriptFormat transcript : media.getTranscript().getAlternateFormats()) {
      if (StringUtils.endsWithIgnoreCase(transcript.getFormat(), format.getValue())) {
        return true;
      }
    }

    return false;
  }

  @Test
  public void testRemoveAlterativeFormats() {
    VbMedia media = new VbMedia();
    VbTranscript transcript = new VbTranscript();
    media.setTranscript(transcript);

    transcript.addAlternateFormatsItem(transcriptFormat(VbCallbackFormatEnum.SRT, 16192));
    transcript.addAlternateFormatsItem(transcriptFormat(VbCallbackFormatEnum.DFXP, 16192));
    transcript.addAlternateFormatsItem(transcriptFormat(VbCallbackFormatEnum.WEBVTT, 16192));
    transcript.addAlternateFormatsItem(transcriptFormat(VbCallbackFormatEnum.TEXT, 16192));

    TranscriptionForwarder forwarder =
        new TranscriptionForwarder(
            Collections.singletonMap(Lambda.ENV_TRANSCRIPT_OUTPUT_TRIM_IF_TOO_LARGE, "true"));

    boolean removed = forwarder.removeTranscriptFormat(media, VbCallbackFormatEnum.DFXP);
    Assert.assertTrue(removed);
    Assert.assertFalse(containsFormat(media, VbCallbackFormatEnum.DFXP));
    Assert.assertTrue(containsFormat(media, VbCallbackFormatEnum.SRT));
    Assert.assertTrue(containsFormat(media, VbCallbackFormatEnum.WEBVTT));
    Assert.assertTrue(containsFormat(media, VbCallbackFormatEnum.TEXT));
  }

  @Test
  public void testReduceSizeIfTooLarge() throws Exception {
    VbMedia media = new VbMedia();
    VbTranscript transcript = new VbTranscript();
    media.setTranscript(transcript);

    transcript.addAlternateFormatsItem(
        transcriptFormat(
            VbCallbackFormatEnum.SRT, TranscriptionForwarder.KINESIS_MAX_MSG_SIZE / 2 + 1000));
    transcript.addAlternateFormatsItem(
        transcriptFormat(
            VbCallbackFormatEnum.DFXP, TranscriptionForwarder.KINESIS_MAX_MSG_SIZE / 2 + 1000));
    transcript.addAlternateFormatsItem(
        transcriptFormat(
            VbCallbackFormatEnum.WEBVTT, TranscriptionForwarder.KINESIS_MAX_MSG_SIZE / 4));
    transcript.addAlternateFormatsItem(
        transcriptFormat(
            VbCallbackFormatEnum.TEXT, TranscriptionForwarder.KINESIS_MAX_MSG_SIZE / 4));

    TranscriptionForwarder forwarder =
        new TranscriptionForwarder(
            Collections.singletonMap(Lambda.ENV_TRANSCRIPT_OUTPUT_TRIM_IF_TOO_LARGE, "true"));

    byte[] msgBytes = OM.writeValueAsBytes(media);
    msgBytes = forwarder.truncateIfNecessary(UUID.randomUUID().toString(), msgBytes);

    media = OM.readValue(msgBytes, VbMedia.class);

    Assert.assertFalse(containsFormat(media, VbCallbackFormatEnum.DFXP));
    Assert.assertFalse(containsFormat(media, VbCallbackFormatEnum.SRT));
    Assert.assertTrue(containsFormat(media, VbCallbackFormatEnum.WEBVTT));
    Assert.assertTrue(containsFormat(media, VbCallbackFormatEnum.TEXT));
  }

  @Test
  public void testDontReduceSizeIfTrimmingTurnedOff() throws Exception {
    VbMedia media = new VbMedia();
    VbTranscript transcript = new VbTranscript();
    media.setTranscript(transcript);

    transcript.addAlternateFormatsItem(
        transcriptFormat(
            VbCallbackFormatEnum.SRT, TranscriptionForwarder.KINESIS_MAX_MSG_SIZE / 2 + 1000));
    transcript.addAlternateFormatsItem(
        transcriptFormat(
            VbCallbackFormatEnum.DFXP, TranscriptionForwarder.KINESIS_MAX_MSG_SIZE / 2 + 1000));
    transcript.addAlternateFormatsItem(
        transcriptFormat(
            VbCallbackFormatEnum.WEBVTT, TranscriptionForwarder.KINESIS_MAX_MSG_SIZE / 4));
    transcript.addAlternateFormatsItem(
        transcriptFormat(
            VbCallbackFormatEnum.TEXT, TranscriptionForwarder.KINESIS_MAX_MSG_SIZE / 4));

    TranscriptionForwarder forwarder =
        new TranscriptionForwarder(
            Collections.singletonMap(Lambda.ENV_TRANSCRIPT_OUTPUT_TRIM_IF_TOO_LARGE, "false"));

    byte[] msgBytes = OM.writeValueAsBytes(media);
    msgBytes = forwarder.truncateIfNecessary(UUID.randomUUID().toString(), msgBytes);

    media = OM.readValue(msgBytes, VbMedia.class);

    Assert.assertTrue(containsFormat(media, VbCallbackFormatEnum.DFXP));
    Assert.assertTrue(containsFormat(media, VbCallbackFormatEnum.SRT));
    Assert.assertTrue(containsFormat(media, VbCallbackFormatEnum.WEBVTT));
    Assert.assertTrue(containsFormat(media, VbCallbackFormatEnum.TEXT));
  }

  @Test
  public void testDontReduceSizeIfFits() throws Exception {
    VbMedia media = new VbMedia();
    VbTranscript transcript = new VbTranscript();
    media.setTranscript(transcript);

    transcript.addAlternateFormatsItem(transcriptFormat(VbCallbackFormatEnum.SRT, 16192));
    transcript.addAlternateFormatsItem(transcriptFormat(VbCallbackFormatEnum.DFXP, 16192));
    transcript.addAlternateFormatsItem(transcriptFormat(VbCallbackFormatEnum.WEBVTT, 16192));
    transcript.addAlternateFormatsItem(transcriptFormat(VbCallbackFormatEnum.TEXT, 16192));

    TranscriptionForwarder forwarder =
        new TranscriptionForwarder(
            Collections.singletonMap(Lambda.ENV_TRANSCRIPT_OUTPUT_TRIM_IF_TOO_LARGE, "true"));

    byte[] msgBytes = OM.writeValueAsBytes(media);
    msgBytes = forwarder.truncateIfNecessary(UUID.randomUUID().toString(), msgBytes);

    media = OM.readValue(msgBytes, VbMedia.class);

    Assert.assertTrue(containsFormat(media, VbCallbackFormatEnum.DFXP));
    Assert.assertTrue(containsFormat(media, VbCallbackFormatEnum.SRT));
    Assert.assertTrue(containsFormat(media, VbCallbackFormatEnum.WEBVTT));
    Assert.assertTrue(containsFormat(media, VbCallbackFormatEnum.TEXT));
  }
}
