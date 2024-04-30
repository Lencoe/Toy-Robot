package za.co.wethinkcode.toyrobot;

public class RightCommand extends Command {
    @Override
    public boolean execute(Robot target) {

        target.setStatus("Turned right.");
        target.updateDirection("right");
//            target.setHistory("Right");

        return true;
    }

    public RightCommand() {
        super("Right");
    }
}

