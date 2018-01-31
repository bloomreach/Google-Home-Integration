package org.example.rest;


import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.SerializedName;

@XmlRootElement
public class GoogleResponse {

    @SerializedName("fulfillmentText")
    private String fulfillmentText;

    @SerializedName("fulfillmentMessages")
    private List<String> fulfillmentMessages;



}
