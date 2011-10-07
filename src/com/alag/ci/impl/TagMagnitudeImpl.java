package com.alag.ci.impl;

import com.alag.ci.TagMagnitude;

public class TagMagnitudeImpl implements TagMagnitude {
    private Double magnitude = 0.;
    private Long tagId = null;
    private String tagText = null;
	
 
    public TagMagnitudeImpl() {
    }
	
    public TagMagnitudeImpl(double magnitude, long tagId, String tagText) {
        this.magnitude = magnitude;
        this.tagId = tagId;
        this.tagText = tagText;
    }
	
    public TagMagnitudeImpl(TagMagnitude tm) {
        this(tm.getMagnitude(),tm.getTagId(),tm.getTagText());
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }

    public double getMagnitude() {
        return this.magnitude;
    }

    public Long getTagId() {
        return this.tagId;
    }
	
    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public int compareTo(TagMagnitude o) {
        int retValue = 1;
        if (o != null) {
            retValue = this.magnitude.compareTo(o.getMagnitude());
        }
        //reverse the order -- highest at the top
        return -1*retValue;
    }
	
    public String toString() {
        return "" + this.tagId + ", " + this.tagText + ", "+ this.magnitude;
    }

}
