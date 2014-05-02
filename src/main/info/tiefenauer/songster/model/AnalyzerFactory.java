package main.info.tiefenauer.songster.model;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public class AnalyzerFactory {

	public static Analyzer create(AnalyzerType type){
		switch(type){
		case WHITESPACE:
			return new WhitespaceAnalyzer(Version.LUCENE_47);
		case SIMPLE:
			return new SimpleAnalyzer(Version.LUCENE_47);
		case STANDARD:
			return new StandardAnalyzer(Version.LUCENE_47);
		case CLASSIC:
			return new ClassicAnalyzer(Version.LUCENE_47);
		case ENGLISH:
			return new EnglishAnalyzer(Version.LUCENE_47);
		}
		return new StandardAnalyzer(Version.LUCENE_47);
	}
	
}
