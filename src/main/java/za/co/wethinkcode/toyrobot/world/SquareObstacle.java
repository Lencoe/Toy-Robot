package za.co.wethinkcode.toyrobot.world;

import za.co.wethinkcode.toyrobot.Position;

public class SquareObstacle implements Obstacle{


    private int x;
    private int y;


    public SquareObstacle(int x, int y){

        this.x =x;
        this.y =y;


    }
    @Override
    public int getBottomLeftX() {
        return x;
    }

    @Override
    public int getBottomLeftY() {
        return y;
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    public boolean blocksPosition(Position position) {
         int positionX = position.getX();
         int positionY = position.getY();
         boolean matchesX = false;
         boolean matchesY = false;



         for(int i = getBottomLeftX() ; i <= getSize();i++){
             if(i == positionX){
                 matchesX = true;
                 break;
             }

         }
        for(int i = getBottomLeftY() ; i <= getSize();i++){
            if(i == positionY){
                matchesY = true;
                break;
            }

        }

        if(matchesX && matchesY){

            return true;
        }

        return false;

    }

    @Override
    public boolean blocksPath(Position a, Position b) {

        return  (a.getX() == b.getX() && (y > a.getY() && y < b.getY() || y < a.getY() && y > b.getY())) || (a.getY() == b.getY() && (x > a.getX() && x < b.getX() || x < a.getX() && x > b.getX()));

    }

}

