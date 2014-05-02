package main.info.tiefenauer.songster.event;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;

public class SearchFinishedEvent extends Event {

	public List<Document> result;
	
	public SearchFinishedEvent(List<Document> result){
		this.result = result;
	}
}
