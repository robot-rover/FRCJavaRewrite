package org.usfirst.frc.team2225.robot;

import com.ctre.CANTalon;
import org.usfirst.frc.team2225.robot.subsystems.DriveTrain;

/**
 *
 *
 */
//todo: fill out header
public class SidePair<T>{

    public enum Side{
        Left, Right
    }

    public T left;
    public T right;
    public SidePair(T left, T right){
        this.left = left;
        this.right = right;
    }

    public T getSide(Side side){
        if(side == Side.Left)
            return left;
        else
            return right;
    }
}
