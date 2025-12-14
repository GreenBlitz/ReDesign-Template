package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.IDs;
import frc.robot.RobotConstants;
import frc.robot.hardware.mechanisms.wpilib.SimpleMotorSimulation;
import frc.robot.hardware.rev.motors.BrushlessSparkMAXMotor;
import frc.robot.hardware.rev.motors.SparkMaxWrapper;
import frc.utils.TimedValue;
import frc.utils.time.TimeUtil;

import java.util.function.Supplier;

public class SparkMaxTestBuilder {

	public static Test buildTest() {
		String logPath = RobotConstants.SUBSYSTEM_LOGPATH_PREFIX + "/Test";

		SparkMaxWrapper sparkMaxWrapper = new SparkMaxWrapper(IDs.SparkMAXIDs.test);

		BrushlessSparkMAXMotor motor = new BrushlessSparkMAXMotor(
			logPath + "/Motor",
			sparkMaxWrapper,
			new SimpleMotorSimulation(new DCMotorSim(LinearSystemId.createDCMotorSystem(DCMotor.getNEO(1), 0.0001, 1 / 1), DCMotor.getNEO(1))),
			new SysIdRoutine.Config()
		);

		TimedValue<Rotation2d> positionTimedValue = new TimedValue<>(
			Rotation2d.fromRotations(sparkMaxWrapper.getEncoder().getPosition()),
			TimeUtil.getCurrentTimeSeconds()
		);

		TimedValue<Rotation2d> velocityTimedValue = new TimedValue<>(
			sparkMaxWrapper.getVelocityAnglePerSecond(),
			TimeUtil.getCurrentTimeSeconds()
		);

		TimedValue<Double> voltageTimedValue = new TimedValue<>(sparkMaxWrapper.getVoltage(), TimeUtil.getCurrentTimeSeconds());

		TimedValue<Double> currentTimedValue = new TimedValue<>(sparkMaxWrapper.getOutputCurrent(), TimeUtil.getCurrentTimeSeconds());


		Supplier<TimedValue<Rotation2d>> positionSignal = () -> {
			positionTimedValue.setValue(Rotation2d.fromRotations(sparkMaxWrapper.getEncoder().getPosition()));
			positionTimedValue.setTimestamp(TimeUtil.getCurrentTimeSeconds());
			return positionTimedValue;
		};

		Supplier<TimedValue<Rotation2d>> velocitySignal = () -> {
			velocityTimedValue.setValue(sparkMaxWrapper.getVelocityAnglePerSecond());
			velocityTimedValue.setTimestamp(TimeUtil.getCurrentTimeSeconds());
			return velocityTimedValue;
		};

		Supplier<TimedValue<Double>> voltageSignal = () -> {
			voltageTimedValue.setValue(sparkMaxWrapper.getVoltage());
			voltageTimedValue.setTimestamp(TimeUtil.getCurrentTimeSeconds());
			return voltageTimedValue;
		};

		Supplier<TimedValue<Double>> currentSignal = () -> {
			currentTimedValue.setValue(sparkMaxWrapper.getOutputCurrent());
			currentTimedValue.setTimestamp(TimeUtil.getCurrentTimeSeconds());
			return currentTimedValue;
		};

		return new Test(logPath, motor, positionSignal, velocitySignal, voltageSignal, currentSignal);
	}

}
