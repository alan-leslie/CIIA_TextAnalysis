package com.alag.ci.textanalysis.lucene.impl;

import com.alag.ci.textanalysis.InverseDocFreqEstimator;
import com.alag.ci.textanalysis.Tag;

public class EqualInverseDocFreqEstimator implements InverseDocFreqEstimator {
    public double estimateInverseDocFreq(Tag tag) {
       return 1.0;
    }
    public void addCount(Tag tag){
        
    }
}
