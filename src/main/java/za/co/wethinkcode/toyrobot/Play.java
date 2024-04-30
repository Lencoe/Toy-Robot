package za.co.wethinkcode.toyrobot;
import za.co.wethinkcode.toyrobot.maze.*;
import za.co.wethinkcode.toyrobot.world.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Play {
    static Scanner scanner;

//    private String[] commands = {"forward", "left", "right", "back", "sprint"};
static ArrayList<String> commands = new ArrayList<>(
        Arrays.asList("forward", "left", "right", "back", "sprint")
);
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        Robot robot;

        Maze maze;
        AbstractWorld world;
        if (args.length > 1 && args[1].equalsIgnoreCase("SimpleMaze")) {
            maze = new SimpleMaze();
        } else if (args.length > 1 && args[1].equalsIgnoreCase("RandomMaze")) {
            maze = new RandomMaze();
        } else if (args.length > 1 && args[1].equalsIgnoreCase("DesignedMaze")) {
            maze = new DesignedMaze();
        } else {
            maze = new EmptyMaze();
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("turtle")){
            world = new TurtleWorld(maze);
        }else {
            world = new TextWorld(maze);
        }




        String name = getInput("What do you want to name your robot?");
        robot = new Robot(name);
        System.out.println("Hello Kiddo!");
        if(args.length > 1){
            System.out.println("Loaded "+args[1]+".");
        }
        else {
            System.out.println("Loaded EmptyMaze.");

        }

        if (maze.getObstacles().size() > 0){
            System.out.println("There are some obstacles:");
            for (Obstacle squareObstacle : maze.getObstacles()) {
                System.out.println("- At position "+squareObstacle.getBottomLeftX()+","+squareObstacle.getBottomLeftY()+" (to "+(squareObstacle.getBottomLeftX()+4)+","+(squareObstacle.getBottomLeftY()+4)+")");
            }
        }


        System.out.println(robot.toString());
        Command command;
        boolean shouldContinue = true;
        do {
            String instruction = getInput(robot.getName() + "> What must I do next?").strip().toLowerCase();
            try {
                command = Command.create(instruction);
                String[] args2 = instruction.toLowerCase().trim().split(" ");

                if (!command.getName().equals("replay")) {
                    robot.setHistory(instruction);
                }

                if (world.moves(command, robot).equals(IWorld.UpdateResponse.SUCCESS)) {
                    shouldContinue = robot.handleCommand(command);
                }
            } catch (IllegalArgumentException e) {
                robot.setStatus("Sorry, I did not understand '" + instruction + "'.");
            }
            System.out.println(robot);
        } while (shouldContinue);

    }

    private static String getInput(String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine();

        while (input.isBlank()) {
            System.out.println(prompt);
            input = scanner.nextLine();
        }


        return input;
    }
}

