package main.info.tiefenauer.songster.model;

public enum AnalyzerType {

	WHITESPACE("WhitespaceAnalyzer"),
	SIMPLE("SimpleAnalyzer"),
	ENGLISH("EnglishAnalyzer"),
	STANDARD("StandardAnalyzer"),
	CLASSIC("ClassicAnalyzer"),
	CUSTOM("Custom");
	
	public final String type;
	
	private AnalyzerType(String type) {
		this.type = type;
	}
}
