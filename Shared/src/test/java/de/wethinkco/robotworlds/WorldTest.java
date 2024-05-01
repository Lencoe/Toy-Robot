package de.wethinkco.robotworlds;

import de.wethinkco.robotworlds.protocol.robots.Robot;
import de.wethinkco.robotworlds.protocol.world.World;
import org.junit.Assert;
import org.junit.jupiter.api.Test;



public class WorldTest {

    @Test
    public void testRemoveRobot(){
        World world = new World();
        Robot robot = new Robot();

        world.addRobot(robot);
        Assert.assertEquals(world.getConnectedRobotsSize(), 1);

        world.removeRobot(robot);
        Assert.assertEquals(world.getConnectedRobotsSize(), 0);
    }
}
