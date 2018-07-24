package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import backups.BackupSystem;

public class TestBackupSystem {

	@Test
	public void testParseManifest() {
		FakeFileSystem fs = new FakeFileSystem();
		BackupSystem bs = new BackupSystem(fs);
		
		fs.addFileContents("manifest.txt", "FILE a.txt /source /backups\nFILE b.txt /source /backups");
		assertTrue(bs.parseManifest("manifest.txt"));
		assertEquals(2, bs.numberOfFiles());
	}

	
	@Test
	public void testParseManifestWithSubmanifest() {
		FakeFileSystem fs = new FakeFileSystem();
		BackupSystem bs = new BackupSystem(fs);
		
		fs.addFileContents("manifest.txt", "FILE a.txt /source /backups\nMANIFEST otherManifest.txt");
		fs.addFileContents("otherManifest.txt", "FILE b.txt /source /backups");
		assertTrue(bs.parseManifest("manifest.txt"));
		assertEquals(2, bs.numberOfFiles());
	}
}
