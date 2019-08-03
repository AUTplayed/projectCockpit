package codes.fepi.entity;

public enum Health {
	UP,
	DOWN,
	CRASHED,
	WTF;

	public static Health determine(boolean active, boolean running) {
		if (active) {
			if (running) {
				return UP;
			} else {
				return CRASHED;
			}
		} else {
			if (running) {
				return WTF;
			} else {
				return DOWN;
			}
		}
	}
}
