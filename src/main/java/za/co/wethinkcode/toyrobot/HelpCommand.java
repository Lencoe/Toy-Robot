package za.co.wethinkcode.toyrobot;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help");
    }

    @Override
    public boolean execute(Robot target) {
        target.setStatus("I can understand these commands:\n" +
                "OFF  - Shut down robot\n" +
                "HELP - provide information about commands\n" +
                "FORWARD - move forward by specified number of steps, e.g. 'FORWARD 10'" +
                "BACKWARD - move backward by specified number of steps, e.g. 'BACKWARD 10'" +
                "LEFT- turn Left'" +
                "RIGHT turn Right"+
                "Sprint ");

        return true;
    }
}

