package za.co.wethinkcode.toyrobot.world;

import java.util.ArrayList;
import java.util.Random;

public class Obstacles {

    private  static  ArrayList<SquareObstacle> obstacles = new ArrayList<>();
    public void generateObstacles(){

        Random random = new Random();

        int randomNumber = random.nextInt(10);

        for(int i = 0; i < randomNumber; i++){

            int x = random.nextInt(395)-200;
            int y = random.nextInt(195)-100;
            SquareObstacle squareObstacle = new SquareObstacle(x,y);
            obstacles.add(squareObstacle);
        }
    }

    public ArrayList<SquareObstacle> getObstacles() {
        return obstacles;
    }
}
