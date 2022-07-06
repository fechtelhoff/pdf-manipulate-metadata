package de.fechtelhoff;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Relevanten Code aus Apache Commons Codec (Vs. 1.15, Class DigestUtils) geklont.
 *
 * @see <a href="https://commons.apache.org/proper/commons-codec/">Apache Commons Codec</a>
 * <p>
 * Die Funktionen dieser Klasse werden ausschließlich dazu benutzt den Hash-Code einer Datei zu ermitteln,
 * und damit nach binär identische Dateien zu suchen.
 * Die Verwendung des veralteten MD5-Algorithmus ist daher an dieser Stelle aus Security-Sicht unbedenklich!
 */
public final class Md5Util {

	private static final int STREAM_BUFFER_LENGTH = 1024;

	/**
	 * The MD5 message digest algorithm defined in RFC 1321.
	 */
	private static final String MESSAGE_DIGEST_ALGORITHMS_MD5 = "MD5";

	/**
	 * Used to build output as hex.
	 */
	private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	private Md5Util() {
		// intentionally blank
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 32 character hex string.
	 * <p>
	 * Nicht Bestandteil von Apache Commons Codec !!!
	 *
	 * @param path Data to digest
	 * @return MD5 digest as a hex string
	 */
	public static String md5Hex(final Path path) {
		if (path == null) {
			return null;
		}
		try {
			return md5Hex(Files.newInputStream(path));
		} catch (final IOException exception) {
			final String pathString = path.toAbsolutePath().toString();
			throw new IllegalStateException("Fehler beim Lesen der Datei \"" + pathString + "\".", exception);
		}
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 32 character hex string.
	 *
	 * @param inputStream Data to digest
	 * @return MD5 digest as a hex string
	 * @throws IOException On error reading from the stream
	 * @since 1.4
	 */
	public static String md5Hex(final InputStream inputStream) throws IOException {
		if (inputStream == null) {
			return null;
		}
		return encodeHexString(md5(inputStream));
	}

	public static String md5Hex(final String valueToHash) {
		if (valueToHash == null) {
			return null;
		}
		return encodeHexString(md5(valueToHash));
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 16 element {@code byte[]}.
	 *
	 * @param inputStream Data to digest
	 * @return MD5 digest
	 * @throws IOException On error reading from the stream
	 * @since 1.4
	 */
	private static byte[] md5(final InputStream inputStream) throws IOException {
		final MessageDigest messageDigest = getMd5Digest();
		final byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
		int read = inputStream.read(buffer, 0, STREAM_BUFFER_LENGTH);
		while (read > -1) {
			messageDigest.update(buffer, 0, read);
			read = inputStream.read(buffer, 0, STREAM_BUFFER_LENGTH);
		}
		return messageDigest.digest();
	}

	private static byte[] md5(final String input) {
		if (input == null) {
			throw new NullPointerException("Input darf nicht leer sein");
		}
		final MessageDigest messageDigest = getMd5Digest();
		messageDigest.update(input.getBytes());
		return messageDigest.digest();
	}

	/**
	 * Returns an MD5 MessageDigest.
	 *
	 * @return An MD5 digest instance.
	 * @throws IllegalArgumentException when a {@link NoSuchAlgorithmException} is caught, which should never happen because MD5 is a
	 * built-in algorithm
	 */
	@SuppressWarnings("java:S4790") // java:S4790 -> Using weak hashing algorithms is security-sensitive
	static MessageDigest getMd5Digest() {
		try {
			return MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHMS_MD5);
		} catch (final NoSuchAlgorithmException exception) {
			throw new IllegalArgumentException(exception);
		}
	}

	/**
	 * Converts an array of bytes into a String representing the hexadecimal values of each byte in order. The returned
	 * String will be double the length of the passed array, as it takes two characters to represent any given byte.
	 *
	 * @param md5HashBytes a byte[] to convert to hex characters
	 * @return A String containing lower-case hexadecimal characters
	 * @since 1.4
	 */
	static String encodeHexString(final byte[] md5HashBytes) {
		final int l = md5HashBytes.length;
		final char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < md5HashBytes.length; i++, j = j + 2) {
			out[j] = DIGITS_LOWER[(0xF0 & md5HashBytes[i]) >>> 4];
			out[j + 1] = DIGITS_LOWER[0x0F & md5HashBytes[i]];
		}
		return new String(out);
	}
}
