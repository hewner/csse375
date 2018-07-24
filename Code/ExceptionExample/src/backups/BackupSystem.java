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
		String contents = fs.getFileContents(filename);
		String[] lines = contents.split("\n");
		for(String line : lines) {
			if(line.startsWith("FILE")) {
				if(!parseFileLine(line)) {
					System.err.println("error reading file line!");
					return false;
					
				}
			} else if(line.startsWith("MANIFEST")) {
				if(!parseManifestLine(line)) {
					System.err.println("error reading manifest line");
					return false;
				}
			} else {
				System.err.println("Bad unknown line!");
			}
		}
		return true;
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
	 */
	private boolean parseFileLine(String line) {
		String[] parts = line.split(" ");
		BackupLink newLink = new BackupLink(this.fs);
		if(!newLink.setSource(parts[1], parts[2])) {
			System.err.println("Error setting source!");
			return false;
		}
		if(!newLink.setBackupDestination(parts[3])) {
			System.err.println("Error setting backup destination!");
			return false;
		}
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
	 */
	private boolean parseManifestLine(String line) {
		String[] parts = line.split(" ");
		return parseManifest(parts[1]);
		
	}
	
	public int numberOfFiles() {
		return backups.size();
	}

}
