package de.wethinkco.robotworlds.protocol.robots;

public class HeavyRobot extends Robot{

    public HeavyRobot(){

    }

    public HeavyRobot(String robotName){
        super(robotName);
        setMAX_SHOTS(2);
        setShots(2);
        setDamagePerShot(4);
    }
}
