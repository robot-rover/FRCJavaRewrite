package org.usfirst.frc.team2225.robot.commands;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2225.robot.Robot;
import org.usfirst.frc.team2225.robot.RobotMap;
import org.usfirst.frc.team2225.robot.SidePair;

/**
 *
 */
//todo: add class description
public class TurnRobot extends Command {
    double degreesToRotate;
    SidePair.Side direction;
    SidePair<Double> motorTargets;
    SidePair<CANTalon> motors;
    public TurnRobot(double degreesToRotate, SidePair.Side direction){
        this.degreesToRotate = degreesToRotate;
        this.direction = direction;
        this.motors = Robot.driveTrain.motors;
    }

    @Override
    protected void execute() {
        SidePair.dualBiConsume(motors, motorTargets, CANTalon::set);
    }

    @Override
    protected void initialize() {
        Robot.driveTrain.verify(CANTalon.TalonControlMode.Position);
        //arc length = radians * radius
        double radians = degreesToRotate/360*2*Math.PI;
        double distanceToMoveSide = radians* RobotMap.robotWidth;
        double wheelCircumference = RobotMap.wheelRadius*2*Math.PI;
        double moveDistance = distanceToMoveSide/wheelCircumference;
        if(direction == SidePair.Side.Left){
            motorTargets =  new SidePair<>(MoveMotor.moveMotor(motors.left, moveDistance), 0.0);
        } else {
            motorTargets = new SidePair<>(0.0, MoveMotor.moveMotor(motors.right, moveDistance));
        }
    }

    @Override
    protected boolean isFinished() {
        return MoveMotor.areMotorsDone(motors, motorTargets);
    }
}
