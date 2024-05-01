package de.wethinkco.robotworlds.protocol.world;

import de.wethinkco.robotworlds.protocol.Position;

import java.util.ArrayList;
import java.util.List;

public class Edge implements IWorldObject{
    private List<Position> edgePositions = new ArrayList<>();
    private String directionOfObject;

    @Override
    public List<Position> getObstacles() {
        return edgePositions;
    }

    @Override
    public TypeOfObject getTypeOfObject() {
        return TypeOfObject.EDGE;
    }

    @Override
    public void setDirectionOfObject(String directionOfObject) {
        this.directionOfObject = directionOfObject;
    }

    @Override
    public String directionOfObject() {
        return this.directionOfObject;
    }

    public void addEdgePosition(Position position){
        edgePositions.add(position);
    }
}
