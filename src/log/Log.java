package log;

import javax.swing.JOptionPane;

public class Log {
	
	private static boolean showLog = false;
	private static boolean showDebug = false;
	private static boolean showWarning = false;
	private static boolean showError = false;
	private static boolean showAlert = false;
	
	public static void log(Object message) {
		if (showLog) {
			doMessage("LOG: " + message);
		}
	}
	
	public static void debug(Object message) {
		if (showDebug) {
			doMessage("DEBUG: " + message);
		}
	}
	
	public static void warn(Object message) {
		if (showWarning) {
			doMessage("WARNING: " + message);
		}
	}
	
	public static void error(Object message) {
		if (showError) {
			doMessage("ERROR: " + message);
		}
	}
	
	public static void alert(Object message) {
		if (showAlert) {
			JOptionPane.showMessageDialog(null, message);
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
	
	public static void showWarning(boolean showWarning) {
		Log.showWarning = showWarning;
	}
	
	public static void showError(boolean showError) {
		Log.showError = showError;
	}
	
	public static void showAlert(boolean showAlert) {
		Log.showAlert = showAlert;
	}

	public static void showAll(boolean show) {
		Log.showLog = show;
		Log.showDebug = show;
		Log.showWarning = show;
		Log.showError = show;
		Log.showAlert = show;
	}

}
