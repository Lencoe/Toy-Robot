package za.co.wethinkcode.toyrobot;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void getShutdownName() {
        Command test = new ShutdownCommand();
        assertEquals("off", test.getName());
    }

    @Test
    void executeShutdown() {
        Robot robot = new Robot("CrashTestDummy");
        Command shutdown = Command.create("shutdown");
        assertFalse(shutdown.execute(robot));
        assertEquals("Shutting down...", robot.getStatus());
    }

    @Test
    void getForwardName() {
        ForwardCommand test = new ForwardCommand("100");
        assertEquals("forward", test.getName());
        assertEquals("100", test.getArgument());
    }


    @Test
    void getSprintName() {
        SprintCommand test = new SprintCommand("5");
        assertEquals("sprint", test.getName());
        assertEquals("5", test.getArgument());
    }

    @Test
    void getLeftName() {
        LeftCommand test = new LeftCommand();
        assertEquals("left", test.getName());
    }

    @Test
    void getRightName() {
        RightCommand test = new RightCommand();
        assertEquals("right", test.getName());
    }

    @Test
    void executeForward() {
        Robot robot = new Robot("CrashTestDummy");
        Command forward100 = Command.create("forward 10");
        assertTrue(forward100.execute(robot));
        Position expectedPosition = new Position(Robot.CENTRE.getX(), Robot.CENTRE.getY() + 10);
        assertEquals(expectedPosition, robot.getPosition());
        assertEquals("Moved forward by 10 steps.", robot.getStatus());
    }

    @Test
    void executeBack() {
        Robot robot = new Robot("CrashTestDummy");
        Command back10 = Command.create("back 10");
        assertTrue(back10.execute(robot));
        Position expectedPosition = new Position(Robot.CENTRE.getX(), Robot.CENTRE.getY() - 10);
        assertEquals(expectedPosition, robot.getPosition());
        assertEquals("Moved back by 10 steps.", robot.getStatus());
    }

    @Test
    void executeSprint() {
        Robot robot = new Robot("CrashTestDummy");
        Command sprint5 = Command.create("sprint 5");
        assertTrue(sprint5.execute(robot));
        Position expectedPosition = new Position(Robot.CENTRE.getX(), Robot.CENTRE.getY() + 15);
        assertEquals(expectedPosition, robot.getPosition());
        assertEquals("Moved forward by 1 steps.", robot.getStatus());
    }

    @Test
    void executeLeft() {
        Robot robot = new Robot("CrashTestDummy");
        Command left = Command.create("left");
        assertTrue(left.execute(robot));
        Position expectedPosition = new Position(Robot.CENTRE.getX(), Robot.CENTRE.getY());
        assertEquals(expectedPosition, robot.getPosition());
        assertEquals("Turned left.", robot.getStatus());
    }

    @Test
    void executeRight() {
        Robot robot = new Robot("CrashTestDummy");
        Command right = Command.create("right");
        assertTrue(right.execute(robot));
        Position expectedPosition = new Position(Robot.CENTRE.getX(), Robot.CENTRE.getY());
        assertEquals(expectedPosition, robot.getPosition());
        assertEquals("Turned right.", robot.getStatus());
    }

    @Test
    void getHelpName() {
        Command test = new HelpCommand();                                                               //<1>
        assertEquals("help", test.getName());
    }

    @Test
    void createCommand() {
        Command forward = Command.create("forward 10");                                                 //<1>
        assertEquals("forward", forward.getName());
        assertEquals("10", forward.getArgument());

        Command back = Command.create("back 10");                                                 //<1>
        assertEquals("back", back.getName());
        assertEquals("10", back.getArgument());

        Command shutdown = Command.create("shutdown");                                                  //<2>
        assertEquals("off", shutdown.getName());

        Command help = Command.create("help");                                                          //<3>
        assertEquals("help", help.getName());

        Command left = Command.create("left");                                                          //<3>
        assertEquals("left", left.getName());

        Command right = Command.create("right");                                                          //<3>
        assertEquals("right", right.getName());

        Command sprint = Command.create("sprint 5");                                                          //<3>
        assertEquals("sprint", sprint.getName());

    }

    @Test
    void createInvalidCommand() {
        try {
            Command forward = Command.create("say hello");                                              //<4>
            fail("Should have thrown an exception");                                                    //<5>
        } catch (IllegalArgumentException e) {
            assertEquals("Unsupported command: say hello", e.getMessage());                             //<6>
        }
    }
}
