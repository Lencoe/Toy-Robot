package de.wethinkco.robotworlds.protocol;

import de.wethinkco.robotworlds.protocol.robots.Robot;
import de.wethinkco.robotworlds.protocol.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShutdownQuitOffRequestMessage extends RequestMessage{

    public ShutdownQuitOffRequestMessage(){

    }

    public ShutdownQuitOffRequestMessage(String robot) {
        super(robot, "quit", new ArrayList<>());
    }

    @Override
    public ResponseMessage execute(Robot target, World world) {
        world.removeRobot(target);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("message", this.getCommand());
        return new SuccessResponseMessage("ok", resultMap);
    }

}
