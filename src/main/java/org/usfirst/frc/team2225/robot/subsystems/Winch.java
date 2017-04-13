package org.usfirst.frc.team2225.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team2225.robot.Resetable;
import org.usfirst.frc.team2225.robot.RobotMap;

/**
 *
 */
//todo: add class description
public class Winch extends Subsystem implements Resetable {
    public SpeedController winch = new VictorSP(RobotMap.winch);

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    public void reset() {
        winch.set(0);
    }

    public void setWinch(double speed){
        winch.set(speed);
    }
}
