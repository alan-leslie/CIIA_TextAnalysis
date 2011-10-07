/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alag.ci.textanalysis.lucene.impl;

import com.alag.ci.textanalysis.SynonymsCache;
import com.alag.ci.textanalysis.PhrasesCache;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import java.io.StringReader;
import org.apache.lucene.analysis.Token;
import java.io.Reader;
import org.apache.lucene.analysis.TokenStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author al
 */
public class SynonymPhraseStopWordAnalyzerTest {

    public SynonymPhraseStopWordAnalyzerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class SynonymPhraseStopWordAnalyzer.
     */
    @Test
    public void testValidPhrase() {
        try {
            SynonymsCache synonymsCache = new SynonymsCacheImpl();
            PhrasesCache phrasesCache = new PhrasesCacheImpl();
            Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                    synonymsCache, phrasesCache);
            String text = "Collective Intelligence in action";
            Reader reader = new StringReader(text);
            TokenStream ts = analyzer.tokenStream(null, reader);
            Token token = ts.next();
            Token firstToken = token;
            int noOfTokens = 1;
            while (token != null) {
                System.out.println(token.termText());
                ++noOfTokens;
                token = ts.next();
            }

            assert (firstToken.termText().equalsIgnoreCase("collective intelligence in action"));
            assert (noOfTokens == 2);  // including the end null
        } catch (IOException ex) {
            Logger.getLogger(SynonymPhraseStopWordAnalyzerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testNotAPhrase() {
        try {
            SynonymsCache synonymsCache = new SynonymsCacheImpl();
            PhrasesCache phrasesCache = new PhrasesCacheImpl();
            Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                    synonymsCache, phrasesCache);
            String text = "Collective Intelligence is action";
            Reader reader = new StringReader(text);
            TokenStream ts = analyzer.tokenStream(null, reader);
            Token token = ts.next();
            Token firstToken = token;
            int noOfTokens = 1;
            while (token != null) {
                System.out.println(token.termText());
                ++noOfTokens;
                token = ts.next();
            }

            assert (firstToken.termText().equalsIgnoreCase("collective"));
            assert (noOfTokens == 4);  // including the end null
        } catch (IOException ex) {
            Logger.getLogger(SynonymPhraseStopWordAnalyzerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testSingleWordSynonym() {
        try {
            SynonymsCache synonymsCache = new SynonymsCacheImpl();
            PhrasesCache phrasesCache = new PhrasesCacheImpl();
            Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                    synonymsCache, phrasesCache);
            String text = "js is javascript so it is";
            Reader reader = new StringReader(text);
            TokenStream ts = analyzer.tokenStream(null, reader);
            Token token = ts.next();
            Token firstToken = token;
            int noOfTokens = 1;
            while (token != null) {
                System.out.println(token.termText());
                ++noOfTokens;
                token = ts.next();
            }

            assert (firstToken.termText().equalsIgnoreCase("javascript"));
            assert (noOfTokens == 3);  // including the end null
        } catch (IOException ex) {
            Logger.getLogger(SynonymPhraseStopWordAnalyzerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testMultipleWordSynonym() {
        try {
            SynonymsCache synonymsCache = new SynonymsCacheImpl();
            PhrasesCache phrasesCache = new PhrasesCacheImpl();
            Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                    synonymsCache, phrasesCache);
            String text = "north atlantic treaty organisation is nato";
            Reader reader = new StringReader(text);
            TokenStream ts = analyzer.tokenStream(null, reader);
            Token token = ts.next();
            Token firstToken = token;
            int noOfTokens = 1;
            while (token != null) {
                System.out.println(token.termText());
                ++noOfTokens;
                token = ts.next();
            }

            assert (firstToken.termText().equalsIgnoreCase("nato"));
            assert (noOfTokens == 3);  // including the end null
        } catch (IOException ex) {
            Logger.getLogger(SynonymPhraseStopWordAnalyzerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testEmbeddedPhrase() {
        try {
            SynonymsCache synonymsCache = new SynonymsCacheImpl();
            PhrasesCache phrasesCache = new PhrasesCacheImpl();
            Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                    synonymsCache, phrasesCache);
            String text = "north of the google maps is collective google map test";
            Reader reader = new StringReader(text);
            TokenStream ts = analyzer.tokenStream(null, reader);
            Token token = ts.next();
            Token firstToken = token;
            Token secondToken = token;
            Token thirdToken = token;
            Token fourthToken = token;
            Token fifthToken = token;
            int noOfTokens = 1;
            while (token != null) {
                System.out.println(token.termText());
                ++noOfTokens;
                token = ts.next();
                if (noOfTokens == 2) {
                    secondToken = token;
                }
                if (noOfTokens == 3) {
                    thirdToken = token;
                }
                if (noOfTokens == 4) {
                    fourthToken = token;
                }
                if (noOfTokens == 5) {
                    fifthToken = token;
                }

            }

            assert (noOfTokens == 6);  // including the end null
            assert (firstToken.termText().equalsIgnoreCase("north"));
            assert (secondToken.termText().equalsIgnoreCase("google maps"));
            assert (thirdToken.termText().equalsIgnoreCase("collective"));
            assert (fourthToken.termText().equalsIgnoreCase("google map"));
            assert (fifthToken.termText().equalsIgnoreCase("test"));
        } catch (IOException ex) {
            Logger.getLogger(SynonymPhraseStopWordAnalyzerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}