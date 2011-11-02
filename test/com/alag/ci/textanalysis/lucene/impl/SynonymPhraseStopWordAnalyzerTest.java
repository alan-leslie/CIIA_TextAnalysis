package com.alag.ci.textanalysis.lucene.impl;

import com.alag.ci.TextFile;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import com.alag.ci.textanalysis.SynonymsCache;
import com.alag.ci.textanalysis.PhrasesCache;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import java.io.StringReader;
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
    public final void testValidPhrase() {
        try {
            SynonymsCache synonymsCache = new SynonymsCacheImpl();
            PhrasesCache phrasesCache = new PhrasesCacheImpl();
            Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                    synonymsCache, phrasesCache);
            String text = "Collective Intelligence in action";
            Reader reader = new StringReader(text);
            TokenStream ts = analyzer.tokenStream(null, reader);
            CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
            ts.reset();
            int noOfTokens = 0;
            String firstToken = "";
            while (ts.incrementToken()) {
                String theTerm = termAttr.toString();
                if (noOfTokens == 0) {
                    firstToken = theTerm;
                }
                System.out.println(theTerm);
                ++noOfTokens;
            }

            assert (firstToken.equalsIgnoreCase("collective intelligence in action"));
            assert (noOfTokens == 1);  // including the end null
        } catch (IOException ex) {
            Logger.getLogger(SynonymPhraseStopWordAnalyzerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of main method, of class SynonymPhraseStopWordAnalyzer.
     */
    @Test
    public final void testEmbeddedValidPhrase() {
        try {
            SynonymsCache synonymsCache = new SynonymsCacheImpl();
            PhrasesCache phrasesCache = new PhrasesCacheImpl();
            Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                    synonymsCache, phrasesCache);
            String text = "the Collective Intelligence agency has action";
            Reader reader = new StringReader(text);
            TokenStream ts = analyzer.tokenStream(null, reader);
            CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
            ts.reset();
            int noOfTokens = 0;
            String secondToken = "";
            while (ts.incrementToken()) {
                String theTerm = termAttr.toString();
                if (noOfTokens == 1) {
                    secondToken = theTerm;
                }
                System.out.println(theTerm);
                ++noOfTokens;
            }

            assert (secondToken.equalsIgnoreCase("intelligence agency"));
            assert (noOfTokens == 3);
        } catch (IOException ex) {
            Logger.getLogger(SynonymPhraseStopWordAnalyzerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public final void testNotAPhrase() {
        try {
            SynonymsCache synonymsCache = new SynonymsCacheImpl();
            PhrasesCache phrasesCache = new PhrasesCacheImpl();
            Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                    synonymsCache, phrasesCache);
            String text = "Collective Intelligence is action";
            Reader reader = new StringReader(text);
            TokenStream ts = analyzer.tokenStream(null, reader);
            CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
            ts.reset();
            int noOfTokens = 0;
            String firstToken = "";
            while (ts.incrementToken()) {
                String theTerm = termAttr.toString();
                if (noOfTokens == 0) {
                    firstToken = theTerm;
                }
                System.out.println(theTerm);
                ++noOfTokens;
            }

            assert (firstToken.equalsIgnoreCase("collective"));
            assert (noOfTokens == 3);
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
            CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
            ts.reset();
            int noOfTokens = 0;
            String firstToken = "";
            System.out.println("Start of op");
            while (ts.incrementToken()) {
                String theTerm = termAttr.toString();
                if (noOfTokens == 0) {
                    firstToken = theTerm;
                }
                System.out.println(theTerm);
                ++noOfTokens;
            }
            System.out.println("End of op");

            assert (firstToken.equalsIgnoreCase("javascript"));
            assert (noOfTokens == 2);
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
            CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
            ts.reset();
            int noOfTokens = 0;
            String firstToken = "";
            System.out.println("Start of op");
            while (ts.incrementToken()) {
                String theTerm = termAttr.toString();
                if (noOfTokens == 0) {
                    firstToken = theTerm;
                }
                System.out.println(theTerm);
                ++noOfTokens;
            }
            System.out.println("End of op");

            assert (firstToken.equalsIgnoreCase("nato"));
            assert (noOfTokens == 2);
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
            CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
            ts.reset();
            int noOfTokens = 0;
            String firstToken = "";
            String secondToken = "";
            String thirdToken = "";
            String fourthToken = "";
            String fifthToken = "";
            while (ts.incrementToken()) {
                String theTerm = termAttr.toString();
                if (noOfTokens == 0) {
                    firstToken = theTerm;
                }
                if (noOfTokens == 1) {
                    secondToken = theTerm;
                }
                if (noOfTokens == 2) {
                    thirdToken = theTerm;
                }
                if (noOfTokens == 3) {
                    fourthToken = theTerm;
                }
                if (noOfTokens == 4) {
                    fifthToken = theTerm;
                }
                System.out.println(theTerm);
                ++noOfTokens;
            }

            assert (noOfTokens == 5);
            assert (firstToken.equalsIgnoreCase("north"));
            assert (secondToken.equalsIgnoreCase("google maps"));
            assert (thirdToken.equalsIgnoreCase("collective"));
            assert (fourthToken.equalsIgnoreCase("google map"));
            assert (fifthToken.equalsIgnoreCase("test"));
        } catch (IOException ex) {
            Logger.getLogger(SynonymPhraseStopWordAnalyzerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public final void testPunctuation() {
        try {
            SynonymsCache synonymsCache = new SynonymsCacheImpl();
            PhrasesCache phrasesCache = new PhrasesCacheImpl();
            Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                    synonymsCache, phrasesCache);
            String text = "Collective @#;, , action";
            Reader reader = new StringReader(text);
            TokenStream ts = analyzer.tokenStream(null, reader);
            CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
            ts.reset();
            int noOfTokens = 0;
            String secondToken = "";
            while (ts.incrementToken()) {
                String theTerm = termAttr.toString();
                if (noOfTokens == 1) {
                    secondToken = theTerm;
                }
                System.out.println(theTerm);
                ++noOfTokens;
            }

            assert (secondToken.equalsIgnoreCase("action"));
            assert (noOfTokens == 2);
        } catch (IOException ex) {
            Logger.getLogger(SynonymPhraseStopWordAnalyzerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public final void testTrimPunctuation() {
        try {
            SynonymsCache synonymsCache = new SynonymsCacheImpl();
            PhrasesCache phrasesCache = new PhrasesCacheImpl();
            Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                    synonymsCache, phrasesCache);
            String text = "\"html, google: action:; N.A.T.O. 5.";
            Reader reader = new StringReader(text);
            TokenStream ts = analyzer.tokenStream(null, reader);
            CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
            ts.reset();
            int noOfTokens = 0;
            String firstToken = "";
            String secondToken = "";
            String thirdToken = "";
            String fourthToken = "";
            String fifthToken = "";
            while (ts.incrementToken()) {
                String theTerm = termAttr.toString();
                if (noOfTokens == 0) {
                    firstToken = theTerm;
                }

                if (noOfTokens == 1) {
                    secondToken = theTerm;
                }
                
                if (noOfTokens == 2) {
                    thirdToken = theTerm;
                }

                if (noOfTokens == 3) {
                    fourthToken = theTerm;
                }
                
                if (noOfTokens == 4) {
                    fifthToken = theTerm;
                }
                
                System.out.println(theTerm);
                ++noOfTokens;
            }

            assert (firstToken.equalsIgnoreCase("html"));
            assert (secondToken.equalsIgnoreCase("google"));
            assert (thirdToken.equalsIgnoreCase("action:"));
            assert (fourthToken.equalsIgnoreCase("n.a.t.o."));
            assert (fifthToken.equalsIgnoreCase("5"));
            assert (noOfTokens == 5);
        } catch (IOException ex) {
            Logger.getLogger(SynonymPhraseStopWordAnalyzerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public final void testEndsWithPhraseStart() {
        try {
            SynonymsCache synonymsCache = new SynonymsCacheImpl();
            PhrasesCache phrasesCache = new PhrasesCacheImpl();
            Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                    synonymsCache, phrasesCache);
            String text = "Collective google";
            Reader reader = new StringReader(text);
            TokenStream ts = analyzer.tokenStream(null, reader);
            CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
            ts.reset();
            int noOfTokens = 0;
            String secondToken = "";
            while (ts.incrementToken()) {
                String theTerm = termAttr.toString();
                if (noOfTokens == 1) {
                    secondToken = theTerm;
                }
                System.out.println(theTerm);
                ++noOfTokens;
            }

            assert (secondToken.equalsIgnoreCase("google"));
            assert (noOfTokens == 2);
        } catch (IOException ex) {
            Logger.getLogger(SynonymPhraseStopWordAnalyzerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testKasteelFile() {
        try {
            String fullTxtFileName = "5.txt";
            String theText = TextFile.getFileData(fullTxtFileName);
            SynonymsCache synonymsCache = new SynonymsCacheImpl();
            PhrasesCache phrasesCache = new PhrasesCacheImpl();
            Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                    synonymsCache, phrasesCache);
            Reader reader = new StringReader(theText);
            TokenStream ts = analyzer.tokenStream(null, reader);
            CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
            ts.reset();
            int noOfTokens = 0;
            String secondToken = "";
            System.out.println("Start of op");
            while (ts.incrementToken()) {
                String theTerm = termAttr.toString();
                if (noOfTokens == 1) {
                    secondToken = theTerm;
                }
                System.out.println(theTerm);
                ++noOfTokens;
            }
            System.out.println("End of op");

            assert (noOfTokens == 6);
            assert (secondToken.equalsIgnoreCase("kasteel"));
        } catch (Exception exc) {
            assert (false);
        }
    }

    @Test
    public void testContainsWalkthroughFile() {
        try {
            String fullTxtFileName = "45.txt";
            String theText = TextFile.getFileData(fullTxtFileName);
            SynonymsCache synonymsCache = new SynonymsCacheImpl();
            PhrasesCache phrasesCache = new PhrasesCacheImpl();
            Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                    synonymsCache, phrasesCache);
            Reader reader = new StringReader(theText);
            TokenStream ts = analyzer.tokenStream(null, reader);
            CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
            ts.reset();
            int noOfTokens = 0;
            boolean tokenFound = false;
            System.out.println("Start of op");
            while (ts.incrementToken()) {
                String theTerm = termAttr.toString();
                if (theTerm.equalsIgnoreCase("walkthrough")) {
                    tokenFound = true;
                }
                System.out.println(theTerm);
                ++noOfTokens;
            }
            System.out.println("End of op");

            assert (tokenFound);
        } catch (Exception exc) {
            assert (false);
        }
    }
}