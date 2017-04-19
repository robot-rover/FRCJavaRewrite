package org.usfirst.frc.team2225.robot;

import edu.wpi.first.wpilibj.SPI;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

    //physical measurements
    /**
     * Width of robot in inches
     */
    public static double robotWidth = 22;

    /**
     * radius of drive wheels in inches
     */
    public static double wheelRadius = 3;

    //Closed Loop Finish Constants
    /**
     * speed in rpm that is considered "stopped"
     */
    public static int stoppedSpeed = 1;

    /**
     * distance in native encoder units that is considered "on target"
     */
    public static int closedLoopTolerance = 10;

    //Camera Settings
    public static int cameraX = 160;
    public static int cameraY = 120;
    public static int cameraFPS = 10;

    // ---Start Ports ---

    //CAN IDs
    public static int leftMotor = 6;
    public static int rightMotor = 5;

    //SPI Port Addresses
    public static SPI.Port gyroPort = SPI.Port.kOnboardCS0;

    //PWM Addresses
    public static int ballLauncher = 3;
    public static int ballSucker = 1;
    public static int winch = 2;

    //Relay Port Addresses
    public static int agitator = 0;
    public static int elevator = 1;

}
