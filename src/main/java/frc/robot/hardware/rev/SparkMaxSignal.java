package frc.robot.hardware.rev;

import frc.robot.hardware.interfaces.InputSignal;

import java.util.function.Supplier;

public class SparkMaxSignal<T> implements InputSignal<T> {

	Supplier<T> valueSupplier;

	public SparkMaxSignal(Supplier<T> valueSupplier) {
		this.valueSupplier = valueSupplier;
	}

	@Override
	public T getLatestValue() {
		return valueSupplier.get();
	}

	@Override
	public T getAndUpdateValue() {
		return null;
	}

	@Override
	public void updateValue() {}

}
