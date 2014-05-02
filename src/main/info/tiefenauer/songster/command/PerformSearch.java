package main.info.tiefenauer.songster.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import main.info.tiefenauer.songster.event.PerformSearchEvent;
import main.info.tiefenauer.songster.event.SearchFinishedEvent;
import main.info.tiefenauer.songster.model.AnalyzerFactory;
import main.info.tiefenauer.songster.model.service.SongsterIndexer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.util.Version;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;


public class PerformSearch extends Observable{

	public IndexSearcher indexSearcher;
	@Inject public EventBus eventBus;
	@Inject public SongsterIndexer indexer;
	
	@Subscribe
	public void performSearch(PerformSearchEvent event) {
		System.out.println("Performing search: " + event.query);
		Analyzer analyzer = AnalyzerFactory.create(event.type);
		
		// recreate index
		indexer.createIndex(analyzer);
		
		IndexReader reader;
		try {
			reader = indexer.getReader();
			indexSearcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(50, true);
			Query q = new QueryParser(Version.LUCENE_47, "lyrics", analyzer).parse(event.query);
			indexSearcher.search(q, collector);;
			
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			List<Document> result = new ArrayList<Document>(); 
			
			System.out.println("Found " + hits.length + " hits");
			for (ScoreDoc scoreDoc : hits){
				Document doc = indexSearcher.doc(scoreDoc.doc);
				result.add(doc);
				System.out.println(doc.get("path") + " score=" + scoreDoc.score);
			}
			eventBus.post(new SearchFinishedEvent(result));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
