package de.wethinkco.robotworlds.protocol;

import de.wethinkco.robotworlds.protocol.robots.Robot;
import de.wethinkco.robotworlds.protocol.world.*;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireRequestMessage extends RequestMessage{
    public FireRequestMessage(){}

    public FireRequestMessage(String robot, List<Object> arguments) {
        super(robot, "fire", arguments);
    }

    public FireRequestMessage(String robot) {
        super(robot, "fire", new ArrayList<>());
    }

    @Override
    public ResponseMessage execute(Robot target, World world) {

        Map<String, Object> mapData = new HashMap<>();
        int distanceToObject = 0;

        if (target.getShots() > 0) {

            target.minusShots();
            System.out.println(target.getShots());

            List<Integer> intPosition = target.getPosition();
            Position robotPosition = new Position(intPosition.get(0), intPosition.get(1));
            List<IWorldObject> obstacles = world.obstaclesInAllDirection(robotPosition, target);

            obstacles = hitObstaclesInPath(target, robotPosition, obstacles);

            if (obstacles.size() > 0) {

                IWorldObject hitObject = getClosestObject(target,
                        obstacles);
                distanceToObject = getDistanceToObject(target, hitObject);

                if (distanceToObject < 0){
                    distanceToObject *= -1;
                }

                mapData.put("message", "Hit");
                mapData.put("distance", distanceToObject);

                if (hitObject.getTypeOfObject() == TypeOfObject.ROBOT) {
                    ((Robot) hitObject).applyDamage(target.getDamagePerShot());
                    mapData.put("robot", ((Robot) hitObject).getRobotName());
                    mapData.put("state", hitObject);
                } else {
                    mapData.put("obstacle", true);
                }

                return new SuccessResponseMessage("OK", mapData, target);

            } else {
                mapData.put("message", "Miss");
                return new SuccessResponseMessage("OK", mapData, target);
            }
        }
        mapData.put("message", "No ammo");
        return new ErrorResponseMessage("Error", mapData);
    }

    public List<IWorldObject> hitObstaclesInPath(Robot target, Position robotPosition, List<IWorldObject> obstacles){

        List<IWorldObject> obstaclesInFire = new ArrayList<>();

        Position rangePosition = getMaxRangePosition(target.getDirection(), robotPosition, target.getDamagePerShot());

        for (IWorldObject obstacle: obstacles) {
            if (obstacle instanceof Robot){
                if (((Robot) obstacle) != target) {
                    if (((Robot) obstacle).isRobotInPath(robotPosition, rangePosition)) {
                        obstaclesInFire.add(obstacle);
                    }
                }
            }
            if (obstacle instanceof IObstacle) {
                if (((IObstacle) obstacle).isPathBlocked(robotPosition, rangePosition)) {
                    obstaclesInFire.add(obstacle);
                }
            }
        }

        return obstaclesInFire;
    }

    @JsonIgnore
    public IWorldObject getClosestObject(Robot target, List<IWorldObject> obstaclesInFire) {
        //Find out how to get the closest object to robot

        Position robotPosition = target.getRobotPosition();
        Position movingShot = new Position(robotPosition.getX(), robotPosition.getY());

        for (int i = 1; i <= target.getDamagePerShot() ; i++) {
            for (IWorldObject object : obstaclesInFire) {
                switch (target.getDirection()) {
                    case "NORTH":
                        movingShot = new Position(robotPosition.getX(), robotPosition.getY() + i);
                        break;
                    case "SOUTH":
                        movingShot = new Position(robotPosition.getX(), robotPosition.getY() - i);
                        break;
                    case "EAST":
                        movingShot = new Position(robotPosition.getX() + i, robotPosition.getY());
                        break;
                    case "WEST":
                        movingShot = new Position(robotPosition.getX() - i, robotPosition.getY());
                        break;
                }

                if (object instanceof SquareObstacle) {
                    if (((SquareObstacle) object).isPathBlocked(robotPosition, movingShot)) {
                        return object;
                    }
                }

                if (object instanceof Robot && object != target) {
                    if (((Robot) object).isRobotInPath(robotPosition, movingShot)) {
                        return object;
                    }
                }
            }
        }


        return null;
    }

    @JsonIgnore
    public int getDistanceToObject(Robot target, IWorldObject object) {

        int objectY = 0;
        int objectX = 0;
        int robotY = target.getRobotPosition().getY();
        int robotX = target.getRobotPosition().getX();

        if (robotX <= 0) {
            robotX *= -1;
        }

        if (robotY <= 0){
            robotY *= -1;
        }


        switch (target.getDirection()) {
            case "NORTH":
                //Oy - Ry
                if (object.getTypeOfObject() == TypeOfObject.ROBOT) {
                    objectY = ((Robot) object).getRobotPosition().getY();
                } else if (object.getTypeOfObject() == TypeOfObject.OBSTACLE) {
                    //Bottom of obstacle
                    objectY = ((SquareObstacle) object).getBottomLeftY();
                }

                if (objectY < 0){
                    objectY *= -1;
                }
                return objectY - robotY;
            case "SOUTH":
                //Ry - Oy
                if (object.getTypeOfObject() == TypeOfObject.ROBOT) {
                    objectY = ((Robot) object).getRobotPosition().getY();
                } else if (object.getTypeOfObject() == TypeOfObject.OBSTACLE) {
                    //Top of obstacle
                    objectY = ((SquareObstacle) object).getTopLeftY();
                }
                if (objectY < 0){
                    objectY *= -1;
                }
                return robotY - objectY;
            case "EAST":
                //Ox - Rx
                if (object.getTypeOfObject() == TypeOfObject.ROBOT) {
                    objectX = ((Robot) object).getRobotPosition().getX();
                } else if (object.getTypeOfObject() == TypeOfObject.OBSTACLE) {
                    //Left side of obstacle
                    objectX = ((SquareObstacle) object).getBottomLeftX();
                }
                if (objectX < 0){
                    objectX *= -1;
                }
                return objectX - robotX;
            case "WEST":
                //Rx - Ox
                if (object.getTypeOfObject() == TypeOfObject.ROBOT) {
                    objectX = ((Robot) object).getRobotPosition().getX();
                } else if (object.getTypeOfObject() == TypeOfObject.OBSTACLE) {
                    //Right side of obstacle
                    objectX = ((SquareObstacle) object).getBottomRightX();
                }
                if (objectX < 0){
                    objectX *= -1;
                }
                return robotX - objectX;
        }

        return 0;
    }

    @JsonIgnore
    public Position getMaxRangePosition(String direction, Position robotPosition, int fireRange){

        Position rangePosition = new Position(0,0);

        switch (direction) {
            case "NORTH":
                rangePosition = new Position(robotPosition.getX(),
                        robotPosition.getY() + fireRange);
                break;
            case "SOUTH":
                rangePosition = new Position(robotPosition.getX(),
                        robotPosition.getY() - fireRange);
                break;
            case "EAST":
                rangePosition = new Position(robotPosition.getX() + fireRange,
                        robotPosition.getY());
                break;
            case "WEST":
                rangePosition = new Position(robotPosition.getX() - fireRange,
                        robotPosition.getY());
                break;
        }
        return rangePosition;
    }

}
