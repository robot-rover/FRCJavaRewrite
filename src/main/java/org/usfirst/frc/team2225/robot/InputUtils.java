package org.usfirst.frc.team2225.robot;

/**
 * @author Sam
 */
public class InputUtils {
    static double controllerDeadzone(double input, double deadzone, double nominalThrottle){
        if(input < deadzone && input > -deadzone)
            return 0F;
        double sign = Math.signum(input);
        return input*(1-nominalThrottle) + sign * nominalThrottle;
    }

    static double squareNoSignChange(double x, double y){
        return Math.pow(Math.abs(x),y)*Math.signum(x);
    }
}
