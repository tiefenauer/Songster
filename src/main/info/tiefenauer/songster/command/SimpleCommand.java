package main.info.tiefenauer.songster.command;

import main.info.tiefenauer.songster.event.Event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

public abstract class SimpleCommand {
	
	@Inject public EventBus eventBus;
	
	public SimpleCommand(){
		execute();
	}

	@Subscribe
	private void handleEvent(Event event) {
		System.out.println("bla");
		execute();
	}
	
	@Subscribe
	public abstract void execute(Event ...argsEvents);
}
