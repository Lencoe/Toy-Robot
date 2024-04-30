package za.co.wethinkcode.toyrobot;

import java.util.*;

public class ReplayCommand extends Command{


    @Override
    public boolean execute(Robot target) {

        ArrayList<String> history = target.getHistory();
//        System.out.println(Command.getArguments()[1]);
        int initialSize = history.size();



        if (Command.getArguments().length < 2) {

            for (int i = 0; i < history.size(); i++) {

                target.handleCommand(Command.create(history.get(i)));
                System.out.println(target);
                if (i >= initialSize - 1) {
                    break;
                }

            }
            target.setStatus("replayed " + initialSize + " commands.");
        }
        else if (Command.getArguments().length == 3 && Objects.equals(Command.getArguments()[1], "reversed") && Command.getArguments()[2].matches("^\\d-\\d")){
//           replay reversed range
            String[] arguments =  Command.getArguments()[2].toLowerCase().trim().split("-");
            List<String> sublist = history.subList(initialSize-Integer.parseInt(arguments[0]), initialSize-Integer.parseInt(arguments[1]));
            ArrayList<String> newHistory = new ArrayList<>(sublist);
            Collections.reverse(newHistory);
            for (int i = 0; i <newHistory.size(); i++){
                target.handleCommand(Command.create(newHistory.get(i)));
                System.out.println(target);
            }

            target.setStatus("replayed " + (Integer.parseInt(arguments[0])-Integer.parseInt(arguments[1])) + " commands.");

        }
        else if (Command.getArguments().length == 3 && Objects.equals(Command.getArguments()[1], "reversed") && Integer.parseInt(Command.getArguments()[2]) > 0){
//            replay reversed number
            List<String> sublist = history.subList(initialSize-Integer.parseInt(Command.getArguments()[2]), initialSize);
            ArrayList<String> newHistory = new ArrayList<>(sublist);
            Collections.reverse(newHistory);
            for (int i = 0; i <newHistory.size(); i++){
                target.handleCommand(Command.create(newHistory.get(i)));
                System.out.println(target);
            }

            target.setStatus("replayed " + (Integer.parseInt(Command.getArguments()[2])) + " commands.");

        }




        else if (Objects.equals(Command.getArguments()[0], "replay") && Objects.equals(Command.getArguments()[1], "reversed")){
//          replay reversed NORMAL
            Collections.reverse(history);
            for (int i = 0; i < history.size(); i++) {
                target.handleCommand(Command.create(history.get(i)));
                System.out.println(target);
                if (i >= initialSize - 1) {
                    break;
                }

            }
            target.setStatus("replayed " + initialSize + " commands.");

        }

//
        else if (Command.getArguments().length > 1){

            if (Command.getArguments()[1].matches("^\\d-\\d")){
//              replay range
                String[] arguments =  Command.getArguments()[1].toLowerCase().trim().split("-");
                for (int i = 0; i < history.subList(initialSize-Integer.parseInt(arguments[0]), initialSize-Integer.parseInt(arguments[1])).size(); i++){
//
                    target.handleCommand(Command.create(history.subList(initialSize-Integer.parseInt(arguments[0]), initialSize-Integer.parseInt(arguments[1])).get(i)));
                    System.out.println(target);
//                    if (i >= initialSize - Integer.parseInt(arguments[1])-1-1){
//                        break;
//                    }

                }
                target.setStatus("replayed "+(Integer.parseInt(arguments[0])-Integer.parseInt(arguments[1]))+" commands.");
            }
            else {

                for (int i = 0; i < history.size(); i++) {
//                  replay normal
                    target.handleCommand(Command.create(history.get(initialSize - Integer.parseInt(Command.getArguments()[1]) + i)));
                    System.out.println(target);
                    if (i >= Integer.parseInt(Command.getArguments()[1]) - 1) {
                        break;
                    }

                }
                target.setStatus("replayed " + Command.getArguments()[1] + " commands.");
            }
        }
        return true;
    }

    public ReplayCommand() {
        super("replay");

    }

    public ReplayCommand(String[] argms) {
        super("replay", argms);
    }
}

