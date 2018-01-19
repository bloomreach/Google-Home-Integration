package org.example.rest;


import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WebhookBody {

    private String session;
    private String responseId;


/*

    {"responseId":"e5448ebe-79b4-4c9a-b232-cd927ac85c06",
            "queryResult":{
        "queryText":"create a new document",
                "action":"input.title",
                "parameters":{"title":"create a new document"},
        "allRequiredParamsPresent":true,
                "fulfillmentText":"Great! A new document with title create a new document was created!",
                "fulfillmentMessages":[{"text":{"text":["Awesome! I just created a new document with title create a new document"]}}],
        "intent":{"name":"projects/mytest-f0cb1/agent/intents/5da9dd14-88f8-44c3-8f4b-938c2e8437be","displayName":"Create a new document"},"intentDetectionConfidence":1,"diagnosticInfo":{},"languageCode":"en"},"originalDetectIntentRequest":{"payload":{}},"session":"projects/mytest-f0cb1/agent/sessions/d4d1af62-0cbc-4aa1-8ebb-ccc73463261a"}
*/



    @Override
    public String toString() {
        return "WebhookBody{" +
                "session='" + session + '\'' +
                ", responseId='" + responseId + '\'' +
                '}';
    }


    @XmlElement
    public String getSession() {
        return session;
    }

    @XmlElement
    public String getResponseId() {
        return responseId;
    }


}
