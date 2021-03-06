package org.usfirst.frc.team2225.robot.subsystems;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usfirst.frc.team2225.robot.Resetable;
import org.usfirst.frc.team2225.robot.RobotMap;
import org.usfirst.frc.team2225.robot.SidePair;
import org.usfirst.frc.team2225.robot.commands.MoveMotor;

import java.util.function.DoubleBinaryOperator;

/**
 * The DriveTrain subsystem incorporates the sensors and actuators attached to
 * the robots chassis. These include four drive motors, a left and right encoder
 * and a gyro.
 */

//todo: add better talon mode management
public class DriveTrain extends Subsystem implements Resetable{

    private static Logger log = LoggerFactory.getLogger(DriveTrain.class);

    public SidePair<CANTalon> motors = new SidePair<>(new CANTalon(RobotMap.leftMotor), new CANTalon(RobotMap.rightMotor));

    private int robotDirection = 1;

    private CANTalon.TalonControlMode controlMode;

    private RobotDrive drive = new RobotDrive(motors.left, motors.right);

    private ADXRS450_Gyro gyro = new ADXRS450_Gyro(RobotMap.gyroPort);

    public DriveTrain() {
        //todo: calculate f-gain, cruise velo, and accel for motion magic
        controlMode = CANTalon.TalonControlMode.Position;
        motors.dualConsume((CANTalon motorRef) -> {
            motorRef.changeControlMode(CANTalon.TalonControlMode.Position);
            motorRef.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
            motorRef.configEncoderCodesPerRev(320);
            motorRef.setPosition(0);
            motorRef.set(0);
            motorRef.configNominalOutputVoltage(0.2, 0.2);
            motorRef.configPeakOutputVoltage(0.5, 0.5);
            motorRef.setPID(0.5, 0, 5);
            //motorRef.setF();
            //motorRef.setMotionMagicCruiseVelocity();
            //motorRef.setMotionMagicAcceleration();
        });

        motors.left.setInverted(true);
        motors.left.reverseOutput(false);

        motors.right.setInverted(true);
        motors.right.reverseOutput(true);


        drive.setSafetyEnabled(false);

        //todo: add real LiveWindow Code
        //<editor-fold desc="Commented Out LiveWindow Sample Code">
        /*LiveWindow.addActuator("Drive Train", "Front_Left Motor", (Talon) frontLeftMotor);
        LiveWindow.addActuator("Drive Train", "Back Left Motor", (Talon) rearLeftMotor);
        LiveWindow.addActuator("Drive Train", "Front Right Motor", (Talon) frontRightMotor);
        LiveWindow.addActuator("Drive Train", "Back Right Motor", (Talon) rearRightMotor);
        LiveWindow.addSensor("Drive Train", "Left Encoder", leftEncoder);
        LiveWindow.addSensor("Drive Train", "Right Encoder", rightEncoder);
        LiveWindow.addSensor("Drive Train", "Rangefinder", rangefinder);
        LiveWindow.addSensor("Drive Train", "Gyro", gyro);*/
        //</editor-fold>
    }

    /**
     * When no other command is running let the operator drive around using the
     * PS3 joystick.
     */
    @Override
    public void initDefaultCommand() {

    }

    public int getRobotDirection(){
        return robotDirection;
    }

    public void setRobotDirection(int direction){
        if(direction > 1 || direction < -1){
            log.warn("Attempted to set the robotDirection to {}", direction);
            return;
        }
        robotDirection = direction;
    }



    public SidePair<Double> getPositions(){
        return new SidePair<>(motors.left.getPosition(), motors.right.getPosition());
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
        verify(CANTalon.TalonControlMode.PercentVbus);
        drive.tankDrive(left, right);
    }

    /**
     * Drive a robot arcade style using a joystick
     * @param joy The joystick to use to drive the robot
     */
    public void tankDrive(Joystick joy) {
        verify(CANTalon.TalonControlMode.PercentVbus);
        tankDrive(-joy.getY(), -joy.getAxis(Joystick.AxisType.kThrottle));
    }

    /**
     * Arcade Style Driving for the Drivetrain
     * @param forward Forward Speed in range [-1,1]
     * @param sideways Turning Speed in range [-1,1]
     */
    public void arcadeDrive(double forward, double sideways){
        verify(CANTalon.TalonControlMode.PercentVbus);
        drive.arcadeDrive(forward, sideways);
    }

    /**
     * Drive a robot arcade style using a joystick
     * @param joy The joystick use to drive the robot
     */
    public void arcadeDrive(Joystick joy){
        verify(CANTalon.TalonControlMode.PercentVbus);
        arcadeDrive(joy.getY(), joy.getX());
    }

    /**
     * @return The robots heading in degrees.
     */
    public double getHeading() {
        return gyro.getAngle();
    }

    public void setControlMode(CANTalon.TalonControlMode mode){
        motors.dualConsume((CANTalon motorRef) -> {
            motorRef.changeControlMode(mode);
        });
        reset();
    }

    public CANTalon.TalonControlMode getControlMode(){
        return controlMode;
    }

    public void verify(CANTalon.TalonControlMode mode){
        if(controlMode != mode){
            log.warn("ControlMode verification failed. Req: {}, Cur: {}. Switching...", mode, controlMode, new Exception());
            setControlMode(mode);
        }
    }

    /**
     * Reset the robots sensors to the zero states.
     */
    @Override
    public void reset() {
        gyro.reset();
        motors.dualConsume((CANTalon motorRef) -> {
            motorRef.setPosition(0);
            motorRef.set(0);
        });
    }
}