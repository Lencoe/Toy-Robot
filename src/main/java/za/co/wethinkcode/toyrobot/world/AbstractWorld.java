package za.co.wethinkcode.toyrobot.world;

import za.co.wethinkcode.toyrobot.Command;
import za.co.wethinkcode.toyrobot.Position;
import za.co.wethinkcode.toyrobot.Robot;
import za.co.wethinkcode.toyrobot.maze.Maze;

import java.util.List;


public abstract  class AbstractWorld implements  IWorld{

    private final Position TOP_LEFT = new Position(-100,200);
    private final Position BOTTOM_RIGHT = new Position(100,-200);

    private Direction currentDirection = Direction.UP;

    private Position position = IWorld.CENTRE;

    private int dict = 0;

    private final List<Obstacle> obstacles;

    public AbstractWorld (Maze maze){
        this.obstacles = maze.getObstacles();
    }

    public UpdateResponse updatePosition(int nrSteps){

        int newX = this.position.getX();
        int newY = this.position.getY();
        Position position = this.getPosition();

        if (Direction.UP.equals(this.getCurrentDirection())) {
            newY = newY + nrSteps;
        } else if (Direction.RIGHT.equals(this.getCurrentDirection())) {
            newX = newX+ nrSteps;
        } else if (Direction.DOWN.equals(this.getCurrentDirection())) {
            newY = newY - nrSteps;
        } else if (Direction.LEFT.equals(this.getCurrentDirection())) {
            newX = newX - nrSteps;
        }

        Position newPosition = new Position(newX, newY);

        for(int i = 0; i<getObstacles().size();i++){

            if(getObstacles().get(i).blocksPosition(newPosition) || getObstacles().get(i).blocksPath(position,newPosition)){
                return UpdateResponse.FAILED_OBSTRUCTED;
            }
        }

        if (newPosition.isIn(TOP_LEFT,BOTTOM_RIGHT)) {
            this.position = newPosition;
            return UpdateResponse.SUCCESS;
        }



        return UpdateResponse.FAILED_OUTSIDE_WORLD;
    }

   public Position getPosition(){

        return position;
   }

    @Override
    public void updateDirection(boolean turnRight) {

        if(turnRight) {
            dict++;

            if ( dict == 4) {
                dict = 0;
            }

        }

        else {

            dict--;
            if ( dict < 0){
                dict = 3;
            }

        }

        switch (dict){
            case 0:
                currentDirection = Direction.UP;
                break;
            case 1:
                currentDirection = Direction.RIGHT;
                break;
            case 2:
                currentDirection = Direction.DOWN;
                break;
            case 3:
                currentDirection = Direction.LEFT;
                break;

        }

    }

    @Override
    public boolean isNewPositionAllowed(Position position) {
        if (position.isIn(TOP_LEFT,BOTTOM_RIGHT)){
            this.position  = position;
            return true;
        }
        return false;
    }

    @Override
    public boolean isAtEdge() {
        if (position.getX() == 100 || position.getX() == -100 || position.getY() == 200 || position.getY() == -200){
            return true;
        }
        return  false;
    }

    @Override
    public void reset() {
        currentDirection = Direction.UP;
        position = CENTRE;

    }


    public  Direction getCurrentDirection(){

        return currentDirection;
    }

    @Override
    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }

    @Override
    public void showObstacles() {

    }

    public Object moves(Command command, Robot robot) {
        return null;
    }
}

