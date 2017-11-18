package com.voicebase.gateways.lily;

import static com.voicebase.gateways.lily.VoiceBaseAttributeExtractor.getBooleanParameter;
import static com.voicebase.gateways.lily.VoiceBaseAttributeExtractor.getStringParameter;
import static com.voicebase.gateways.lily.VoiceBaseAttributeExtractor.getStringParameterList;
import static com.voicebase.gateways.lily.VoiceBaseAttributeExtractor.getStringParameterSet;
import static com.voicebase.gateways.lily.VoiceBaseAttributeExtractor.getVoicebaseAttributeName;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.voicebase.sdk.Identifier;
import com.voicebase.sdk.processing.Channels;
import com.voicebase.sdk.processing.Configuration;
import com.voicebase.sdk.processing.DetectionConfiguration;
import com.voicebase.sdk.processing.DetectionModel;
import com.voicebase.sdk.processing.IngestConfiguration;
import com.voicebase.sdk.processing.KeywordConfiguration;
import com.voicebase.sdk.processing.MediaProcessingRequest;
import com.voicebase.sdk.processing.Metadata;
import com.voicebase.sdk.processing.PredictionConfiguration;
import com.voicebase.sdk.processing.Priority;
import com.voicebase.sdk.processing.PublishConfiguration;
import com.voicebase.sdk.processing.RedactionConfiguration;
import com.voicebase.sdk.processing.Speaker;
import com.voicebase.sdk.processing.TranscriptConfiguration;
import com.voicebase.sdk.processing.Vocabulary;

