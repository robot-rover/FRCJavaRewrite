package org.usfirst.frc.team2225.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team2225.robot.SidePair;
import org.usfirst.frc.team2225.robot.commands.MoveRobot;
import org.usfirst.frc.team2225.robot.commands.TurnRobot;

/**
 *
 */
//todo: add class description
public class CrossBaseline {
    private CrossBaseline(){}

    public static class Left extends CommandGroup {
        public Left(){
            addSequential(new MoveRobot(40));
        }
    }

    public static class Center extends CommandGroup {
        public Center(){
            addSequential(new MoveRobot(12));
            addSequential(new TurnRobot(60, SidePair.Side.Right));
            addSequential(new MoveRobot(100));
            addSequential(new TurnRobot(72, SidePair.Side.Left));
            addSequential(new MoveRobot(30));

        }
    }

    public static class Right extends CommandGroup {
        public Right(){
            addSequential(new MoveRobot(40));
        }
    }
}
