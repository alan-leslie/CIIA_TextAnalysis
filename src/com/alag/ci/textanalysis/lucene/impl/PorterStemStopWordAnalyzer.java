package com.alag.ci.textanalysis.lucene.impl;

import java.io.Reader;

import java.io.StringReader;
import java.lang.String;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizerImpl;
import org.apache.lucene.util.Version;
//package org.apache.lucene.analysis;

public final class PorterStemStopWordAnalyzer extends Analyzer {
    public static final String [] stopWords = 
        {"and","of","the","to","is","their","can","all","i","in"};
    
    public TokenStream tokenStream(String fieldName, Reader reader) {
        WhitespaceTokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_34, reader);
        TokenFilter lowerCaseFilter = new LowerCaseFilter(tokenizer);

        Set<String> theStopWords = new HashSet<String>(Arrays.asList(EnglishStopWords.SMART_STOP_WORDS));

        TokenFilter stopFilter = new StopFilter(true, lowerCaseFilter, theStopWords, true);
//          public StopFilter(boolean enablePositionIncrements, TokenStream input, Set<?> stopWords, boolean ignoreCase)

//        TokenFilter stopFilter = new StopFilter(lowerCaseFilter, stopWords);
        // put something in here that stems collective as collective
        TokenFilter stemFilter =  new PorterStemFilter(stopFilter);
        return stemFilter;
    } 
}
