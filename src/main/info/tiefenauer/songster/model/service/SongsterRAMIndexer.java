package main.info.tiefenauer.songster.model.service;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class SongsterRAMIndexer extends SongsterIndexer{

	/**
	 * Default constructor
	 */
	public SongsterRAMIndexer(){
		sourceDirectory = defaultSourceDirectory;
	}
	/**
	 * Constructor
	 */
	public SongsterRAMIndexer(File directory) {
		if (directory.exists())
			sourceDirectory = directory;
		else
			sourceDirectory = defaultSourceDirectory;
	}
	
	@Override
	public void createIndex() {
		createIndex(new StandardAnalyzer(Version.LUCENE_47));
	}

	@Override
	public void createIndex(Analyzer analyzer) {
		indexDirectory = new RAMDirectory();
		createIndex(analyzer, new RAMDirectory());
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
