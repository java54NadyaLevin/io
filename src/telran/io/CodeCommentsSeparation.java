package telran.io;

import java.io.*;
import java.util.stream.Collectors;

public class CodeCommentsSeparation {

	public static void main(String[] args) {

		try (BufferedReader reader = new BufferedReader(new FileReader(args[0]));
				PrintWriter  writer1 = new PrintWriter (new FileWriter(args[1]));
				PrintWriter  writer2 = new PrintWriter (new FileWriter(args[2]))) {
			reader.lines().forEach(line -> {
				if(line.trim().startsWith("//")) {
					writer2.println(line);
				}else {
					writer1.println(line);
				}
			});
	
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
