package za.co.wethinkcode.toyrobot.maze;

import za.co.wethinkcode.toyrobot.world.Obstacle;

import java.util.ArrayList;
import java.util.List;

public class EmptyMaze extends AbstractMaze{
    public EmptyMaze () {
        List<Obstacle> obstacles = new ArrayList<>();
        setObstacles(obstacles);
    }

}

