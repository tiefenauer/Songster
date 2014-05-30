package main.info.tiefenauer.songster.model;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public class AnalyzerFactory {

	public static Analyzer create(AnalyzerConfig config){
		Analyzer analyzer;
		switch(config.analyzerType){
			case WHITESPACE:
				analyzer = new WhitespaceAnalyzer(Version.LUCENE_48);
				break;
			case SIMPLE:
				analyzer = new SimpleAnalyzer(Version.LUCENE_48);
				break;
			case CLASSIC:
				analyzer = new ClassicAnalyzer(Version.LUCENE_48);
				break;
			case ENGLISH:
				analyzer = new EnglishAnalyzer(Version.LUCENE_48);
				break;
			case CUSTOM:
				analyzer = new CustomAnalyzer(Version.LUCENE_48, config);
				break;
			case STANDARD:
			default:
				analyzer = new StandardAnalyzer(Version.LUCENE_48);
				break;
			}
		return analyzer;
	}
	
}
