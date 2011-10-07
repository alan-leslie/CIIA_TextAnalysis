package com.alag.ci;

public interface TagMagnitude extends Comparable<TagMagnitude> {
    public Long getTagId();
    public String getTagText();
    public double getMagnitude();
    public void setMagnitude(double magnitude);
}
