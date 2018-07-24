package backups;
/**
 * This class represents a link between a file, and the location where it ought to be backed up.
 * 
 * For example, if I had a file /home/hewner/csse375/essentialGradeData.txt and I might want it 
 * to be backed up to /backups/gradeInfo
 * 
 * There are 3 simple rules with regard to BackupLinks.
 * 
 * The file should exist
 * The backup directory should exist
 * The file should NOT exist in the backup directory
 * 
 * 		
 * @author hewner
 *
 */
public class BackupLink {

	String filename;
	String fileDirectory;
	String backupDirectory;
	FileSystem fs;
	
	public BackupLink(FileSystem fs) { 
		this.fs = fs;
	}
	
	public boolean setSource(String filename, String fileDirectory) {
		if(!fs.fileExists(filename, fileDirectory)) {
			System.err.println("Error setting source!");
			return false; //ERROR!
		}
		this.filename = filename;
		this.fileDirectory = fileDirectory;
		return true; //SUCCESS!

	}
	
	public boolean setBackupDestination(String backupDirectory) {
		if(!fs.directoryExists(backupDirectory)) {
			System.err.println("Error setting directory");
			return false; //ERROR!
		}
		if(fs.fileExists(filename, backupDirectory)) {
			System.err.println("Error setting directory");
			return false; //ERROR!			
		}
		this.backupDirectory = backupDirectory;
		return true; //SUCCESS!
	}
	
}
