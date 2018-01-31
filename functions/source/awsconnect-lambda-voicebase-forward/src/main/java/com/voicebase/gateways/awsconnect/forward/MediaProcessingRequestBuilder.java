/**
 * Copyright 2017-2018 VoiceBase, Inc. or its affiliates. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not
 * use this file except in compliance with the License. A copy of the License is
 * located at 
 * 
 *      http://aws.amazon.com/apache2.0/ 
 *      
 * or in the "license" file
 * accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package com.voicebase.gateways.awsconnect.forward;

import static com.voicebase.gateways.awsconnect.VoiceBaseAttributeExtractor.getBooleanParameter;
import static com.voicebase.gateways.awsconnect.VoiceBaseAttributeExtractor.getStringParameter;
import static com.voicebase.gateways.awsconnect.VoiceBaseAttributeExtractor.getStringParameterSet;
import static com.voicebase.gateways.awsconnect.VoiceBaseAttributeExtractor.getVoicebaseAttributeName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.voicebase.api.model.VbAudioRedactorConfiguration;
import com.voicebase.api.model.VbCallbackConfiguration;
import com.voicebase.api.model.VbChannelConfiguration;
import com.voicebase.api.model.VbClassifierConfiguration;
import com.voicebase.api.model.VbConfiguration;
import com.voicebase.api.model.VbConfiguration.VbConfigurationBuilder;
import com.voicebase.api.model.VbContentFilteringConfiguration;
import com.voicebase.api.model.VbDetectorConfiguration;
import com.voicebase.api.model.VbFormattingConfiguration;
import com.voicebase.api.model.VbHttpMethodEnum;
import com.voicebase.api.model.VbIncludeTypeEnum;
import com.voicebase.api.model.VbIngestConfiguration;
import com.voicebase.api.model.VbIngestConfiguration.VbIngestConfigurationBuilder;
import com.voicebase.api.model.VbKnowledgeConfiguration;
import com.voicebase.api.model.VbKnowledgeConfiguration.VbKnowledgeConfigurationBuilder;
import com.voicebase.api.model.VbMetadata;
import com.voicebase.api.model.VbParameter;
import com.voicebase.api.model.VbPredictionConfiguration;
import com.voicebase.api.model.VbPredictionConfiguration.VbPredictionConfigurationBuilder;
import com.voicebase.api.model.VbPriorityEnum;
import com.voicebase.api.model.VbPublishConfiguration;
import com.voicebase.api.model.VbPublishConfiguration.VbPublishConfigurationBuilder;
import com.voicebase.api.model.VbRedactorConfiguration;
import com.voicebase.api.model.VbSpeechModelConfiguration;
import com.voicebase.api.model.VbSpeechModelConfiguration.VbSpeechModelConfigurationBuilder;
import com.voicebase.api.model.VbSpottingConfiguration;
import com.voicebase.api.model.VbSpottingGroupConfiguration;
import com.voicebase.api.model.VbTranscriptConfiguration;
import com.voicebase.api.model.VbTranscriptConfiguration.VbTranscriptConfigurationBuilder;
import com.voicebase.api.model.VbTranscriptRedactorConfiguration;
import com.voicebase.api.model.VbVocabularyConfiguration;
import com.voicebase.api.model.VbVocabularyConfiguration.VbVocabularyConfigurationBuilder;
import com.voicebase.api.model.VbVocabularyTermConfiguration;
import com.voicebase.gateways.awsconnect.VoiceBaseAttributeExtractor;
import com.voicebase.gateways.awsconnect.lambda.Lambda;
import com.voicebase.sdk.v3.MediaProcessingRequest;

/**
 * 
 * @author Volker Kueffel <volker@voicebase.com>
 *
 */
