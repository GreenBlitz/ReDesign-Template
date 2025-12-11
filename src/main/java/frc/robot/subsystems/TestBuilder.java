package frc.robot.subsystems;

import com.ctre.phoenix6.StatusSignal;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.IDs;
import frc.robot.RobotConstants;
import frc.robot.hardware.mechanisms.wpilib.SimpleMotorSimulation;
import frc.robot.hardware.phoenix6.motors.TalonFXMotor;

import java.util.function.Supplier;

public class TestBuilder {

	public static Test buildTest() {
		String logPath = RobotConstants.SUBSYSTEM_LOGPATH_PREFIX + "/Test";

		TalonFXMotor motor = new TalonFXMotor(
			logPath + "/Motor",
			IDs.TalonFXIDs.test,
			new SysIdRoutine.Config(),
			new SimpleMotorSimulation(new DCMotorSim(LinearSystemId.createDCMotorSystem(DCMotor.getNEO(1), 0.0001, 1 / 1), DCMotor.getNEO(1)))
		);

		StatusSignal<AngularVelocity> rawVelocitySignal = motor.getDevice().getVelocity(true);
		StatusSignal<Voltage> rawVoltageSignal = motor.getDevice().getMotorVoltage(true);
		StatusSignal<Current> rawCurrentSignal = motor.getDevice().getStatorCurrent(true);

		rawVelocitySignal.setUpdateFrequency(RobotConstants.DEFAULT_SIGNALS_FREQUENCY_HERTZ);
		rawVoltageSignal.setUpdateFrequency(RobotConstants.DEFAULT_SIGNALS_FREQUENCY_HERTZ);
		rawCurrentSignal.setUpdateFrequency(RobotConstants.DEFAULT_SIGNALS_FREQUENCY_HERTZ);

		Supplier<Rotation2d> velocitySignal = () -> Rotation2d.fromRotations(rawVelocitySignal.refresh().getValueAsDouble());
		Supplier<Double> voltageSignal = () -> rawVoltageSignal.refresh().getValueAsDouble();
		Supplier<Double> currentSignal = () -> rawCurrentSignal.refresh().getValueAsDouble();

		motor.getDevice().optimizeBusUtilization();

		return new Test(logPath, motor, velocitySignal, voltageSignal, currentSignal);
	}

}
