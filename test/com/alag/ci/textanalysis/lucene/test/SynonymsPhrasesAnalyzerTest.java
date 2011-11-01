package com.alag.ci.textanalysis.lucene.test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import com.alag.ci.textanalysis.PhrasesCache;
import com.alag.ci.textanalysis.SynonymsCache;
import com.alag.ci.textanalysis.lucene.impl.PhrasesCacheImpl;
import com.alag.ci.textanalysis.lucene.impl.PorterStemStopWordAnalyzer;
import com.alag.ci.textanalysis.lucene.impl.SynonymPhraseStopWordAnalyzer;
import com.alag.ci.textanalysis.lucene.impl.SynonymsCacheImpl;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class SynonymsPhrasesAnalyzerTest extends TestCase {

    public void testSynonymsPhrases() throws IOException {
        SynonymsCache synonymsCache = new SynonymsCacheImpl();
        PhrasesCache phrasesCache = new PhrasesCacheImpl();
        Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                synonymsCache, phrasesCache);
        String text = "Collective Intelligence and Web2.0";
        Reader reader = new StringReader(text);
        TokenStream ts = analyzer.tokenStream(null, reader);
        CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
        ts.reset();
        int noOfTokens = 0;
        System.out.println("Start of op");
        while (ts.incrementToken()) {
            String theTerm = termAttr.toString();
            System.out.println(theTerm);
            ++noOfTokens;
        }
        System.out.println("End of op");
    }

    public void testPorterStemmingAnalyzer() throws IOException {
        Analyzer analyzer = new PorterStemStopWordAnalyzer();
        String text = "Collective Intelligence and Web2.0";
        Reader reader = new StringReader(text);
        TokenStream ts = analyzer.tokenStream(null, reader);
        CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
        ts.reset();
        int noOfTokens = 0;
        System.out.println("Start of op");
        while (ts.incrementToken()) {
            String theTerm = termAttr.toString();
            System.out.println(theTerm);
            ++noOfTokens;
        }
        System.out.println("End of op");
    }
}
