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
package com.voicebase.v3client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voicebase.v3client.datamodel.VbMedia;
import com.voicebase.v3client.datamodel.VbMetadata;
import com.voicebase.v3client.datamodel.VbStatusEnum;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

/** @author Volker Kueffel <volker@voicebase.com> */
public class ObjectMapperTest {

  @Test
  public void testIfVbMediaSerializationWorks() throws Exception {
    final ObjectMapper om = JacksonFactory.objectMapper();

    // create VbMedia and fill in a few random values
    VbMedia media = new VbMedia();
    media.setAccountId(UUID.randomUUID().toString());
    media.setMediaId(UUID.randomUUID().toString());

    VbMetadata metadata = new VbMetadata();
    metadata.setExternalId(UUID.randomUUID().toString());

    HashMap<String, Object> extended = new HashMap<>();
    extended.put("somekey", "somevalue");
    metadata.setExtended(extended);
    media.setMetadata(metadata);

    media.setLength(1234567L);

    media.setDateCreated(OffsetDateTime.now());
    media.setStatus(VbStatusEnum.FINISHED);

    // serialize VbMedia
    String serialized1 = om.writeValueAsString(media);

    // deserialize as map, add a few unknown keys
    Map<String, Object> map =
        om.readValue(serialized1, new TypeReference<Map<String, Object>>() {});
    map.put("_link", "http://www.example.com/link");
    map.put("newField", "newValue");
    String serialized2 = om.writeValueAsString(map);

    // deserialize as VbMedia
    VbMedia deserialized = om.readValue(serialized2, VbMedia.class);

    Assert.assertNotNull(deserialized);
    Assert.assertEquals(media.getAccountId(), deserialized.getAccountId());
    Assert.assertEquals(media.getMediaId(), deserialized.getMediaId());
    Assert.assertEquals(media.getLength(), deserialized.getLength());
    Assert.assertEquals(media.getMetadata(), deserialized.getMetadata());
    Assert.assertNotNull(deserialized.getDateCreated());
    Assert.assertTrue(media.getDateCreated().isEqual(deserialized.getDateCreated()));
    Assert.assertEquals(media.getStatus(), deserialized.getStatus());
  }
}
