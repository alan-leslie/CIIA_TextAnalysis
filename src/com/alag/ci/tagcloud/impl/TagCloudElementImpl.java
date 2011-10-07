package com.alag.ci.tagcloud.impl;

import com.alag.ci.tagcloud.TagCloudElement;

public class TagCloudElementImpl implements TagCloudElement {
    private String fontSize = null;
    private Double weight = null;
    private String tagText = null;
    
    public TagCloudElementImpl(String tagText, double tagCount) {
        this.tagText = tagText;
        this.weight = tagCount;
    }
    
    public String getFontSize() {
        return this.fontSize;
    }

    public double getWeight() {
        return this.weight;
    }

    public String getTagText() {
       return this.tagText;
    }

    public int compareTo(TagCloudElement o) {
        return this.tagText.compareTo(o.getTagText());
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String toString() {
        return this.tagText + "," + this.fontSize + "(" + this.weight + ")";
    }
}
