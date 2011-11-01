package com.alag.ci.textanalysis.lucene.impl;

import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.util.Version;


public final class PorterStemStopWordAnalyzer extends Analyzer {
    public static final String [] stopWords = 
        {"and","of","the","to","is","their","can","all","i","in"};
    
    public TokenStream tokenStream(String fieldName, Reader reader) {
        WhitespaceTokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_34, reader);
        TokenFilter lowerCaseFilter = new LowerCaseFilter(tokenizer);

        Set<String> theStopWords = new HashSet<String>(Arrays.asList(EnglishStopWords.SMART_STOP_WORDS));

        TokenFilter stopFilter = new StopFilter(true, lowerCaseFilter, theStopWords, true);

        TokenFilter stemFilter =  new PorterStemFilter(stopFilter);
        return stemFilter;
    } 
}
