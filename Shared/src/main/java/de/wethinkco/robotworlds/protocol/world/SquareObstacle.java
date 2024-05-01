package de.wethinkco.robotworlds.protocol.world;

import de.wethinkco.robotworlds.protocol.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class SquareObstacle implements IObstacle{

    private final List<Position> obstacleSet = new ArrayList<>();
    private final int x, y;
    private String directionOfObject;

    public SquareObstacle(int x, int y) {
        this.x = x;
        this.y = y;

        int xPlusSize = this.x + getSize();
        int yPlusSize = this.y + getSize();

        obstacleSet.add(new Position(x,y));
        obstacleSet.add(new Position(xPlusSize,y));
        obstacleSet.add(new Position(x,yPlusSize));
        obstacleSet.add(new Position(xPlusSize,yPlusSize));
    }

    @Override
    public List<Position> getObstacles() {
        return obstacleSet;
    }

    @Override
    public TypeOfObject getTypeOfObject() {
        return TypeOfObject.OBSTACLE;
    }

    @Override
    public void setDirectionOfObject(String directionOfObject) {
        this.directionOfObject = directionOfObject;
    }

    @Override
    public String directionOfObject() {
        return this.directionOfObject;
    }

    @Override
    public boolean isPathBlocked(Position oldPosition, Position newPosition) {
        boolean changeInY = oldPosition.getY() != newPosition.getY();
        boolean changeInX  = oldPosition.getX() != newPosition.getX();
        IntStream rangeSteps = null;

        if(changeInX){
            rangeSteps = oldPosition.getX() < newPosition.getX()
                    ? IntStream.rangeClosed(oldPosition.getX(), newPosition.getX())
                    : IntStream.rangeClosed(newPosition.getX(), oldPosition.getX());

            return rangeSteps
                    .mapToObj(x -> new Position(x, oldPosition.getY())) // map
                    .anyMatch(position-> this.isPositionBlocked(position)); // reduce
        }
        if(changeInY){
            rangeSteps = oldPosition.getY() < newPosition.getY()
                    ? IntStream.rangeClosed(oldPosition.getY(), newPosition.getY())
                    : IntStream.rangeClosed(newPosition.getY(), oldPosition.getY());

            return rangeSteps
                    .mapToObj(y -> new Position(oldPosition.getX(), y)) // map
                    .anyMatch(position-> this.isPositionBlocked(position)); //reduce
        }
        return false;
    }

    @Override
    public boolean isPositionBlocked(Position newPosition) {
        return (newPosition.getX() >= this.x && newPosition.getX() <= this.x + getSize() - 1) &&
                (newPosition.getY() >= this.y && newPosition.getY() <= this.y + getSize() - 1);
    }

    @Override
    public int getBottomLeftX() {
        return this.x;
    }

    @Override
    public int getBottomLeftY() {
        return this.y;
    }

    @Override
    public int getBottomRightX() {
        return this.x + getSize() - 1;
    }

    @Override
    public int getTopLeftY() {
        return this.y + getSize() - 1;
    }

    private int getSize() {
        return 5;
    }

    @Override
    public String toString() {
        return "SquareObstacle{" +
                "obstacle=" + x + ", " + y +
                '}';
    }
}
