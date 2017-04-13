package org.usfirst.frc.team2225.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2225.robot.Robot;
import org.usfirst.frc.team2225.robot.subsystems.Winch;

/**
 *
 */
//todo: add class description
public class RunWinch extends Command {
    private double speed;
    public RunWinch(double speed) {
        requires(Robot.winch);
        this.speed = speed;
    }

    @Override
    protected void execute() {
        Robot.winch.setWinch(speed);
    }

    @Override
    protected void end() {
        Robot.winch.reset();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
