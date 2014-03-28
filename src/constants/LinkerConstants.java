package constants;

public enum LinkerConstants {

	APPS("Current Applications"), NEWAPP("New Application");

	private LinkerConstants(String name) {
		this.name = name;
	}

	private final String name;

	@Override
	public String toString() {
		return name;
	}

	public static LinkerConstants fromString(String text) {
		if (text != null) {
			for (LinkerConstants b : LinkerConstants.values()) {
				if (text.equalsIgnoreCase(b.name)) {
					return b;
				}
			}
		}
		return null;
	}

}
