package main.info.tiefenauer.songster.command;

import main.info.tiefenauer.songster.event.CreateIndexEvent;
import main.info.tiefenauer.songster.model.service.SongsterFSIndexer;
import main.info.tiefenauer.songster.model.service.SongsterIndexer;

import com.google.common.eventbus.Subscribe;

public class CreateIndex  {

	@Subscribe
	public void execute(CreateIndexEvent event){
		System.out.println("Creating index...");
		SongsterIndexer indexer = new SongsterFSIndexer();
		indexer.createIndex();
	}

}
