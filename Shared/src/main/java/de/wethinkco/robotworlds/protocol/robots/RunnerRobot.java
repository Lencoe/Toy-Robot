package de.wethinkco.robotworlds.protocol.robots;

public class RunnerRobot extends Robot{

    public RunnerRobot(){

    }

    public RunnerRobot(String robotName){
        super(robotName);
        setMAX_SHOTS(4);
        setShots(4);
        setDamagePerShot(2);
    }
}
