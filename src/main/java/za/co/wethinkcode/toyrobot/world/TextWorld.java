package za.co.wethinkcode.toyrobot.world;

import za.co.wethinkcode.toyrobot.Command;
import za.co.wethinkcode.toyrobot.Robot;
import za.co.wethinkcode.toyrobot.maze.Maze;

import java.util.List;

public class TextWorld extends AbstractWorld{

    public TextWorld (Maze maze) {
        super(maze);

    }

    public UpdateResponse moves(Command command, Robot target) {
//        System.out.println(updatePosition(Integer.parseInt(command.getArgument())).equals(UpdateResponse.FAILED_OUTSIDE_WORLD));
//        System.out.println(command.getName().equals("forward") && updatePosition(Integer.parseInt(command.getArgument())).equals(UpdateResponse.SUCCESS));
        if (command.getName().equals("forward") && updatePosition(Integer.parseInt(command.getArgument())).equals(UpdateResponse.SUCCESS)) {
            return UpdateResponse.SUCCESS;
        } else if (command.getName().equals("forward") && updatePosition(Integer.parseInt(command.getArgument())).equals(UpdateResponse.FAILED_OBSTRUCTED)) {
            target.setStatus("Sorry, there is an obstacle in the way.");
            return UpdateResponse.FAILED_OBSTRUCTED;
        } else if (command.getName().equals("forward") && updatePosition(Integer.parseInt(command.getArgument())).equals(UpdateResponse.FAILED_OUTSIDE_WORLD)) {
            target.setStatus("Sorry, I cannot go outside my safe zone.");
            return UpdateResponse.FAILED_OUTSIDE_WORLD;
        }else if (command.getName().equals("back") && updatePosition(-Integer.parseInt(command.getArgument())).equals(UpdateResponse.SUCCESS)) {
            return UpdateResponse.SUCCESS;
        } else if (command.getName().equals("back") && updatePosition(-Integer.parseInt(command.getArgument())).equals(UpdateResponse.FAILED_OBSTRUCTED)) {
            target.setStatus("Sorry, there is an obstacle in the way.");
            return UpdateResponse.FAILED_OBSTRUCTED;
        } else if (command.getName().equals("back") && updatePosition(-Integer.parseInt(command.getArgument())).equals(UpdateResponse.FAILED_OUTSIDE_WORLD)) {
            target.setStatus("Sorry, I cannot go outside my safe zone.");
            return UpdateResponse.FAILED_OUTSIDE_WORLD;
        } else if (command.getName().equals("left")) {
            updateDirection(false);
            return UpdateResponse.SUCCESS;
        } else if (command.getName().equals("right")) {
            updateDirection(true);
            return UpdateResponse.SUCCESS;
        } else {
            return UpdateResponse.SUCCESS;
        }



    }






}

