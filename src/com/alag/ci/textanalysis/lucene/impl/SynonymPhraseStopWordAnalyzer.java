package com.alag.ci.textanalysis.lucene.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import com.alag.ci.textanalysis.*;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public final class SynonymPhraseStopWordAnalyzer extends Analyzer{
    private  SynonymsCache synonymsCache = null;
    private PhrasesCache phrasesCache = null;
    
    public SynonymPhraseStopWordAnalyzer(SynonymsCache synonymsCache,
            PhrasesCache phrasesCache) {
        this.synonymsCache = synonymsCache;
        this.phrasesCache = phrasesCache;
    }

//    @Override
//    public final TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
//        return super.reusableTokenStream(fieldName, reader);
//    }
    
    public TokenStream tokenStream(String fieldName, Reader reader) {
                WhitespaceTokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_34, reader);

//        Tokenizer tokenizer = new StandardTokenizer(reader);
        TokenFilter lowerCaseFilter = new LowerCaseFilter(tokenizer);
//        TokenFilter stopFilter = new StopFilter(lowerCaseFilter,
//                PorterStemStopWordAnalyzer.stopWords);
        
        // todo there must be a stemmer about here seomwhere???
        // also might want to move phrase filter above stop fiter
        // so I can add phrases like "collective intelligence in action"
        // so that the in does not get stripped out
        TokenFilter phraseFilter = new SynonymPhraseStopWordFilter(lowerCaseFilter,
                this.synonymsCache, this.phrasesCache);
//        TokenFilter stopFilter = new StopFilter(lowerCaseFilter,
//                EnglishStopWords.SMART_STOP_WORDS);
        return phraseFilter;
    }
    
    public static final void main(String [] args) throws IOException {
        SynonymsCache synonymsCache = new SynonymsCacheImpl();
        PhrasesCache phrasesCache = new PhrasesCacheImpl();
        Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                synonymsCache,phrasesCache);
        String text = "Collective Intelligence agency and Web2.0";
        Reader reader = new StringReader(text);
        TokenStream tokenStream = analyzer.tokenStream(null, reader);
        CharTermAttribute termAttr = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            String theTerm = termAttr.toString();
            System.out.println(theTerm);
        }
//        Token token = ts.next();
//        while (token != null) {
//            System.out.println(token.termText());
//            token = ts.next();
//        }
        
    }
}
