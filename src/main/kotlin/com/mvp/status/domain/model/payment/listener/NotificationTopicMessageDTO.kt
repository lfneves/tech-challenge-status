package com.mvp.status.domain.model.payment.listener


import com.fasterxml.jackson.annotation.JsonProperty

data class NotificationTopicMessageDTO(
    @JsonProperty("Message")
    var message: String,
    @JsonProperty("MessageId")
    var messageId: String,
    @JsonProperty("Signature")
    var signature: String,
    @JsonProperty("SignatureVersion")
    var signatureVersion: String,
    @JsonProperty("SigningCertURL")
    var signingCertURL: String,
    @JsonProperty("Timestamp")
    var timestamp: String,
    @JsonProperty("TopicArn")
    var topicArn: String,
    @JsonProperty("Type")
    var type: String,
    @JsonProperty("UnsubscribeURL")
    var unsubscribeURL: String
)