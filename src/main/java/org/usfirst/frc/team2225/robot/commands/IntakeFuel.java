package org.usfirst.frc.team2225.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2225.robot.Robot;
import org.usfirst.frc.team2225.robot.subsystems.FuelAquisition;

/**
 *
 */
//todo: add class description
public class IntakeFuel extends Command{
    public IntakeFuel(){
        requires(Robot.fuelAquisition);
    }

    @Override
    protected void execute(){
        Robot.fuelAquisition.aquireFuel(true);
    }

    @Override
    protected void end(){
        Robot.fuelAquisition.reset();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
