package main.info.tiefenauer.songster.model.service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class SongIndexer {
	
	private static File defaultSourceDirectory = new File("C:\\Users\\Daniel\\Documents\\ZHAW\\Semester 6\\Information Retrieval\\lyrics");
	private static File defaultIndexDirectory = new File("C:\\Users\\Daniel\\Documents\\ZHAW\\Semester 6\\Information Retrieval\\index");
	private File sourceDirectory;
	private Directory indexDirectory;
	private Analyzer indexAnalyzer;
	private IndexWriter indexWriter;
	
	
	/**
	 * Default constructor
	 */
	public SongIndexer(){
		sourceDirectory = defaultSourceDirectory;
		init();
	}
	/**
	 * Constructor
	 */
	public SongIndexer(File directory) {
		if (directory.exists())
			sourceDirectory = directory;
		else
			sourceDirectory = defaultSourceDirectory;
		init();
	}
	
	private void init(){
		indexAnalyzer = new StandardAnalyzer(Version.LUCENE_47);
	}
	
	/**
	 * Create index in directory
	 * @param directory
	 */
	public void createIndex() {
		if (defaultIndexDirectory.exists()){
			try {
				FileUtils.deleteDirectory(defaultIndexDirectory);
				indexDirectory = new SimpleFSDirectory(defaultIndexDirectory);
				indexWriter = new IndexWriter(indexDirectory, new IndexWriterConfig(Version.LUCENE_47, indexAnalyzer));

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
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
				Document doc = new Document();
				doc.add(new StringField("path", path, Store.YES));
				doc.add(new StringField("artist", artist, Store.YES));
				doc.add(new StringField("title", title, Store.YES));
				doc.add(new TextField("lyrics", lyrics, Store.YES));
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
