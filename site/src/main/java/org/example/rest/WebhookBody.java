package org.example.rest;


import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WebhookBody {

    private String session;
    private String responseId;

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
