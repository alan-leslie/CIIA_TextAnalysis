package com.alag.ci.textanalysis.lucene.impl;

import com.alag.ci.CSVFile;
import java.io.IOException;
import java.util.*;

import com.alag.ci.textanalysis.PhrasesCache;

public class PhrasesCacheImpl extends CacheImpl implements PhrasesCache {
    private Set<String> validPhrases = null;
    
    public PhrasesCacheImpl() throws IOException {
        initCache();
    }
    
    private void initCache() {
        String phrasesFileName = "phrases.txt";
        validPhrases = new HashSet<String>();
        
        List<String[]> theFileData = CSVFile.getFileData(phrasesFileName);
        Iterator<String[]> theIterator = theFileData.iterator();

        while (theIterator.hasNext()) {
            String theLineArr[] = theIterator.next();

            if (theLineArr.length > 0) {
                String thePhrase = theLineArr[0].trim();
                validPhrases.add(thePhrase);
            }
        }
    }
    
    public boolean isValidPhrase(String text) throws IOException {
        return this.validPhrases.contains(text);
    }
    
    public boolean isStartOfPhrase(String text) throws IOException {
        Iterator<String> iterator = validPhrases.iterator();
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
