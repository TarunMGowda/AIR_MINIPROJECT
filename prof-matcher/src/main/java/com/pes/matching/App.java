package com.pes.matching;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.println("=== PES Professor Matching System Ready ===");
        
        // We already built the index, so we don't need the CsvParser or Indexer right now!
        
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