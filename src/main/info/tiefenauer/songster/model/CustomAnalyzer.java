package main.info.tiefenauer.songster.model;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;
import org.apache.lucene.util.Version;

/**
 * @author Daniel
 *
 */
public class CustomAnalyzer extends Analyzer {

	private static final String synFilePath = "assets\\wn_s.pl";
	
	private AnalyzerConfig config;
	private final Version matchVersion;
	
	/**
	 * @param version
	 */
	protected CustomAnalyzer(Version version) {
		this.matchVersion = version;
		this.config = new AnalyzerConfig();
		this.config.includeSynonyms = false;
	}
	
	/**
	 * @param version
	 * @param config
	 */
	public CustomAnalyzer(Version version, AnalyzerConfig config){
		this.matchVersion = version;
		this.config = config;
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.Analyzer#createComponents(java.lang.String, java.io.Reader)
	 */
	@Override
	protected TokenStreamComponents createComponents(final String fieldName, Reader reader) {
	    final Tokenizer source = new StandardTokenizer(matchVersion, reader);
	    TokenStream tok = new StandardFilter(matchVersion, source);
	    tok = new LowerCaseFilter(matchVersion, tok);

	    if (config.includeSynonyms){
	    	System.out.println("Synonyms enabled");
	    	
	    	SynonymMap mySynonymMap = null;
	    	try {
				mySynonymMap = buildSynonym();
			} catch (IOException | ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	tok = new SynonymFilter(tok, mySynonymMap, true);
	    }

	    // Stemming
	    if (config.useStemFilter){
	    	System.out.println("Stemming enabled");
	    	tok = new PorterStemFilter(tok);	    
	    }

	    // NO STOPWORDS BECAUSE THEY DON'T WORK WITH WORDNET!!!
	    //tok = new StopFilter(matchVersion, tok, stopwords);


	    return new TokenStreamComponents(source, tok) {
	      @Override
	      protected void setReader(final Reader reader) throws IOException {
	        super.setReader(reader);
	      }
	    };
	}
	
	/**
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	private SynonymMap buildSynonym() throws IOException, ParseException
	{
	    FileReader rulesReader = new FileReader(synFilePath);
	    SynonymMap.Builder parser = null;
	    parser = new WordnetSynonymParser(true, true, new CustomAnalyzer(Version.LUCENE_48));
	    ((WordnetSynonymParser) parser).parse(rulesReader);         
	    SynonymMap synonymMap = parser.build();
	    return synonymMap;
	}

}
