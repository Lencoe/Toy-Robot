package de.wethinkco.robotworlds.protocol.robots;

import de.wethinkco.robotworlds.protocol.world.GridSize;
import de.wethinkco.robotworlds.protocol.*;
import de.wethinkco.robotworlds.protocol.world.IWorldObject;
import de.wethinkco.robotworlds.protocol.world.TypeOfObject;
import de.wethinkco.robotworlds.protocol.world.World;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


//@JsonTypeInfo(
//        use = JsonTypeInfo.Id.NAME,
//        include = JsonTypeInfo.As.PROPERTY,
//        property = "type"
//)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = AssaultRobot.class, name = "assault"),
//        @JsonSubTypes.Type(value = HeavyRobot.class, name = "heavy"),
//        @JsonSubTypes.Type(value = RunnerRobot.class, name = "runner"),
//        @JsonSubTypes.Type(value = SniperRobot.class, name = "sniper"),
//        @JsonSubTypes.Type(value = SpyRobot.class, name = "spy"),
//})
public class Robot implements IWorldObject {

    private String robotName;
    private List<Integer> position = null;
    private String direction;
    private int shields;
    private int shots;
    private RobotStatus status;
    @JsonIgnore
    private Position robotPosition;
    @JsonIgnore
    private Position TOP_LEFT = new Position(-GridSize.getWidth(),GridSize.getHeight());
    @JsonIgnore
    private Position BOTTOM_RIGHT = new Position(GridSize.getWidth(),-GridSize.getHeight());
    @JsonIgnore
    int MAX_SHOTS;
    @JsonIgnore
    int damagePerShot;
    @JsonIgnore
    private String directionOfObject;

    public Robot(String robotName)
    {
        this.robotPosition = new Position(0,0);
        this.robotName = robotName;
        this.position = new ArrayList<>();
        this.position.add(0);
        this.position.add(0); //Position gets randomly generated inside LaunchRequestMessage
        generateRandomDirection(); // Randomly generated
        this.shots = 0; //Based on class
        this.shields = 10; //Based on config
        this.status = RobotStatus.NORMAL;
    }

