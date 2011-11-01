
package com.alag.ci.textanalysis.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author al
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({com.alag.ci.textanalysis.test.LuceneIntegrationTest.class, 
    com.alag.ci.textanalysis.test.MetaDataExtractorTest.class,
    com.alag.ci.textanalysis.lucene.test.SynonymsPhrasesAnalyzerTest.class,
    com.alag.ci.textanalysis.lucene.impl.SynonymPhraseStopWordAnalyzerTest.class,
    com.alag.ci.tagcloud.test.TagCloudTest.class,
    com.alag.ci.textanalysis.termvector.test.TagMagnitudeVectorTest.class})
public class FullTests {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
