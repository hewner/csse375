package backups;

import java.util.Stack;

public class BackupProblemException extends Exception {

	private String problemFile;
	
	public BackupProblemException(String message) {
		super(message + "(MANIFEST: " + currentFile() + ")");
		problemFile = currentFile();
	}

	public String getProbemFile() {
		return problemFile;
	}
	
	private static Stack<String> currentFiles = new Stack<String>();
	
	public static void pushFile(String filename) {
		currentFiles.push(filename);
	}
	
	public static void popFile() {
		currentFiles.pop();
	}
	
	public static String currentFile() {
		if(!currentFiles.isEmpty())
			return currentFiles.peek();
		return "???";
	}
	
}
