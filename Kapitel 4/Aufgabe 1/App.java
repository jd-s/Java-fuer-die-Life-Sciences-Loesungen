package de.bit.pl2.ex10;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class App {
    public static void main(String[] args) {

        Options options = new Options();
        Option pmidlist = new Option("l", "pmidlist", true, "List of pmids");
        Option outputpath = new Option("e", "outputpath", true, "filepath to which " +
                "classifications should be written");

        options.addOption(pmidlist);
        options.addOption(outputpath);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmdLine;

        try {
            cmdLine = parser.parse(options, args);

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Classify each in a list of pmids, providing the following arguments:", options);
            System.exit(1);
            return;
        }


        if (cmdLine.hasOption("l") && cmdLine.hasOption("e")) {
            String pmidlist1 = cmdLine.getOptionValue("pmidlist");
            String outputpath1 = cmdLine.getOptionValue("outputpath");


            // get list of pmids from file
            FileParser fileParser = new FileParser();
            List<Integer> pmids = fileParser.parseFile(new File(pmidlist1));

            // read jsons into list of documents
            JsonReader jsonReader = new JsonReader();
            List<Document> docs = jsonReader.readAllJsons(pmids);

            // bin each document and write result into file
            PrintWriter printWriter = null;
            try {
                printWriter = new PrintWriter(new File(String.valueOf(outputpath1)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // Print header
            printWriter.println("PMID, Classification");
            for (Document doc : docs) {
                printWriter.println(doc.getPmid() + "," + Document.bin(doc));
            }
            // Force the PrintWriter to actually write to the file now before we quit the app
            printWriter.flush();


        } else {
            formatter.printHelp("Classify pmids:", options);
            System.exit(1);
        }

    }
}
