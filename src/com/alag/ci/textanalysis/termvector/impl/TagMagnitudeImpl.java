package com.alag.ci.textanalysis.termvector.impl;

import com.alag.ci.textanalysis.*;

public class TagMagnitudeImpl implements TagMagnitude {
    private Tag tag = null;
    private double magnitude ;
    
    public TagMagnitudeImpl(Tag tag, double magnitude) {
        this.tag = tag;
        this.magnitude = magnitude;
    }
    
    public Tag getTag() {
        return this.tag;
    }

    public double getMagnitude() {
        return this.magnitude;
    }
    
    public double getMagnitudeSqd() {
        return this.magnitude*this.magnitude;
    }
    
    public String getDisplayText() {
        return this.tag.getDisplayText();
    }
    
    public String getStemmedText() {
        return this.tag.getStemmedText();
    }

    @Override
    public String toString() {
       return "[" + this.tag.getDisplayText() + ", " + this.tag.getStemmedText() +
           ", " + this.getMagnitude() + "]";
    }
    
    public int compareTo(TagMagnitude o) {
        double diff = this.magnitude - o.getMagnitude();
        if (diff > 0) {
            return -1;
        }else if (diff < 0) {
            return 1;
        }
        return 0;
    }
    
    
}
