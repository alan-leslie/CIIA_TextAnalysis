package com.alag.ci.textanalysis.impl;

import java.util.HashMap;
import java.util.Map;

public class SimpleStopWordMetaDataExtractor extends SimpleMetaDataExtractor {
    private static final String[] stopWords =
        {"and","of","the","to","is","their","can","all", ""};
    private Map<String,String> stopWordsMap = null;

    public SimpleStopWordMetaDataExtractor() {
        this.stopWordsMap = new HashMap<String,String>();
        for (String s: stopWords) {
            this.stopWordsMap.put(s, s);
        }    
    }
    
    protected boolean acceptToken(String token) {
        return !this.stopWordsMap.containsKey(token);
    }
}
