package org.usfirst.frc.team2225.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.usfirst.frc.team2225.robot.Robot;

/**
 *
 */
//todo: add class description
public class InvertRobot extends InstantCommand {
    @Override
    protected void execute() {
        Robot.driveTrain.robotDirection = -Robot.driveTrain.robotDirection;
    }
}