public class MediaProcessingRequestBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(MediaProcessingRequestBuilder.class);

  private static final String SPEECH_FEATURE_VOICE = "voiceFeatures";
  private static final String REDACTION_REPLACEMENT = "[redacted]";
  private static final Float REDACTION_GAIN = new Float(0.5f);
  private static final Integer REDACTION_TONE = new Integer(270);
  private static final String DETECTOR_NAME_PCI = "PCI";
  private static final String DETECTOR_PCI_PARAM_DETECTION_LEVEL_VALUE = "probableNumbers";
  private static final String DETECTOR_PCI_PARAM_DETECTION_LEVEL_NAME = "detectionLevel";

  private Map<String, Object> awsInputData;

  private boolean predictionsEnabled = true;
  private boolean knowledgeDiscoveryEnabled = false;
  private boolean configureSpeakers = true;

  private String leftSpeakerName;
  private String rightSpeakerName;

  private CallbackProvider callbackProvider;

  private String externalId;
  private VbConfiguration configuration;
  private VbMetadata metaData;
  private MediaProcessingRequest request;

  public void setAwsInputData(Map<String, Object> dataAsMap) {
    this.awsInputData = dataAsMap;
  }

  public MediaProcessingRequestBuilder withAwsInputData(Map<String, Object> dataAsMap) {
    setAwsInputData(dataAsMap);
    return this;
  }

  public void setPredictionsEnabled(boolean predictionsEnabled) {
    this.predictionsEnabled = predictionsEnabled;
  }

  public MediaProcessingRequestBuilder withPredictionsEnabled(boolean predictionsEnabled) {
    setPredictionsEnabled(predictionsEnabled);
    return this;
  }

  public boolean isKnowledgeDiscoveryEnabled() {
    return knowledgeDiscoveryEnabled;
  }

  public void setKnowledgeDiscoveryEnabled(boolean knowledgeEnabled) {
    this.knowledgeDiscoveryEnabled = knowledgeEnabled;
  }

  public MediaProcessingRequestBuilder withKnowledgeDiscoveryEnabled(boolean knowledgeEnabled) {
    setKnowledgeDiscoveryEnabled(knowledgeEnabled);
    return this;
  }

  public void setConfigureSpeakers(boolean configureSpeakers) {
    this.configureSpeakers = configureSpeakers;
  }

  public MediaProcessingRequestBuilder withConfigureSpeakers(boolean configureSpeakers) {
    setConfigureSpeakers(configureSpeakers);
    return this;
  }

  public void setLeftSpeakerName(String leftSpeakerName) {
    this.leftSpeakerName = leftSpeakerName;
  }

  public MediaProcessingRequestBuilder withLeftSpeakerName(String leftSpeakerName) {
    setLeftSpeakerName(leftSpeakerName);
    return this;
  }

  public void setRightSpeakerName(String rightSpeakerName) {
    this.rightSpeakerName = rightSpeakerName;
  }

  public void setCallbackProvider(CallbackProvider callbackProvider) {
    this.callbackProvider = callbackProvider;
  }

  public MediaProcessingRequestBuilder withCallbackProvider(CallbackProvider callbackProvider) {
    setCallbackProvider(callbackProvider);
    return this;
  }

  public MediaProcessingRequestBuilder withRightSpeakerName(String rightSpeakerName) {
    setRightSpeakerName(rightSpeakerName);
    return this;
  }

  public String getExternalId() {
    return externalId;
  }

  public VbConfiguration getConfiguration() {
    return configuration;
  }

  public VbMetadata getMetaData() {
    return metaData;
  }

  public MediaProcessingRequest getRequest() {
    return request;
  }

  public MediaProcessingRequest build() {
    externalId = awsInputData.get(Lambda.KEY_EXTERNAL_ID).toString();
    configuration = createConfiguration();
    metaData = createMetaData();

    request = new MediaProcessingRequest();
    request.setConfiguration(configuration);
    request.setMetadata(metaData);

    return request;
  }

  private VbMetadata createMetaData() {
    return VbMetadata.builder().externalId(externalId).extended(awsInputData).build();

  }

  /**
   * Create VB configuration out of Lily message.
   * <p/>
   * NOTE: As a side effect some of the attributes in the map are rewritten with
   * expanded lists.
   * 
   * 
   * @return VB configuration
   */
  private VbConfiguration createConfiguration() {

    VbConfigurationBuilder configBuilder = VbConfiguration.builder();
    VbPublishConfigurationBuilder publishConfigBuilder = VbPublishConfiguration.builder();
    VbTranscriptConfigurationBuilder transcriptConfigBuilder = VbTranscriptConfiguration.builder();
    VbIngestConfigurationBuilder ingestConfigBuilder = VbIngestConfiguration.builder();
    VbSpeechModelConfigurationBuilder speechConfigBuilder = VbSpeechModelConfiguration.builder();
    VbPredictionConfigurationBuilder predictionConfigBuilder = VbPredictionConfiguration.builder();
    VbKnowledgeConfigurationBuilder knowledgeConfigBuilder = VbKnowledgeConfiguration.builder()
        .enableDiscovery(Boolean.valueOf(knowledgeDiscoveryEnabled));

    // callbacks
    List<VbIncludeTypeEnum> includes = new ArrayList<>();
    if (callbackProvider.hasIncludes()) {
      for (String include : callbackProvider.getIncludes()) {
        includes.add(VbIncludeTypeEnum.fromValue(include.toUpperCase()));
      }
    }

    List<VbCallbackConfiguration> callbacks = new ArrayList<>();
    callbacks.add(VbCallbackConfiguration.builder().url(callbackProvider.getCallbackUrl())
        .method(VbHttpMethodEnum.valueOf(callbackProvider.getCallbackMethod())).include(includes).build());
    if (callbackProvider.hasAdditionalCallbackUrls()) {
      for (String callback : callbackProvider.getAdditionalCallbackUrls()) {
        callbacks.add(VbCallbackConfiguration.builder().url(callback)
            .method(VbHttpMethodEnum.valueOf(callbackProvider.getCallbackMethod())).include(includes).build());
      }
    }
    publishConfigBuilder.callbacks(callbacks);

    @SuppressWarnings("unchecked")
    Map<String, Object> attributes = (Map<String, Object>) awsInputData.get(Lambda.KEY_ATTRIBUTES);
    if (attributes != null && !attributes.isEmpty()) {
      VoiceBaseAttributeExtractor mc = new VoiceBaseAttributeExtractor(attributes);
      mc.setThrowExceptionOnMissing(false);

      ImmutableConfiguration vbAttrs = mc.immutableSubset(Lambda.VB_ATTR);

      Boolean redactPCI = getBooleanParameter(vbAttrs, Lambda.VB_ATTR_PCIREDACT);
      if (redactPCI != null && redactPCI.booleanValue()) {
        VbDetectorConfiguration detectorConfig = VbDetectorConfiguration.builder().detectorName(DETECTOR_NAME_PCI)
            .parameters(
                Collections.singletonList(VbParameter.builder().parameter(DETECTOR_PCI_PARAM_DETECTION_LEVEL_NAME)
                    .value(DETECTOR_PCI_PARAM_DETECTION_LEVEL_VALUE).build()))
            .redactor(VbRedactorConfiguration.builder()
                .transcript(VbTranscriptRedactorConfiguration.builder().replacement(REDACTION_REPLACEMENT).build())
                .audio(VbAudioRedactorConfiguration.builder().tone(REDACTION_TONE).gain(REDACTION_GAIN).build())
                .build())
            .build();
        predictionConfigBuilder.detectors(Collections.singletonList(detectorConfig));

      }

      speechConfigBuilder.language(getStringParameter(vbAttrs, Lambda.VB_ATTR_LANGUAGE));

      String priorityString = getStringParameter(vbAttrs, Lambda.VB_ATTR_PRIORIY);
      try {
        VbPriorityEnum p = VbPriorityEnum.fromValue(priorityString);
        if (p == null || p == VbPriorityEnum.NOT_SUPPORTED) {
          p = VbPriorityEnum.NORMAL;
        }
        configBuilder.priority(p);
      } catch (Exception e) {
        LOGGER.error("Unknown priority '{}' for ext ID {}", vbAttrs.getString(Lambda.VB_ATTR_PRIORIY),
            awsInputData.get(Lambda.KEY_EXTERNAL_ID));
        configBuilder.priority(VbPriorityEnum.NORMAL);
      }

      ImmutableConfiguration transcriptAttr = vbAttrs.immutableSubset(Lambda.VB_ATTR_TRANSCRIPT);

      transcriptConfigBuilder.formatting(VbFormattingConfiguration.builder()
          .enableNumberFormatting(getBooleanParameter(transcriptAttr, Lambda.VB_ATTR_TRANSCRIPT_NUMBER_FORMAT))
          .build());

      transcriptConfigBuilder.contentFiltering(VbContentFilteringConfiguration.builder()
          .enableProfanityFiltering(getBooleanParameter(transcriptAttr, Lambda.VB_ATTR_TRANSCRIPT_SWEARWORD_FILTER))
          .build());

      // knowledge discovery
      ImmutableConfiguration knowledgeAttr = vbAttrs.immutableSubset(Lambda.VB_ATTR_KNOWLEDGE);
      Boolean knowledgeDiscoveryEnabledAttr = getBooleanParameter(knowledgeAttr, Lambda.VB_ATTR_KNOWLEDGE_DISCOVERY);
      if (knowledgeDiscoveryEnabledAttr != null) {
        knowledgeConfigBuilder.enableDiscovery(Boolean.valueOf(knowledgeDiscoveryEnabledAttr));
      }
      

      // phrase spotting
      ImmutableConfiguration keywordAttr = vbAttrs.immutableSubset(Lambda.VB_ATTR_KEYWORDS);

      Set<String> groups = getStringParameterSet(keywordAttr, Lambda.VB_ATTR_KEYWORDS_GROUPS);
      if (groups != null && !groups.isEmpty()) {

        List<VbSpottingGroupConfiguration> spottingGroups = new ArrayList<>();
        for (String groupName : groups) {
          spottingGroups.add(VbSpottingGroupConfiguration.builder().groupName(groupName).build());
        }

        configBuilder.spotting(VbSpottingConfiguration.builder().groups(spottingGroups).build());

        // overwrite metadata
        attributes.put(getVoicebaseAttributeName(Lambda.VB_ATTR_KEYWORDS, Lambda.VB_ATTR_KEYWORDS_GROUPS),
            groups);
      }

      // classifiers
      if (predictionsEnabled) {

        ImmutableConfiguration classificationAttr = vbAttrs.immutableSubset(Lambda.VB_ATTR_CLASSIFIER);
        Set<String> classifierNames = getStringParameterSet(classificationAttr, Lambda.VB_ATTR_CLASSIFIER_NAMES);
        if (classifierNames != null && !classifierNames.isEmpty()) {
          List<VbClassifierConfiguration> classifierConfigs = new ArrayList<>();

          for (String classifier : classifierNames) {
            classifierConfigs.add(VbClassifierConfiguration.builder().classifierName(classifier).build());
          }

          speechConfigBuilder.features(Collections.singletonList(SPEECH_FEATURE_VOICE));
          predictionConfigBuilder.classifiers(classifierConfigs);

          attributes.put(getVoicebaseAttributeName(Lambda.VB_ATTR_CLASSIFIER, Lambda.VB_ATTR_CLASSIFIER_NAMES),
              classifierNames);
        }
      }

      // custom vocabularies
      List<VbVocabularyConfiguration> vocabs = new ArrayList<>();

      ImmutableConfiguration vocabAttr = vbAttrs.immutableSubset(Lambda.VB_ATTR_VOCABULARY);
      // vocab terms need to be unique
      Set<String> terms = getStringParameterSet(vocabAttr, Lambda.VB_ATTR_VOCABULARY_TERMS);
      if (terms != null && !terms.isEmpty()) {
        VbVocabularyConfigurationBuilder vocabularyConfigBuilder = VbVocabularyConfiguration.builder();
        ArrayList<VbVocabularyTermConfiguration> vocabTermConfigs = new ArrayList<>();
        for (String term : terms) {
          vocabTermConfigs.add(VbVocabularyTermConfiguration.builder().term(term).build());
        }
        vocabularyConfigBuilder.terms(vocabTermConfigs);
        vocabs.add(vocabularyConfigBuilder.build());

        // overwrite metadata
        attributes.put(getVoicebaseAttributeName(Lambda.VB_ATTR_VOCABULARY, Lambda.VB_ATTR_VOCABULARY_TERMS),
            terms);
      }

      Set<String> vocabNames = getStringParameterSet(vocabAttr, Lambda.VB_ATTR_VOCABULARY_NAMES);
      if (vocabNames != null && !vocabNames.isEmpty()) {

        for (String vocab : vocabNames) {
          vocabs.add(VbVocabularyConfiguration.builder().vocabularyName(vocab).build());
        }
        // overwrite metadata
        attributes.put(getVoicebaseAttributeName(Lambda.VB_ATTR_VOCABULARY, Lambda.VB_ATTR_VOCABULARY_NAMES),
            vocabNames);
      }

      if (!vocabs.isEmpty()) {
        configBuilder.vocabularies(vocabs);
      }

      // speakers
      if (configureSpeakers) {
        VbChannelConfiguration leftChannelConfig = VbChannelConfiguration.builder().speakerName(leftSpeakerName)
            .build();
        VbChannelConfiguration rightChannelConfig = VbChannelConfiguration.builder().speakerName(rightSpeakerName)
            .build();

        ingestConfigBuilder.channels(Lists.newArrayList(leftChannelConfig, rightChannelConfig));
      }

    }

    configBuilder.ingest(ingestConfigBuilder.build()).publish(publishConfigBuilder.build())
        .transcript(transcriptConfigBuilder.build()).speechModel(speechConfigBuilder.build())
        .prediction(predictionConfigBuilder.build()).knowledge(knowledgeConfigBuilder.build());

    return configBuilder.build();
  }

}
