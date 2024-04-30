package za.co.wethinkcode.toyrobot.maze;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import za.co.wethinkcode.toyrobot.world.Obstacle;
import za.co.wethinkcode.toyrobot.world.SquareObstacle;
import za.co.wethinkcode.toyrobot.world.TurtleWorld;

public class DesignedMaze extends AbstractMaze{

    public static List<String> maze = new ArrayList<>();
    public DesignedMaze() {
        List<String> maze = getCharacters();
        List<Obstacle> obstacles = generateObstacles(maze);
        setObstacles(obstacles);

    }



    public static List<String> getCharacters(){

        try {
            List<String> allLines = Files.readAllLines(Paths.get("DesignedMaze.txt"));

            maze.addAll(allLines);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return maze;
    }


    private List<Obstacle> generateObstacles (List<String> maze) {

        List<Obstacle> obstacles = new ArrayList<>();
        for (int i = 0; i < maze.size(); i++){
            for (int j = 0; j < maze.get(j).length(); j++) {
                String character = maze.get(i).split("")[j];
                int x = -100 + (j * 4);
                int y = 196 - (i * 4);
                if (Objects.equals(character, "c")){
                    Obstacle obstacle = new SquareObstacle(x, y);
                    obstacles.add(obstacle);
                }
            }
        }

        return obstacles;
    }

//    private char[][] maze;
//
//    public DesignedMaze(String filename) throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader(filename));
//        String line = reader.readLine();
//        int rows = 0;
//        int cols = 0;
//        while (line != null) {
//            rows++;
//            if (line.length() > cols) {
//                cols = line.length();
//            }
//            line = reader.readLine();
//        }
//        reader.close();
//
//        maze = new char[rows][cols];
//        reader = new BufferedReader(new FileReader(filename));
//        int row = 0;
//        while ((line = reader.readLine()) != null) {
//            for (int col = 0; col < line.length(); col++) {
//                maze[row][col] = line.charAt(col);
//            }
//            row++;
//        }
//        reader.close();
//    }
//
//    public void print() {
//        for (int row = 0; row < maze.length; row++) {
//            for (int col = 0; col < maze[row].length; col++) {
//                System.out.print(maze[row][col]);
//            }
//            System.out.println();
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//        DesignedMaze maze = new DesignedMaze("DesignedMaze.txt");
//        maze.print();
//    }
}