package main.info.tiefenauer.songster.event;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;

public class SearchFinishedEvent extends Event {

	public ScoreDoc[] result;
	public IndexSearcher searcher;
	
	/**
	 * Constructor
	 * @param result
	 * @param searcher
	 */
	public SearchFinishedEvent(ScoreDoc[] result, IndexSearcher searcher){
		this.result = result;
		this.searcher = searcher;
	}
}
