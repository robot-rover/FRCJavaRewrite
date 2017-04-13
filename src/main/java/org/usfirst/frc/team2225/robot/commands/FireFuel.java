package org.usfirst.frc.team2225.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2225.robot.Robot;

/**
 *
 */
//todo: add class description
public class FireFuel extends Command {
    public FireFuel() {
        requires(Robot.shooterSystem);
    }

    @Override
    protected void execute() {
        Robot.shooterSystem.setFire(true);
    }

    @Override
    protected void end() {
        Robot.shooterSystem.reset();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
