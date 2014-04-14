package main.info.tiefenauer.songster.bootstrap;

import main.info.tiefenauer.songster.model.service.SongIndexer;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;

public class SongFinderModule extends AbstractModule {

	private final EventBus eventbus = new EventBus();
	
	@Override
	protected void configure() {
		bind(EventBus.class).toInstance(eventbus);
		
		// models
		bind(SongIndexer.class);
	}

}
