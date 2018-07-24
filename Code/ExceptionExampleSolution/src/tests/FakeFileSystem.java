package tests;

import java.util.HashMap;
import java.util.Map;

import backups.FileSystem;

public class FakeFileSystem implements FileSystem {

	public Map<String,String> files = new HashMap<String,String>();
	
	@Override
	public boolean fileExists(String fileName, String fileDirectory) {
		return !(fileName.contains("NotExisting") || fileDirectory.contains("backup"));
	}

	@Override
	public boolean directoryExists(String directory) {
		return !directory.contains("NotExisting");
	}

	@Override
	public String getFileContents(String filename) {
		return files.get(filename);
	}
	
	public void addFileContents(String file, String contents) {
		files.put(file,contents);
	}

}
