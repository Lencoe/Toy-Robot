package za.co.wethinkcode.toyrobot.maze;

import za.co.wethinkcode.toyrobot.world.Obstacle;
import za.co.wethinkcode.toyrobot.world.SquareObstacle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleMaze extends AbstractMaze {
    public SimpleMaze () {
        SquareObstacle squareObstacle = new SquareObstacle(1, 1);
        List<Obstacle> obstacles = new ArrayList<>(List.of(squareObstacle));
        setObstacles(obstacles);
    }


}

