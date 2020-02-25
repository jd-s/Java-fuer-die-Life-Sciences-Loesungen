package de.bit.pl2.ex05;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class App {
    public static void main(String[] args) throws FileNotFoundException {

        if (args.length < 2) {
            System.out.println("Please call with two parameters: <input path> <REGEX>");
            return;
        }

        String inputPath = args[0];  
        String inputRegex = args[1]; 

        try {
            Pattern pattern = Pattern.compile(inputRegex);
            Scanner scanner = new Scanner(new File(inputPath));
            // Advance once to ignore the csv header
            if (scanner.hasNextLine()) {
                scanner.nextLine();


                while (scanner.hasNextLine()) {
                    String nextLine = scanner.nextLine();
                    Matcher matcher = pattern.matcher(nextLine);
                    if (matcher.find()) {
                        System.out.println(nextLine);
                    }
                }
            }
        } catch (PatternSyntaxException ex) {
            System.out.println("Warning: not a valid regular expression");
        }

    }
}
