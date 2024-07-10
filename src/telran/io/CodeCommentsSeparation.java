package telran.io;

import java.io.*;

public class CodeCommentsSeparation {

	public static void main(String[] args) {

		try (BufferedReader reader = new BufferedReader(new FileReader(args[0]));
				BufferedWriter writer1 = new BufferedWriter(new FileWriter(args[1]));
				BufferedWriter writer2 = new BufferedWriter(new FileWriter(args[2]))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty()) {
					
					if (line.trim().startsWith("//")) {
						writer2.write(line + "\n");
					} else {
						writer1.write(line + "\n");
					}
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		// args[0] - file path for file containing both Java class code and comments
		// args[1] - result file with only code
		// args[2] -result file with only comments
		// example of args[0] "src/telran/io/test/InputOutputTest.java"
		// TODO
		// from one file containing code and comments to create two files
		// one with only comments and second with only code
	}

}
