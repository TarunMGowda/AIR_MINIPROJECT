package com.pes.matching;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {

    // The folder where our Inverted Index will be saved on your computer
    private static final String INDEX_DIR = "lucene_index";

    public static void buildIndex(List<Professor> professors) {
        try {
            // 1. Where to store the index
            Directory directory = FSDirectory.open(Paths.get(INDEX_DIR));

            // 2. The Analyzer (Handles lowercasing, removing stop words like "and", "the")
            Analyzer analyzer = new StandardAnalyzer();

            // 3. Configure the IndexWriter
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE); // Overwrite index if it exists
            
            try (IndexWriter writer = new IndexWriter(directory, config)) {
                System.out.println("Building Lucene Index...");

                // 4. Loop through our CSV data and convert them to Lucene Documents
                int count = 0;
                for (Professor prof : professors) {
                    Document doc = new Document();

                    // StringField = Exact Match only. We store it so we can display it later.
                    doc.add(new StringField("name", prof.getName() != null ? prof.getName() : "", Field.Store.YES));
                    doc.add(new StringField("mail", prof.getMail() != null ? prof.getMail() : "", Field.Store.YES));
                    doc.add(new StringField("designation", prof.getDesignation() != null ? prof.getDesignation() : "", Field.Store.YES));

                    // TextField = Tokenized and Processed. This is what the search engine actually looks at!
                    doc.add(new TextField("research", prof.getResearch() != null ? prof.getResearch() : "", Field.Store.YES));
                    doc.add(new TextField("teaching", prof.getTeaching() != null ? prof.getTeaching() : "", Field.Store.YES));
                    doc.add(new TextField("about", prof.getAbout() != null ? prof.getAbout() : "", Field.Store.YES));
                    doc.add(new TextField("publications", prof.getPublicationsJournals() + " " + prof.getPublicationsConferences(), Field.Store.YES));

                    // Add the document to the index
                    writer.addDocument(doc);
                    count++;
                }

                // 5. Commit and close
                writer.commit();
                System.out.println("Successfully indexed " + count + " professors into '" + INDEX_DIR + "' directory.");
            }

        } catch (IOException e) {
            System.out.println("Error building the index: " + e.getMessage());
            e.printStackTrace();
        }
    }
}