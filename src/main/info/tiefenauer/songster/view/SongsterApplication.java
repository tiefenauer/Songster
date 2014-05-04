package main.info.tiefenauer.songster.view;

import java.io.IOException;

import main.info.tiefenauer.songster.event.CreateIndexEvent;
import main.info.tiefenauer.songster.event.PerformSearchEvent;
import main.info.tiefenauer.songster.event.SearchFinishedEvent;
import main.info.tiefenauer.songster.model.AnalyzerConfig;
import main.info.tiefenauer.songster.model.AnalyzerType;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

public class SongsterApplication {
	
	@Inject EventBus eventBus;

	protected Shell shlSongster;
	private Text searchTI;
	private StyledText resultTA;
	private Button submitBtn;
	private Button stemCheckbox;
	private Button synonymCheckbox;
	private Button titleCheckbox;
	private Button lyricsCheckbox;
	private Button artistCheckbox;
	private Scale titleBoostScale;
	private Label titleBoostLabel;
	private Scale lyricsBoostScale;
	private Label lyricsBoostLabel;
	private AnalyzerType selectedAnalyzer = AnalyzerType.WHITESPACE;

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlSongster.open();
		shlSongster.layout();
		while (!shlSongster.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlSongster = new Shell();
		shlSongster.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent arg0) {
				System.exit(0);
			}
		});
		shlSongster.setSize(541, 645);
		shlSongster.setText("Songster");
		shlSongster.setLayout(null);
		
		Menu menu = new Menu(shlSongster, SWT.BAR);
		shlSongster.setMenuBar(menu);
		
		MenuItem mntmRecreateIndex = new MenuItem(menu, SWT.NONE);
		mntmRecreateIndex.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(eventBus);
				eventBus.post(new CreateIndexEvent());
			}
		});
		mntmRecreateIndex.setText("Recreate Index");
		
		MenuItem mntmAbout = new MenuItem(menu, SWT.NONE);
		mntmAbout.setText("About");
		
		Group grpAnalyzer = new Group(shlSongster, SWT.NONE);
		grpAnalyzer.setText("Analyzer");
		grpAnalyzer.setBounds(10, 85, 505, 67);
		
		Button simpleRadio = new Button(grpAnalyzer, SWT.RADIO);
		simpleRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disableParameters();
				selectedAnalyzer = AnalyzerType.SIMPLE;
			}
		});
		simpleRadio.setBounds(10, 42, 113, 16);
		simpleRadio.setText("SimpleAnalyzer");
		
		Button whitespaceRadio = new Button(grpAnalyzer, SWT.RADIO);
		whitespaceRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disableParameters();
				selectedAnalyzer = AnalyzerType.WHITESPACE;
			}
		});
		whitespaceRadio.setSelection(true);
		whitespaceRadio.setText("WhitespaceAnalyzer");
		whitespaceRadio.setBounds(10, 21, 136, 16);
		
		Button standardRadio = new Button(grpAnalyzer, SWT.RADIO);
		standardRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disableParameters();
				selectedAnalyzer = AnalyzerType.STANDARD;
			}
		});
		standardRadio.setText("StandardAnalyzer");
		standardRadio.setBounds(189, 23, 113, 16);
		
		Button englishRadio = new Button(grpAnalyzer, SWT.RADIO);
		englishRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disableParameters();
				selectedAnalyzer = AnalyzerType.ENGLISH;
			}
		});
		englishRadio.setText("EnglishAnalyzer");
		englishRadio.setBounds(189, 44, 113, 16);
		
		Button classicRadio = new Button(grpAnalyzer, SWT.RADIO);
		classicRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disableParameters();
				selectedAnalyzer = AnalyzerType.CLASSIC;
			}
		});
		classicRadio.setText("ClassicAnalyzer");
		classicRadio.setBounds(358, 21, 137, 16);
		
		Button customRadio = new Button(grpAnalyzer, SWT.RADIO);
		customRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				enableParameters();
				selectedAnalyzer = AnalyzerType.CUSTOM;
			}
		});
		customRadio.setText("CustomAnalyzer");
		customRadio.setBounds(358, 42, 137, 16);		
		
		searchTI = new Text(shlSongster, SWT.BORDER);
		searchTI.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13)
					performSearch();
			}
		});
		searchTI.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				submitBtn.setEnabled(searchTI.getText().length() > 0);
			}
		});
		searchTI.setBounds(10, 10, 424, 21);
		
		submitBtn = new Button(shlSongster, SWT.NONE);
		submitBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				performSearch();
			}
		});
		submitBtn.setEnabled(false);
		submitBtn.setBounds(440, 6, 75, 25);
		submitBtn.setText("Search");
		
		resultTA = new StyledText(shlSongster, SWT.V_SCROLL);
		resultTA.setAlwaysShowScrollBars(false);
		resultTA.setBounds(10, 253, 501, 323);
		
		Group parameterGroup = new Group(shlSongster, SWT.NONE);
		parameterGroup.setText("Parameter");
		parameterGroup.setBounds(10, 158, 505, 89);
		
		stemCheckbox = new Button(parameterGroup, SWT.CHECK);
		stemCheckbox.setEnabled(false);
		stemCheckbox.setBounds(10, 22, 93, 16);
		stemCheckbox.setText("Stem words");
		
		synonymCheckbox = new Button(parameterGroup, SWT.CHECK);
		synonymCheckbox.setEnabled(false);
		synonymCheckbox.setBounds(190, 22, 153, 16);
		synonymCheckbox.setText("Include Synonyms");
		
		titleBoostScale = new Scale(parameterGroup, SWT.NONE);
		titleBoostScale.setEnabled(false);
		titleBoostScale.addListener(SWT.Selection,  new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				titleBoostLabel.setText("" + titleBoostScale.getSelection());
			}
		});
		titleBoostScale.setMaximum(10);
		titleBoostScale.setMinimum(1);
		titleBoostScale.setSelection(1);
		titleBoostScale.setBounds(71, 44, 129, 42);
		
		Label lblTitleBoost = new Label(parameterGroup, SWT.NONE);
		lblTitleBoost.setBounds(10, 55, 55, 16);
		lblTitleBoost.setText("Title Boost");
		
		titleBoostLabel = new Label(parameterGroup, SWT.NONE);
		titleBoostLabel.setBounds(206, 55, 21, 16);
		titleBoostLabel.setText("1");
		
		Label lblLyricsBoost = new Label(parameterGroup, SWT.NONE);
		lblLyricsBoost.setBounds(270, 55, 68, 15);
		lblLyricsBoost.setText("Lyrics Boost");
		
		lyricsBoostScale = new Scale(parameterGroup, SWT.NONE);
		lyricsBoostScale.setEnabled(false);
		lyricsBoostScale.addListener(SWT.Selection,  new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				lyricsBoostLabel.setText("" + lyricsBoostScale.getSelection());
			}
		});
		lyricsBoostScale.setMaximum(10);
		lyricsBoostScale.setMinimum(1);
		lyricsBoostScale.setBounds(344, 44, 129, 42);
		
		lyricsBoostLabel = new Label(parameterGroup, SWT.NONE);
		lyricsBoostLabel.setBounds(479, 55, 16, 15);
		lyricsBoostLabel.setText("1");
		
		Group grpFields = new Group(shlSongster, SWT.NONE);
		grpFields.setText("Fields");
		grpFields.setBounds(10, 37, 505, 42);
		
		titleCheckbox = new Button(grpFields, SWT.CHECK);
		titleCheckbox.setSelection(true);
		titleCheckbox.setBounds(10, 23, 93, 16);
		titleCheckbox.setText("Title");
		
		lyricsCheckbox = new Button(grpFields, SWT.CHECK);
		lyricsCheckbox.setSelection(true);
		lyricsCheckbox.setBounds(121, 23, 93, 16);
		lyricsCheckbox.setText("Lyrics");
		
		artistCheckbox = new Button(grpFields, SWT.CHECK);
		artistCheckbox.setBounds(233, 23, 93, 16);
		artistCheckbox.setText("Artist");

	}
	
	private void enableParameters(){
		if (!stemCheckbox.isEnabled())
			stemCheckbox.setEnabled(true);
		if (!synonymCheckbox.isEnabled())
			synonymCheckbox.setEnabled(true);
		if (!titleBoostScale.isEnabled())
			titleBoostScale.setEnabled(true);
		if (!lyricsBoostScale.isEnabled())
			lyricsBoostScale.setEnabled(true);
	}
	
	private void disableParameters(){
		if (stemCheckbox.isEnabled())
			stemCheckbox.setEnabled(false);
		if (synonymCheckbox.isEnabled())
			synonymCheckbox.setEnabled(false);
		if (titleBoostScale.isEnabled())
			titleBoostScale.setEnabled(false);
		if (lyricsBoostScale.isEnabled())
			lyricsBoostScale.setEnabled(false);
	}
	
	private void performSearch(){	
		AnalyzerConfig config = new AnalyzerConfig();
		config.analyzerType = selectedAnalyzer;
		config.searchInTitle = titleCheckbox.getSelection();
		config.searchInLyrics = lyricsCheckbox.getSelection();
		config.searchInArtist = artistCheckbox.getSelection();
		
		config.titleBoost = titleBoostScale.isEnabled()?titleBoostScale.getSelection():1;
		config.lyricsBoost = lyricsBoostScale.isEnabled()?lyricsBoostScale.getSelection():1;
		
		config.includeSynonyms = synonymCheckbox.isEnabled() && synonymCheckbox.getSelection();		
		config.useStemFilter = stemCheckbox.isEnabled() && stemCheckbox.getSelection();
		
		eventBus.register(SongsterApplication.this);
		eventBus.post(new PerformSearchEvent(searchTI.getText(), config));
	}

	@Subscribe 
	public void onSearchPerformed(SearchFinishedEvent event) throws IOException{
		resultTA.setText("");
		int i=0;
		for (ScoreDoc scoreDoc : event.result){
			Document doc = event.searcher.doc(scoreDoc.doc);
			String resultString = "" + ++i + " " + doc.get("artist") + " - " + doc.get("title") + " (" + scoreDoc.score + ")\r\n"; 
			resultTA.append(resultString);
		}
	}
}
