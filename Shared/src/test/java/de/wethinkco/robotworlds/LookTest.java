package de.wethinkco.robotworlds;

import de.wethinkco.robotworlds.protocol.LookRequestMessage;
import de.wethinkco.robotworlds.protocol.ObstacleResponse;
import de.wethinkco.robotworlds.protocol.ResponseMessage;
import de.wethinkco.robotworlds.protocol.robots.Robot;
import de.wethinkco.robotworlds.protocol.world.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LookTest {

    @Test
    void getLookRequest(){
        LookRequestMessage test = new LookRequestMessage("aalz");
        assertEquals("look", test.getCommand());
    }

    @Test
    public void lookRequest() {
        List<IObstacle> list = new ArrayList<IObstacle>();
        IObstacle obstacle = new SquareObstacle(1,1);
        LookRequestMessage test = new LookRequestMessage();
        list.add(obstacle);
        GridSize grid = new GridSize(200, 100);
        RobotVisionField visionField = new RobotVisionField(10);
        Robot robot = new Robot();
        World world = new World(list);

        ResponseMessage responseMessage= test.execute(robot, world);
        assertEquals("OK",responseMessage.getResult());
        assertEquals(1, responseMessage.getData().size());
        assertTrue(responseMessage.getData().containsKey("objects"));
        List<ObstacleResponse> obstacles = (List<ObstacleResponse>) responseMessage.getData().get("objects");
        assertEquals(1, obstacles.size());
        ObstacleResponse obstacleResponse = obstacles.get(0);
        assertEquals("NORTH", obstacleResponse.getDirection());
        assertEquals(TypeOfObject.OBSTACLE, obstacleResponse.getTypeOfObject());
        assertEquals(1, obstacleResponse.getSteps());
    }
}
