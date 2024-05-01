package de.wethinkco.robotworlds.protocol.robots;

public class SniperRobot extends Robot{

    public SniperRobot(){

    }

    public SniperRobot(String robotName){
        super(robotName);
        setMAX_SHOTS(1);
        setShots(1);
        setDamagePerShot(5);
    }

}
