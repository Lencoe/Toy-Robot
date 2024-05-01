package de.wethinkco.robotworlds;

import de.wethinkco.robotworlds.protocol.robots.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RobotTest {

    @Test
    public void testCreateRobot() {
        Robot robot = Robot.createRobot("R2D2", "assault");
        assertTrue(robot instanceof AssaultRobot);

        Robot robot1 = Robot.createRobot("C3PO", "runner");
        assertTrue(robot1 instanceof RunnerRobot);

        Robot robot2 = Robot.createRobot("MuffinMan", "spy");
        assertTrue(robot2 instanceof SpyRobot);
        // Do you know the Muffin Man?

        Robot robot3 = Robot.createRobot("BB-8", "heavy");
        assertTrue(robot3 instanceof HeavyRobot);

        Robot robot4 = Robot.createRobot("CT-9904", "sniper");
        assertTrue(robot4 instanceof SniperRobot);
    }

    @Test
    public void testCreateRobotWithInvalidName() {
        Robot robot = Robot.createRobot("", "assault");
        assertNull(robot);
    }

    @Test
    public void testCreateRobotWithInvalidType() {
        Robot robot = Robot.createRobot("Teemo", "devilspawn");
        assertNull(robot);
    }

    // TODO: Update to use NewPositionResponse
    //Test robot update position works
//    @Test
//    public void testUpdatePosition() {
//        List<IObstacle> list = new ArrayList<IObstacle>();
//        IObstacle obstacle = new SquareObstacle(1,1);
//        list.add(obstacle);

//        Robot robot = Robot.createRobot("Yasuo", "assault");
//        assert robot != null;
//        robot.setDirection("NORTH");
//        robot.setTOP_LEFT(-100, 200);
//        robot.setBOTTOM_RIGHT(100, -200);
//        assertTrue(robot.updatePosition(5, new World(list)));
//        assertEquals(5, (int) robot.getPosition().get(1));
//    }
    // TODO: Update to use NewPositionResponse
//    @Test
//    public void testUpdatePositionOutOfBounds(){
//        List<IObstacle> list = new ArrayList<IObstacle>();
//        IObstacle obstacle = new SquareObstacle(1,1);
//        list.add(obstacle);
//        Robot robot = Robot.createRobot("Legolas", "assault");
//        assert robot != null;
//        assertFalse(robot.updatePosition(201, new World(list)));
//    }

    //Test robot takes damage
    @Test
    public void testTakeDamage() {
        Robot robot = Robot.createRobot("JohnDoe", "assault");
        assert robot != null;

        robot.setShields(5);
        assertEquals(5, robot.getShields());
        robot.applyDamage(2);
        assertEquals(3, robot.getShields());
        assertFalse(robot.isDead());
    }

    @Test
    public void testRobotDead(){
        Robot robot = Robot.createRobot("Kenny", "assault");
        assert robot != null;
        robot.setShields(5);
        robot.applyDamage(5);
        assertTrue(robot.isDead());
        // YOU KILLED KENNY! YOU BASTARD!
    }

}
// End of RobotTest.java