    // for unit test
    public Robot() {
        this.robotPosition = new Position(1,0);
        this.position = new ArrayList<>();
        this.position.add(1);
        this.position.add(0); //Position gets randomly generated inside LaunchRequestMessage
        this.direction = "NORTH";
        this.shots = 0; //Based on class
        this.shields = 0; //Based on config
        this.status = RobotStatus.NORMAL;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public List<Integer> getPosition() {
        return position;
    }

    public void setPosition(List<Integer> position) {
        this.position = position;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Integer getShields() {
        return shields;
    }

    public void setShields(Integer shields) {
        this.shields = shields;
    }

    public Integer getShots() {
        return shots;
    }

    public void setShots(Integer shots) {
        this.shots = shots;
    }

    public RobotStatus getStatus() {
        return status;
    }

    public void setStatus(RobotStatus status) {
        this.status = status;
    }

    public void setMAX_SHOTS(int maxShots){
        this.MAX_SHOTS = maxShots;
    }

    public int getMAX_SHOTS(){
        return this.MAX_SHOTS;
    }

    public String getRobotName() {
        return robotName;
    }

    public int getDamagePerShot() {
        return damagePerShot;
    }

    public void setDamagePerShot(int damagePerShot) {
        this.damagePerShot = damagePerShot;
    }

    public Position getRobotPosition(){
        return this.robotPosition;
    }

    @JsonIgnore
    public boolean isDead(){
        return this.status == RobotStatus.DEAD;
    }

    @JsonIgnore
    public boolean isReloading(){
        return this.status == RobotStatus.RELOAD;
    }

    public void applyDamage(int damage){
        this.shields -= damage;
        if (this.shields <= 0) {
            this.status = RobotStatus.DEAD;
        }
    }

    public void generateRandomPosition(World world){

//        Random random = new Random();
//        int x = random.nextInt((GridSize.getWidth()+1)*2) - GridSize.getWidth();
//        int y = random.nextInt((GridSize.getHeight()+1)*2) - GridSize.getHeight();
//
//        while (world.isNewPositionAllowed(new Position(x, y))){
//            x = random.nextInt((GridSize.getWidth()+1)*2) - GridSize.getWidth();
//            y = random.nextInt((GridSize.getHeight()+1)*2) - GridSize.getHeight();
//
//        }
        int x = 0;
        int y = 0;

        this.position.set(0,x);
        this.position.set(1,y);
        this.robotPosition = new Position(x,y);
    }

    public void generateRandomDirection(){
        int random = (int) (Math.random() * 4);
        switch(random){
            case 0:
                this.direction = "NORTH";
                break;
            case 1:
                this.direction = "SOUTH";
                break;
            case 2:
                this.direction = "EAST";
                break;
            case 3:
                this.direction = "WEST";
                break;
        }
    }

    @Override
    public List<Position> getObstacles() {
        List<Position> obstacles = new ArrayList<>();
        obstacles.add(this.robotPosition);
        return obstacles;
    }

    @Override
    @JsonIgnore
    public TypeOfObject getTypeOfObject() {
        return TypeOfObject.ROBOT;
    }

    @Override
    public void setDirectionOfObject(String directionOfObject) {
        this.directionOfObject = directionOfObject;
    }

    @Override
    public String directionOfObject() {
        return this.directionOfObject;
    }

    public NewPositionResponse updatePosition(int nrSteps, World world)
    {
        int newX = this.robotPosition.getX();
        int newY = this.robotPosition.getY();
        //boolean is_blocked = false;

        if (this.getDirection().equals("NORTH")){
            newY = newY + nrSteps;
        } else if (this.getDirection().equals("EAST")){
            newX = newX + nrSteps;
        } else if (this.getDirection().equals("SOUTH")){
            newY = newY - nrSteps;
        } else if (this.getDirection().equals("WEST")) {
            newX = newX - nrSteps;
        }

        Position newPosition = new Position(newX, newY);

        NewPositionResponse checkPathBlocked = world.isPathBlocked(this.robotPosition,
                                                                    newPosition,
                                                                this);
        if(!(checkPathBlocked == NewPositionResponse.NOT_BLOCKED)) {
            return checkPathBlocked;
        }

        if (newPosition.isInBounds(TOP_LEFT,BOTTOM_RIGHT) )
        {
            this.robotPosition = newPosition;
            this.position.set(0, newX);
            this.position.set(1, newY);

            return NewPositionResponse.NOT_BLOCKED;
        }

        if (newX >= getBOTTOM_RIGHT().getX()){
            newX = getBOTTOM_RIGHT().getX() - 1;
        } else if (newX <= getTOP_LEFT().getX()){
            newX = getTOP_LEFT().getX() - 1;
        } else if (newY >= getTOP_LEFT().getY()){
            newY = getTOP_LEFT().getY() - 1;
        } else if (newY <= getBOTTOM_RIGHT().getY()){
            newY = getBOTTOM_RIGHT().getY() - 1;
        }

        newPosition = new Position(newX, newY);
        this.robotPosition = newPosition;
        this.position.set(0, newX);
        this.position.set(1, newY);

        return NewPositionResponse.OUTSIDE_WORLD;
    }

    public boolean updateDirection(String directionSignal){

        if (directionSignal.equalsIgnoreCase("right")) {
            switch (this.direction) {
                case "NORTH":
                    this.direction = "EAST";
                    return true;
                case "EAST":
                    this.direction = "SOUTH";
                    return true;
                case "SOUTH":
                    this.direction = "WEST";
                    return true;
                case "WEST":
                    this.direction = "NORTH";
                    return true;
            }
        } else if (directionSignal.equalsIgnoreCase("left")) {
            switch (this.direction) {
                case "NORTH":
                    this.direction = "WEST";
                    return true;
                case "WEST":
                    this.direction = "SOUTH";
                    return true;
                case "SOUTH":
                    this.direction = "EAST";
                    return true;
                case "EAST":
                    this.direction = "NORTH";
                    return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Robot{" +
                "position=" + position +
                ", direction='" + direction + '\'' +
                ", shields=" + shields +
                ", shots=" + shots +
                ", status='" + status + '\'' +
                '}';
    }

    public static Robot createRobot(String robotName, String robotMake){
        if (!(robotName.equals("") || robotName.equals(" "))) {
            switch (robotMake) {
                case "assault":
                    return new AssaultRobot(robotName);
                case "spy":
                    return new SpyRobot(robotName);
                case "heavy":
                    return new HeavyRobot(robotName);
                case "sniper":
                    return new SniperRobot(robotName);
                case "runner":
                    return new RunnerRobot(robotName);
            }
        }
        return null;
    }

    public boolean isRobotInPath(Position robotPosition, Position edgePosition) {
        if (robotPosition.getX() == edgePosition.getX() && robotPosition.getY() != edgePosition.getY()) {
            boolean forwardMovement = edgePosition.getY() > robotPosition.getY() ? true : false;
            if (forwardMovement) {
                for (int y = robotPosition.getY(); y <= edgePosition.getY(); y++) {
                    final Position currentPosition = new Position(edgePosition.getX(), y);
                    if (currentPosition.getY() == this.robotPosition.getY() && currentPosition.getX() == this.getRobotPosition().getX()) {
                        return true;
                    }
                }
            } else {
                for (int y = edgePosition.getY(); y >= edgePosition.getY(); y--) {
                    final Position currentPosition = new Position(edgePosition.getX(), y);
                    if (currentPosition.getY() == this.robotPosition.getY() && currentPosition.getX() == this.getRobotPosition().getX()) {
                        return true;
                    }
                }
            }
            return false;
        } else if (robotPosition.getY() == edgePosition.getY() && robotPosition.getX() != edgePosition.getX()) {
            // handle change in X
            boolean forwardMovement = edgePosition.getX() > robotPosition.getX() ? true : false;
            if (forwardMovement) {
                for (int x = robotPosition.getX(); x <= edgePosition.getX(); x++) {
                    final Position currentPosition = new Position(x, robotPosition.getY());
                    if (currentPosition.getY() == this.robotPosition.getY() && currentPosition.getX() == this.getRobotPosition().getX()) {
                        return true;
                    }
                }
            } else {
                for (int x = robotPosition.getX(); x >= edgePosition.getX(); x--) {
                    final Position currentPosition = new Position(x, robotPosition.getY());
                    if (currentPosition.getY() == this.robotPosition.getY() && currentPosition.getX() == this.getRobotPosition().getX()){
                        return true;
                    }
                }
            }
            return false;
        } else {
            throw new RuntimeException("Invalid path");
        }
    }
    @JsonIgnore
    public Position getTOP_LEFT(){
        return TOP_LEFT;
    }

    @JsonIgnore
    public void setTOP_LEFT(int x, int y){
        this.TOP_LEFT = new Position(x, y);
    }

    @JsonIgnore
    public void setBOTTOM_RIGHT(int x, int y){
        this.BOTTOM_RIGHT = new Position(x, y);
    }

    @JsonIgnore
    public Position getBOTTOM_RIGHT(){
        return BOTTOM_RIGHT;
    }

    public void setRobotPosition(Position newPosition){
        this.robotPosition = newPosition;
    }

    @JsonIgnore
    public Robot getUpdatedRobot(){
        return this;
    }

    public void setPosition(int x, int y){
        this.position.set(0, x);
        this.position.set(1, y);
    }

    public void minusShots(){
        this.shots -= 1;
    }
}
