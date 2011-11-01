package com.alag.ci.textanalysis.lucene.impl;

import java.io.IOException;
import java.util.*;

import org.apache.lucene.analysis.*;

import com.alag.ci.textanalysis.*;
import java.io.Reader;
import java.io.StringReader;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

public final class SynonymPhraseStopWordFilter extends TokenFilter {

    private Stack<String> injectedTokensStack = null;
    private Queue<String> phraseStartQueue = null;
    private String previousToken = null;
    private SynonymsCache synonymsCache = null;
    private PhrasesCache phrasesCache = null;
    private Analyzer stemmer = null;
    private CharTermAttribute termAtt = null;
    private OffsetAttribute offsetAtt = null;
    private boolean hasNext = true;

    public SynonymPhraseStopWordFilter(TokenStream input,
            SynonymsCache synonymsCache, PhrasesCache phrasesCache) {
        super(input);
        this.synonymsCache = synonymsCache;
        this.phrasesCache = phrasesCache;
        this.injectedTokensStack = new Stack<String>();
        this.phraseStartQueue = new LinkedList<String>();
        this.stemmer = new PorterStemStopWordAnalyzer();
        termAtt = input.addAttribute(CharTermAttribute.class);
        offsetAtt = input.addAttribute(OffsetAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {
        boolean tokenAdded = false;

        while (termsRemain() && !tokenAdded) {
            String token = nextPhrase();

            if (token == null || token.isEmpty()) {
                hasNext = false;
            } else {
                List<String> synonyms = synonymsCache.getSynonym(token);

                if (synonyms != null
                        && !synonyms.isEmpty()) {
                    token = synonyms.get(0);
                    clearAttributes();
                    termAtt.append(token);
                    offsetAtt.setOffset(0, 0);
                    tokenAdded = true;
                } else {
                    String stemmedText = getStemmedText(token);
                    if (!stemmedText.isEmpty()) {
                        clearAttributes();
                        termAtt.append(token);
                        offsetAtt.setOffset(0, 0);
                        tokenAdded = true;
                    }
                }
            }
        }

        return termsRemain();
    }

    private boolean termsRemain() {
        return hasNext || !phraseStartQueue.isEmpty();
    }

    private String nextPhrase() throws IOException {
        if (phraseStartQueue.isEmpty()) {
            hasNext = input.incrementToken();

            if (hasNext) {
                String theTerm = termAtt.toString();
                phraseStartQueue.add(theTerm);
            }
        } else {
            String testPhrase = getTestPhrase("");

            if (phrasesCache.isValidPhrase(testPhrase)) {
                phraseStartQueue.clear();
                return testPhrase;
            }
        }

        boolean partialPhrase = true;
        while (phrasesCache.isStartOfPhrase(getTestPhrase(""))
                && hasNext) {
            hasNext = input.incrementToken();

            if (hasNext) {
                String theTerm = termAtt.toString();
                phraseStartQueue.add(theTerm);
            }

            String testPhrase = getTestPhrase("");

            if (phrasesCache.isValidPhrase(testPhrase)) {
                phraseStartQueue.clear();
                return testPhrase;
            }
        }

        // not a valid phrase return first in queue
        String testPhrase = getTestPhrase("");
        if (phrasesCache.isStartOfPhrase(testPhrase)) {
            assert (false);
        } else {
            if (phraseStartQueue.isEmpty()) {
                if (hasNext) {
                    assert (false);
                }
                return "";
            } else {
                return phraseStartQueue.poll();
            }
        }

        assert (false);
        return "";
    }

    private String getStemmedText(String text) throws IOException {
        StringBuilder sb = new StringBuilder();
        Reader reader = new StringReader(text);
        TokenStream tokenStream = this.stemmer.tokenStream(null, reader);
        CharTermAttribute termAttr = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        int i = 0;
        while (tokenStream.incrementToken()) {
            String theTerm = termAttr.toString();

            if (i != 0) {
                sb.append(" ");
            }

            sb.append(theTerm);
            ++i;
        }

        return sb.toString();
    }

    private String getTestPhrase(String token) {
        StringBuilder testPhrase = new StringBuilder();

        if (!phraseStartQueue.isEmpty()) {
            Iterator<String> iterator = phraseStartQueue.iterator();
            while (iterator.hasNext()) {
                testPhrase.append(iterator.next());
                testPhrase.append(" ");
            }
        }

        testPhrase.append(token);

        return testPhrase.toString().trim();
    }
}
