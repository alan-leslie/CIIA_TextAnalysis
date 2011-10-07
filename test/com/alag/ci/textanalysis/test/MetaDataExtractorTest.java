package com.alag.ci.textanalysis.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.TestCase;

import com.alag.ci.MetaDataVector;
import com.alag.ci.tagcloud.FontSizeComputationStrategy;
import com.alag.ci.tagcloud.TagCloud;
import com.alag.ci.tagcloud.VisualizeTagCloudDecorator;
import com.alag.ci.tagcloud.impl.HTMLTagCloudDecorator;
import com.alag.ci.tagcloud.impl.LinearFontSizeComputationStrategy;
import com.alag.ci.tagcloud.impl.TagCloudImpl;
import com.alag.ci.textanalysis.MetaDataExtractor;
import com.alag.ci.textanalysis.impl.SimpleBiTermStopWordStemmerMetaDataExtractor;
import com.alag.ci.textanalysis.impl.SimpleMetaDataExtractor;
import com.alag.ci.textanalysis.impl.SimpleStopWordMetaDataExtractor;
import com.alag.ci.textanalysis.impl.SimpleStopWordStemmerMetaDataExtractor;


public class MetaDataExtractorTest extends TestCase {
    private static final String title = "Collective Intelligence and Web2.0";
    private static final String body = "Web2.0 is all about connecting users to users, " +
    "inviting users to participate and applying their collective " +
    "intelligence to improve the application. Collective intelligence " +
    "enhances the user experience";
    private static final int numSizes = 3;
    private static final String fontPrefix = "font-size: ";

    public void testSimpleMetaDataExtractor() throws Exception {
        SimpleMetaDataExtractor ex = new SimpleMetaDataExtractor();
        String fileName = "simpleExtractorChap3.html";
        generateTagCloud(ex,fileName);
    }
    
    public void testSimpleStopMetaDataExtractor() throws Exception {
        SimpleMetaDataExtractor ex = new SimpleStopWordMetaDataExtractor();
        String fileName = "simpleStopExtractorChap3.html";
        generateTagCloud(ex,fileName);
    }
    
    public void testSimpleStopStemmerMetaDataExtractor() throws Exception {
        SimpleMetaDataExtractor ex = new SimpleStopWordStemmerMetaDataExtractor();
        String fileName = "simpleStopCannonicalExtractorChap3.html";
        generateTagCloud(ex,fileName);
    }
    
    public void testSimpleBiTermStopStemmerMetaDataExtractor() throws Exception {
        SimpleMetaDataExtractor ex = new SimpleBiTermStopWordStemmerMetaDataExtractor();
        String fileName = "simpleBiTermStopCannonicalExtractorChap3.html";
        generateTagCloud(ex,fileName);
    }
    
    
    private static void generateTagCloud(MetaDataExtractor ex, String fileName)
        throws IOException {
        MetaDataVector mdv = ex.extractMetaData(title, body);
        
        FontSizeComputationStrategy strategy = 
            new LinearFontSizeComputationStrategy(numSizes,fontPrefix);
        TagCloud tagCloud = new TagCloudImpl(mdv,strategy);
        writeToFile(fileName,tagCloud);
    }
    
    private static void writeToFile(String fileName, TagCloud cloud) 
        throws IOException {
        BufferedWriter out = new BufferedWriter(
                new FileWriter(fileName));
        VisualizeTagCloudDecorator decorator = new HTMLTagCloudDecorator();
        out.write(decorator.decorateTagCloud(cloud));
        out.close();
    }
    
    public static void main(String [] args) throws Exception {
        MetaDataExtractorTest test = new MetaDataExtractorTest();
        test.testSimpleBiTermStopStemmerMetaDataExtractor();
    }
}
