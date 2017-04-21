package org.usfirst.frc.team2225.robot.commands;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usfirst.frc.team2225.robot.Robot;
import org.usfirst.frc.team2225.robot.SidePair;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
//todo: add class description
public class RecordDrive extends Command {
    private static Logger log = LoggerFactory.getLogger(RobotDrive.class);
    //Left:{Position, Velocity} + Right:{Position, Velocity} + {Duration}
    ArrayList<double[]> points;
    Thread serialize;
    AtomicBoolean isRecording;
    String opName;

    public RecordDrive(String opName) {
        points = new ArrayList<>(60);
        isRecording = new AtomicBoolean(true);
        serialize = new Thread(new RecordTask(points));
        this.opName = opName;
    }

    @Override
    protected void initialize() {
        serialize.start();
    }

    @Override
    protected void end() {
        isRecording.set(false);
        try {
            serialize.join(500);
        } catch (InterruptedException e) {
            log.warn("Thread interrupted while waiting for record thread to finish", e);
        }
        if(serialize.isAlive())
            serialize.interrupt();
        writeCSV(opName);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    private void writeCSV(String filename) {
        File csvFile = new File("./data/" + filename + ".csv");
        if (csvFile.exists())
            log.warn("Trajectory file \"{}\" already exists, overwriting...", csvFile.getAbsolutePath());
        try (PrintWriter writeStream = new PrintWriter(Files.newBufferedWriter(csvFile.toPath(), Charset.forName("UTF-8"), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE, StandardOpenOption.WRITE))) {
            for (double[] point : points) {
                writeStream.format("%f,%f,%f,%f,%f\n", point[0], point[1], point[2], point[3], point[4]);
            }
        } catch (IOException e) {
            log.error("IO Exception writing csv file \"{}\"", csvFile.getAbsolutePath());
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
            motors.dualConsume((CANTalon motorRef) -> motorRef.setPosition(0));
            points.add(new double[]{0.0, 0.0, 0.0, 0.0, 1.0});
            long lastRecordInMillis = System.currentTimeMillis();
            while (isRecording.get()){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    log.warn("Drive logging thread interrupted", e);
                    return;
                }
                int duration = (int)(System.currentTimeMillis() - lastRecordInMillis);
                lastRecordInMillis = System.currentTimeMillis();
                points.add(new double[]{motors.left.getPosition(), motors.left.getEncVelocity(), motors.right.getPosition(), motors.right.getEncVelocity(), duration});
            }

        }
    }
}
