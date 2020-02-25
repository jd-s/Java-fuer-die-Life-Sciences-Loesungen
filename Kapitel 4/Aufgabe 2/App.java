package de.bit.pl2.ex10;

import org.apache.commons.cli.*;

public class App {
    public static void main(String[] args) {
        Options options = new Options();
        Option color = new Option("c", "color", true, "Color in hexadecimal notation");
        options.addOption(color);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmdLine;

        try {
            cmdLine = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Returns color class and compliment of given color.", options);
            System.exit(1);
            return;
        }

        if (cmdLine.hasOption("c") && cmdLine.getOptionValue("color").length() == 6 &&
                cmdLine.getOptionValue("color").matches("-?[0-9a-fA-F]+")) {
            String hex = cmdLine.getOptionValue("color");
            ColorTools.printClassAndCompl(hex);
        } else {
            System.err.println("Please provide a correct hexadecimal color as --color input.");
            System.exit(1);
            return;
        }
    }
}
