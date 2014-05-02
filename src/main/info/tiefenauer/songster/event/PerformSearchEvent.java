package main.info.tiefenauer.songster.event;

import main.info.tiefenauer.songster.model.AnalyzerType;


public class PerformSearchEvent extends Event {

	public String query;
	public AnalyzerType type;
	
	public PerformSearchEvent(String query, AnalyzerType type){
		this.query = query;
		this.type = type;
	}
	
}
