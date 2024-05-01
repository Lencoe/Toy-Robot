package de.wethinkco.robotworlds;

import de.wethinkco.robotworlds.protocol.*;
import de.wethinkco.robotworlds.protocol.robots.Robot;
import de.wethinkco.robotworlds.protocol.world.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StateTest {

    @Test
    void getStateRequest(){
        StateRequestMessage test = new StateRequestMessage("aalz");
        assertEquals("state", test.getCommand());
    }

    @Test
    public void testRobotNotExists() {
        World world = new World();
        Robot robot = new Robot();

        world.addRobot(robot);

        Assert.assertTrue("Robot does not exist in the world.", world.hasRobot(robot));
    }

    @Test
    public void testRobotExistence() {
        World world = new World();
        Robot robot = new Robot();
        world.addRobot(robot);

        assertTrue(world.containsRobot(robot));
    }




//    @Test
//    public void doStateRequest() {
//        List<IObstacle> list = new ArrayList<IObstacle>();
//        IObstacle obstacle = new SquareObstacle(1,1);
//        StateRequestMessage test = new StateRequestMessage();
//        list.add(obstacle);
//        GridSize grid = new GridSize(200, 100);
//        RobotVisionField visionField = new RobotVisionField(10);
//        Robot robot = new Robot();
//        World world = new World(list);
//
//        ResponseMessage responseMessage= test.execute(robot, world);
//        assertEquals("OK",responseMessage.getResult());
//        assertEquals(0, responseMessage.getData().size());
//        assertInstanceOf(SuccessResponseMessage.class, responseMessage);
//        SuccessResponseMessage success = (SuccessResponseMessage) responseMessage;
//        assertNotNull(success);
//        assertNotNull(success.getState());
//        assertEquals("NORTH", success.getState().getDirection());
//
//        //do a move to blocked
//        // assert state again
//        RequestMessage forward = RequestMessage.createRequest("aalz", "forward 1");
//        responseMessage= forward.execute(robot, world);
//        assertEquals("Blocked",responseMessage.getResult());
//        assertEquals(1, responseMessage.getData().size());
//        assertInstanceOf(SuccessResponseMessage.class, responseMessage);
//        success = (SuccessResponseMessage) responseMessage;
//        assertNotNull(success);
//        assertNotNull(success.getState());
//        assertEquals("NORTH", success.getState().getDirection());
//    }
}
