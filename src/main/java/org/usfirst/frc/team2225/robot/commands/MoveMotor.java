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
public class MoveMotor extends Command {
    CANTalon motorRef;
    double unitsToMove;
    double targetPosition;
    public MoveMotor(CANTalon motorRef, double unitsToMove) {
        requires(Robot.driveTrain);
        this.motorRef = motorRef;
        this.unitsToMove = unitsToMove;
    }

    @Override
    protected void initialize() {
        targetPosition = moveMotor(motorRef, unitsToMove);
    }

    @Override
    protected void execute() {
        motorRef.set(targetPosition);
    }

    /**
     * This method converts a relative distance to an absolute position for the motor
     * This method does <b>NOT</b> actually move the motor!
     * @param motorRef the motor to get absolute position for
     * @param unitsToMove rotations to move in degrees
     * @return the absolute position to move to in order to apply unitsToMove
     */
    public static double moveMotor(CANTalon motorRef, double unitsToMove){
        boolean invert = motorRef.getInverted();
        double position = motorRef.getPosition() * (invert ? -1 : 1);
        return position + unitsToMove * Robot.driveTrain.getRobotDirection();
    }

    /**
     * Tell when a motor has arrived at its target
     * @param motorRef the reference to the motor controller
     * @param target the target absolute position in Rotations
     * @param tolerance the Allowable closed loop error in Native Units
     * @return if the motor is at the target position
     */
    public static boolean isMotorAtTarget(CANTalon motorRef, double target, double tolerance){
        return target - tolerance < motorRef.getPosition() && target + tolerance > motorRef.getPosition();
    }

    public static boolean isMotorAtTarget(CANTalon motorRef, double target){
        return isMotorAtTarget(motorRef, target, RobotMap.closedLoopTolerance);
    }

    public static boolean areMotorsAtTarget(SidePair<CANTalon> motors, SidePair<Double> target, double tolerance){
        return isMotorAtTarget(motors.left, target.left, tolerance) && isMotorAtTarget(motors.right, target.right, tolerance);
    }

    public static boolean areMotorsAtTarget(SidePair<CANTalon> motors, SidePair<Double> target){
        return areMotorsAtTarget(motors, target, RobotMap.closedLoopTolerance);
    }

    /**
     * Tell when a motor has stopped at its target
     * @param motorRef the reference to the motor controller
     * @param target the target absolute position in Rotations
     * @param tolerance the Allowable closed loop error in Native Units
     * @param maxSpeed the maximum speed in rpm to consider "stopped"
     * @return if the motor is stopped at the target position
     */
    public static boolean isMotorDone(CANTalon motorRef, double target, double tolerance, double maxSpeed){
        return isMotorAtTarget(motorRef, target, tolerance) && Math.abs(motorRef.getSpeed()) < maxSpeed;
    }

    public static boolean isMotorDone(CANTalon motorRef, double target){
        return isMotorDone(motorRef, target, RobotMap.closedLoopTolerance, RobotMap.stoppedSpeed);
    }

    public static boolean areMotorsDone(SidePair<CANTalon> motors, SidePair<Double> target, double tolerance, double maxSpeed){
        return areMotorsAtTarget(motors, target, tolerance) && Math.abs(motors.left.getSpeed()) < maxSpeed && Math.abs(motors.right.getSpeed()) < maxSpeed;
    }

    public static boolean areMotorsDone(SidePair<CANTalon> motors, SidePair<Double> target){
        return areMotorsDone(motors, target, RobotMap.closedLoopTolerance, RobotMap.stoppedSpeed);
    }

    @Override
    protected boolean isFinished() {
        return isMotorDone(motorRef, targetPosition);
    }
}
