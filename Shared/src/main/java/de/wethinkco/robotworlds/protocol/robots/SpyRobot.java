package de.wethinkco.robotworlds.protocol.robots;

public class SpyRobot extends Robot{

    public SpyRobot(){

    }

    public SpyRobot(String robotName){
        super(robotName);
        setMAX_SHOTS(5);
        setShots(5);
        setDamagePerShot(1);
    }

}
