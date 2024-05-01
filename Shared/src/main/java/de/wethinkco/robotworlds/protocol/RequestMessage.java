package de.wethinkco.robotworlds.protocol;

import de.wethinkco.robotworlds.protocol.robots.Robot;
import de.wethinkco.robotworlds.protocol.world.World;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "command"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BackRequestMessage.class, name = "back"),
        @JsonSubTypes.Type(value = FireRequestMessage.class, name = "fire"),
        @JsonSubTypes.Type(value = ForwardRequestMessage.class, name = "forward"),
        @JsonSubTypes.Type(value = LaunchRequestMessage.class, name = "launch"),
        @JsonSubTypes.Type(value = LookRequestMessage.class, name = "look"),
        @JsonSubTypes.Type(value = ReloadRequestMessage.class, name = "reload"),
        @JsonSubTypes.Type(value = RepairRequestMessage.class, name = "repair"),
        @JsonSubTypes.Type(value = StateRequestMessage.class, name = "state"),
        @JsonSubTypes.Type(value = TurnRequestMessage.class, name = "turn"),
        @JsonSubTypes.Type(value = ShutdownQuitOffRequestMessage.class, name = "shutdown")
})
public abstract class RequestMessage {

    public RequestMessage(){

    }

    public abstract ResponseMessage execute(Robot target, World world);

    public RequestMessage(String robot, String command, List<Object>arguments){
        this.robot = robot;
        this.command = command;
        this.arguments = arguments;
    }
    public static List<Object> getArgs(String[] splitCommand) {
        List<Object> args = new ArrayList<>();
        Object arg;
        for (int i = 1; i <= splitCommand.length-1; i++) {
            try {
                arg = Integer.valueOf(splitCommand[i]);
            } catch (NumberFormatException e) {
                arg = splitCommand[i];
            }
            args.add(arg);
        }
        return args;
    }

    public static RequestMessage createRequest(String robot, String request){
        String[] splitCommands = request.split(" ");
        String command = splitCommands[0];
        List<Object> args = new ArrayList<>();

        if (splitCommands.length > 1) {
            args = getArgs(splitCommands);
        }

        switch (command) {
            case "turn":
                return new TurnRequestMessage(robot, args);
            case "forward":
                return new ForwardRequestMessage(robot, args);
            case "back":
                return new BackRequestMessage(robot, args);
            case "fire":
                return new FireRequestMessage(robot);
            case "look":
                return new LookRequestMessage(robot);
            case "reload":
                return new ReloadRequestMessage(robot);
            case "repair":
                return new RepairRequestMessage(robot);
            case "launch":
                return new LaunchRequestMessage(robot, args);
            case "shutdown":
            case "quit":
            case "off":
                return new ShutdownQuitOffRequestMessage(robot);
            case "state":
                return new StateRequestMessage(robot);
            default:
                System.out.println("Invalid command.");
        }

        return null;
    }

    private String robot;
    private String command;
    private List<Object> arguments = new ArrayList<>();

    public String getRobot() {
        return robot;
    }
    public void setRobot(String robot) {
        this.robot = robot;
    }
    public String getCommand() {
        return command;
    }
    public void setCommand(String command) {
        this.command = command;
    }
    public List<Object> getArguments() {
        return arguments;
    }
    public void setArguments(List<Object> arguments) {
        this.arguments = arguments;
    }

}