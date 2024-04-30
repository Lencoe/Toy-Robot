package za.co.wethinkcode.toyrobot;

import za.co.wethinkcode.toyrobot.maze.EmptyMaze;
import za.co.wethinkcode.toyrobot.world.AbstractWorld;
import za.co.wethinkcode.toyrobot.world.TextWorld;

public class LeftCommand extends Command {
    @Override
    public boolean execute(Robot target) {

        target.setStatus("Turned left.");
        target.updateDirection("left");
//        target.setHistory("left");

        return true;
    }

    public LeftCommand() {
        super("Left");
    }
}

