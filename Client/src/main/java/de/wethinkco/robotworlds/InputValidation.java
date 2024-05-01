package de.wethinkco.robotworlds;

import java.util.Arrays;
import java.util.Locale;

public class InputValidation {

    public static boolean checkIpAndPort(String ip, String port){

        if (ip.contains(".")) {
            String[] splitIP = ip.split("\\.");
            if (splitIP.length == 4) {
                for (String s : splitIP) {
                    try {
                        Integer.parseInt(s);
                        if (-1 > Integer.parseInt(s) && Integer.parseInt(s) > 256) {
                            System.out.println("Invalid IP format. Correct example: 192.168.0.1. " +
                                    "Ask the host for the correct IP.");
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid IP format. Correct example: 192.168.0.1. " +
                                "Ask the host for the correct IP.");
                        return false;
                    }
                }
            }
        } else {
            System.out.println("Invalid IP format. Correct example: 192.168.0.1. " +
                    "Ask the host for the correct IP.");
            return false;
        }

        try {
            if (port.length() == 0 || port.length() >= 5){
                System.out.println("Incorrect port length. Must be 0 < PORT < 5.");
                return false;
            }
        } catch (NumberFormatException ignored){
            System.out.println("Port must only contain numbers.");
            return false;
        }

        return true;
    }

    public static void outputMakes(){
        System.out.println("Spy - Close ranged robot that is allowed " +
                "to take up to 5 shots all dealing 1 damage each.");
        System.out.println("Runner - Another close ranged robot that is allowed to take up " +
                "to 4 shots before reloading, all dealing 2 damage each.");
        System.out.println("Assault - A medium ranged, all rounder robot that is allowed " +
                "to take up to 3 shots, all dealing 3 damage each.");
        System.out.println("Heavy - A medium-close ranged robot that is allowed " +
                "to take up to 2 shots, both dealing 4 damage each.");
        System.out.println("Sniper - A long ranged robot that is only allowed " +
                "to take one shot, dealing 5 damage. " +
                "If you choose this robot, it is advised to keep your distance, since you will have to reload often.");
        System.out.println();
    }
}
