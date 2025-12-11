package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.hardware.interfaces.ControllableMotor;
import org.littletonrobotics.junction.Logger;

import java.util.function.Supplier;

public class Test extends GBSubsystem {

	ControllableMotor motor;

	Supplier<Rotation2d> velocitySignal;
	Supplier<Double> voltageSignal;
	Supplier<Double> currentSignal;

	public Test(
		String logPath,
		ControllableMotor motor,
		Supplier<Rotation2d> velocitySignal,
		Supplier<Double> voltageSignal,
		Supplier<Double> currentSignal
	) {
		super(logPath);

		this.motor = motor;

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

		Logger.recordOutput(getLogPath() + "/Voltage", voltageSignal.get());
		Logger.recordOutput(getLogPath() + "/Velocity", velocitySignal.get());
		Logger.recordOutput(getLogPath() + "/Current", currentSignal.get());
	}

}
