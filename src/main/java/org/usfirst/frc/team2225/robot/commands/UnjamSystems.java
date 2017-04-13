package org.usfirst.frc.team2225.robot.commands;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2225.robot.Robot;

/**
 *
 */
//todo: add class description
public class UnjamSystems extends Command {
    public UnjamSystems(){
        requires(Robot.fuelAquisition);
        requires(Robot.shooterSystem);
    }

    @Override
    protected void execute(){
        Robot.fuelAquisition.elevator.set(Relay.Value.kReverse);
        Robot.shooterSystem.agitator.set(Relay.Value.kReverse);
    }

    @Override
    protected void end(){
        Robot.fuelAquisition.reset();
        Robot.shooterSystem.reset();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
