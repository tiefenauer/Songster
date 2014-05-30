package main.info.tiefenauer.songster.model;

public class AnalyzerConfig {

	public AnalyzerType analyzerType = AnalyzerType.STANDARD;
	
	public boolean searchInTitle = true;
	public boolean searchInLyrics = true;
	public boolean searchInArtist = false;
	
	public int titleBoost = 1;
	public int lyricsBoost = 1;
	
	public boolean useStemFilter = false;
	public boolean includeSynonyms = false;
}
