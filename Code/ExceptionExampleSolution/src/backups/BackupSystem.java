package backups;

import java.util.ArrayList;
import java.util.List;

public class BackupSystem {
	
	private FileSystem fs;
	private List<BackupLink> backups;
	
	
	public BackupSystem(FileSystem fs) {
		this.fs = fs;
		backups = new ArrayList<BackupLink>();
	}
	
	/**
	 * Parses a given backup manifest and add all links
	 * (a manifest file may include submanifests)
	 * 
	 * @param filename
	 * @return true if successful, false if an error occurs 
	 */
	public boolean parseManifest(String filename) {
		try {
			parseManifestInternal(filename);
		} catch (BackupProblemException e) {
			System.err.println("An error occured parsing manifest " + filename);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	private void parseManifestInternal(String filename) throws BackupProblemException {
		BackupProblemException.pushFile(filename);
		try {
			String contents = fs.getFileContents(filename);
			String[] lines = contents.split("\n");
			for(String line : lines) {
				if(line.startsWith("FILE")) {
					parseFileLine(line);
					
				} else if(line.startsWith("MANIFEST")) {
					parseManifestLine(line);
				} else {
					throw new BackupProblemException("Bad manifest line - " + line);
				}
			}
		} finally {
			BackupProblemException.popFile();
		}
	}

	/**
	 * Parses a line of the manifest representing a single file link
	 * 
	 * File lines should be of the form:
	 * FILE filename sourceDirectory backupDirectory
	 * 
	 * It is safe to assume that files never have spaces
	 * (IN THIS FAKE EXAMPLE - of course that would never be safe in real life)
	 * 
	 * For example:
	 * FILE criticalData.dat /home/buffalo/Documents /backups
	 * @throws BackupProblemException 
	 */
	private boolean parseFileLine(String line) throws BackupProblemException {
		String[] parts = line.split(" ");
		BackupLink newLink = new BackupLink(this.fs);
		newLink.setSource(parts[1], parts[2]);
		newLink.setBackupDestination(parts[3]);
		backups.add(newLink);
		return true;
	}
	
	/**
	 * Parses a line of the manifest linking to another sub-manifest
	 * 
	 * Manifest lines should be of the form:
	 * MANIFEST filename
	 * 
	 * For example:
	 * MANIFEST projectThreeFilesTobBackup.txt
	 * 
	 * @param line to parse
	 * @return true if the operation was successful
	 * @throws BackupProblemException 
	 */
	private void parseManifestLine(String line) throws BackupProblemException {
		String[] parts = line.split(" ");
		parseManifestInternal(parts[1]);
		
	}
	
	public int numberOfFiles() {
		return backups.size();
	}

}
