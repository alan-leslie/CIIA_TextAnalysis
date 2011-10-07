package com.alag.ci.textanalysis.lucene.impl;

import java.io.IOException;
import java.util.*;

import com.alag.ci.textanalysis.SynonymsCache;

public class SynonymsCacheImpl extends CacheImpl implements SynonymsCache {
    private Map<String,List<String>> synonyms = null;
    
    public SynonymsCacheImpl() throws IOException {
        this.synonyms = new HashMap<String,List<String>>();
        List<String> ciList = new ArrayList<String>();
        ciList.add("collective intelligence in action");
        this.synonyms.put("ciia", ciList);
        List<String> natoList = new ArrayList<String>();
        natoList.add("NATO");
        this.synonyms.put("north atlantic treaty organization", natoList);
        this.synonyms.put("north atlantic treaty organisation", natoList);
        List<String> jsList = new ArrayList<String>();
        jsList.add("javascript");
        this.synonyms.put("js", jsList);
    }
    
    public List<String> getSynonym(String text) throws IOException{
        return this.synonyms.get(text);  
    }
}
