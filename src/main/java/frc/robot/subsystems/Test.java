package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.hardware.interfaces.ControllableMotor;
import frc.utils.TimedValue;
import org.littletonrobotics.junction.Logger;

import java.util.function.Supplier;

public class Test extends GBSubsystem {

	ControllableMotor motor;

	Supplier<TimedValue<Rotation2d>> positionSignal;
	Supplier<TimedValue<Rotation2d>> velocitySignal;
	Supplier<TimedValue<Double>> voltageSignal;
	Supplier<TimedValue<Double>> currentSignal;

	public Test(
		String logPath,
		ControllableMotor motor,
		Supplier<TimedValue<Rotation2d>> positionSignal,
		Supplier<TimedValue<Rotation2d>> velocitySignal,
		Supplier<TimedValue<Double>> voltageSignal,
		Supplier<TimedValue<Double>> currentSignal
	) {
		super(logPath);

		this.motor = motor;

		this.positionSignal = positionSignal;
		this.voltageSignal = voltageSignal;
		this.velocitySignal = velocitySignal;
		this.currentSignal = currentSignal;
	}

	public ControllableMotor getMotor() {
		return motor;
	}

	@Override
	protected void subsystemPeriodic() {
		motor.updateSimulation();

		Logger.recordOutput(getLogPath() + "/Position", positionSignal.get().getValue());
		Logger.recordOutput(getLogPath() + "/Velocity", velocitySignal.get().getValue());
		Logger.recordOutput(getLogPath() + "/Voltage", voltageSignal.get().getValue());
		Logger.recordOutput(getLogPath() + "/Current", currentSignal.get().getValue());
	}

}