public class VoicebaseRequestBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(VoicebaseRequestBuilder.class);

  private Map<String, ?> awsInputData;

  private boolean predictionsEnabled = true;
  private boolean configureSpeakers = true;

  private String leftSpeakerName;
  private String rightSpeakerName;

  private CallbackProvider callbackProvider;

  private String externalId;
  private Configuration configuration;
  private Metadata metaData;
  private MediaProcessingRequest request;

  public void setAwsInputData(Map<String, ?> dataAsMap) {
    this.awsInputData = dataAsMap;
  }

  public VoicebaseRequestBuilder withAwsInputData(Map<String, ?> dataAsMap) {
    setAwsInputData(dataAsMap);
    return this;
  }

  public void setPredictionsEnabled(boolean predictionsEnabled) {
    this.predictionsEnabled = predictionsEnabled;
  }

  public VoicebaseRequestBuilder withPredictionsEnabled(boolean predictionsEnabled) {
    setPredictionsEnabled(predictionsEnabled);
    return this;
  }

  public void setConfigureSpeakers(boolean configureSpeakers) {
    this.configureSpeakers = configureSpeakers;
  }

  public VoicebaseRequestBuilder withConfigureSpeakers(boolean configureSpeakers) {
    setConfigureSpeakers(configureSpeakers);
    return this;
  }

  public void setLeftSpeakerName(String leftSpeakerName) {
    this.leftSpeakerName = leftSpeakerName;
  }

  public VoicebaseRequestBuilder withLeftSpeakerName(String leftSpeakerName) {
    setLeftSpeakerName(leftSpeakerName);
    return this;
  }

  public void setRightSpeakerName(String rightSpeakerName) {
    this.rightSpeakerName = rightSpeakerName;
  }

  public void setCallbackProvider(CallbackProvider callbackProvider) {
    this.callbackProvider = callbackProvider;
  }

  public VoicebaseRequestBuilder withCallbackProvider(CallbackProvider callbackProvider) {
    setCallbackProvider(callbackProvider);
    return this;
  }

  public VoicebaseRequestBuilder withRightSpeakerName(String rightSpeakerName) {
    setRightSpeakerName(rightSpeakerName);
    return this;
  }

  public String getExternalId() {
    return externalId;
  }

  public Configuration getConfiguration() {
    return configuration;
  }

  public Metadata getMetaData() {
    return metaData;
  }

  public MediaProcessingRequest getRequest() {
    return request;
  }

  public MediaProcessingRequest build() {
    externalId = awsInputData.get(Constants.KEY_EXTERNAL_ID).toString();
    configuration = createConfiguration();
    metaData = createMetaData();

    request = new MediaProcessingRequest();
    request.setConfiguration(configuration);
    request.setMetadata(metaData);

    return request;
  }

  private Metadata createMetaData() {
    Metadata metaData = new Metadata();
    Identifier extId = new Identifier(externalId);
    metaData.setExternal(extId);
    metaData.getExtended().putAll(awsInputData);
    return metaData;
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
  private Configuration createConfiguration() {

    Configuration conf = new Configuration()
        .withPublishConfiguration(new PublishConfiguration().addCallback(callbackProvider.newCallback())
            .addCallbacks(callbackProvider.getAdditionalCallbacks()))
        .withTranscriptConfiguration(new TranscriptConfiguration()).withIngestConfiguration(new IngestConfiguration());

    @SuppressWarnings("unchecked")
    Map<String, Object> attributes = (Map<String, Object>) awsInputData.get(Constants.KEY_ATTRIBUTES);
    if (attributes != null && !attributes.isEmpty()) {
      VoiceBaseAttributeExtractor mc = new VoiceBaseAttributeExtractor(attributes);
      mc.setThrowExceptionOnMissing(false);

      ImmutableConfiguration vbAttrs = mc.immutableSubset(Constants.VB_ATTR);

      Boolean redactPCI = getBooleanParameter(vbAttrs, Constants.VB_ATTR_PCIREDACT);
      if (redactPCI != null && redactPCI.booleanValue()) {
        DetectionConfiguration dc = new DetectionConfiguration().withModel(DetectionModel.PCI)
            .withRedactionConfiguration(new RedactionConfiguration().withTranscriptRedaction("[redacted]"));
        conf.setDetectionConfigurations(Sets.newHashSet(dc));
      }

      conf.setLanguage(getStringParameter(vbAttrs, Constants.VB_ATTR_LANGUAGE));

      String priorityString = getStringParameter(vbAttrs, Constants.VB_ATTR_PRIORIY);
      try {
        Priority p = Priority.fromValue(priorityString);
        if (p != null) {
          conf.getIngestConfiguration().setPriority(p);
        }
      } catch (Exception e) {
        LOGGER.error("Unknown priority '{}' for ext ID {}", vbAttrs.getString(Constants.VB_ATTR_PRIORIY),
            awsInputData.get(Constants.KEY_EXTERNAL_ID));
      }

      ImmutableConfiguration transcriptAttr = vbAttrs.immutableSubset(Constants.VB_ATTR_TRANSCRIPT);

      conf.getTranscriptConfiguration()
          .setFormatNumbers(getBooleanParameter(transcriptAttr, Constants.VB_ATTR_TRANSCRIPT_NUMBER_FORMAT));
      conf.getTranscriptConfiguration()
          .setFilterSwearWords(getBooleanParameter(transcriptAttr, Constants.VB_ATTR_TRANSCRIPT_SWEARWORD_FILTER));

      // phrase spotting
      ImmutableConfiguration keywordAttr = vbAttrs.immutableSubset(Constants.VB_ATTR_KEYWORDS);

      Set<String> groups = getStringParameterSet(keywordAttr, Constants.VB_ATTR_KEYWORDS_GROUPS);
      if (groups != null) {
        conf.setKeywordConfiguration(new KeywordConfiguration().withGroups(groups));
        // overwrite metadata
        attributes.put(getVoicebaseAttributeName(Constants.VB_ATTR_KEYWORDS, Constants.VB_ATTR_KEYWORDS_GROUPS),
            groups);
      }

      // classifiers
      if (predictionsEnabled) {
        ImmutableConfiguration classificationAttr = vbAttrs.immutableSubset(Constants.VB_ATTR_CLASSIFIER);
        Set<String> classifierNames = getStringParameterSet(classificationAttr, Constants.VB_ATTR_CLASSIFIER_NAMES);
        if (classifierNames != null) {
          Set<PredictionConfiguration> predictions = new HashSet<>();
          for (String classifier : classifierNames) {
            predictions.add(new PredictionConfiguration().withModelName(classifier));
          }
          conf.setPredictionConfigurations(predictions);
          conf.getTranscriptConfiguration().setVoiceFeatures(true);
          attributes.put(getVoicebaseAttributeName(Constants.VB_ATTR_CLASSIFIER, Constants.VB_ATTR_CLASSIFIER_NAMES),
              classifierNames);
        }
      }

      // custom vocabularies
      ImmutableConfiguration vocabAttr = vbAttrs.immutableSubset(Constants.VB_ATTR_VOCABULARY);
      List<Vocabulary> vocabularies = new ArrayList<>();
      List<String> terms = getStringParameterList(vocabAttr, Constants.VB_ATTR_VOCABULARY_TERMS);
      if (terms != null) {
        vocabularies.add(new Vocabulary().withTerms(terms));
        // overwrite metadata
        attributes.put(getVoicebaseAttributeName(Constants.VB_ATTR_VOCABULARY, Constants.VB_ATTR_VOCABULARY_TERMS),
            terms);
      }
      Set<String> vocabNames = getStringParameterSet(vocabAttr, Constants.VB_ATTR_VOCABULARY_NAMES);
      if (vocabNames != null) {
        for (String vocab : vocabNames) {
          vocabularies.add(new Vocabulary().withName(vocab));
        }
        // overwrite metadata
        attributes.put(getVoicebaseAttributeName(Constants.VB_ATTR_VOCABULARY, Constants.VB_ATTR_VOCABULARY_NAMES),
            vocabNames);
      }
      if (!vocabularies.isEmpty()) {
        conf.getTranscriptConfiguration().setVocabularies(vocabularies);
      }

      // speakers
      if (configureSpeakers) {
        conf.getIngestConfiguration().setChannels(new Channels().withLeft(new Speaker().withName(leftSpeakerName))
            .withRight(new Speaker().withName(rightSpeakerName)));
      }

    }

    return conf;
  }

}
