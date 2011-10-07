package com.alag.ci.textanalysis.lucene.impl;

import java.io.IOException;
import java.util.*;

import com.alag.ci.textanalysis.PhrasesCache;

public class PhrasesCacheImpl extends CacheImpl implements PhrasesCache {
    private Map<String,String> validPhrases = null;
    
    public PhrasesCacheImpl() throws IOException {
        validPhrases = new HashMap<String,String>();
        validPhrases.put(getStemmedText("collective intelligence"), null);
    }
    
    public boolean isValidPhrase(String text) throws IOException {
        return this.validPhrases.containsKey(getStemmedText(text));
    }
}
