package telran.io.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

class InputOutputTest {

	private static final String STREAM_FILE = "stream-file";
	private static final String HELLO = "Hello";
	private static final String WRITER_FILE = "writer-file";
	protected static final int SPACES_PER_DEPTH_LEVEL = 5;

	@AfterAll
	static void tearDown() throws IOException {
		Files.deleteIfExists(Path.of(STREAM_FILE));
		Files.deleteIfExists(Path.of(WRITER_FILE));
	}

	@Test
	// Testing PrintStream class
	// creates PrintStream object and connects it to file with name STREAM_FILE
	void printStreamTest() throws Exception {
		// try resources block allows automatic closing after existing from the block
		try (PrintStream printStream = new PrintStream(STREAM_FILE);) {
			printStream.println(HELLO);
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(STREAM_FILE))) {
			assertEquals(HELLO, reader.readLine());
			assertNull(reader.readLine());
		}
	}

	@Test
	// The same test as for PrintStream but for PrintWriter
	// The difference between PrintSTream and PrintWriter is that
	// PrintStream puts line immediately while PrintWriter puts line into a buffer
	// The buffer flushing is happening after closing the stream
	void printWriterTest() throws Exception {
		try (PrintWriter printWriter = new PrintWriter(WRITER_FILE)) {
			printWriter.println(HELLO);
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(WRITER_FILE))) {
			assertEquals(HELLO, reader.readLine());
			assertNull(reader.readLine());
		}
	}

	@Test
	void pathTest() {
		Path pathCurrent = Path.of(".");
//		System.out.printf("%s - path \".\"\n",pathCurrent);
//		System.out.printf("%s - normalized path \".\"\n", pathCurrent.normalize());
		System.out.printf("%s - %s\n", pathCurrent.toAbsolutePath().normalize(),
				Files.isDirectory(pathCurrent) ? "directory" : "file");
		pathCurrent = pathCurrent.toAbsolutePath().normalize();
		System.out.printf("count of levels is %d\n", pathCurrent.getNameCount());

	}

	@Test
	void printDirectoryTest() throws IOException {
		printDirectory(".", 3);
	}

	private void printDirectory(String dirPathStr, final int depth) throws IOException {
		int depthValue = depth == -1 ? Integer.MAX_VALUE : depth;

		// print directory content in the format with offset according to the level
		// if depth == -1 all levels should be printed out
		// <name> - <dir / file>
		// <name>
		// using FIles.walkFileTree
		Path path = Path.of(dirPathStr).toAbsolutePath().normalize();
		try {
			Files.walkFileTree(path, new HashSet<>(), depthValue, new FileVisitor<Path>() {
				int offset = 0;

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					printWithOffset(dir);
					offset++;
					return FileVisitResult.CONTINUE;
				}

				private void printWithOffset(Path path) {
					String tab = new String(" ").repeat(offset * SPACES_PER_DEPTH_LEVEL);
					String dirOrFile = Files.isDirectory(path) ? "directory" : "file";
					System.out.println(tab + path.getFileName() + " - " + dirOrFile);
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					printWithOffset(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					System.err.println(exc);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					offset--;
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
