package de.wethinkco.robotworlds.protocol.world;

public class ServerConfig {
    private GridSize gridSize;
    private RobotVisionField robotVisionField;

    public ServerConfig(){

    }

    public ServerConfig(GridSize gridSize, RobotVisionField robotVisionField){
        this.gridSize = gridSize;
        this.robotVisionField = robotVisionField;
    }

    public ServerConfig(ClientConfig clientConfig) {
        this.gridSize = new GridSize(clientConfig.getGridHeight(), clientConfig.getGridWidth());
        this.robotVisionField = new RobotVisionField(clientConfig.getVisibility());
    }

    public GridSize getGridSize() {

        return gridSize;
    }

    public void setGridSize(GridSize gridSize) {

        this.gridSize = gridSize;
    }

    public RobotVisionField getRobotVisionField() {
        return robotVisionField;
    }

    public void setRobotVisionField(RobotVisionField robotVisionField) {
        this.robotVisionField = robotVisionField;
    }

    @Override
    public String toString(){
        return "gridSize: " + gridSize + "\n"
                + "vision: " + robotVisionField + "\n";
    }
}
