package main.info.tiefenauer.songster.command;

import main.info.tiefenauer.songster.event.CreateIndexEvent;
import main.info.tiefenauer.songster.model.service.SongIndexer;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

public class CreateIndex  {

	@Inject SongIndexer indexer;
	
	@Subscribe
	public void execute(CreateIndexEvent event){
		System.out.println("Creating index...");
		indexer.createIndex();
	}

}
