package de.wethinkco.robotworlds.protocol;

import de.wethinkco.robotworlds.protocol.robots.Robot;
import de.wethinkco.robotworlds.protocol.world.World;

import java.util.ArrayList;
import java.util.List;

public class RepairRequestMessage extends RequestMessage{
    public RepairRequestMessage(String robot, List<Object> arguments) {
        super(robot, "repair", arguments);
    }
    public RepairRequestMessage(String robot) {
        super(robot, "repair", new ArrayList<>());
    }
    @Override
    public ResponseMessage execute(Robot target, World world) {
//        target.setStatus(RobotStatus.REPAIR);

        return null;
    }
}
