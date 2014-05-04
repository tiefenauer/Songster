package main.info.tiefenauer.songster.event;

import main.info.tiefenauer.songster.model.AnalyzerConfig;


public class PerformSearchEvent extends Event {

	public String query;
	public AnalyzerConfig config;
	
	/**
	 * @param query
	 * @param type
	 * @param config
	 */
	public PerformSearchEvent(String query, AnalyzerConfig config){
		this.query = query;
		this.config = config;
	}
	
}
