package za.co.wethinkcode.toyrobot;

public class BackwardCommand extends Command {

        @Override
        public boolean execute(Robot target) {
            int nrSteps = Integer.parseInt(getArgument());
            int newSteps = nrSteps - (nrSteps*2);
            //EmptyMaze maze = new EmptyMaze();
            //AbstractWorld textWorld = new TextWorld(maze);
            if (target.updatePosition(newSteps)){
                target.setStatus("Moved back by "+nrSteps+" steps.");
//                target.setHistory("back "+nrSteps);
            } else {
                target.setStatus("Sorry, I cannot go outside my safe zone.");
            }
            return true;
        }

        public BackwardCommand(String argument) {
            super("back", argument);
        }
    }


