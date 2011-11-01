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

//    @Override
//    public Token next() throws IOException {
    @Override
    public boolean incrementToken() throws IOException {
//        boolean hasNext = true;
        boolean tokenAdded = false;
        
        while(termsRemain() && !tokenAdded){
        String token = nextPhrase();

        if (token == null || token.isEmpty()) {
            hasNext = false;
        } else {
            List<String> synonyms = synonymsCache.getSynonym(token);

            if (synonyms != null
                    && !synonyms.isEmpty()) {
//                Token synonymToken = new Token(synonyms.get(0), token.startOffset(),
//                        token.endOffset(), "synonym");
//                synonymToken.setPositionIncrement(0);

                token = synonyms.get(0);
                clearAttributes();
                termAtt.append(token);
                offsetAtt.setOffset(0, 0);
                tokenAdded = true;
            } else {
                // if it is just a stop word filter it out
                String stemmedText = getStemmedText(token);
                if (stemmedText.isEmpty()) {
//                    CharTermAttribute termAttr = input.addAttribute(CharTermAttribute.class);
//                    input.reset();
//                    clearAttributes();

//                    hasNext = input.incrementToken();
//                    token = termAttr.toString();
                } else {
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
    
    private boolean termsRemain(){
        return hasNext || !phraseStartQueue.isEmpty();
    }

    private String nextPhrase() throws IOException {
        // entry here after phrase has been returned - so queue is empty
        // on start so queue is empty
        // on single word returned - queue may still have data
//        CharTermAttribute termAttr = input.addAttribute(CharTermAttribute.class);
//        input.reset();
//        String token = "";
//        boolean hasNext = true;
        
        if (phraseStartQueue.isEmpty()) {
            hasNext = input.incrementToken();
            
            if(hasNext){
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
        while(phrasesCache.isStartOfPhrase(getTestPhrase("")) &&
                hasNext){
            hasNext = input.incrementToken();
            
            if(hasNext){
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
        if(phrasesCache.isStartOfPhrase(testPhrase)){
            assert(false);
        } else {
            if (phraseStartQueue.isEmpty()) {
                if(hasNext){
                    assert(false);
                }
                return "";
            } else {
                return phraseStartQueue.poll();
            }            
        }       

        
        // on first entry you MUST nope!!! 
        // if the queue is not empty and does not make a partial phrase need to just return the
        // first item - increment the token
        // if nothing left then return anything in the queue
        // Problem may be that the outside world sees the token stream as finished
        // need to differentiate between the input token stream and this items token stream
        // ?? has to be being done in test cache??
        
        // if the new term plus the queue is a partial phrase increment again
        
        // if it is a full phrase return the phrase
        
        // if the term plus the queue is not a partial phrase return the first queue item

////        while (partialPhrase){
//            boolean hasNext = input.incrementToken();
//
//            if (!hasNext) {
//                if (phraseStartQueue.isEmpty()) {
//                    return "";
//                } else {
//                    return phraseStartQueue.poll();
//                }
//            } else {
//                String token = termAtt.toString();
//                String testPhrase = getTestPhrase(token);
//
//                if (phrasesCache.isValidPhrase(testPhrase)) {
////                    int startOffset = token.startOffset();
////
////                    if (!phraseStartQueue.isEmpty()) {
////                        startOffset = phraseStartQueue.peek().startOffset();
////                    }
////
////                    Token phraseToken = new Token(testPhrase, token.startOffset(),
////                            token.endOffset(), "phrase");
////                    phraseToken.setPositionIncrement(0);
//
//                    phraseStartQueue.clear();
//
//                    return testPhrase;
//                }

//                if (phrasesCache.isStartOfPhrase(testPhrase)) {
//                    phraseStartQueue.add(token);
//                } else {
//                    partialPhrase = false;
//                    if (!phraseStartQueue.isEmpty()) {
//                        String qToken = phraseStartQueue.poll();
//                        // eaten up this token so need to put it somewhere
//                        phraseStartQueue.add(token);
//                        return qToken;
//                    } else {
//                        return token;
//                    }
//                }
//            }
//        }

        assert(false);
        return "";
    }

//    private String injectPhrases(String currentToken) throws IOException {
//        if (this.previousToken != null) {
//            String phrase = this.previousToken + " "
//                    + currentToken;
//            if (this.phrasesCache.isValidPhrase(phrase)) {
////                String phraseToken = new Token(phrase, currentToken.startOffset(),
////                        currentToken.endOffset(), "phrase");
////                phraseToken.setPositionIncrement(0);
//                this.injectedTokensStack.push(phrase);
//                return phrase;
//            }
//        }
//        return null;
//    }

//    private void injectSynonyms(String text, Token currentToken) throws IOException {
//        if (text != null) {
//            List<String> synonyms = this.synonymsCache.getSynonym(text);
//            if (synonyms != null) {
//                for (String synonym : synonyms) {
////                    Token synonymToken = new Token(synonym, currentToken.startOffset(),
////                            currentToken.endOffset(), "synonym");
////                    synonymToken.setPositionIncrement(0);
//                    this.injectedTokensStack.push(synonym);
//                }
//            }
//        }
//    }

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
//        Token token = tokenStream.next();
//        while (token != null) {
//            sb.append(token.termText());
//            token = tokenStream.next();
//            if (token != null) {
//                sb.append(" ");
//            }
//        }
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
