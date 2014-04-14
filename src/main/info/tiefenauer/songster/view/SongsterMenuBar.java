package main.info.tiefenauer.songster.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import main.info.tiefenauer.songster.event.CreateIndexEvent;
import main.info.tiefenauer.songster.event.ShowAbout;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

public class SongsterMenuBar extends JMenu {

	@Inject EventBus eventBus;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JMenuItem rebuildMenuItem;
	private JMenuItem aboutMenuItem;
	
	public SongsterMenuBar(){
		init();
		layoutComponents();
	}
	
	private void init(){
		rebuildMenuItem = new JMenuItem("Rebuld Index");
		aboutMenuItem = new JMenuItem("About");
		rebuildMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventBus.post(new CreateIndexEvent());
			}
		});
		aboutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventBus.post(new ShowAbout());
			}
		});
	}
	private void layoutComponents(){
		add(rebuildMenuItem);
		add(aboutMenuItem);
	}
	
}
