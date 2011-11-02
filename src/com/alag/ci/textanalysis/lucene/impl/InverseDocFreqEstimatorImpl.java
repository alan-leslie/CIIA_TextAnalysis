package com.alag.ci.textanalysis.lucene.impl;

import java.util.*;

import com.alag.ci.textanalysis.InverseDocFreqEstimator;
import com.alag.ci.textanalysis.Tag;

public class InverseDocFreqEstimatorImpl implements InverseDocFreqEstimator {

    private Map<Tag,Integer> tagFreq = null;
    private int totalNumDocs;
    private int highestPercentage = 100;
    private int lowestPercentage = 0;
    
    public InverseDocFreqEstimatorImpl(int totalNumDocs) {
        this.totalNumDocs = totalNumDocs;
        this.tagFreq = new HashMap<Tag,Integer>();
    }
    
    /**
     * 
     * @param tag
     * @return
     */
    @Override
    public double estimateInverseDocFreq(Tag tag) {
        // return 0 if it has been pruned
        Integer freq = this.tagFreq.get(tag);
        if ((freq == null) || (freq.intValue() == 0)){
            return 1.;
        }
        
        double thePercentage = freq.doubleValue()/totalNumDocs * (double)100;
        
        if(thePercentage < lowestPercentage){
            return 0;
        }
        
        if(thePercentage > highestPercentage){
            return 0; 
        }
        
        return Math.log(totalNumDocs/freq.doubleValue());
    }
    
    /**
     * 
     * @param tag
     */
    @Override
    public void addCount(Tag tag) {
        Integer count = this.tagFreq.get(tag);
        if (count == null) {
            count = new Integer(1);
        } else {
            count = new Integer(count.intValue() + 1);
        }
        this.tagFreq.put(tag, count);
    }

    @Override
    public int noOfTags() {
        return tagFreq.size();
    }

    @Override
    public void outputFrequencies() {
        Iterator it = tagFreq.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
        }
    }

    @Override
    public void prune(int lowestPercentage, int highestPercentage) {
        this.lowestPercentage = lowestPercentage;
        this.highestPercentage = highestPercentage;
    }
}
