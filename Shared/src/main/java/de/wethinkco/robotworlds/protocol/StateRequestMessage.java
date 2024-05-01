package de.wethinkco.robotworlds.protocol;

import de.wethinkco.robotworlds.protocol.robots.Robot;

import de.wethinkco.robotworlds.protocol.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateRequestMessage extends RequestMessage{
    public StateRequestMessage(String robot, List<Object> arguments) {
        super(robot, "state", arguments);
    }
    public StateRequestMessage(String robot) {
        super(robot, "state", new ArrayList<>());
    }

    public StateRequestMessage() {
    }

    @Override
    public ResponseMessage execute(Robot target, World world) {
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("message", "state");
        return new SuccessResponseMessage("OK",mapData, target);
    }
}
