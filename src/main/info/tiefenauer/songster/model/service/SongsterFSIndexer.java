package main.info.tiefenauer.songster.model.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class SongsterFSIndexer extends SongsterIndexer {
	
	private static File defaultIndexFSDirectory = new File("C:\\Users\\Daniel\\Documents\\ZHAW\\Semester 6\\Information Retrieval\\index");
	private IndexReader indexReader;
	
	/**
	 * Default constructor
	 */
	public SongsterFSIndexer(){
		sourceDirectory = defaultSourceDirectory;
	}
	/**
	 * Constructor
	 */
	public SongsterFSIndexer(File directory) {
		if (directory.exists())
			sourceDirectory = directory;
		else
			sourceDirectory = defaultSourceDirectory;
		try {
			indexReader = DirectoryReader.open(indexDirectory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Index mit StandardAnalyzer im RAM erstellen
	 */
	public void createIndex() {
		createIndex(new StandardAnalyzer(Version.LUCENE_47));
	}
	
	/**
	 * Index mit Analyzer im Default-Dir erstellen
	 */
	public void createIndex(Analyzer analyzer) {
		indexDirectory = prepareFSDirectory();
		createIndex(analyzer, indexDirectory);
	}
	
	private FSDirectory prepareFSDirectory(){
		FSDirectory dir = null;
		try {
			dir = new SimpleFSDirectory(defaultIndexFSDirectory);
			if (dir.getDirectory().exists()){
				//FileUtils.deleteDirectory(dir.getDirectory());
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return dir;
	}
	
	@Override
	public IndexReader getReader() {
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(indexDirectory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reader;
	}
	
}
