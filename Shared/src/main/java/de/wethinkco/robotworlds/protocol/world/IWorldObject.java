package de.wethinkco.robotworlds.protocol.world;

import de.wethinkco.robotworlds.protocol.Position;

import java.util.List;

public interface IWorldObject {
    List<Position> getObstacles();
    TypeOfObject getTypeOfObject();
    void setDirectionOfObject(String directionOfObject);
    String directionOfObject();
}
