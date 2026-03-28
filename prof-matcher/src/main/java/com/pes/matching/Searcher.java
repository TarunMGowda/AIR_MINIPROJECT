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

    public static void search(String queryString, int maxResults) {
        try {
            Directory directory = FSDirectory.open(Paths.get(INDEX_DIR));
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer();

            // ==========================================
            // WOW FACTOR 1: FIELD BOOSTING
            // We tell Lucene that research is the most important field for a mentor.
            // ==========================================
            Map<String, Float> boosts = new HashMap<>();
            boosts.put("research", 3.0f);     // 3x weight
            boosts.put("publications", 2.0f); // 2x weight
            boosts.put("teaching", 1.5f);     // 1.5x weight
            boosts.put("about", 1.0f);        // standard weight

            String[] fieldsToSearch = {"research", "teaching", "about", "publications"};
            
            // Pass the boosts map into the parser
            MultiFieldQueryParser parser = new MultiFieldQueryParser(fieldsToSearch, analyzer, boosts);

            // ==========================================
            // WOW FACTOR 2: AUTO-FUZZY SEARCH (TYPO TOLERANCE)
            // We append the Lucene fuzzy operator "~" to each word automatically.
            // ==========================================
            String[] words = queryString.split("\\s+");
            StringBuilder fuzzyQueryStr = new StringBuilder();
            
            for (String word : words) {
                // Keep boolean operators normal, but make regular words "fuzzy"
                if (word.equalsIgnoreCase("AND") || word.equalsIgnoreCase("OR")) {
                    fuzzyQueryStr.append(word).append(" ");
                } else {
                    fuzzyQueryStr.append(word).append("~ "); // The ~ tells Lucene to allow typos!
                }
            }

            System.out.println("\n==================================================");
            System.out.println("Original Input : '" + queryString + "'");
            System.out.println("Processed Query: '" + fuzzyQueryStr.toString().trim() + "'");
            System.out.println("==================================================");

            // Parse the new fuzzy query
            Query query = parser.parse(fuzzyQueryStr.toString().trim());

            // Execute search
            TopDocs results = searcher.search(query, maxResults);
            ScoreDoc[] hits = results.scoreDocs;

            System.out.println("Found " + hits.length + " matching professors.\n");

            for (int i = 0; i < hits.length; i++) {
                int docId = hits[i].doc;
                float score = hits[i].score;
                Document doc = searcher.doc(docId);
                
                System.out.printf("%d. %s (Score: %.4f)\n", (i + 1), doc.get("name"), score);
                System.out.println("   Designation: " + doc.get("designation"));
                System.out.println("   Email: " + doc.get("mail"));
                System.out.println("--------------------------------------------------");
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error during search: " + e.getMessage());
        }
    }
}