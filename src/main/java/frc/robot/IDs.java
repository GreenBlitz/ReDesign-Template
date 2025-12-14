package frc.robot;

import com.revrobotics.spark.SparkLowLevel;
import frc.robot.hardware.phoenix6.BusChain;
import frc.robot.hardware.phoenix6.Phoenix6DeviceID;
import frc.robot.hardware.rev.motors.SparkMaxDeviceID;

public class IDs {

	public static class TalonFXIDs {

		public static final Phoenix6DeviceID test = new Phoenix6DeviceID(0, BusChain.ROBORIO);

	}

	public static class CANCoderIDs {
	}

	public static class Pigeon2IDs {
	}

	public static class CANdleIDs {
	}

	public static class SparkMAXIDs {

		public static final SparkMaxDeviceID test = new SparkMaxDeviceID(0, SparkLowLevel.MotorType.kBrushless);

	}

	public static class DigitalInputsIDs {
	}

}
