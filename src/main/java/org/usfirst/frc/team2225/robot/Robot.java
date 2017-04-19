
package org.usfirst.frc.team2225.robot;

import com.ctre.CANTalon;
import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2225.robot.commands.ArcadeDrive;
import org.usfirst.frc.team2225.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2225.robot.subsystems.FuelAquisition;
import org.usfirst.frc.team2225.robot.subsystems.ShooterSystem;
import org.usfirst.frc.team2225.robot.subsystems.Winch;
import org.usfirst.frc.team2225.robot.vision.VisionThread;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static DriveTrain driveTrain;
	public static FuelAquisition fuelAquisition;
	public static ShooterSystem shooterSystem;
	public static Winch winch;
	public static OI oi;
	public static Thread visionThread;
	public static Gson gson;
	public static boolean isPrettyPrinting = false;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
	    GsonBuilder builder = new GsonBuilder();
	    if(isPrettyPrinting)
	        builder.setPrettyPrinting();
	    gson = builder.create();
        driveTrain = new DriveTrain();
        fuelAquisition = new FuelAquisition();
        shooterSystem = new ShooterSystem();
        winch = new Winch();
        oi = new OI();
		chooser.addDefault("Default Auto", null);
		SmartDashboard.putData("Auto mode", chooser);
		visionThread = new Thread(new VisionThread());
		visionThread.start();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
	    Robot.driveTrain.motors.dualConsume((CANTalon motorRef) -> {
	    	motorRef.changeControlMode(CANTalon.TalonControlMode.Position);
			motorRef.setPosition(0);
	    	motorRef.set(0);
		});
		autonomousCommand = chooser.getSelected();

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
		else
			SmartDashboard.putString("Error", "No Autonomous Selected");
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		/* This makes sure that the autonomous stops running when
        teleop starts running. If you want the autonomous to
        continue until interrupted by another command, remove
        this line or comment it out. */
		if (autonomousCommand != null)
			autonomousCommand.cancel();
        Robot.driveTrain.motors.dualConsume((CANTalon motorRef) -> {
        	motorRef.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        	motorRef.setPosition(0);
        	motorRef.set(0);
        });
        new ArcadeDrive().start();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
