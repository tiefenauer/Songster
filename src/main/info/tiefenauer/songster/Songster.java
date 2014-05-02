package main.info.tiefenauer.songster;

import main.info.tiefenauer.songster.bootstrap.SongFinderModule;
import main.info.tiefenauer.songster.command.CreateIndex;
import main.info.tiefenauer.songster.command.PerformSearch;
import main.info.tiefenauer.songster.view.SongsterApplication;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Songster {

	public static void main(String[] args) {
		EventBus eventbus = new EventBus();
		System.out.println(eventbus);
		Injector i = Guice.createInjector(new SongFinderModule(eventbus));
		
		// map events
		eventbus.register(i.getInstance(CreateIndex.class));
		eventbus.register(i.getInstance(PerformSearch.class));
		
		SongsterApplication view = i.getInstance(SongsterApplication.class);
		view.open();
				
		/*
		SongIndexer indexer = new SongIndexer();
		indexer.createIndex();
		*/
	}
}
