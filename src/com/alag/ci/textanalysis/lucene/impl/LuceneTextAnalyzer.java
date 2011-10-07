package com.alag.ci.textanalysis.lucene.impl;

import java.io.*;
import java.util.*;

import org.apache.lucene.analysis.*;

import com.alag.ci.tagcloud.TagCloud;
import com.alag.ci.tagcloud.TagCloudElement;
import com.alag.ci.tagcloud.impl.HTMLTagCloudDecorator;
import com.alag.ci.tagcloud.impl.LinearFontSizeComputationStrategy;
import com.alag.ci.tagcloud.impl.TagCloudElementImpl;
import com.alag.ci.tagcloud.impl.TagCloudImpl;
import com.alag.ci.textanalysis.*;
import com.alag.ci.textanalysis.termvector.impl.*;

public class LuceneTextAnalyzer implements TextAnalyzer {
    private TagCache tagCache = null;
    private InverseDocFreqEstimator inverseDocFreqEstimator = null;
    
    public LuceneTextAnalyzer(TagCache tagCache, 
            InverseDocFreqEstimator inverseDocFreqEstimator) {
        this.tagCache = tagCache;
        this.inverseDocFreqEstimator = inverseDocFreqEstimator;
    }
    
    public List<Tag> analyzeText(String text) throws IOException {
        Reader reader = new StringReader(text);
        Analyzer analyzer = getAnalyzer();
        List<Tag> tags = new ArrayList<Tag>();
        TokenStream tokenStream = analyzer.tokenStream(null, reader) ;
        Token token = tokenStream.next();
        while ( token != null) {
            Tag tag = getTag(token.termText());
            tags.add(tag);
            token = tokenStream.next();
        }
        return tags;
    }
    
    public TagMagnitudeVector createTagMagnitudeVector(String text) throws IOException {
        List<Tag> tagList =  analyzeText(text);
        Map<Tag,Integer> tagFreqMap = computeTermFrequency(tagList);
        return applyIDF(tagFreqMap);
    }
    
  
    private Map<Tag,Integer> computeTermFrequency(List<Tag> tagList) {
        Map<Tag,Integer> tagFreqMap = new HashMap<Tag,Integer>();
        for (Tag tag: tagList) {
            Integer count = tagFreqMap.get(tag);
            if (count == null) {
                count = new Integer(1);
            } else {
                count = new Integer(count.intValue() + 1);
            }
            tagFreqMap.put(tag, count);
        }
        return tagFreqMap;
    }
    
    private TagMagnitudeVector applyIDF(Map<Tag,Integer> tagFreqMap) {
        List<TagMagnitude> tagMagnitudes = new ArrayList<TagMagnitude>();
        for (Tag tag: tagFreqMap.keySet()) {
            double idf = this.inverseDocFreqEstimator.estimateInverseDocFreq(tag);
            double tf = tagFreqMap.get(tag);
            double wt = tf*idf;
            tagMagnitudes.add(new TagMagnitudeImpl(tag,wt));
        }
        return new TagMagnitudeVectorImpl(tagMagnitudes);
    }
    
    
    private Tag getTag(String text) throws IOException {
        return this.tagCache.getTag(text);
    }
    
    protected Analyzer getAnalyzer() throws IOException {
        return new SynonymPhraseStopWordAnalyzer(new SynonymsCacheImpl(),
                new PhrasesCacheImpl());
    }
    
    private TagCloud createTagCloud(TagMagnitudeVector tmVector) {
        List<TagCloudElement> elements = new ArrayList<TagCloudElement>();
        for (TagMagnitude tm: tmVector.getTagMagnitudes()) {
            TagCloudElement element = new TagCloudElementImpl(tm.getDisplayText(), tm.getMagnitude());
            elements.add(element);
        }
        return new TagCloudImpl(elements, new LinearFontSizeComputationStrategy(3,"font-size: "));
    }
    
    private String visualizeTagCloud(TagCloud tagCloud) {
        HTMLTagCloudDecorator decorator = new HTMLTagCloudDecorator();
        String html = decorator.decorateTagCloud(tagCloud);
        System.out.println(html);
        return html;
    }

    public static void main(String [] args) throws IOException {
        String title = "Collective Intelligence and Web2.0";
        String body = "Web2.0 is all about connecting users to users, "  +
                " inviting users to participate and applying their collective" +
                " intelligence to improve the application. Collective intelligence" +
                " enhances the user experience" ;
                
        TagCacheImpl t = new TagCacheImpl();
        InverseDocFreqEstimator idfEstimator = new EqualInverseDocFreqEstimator();
        LuceneTextAnalyzer lta = new LuceneTextAnalyzer(t, idfEstimator);
//        System.out.print("Analyzing the title .... \n");
//        lta.displayTextAnalysis(title);
//        System.out.print("\nAnalyzing the body .... \n");
//        lta.displayTextAnalysis(body);
        TagMagnitudeVector tmTitle = lta.createTagMagnitudeVector(title);
        TagMagnitudeVector tmBody = lta.createTagMagnitudeVector(body);
        TagMagnitudeVector tmCombined = tmTitle.add(tmBody);
        System.out.println(tmCombined);    
        TagCloud tagCloud = lta.createTagCloud(tmBody);
        String html = lta.visualizeTagCloud(tagCloud);
        
    }
    
    private void displayTextAnalysis(String text) throws IOException {
        List<Tag> tags = analyzeText(text);
        for (Tag tag: tags) {
            System.out.print(tag + " ");
        }      
    }
}
