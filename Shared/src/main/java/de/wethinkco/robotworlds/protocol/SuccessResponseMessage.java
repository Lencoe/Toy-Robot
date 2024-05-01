package de.wethinkco.robotworlds.protocol;

import de.wethinkco.robotworlds.protocol.robots.Robot;

import java.util.HashMap;
import java.util.Map;

public class SuccessResponseMessage extends ResponseMessage {

    private Robot state;

    public SuccessResponseMessage() {
        super("ok", new HashMap<String, Object>() {});
    }
    public SuccessResponseMessage(String result, Map < String, Object > data) {
            super(result, data);
        }
    public SuccessResponseMessage(String result, Map < String, Object > data, Robot state) {
            super(result, data);
            this.state = state;
        }

    public Robot getState () {
        return state;
    }
    public void setState (Robot state){
        this.state = state;
    }

}

