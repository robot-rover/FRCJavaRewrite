package org.usfirst.frc.team2225.robot.subsystems;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team2225.robot.Resetable;
import org.usfirst.frc.team2225.robot.RobotMap;

/**
 *
 */
//todo: add class description
public class ShooterSystem extends Subsystem implements Resetable{
    public CANTalon ballLauncher = new CANTalon(RobotMap.ballLauncher);
    public Relay agitator = new Relay(RobotMap.agitator);
    public double targetSpeed = 465;

    public ShooterSystem(){
        ballLauncher.changeControlMode(CANTalon.TalonControlMode.Speed);
        ballLauncher.setPID(3,0.006, 2);
    }

    @Override
    public void reset(){
        ballLauncher.setPosition(0);
        ballLauncher.set(0);
        agitator.set(Relay.Value.kOff);
    }

    @Override
    protected void initDefaultCommand() {

    }

    public void setFire(boolean shouldFire){
        if(shouldFire){
            ballLauncher.set(targetSpeed);
            if(ballLauncher.getAnalogInVelocity()>400){
                agitator.set(Relay.Value.kForward);
            } else {
                agitator.set(Relay.Value.kOff);
            }
        } else {
            ballLauncher.set(0);
            agitator.set(Relay.Value.kOff);
        }
    }
}
