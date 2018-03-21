package sample.network.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ProtocolClient {

    private Gson gson;

    public ProtocolClient() {
        gson  = new Gson();
    }

    public String transform (Object outputClientMessage) {
        Gson out = new GsonBuilder().create();
        return out.toJson(outputClientMessage);
    }

    public IncomingServerMessage transformOut(String response) {
        return gson.fromJson(response, IncomingServerMessage.class);
    }

}