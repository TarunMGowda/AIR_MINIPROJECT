package com.pes.matching;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Searcher {
    private static final String INDEX_DIR = "lucene_index";

    // 1. Strip punctuation and remove conversational noise
    private static String cleanConversationalQuery(String query) {
        // Remove punctuation first! (replaces . , ? ! ' with space)
        String cleaned = query.replaceAll("['.,?!]", " ").toLowerCase();
        
        String[] stopWords = {"can", "you", "get", "me", "the", "details", "of", "list", "who", "come", "under", "a", "part", "takes", "i", "dont", "know", "exactly", "but", "she", "he", "is", "image", "want", "to", "work", "on", "my", "related", "works", "well", "for", "give", "names", "professor", "professors", "teacher", "capstone", "project", "and", "name"};
        
        for (String word : stopWords) {
            // Replace whole words only
            cleaned = cleaned.replaceAll("\\b" + word + "\\b", "");
        }
        return cleaned.trim().replaceAll(" +", " "); // Remove extra spaces
    }

    // 2. Expand acronyms
    private static String expandSynonyms(String query) {
        String expanded = query;
        expanded = expanded.replaceAll("\\baiml\\b", "(aiml OR \"artificial intelligence\" OR \"machine learning\")");
        expanded = expanded.replaceAll("\\bml\\b", "(ml OR \"machine learning\")");
        expanded = expanded.replaceAll("\\biot\\b", "(iot OR \"internet of things\")");
        return expanded;
    }

    public static void search(String originalQuery, int maxResults) {
        try {
            Directory directory = FSDirectory.open(Paths.get(INDEX_DIR));
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer();

            // Field Weights
            Map<String, Float> boosts = new HashMap<>();
            boosts.put("name", 10.0f);        
            boosts.put("research", 3.0f);
            boosts.put("publications", 2.0f);
            boosts.put("campus", 2.0f);       
            boosts.put("department", 2.0f);
            boosts.put("teaching", 1.5f);
            boosts.put("about", 1.0f);

            String[] fields = {"name", "research", "teaching", "about", "publications", "campus", "department"};
            MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);

            // PROCESS PIPELINE
            String cleanedQuery = cleanConversationalQuery(originalQuery);
            String expandedQuery = expandSynonyms(cleanedQuery);
            
            // 3. SMART FUZZY LOGIC
            StringBuilder finalQueryStr = new StringBuilder();
            for (String word : expandedQuery.split("\\s+")) {
                // Only apply fuzzy (~) if it's a pure word (no brackets/quotes) AND longer than 3 letters
                if (word.matches("^[a-zA-Z0-9]+$") && word.length() > 3 
                    && !word.equalsIgnoreCase("AND") && !word.equalsIgnoreCase("OR")) {
                    finalQueryStr.append(word).append("~1 "); // ~1 allows 1 typo, much safer than default
                } else {
                    finalQueryStr.append(word).append(" "); // Leave exact phrases alone!
                }
            }

            String finalParsed = finalQueryStr.toString().trim();
            
            // Failsafe: If the query is empty after cleaning, abort
            if(finalParsed.isEmpty()) {
                System.out.println("Query was too generic after filtering. Try adding specific keywords.");
                return;
            }

            System.out.println("\n--- SEARCH PIPELINE ---");
            System.out.println("1. Raw Input   : '" + originalQuery + "'");
            System.out.println("2. Cleaned     : '" + cleanedQuery + "'");
            System.out.println("3. Lucene Exec : '" + finalParsed + "'");
            System.out.println("-----------------------\n");

            Query query = parser.parse(finalParsed);
            TopDocs results = searcher.search(query, maxResults);
            ScoreDoc[] hits = results.scoreDocs;

            if (hits.length == 0) {
                System.out.println("No professors found matching your query.");
            } else {
                for (int i = 0; i < hits.length; i++) {
                    Document doc = searcher.doc(hits[i].doc);
                    System.out.printf("%d. %s (Score: %.2f) | %s, %s\n", 
                        (i + 1), doc.get("name"), hits[i].score, 
                        doc.get("department") != null ? doc.get("department") : "N/A", 
                        doc.get("campus") != null ? doc.get("campus") : "N/A");
                    System.out.println("   Email: " + doc.get("mail"));
                    System.out.println();
                }
            }
            reader.close();
        } catch (Exception e) { 
            System.out.println("Error processing query: " + e.getMessage()); 
        }
    }
}