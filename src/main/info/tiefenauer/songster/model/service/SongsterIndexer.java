package main.info.tiefenauer.songster.model.service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;


public abstract class SongsterIndexer {
	
	protected static File defaultSourceDirectory = new File("C:\\Users\\Daniel\\Documents\\ZHAW\\Semester 6\\Information Retrieval\\lyrics");
	protected File sourceDirectory;
	protected IndexWriter indexWriter;
	protected Directory indexDirectory = null;

	public abstract void createIndex();
	public abstract void createIndex(Analyzer analyzer);
	public abstract IndexReader getReader();
	
	public IndexWriter getWriter(){
		return indexWriter;
	}
	
	/**
	 * Index mit Analyzer im Directory erstellen
	 */
	protected void createIndex(Analyzer analyzer, Directory directory) {
		try {
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
			config.setOpenMode(OpenMode.CREATE);
			indexWriter = new IndexWriter(directory, config);
			indexWriter.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writeToIndex();
	}
	
	/**
	 * Alle Files im Directory einlesen und in Index schreiben
	 */
	protected void writeToIndex(){
		int numDocs = 0;
		Collection<File> files = FileUtils.listFiles(sourceDirectory, FileFilterUtils.suffixFileFilter(".txt"), TrueFileFilter.INSTANCE);
		for (File file :files){
			String[] nameParts = FilenameUtils.removeExtension(file.getName()).split(" - ");
			String path = file.getPath();
			String artist = "";
			String title = "";
			String lyrics = "";
			
			if (nameParts.length > 0)
				artist = nameParts[0];
			if(nameParts.length > 1)
				title = nameParts[1];
			
			try {
				lyrics = FileUtils.readFileToString(file);
				
				// Felder erstellen
				Field titleField = new StringField("title", title, Store.YES);
				Field pathField = new StringField("path", path, Store.YES);
				Field artistField = new StringField("artist", artist, Store.YES);
				Field lyricsField = new TextField("lyrics", lyrics, Store.YES); 
				
				// Dokument erstellen
				Document doc = new Document();
				doc.add(titleField);
				doc.add(pathField);				
				doc.add(artistField);				
				doc.add(lyricsField);
				
				indexWriter.addDocument(doc);
				numDocs++;
				System.out.println("Added: " + file);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			indexWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    System.out.println("");
	    System.out.println("************************");
	    System.out.println(numDocs + " documents added.");
	    System.out.println("************************");	
	}

}
