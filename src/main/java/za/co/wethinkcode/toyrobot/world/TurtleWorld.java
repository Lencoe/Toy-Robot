package za.co.wethinkcode.toyrobot.world;

import za.co.wethinkcode.toyrobot.Command;
import za.co.wethinkcode.toyrobot.Robot;
import za.co.wethinkcode.toyrobot.maze.Maze;

import za.co.wethinkcode.toyrobot.world.turtle.Turtle;
import za.co.wethinkcode.toyrobot.world.turtle.World;

public class TurtleWorld extends AbstractWorld {

    private static final World world = new World(800,800);
    private static final Turtle turtle = new Turtle(world);

    public TurtleWorld(Maze maze) {
        super(maze);
        turtle.setDelay(0);
        showObstacles();
        drawWorld();
    }

    public UpdateResponse moves(Command command, Robot target) {
        if (command.getName().equals("forward") && updatePosition(Integer.parseInt(command.getArgument())).equals(UpdateResponse.SUCCESS)) {
            turtle.move_forward = Integer.parseInt(command.getArgument());
            turtle.forward();
            return UpdateResponse.SUCCESS;
        } else if (command.getName().equals("forward") && updatePosition(Integer.parseInt(command.getArgument())).equals(UpdateResponse.FAILED_OBSTRUCTED)) {
            target.setStatus("Sorry, there is an obstacle in the way.");
            return UpdateResponse.FAILED_OBSTRUCTED;
        } else if (command.getName().equals("forward") && updatePosition(Integer.parseInt(command.getArgument())).equals(UpdateResponse.FAILED_OUTSIDE_WORLD)) {
            target.setStatus("Sorry, I cannot go outside my safe zone.");
            return UpdateResponse.FAILED_OUTSIDE_WORLD;
        }else if (command.getName().equals("back") && updatePosition(-Integer.parseInt(command.getArgument())).equals(UpdateResponse.SUCCESS)) {
            turtle.move_back = Integer.parseInt(command.getArgument());
            turtle.backward();
            return UpdateResponse.SUCCESS;
        } else if (command.getName().equals("back") && updatePosition(-Integer.parseInt(command.getArgument())).equals(UpdateResponse.FAILED_OBSTRUCTED)) {
            target.setStatus("Sorry, there is an obstacle in the way.");
            return UpdateResponse.FAILED_OBSTRUCTED;
        } else if (command.getName().equals("back") && updatePosition(-Integer.parseInt(command.getArgument())).equals(UpdateResponse.FAILED_OUTSIDE_WORLD)) {
            target.setStatus("Sorry, I cannot go outside my safe zone.");
            return UpdateResponse.FAILED_OUTSIDE_WORLD;
        } else if (command.getName().equals("left")) {
            updateDirection(false);
            turtle.turnLeft(90);
            return UpdateResponse.SUCCESS;
        } else if (command.getName().equals("right")) {
            updateDirection(true);
            turtle.turnRight(90);
            return UpdateResponse.SUCCESS;
        } else {
            return UpdateResponse.SUCCESS;
        }



    }


    public static void drawWorld() {
        turtle.penUp();
        turtle.goTo(-100,200);
        turtle.penDown();
        turtle.goTo(100,200);
        turtle.goTo(100,-200);
        turtle.goTo(-100,-200);
        turtle.goTo(-100,200);
        turtle.penUp();
        turtle.goTo(0, 0);
        turtle.turnLeft(90);
    }

    @Override
    public void showObstacles() {
        super.showObstacles();
        for (int i = 0; i < getObstacles().size(); i++) {
            turtle.penUp();
            Obstacle obstacle = getObstacles().get(i);
            int x = obstacle.getBottomLeftX();
            int y = obstacle.getBottomLeftY();
            turtle.goTo(x,y);
            turtle.penDown();
            turtle.goTo(x, y+4);
            turtle.goTo(x+4, y+4);
            turtle.goTo(x+4, y);
            turtle.goTo(x, y);
            turtle.penUp();
            turtle.goTo(0,0);
        }
    }
}
