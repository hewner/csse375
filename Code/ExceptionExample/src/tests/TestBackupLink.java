package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import backups.BackupLink;

public class TestBackupLink {

	@Test
	public void testCreationProblems() {
		
		//to understand how these tests work you'll want to look at FakeFileSystem
		
		// good case
		FakeFileSystem fs = new FakeFileSystem();
		BackupLink b = new BackupLink(fs);
		assertTrue(b.setSource("foo.txt", "/bar"));
		assertTrue(b.setBackupDestination("/backups"));
		
		// file does not exist
		fs = new FakeFileSystem();
		b = new BackupLink(fs);
		assertFalse(b.setSource("fooNotExisting.txt", "/bar"));
		
		// destination directory does not exist
		fs = new FakeFileSystem();
		b = new BackupLink(fs);
		assertTrue(b.setSource("foo.txt", "/bar"));
		assertFalse(b.setBackupDestination("/NotExisting"));

	}
    /*  Example test for when you get exceptions working

	@Test(expected = BackupProblemException.class)
	public void testCreationProblems2() throws BackupProblemException {
		// file does not exist
		FakeFileSystem fs = new FakeFileSystem();
		BackupLink b = new BackupLink(fs);
		b.setSource("fooNotExisting.txt", "/bar");
	}

    */
}
