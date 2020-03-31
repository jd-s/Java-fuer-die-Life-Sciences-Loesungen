package de.bit.pl2.ex10;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    public List<Integer> parseFile(File file) {
        List<Integer> pmids = new ArrayList<>();
        try {
            List<String> list = Files.readAllLines(file.toPath());
            for (String line : list) {
                int pmid = Integer.parseInt(line);
                pmids.add(pmid);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pmids;
    }
}
