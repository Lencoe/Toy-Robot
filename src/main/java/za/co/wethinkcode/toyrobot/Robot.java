package za.co.wethinkcode.toyrobot;

import za.co.wethinkcode.toyrobot.world.IWorld;

import java.util.ArrayList;


public class Robot {
    private final Position TOP_LEFT = new Position(-100,200);
    private final Position BOTTOM_RIGHT = new Position(100,-200);

    public static final Position CENTRE = new Position(0,0);
    public static int dict = 0;


    private Position position;
    private IWorld.Direction currentDirection;
    private String status;
    private final String name;

    public ArrayList<String> history;

    public Robot(String name) {
        this.name = name;
        this.status = "Ready";
        this.position = CENTRE;
        this.currentDirection = IWorld.Direction.UP;
        history = new ArrayList<>();
    }

    public String getStatus() {
        return this.status;
    }

    public IWorld.Direction getCurrentDirection() {
        return this.currentDirection;
    }

    public boolean handleCommand(Command command) {
        return command.execute(this);
    }

    public ArrayList<String> getHistory(){

        return history;
    }

    public void setHistory(String Command) {
        history.add(Command);
    }

    public boolean updatePosition(int nrSteps){
        int newX = this.position.getX();
        int newY = this.position.getY();

        if (IWorld.Direction.UP.equals(this.currentDirection)) {
            newY = newY + nrSteps;
        } else if (IWorld.Direction.RIGHT.equals(this.currentDirection)) {
            newX = newX+ nrSteps;
        } else if (IWorld.Direction.DOWN.equals(this.currentDirection)) {
            newY = newY - nrSteps;
        } else if (IWorld.Direction.LEFT.equals(this.currentDirection)) {
            newX = newX - nrSteps;
        }

        Position newPosition = new Position(newX, newY);
        if (newPosition.isIn(TOP_LEFT,BOTTOM_RIGHT)){
            this.position = newPosition;
            return true;
        }
        return false;
    }

    public void updateDirection(String command){

        if(command.equals("right") && IWorld.Direction.UP.equals(this.currentDirection)) {
            this.currentDirection = IWorld.Direction.RIGHT;
        } else if(command.equals("right") && IWorld.Direction.RIGHT.equals(this.currentDirection)) {
            this.currentDirection = IWorld.Direction.DOWN;
        } else if(command.equals("right") && IWorld.Direction.DOWN.equals(this.currentDirection)) {
            this.currentDirection = IWorld.Direction.LEFT;
        } else if(command.equals("right") && IWorld.Direction.LEFT.equals(this.currentDirection)) {
            this.currentDirection = IWorld.Direction.UP;
        }

        if(command.equals("left") && IWorld.Direction.UP.equals(this.currentDirection)) {
            this.currentDirection = IWorld.Direction.LEFT;
        } else if(command.equals("left") && IWorld.Direction.LEFT.equals(this.currentDirection)) {
            this.currentDirection = IWorld.Direction.DOWN;
        } else if(command.equals("left") && IWorld.Direction.DOWN.equals(this.currentDirection)) {
            this.currentDirection = IWorld.Direction.RIGHT;
        }else if(command.equals("left") && IWorld.Direction.RIGHT.equals(this.currentDirection)) {
            this.currentDirection = IWorld.Direction.UP;
        }

//        if(command.equals("right")) {
//                dict++;
//
//                if (dict == 4) {
//                    dict = 0;
//                }
//
//            }
//
//            else if(command.equals("left")){
//
//                dict--;
//                if (dict < 0){
//                    dict = 3;
//                }
//
//            }
//
//            switch (dict){
//                case 0:
//                    currentDirection = IWorld.Direction.UP;
//                    break;
//                case 1:
//                    currentDirection = IWorld.Direction.RIGHT;
//                    break;
//                case 2:
//                    currentDirection = IWorld.Direction.DOWN;
//                    break;
//                case 3:
//                    currentDirection = IWorld.Direction.LEFT;
//                    break;
//
//            }

    }
    @Override
    public String toString() {
       return "[" + this.position.getX() + "," + this.position.getY() + "] "
               + this.name + "> " + this.status;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }
}
