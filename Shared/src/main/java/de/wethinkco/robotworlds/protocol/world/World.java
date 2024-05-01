package de.wethinkco.robotworlds.protocol.world;

import de.wethinkco.robotworlds.protocol.NewPositionResponse;
import de.wethinkco.robotworlds.protocol.Position;
import de.wethinkco.robotworlds.protocol.robots.Robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    public int getConnectedRobotsSize() {
        return connectedRobots.size();
    }

    private final List<Robot> connectedRobots = new ArrayList<>();
    private int width = GridSize.getWidth();
    private int height = GridSize.getHeight();

    private final Position CENTRE = new Position(0, 0);
    private final int right = width;
    private final int bottom = -height;
    private final int top = height;
    private final int left = -height;

    private final int visibility = RobotVisionField.getVisibility();
//    private final Random random;

    private final List<IObstacle> obstacles;

    public World() {
        this.obstacles = getObstacles();
//        this.random = new Random();
    }

    public World(List<IObstacle> obstacles){
        this.obstacles = obstacles;
    }

    public  World(List<IObstacle> obstacles, int width, int height){
        this.obstacles = obstacles;
        this.width = width;
        this.height = height;
    }

    public void removeRobot (Robot robot) {
        this.connectedRobots.remove(robot);
    }

    public boolean isNewPositionAllowed(Position position) {
        return obstacles
                .stream()
                .anyMatch(obstacle-> obstacle.isPositionBlocked(position));
    }

    public List<IObstacle> getObstacles() {
        Random random = new Random();
        List<IObstacle> obstacleList = new ArrayList<>();
        int randomNum = random.nextInt(20) + 1;
        for (int index = 0; index < randomNum; index++) {
            int xCoordinate = random.nextInt(95 - (-width)) + (-width);
            int yCoordinate = random.nextInt(195 - (-height)) + (-height);

            obstacleList.add(new SquareObstacle(xCoordinate, yCoordinate));
//                System.out.println(obstacleList);
        }
        return obstacleList;
    }

    public int getVisibility(){
        return this.visibility;
    }

    public void addRobot(Robot newRobot) {

        connectedRobots.add(newRobot);
    }



    @Override
    public String toString() {
        return "World{"
                + "connectedRobots=" + connectedRobots
                + ", CENTRE=" + CENTRE
                + ", obstacles=" + obstacles + '}';
    }

    public List<Robot> getRobots() {
        return connectedRobots;
    }

    public Robot getRobotByName(String robot) {
        return connectedRobots
                .stream()
                .filter(robotToCheck -> robotToCheck.getRobotName().equalsIgnoreCase(robot))
                .findFirst()
                .orElse(null);
    }

    public List<IWorldObject> obstaclesInAllDirection(Position robotPosition, Robot target){
        List<PositionAndDirection> positions = new ArrayList<>();
        Position north = new Position(robotPosition.getX(),robotPosition.getY() + this.visibility);
        Position south = (new Position(robotPosition.getX(),robotPosition.getY() - this.visibility));
        Position east = (new Position(robotPosition.getX() + this.visibility, robotPosition.getY()));
        Position west = (new Position(robotPosition.getX() - this.visibility, robotPosition.getY()));

        positions.add(new PositionAndDirection(north, "NORTH"));
        positions.add(new PositionAndDirection(south, "SOUTH"));
        positions.add(new PositionAndDirection(east, "EAST"));
        positions.add(new PositionAndDirection(west, "WEST"));

        List<IWorldObject> list = new ArrayList<>();
        for(PositionAndDirection edgePosition : positions){

            for (IObstacle obstacle : obstacles) {
                if (obstacle.isPathBlocked(robotPosition, edgePosition.getPosition())) {
                    obstacle.setDirectionOfObject(edgePosition.getDirection());
                    list.add(obstacle);
                }
            }
            for (Robot robot : connectedRobots){
                if (robot.isRobotInPath(robotPosition, edgePosition.getPosition()) && robot != target) {
                    robot.setDirectionOfObject(edgePosition.getDirection());
                    list.add(robot);
                }
            }
            checkEdges(list, edgePosition);
        }
        return list;
    }

    public NewPositionResponse isPathBlocked(Position robotPosition, Position newPosition, Robot target) {

        for (IObstacle obstacle : obstacles) {
            try {
                if (obstacle.isPathBlocked(robotPosition, newPosition)) {
                    updatePositionAfterObstacleBlocked(target, obstacle);
                    return NewPositionResponse.OBSTACLE_BLOCKED;
                }
            } catch (RuntimeException e){
                return NewPositionResponse.INVALID_MOVEMENT;
            }
        }
        for (Robot robot : connectedRobots){
            if (robot.isRobotInPath(robotPosition, newPosition) && robot != target) {
                updatePositionAfterRobotBlocked(target, robot.getRobotPosition());
                return NewPositionResponse.ROBOT_BLOCKED;
            }
        }
        return NewPositionResponse.NOT_BLOCKED;
    }

    public void updatePositionAfterRobotBlocked(Robot target, Position blockRobotPosition){
        Position robotPosition = target.getRobotPosition();
        Position newPosition;

        if (robotPosition.getX() >= blockRobotPosition.getX() && robotPosition.getX() >= blockRobotPosition.getX() + 4) {
            if (robotPosition.getY() > blockRobotPosition.getY()) {
                newPosition = new Position(robotPosition.getX(),
                                        blockRobotPosition.getY()+1);
                target.setRobotPosition(newPosition);
                target.setPosition(newPosition.getX(), newPosition.getY());
            } else {
                newPosition = new Position(robotPosition.getX(),
                                        blockRobotPosition.getY()-1);
                target.setRobotPosition(newPosition);
                target.setPosition(newPosition.getX(), newPosition.getY());
            }
        }

        if (robotPosition.getY() >= blockRobotPosition.getY() && robotPosition.getY() <= blockRobotPosition.getY() + 4) {
            if (robotPosition.getX() > blockRobotPosition.getX()) {
                newPosition = new Position(blockRobotPosition.getX() + 1, robotPosition.getY());
                target.setRobotPosition(newPosition);
                target.setPosition(newPosition.getX(), newPosition.getY());
            } else {
                newPosition = new Position(blockRobotPosition.getX() - 1, robotPosition.getY());
                target.setRobotPosition(newPosition);
                target.setPosition(newPosition.getX(), newPosition.getY());
            }
        }

    }

    public void updatePositionAfterObstacleBlocked(Robot target, IObstacle obstacle){

        Position robotPosition = target.getRobotPosition();
        Position newPosition;

        if (robotPosition.getX() >= obstacle.getBottomLeftX() && robotPosition.getX() <= obstacle.getBottomRightX()) {
            //Checking if above obstacle
            if (robotPosition.getY() > obstacle.getTopLeftY()){
                newPosition = new Position(robotPosition.getX(), obstacle.getTopLeftY() + 1);
                target.setRobotPosition(newPosition);
                target.setPosition(newPosition.getX(), newPosition.getY());
            // Checking if below obstacle
            } else if (robotPosition.getY() < obstacle.getBottomLeftY()){
                newPosition = new Position(robotPosition.getX(), obstacle.getBottomLeftY()-1);
                target.setRobotPosition(newPosition);
                target.setPosition(newPosition.getX(), newPosition.getY());
            }
        }

        if (robotPosition.getY() >= obstacle.getBottomLeftY() && robotPosition.getY() <= obstacle.getTopLeftY()){
            //Checking if on right side of obstacle wall
            if (robotPosition.getX() > obstacle.getBottomRightX()){
                newPosition = new Position(obstacle.getBottomRightX() + 1, robotPosition.getY());
                target.setRobotPosition(newPosition);
                target.setPosition(newPosition.getX(), newPosition.getY());
            //Checking if on left side of obstacle wall
            } else if (robotPosition.getX() < obstacle.getBottomLeftX()){
                newPosition = new Position(obstacle.getBottomLeftX() - 1, robotPosition.getY());
                target.setRobotPosition(newPosition);
                target.setPosition(newPosition.getX(), newPosition.getY());
            }
        }
//
//        if (robotPosition.getY() >= obstaclePosition.getY() && robotPosition.getY() <= obstaclePosition.getY() + 4) {
//            if (robotPosition.getX() > obstaclePosition.getX()) {
//                newPosition = new Position(obstaclePosition.getX() + 1, robotPosition.getY());
//                target.setRobotPosition(newPosition);
//                target.setPosition(newPosition.getX(), newPosition.getY());
//            } else {
//                newPosition = new Position(obstaclePosition.getX() - 1, robotPosition.getY());
//                target.setRobotPosition(newPosition);
//                target.setPosition(newPosition.getX(), newPosition.getY());
//            }
//        }

    }

    private void checkEdges(List<IWorldObject> list, PositionAndDirection edgePosition) {
        Position position = edgePosition.getPosition();
        if(position.getX() >= right){
            Edge newEdge = new Edge();
            newEdge.addEdgePosition(new Position(right, position.getY()));
            newEdge.setDirectionOfObject(edgePosition.getDirection());
            list.add(newEdge);
        }
        if(position.getX() <= left){
            Edge newEdge = new Edge();
            newEdge.addEdgePosition(new Position(left, position.getY()));
            newEdge.setDirectionOfObject(edgePosition.getDirection());
            list.add(newEdge);
        }
        if(position.getY() >= top){
            Edge newEdge = new Edge();
            newEdge.addEdgePosition(new Position(position.getX(), top));
            newEdge.setDirectionOfObject(edgePosition.getDirection());
            list.add(newEdge);
        }
        if(position.getY() <= bottom){
            Edge newEdge = new Edge();
            newEdge.addEdgePosition(new Position(position.getX(), bottom));
            newEdge.setDirectionOfObject(edgePosition.getDirection());
            list.add(newEdge);
        }
    }

    public boolean containsRobot(Robot robot) {
        return true;
    }

    public boolean hasRobot(Robot robot) {
        return true;
    }
}
