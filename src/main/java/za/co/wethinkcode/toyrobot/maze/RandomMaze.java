package za.co.wethinkcode.toyrobot.maze;

import za.co.wethinkcode.toyrobot.world.Obstacle;
import za.co.wethinkcode.toyrobot.world.Obstacles;
import za.co.wethinkcode.toyrobot.world.SquareObstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMaze extends AbstractMaze{

    public RandomMaze () {
        List<Obstacle> obstacles = obstacles();
        setObstacles(obstacles);
    }

   public List<Obstacle> obstacles () {
       Random random = new Random();
       List<Obstacle> obstacles = new ArrayList<>();
       int randomNum = random.nextInt(20);
       for (int i = 0; i < randomNum; i++){
           SquareObstacle squareObstacle = new SquareObstacle(random.nextInt(195) - 100, random.nextInt(395) - 200);
           obstacles.add(squareObstacle);
       }
       return obstacles;
   }

}
