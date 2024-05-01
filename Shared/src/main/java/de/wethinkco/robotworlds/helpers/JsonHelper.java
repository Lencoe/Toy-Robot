package de.wethinkco.robotworlds.helpers;

import de.wethinkco.robotworlds.protocol.RequestMessage;
import de.wethinkco.robotworlds.protocol.ResponseMessage;
import de.wethinkco.robotworlds.protocol.world.ClientConfig;
import de.wethinkco.robotworlds.protocol.world.ServerConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;


public class JsonHelper {
    private final ObjectMapper mapper = new ObjectMapper();

    public String RequestMessageToJson(RequestMessage message){
        try {
            return mapper.writeValueAsString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //https://mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/
    public RequestMessage JsonToRequestMessage(String json){
        RequestMessage message = null;
        try {
            message = mapper.readValue(json, RequestMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    public String ResponseMessageToJson(ResponseMessage message){
        try {
            return mapper.writeValueAsString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //https://mkyong.com/java/how-to-convert-java-object-to-from-json-jackson/
    public ResponseMessage JsonToResponseMessage(String json){
        ResponseMessage message = null;
        try {
            message = mapper.readValue(json, ResponseMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    public String ServerConfigToJson(ServerConfig serverConfig) {
        ClientConfig clientConfig = new ClientConfig();

        try {
            return mapper.writeValueAsString(clientConfig);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public ServerConfig JsonToServerConfig(String json){
        ServerConfig serverConfig = null;
        ClientConfig clientConfig = null;
        try {
            clientConfig = mapper.readValue(json, ClientConfig.class);
            serverConfig = new ServerConfig(clientConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverConfig;
    }

}
