package de.bit.pl2.ex08;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileParser {

    public ArrayList<Document> readDocumentsFromFile(String file) {
        ArrayList<Document> documents = new ArrayList<Document>();

        try {
            FileReader filereader = new FileReader(file);
            final CSVParser parser =
                    new CSVParserBuilder()
                            .withSeparator(';')
                            .build();
            final CSVReader csvReader =
                    new CSVReaderBuilder(filereader)
                            .withCSVParser(parser)
                            .build();
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                Document document = new Document();
                document.setTitle(line[0]);
                document.setJournal(line[1]);
                document.setYear(Integer.parseInt(line[2]));
                documents.add(document);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return documents;
    }
}
