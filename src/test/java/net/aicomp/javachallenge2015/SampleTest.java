package net.aicomp.javachallenge2015;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SampleTest {

	private ByteArrayOutputStream _baos;
	private PrintStream _out;

	@Before
	public void setUp() {
		_baos = new ByteArrayOutputStream();
		_out = System.out;
		System.setOut(new PrintStream(new BufferedOutputStream(_baos)));
	}

	@After
	public void tearDown() {
		System.setOut(_out);
	}

	@Test
	@Ignore
	public void testSeed0() {
		try {
			Bookmaker.main(new String[] { "-a", "\"java SampleAI -s 0\"", "-a",
					"\"java SampleAI -s 0\"", "-a", "\"java SampleAI -s 0\"",
					"-a", "\"java SampleAI -s 0\"", "-s", "0" });
		} catch (InterruptedException | ParseException e) {
			e.printStackTrace();
			fail();
		}
		System.out.flush();
		String expected = getFileContents(new File(
				"fixture/sample_log_seed0.txt"));
		String actual = _baos.toString();
		System.out.println(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Ignore
	public void testSeed1000() {
		try {
			Bookmaker.main(new String[] { "-a", "\"java SampleAI -s 0\"", "-a",
					"\"java SampleAI -s 0\"", "-a", "\"java SampleAI -s 0\"",
					"-a", "\"java SampleAI -s 0\"", "-s", "1000" });
		} catch (InterruptedException | ParseException e) {
			e.printStackTrace();
			fail();
		}
		System.out.flush();
		String expected = getFileContents(new File(
				"fixture/sample_log_seed1000.txt"));
		String actual = _baos.toString();
		System.out.println(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Ignore
	public void testSeed0WithPlayer() {
		try {
			Bookmaker.main(new String[] { "-a", "\"java SampleAI -s 20\"",
					"-a", "\"java SampleAI -s 40\"", "-a",
					"\"java SampleAI -s 60\"", "-a", "\"java SampleAI -s 80\"",
					"-s", "0" });
		} catch (InterruptedException | ParseException e) {
			e.printStackTrace();
			fail();
		}
		System.out.flush();
		String expected = getFileContents(new File(
				"fixture/sample_log_seed0_player.txt"));
		String actual = _baos.toString();
		System.out.println(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Ignore
	public void testSeed2000WithPlayer() {
		try {
			Bookmaker.main(new String[] { "-a", "\"java SampleAI -s 20\"",
					"-a", "\"java SampleAI -s 40\"", "-a",
					"\"java SampleAI -s 60\"", "-a", "\"java SampleAI -s 80\"",
					"-s", "2000" });
		} catch (InterruptedException | ParseException e) {
			e.printStackTrace();
			fail();
		}
		System.out.flush();
		String expected = getFileContents(new File(
				"fixture/sample_log_seed2000_player.txt"));
		String actual = _baos.toString();
		System.out.println(actual);
		assertEquals(expected, actual);
	}

	@Test
	public void testNew() {
		try {
			Bookmaker.main(new String[] { "-a", "\"java SampleAIL\"", "-a",
					"\"java SampleAIL\"", "-a", "\"java SampleAIL\"", "-a",
					"\"java SampleAIL\"", "-s", "0" });
		} catch (InterruptedException | ParseException e) {
			e.printStackTrace();
			fail();
		}
		System.out.flush();
		String expected = getFileContents(new File("fixture/sample_log_new.txt"));
		String actual = _baos.toString();
		System.out.println(actual);
		assertEquals(expected, actual);
	}

	private String getFileContents(File file) {
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail();
		}
		StringBuilder builder = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		while (sc.hasNextLine()) {
			builder.append(sc.nextLine());
			builder.append(newLine);
		}
		if (sc != null) {
			sc.close();
		}
		return builder.toString();
	}

}
