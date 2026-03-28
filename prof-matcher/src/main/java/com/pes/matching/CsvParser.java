package com.pes.matching;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class CsvParser {

    // This method takes the file path and returns a List of Professor objects
    public static List<Professor> loadProfessors(String filePath) {
        List<Professor> professors = new ArrayList<>();

        try {
            // Load the CSV file as a resource from the classpath
            InputStream inputStream = CsvParser.class.getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                System.out.println("Error: CSV file not found in resources: " + filePath);
                return professors;
            }

            // Create a reader and skip the first line (the header row)
            CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream))
                    .withSkipLines(1) 
                    .build();

            List<String[]> allRows = reader.readAll();

            for (String[] row : allRows) {
                // Ensure the row has enough columns so our code doesn't crash
                if (row.length >= 10) {
                    String name = row[0];
                    String designation = row[1];
                    String research = row[2];
                    String teaching = row[3];
                    String about = row[4];
                    String pubJournals = row[5];
                    String pubConfs = row[6];
                    String education = row[7];
                    String mail = row[8];
                    String phone = row[9];

                    // Create a new Professor object and add it to our list
                    Professor prof = new Professor(name, designation, research, teaching, 
                                                   about, pubJournals, pubConfs, education, mail, phone);
                    professors.add(prof);
                }
            }
            System.out.println("Success: Loaded " + professors.size() + " professors from CSV.");

        } catch (Exception e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        }

        return professors;
    }
}