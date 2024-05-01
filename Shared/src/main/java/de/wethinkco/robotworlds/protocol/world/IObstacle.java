package de.wethinkco.robotworlds.protocol.world;

import de.wethinkco.robotworlds.protocol.Position;

public interface IObstacle extends IWorldObject{

    boolean isPathBlocked(Position oldPosition, Position newPosition);
    boolean isPositionBlocked(Position newPosition);

    int getBottomLeftX();
    int getBottomLeftY();

    int getBottomRightX();
    int getTopLeftY();

}
