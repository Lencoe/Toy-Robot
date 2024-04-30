package za.co.wethinkcode.toyrobot;

public class Mazerun extends Command{
    public Mazerun() {
        super("mazerun");
    }

    @Override
    public boolean execute(Robot target) {

        target.updatePosition(200);
        target.setStatus("I am at the top edge. (Cost: 2 steps)");
        return true;
    }
}
