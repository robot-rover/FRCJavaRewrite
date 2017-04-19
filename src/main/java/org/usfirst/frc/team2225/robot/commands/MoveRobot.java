package org.usfirst.frc.team2225.robot.commands;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2225.robot.Robot;
import org.usfirst.frc.team2225.robot.RobotMap;
import org.usfirst.frc.team2225.robot.SidePair;
import org.usfirst.frc.team2225.robot.subsystems.DriveTrain;

/**
 *
 */
//todo: add class description
public class MoveRobot extends Command {
    SidePair<CANTalon> motors;
    SidePair<Double> motorTargets;
    double inchesToMove;
    public MoveRobot(double inchesToMove) {
        requires(Robot.driveTrain);
        this.motors = Robot.driveTrain.motors;
        this.inchesToMove = inchesToMove;
    }

    @Override
    protected void execute() {
        SidePair.dualBiConsume(motors, motorTargets, CANTalon::set);
    }

    @Override
    protected void initialize() {
        Robot.driveTrain.verify(CANTalon.TalonControlMode.Position);
        double moveDisntance = inchesToMove/(RobotMap.wheelRadius*2*Math.PI);
        motorTargets = new SidePair<>(MoveMotor.moveMotor(motors.left, moveDisntance), MoveMotor.moveMotor(motors.left, moveDisntance));
    }

    @Override
    protected boolean isFinished() {
        return MoveMotor.areMotorsDone(motors, motorTargets);
    }
}
