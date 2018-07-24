package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import backups.BackupLink;
import backups.BackupProblemException;

public class TestBackupLink {

	@Test
	public void testCreationProblems() throws BackupProblemException {
		//to understand how these tests work you'll want to look at FakeFileSystem
		
		// good case
		FakeFileSystem fs = new FakeFileSystem();
		BackupLink b = new BackupLink(fs);
		b.setSource("foo.txt", "/bar");
		b.setBackupDestination("/backups");

	}

	@Test(expected = BackupProblemException.class)
	public void testCreationProblems2() throws BackupProblemException {
		// file does not exist
		FakeFileSystem fs = new FakeFileSystem();
		BackupLink b = new BackupLink(fs);
		b.setSource("fooNotExisting.txt", "/bar");
	}
	
	@Test(expected = BackupProblemException.class)
	public void testCreationProblems3() throws BackupProblemException {
		// destination directory does not exist
		FakeFileSystem fs = new FakeFileSystem();
		BackupLink b = new BackupLink(fs);
		b.setSource("foo.txt", "/bar");
		b.setBackupDestination("/NotExisting");
	}
}
