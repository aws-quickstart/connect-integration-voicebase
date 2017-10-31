package com.voicebase.sdk.processing;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_EMPTY)
public class Configuration {
	@JsonProperty("executor")
	private Executor executor = Executor.V2;
	@JsonProperty("ingest")
	private IngestConfiguration ingestConfiguration;
	@JsonProperty("topics")
	private KnowledgeConfiguration topicConfiguration;
	@JsonProperty("keywords")
	private KeywordConfiguration keywordConfiguration;
	@JsonProperty("transcripts")
	private TranscriptConfiguration transcriptConfiguration;
	@JsonProperty("publish")
	private PublishConfiguration publishConfiguration;
	@JsonProperty("detections")
	private Set<DetectionConfiguration> detectionConfigurations;
	@JsonProperty("predictions")
	private Set<PredictionConfiguration> predictionConfigurations;
	
	@JsonProperty("language")
	private String language;

	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public Configuration withExecutor(Executor executor) {
		setExecutor(executor);
		return this;
	}

	public IngestConfiguration getIngestConfiguration() {
		return ingestConfiguration;
	}

	public void setIngestConfiguration(IngestConfiguration ingest) {
		this.ingestConfiguration = ingest;
	}

	public Configuration withIngestConfiguration(IngestConfiguration ingest) {
		setIngestConfiguration(ingest);
		return this;
	}

	public Configuration withTopicConfiguration(KnowledgeConfiguration topicConfiguration) {
		setTopicConfiguration(topicConfiguration);
		return this;
	}

	public KnowledgeConfiguration getTopicConfiguration() {
		return topicConfiguration;
	}

	public void setTopicConfiguration(KnowledgeConfiguration topicConfiguration) {
		this.topicConfiguration = topicConfiguration;
	}

	public Configuration withKeywordConfiguration(KeywordConfiguration keywordConfiguration) {
		setKeywordConfiguration(keywordConfiguration);
		return this;
	}

	public KeywordConfiguration getKeywordConfiguration() {
		return keywordConfiguration;
	}

	public void setKeywordConfiguration(KeywordConfiguration keywordConfiguration) {
		this.keywordConfiguration = keywordConfiguration;
	}

	public Configuration withTranscriptConfiguration(TranscriptConfiguration transcriptConfiguration) {
		setTranscriptConfiguration(transcriptConfiguration);
		return this;
	}

	public TranscriptConfiguration getTranscriptConfiguration() {
		return transcriptConfiguration;
	}

	public void setTranscriptConfiguration(TranscriptConfiguration transcriptConfiguration) {
		this.transcriptConfiguration = transcriptConfiguration;
	}

	public PublishConfiguration getPublishConfiguration() {
		return publishConfiguration;
	}

	public void setPublishConfiguration(PublishConfiguration publish) {
		this.publishConfiguration = publish;
	}

	public Configuration withPublishConfiguration(PublishConfiguration publish) {
		setPublishConfiguration(publish);
		return this;
	}

	public Set<DetectionConfiguration> getDetectionConfigurations() {
		return detectionConfigurations;
	}

	public void setDetectionConfigurations(Set<DetectionConfiguration> detections) {
		this.detectionConfigurations = detections;
	}

	public Configuration withDetectionConfigurations(Set<DetectionConfiguration> detections) {
		setDetectionConfigurations(detections);
		return this;
	}
	
	public Set<PredictionConfiguration> getPredictionConfigurations() {
		return predictionConfigurations;
	}

	public void setPredictionConfigurations(Set<PredictionConfiguration> predictionConfiguration) {
		this.predictionConfigurations = predictionConfiguration;
	}
	
	public Configuration withPredictionConfigurations(Set<PredictionConfiguration> predictionConfiguration) {
		setPredictionConfigurations(predictionConfiguration);
		return this;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Configuration withLanguage(String language) {
		setLanguage(language);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((detectionConfigurations == null) ? 0 : detectionConfigurations.hashCode());
		result = prime * result + ((executor == null) ? 0 : executor.hashCode());
		result = prime * result + ((ingestConfiguration == null) ? 0 : ingestConfiguration.hashCode());
		result = prime * result + ((keywordConfiguration == null) ? 0 : keywordConfiguration.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((publishConfiguration == null) ? 0 : publishConfiguration.hashCode());
		result = prime * result + ((topicConfiguration == null) ? 0 : topicConfiguration.hashCode());
		result = prime * result + ((transcriptConfiguration == null) ? 0 : transcriptConfiguration.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Configuration other = (Configuration) obj;
		if (detectionConfigurations == null) {
			if (other.detectionConfigurations != null)
				return false;
		} else if (!detectionConfigurations.equals(other.detectionConfigurations))
			return false;
		if (executor != other.executor)
			return false;
		if (ingestConfiguration == null) {
			if (other.ingestConfiguration != null)
				return false;
		} else if (!ingestConfiguration.equals(other.ingestConfiguration))
			return false;
		if (keywordConfiguration == null) {
			if (other.keywordConfiguration != null)
				return false;
		} else if (!keywordConfiguration.equals(other.keywordConfiguration))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (publishConfiguration == null) {
			if (other.publishConfiguration != null)
				return false;
		} else if (!publishConfiguration.equals(other.publishConfiguration))
			return false;
		if (topicConfiguration == null) {
			if (other.topicConfiguration != null)
				return false;
		} else if (!topicConfiguration.equals(other.topicConfiguration))
			return false;
		if (transcriptConfiguration == null) {
			if (other.transcriptConfiguration != null)
				return false;
		} else if (!transcriptConfiguration.equals(other.transcriptConfiguration))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Configuration [executor=");
		builder.append(executor);
		builder.append(", ingestConfiguration=");
		builder.append(ingestConfiguration);
		builder.append(", topicConfiguration=");
		builder.append(topicConfiguration);
		builder.append(", keywordConfiguration=");
		builder.append(keywordConfiguration);
		builder.append(", transcriptConfiguration=");
		builder.append(transcriptConfiguration);
		builder.append(", publishConfiguration=");
		builder.append(publishConfiguration);
		builder.append(", detections=");
		builder.append(detectionConfigurations);
		builder.append(", language=");
		builder.append(language);
		builder.append("]");
		return builder.toString();
	}

}
