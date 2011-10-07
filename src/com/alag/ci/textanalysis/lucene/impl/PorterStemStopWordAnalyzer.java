package com.alag.ci.textanalysis.lucene.impl;

import java.io.Reader;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class PorterStemStopWordAnalyzer extends Analyzer {
    public static final String [] stopWords = 
        {"and","of","the","to","is","their","can","all","i","in"};
    
    public TokenStream tokenStream(String fieldName, Reader reader) {
        Tokenizer tokenizer = new StandardTokenizer(reader);
        TokenFilter lowerCaseFilter = new LowerCaseFilter(tokenizer);
        TokenFilter stopFilter = new StopFilter(lowerCaseFilter,stopWords);
        TokenFilter stemFilter =  new PorterStemFilter(stopFilter);
        return stemFilter;
    } 
}
