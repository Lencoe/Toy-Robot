package de.wethinkco.robotworlds.protocol.robots;

import de.wethinkco.robotworlds.protocol.RobotStatus;

import java.util.ArrayList;

public class AssaultRobot extends Robot{

    public AssaultRobot(){

    }

    public AssaultRobot(String robotName){
        super(robotName);
        setMAX_SHOTS(3);
        setShots(3);
        setDamagePerShot(3);
    }
}
