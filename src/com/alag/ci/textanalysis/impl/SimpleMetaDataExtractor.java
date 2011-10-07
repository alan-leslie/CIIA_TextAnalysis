package com.alag.ci.textanalysis.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.alag.ci.MetaDataVector;
import com.alag.ci.TagMagnitude;
import com.alag.ci.impl.MetaDataVectorImpl;
import com.alag.ci.impl.TagMagnitudeImpl;
import com.alag.ci.tagcloud.FontSizeComputationStrategy;
import com.alag.ci.tagcloud.TagCloud;
import com.alag.ci.tagcloud.VisualizeTagCloudDecorator;
import com.alag.ci.tagcloud.impl.HTMLTagCloudDecorator;
import com.alag.ci.tagcloud.impl.LinearFontSizeComputationStrategy;
import com.alag.ci.tagcloud.impl.TagCloudImpl;
import com.alag.ci.textanalysis.MetaDataExtractor;

public class SimpleMetaDataExtractor implements MetaDataExtractor { 
    private Map<String, Long> idMap = null;
    private Long currentId = null;
    
    public SimpleMetaDataExtractor() {
        this.idMap = new HashMap<String,Long>();
        this.currentId = new Long(0);
    }
    
    public MetaDataVector extractMetaData(String title, String body) {
        MetaDataVector titleMDV = getMetaDataVector(title);
        MetaDataVector bodyMDV = getMetaDataVector(body);
        
        try {
            FontSizeComputationStrategy strategy = 
                new LinearFontSizeComputationStrategy(3,"font-size: ");
            TagCloud tagCloud = new TagCloudImpl(titleMDV,strategy);
            writeToFile("titleTagCloud.html",tagCloud);
            tagCloud = new TagCloudImpl(bodyMDV,strategy);
            writeToFile("bodyTagCloud.html",tagCloud);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titleMDV.add(bodyMDV);
    }
    
    protected Long getTokenId(String token) {
        Long id = this.idMap.get(token);
        if (id == null) {
            id = this.currentId ++;
            this.idMap.put(token, id);
        }
        return id;
    }
    
    private static void writeToFile(String fileName, TagCloud cloud) 
    throws IOException {
        BufferedWriter out = new BufferedWriter(
                new FileWriter(fileName));
        VisualizeTagCloudDecorator decorator = new HTMLTagCloudDecorator();
        out.write(decorator.decorateTagCloud(cloud));
        out.close();
    }
    
    protected MetaDataVector getMetaDataVector(String text) {
        Map<String,Integer> keywordMap = new HashMap<String,Integer>();
        StringTokenizer st = new StringTokenizer(text);
        while (st.hasMoreTokens()) {
            String token = normalizeToken(st.nextToken());
            //Check to see if it is not a stop word
            if (acceptToken(token)) {
                Integer count = keywordMap.get(token);
                if (count == null) {
                    count = new Integer(0);              
                }
                count ++;
                keywordMap.put(token, count);
            }
        }
 
        MetaDataVector mdv =  createMetaDataVector(keywordMap);
        return mdv;
    }
    
    
    protected MetaDataVector createMetaDataVector(Map<String,Integer> keywordMap) {
        List<TagMagnitude> tmList = new ArrayList<TagMagnitude>();
        for (String tagName: keywordMap.keySet()) {
            TagMagnitude tm = new TagMagnitudeImpl(
                    keywordMap.get(tagName),getTokenId(tagName),tagName);
            tmList.add(tm);
        }
        MetaDataVector mdv =  new MetaDataVectorImpl(tmList);
        return mdv;
    }
    
    protected boolean acceptToken(String token) {
        return true;
    }
    
    protected String normalizeToken(String token) {
        String normalizedToken = token.toLowerCase().trim();
        if ( (normalizedToken.endsWith(".")) || (normalizedToken.endsWith(",")) ) {
            int size = normalizedToken.length();
            normalizedToken = normalizedToken.substring(0, size -1);
        }
        return normalizedToken;
    }
}
