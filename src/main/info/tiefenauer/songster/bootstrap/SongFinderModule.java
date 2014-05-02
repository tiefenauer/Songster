package main.info.tiefenauer.songster.bootstrap;

import main.info.tiefenauer.songster.model.service.SongsterFSIndexer;
import main.info.tiefenauer.songster.model.service.SongsterIndexer;
import main.info.tiefenauer.songster.view.SongsterApplication;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;

public class SongFinderModule extends AbstractModule {

	private EventBus eventbus = new EventBus();
	
	public SongFinderModule(EventBus eventBus){
		this.eventbus = eventBus;
	}
	
	@Override
	protected void configure() {
		bind(EventBus.class).toInstance(eventbus);
		bind(SongsterApplication.class);
		bind(SongsterIndexer.class).to(SongsterFSIndexer.class);
		
		// models
		bind(SongsterFSIndexer.class);
	}

}
