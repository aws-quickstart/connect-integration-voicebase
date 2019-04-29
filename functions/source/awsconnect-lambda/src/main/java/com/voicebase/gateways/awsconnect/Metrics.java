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

/** @author Volker Kueffel <volker@voicebase.com> */
public class Metrics {

  public static enum Name {
    RecordsReceived("NumberOfRecordsReceived"),
    NonCtrReceived("NumberOfOtherRecordsReceived"),
    CtrReceived("NumberOfContactRecordsReceived"),
    CtrSkipped("NumberOfContactRecordsSkipped"),
    CtrDropped("NumberOfContactRecordsDropped"),
    CtrMissingAudio("NumberOfContactRecordsMissingAudio"),
    CtrNoAudio("NumberOfContactRecordsWithoutAudio"),
    Retries("NumberOfContactRecordsDelayed"),
    DelayTime("DelayTimeSeconds"),
    RetriesExceeded("NumberOfContactRecordsRetryExceeded"),
    VoicebaseRequestAttempted("NumberOfVoiceBaseRequestAttempts"),
    VoicebaseRequestFailed("NumberOfVoiceBaseRequestsFailed"),
    VoicebaseAccepted("NumberOfVoiceBaseRequestsAccepted"),
    VoicebaseRequestsUntilAccepted("NumberOfVoiceBaseRequestAttemptsUntilAccepted"),
    VoicebaseTurnAroundTime("VoicebaseTurnAroundTimeSeconds"),
    VoicebaseResultReceived("NumberOfVoiceBaseResultsReceived"),
    VoicebaseResultSendSuccess("NumberOfVoiceBaseResultsSent"),
    VoicebaseResultSendFailed("NumberOfVoiceBaseResultsSendFailed"),
    VoicebaseResultTrimmed("VoicebaseResultTrimmed"),
    RecordsDropped("NumberOfRecordsDropped");
    ;

    private final String name;

    private Name(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    @Override
    public String toString() {
      return getName();
    }
  }

  public static enum Dimension {
    InstanceId("ConnectInstanceId"),
    ProcessingStatus("ProcessingStatus");

    private final String name;

    private Dimension(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    @Override
    public String toString() {
      return getName();
    }
  }
}
