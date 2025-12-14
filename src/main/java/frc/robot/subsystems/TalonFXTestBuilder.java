package frc.robot.subsystems;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.IDs;
import frc.robot.RobotConstants;
import frc.robot.hardware.mechanisms.wpilib.SimpleMotorSimulation;
import frc.robot.hardware.phoenix6.motors.TalonFXMotor;
import frc.utils.TimedValue;
import frc.utils.time.TimeUtil;

import java.util.function.Supplier;

public class TalonFXTestBuilder {

	public static Test buildTest() {
		String logPath = RobotConstants.SUBSYSTEM_LOGPATH_PREFIX + "/Test";

		TalonFXMotor motor = new TalonFXMotor(
			logPath + "/Motor",
			IDs.TalonFXIDs.test,
			new SysIdRoutine.Config(),
			new SimpleMotorSimulation(new DCMotorSim(LinearSystemId.createDCMotorSystem(DCMotor.getNEO(1), 0.0001, 1 / 1), DCMotor.getNEO(1)))
		);

		StatusSignal<Angle> rawPositionSignal = motor.getDevice().getPosition(true);
		StatusSignal<AngularVelocity> rawVelocitySignal = motor.getDevice().getVelocity(true);
		StatusSignal<Voltage> rawVoltageSignal = motor.getDevice().getMotorVoltage(true);
		StatusSignal<Current> rawCurrentSignal = motor.getDevice().getStatorCurrent(true);

		rawPositionSignal.setUpdateFrequency(RobotConstants.DEFAULT_SIGNALS_FREQUENCY_HERTZ);
		rawVelocitySignal.setUpdateFrequency(RobotConstants.DEFAULT_SIGNALS_FREQUENCY_HERTZ);
		rawVoltageSignal.setUpdateFrequency(RobotConstants.DEFAULT_SIGNALS_FREQUENCY_HERTZ);
		rawCurrentSignal.setUpdateFrequency(RobotConstants.DEFAULT_SIGNALS_FREQUENCY_HERTZ);

		TimedValue<Rotation2d> positionTimedValue = new TimedValue<>(
			Rotation2d
				.fromRotations(BaseStatusSignal.getLatencyCompensatedValueAsDouble(rawPositionSignal.refresh(), rawVelocitySignal.refresh())),
			TimeUtil.getCurrentTimeSeconds()
		);
		TimedValue<Rotation2d> velocityTimedValue = new TimedValue<>(
			Rotation2d.fromRotations(rawVelocitySignal.refresh().getValueAsDouble()),
			TimeUtil.getCurrentTimeSeconds()
		);
		TimedValue<Double> voltageTimedValue = new TimedValue<>(rawVoltageSignal.refresh().getValueAsDouble(), TimeUtil.getCurrentTimeSeconds());
		TimedValue<Double> currentTimedValue = new TimedValue<>(rawCurrentSignal.refresh().getValueAsDouble(), TimeUtil.getCurrentTimeSeconds());

		Supplier<TimedValue<Rotation2d>> positionSignal = () -> {
			positionTimedValue.setValue(
				Rotation2d
					.fromRotations(BaseStatusSignal.getLatencyCompensatedValueAsDouble(rawPositionSignal.refresh(), rawVelocitySignal.refresh()))
			);
			positionTimedValue.setTimestamp(TimeUtil.getCurrentTimeSeconds());
			return positionTimedValue;
		};

		Supplier<TimedValue<Rotation2d>> velocitySignal = () -> {
			velocityTimedValue.setValue(Rotation2d.fromRotations(rawVelocitySignal.refresh().getValueAsDouble()));
			velocityTimedValue.setTimestamp(TimeUtil.getCurrentTimeSeconds());
			return velocityTimedValue;
		};

		Supplier<TimedValue<Double>> voltageSignal = () -> {
			voltageTimedValue.setValue(rawVoltageSignal.refresh().getValueAsDouble());
			voltageTimedValue.setTimestamp(TimeUtil.getCurrentTimeSeconds());
			return voltageTimedValue;
		};

		Supplier<TimedValue<Double>> currentSignal = () -> {
			currentTimedValue.setValue(rawCurrentSignal.refresh().getValueAsDouble());
			currentTimedValue.setTimestamp(TimeUtil.getCurrentTimeSeconds());
			return currentTimedValue;
		};

		motor.getDevice().optimizeBusUtilization();

		return new Test(logPath, motor, positionSignal, velocitySignal, voltageSignal, currentSignal);
	}

}
