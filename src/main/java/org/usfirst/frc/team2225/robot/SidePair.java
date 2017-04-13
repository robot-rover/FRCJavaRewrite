package org.usfirst.frc.team2225.robot;

import com.ctre.CANTalon;
import org.usfirst.frc.team2225.robot.subsystems.DriveTrain;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public void dualConsume(Consumer<T> consumer){
        consumer.accept(left);
        consumer.accept(right);
    }

    public <R> SidePair<R> dualFunction(Function<T,R> function){
        return new SidePair<R>(function.apply(left), function.apply(right));
    }

    public static <T, U> void dualBiConsume(SidePair<T> pair1, SidePair<U> pair2, BiConsumer<T, U> biConsumer){
        biConsumer.accept(pair1.left, pair2.left);
        biConsumer.accept(pair1.right, pair2.right);
    }
}
