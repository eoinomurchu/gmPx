package util;

public class Timer {

	/**
	 * The start time for the timer.
	 */
	private long startTime;

	/**
	 * Creates a new Timer object and starts timing by calling <var>start</var>
	 */
	public Timer() {
		start();
	}

	/**
	 * Starts the timer from 0
	 */
	public void start() {
		startTime = System.nanoTime();
	}

	/**
	 * Resets the timer back to 0
	 */
	public void reset() {
		start();
	}

	/**
	 * Returns the current time in nano seconds
	 */
	public long nanoTime() {
		return System.nanoTime() - startTime;
	}

	/**
	 * Returns the current time in millis seconds
	 */
	public long milliTime() {
		return nanoTime() / 1000000;
	}

	/**
	 * Returns the current time in seconds
	 */
	public long secondTime() {
		return nanoTime() / 1000000000;
	}

}
