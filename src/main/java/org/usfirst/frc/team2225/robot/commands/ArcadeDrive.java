package org.usfirst.frc.team2225.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2225.robot.Robot;

/**
 *
 */
//todo: add class description
public class ArcadeDrive extends Command{
    public ArcadeDrive() {
        requires(Robot.driveTrain);
    }

    double controllerDeadzone(double input, double deadzone, double nominalThrottle){
        if(input < deadzone && input > -deadzone)
            return 0F;
        double sign = Math.signum(input);
        return input*(1-nominalThrottle) + sign * nominalThrottle;
    }

    double squareNoSignChange(double x, double y){
        return Math.pow(Math.abs(x),y)*Math.signum(x);
    }

    //todo: add better controller algorithm4
    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        Robot.driveTrain.arcadeDrive(Robot.oi.stick);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false; // Runs until interrupted
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.driveTrain.tankDrive(0,0);
    }
}
