package org.usfirst.frc.team2225.robot.subsystems;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team2225.robot.RobotMap;
import org.usfirst.frc.team2225.robot.SidePair;

/**
 * The DriveTrain subsystem incorporates the sensors and actuators attached to
 * the robots chassis. These include four drive motors, a left and right encoder
 * and a gyro.
 */
public class DriveTrain extends Subsystem {
    CANTalon leftMotor = new CANTalon(RobotMap.leftMotor);
    CANTalon rightMotor = new CANTalon(RobotMap.rightMotor);
    SidePair<CANTalon> motors = new SidePair<>(leftMotor, rightMotor);

    int robotDirection = 1;

    RobotDrive drive = new RobotDrive(leftMotor, rightMotor);

    ADXRS450_Gyro gyro = new ADXRS450_Gyro(RobotMap.gyroPort);

    public DriveTrain() {
        super();

        // Encoders may measure differently in the real world and in
        // simulation. In this example the robot moves 0.042 barleycorns
        // per tick in the real world, but the simulated encoders
        // simulate 360 tick encoders. This if statement allows for the
        // real robot to handle this difference in devices.
        leftMotor.setInverted(true);
        leftMotor.changeControlMode(CANTalon.TalonControlMode.Position);
        leftMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        leftMotor.reverseOutput(false);
        leftMotor.configEncoderCodesPerRev(320);

        rightMotor.setInverted(true);
        rightMotor.changeControlMode(CANTalon.TalonControlMode.Position);
        rightMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        rightMotor.reverseOutput(true);
        rightMotor.configEncoderCodesPerRev(320);

        drive.setSafetyEnabled(false);

        //todo: add real LiveWindow Code
        /*LiveWindow.addActuator("Drive Train", "Front_Left Motor", (Talon) frontLeftMotor);
        LiveWindow.addActuator("Drive Train", "Back Left Motor", (Talon) rearLeftMotor);
        LiveWindow.addActuator("Drive Train", "Front Right Motor", (Talon) frontRightMotor);
        LiveWindow.addActuator("Drive Train", "Back Right Motor", (Talon) rearRightMotor);
        LiveWindow.addSensor("Drive Train", "Left Encoder", leftEncoder);
        LiveWindow.addSensor("Drive Train", "Right Encoder", rightEncoder);
        LiveWindow.addSensor("Drive Train", "Rangefinder", rangefinder);
        LiveWindow.addSensor("Drive Train", "Gyro", gyro);*/
    }

    /**
     * When no other command is running let the operator drive around using the
     * PS3 joystick.
     */
    @Override
    public void initDefaultCommand() {
        //todo: set default command
        //setDefaultCommand(new TankDriveWithJoystick());
    }

    /**
     * The log method puts interesting information to the SmartDashboard.
     */
    public void log() {
        //todo: add logging info
    }

    /**
     * Tank style driving for the DriveTrain.
     *
     * @param left
     *            Speed in range [-1,1]
     * @param right
     *            Speed in range [-1,1]
     */
    public void tankDrive(double left, double right) {
        drive.tankDrive(left, right);
    }

    /**
     * Drive a robot arcade style using a joystick
     * @param joy The joystick to use to drive the robot
     */
    public void tankDrive(Joystick joy) {
        tankDrive(-joy.getY(), -joy.getAxis(Joystick.AxisType.kThrottle));
    }

    /**
     * Arcade Style Driving for the Drivetrain
     * @param forward Forward Speed in range [-1,1]
     * @param sideways Turning Speed in range [-1,1]
     */
    public void arcadeDrive(double forward, double sideways){
        drive.arcadeDrive(forward, sideways);
    }

    /**
     * Drive a robot arcade style using a joystick
     * @param joy The joystick use to drive the robot
     */
    public void arcadeDrive(Joystick joy){
        arcadeDrive(joy.getY(), joy.getX());
    }

    /**
     * @return The robots heading in degrees.
     */
    public double getHeading() {
        return gyro.getAngle();
    }

    /**
     * Reset the robots sensors to the zero states.
     */
    public void reset() {
        gyro.reset();
        leftMotor.setPosition(0);
        rightMotor.setPosition(0);
    }

    // Divider -------------------------------
    double MoveMotor(CANTalon motorRef, double unitsToMove){
        boolean invert = motorRef.getInverted();
        double position = motorRef.getPosition() * (invert ? -1 : 1);
        double newPosition = position + unitsToMove * robotDirection;
        motorRef.set(newPosition);
        return newPosition;
    }

    SidePair<Double> TurnRobot(double degreesToRotate, SidePair.Side direction){
        //arc length = radians * radius
        double radians = degreesToRotate/360*2*Math.PI;
        double distanceToMoveSide = radians*RobotMap.robotWidth;
        double wheelCircumference = RobotMap.wheelRadius*2*Math.PI;
        double moveDistance = distanceToMoveSide/wheelCircumference;
        if(direction == SidePair.Side.Left){
            return new SidePair<>(MoveMotor(motors.left, moveDistance), 0.0);
        } else {
            return new SidePair<>(0.0, MoveMotor(motors.right, moveDistance));
        }
    }

    SidePair<Double> MoveRobot(SidePair<CANTalon> motors, double inchesToMove){
        double moveDisntance = inchesToMove/(RobotMap.wheelRadius*2*Math.PI);
        return new SidePair<>(MoveMotor(motors.left, moveDisntance), MoveMotor(motors.left, moveDisntance));
    }
}