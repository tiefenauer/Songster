package main.info.tiefenauer.songster.command;

import java.io.IOException;
import java.util.Observable;

import main.info.tiefenauer.songster.event.PerformSearchEvent;
import main.info.tiefenauer.songster.event.SearchFinishedEvent;
import main.info.tiefenauer.songster.model.AnalyzerFactory;
import main.info.tiefenauer.songster.model.service.SongsterIndexer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.util.Version;
import org.tartarus.snowball.ext.PorterStemmer;

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
		Analyzer analyzer = AnalyzerFactory.create(event.config);
		
		// preprocess query if stemming is enabled
		if (event.config.useStemFilter){
			PorterStemmer stemmer = new PorterStemmer();
			stemmer.setCurrent(event.query.toLowerCase());
			stemmer.stem();
			event.query = stemmer.getCurrent();
		}
		
		// recreate index
		indexer.createIndex(analyzer);
		
		IndexReader reader;
		try {
			reader = indexer.getReader();
			indexSearcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(50, true);
			BooleanQuery booleanQuery = new BooleanQuery();
			if (event.config.searchInLyrics){
				Query lyricsQuery = new TermQuery(new Term("lyrics", event.query));
				lyricsQuery.setBoost(event.config.lyricsBoost);
				booleanQuery.add(lyricsQuery, Occur.SHOULD);
			}
			if (event.config.searchInTitle){
				Query titleQuery = new TermQuery(new Term("title", event.query));
				titleQuery.setBoost(event.config.titleBoost);
				booleanQuery.add(titleQuery, Occur.SHOULD);
			}
			if (event.config.searchInArtist){
				Query artistQuery = new TermQuery(new Term("artist", event.query));
				booleanQuery.add(artistQuery, Occur.SHOULD);
			}
			
			Query q = new QueryParser(Version.LUCENE_48, "lyrics", analyzer).parse(event.query);
			indexSearcher.search(q, collector);
			
			ScoreDoc[] hits = collector.topDocs().scoreDocs;		
			System.out.println("Found " + hits.length + " hits");
			for(ScoreDoc doc : hits){
				Explanation explanation = indexSearcher.explain(q, doc.doc);
				System.out.println(explanation.getDescription());
				System.out.println(explanation.toString());
				//System.out.println(explanation.getDetails());
			}
			eventBus.post(new SearchFinishedEvent(hits, indexSearcher));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
