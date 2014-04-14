package main.info.tiefenauer.songster.event;


public class PerformSearchEvent extends Event {

	public String query;
	
	public PerformSearchEvent(){
		query = "";
	}
	
	public PerformSearchEvent(String query){
		this.query = query;
	}
}
