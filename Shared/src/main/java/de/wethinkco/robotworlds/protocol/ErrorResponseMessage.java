package de.wethinkco.robotworlds.protocol;

import java.util.Map;

public class ErrorResponseMessage extends ResponseMessage{

    public ErrorResponseMessage(){}
    public ErrorResponseMessage(String result, Map<String, Object> data) {
        super(result, data);
    }
}
