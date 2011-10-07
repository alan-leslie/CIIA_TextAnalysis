package com.alag.ci.tagcloud.impl;

import java.util.List;

import com.alag.ci.tagcloud.FontSizeComputationStrategy;
import com.alag.ci.tagcloud.TagCloudElement;

public abstract class FontSizeComputationStrategyImpl implements
        FontSizeComputationStrategy {
    private static final double PRECISION = 0.00001;
    private Integer numSizes = null;
    private String prefix = null;
    
    public FontSizeComputationStrategyImpl(int numSizes, String prefix) {
        this.numSizes = numSizes;
        this.prefix = prefix;
    }
    
    public int getNumSizes() {
        return this.numSizes;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public void computeFontSize(List<TagCloudElement> elements) {
        if (elements.size() > 0) {
            Double minCount = null;
            Double maxCount = null;
            for (TagCloudElement tce: elements) {
                double n = tce.getWeight();
                if ( (minCount == null) || (minCount > n)) {
                    minCount = n;
                }
                if ( (maxCount == null) || (maxCount < n)) {
                    maxCount = n;
                }
            }
            double maxScaled = scaleCount(maxCount);
            double minscaled = scaleCount(minCount);
            double diff = (maxScaled - minscaled)/(double)this.numSizes;
 
            //Assign the font size
            for (TagCloudElement tce: elements) {
                int index = (int) Math.floor((scaleCount(tce.getWeight()) - minscaled)/diff);  
                if (Math.abs(tce.getWeight() - maxCount) < PRECISION) {
                    index = this.numSizes - 1;
                }
                tce.setFontSize(this.prefix + index);
            }
        }
    }
    
    protected abstract double scaleCount(double count) ;

}
