package log;

public class Log {
	
	private static boolean showLog = false;
	private static boolean showDebug = false;
	private static boolean showError = false;
	
	public static void log(String message) {
		if (showLog) {
			doMessage("LOG: " + message);
		}
	}
	
	public static void debug(String message) {
		if (showDebug) {
			doMessage("DEBUG: " + message);
		}
	}
	
	public static void error(String message) {
		if (showError) {
			doMessage("ERROR: " + message);
		}
	}
	
	private static void doMessage(String message) {
		System.out.println(message);
	}
	
	public static void showLog(boolean showLog) {
		Log.showLog = showLog;
	}
	
	public static void showDebug(boolean showDebug) {
		Log.showDebug = showDebug;
	}
	
	public static void showError(boolean showError) {
		Log.showError = showError;
	}

}
