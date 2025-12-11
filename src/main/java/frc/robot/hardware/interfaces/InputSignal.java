package frc.robot.hardware.interfaces;

public interface InputSignal<T> {

	T getLatestValue();

	T getAndUpdateValue();

	void updateValue();

}
