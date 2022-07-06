package de.fechtelhoff;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PdfManipulateMetadataTest {

	private static final Path TESTDATA_DIRECTORY = Path.of("target/test-classes").toAbsolutePath();

	private static final Path PDF_DUMMY_FILE = TESTDATA_DIRECTORY.resolve("DummyFile.pdf");

	@BeforeAll
	static void beforeAll() {
		Assertions.assertTrue(checkPath(TESTDATA_DIRECTORY));
		Assertions.assertTrue(checkPath(PDF_DUMMY_FILE));
		System.out.println();
	}

	@Test
	void test() {
		final String md5HashBefore = Md5Util.md5Hex(PDF_DUMMY_FILE);
		final String[] args = new String[] {PDF_DUMMY_FILE.toString()};
		PdfManipulateMetadata.main(args);
		final String md5HashAfter = Md5Util.md5Hex(PDF_DUMMY_FILE);
		Assertions.assertNotSame(md5HashBefore, md5HashAfter);
	}

	@SuppressWarnings("SameParameterValue")
	private static boolean checkPath(final Path path) {
		if (Files.exists(path)) {
			if (Files.isDirectory(path)) {
				System.out.printf("Das Verzeichnis: \"%s\" existiert.%n", path);
			} else if (Files.isRegularFile(path)) {
				System.out.printf("Die Datei: \"%s\" existiert.%n", path);
			}
			return true;
		} else {
			System.out.printf("Der Pfad: \"%s\" existiert NICHT.%n", path);
			return false;
		}
	}
}
