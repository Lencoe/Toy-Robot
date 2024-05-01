package de.wethinkco.robotworlds.protocol;

import de.wethinkco.robotworlds.protocol.robots.Robot;
import de.wethinkco.robotworlds.protocol.world.RobotVisionField;
import de.wethinkco.robotworlds.protocol.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaunchRequestMessage extends RequestMessage{

    public LaunchRequestMessage(){}
    public LaunchRequestMessage(String robotName, List<Object> arguments){
        super(robotName, "launch", arguments);
    }

    public LaunchRequestMessage(String robot) {
    }

    public Map<String, Object> mapNewRobot(Robot robot){
        Map<String, Object> mappedRobot = new HashMap<>();

        mappedRobot.put("position", robot.getPosition());
        mappedRobot.put("direction", robot.getDirection());
        mappedRobot.put("visibility", RobotVisionField.getVisibility());
        mappedRobot.put("shield", robot.getShields());
        mappedRobot.put("shots", robot.getMAX_SHOTS());
        mappedRobot.put("status", robot.getStatus());

        return mappedRobot;
    }

    @Override
    public ResponseMessage execute(Robot target, World world) {

        Map<String, Object> mapData = new HashMap<>();

        if (target == null){

            Robot newRobot = Robot.createRobot((String) this.getArguments().get(1),
                    (String) this.getArguments().get(0));

            if (newRobot == null){
                mapData.put("message", "Invalid make provided");
                return new ErrorResponseMessage("ERROR", mapData);
            }

            if (!(world.getRobotByName((String) getArguments().get(1)) == null)){
                mapData.put("message", "No more space in this world");

                // Error message stating that the user already has a robot launched.
                return new ErrorResponseMessage("ERROR", mapData);
            }

            newRobot.generateRandomPosition(world);

            world.addRobot(newRobot);
            mapData = mapNewRobot(newRobot);


            System.out.println("New robot launched: \n" + "  Name: " + newRobot.getRobotName() + "\n"
                    + "  " + newRobot.getRobotPosition()
                    + "\n  Direction: " + newRobot.getDirection() );

            //return success with "OK", mapData, state
            return new SuccessResponseMessage("OK", mapData, newRobot);

        }
        //TODO: Find out how to check if client has already launched a robot.
        // Also find out whether the launch robot name is the same as their current robot.
        mapData.put("message", "No more space in this world");

        // Error message stating that the user already has a robot launched.
        return new ErrorResponseMessage("ERROR", mapData);
    }

    public LaunchRequestMessage(String robot, String command, List<Object> arguments) {
        super(robot, command, arguments);
    }
}
