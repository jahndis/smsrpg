package test.sqlitedb;

import java.io.File;

public class TestUtils {
	
	public static void deleteFile(String filePath) {
		File file = new File(filePath); 
		if (file.exists()) { 
		  file.delete();
		}
	}

}
