package org.example.rest;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WebhookBody {

    private String id;
    private Object result;
    private String action;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
