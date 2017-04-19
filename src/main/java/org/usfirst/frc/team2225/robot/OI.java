package org.usfirst.frc.team2225.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.usfirst.frc.team2225.robot.commands.IntakeFuel;
import org.usfirst.frc.team2225.robot.commands.InvertRobot;
import org.usfirst.frc.team2225.robot.commands.RunWinch;
import org.usfirst.frc.team2225.robot.commands.UnjamSystems;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    private static class AxisID {
        final static int LEFT_STICK_X = 1, LEFT_STICK_Y = 2, RIGHT_STICK_X = 3, RIGHT_STICK_Y = 5;
    }
    private static class JoystickID {
        final static int PRIMARY_JOY = 1, SECONDARY_JOY = 2;
    }
    private static class ButtonID {
        final static int A_BUTTON = 1, B_BUTTON = 2, X_BUTTON = 3, Y_BUTTON = 4,
                LB = 5, RB = 6, SELECT = 7, START = 8, LEFT_JOY_CLICK = 9, RIGHT_JOY_CLICK = 10;
    }
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.
    public Joystick stick;
    public OI() {
        stick = new Joystick(0);

        Button aButton = new JoystickButton(stick, ButtonID.A_BUTTON);
        Button bButton = new JoystickButton(stick, ButtonID.B_BUTTON);
        Button xButton = new JoystickButton(stick, ButtonID.X_BUTTON);
        Button yButton = new JoystickButton(stick, ButtonID.Y_BUTTON);
        Button startButton = new JoystickButton(stick, ButtonID.START);
        //todo: test pov button axis (4 or 5?)
        //Button rightPOV = null;

        //Button Bindings
        aButton.whileHeld(new IntakeFuel());
        xButton.whileHeld(new RunWinch(0.5));
        yButton.whileHeld(new RunWinch(1));
        startButton.whenPressed(new InvertRobot());
        //rightPOV.whileHeld(new UnjamSystems());
    }
}
