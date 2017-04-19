package org.usfirst.frc.team2225.robot.commands;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2225.robot.Robot;
import org.usfirst.frc.team2225.robot.SidePair;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
//todo: add class description
public class RecordDrive extends Command {
    //Left:{Position, Velocity} + Right:{Position, Velocity} + {Duration}
    ArrayList<double[]> points;
    Thread exec;
    AtomicBoolean isRecording;

    public RecordDrive() {
        points = new ArrayList<>(60);
        isRecording = new AtomicBoolean(true);
        exec = new Thread(new RecordTask(points));
    }

    @Override
    protected void initialize() {
        exec.start();
    }

    @Override
    protected void end() {
        isRecording.set(false);
        try {
            exec.join(500);
        } catch (InterruptedException e) {
            SmartDashboard.putString("DriveRecordError", "Command interrupted Somehow");
        }
        if(exec.isAlive())
            exec.interrupt();
        writeCSV("test");
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    private void writeCSV(String filename) {
        File csvFile = new File("./data/" + filename + ".csv");
        if (csvFile.exists())
            SmartDashboard.putString("RecordWarning", "overwriting old trajectory file");
        try (PrintWriter writeStream = new PrintWriter(Files.newBufferedWriter(csvFile.toPath(), Charset.forName("UTF-8"), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE, StandardOpenOption.WRITE));) {
            for (double[] point : points) {
                writeStream.format("%f,%f,%f,%f,%f\n");
            }
        } catch (IOException e) {
            SmartDashboard.putString("RecordWarning", e.getMessage());
        }
    }

    class RecordTask implements Runnable{
        ArrayList<double[]> points;
        SidePair<CANTalon> motors;
        public RecordTask(ArrayList<double[]> points){
            this.points = points;
            this.motors = Robot.driveTrain.motors;
        }

        @Override
        public void run() {
            points.clear();
            motors.dualConsume((CANTalon motorRef) -> motorRef.setPosition(0));
            points.add(new double[]{0.0, 0.0, 0.0, 0.0, 1.0});
            long lastRecordInMillis = System.currentTimeMillis();
            while (isRecording.get()){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    SmartDashboard.putString("DriveLoggingThread", "Thread Interrupted");
                }
                int duration = (int)(System.currentTimeMillis() - lastRecordInMillis);
                lastRecordInMillis = System.currentTimeMillis();
                points.add(new double[]{motors.left.getPosition(), motors.left.getEncVelocity(), motors.right.getPosition(), motors.right.getEncVelocity(), duration});
            }

        }
    }
}
