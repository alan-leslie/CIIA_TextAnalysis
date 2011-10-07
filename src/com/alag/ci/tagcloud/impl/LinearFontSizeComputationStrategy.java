package com.alag.ci.tagcloud.impl;

import com.alag.ci.tagcloud.FontSizeComputationStrategy;

public class LinearFontSizeComputationStrategy extends FontSizeComputationStrategyImpl
    implements FontSizeComputationStrategy {
 
    public LinearFontSizeComputationStrategy(int numSizes, String prefix) {
       super(numSizes,prefix);
    }
 
    protected double scaleCount(double count) {
        return  count;
    }
}
