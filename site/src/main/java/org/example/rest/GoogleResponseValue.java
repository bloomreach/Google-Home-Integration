package org.example.rest;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.protobuf.Struct;

@XmlRootElement(name = "WebhookRequest")
public class GoogleResponseValue {

    @XmlElement
    private String session;
    @XmlElement
    private String responseId;
    @XmlElement
    private QueryResult queryResult;
    @XmlElement
    private OriginalDetectIntentRequest originalDetectIntentRequest;


    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }


    public QueryResult getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(QueryResult queryResult) {
        this.queryResult = queryResult;
    }

    public OriginalDetectIntentRequest getOriginalDetectIntentRequest() {
        return originalDetectIntentRequest;
    }

    public void setOriginalDetectIntentRequest(OriginalDetectIntentRequest originalDetectIntentRequest) {
        this.originalDetectIntentRequest = originalDetectIntentRequest;
    }
}

@XmlRootElement
class Parameters {
    @XmlElement
    private Struct format;


    public Struct getFormat() {
        return format;
    }

    public void setFormat(Struct format) {
        this.format = format;
    }
}

@XmlRootElement
class QueryResult {

    @XmlElement
    private String queryText;
    @XmlElement
    private String languageCode;
    @XmlElement
    private String action;
    @XmlElement
    private String fulfillmentText;
    @XmlElement
    private Message[] fulfillmentMessages;
    @XmlElement
    private Parameters parameters;

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFulfillmentText() {
        return fulfillmentText;
    }

    public void setFulfillmentText(String fulfillmentText) {
        this.fulfillmentText = fulfillmentText;
    }

    public Message[] getFulfillmentMessages() {
        return fulfillmentMessages;
    }

    public void setFulfillmentMessages(Message[] fulfillmentMessages) {
        this.fulfillmentMessages = fulfillmentMessages;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }
}

@XmlRootElement
class Message {

    @XmlElement
    private Text text;

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
}

@XmlRootElement
class Text {
    @XmlElement
    private String[] text;

    public String[] getText() {
        return text;
    }

    public void setText(String[] text) {
        this.text = text;
    }
}

@XmlRootElement
class OriginalDetectIntentRequest {
    @XmlElement
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}





