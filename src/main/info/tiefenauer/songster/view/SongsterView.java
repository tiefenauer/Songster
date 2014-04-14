package main.info.tiefenauer.songster.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.info.tiefenauer.songster.event.CreateIndexEvent;
import main.info.tiefenauer.songster.event.PerformSearchEvent;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

public class SongsterView extends JFrame {

	@Inject EventBus eventBus;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LayoutManager layout;
	private JMenuBar menuBar;
	private JTextField searchTI;
	private JButton submitButton;
	private JLabel statsLabel;
	
	public SongsterView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		addComponents();
	}

	/**
	 * initialize
	 */
	private void init(){
		setSize(new Dimension(500, 400));
		setResizable(false);
		layout = new BorderLayout();
		setLayout(layout);
		searchTI = new JTextField();
		submitButton = new JButton();
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventBus.post(new PerformSearchEvent(searchTI.getText()));
			}
		});
		statsLabel = new JLabel();
	}
	
	/**
	 * add Components
	 */
	private void addComponents(){
		menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenuItem rebuildItem = new JMenuItem("Rebuild Index");
		rebuildItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventBus.post(new CreateIndexEvent());
			}
		});
		fileMenu.add(rebuildItem);
		setJMenuBar(menuBar);
		
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new GridLayout());
		searchPanel.add(searchTI);
		searchPanel.add(submitButton);
		add(searchPanel, BorderLayout.NORTH);
		
		add(statsLabel, BorderLayout.SOUTH);
	}
}
