package com.alag.ci.textanalysis.lucene.impl;

import com.alag.ci.CSVFile;
import java.io.IOException;

import com.alag.ci.textanalysis.PhrasesCache;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

    /**
     * 
     * @param text
     * @return
     * @throws IOException
     */
    @Override
    public boolean isValidPhrase(String text) throws IOException {
        return this.validPhrases.contains(text);
    }

    /**
     * 
     * @param text
     * @return
     * @throws IOException
     */
    @Override
    public boolean isStartOfPhrase(String text) throws IOException {
        boolean found = false;

        if (!text.isEmpty()) {
            Iterator<String> iterator = validPhrases.iterator();
            while (iterator.hasNext() && !found) {
                String phrase = iterator.next();
                int foundPos = phrase.indexOf(text);
                if (foundPos == 0) {
                    found = true;
                }
            }
        }

        return found;
    }
}
