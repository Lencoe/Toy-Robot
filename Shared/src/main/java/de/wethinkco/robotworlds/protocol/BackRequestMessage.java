package de.wethinkco.robotworlds.protocol;

import de.wethinkco.robotworlds.protocol.robots.Robot;
import de.wethinkco.robotworlds.protocol.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackRequestMessage extends RequestMessage {
    public BackRequestMessage(){}

    public BackRequestMessage(String robot, List<Object> arguments) {
        super(robot, "back", arguments);
    }

    @Override
    public ResponseMessage execute(Robot target, World world)
    {
        List<Object> args = getArguments();
        Map<String, Object> mapData = new HashMap<>();

        if(getArguments().size()==1)
        {
            int nrSteps = 0;

            try {
                nrSteps = Integer.parseInt(args.get(0).toString());
            }catch (NumberFormatException e){
                mapData.put("message", "Invalid Steps");
                return new ErrorResponseMessage("ERROR", mapData);
            }

            NewPositionResponse newPositionResponse = null;
            try {
                newPositionResponse = target.updatePosition(-nrSteps, world);
            } catch (RuntimeException e) {
                mapData.put("message", "0 steps");
                return new ErrorResponseMessage("ERROR", mapData);
            }

            if(newPositionResponse == NewPositionResponse.NOT_BLOCKED)
            {
                mapData.put("message", "Moved back " + nrSteps + " steps.");
                return new SuccessResponseMessage("OK", mapData, target);
            } else if (newPositionResponse == NewPositionResponse.OBSTACLE_BLOCKED){
                mapData.put("message", "There is an obstacle in the way.");
                return new SuccessResponseMessage("Blocked", mapData, target);
            } else if (newPositionResponse == NewPositionResponse.ROBOT_BLOCKED) {
                mapData.put("message", "There is a robot in the way.");
                return new SuccessResponseMessage("Blocked", mapData, target);
            } else
            {
                mapData.put("message", "Sorry, I cannot go outside my safe zone.");
                return new SuccessResponseMessage("Blocked", mapData, target);
            }
        }
        else if (getArguments().size() > 1)
        {
            mapData.put("message", "Too many arguments provided.");
            return new ErrorResponseMessage("Error", mapData);
        } else {
            mapData.put("message", "Not enough arguments provided.");
            return new ErrorResponseMessage("Error", mapData);
        }
    }
}
