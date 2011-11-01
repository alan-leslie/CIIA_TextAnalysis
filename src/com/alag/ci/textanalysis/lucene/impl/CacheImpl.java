package com.alag.ci.textanalysis.lucene.impl;

import java.io.*;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

public class CacheImpl {

    private Analyzer stemmer = null;

    public CacheImpl() {
        this.stemmer = new PorterStemStopWordAnalyzer();
    }

    protected String getStemmedText(String text) throws IOException {
        StringBuilder sb = new StringBuilder();
        Reader reader = new StringReader(text);
        TokenStream tokenStream = this.stemmer.tokenStream(null, reader);
//        Token token = tokenStream.next();
//        while (token != null) {
//            sb.append(token.termText());
//            token = tokenStream.next();
//            if (token != null) {
//                sb.append(" ");
//            }
//        }
        CharTermAttribute termAttr = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        int i = 0;
        while (tokenStream.incrementToken()) {
            String theTerm = termAttr.toString();

            if (i != 0) {
                sb.append(" ");
            }

            sb.append(theTerm);
            ++i;
        }

        return sb.toString();
    }
}
