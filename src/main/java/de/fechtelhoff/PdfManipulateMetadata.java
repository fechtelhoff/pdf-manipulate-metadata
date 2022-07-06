package de.fechtelhoff;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aktualisiert das "Modification Date" in den PDF-Metadaten auf das aktuelle Datum.
 */
public class PdfManipulateMetadata {

	private static final Logger LOG = LoggerFactory.getLogger(PdfManipulateMetadata.class);

	public static void main(String[] args) {
		if (args != null && args.length != 0 && args[0] != null && args[0].endsWith(".pdf")) {
			final String pdfFileName = args[0];
			final Path pdfFilePath = Path.of(pdfFileName).toAbsolutePath();
			LOG.info("Dateiname: {}", pdfFileName);
			LOG.info("Vollständiger Pfad: {}", pdfFilePath);
			if (Files.exists(pdfFilePath) && Files.isRegularFile(pdfFilePath)) {
				final String md5HashBefore = Md5Util.md5Hex(pdfFilePath);
				try (final PDDocument document = PDDocument.load(pdfFilePath.toFile())) {
					final PDDocumentInformation info = document.getDocumentInformation();
					info.setModificationDate(Calendar.getInstance());
					document.setDocumentInformation(info);
					document.save(pdfFilePath.toFile());
					final String md5HashAfter = Md5Util.md5Hex(pdfFilePath);
					LOG.info("MD5 Hash (vorher):  {}", md5HashBefore);
					LOG.info("MD5 Hash (nachher): {}", md5HashAfter);
				} catch (final IOException exception) {
					final String message = String.format("Die Datei \"%s\" kann nicht gelesen werden.", pdfFileName);
					throw new IllegalStateException(message, exception);
				}
			} else {
				LOG.error("Die Datei \"{}\" existiert nicht.", pdfFileName);
			}
		} else {
			LOG.warn("Bitte als Argument eine PDF-Datei übergeben.");
		}
	}
}
