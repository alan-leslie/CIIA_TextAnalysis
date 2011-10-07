package com.alag.ci.textanalysis.lucene.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import com.alag.ci.textanalysis.*;

public class SynonymPhraseStopWordAnalyzer extends Analyzer{
    private  SynonymsCache synonymsCache = null;
    private PhrasesCache phrasesCache = null;
    
    public SynonymPhraseStopWordAnalyzer(SynonymsCache synonymsCache,
            PhrasesCache phrasesCache) {
        this.synonymsCache = synonymsCache;
        this.phrasesCache = phrasesCache;
    }
    
    public TokenStream tokenStream(String fieldName, Reader reader) {
        Tokenizer tokenizer = new StandardTokenizer(reader);
        TokenFilter lowerCaseFilter = new LowerCaseFilter(tokenizer);
        TokenFilter stopFilter = new StopFilter(lowerCaseFilter,
                PorterStemStopWordAnalyzer.stopWords);
        return new SynonymPhraseStopWordFilter(stopFilter,
                this.synonymsCache, this.phrasesCache);
    }
    
    public static final void main(String [] args) throws IOException {
        SynonymsCache synonymsCache = new SynonymsCacheImpl();
        PhrasesCache phrasesCache = new PhrasesCacheImpl();
        Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                synonymsCache,phrasesCache);
        String text = "Collective Intelligence and Web2.0";
        Reader reader = new StringReader(text);
        TokenStream ts = analyzer.tokenStream(null, reader);
        Token token = ts.next();
        while (token != null) {
            System.out.println(token.termText());
            token = ts.next();
        }
        
    }
}
