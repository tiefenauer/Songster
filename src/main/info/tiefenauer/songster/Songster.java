package main.info.tiefenauer.songster;

import main.info.tiefenauer.songster.bootstrap.SongFinderModule;
import main.info.tiefenauer.songster.command.CreateIndex;
import main.info.tiefenauer.songster.command.PerformSearch;
import main.info.tiefenauer.songster.view.SongsterView;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Songster {

	public static void main(String[] args) {
		Injector i = Guice.createInjector(new SongFinderModule());
		EventBus eventbus = i.getInstance(EventBus.class);
		
		// map events
		eventbus.register(i.getInstance(CreateIndex.class));
		eventbus.register(i.getInstance(PerformSearch.class));
		
		SongsterView view = i.getInstance(SongsterView.class);
		view.setVisible(true);
				
		/*
		SongIndexer indexer = new SongIndexer();
		indexer.createIndex();
		*/
	}
}
