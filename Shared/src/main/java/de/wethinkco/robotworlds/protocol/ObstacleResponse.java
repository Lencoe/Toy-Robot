package de.wethinkco.robotworlds.protocol;

import de.wethinkco.robotworlds.protocol.world.TypeOfObject;

public class ObstacleResponse
{
    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setType(TypeOfObject type) {
        this.type = type;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    private String direction;
    private TypeOfObject type;
    private int steps;

    public ObstacleResponse(){}

    public ObstacleResponse(String direction, TypeOfObject type, int steps){
        this.direction = direction;
        this.type = type;
        this.steps = steps;
    }

    public String getDirection() {
        return direction;
    }

    public TypeOfObject getTypeOfObject() {
        return type;
    }

    public int getSteps() {
        return steps;
    }
}
