package com.alag.ci.textanalysis.lucene.impl;

import com.alag.ci.CSVFile;
import java.io.IOException;
import java.util.*;

import com.alag.ci.textanalysis.SynonymsCache;

public class SynonymsCacheImpl extends CacheImpl implements SynonymsCache {
    private Map<String,List<String>> synonyms = null;
    
    public SynonymsCacheImpl() throws IOException {
        initCache();
    }
    
    private void initCache() {
        String phrasesFileName = "synonyms.txt";
        this.synonyms = new HashMap<String,List<String>>();
        
        List<String[]> theFileData = CSVFile.getFileData(phrasesFileName);
        Iterator<String[]> theIterator = theFileData.iterator();

        while (theIterator.hasNext()) {
            String theLineArr[] = theIterator.next();

            if (theLineArr.length > 1) {
                String theFirstSynonym = theLineArr[0].trim();                
                String theSecondSynonym = theLineArr[1].trim();
                List<String> synonymList = null;
                
                if (synonyms.containsKey(theFirstSynonym)) {
                    synonymList = synonyms.get(theFirstSynonym);
                } else {
                    synonymList = new ArrayList<String>();
                }
                
                synonymList.add(theSecondSynonym);

                synonyms.put(theFirstSynonym, synonymList);
            }
        }
    }
    
    public List<String> getSynonym(String text) throws IOException{
        return this.synonyms.get(text);  
    }
}
