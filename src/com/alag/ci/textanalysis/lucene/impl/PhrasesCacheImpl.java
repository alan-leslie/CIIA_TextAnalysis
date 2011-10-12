package com.alag.ci.textanalysis.lucene.impl;

import java.io.IOException;
import java.util.*;

import com.alag.ci.textanalysis.PhrasesCache;

// TODO - use CSV file to get phrases

public class PhrasesCacheImpl extends CacheImpl implements PhrasesCache {
    private Map<String,String> validPhrases = null;
    
    public PhrasesCacheImpl() throws IOException {
        validPhrases = new HashMap<String,String>();
        validPhrases.put("collective intelligence in action", null);
        validPhrases.put("north atlantic treaty organisation", null);
        validPhrases.put("north atlantic treaty organization", null);
        validPhrases.put("google maps", null);
        validPhrases.put("google map", null);
//        validPhrases.put(getStemmedText("collective intelligence"), null);
    }
    
    public boolean isValidPhrase(String text) throws IOException {
//        return this.validPhrases.containsKey(getStemmedText(text));
        return this.validPhrases.containsKey(text);
    }
    
    public boolean isStartOfPhrase(String text) throws IOException {
        Set<String> theKeys = validPhrases.keySet();
        Iterator<String> iterator = theKeys.iterator();
        boolean found = false;
        
        while(iterator.hasNext() && !found){
            String phrase = iterator.next(); 
            int foundPos = phrase.indexOf(text);
            if(foundPos == 0){
                found = true;
            }
        }
        
        return found;
    }
}
