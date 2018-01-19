package org.example.rest;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WebhookBody {

    private String id;
    private Object result;
    private String action;

    @XmlElement
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @XmlElement
    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @XmlElement
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "WebhookBody{" +
                "id='" + id + '\'' +
                ", result=" + result +
                ", action='" + action + '\'' +
                '}';
    }
}
