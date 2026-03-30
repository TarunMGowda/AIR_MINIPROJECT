package com.pes.matching;

import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.println("=== PES Professor Matching System Ready ===");
        
        // Build the new, larger index with new columns (uncomment to rebuild)
        List<Professor> professors = CsvParser.loadProfessors("pes_university_staff_data-v2_addded_dept_campus.csv");
        Indexer.buildIndex(professors);
        // End of index building
        
        // Create an interactive terminal for testing queries
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("\nEnter a research topic to find a mentor (or type 'exit' to quit): ");
            String query = scanner.nextLine();
            
            if (query.equalsIgnoreCase("exit")) {
                System.out.println("Shutting down system. Goodbye!");
                break;
            }
            
            if (!query.trim().isEmpty()) {
                // Search for the top 5 professors matching the query
                Searcher.search(query, 5);
            }
        }
        
        scanner.close();
    }
}