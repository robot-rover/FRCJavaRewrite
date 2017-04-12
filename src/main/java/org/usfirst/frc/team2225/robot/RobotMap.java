package org.usfirst.frc.team2225.robot;

import edu.wpi.first.wpilibj.SPI;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    //used for inverting robot drive direction

    //physical measurements
    public static double robotWidth = 22;
    public static double wheelRadius = 3;

    //ports
    public static int leftMotor = 6;
    public static int rightMotor = 5;

    public static SPI.Port gyroPort = SPI.Port.kOnboardCS0;

}
