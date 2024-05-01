package de.wethinkco.robotworlds.protocol.world;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class RobotVisionField {

    private static int visibility;

    public RobotVisionField(){

    }
    public RobotVisionField(int visibility){
        this.setVisibility(visibility);
    }

    public static int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString(){
        return Integer.toString(visibility);
    }
}
