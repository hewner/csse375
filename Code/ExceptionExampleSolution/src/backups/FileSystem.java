package backups;

public interface FileSystem {

	boolean fileExists(String filename, String fileDirectory);

	boolean directoryExists(String directory);

	String getFileContents(String filename);
	
}
