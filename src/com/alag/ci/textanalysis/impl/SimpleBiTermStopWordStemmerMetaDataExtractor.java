package com.alag.ci.textanalysis.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.alag.ci.MetaDataVector;


public class SimpleBiTermStopWordStemmerMetaDataExtractor extends
        SimpleStopWordStemmerMetaDataExtractor {
    
    protected MetaDataVector getMetaDataVector(String text) {
        Map<String,Integer> keywordMap = new HashMap<String,Integer>();
        List<String> allTokens = new ArrayList<String>();
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
                allTokens.add(token);
            }
        }
        String firstToken = allTokens.get(0);
        for (String token: allTokens.subList(1, allTokens.size())) {
            String biTerm = firstToken + " " + token;
            if (isValidBiTermToken(biTerm)) {             
                Integer count = keywordMap.get(biTerm);
                if (count == null) {
                    count = new Integer(0);              
                }
                count ++;
                keywordMap.put(biTerm, count);
            }
            firstToken = token;
        }
        MetaDataVector mdv =  createMetaDataVector(keywordMap);
        return mdv;
    }
    
    private boolean isValidBiTermToken(String biTerm) {
        if ("collective intelligence".compareTo(biTerm) == 0) {
            return true;
        }
        return false;
    }
}
