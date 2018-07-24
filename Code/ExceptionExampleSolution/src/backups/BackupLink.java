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
	
	public void setSource(String filename, String fileDirectory) throws BackupProblemException {
		if(!fs.fileExists(filename, fileDirectory)) {
			throw new BackupProblemException(filename + " does not exist in " + fileDirectory);
		}
		this.filename = filename;
		this.fileDirectory = fileDirectory;

	}
	
	public void setBackupDestination(String backupDirectory) throws BackupProblemException {
		if(!fs.directoryExists(backupDirectory)) {
			throw new BackupProblemException(backupDirectory + " does not exist");
		}
		if(fs.fileExists(filename, backupDirectory)) {
			throw new BackupProblemException(filename + "already exists in backup location " + backupDirectory);
		}
		this.backupDirectory = backupDirectory;
	}
	
}
