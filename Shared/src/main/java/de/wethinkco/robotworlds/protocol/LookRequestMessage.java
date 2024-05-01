package de.wethinkco.robotworlds.protocol;

import de.wethinkco.robotworlds.protocol.robots.Robot;
import de.wethinkco.robotworlds.protocol.world.IWorldObject;
import de.wethinkco.robotworlds.protocol.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * The look command is implemented from the Robot Client
 */

public class LookRequestMessage extends RequestMessage{
    public LookRequestMessage(){}

    public LookRequestMessage(String robot, List<Object> arguments) {
        super(robot, "look", arguments);
    }
    public LookRequestMessage(String robot) {
        super(robot, "look", new ArrayList<>());
    }
    @Override
    public ResponseMessage execute(Robot target, World world) {
        List<Integer> intPosition = target.getPosition();
        Position robotPosition = new Position(intPosition.get(0),intPosition.get(1));
        List<IWorldObject>obstacles = world.obstaclesInAllDirection(robotPosition, target);
        List<ObstacleResponse> responseData = new ArrayList<>();

        lookAtObstacles(target, robotPosition, obstacles, responseData);

        Map<String, Object> mapData = new HashMap<>();
        mapData.put("objects", responseData);
        return new SuccessResponseMessage("OK",mapData, target);
    }

    private void lookAtObstacles(Robot target, Position robotPosition, List<IWorldObject> obstacles, List<ObstacleResponse> responseData) {
        int differenceInSteps = 0;
        for (IWorldObject obstacle: obstacles) {
            switch (obstacle.directionOfObject()){
                case "NORTH":
                case "SOUTH": Position p = obstacle.getObstacles().stream().filter(x->x.getX() == robotPosition.getX()).findFirst().orElse(null);
                    if (p != null) {
                        differenceInSteps = Math.abs(p.getY() - robotPosition.getY());
                        responseData.add(new ObstacleResponse(obstacle.directionOfObject(),
                                obstacle.getTypeOfObject(), differenceInSteps));
                        break;
                    }
                case "EAST":
                case "WEST":  p = obstacle.getObstacles().stream().filter(x->x.getY() == robotPosition.getY()).findFirst().orElse(null);
                    if (p != null) {
                        differenceInSteps = Math.abs(p.getX() - robotPosition.getX());
                        responseData.add(new ObstacleResponse(obstacle.directionOfObject(),
                                obstacle.getTypeOfObject(), differenceInSteps));
                        break;
                    }
            }
        }
    }
}
