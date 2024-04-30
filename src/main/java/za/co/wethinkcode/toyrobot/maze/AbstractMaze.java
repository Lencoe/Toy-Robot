package za.co.wethinkcode.toyrobot.maze;

import za.co.wethinkcode.toyrobot.Position;
import za.co.wethinkcode.toyrobot.world.Obstacle;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMaze implements Maze{

    private List<Obstacle> obstacles = new ArrayList<>();


    public void setObstacles(List<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }
    @Override
    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }

    @Override
    public boolean blocksPath(Position a, Position b) {
        return false;
    }

}

