package org.usfirst.frc.team2225.robot.commands;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jdk.internal.util.xml.impl.ReaderUTF8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usfirst.frc.team2225.robot.Robot;
import org.usfirst.frc.team2225.robot.SidePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 */
//todo: add class description
public class RecallDrive extends Command {
    private static Logger log = LoggerFactory.getLogger(RecallDrive.class);
    Thread deSerialize;
    String opName;
    //BlockingQueue<double[]> points;

    public RecallDrive(String opName) {
        requires(Robot.driveTrain);
        this.opName = opName;
        //points = new LinkedBlockingQueue<>();
        deSerialize = new Thread(new ReadTask(opName));
    }

    @Override
    protected void initialize() {
        Robot.driveTrain.verify(CANTalon.TalonControlMode.MotionProfile);
        deSerialize.start();
    }

    @Override
    protected boolean isFinished() {
        return deSerialize.isAlive();
    }

    class ReadTask implements Runnable {
        //BlockingQueue<double[]> points;
        String opName;
        public ReadTask(String opName) {
            this.opName = opName;
        }

        private void pushPoint(double[] point, boolean isLastPoint, boolean isFirstPoint){
            SidePair<CANTalon> motors = Robot.driveTrain.motors;
            SidePair<CANTalon.TrajectoryPoint> points = new SidePair<>(new CANTalon.TrajectoryPoint(), new CANTalon.TrajectoryPoint());
            // This mess is what happens when you don't write constructors. Thanks!
            points.left.position = point[0];
            points.left.velocity = point[1];
            points.left.timeDurMs = (int)point[4];
            points.left.isLastPoint = isLastPoint;
            points.left.zeroPos = isFirstPoint;
            points.right.position = point[2];
            points.right.velocity = point[3];
            points.right.timeDurMs = (int)point[4];
            points.right.isLastPoint = isLastPoint;
            points.right.zeroPos = isFirstPoint;
            SidePair.dualBiConsume(motors, points, CANTalon::pushMotionProfileTrajectory);
        }

        @Override
        public void run() {
            File csvFile = new File("./data/" + opName + ".csv");
            if(!csvFile.exists()){
                log.error("csvFile doesn't exist: \"{}\"", csvFile.getAbsolutePath());
                return;
            }
            try(Scanner input = new Scanner(new ReaderUTF8(Files.newInputStream(csvFile.toPath(), StandardOpenOption.READ)))){
                boolean isFirst = true;
                while(input.hasNext()){
                    String line = input.nextLine();
                    String[] parse = line.split(",");
                    if(parse.length != 5){
                        log.warn("Parsed Point: ({}) doesn't have 5 values. Skipping...", line);
                        continue;
                    }
                    double[] point = new double[5];
                    try {
                        for (int i = 0; i < 5; i++) {
                            point[i] = Double.parseDouble(parse[i]);
                        }
                    } catch (NumberFormatException e){
                        log.warn("Parsed Point has invalid double val", e);
                        continue;
                    }
                    pushPoint(point, !input.hasNextLine(), isFirst);
                    isFirst = false;
                }
            } catch (IOException e) {
                log.error("IO Exception parsing csv file: \"{}\"", csvFile.getAbsolutePath(), e);
            }
        }
    }
}
