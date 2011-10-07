package com.alag.ci.textanalysis.termvector.impl;

import java.util.*;
import com.alag.ci.textanalysis.*;

public class TagMagnitudeVectorImpl implements TagMagnitudeVector {
    private Map<Tag,TagMagnitude> tagMagnitudesMap = null;
    
    public TagMagnitudeVectorImpl(List<TagMagnitude> tagMagnitudes) {
        normalize(tagMagnitudes);
    }
    
    private void normalize(List<TagMagnitude> tagMagnitudes) {
        tagMagnitudesMap = new HashMap<Tag,TagMagnitude>();
        if ( (tagMagnitudes == null) || (tagMagnitudes.size() == 0)) {
            return;
        }
        double sumSqd = 0.;
        for (TagMagnitude tm: tagMagnitudes) {
            sumSqd += tm.getMagnitudeSqd();
        }
        if (sumSqd == 0. ) {
            sumSqd = 1./tagMagnitudes.size();
        }
        double normFactor = Math.sqrt(sumSqd);
        for (TagMagnitude tm: tagMagnitudes) {
            TagMagnitude otherTm = this.tagMagnitudesMap.get(tm.getTag());
            double magnitude = tm.getMagnitude();
            if (otherTm != null) {
                magnitude = mergeMagnitudes(magnitude,
                        otherTm.getMagnitude()*normFactor);
            }           
            TagMagnitude normalizedTm = new TagMagnitudeImpl(tm.getTag(),
                    (magnitude/normFactor));
            this.tagMagnitudesMap.put(tm.getTag(), normalizedTm);
        }
    }

    public List<TagMagnitude> getTagMagnitudes() {
        List<TagMagnitude> sortedTagMagnitudes = new ArrayList<TagMagnitude>();
        sortedTagMagnitudes.addAll(tagMagnitudesMap.values());
        Collections.sort(sortedTagMagnitudes);
        return sortedTagMagnitudes;
    }
    
    public Map<Tag,TagMagnitude> getTagMagnitudeMap() {
        return this.tagMagnitudesMap;
    }
    
    private double mergeMagnitudes(double a, double b) {
        return Math.sqrt(a*a + b*b);
    }
 
    public double dotProduct(TagMagnitudeVector o) {
        Map<Tag,TagMagnitude> otherMap = o.getTagMagnitudeMap() ;
        double dotProduct = 0.;
        for (Tag tag: this.tagMagnitudesMap.keySet()) {
            TagMagnitude otherTm = otherMap.get(tag);
            if (otherTm != null) {
                TagMagnitude tm = this.tagMagnitudesMap.get(tag);
                dotProduct += tm.getMagnitude()*otherTm.getMagnitude();
            }
        }
        return dotProduct;
    }
    
    public TagMagnitudeVector add(TagMagnitudeVector o) {
        Map<Tag,TagMagnitude> otherMap = o.getTagMagnitudeMap() ;
        Map<Tag,Tag> uniqueTags = new HashMap<Tag,Tag>();
        for (Tag tag: this.tagMagnitudesMap.keySet()) {
            uniqueTags.put(tag,tag);
        }
        for (Tag tag:  otherMap.keySet()) {
            uniqueTags.put(tag,tag);
        }
        List<TagMagnitude> tagMagnitudesList = new ArrayList<TagMagnitude>(uniqueTags.size());
        for (Tag tag: uniqueTags.keySet()) {
            TagMagnitude tm = mergeTagMagnitudes(this.tagMagnitudesMap.get(tag), 
                    otherMap.get(tag));
            tagMagnitudesList.add(tm);          
        }
        return new TagMagnitudeVectorImpl(tagMagnitudesList);
    }
    
    public TagMagnitudeVector add(Collection<TagMagnitudeVector> tmList) {
        Map<Tag,Double> uniqueTags = new HashMap<Tag,Double>();
        for (TagMagnitude tagMagnitude: this.tagMagnitudesMap.values()) {          
            uniqueTags.put(tagMagnitude.getTag(), 
                    new Double(tagMagnitude.getMagnitudeSqd()));
        }
        for (TagMagnitudeVector tmv : tmList) {
            Map<Tag,TagMagnitude> tagMap= tmv.getTagMagnitudeMap();
            for (TagMagnitude tm: tagMap.values()) {
                Double sumSqd = uniqueTags.get(tm.getTag());
                if (sumSqd == null) {
                    uniqueTags.put(tm.getTag(), tm.getMagnitudeSqd());
                } else {
                    sumSqd = new Double(sumSqd.doubleValue() + tm.getMagnitudeSqd());
                    uniqueTags.put(tm.getTag(), sumSqd);
                }
            }
        }
        List<TagMagnitude> newList = new ArrayList<TagMagnitude>();
        for (Tag tag: uniqueTags.keySet()) {
            newList.add(new TagMagnitudeImpl(tag,Math.sqrt(uniqueTags.get(tag))));
        }
        return new TagMagnitudeVectorImpl(newList);
    }
    
    private TagMagnitude mergeTagMagnitudes(TagMagnitude a, TagMagnitude b) {
        if (a == null) {
            if (b == null) {
                return null;
            }
            return b;
        } else if (b == null) {
            return a;
        } else {
            double magnitude = mergeMagnitudes(a.getMagnitude(), b.getMagnitude());
            return new TagMagnitudeImpl(a.getTag(),magnitude);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<TagMagnitude> sortedList = getTagMagnitudes();
        double sumSqd = 0.;
        for (TagMagnitude tm: sortedList) {
            sb.append(tm);
            sumSqd += tm.getMagnitude()*tm.getMagnitude();
        }
       // sb.append("\nSumSqd = " + sumSqd);
        return sb.toString();
    }
}
