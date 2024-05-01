package de.wethinkco.robotworlds.protocol.world;

import de.wethinkco.robotworlds.protocol.Position;

public class PositionAndDirection {
    public Position getPosition() {
        return position;
    }

    public String getDirection() {
        return direction;
    }

    private final Position position;
    private final String direction;

    public PositionAndDirection(Position position, String direction) {
        this.position = position;
        this.direction = direction;
    }
}
