package com.alag.ci.textanalysis.lucene.impl;

import com.alag.ci.textanalysis.InverseDocFreqEstimator;
import com.alag.ci.textanalysis.Tag;
import java.util.HashSet;
import java.util.Set;

public class EqualInverseDocFreqEstimator implements InverseDocFreqEstimator {
    private Set<Tag> tags = new HashSet<Tag>();
        
    public double estimateInverseDocFreq(Tag tag) {
        tags.add(tag);
       return 1.0;
    }
    public void addCount(Tag tag){
        
    }

    @Override
    public int noOfTags() {
        return tags.size();
    }

    @Override
    public void outputFrequencies() {
        for(Tag theTag: tags){
            System.out.println(theTag.getDisplayText() + ", 1");           
        }
    }

    @Override
    public void prune(int lowest, int hightest) {
        // all equal so cannot prune
    }
}
