package com.alag.ci.textanalysis.lucene.impl;

import com.alag.ci.textanalysis.Tag;

public class TagImpl implements Tag {
    private String displayText = null;
    private String stemmedText = null;
    private int hashCode ;
    
    public TagImpl(String displayText, String stemmedText) {
        this.displayText = displayText;
        this.stemmedText = stemmedText;
        hashCode = stemmedText.hashCode();
    }

    public String getDisplayText() {
        return displayText;
    }

    public String getStemmedText() {
        return stemmedText;
    }

    @Override
    public boolean equals(Object obj) {
        return (this.hashCode == obj.hashCode());
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public String toString() {
        return "[" + this.displayText + ", " + this.stemmedText + "]";
    } 
}
