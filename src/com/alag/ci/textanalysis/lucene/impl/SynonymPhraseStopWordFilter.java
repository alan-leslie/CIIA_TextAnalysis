package com.alag.ci.textanalysis.lucene.impl;

import java.io.IOException;
import java.util.*;

import org.apache.lucene.analysis.*;

import com.alag.ci.textanalysis.*;
import java.io.Reader;
import java.io.StringReader;

public class SynonymPhraseStopWordFilter extends TokenFilter {

    private Stack<Token> injectedTokensStack = null;
    private Queue<Token> phraseStartQueue = null;
    private Token previousToken = null;
    private SynonymsCache synonymsCache = null;
    private PhrasesCache phrasesCache = null;
    private Analyzer stemmer = null;
   
    public SynonymPhraseStopWordFilter(TokenStream input,
            SynonymsCache synonymsCache, PhrasesCache phrasesCache) {
        super(input);
        this.synonymsCache = synonymsCache;
        this.phrasesCache = phrasesCache;
        this.injectedTokensStack = new Stack<Token>();
        this.phraseStartQueue = new LinkedList<Token>();
        this.stemmer = new PorterStemStopWordAnalyzer();
    }

    @Override
    public Token next() throws IOException {
        Token token = nextPhrase();
        
        if(token != null){
            List<String> synonyms = synonymsCache.getSynonym(token.termText());
            
            if(synonyms != null &&
                    !synonyms.isEmpty()){
                Token synonymToken = new Token(synonyms.get(0), token.startOffset(),
                                              token.endOffset(), "synonym");
                synonymToken.setPositionIncrement(0);

                token = synonymToken;
            } else {
                // if it is just a stop word filter it out
                String stemmedText = getStemmedText(token.termText());
                if(stemmedText.isEmpty()){
                    token = next();
                }
            }
        }
        
        return token;
    }
    
    private Token nextPhrase() throws IOException {
        while (true) {
            Token token = input.next();

            if (token == null) {
                if (phraseStartQueue.isEmpty()) {
                    return null;
                } else {
                    return phraseStartQueue.poll();
                }
            } else {
                String testPhrase = getTestPhrase(token);

                if (phrasesCache.isValidPhrase(testPhrase)) {
                    int startOffset = token.startOffset();    
                
                    if (!phraseStartQueue.isEmpty()) {
                        startOffset = phraseStartQueue.peek().startOffset();
                    }
                    
                    Token phraseToken = new Token(testPhrase, token.startOffset(),
                            token.endOffset(), "phrase");
                    phraseToken.setPositionIncrement(0);

                    phraseStartQueue.clear();
                    
                    return phraseToken;
                }

                if (phrasesCache.isStartOfPhrase(testPhrase)) {
                    phraseStartQueue.add(token);
                } else {
                    if (!phraseStartQueue.isEmpty()) {
                        Token qToken = phraseStartQueue.poll();
                        // eaten up this token so need to put it somewhere
                        phraseStartQueue.add(token);
                        return qToken;
                    } else {
                        return token;
                    }
                }
            }
        }
    }

    private String injectPhrases(Token currentToken) throws IOException {
        if (this.previousToken != null) {
            String phrase = this.previousToken.termText() + " "
                    + currentToken.termText();
            if (this.phrasesCache.isValidPhrase(phrase)) {
                Token phraseToken = new Token(phrase, currentToken.startOffset(),
                        currentToken.endOffset(), "phrase");
                phraseToken.setPositionIncrement(0);
                this.injectedTokensStack.push(phraseToken);
                return phrase;
            }
        }
        return null;
    }

    private void injectSynonyms(String text, Token currentToken) throws IOException {
        if (text != null) {
            List<String> synonyms = this.synonymsCache.getSynonym(text);
            if (synonyms != null) {
                for (String synonym : synonyms) {
                    Token synonymToken = new Token(synonym, currentToken.startOffset(),
                            currentToken.endOffset(), "synonym");
                    synonymToken.setPositionIncrement(0);
                    this.injectedTokensStack.push(synonymToken);
                }
            }
        }
    }
    
    private String getStemmedText(String text) throws IOException {
        StringBuilder sb = new StringBuilder();
        Reader reader = new StringReader(text);
        TokenStream tokenStream = this.stemmer.tokenStream(null, reader);
        Token token = tokenStream.next();
        while (token != null) {
            sb.append(token.termText());
            token = tokenStream.next();
            if (token != null) {
                sb.append(" ");
            }
        }
        return sb.toString();    
    }
    
    private String getTestPhrase(Token token){
                    StringBuilder testPhrase = new StringBuilder();

                if (!phraseStartQueue.isEmpty()) {
                    Iterator<Token> iterator = phraseStartQueue.iterator();
                    while (iterator.hasNext()) {
                        testPhrase.append(iterator.next().termText());
                        testPhrase.append(" ");
                    }
                }

                testPhrase.append(token.termText());
                
                return testPhrase.toString();
}
}
