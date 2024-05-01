package de.wethinkco.robotworlds.protocol;

import de.wethinkco.robotworlds.protocol.robots.Robot;
import de.wethinkco.robotworlds.protocol.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TurnRequestMessage extends RequestMessage
{
    public TurnRequestMessage() {
    }

    public TurnRequestMessage(String robot, List<Object> arguments)
    {
        super(robot, "turn", arguments);
    }


    @Override
    public ResponseMessage execute(Robot target, World world)
    {
        List<Object> args = getArguments();
        String currentDirection = target.getDirection();
        Map<String, Object> mapData = new HashMap<>();

        if(getArguments().size()==1)
        {
            String directionSignal = args.get(0).toString();

            boolean validDirectionSignal = target.updateDirection(directionSignal);

            if (!validDirectionSignal)
            {
                mapData.put("message", "Invalid arguments provided.");
                return new ErrorResponseMessage("Error", mapData);
            }

            mapData.put("message", "Turned " + directionSignal);
            return new SuccessResponseMessage("OK", mapData, target);
        }
        else if (getArguments().size() > 1)
        {
            mapData.put("message", "Too many arguments provided.");
            return new ErrorResponseMessage("Error", mapData);
        }
        else
        {
            mapData.put("message", "Not enough arguments provided.");
            return new ErrorResponseMessage("Error", mapData);
        }
    }
}

