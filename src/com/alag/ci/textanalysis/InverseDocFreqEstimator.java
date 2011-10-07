package com.alag.ci.textanalysis;

public interface InverseDocFreqEstimator {
    public double estimateInverseDocFreq(Tag tag);
    public void addCount(Tag tag);
}
