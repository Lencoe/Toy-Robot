package de.wethinkco.robotworlds.protocol;

import de.wethinkco.robotworlds.protocol.robots.Robot;
import de.wethinkco.robotworlds.protocol.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReloadRequestMessage extends RequestMessage{
    public ReloadRequestMessage(){}
    public ReloadRequestMessage(String robot, List<Object> arguments) {
        super(robot, "reload", arguments);
    }

    public ReloadRequestMessage(String robot) {
        super(robot, "reload", new ArrayList<>());
    }

    @Override
    public ResponseMessage execute(Robot target, World world) {
        target.setStatus(RobotStatus.RELOAD);
        target.setShots(target.getMAX_SHOTS());
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("message", "Reloaded");
        return new SuccessResponseMessage("OK", mapData, target);
    }
}
