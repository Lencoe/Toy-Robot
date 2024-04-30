package za.co.wethinkcode.toyrobot;

public class SprintCommand extends Command {
    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());

        if (target.updatePosition(nrSteps)){
            Sprint(target,nrSteps);
        } else {
            target.setStatus("Sorry, I cannot go outside my safe zone.");
        }
        return true;
    }

    public void Sprint(Robot target , int nrSteps ){
        target.setStatus("Moved forward by "+nrSteps+" steps.");
//        target.setHistory("Sprint "+nrSteps);

        if (nrSteps == 1){
            return;
        }else
            target.setStatus("Moved forward by "+nrSteps+" steps.");
            System.out.println(target.toString());
            nrSteps = nrSteps-1;
            target.updatePosition(nrSteps);
            Sprint(target,nrSteps);
    }
    public SprintCommand(String argument) {
        super("sprint", argument);
    }
}


