package com.pes.matching;

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
    private static final String INDEX_DIR = "lucene_index";

    public static void buildIndex(List<Professor> professors) {
        try {
            Directory directory = FSDirectory.open(Paths.get(INDEX_DIR));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            IndexWriter writer = new IndexWriter(directory, config);

            System.out.println("Building Upgraded Lucene Index...");
            for (Professor prof : professors) {
                Document doc = new Document();

                // Stored strings (Not tokenized, just for UI display)
                doc.add(new StringField("mail", prof.getMail() != null ? prof.getMail() : "", Field.Store.YES));
                doc.add(new StringField("image", prof.getImage() != null ? prof.getImage() : "", Field.Store.YES));

                // Tokenized Text Fields (The actual searchable data)
                doc.add(new TextField("name", prof.getName() != null ? prof.getName() : "", Field.Store.YES));
                doc.add(new TextField("designation", prof.getDesignation() != null ? prof.getDesignation() : "", Field.Store.YES));
                doc.add(new TextField("research", prof.getResearch() != null ? prof.getResearch() : "", Field.Store.YES));
                doc.add(new TextField("teaching", prof.getTeaching() != null ? prof.getTeaching() : "", Field.Store.YES));
                doc.add(new TextField("about", prof.getAbout() != null ? prof.getAbout() : "", Field.Store.YES));
                doc.add(new TextField("publications", prof.getPubJournals() + " " + prof.getPubConfs(), Field.Store.YES));
                doc.add(new TextField("department", prof.getDepartment() != null ? prof.getDepartment() : "", Field.Store.YES));
                doc.add(new TextField("campus", prof.getCampus() != null ? prof.getCampus() : "", Field.Store.YES));

                writer.addDocument(doc);
            }
            writer.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}