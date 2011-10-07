package com.alag.ci.textanalysis.lucene.impl;

import java.io.*;

import org.apache.lucene.analysis.*;

public class CacheImpl {
    private Analyzer stemmer = null;
    
    public CacheImpl() {
        this.stemmer = new PorterStemStopWordAnalyzer();
    }
    
    protected String getStemmedText(String text) throws IOException {
        StringBuilder sb = new StringBuilder();
        Reader reader = new StringReader(text);
        TokenStream tokenStream = this.stemmer.tokenStream(null, reader);
        Token token = tokenStream.next();
        while (token != null) {
            sb.append(token.termText());
            token = tokenStream.next();
            if (token != null) {
                sb.append(" ");
            }
        }
        return sb.toString();    
    }
}
