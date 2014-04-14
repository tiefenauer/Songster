package main.info.tiefenauer.songster.command;

import java.io.File;
import java.io.IOException;

import main.info.tiefenauer.songster.event.PerformSearchEvent;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.google.common.eventbus.Subscribe;


public class PerformSearch {

	public IndexSearcher indexSearcher;
	
	@Subscribe
	public void performSearch(PerformSearchEvent event) {
		System.out.println("Performing search: " + event.query);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
		IndexReader reader;
		try {
			reader = DirectoryReader.open(FSDirectory.open(new File("C:\\Users\\Daniel\\Documents\\ZHAW\\Semester 6\\Information Retrieval\\index")));
			indexSearcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
			Query q = new QueryParser(Version.LUCENE_47, "lyrics", analyzer).parse(event.query);
			indexSearcher.search(q, collector);;
			
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			
			System.out.println("Found " + hits.length + " hits");
			for (ScoreDoc scoreDoc : hits){
				Document doc = indexSearcher.doc(scoreDoc.doc);
				System.out.println(doc.get("path") + " score=" + scoreDoc.score);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
