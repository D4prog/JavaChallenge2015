package net.aicomp.javachallenge2015;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import net.aicomp.javachallenge2015.log.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SampleTest {

	private ByteArrayOutputStream _sysout;
	private ByteArrayOutputStream _syserr;
	private PrintStream _out;
	private PrintStream _err;

	@Before
	public void setUp() {
		_sysout = new ByteArrayOutputStream();
		_out = System.out;
		System.setOut(new PrintStream(new BufferedOutputStream(_sysout)));
		_syserr = new ByteArrayOutputStream();
		_err = System.err;
		System.setErr(new PrintStream(new BufferedOutputStream(_syserr)));
	}

	@After
	public void tearDown() {
		System.setOut(_out);
		System.setErr(_err);
	}

	@Test
	@Ignore
	public void testSeed0() {
		try {
			Bookmaker.main(new String[] { "-a", "\"java SampleAI -s 0\"", "-a", "\"java SampleAI -s 0\"", "-a",
					"\"java SampleAI -s 0\"", "-a", "\"java SampleAI -s 0\"", "-s", "0" });
		} catch (InterruptedException | ParseException e) {
			e.printStackTrace();
			fail();
		}
		System.out.flush();
		String expected = getFileContents(new File("fixture/sample_log_seed0.txt"));
		String actual = _sysout.toString();
		System.out.println(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Ignore
	public void testSeed1000() {
		try {
			Bookmaker.main(new String[] { "-a", "\"java SampleAI -s 0\"", "-a", "\"java SampleAI -s 0\"", "-a",
					"\"java SampleAI -s 0\"", "-a", "\"java SampleAI -s 0\"", "-s", "1000" });
		} catch (InterruptedException | ParseException e) {
			e.printStackTrace();
			fail();
		}
		System.out.flush();
		String expected = getFileContents(new File("fixture/sample_log_seed1000.txt"));
		String actual = _sysout.toString();
		System.out.println(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Ignore
	public void testSeed0WithPlayer() {
		try {
			Bookmaker.main(new String[] { "-a", "\"java SampleAI -s 20\"", "-a", "\"java SampleAI -s 40\"", "-a",
					"\"java SampleAI -s 60\"", "-a", "\"java SampleAI -s 80\"", "-s", "0" });
		} catch (InterruptedException | ParseException e) {
			e.printStackTrace();
			fail();
		}
		System.out.flush();
		String expected = getFileContents(new File("fixture/sample_log_seed0_player.txt"));
		String actual = _sysout.toString();
		System.out.println(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Ignore
	public void testSeed2000WithPlayer() {
		try {
			Bookmaker.main(new String[] { "-a", "\"java SampleAI -s 20\"", "-a", "\"java SampleAI -s 40\"", "-a",
					"\"java SampleAI -s 60\"", "-a", "\"java SampleAI -s 80\"", "-s", "2000" });
		} catch (InterruptedException | ParseException e) {
			e.printStackTrace();
			fail();
		}
		System.out.flush();
		String expected = getFileContents(new File("fixture/sample_log_seed2000_player.txt"));
		String actual = _sysout.toString();
		System.out.println(actual);
		assertEquals(expected, actual);
	}

	@Test
	@Ignore
	public void testNew() {
		main(new String[] { "-a", "\"java SampleAIL\"", "-a", "\"java SampleAIL\"", "-a", "\"java SampleAIL\"", "-a",
				"\"java SampleAIL\"", "-s", "0", "-t", "3" });
		System.out.flush();
		String expected = getFileContents(new File("fixture/sample_log_new.txt"));
		String actual = _sysout.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void testTimeout() {
		main(new String[] { "-a", "\"java TimeoutAI\"", "-a", "\"java TimeoutAI\"", "-a", "\"java TimeoutAI\"", "-a",
				"\"java TimeoutAI\"", "-s", "0", "-t", "30" });
		System.out.flush();
		System.err.flush();
		String actual = _syserr.toString();
		assertEquals(
				actual.length()
						- actual.replace("Terminated the thread because time was exceeded.",
								"Terminated the thread because time was exceeded").length(), 4);
	}

	private void main(String[] args) {
		Options options = Bookmaker.buildOptions();

		try {
			Logger.initialize();
			CommandLineParser parser = new DefaultParser();
			CommandLine line = parser.parse(options, args);
			Bookmaker.start(new Game(), line);
		} catch (ParseException e) {
			System.err.println("Error: " + e.getMessage());
			System.exit(-1);
		} finally {
			Logger.outputLog("Game Finished!", Logger.LOG_LEVEL_DETAILS);
			Logger.close();
		}
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
		String newLine = System.getProperty("line.separator").replace("\r\n", "\n");
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
