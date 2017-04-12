package org.usfirst.frc.team2225.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2225.robot.Robot;

/**
 *
 */
//todo: add class description
public class DisableDrive extends Command{
    public DisableDrive(){
        super();
        setRunWhenDisabled(true);
        requires(Robot.driveTrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        Robot.driveTrain.reset();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }


}
