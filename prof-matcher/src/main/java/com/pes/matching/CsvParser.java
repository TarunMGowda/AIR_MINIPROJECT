package com.pes.matching;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class CsvParser {
    public static List<Professor> loadProfessors(String filePath) {
        List<Professor> professors = new ArrayList<>();
        try {
            // Load from classpath resources
            InputStreamReader reader = new InputStreamReader(CsvParser.class.getClassLoader().getResourceAsStream(filePath));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> allRows = csvReader.readAll();

            for (String[] row : allRows) {
                if (row.length >= 10) {
                    // Safe parsing for optional columns at the end of the CSV
                    String name = row[0]; String designation = row[1]; String research = row[2];
                    String teaching = row[3]; String about = row[4]; String pubJ = row[5];
                    String pubC = row[6]; String edu = row[7]; String mail = row[8]; String phone = row[9];
                    String resp = row.length > 10 ? row[10] : "";
                    String image = row.length > 11 ? row[11] : "";
                    String dept = row.length > 12 ? row[12] : "";
                    String campus = row.length > 13 ? row[13] : "";

                    professors.add(new Professor(name, designation, research, teaching, about, pubJ, pubC, edu, mail, phone, resp, image, dept, campus));
                }
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return professors;
    }
}