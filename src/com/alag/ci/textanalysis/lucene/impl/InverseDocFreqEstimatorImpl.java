package com.alag.ci.textanalysis.lucene.impl;

import java.util.*;

import com.alag.ci.textanalysis.InverseDocFreqEstimator;
import com.alag.ci.textanalysis.Tag;

public class InverseDocFreqEstimatorImpl implements InverseDocFreqEstimator {

    private Map<Tag,Integer> tagFreq = null;
    private int totalNumDocs;
    
    public InverseDocFreqEstimatorImpl(int totalNumDocs) {
        this.totalNumDocs = totalNumDocs;
        this.tagFreq = new HashMap<Tag,Integer>();
    }
    
    public double estimateInverseDocFreq(Tag tag) {
        Integer freq = this.tagFreq.get(tag);
        if ((freq == null) || (freq.intValue() == 0)){
            return 1.;
        }
        return Math.log(totalNumDocs/freq.doubleValue());
    }
    
    public void addCount(Tag tag) {
        Integer count = this.tagFreq.get(tag);
        if (count == null) {
            count = new Integer(1);
        } else {
            count = new Integer(count.intValue() + 1);
        }
        this.tagFreq.put(tag, count);
    }

}
