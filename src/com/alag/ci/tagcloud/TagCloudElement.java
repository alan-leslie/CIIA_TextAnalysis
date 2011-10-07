package com.alag.ci.tagcloud;

public interface TagCloudElement extends Comparable<TagCloudElement> {
    public String getTagText();
    public double getWeight();
    public String getFontSize();
    public void setFontSize(String fontSize);
}
