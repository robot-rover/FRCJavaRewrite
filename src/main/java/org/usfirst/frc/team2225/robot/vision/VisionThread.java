package org.usfirst.frc.team2225.robot.vision;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.wpilibj.CameraServer;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team2225.robot.RobotMap;

import java.awt.image.ImageProducer;

/**
 *
 */
//todo: add class description
public class VisionThread implements Runnable {
    UsbCamera camera;
    @Override
    public void run() {
        camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setVideoMode(VideoMode.PixelFormat.kMJPEG, RobotMap.cameraX, RobotMap.cameraY, RobotMap.cameraFPS);

        CvSink cvSink = CameraServer.getInstance().getVideo();
        CvSource outputStream = CameraServer.getInstance().putVideo("Robot Feed", RobotMap.cameraX, RobotMap.cameraY);

        Mat source = new Mat();
        Mat output = new Mat();

        while(!Thread.interrupted()) {
            cvSink.grabFrame(source, 1.0);
            visionAlgorithm(source, output);
            outputStream.putFrame(output);
        }
    }

    private void visionAlgorithm(Mat src, Mat dst){
        src.assignTo(dst);
    }
}
