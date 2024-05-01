package de.wethinkco.robotworlds.protocol.world;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class GridSize {
    private static int height;
    private static int width;

    public GridSize(){

    }

    public GridSize(int height, int width) {
        this.setHeight(height);
        this.setWidth(width);
    }

    public static int getHeight() {
        return height;
    }

    public void setHeight(int height) {

        this.height = height;
    }

    public static int getWidth() {

        return width;
    }

    public void setWidth(int width) {

        this.width = width;
    }

    @Override
    public String toString(){
        return "{height: " + height +
                ", width: " + width + "}";
    }
}
