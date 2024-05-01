package de.wethinkco.robotworlds.protocol.world;

public class ClientConfig {
    private int gridHeight;
    private int gridWidth;

    private int visibility;

    public int getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVision(int vision) {
        this.visibility = vision;
    }

    public ClientConfig(){
        this.gridHeight = GridSize.getHeight();
        this.gridWidth = GridSize.getWidth();
        this.visibility = RobotVisionField.getVisibility();
    }

    @Override
    public String toString(){
        return "gridSize: " + gridHeight + ", " + gridWidth + "\n"
                + "vision: " + visibility + "\n";
    }
}
