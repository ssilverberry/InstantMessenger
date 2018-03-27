package com.group42.client.network.protocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.group42.client.controllers.StringCrypter;

public class ProtocolClient {

    private StringCrypter crypter = new StringCrypter(new byte[]{1,4,5,6,8,9,7,8});
    private Gson gson;

    public ProtocolClient() {
        gson  = new Gson();
    }

    public String transform (Object outputClientMessage) {
        Gson out = new GsonBuilder().create();
        return crypter.encrypt(out.toJson(outputClientMessage));
    }

    public IncomingServerMessage transformOut(String response) {
        return gson.fromJson(response, IncomingServerMessage.class);
    }

}