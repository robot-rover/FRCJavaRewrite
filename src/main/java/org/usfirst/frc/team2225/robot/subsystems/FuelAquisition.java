package org.usfirst.frc.team2225.robot.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team2225.robot.Resetable;
import org.usfirst.frc.team2225.robot.RobotMap;

/**
 *
 */
//todo: add class description
public class FuelAquisition extends Subsystem implements Resetable {

    public Relay elevator = new Relay(RobotMap.elevator);
    public SpeedController ballSucker = new VictorSP(RobotMap.ballSucker);

    @Override
    public void reset(){
        ballSucker.set(0);
        elevator.set(Relay.Value.kOff);
    }

    public void aquireFuel(boolean shouldAquire){
        if(shouldAquire){
            ballSucker.set(1);
            elevator.set(Relay.Value.kForward);
        } else {
            ballSucker.set(0);
            elevator.set(Relay.Value.kOff);
        }
    }

    @Override
    protected void initDefaultCommand() {

    }
}
