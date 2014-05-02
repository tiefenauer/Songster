package main.info.tiefenauer.songster.view;

import main.info.tiefenauer.songster.event.CreateIndexEvent;
import main.info.tiefenauer.songster.event.PerformSearchEvent;
import main.info.tiefenauer.songster.event.SearchFinishedEvent;
import main.info.tiefenauer.songster.model.AnalyzerType;

import org.apache.lucene.document.Document;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import org.eclipse.swt.custom.StyledText;

public class SongsterApplication {
	
	@Inject EventBus eventBus;

	protected Shell shlSongster;
	private Text searchTI;
	private StyledText resultTA;
	private Button submitBtn;
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
		shlSongster.setSize(541, 403);
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
		grpAnalyzer.setBounds(10, 37, 505, 67);
		
		Button simpleRadio = new Button(grpAnalyzer, SWT.RADIO);
		simpleRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedAnalyzer = AnalyzerType.SIMPLE;
			}
		});
		simpleRadio.setBounds(10, 42, 113, 16);
		simpleRadio.setText("SimpleAnalyzer");
		
		Button whitespaceRadio = new Button(grpAnalyzer, SWT.RADIO);
		whitespaceRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
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
				selectedAnalyzer = AnalyzerType.STANDARD;
			}
		});
		standardRadio.setText("StandardAnalyzer");
		standardRadio.setBounds(189, 23, 113, 16);
		
		Button englishRadio = new Button(grpAnalyzer, SWT.RADIO);
		englishRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedAnalyzer = AnalyzerType.ENGLISH;
			}
		});
		englishRadio.setText("EnglishAnalyzer");
		englishRadio.setBounds(189, 44, 113, 16);
		
		Button classicRadio = new Button(grpAnalyzer, SWT.RADIO);
		classicRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedAnalyzer = AnalyzerType.CLASSIC;
			}
		});
		classicRadio.setText("ClassicAnalyzer");
		classicRadio.setBounds(358, 21, 137, 16);
		
		Button customRadio = new Button(grpAnalyzer, SWT.RADIO);
		customRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedAnalyzer = AnalyzerType.CUSTOM_WITH_SYNONYMS;
			}
		});
		customRadio.setText("CustomAnalyzer");
		customRadio.setBounds(358, 42, 137, 16);		
		
		searchTI = new Text(shlSongster, SWT.BORDER);
		searchTI.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				submitBtn.setEnabled(searchTI.getText().length() > 0);
			}
		});
		searchTI.setBounds(10, 10, 312, 21);
		
		submitBtn = new Button(shlSongster, SWT.NONE);
		submitBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				eventBus.register(SongsterApplication.this);
				eventBus.post(new PerformSearchEvent(searchTI.getText(), selectedAnalyzer));
			}
		});
		submitBtn.setEnabled(false);
		submitBtn.setBounds(336, 8, 75, 25);
		submitBtn.setText("Search");
		

		
		resultTA = new StyledText(shlSongster, SWT.BORDER);
		resultTA.setBounds(10, 110, 505, 224);

	}

	@Subscribe 
	public void onSearchPerformed(SearchFinishedEvent event){
		resultTA.setText("");
		for (Document doc : event.result){
			resultTA.append(doc.get("artist") + " - " + doc.get("title") + "\r\n");
		}
	}
}
