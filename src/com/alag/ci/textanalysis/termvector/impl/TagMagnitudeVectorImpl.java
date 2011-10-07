package com.alag.ci.textanalysis.termvector.impl;

import java.util.*;
import com.alag.ci.textanalysis.*;

public class TagMagnitudeVectorImpl implements TagMagnitudeVector {

    private Map<Tag, TagMagnitude> tagMagnitudesMap = null;

    public TagMagnitudeVectorImpl(List<TagMagnitude> tagMagnitudes) {
        normalize(tagMagnitudes);
    }

    private void normalize(List<TagMagnitude> tagMagnitudes) {
        if ((tagMagnitudes == null) || (tagMagnitudes.isEmpty())) {
            return;
        }

        Map<Tag, TagMagnitude> notNormTagMagnitudesMap = new HashMap<Tag, TagMagnitude>();
        for (TagMagnitude tm : tagMagnitudes) {
            TagMagnitude otherTm = notNormTagMagnitudesMap.get(tm.getTag());
            double magnitude = tm.getMagnitude();
            if (otherTm != null) {
                magnitude = magnitude + otherTm.getMagnitude();
            }
            TagMagnitude newNotNormalizedTm = new TagMagnitudeImpl(tm.getTag(),
                    magnitude);
            notNormTagMagnitudesMap.put(tm.getTag(), newNotNormalizedTm);
        }

        double sumSqd = 0.;
        for (TagMagnitude tm : notNormTagMagnitudesMap.values()) {
            sumSqd += tm.getMagnitudeSqd();
        }
        if (sumSqd == 0.) {
            sumSqd = 1. / notNormTagMagnitudesMap.size();
        }

        tagMagnitudesMap = new HashMap<Tag, TagMagnitude>();
        double normFactor = Math.sqrt(sumSqd);
        for (TagMagnitude tm : notNormTagMagnitudesMap.values()) {
            TagMagnitude otherTm = this.tagMagnitudesMap.get(tm.getTag());
            double magnitude = tm.getMagnitude();
            if (otherTm != null) {
                magnitude = mergeMagnitudes(magnitude,
                        otherTm.getMagnitude() * normFactor);
            }
            TagMagnitude normalizedTm = new TagMagnitudeImpl(tm.getTag(),
                    (magnitude / normFactor));
            tagMagnitudesMap.put(tm.getTag(), normalizedTm);
        }
    }

    public List<TagMagnitude> getTagMagnitudes() {
        List<TagMagnitude> sortedTagMagnitudes = new ArrayList<TagMagnitude>();
        if (tagMagnitudesMap != null) {
            sortedTagMagnitudes.addAll(tagMagnitudesMap.values());
            Collections.sort(sortedTagMagnitudes);
        }
        return sortedTagMagnitudes;
    }

    public Map<Tag, TagMagnitude> getTagMagnitudeMap() {
        Map<Tag, TagMagnitude> theMap = new HashMap<Tag, TagMagnitude>();
        theMap.putAll(tagMagnitudesMap);
        return theMap;
    }

    private double mergeMagnitudes(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }

    public double dotProduct(TagMagnitudeVector o) {
        Map<Tag, TagMagnitude> otherMap = o.getTagMagnitudeMap();
        double dotProduct = 0.;
        for (Tag tag : this.tagMagnitudesMap.keySet()) {
            TagMagnitude otherTm = otherMap.get(tag);
            if (otherTm != null) {
                TagMagnitude tm = this.tagMagnitudesMap.get(tag);
                dotProduct += tm.getMagnitude() * otherTm.getMagnitude();
            }
        }
        return dotProduct;
    }

    public TagMagnitudeVector add(TagMagnitudeVector o) {
        Map<Tag, TagMagnitude> otherMap = o.getTagMagnitudeMap();
        Map<Tag, Tag> uniqueTags = new HashMap<Tag, Tag>();
        for (Tag tag : this.tagMagnitudesMap.keySet()) {
            uniqueTags.put(tag, tag);
        }
        for (Tag tag : otherMap.keySet()) {
            uniqueTags.put(tag, tag);
        }
        List<TagMagnitude> tagMagnitudesList = new ArrayList<TagMagnitude>(uniqueTags.size());
        for (Tag tag : uniqueTags.keySet()) {
            TagMagnitude tm = mergeTagMagnitudes(this.tagMagnitudesMap.get(tag),
                    otherMap.get(tag));
            tagMagnitudesList.add(tm);
        }
        return new TagMagnitudeVectorImpl(tagMagnitudesList);
    }

    public TagMagnitudeVector add(Collection<TagMagnitudeVector> tmList) {
        Map<Tag, Double> uniqueTags = new HashMap<Tag, Double>();
        for (TagMagnitude tagMagnitude : this.tagMagnitudesMap.values()) {
            uniqueTags.put(tagMagnitude.getTag(),
                    new Double(tagMagnitude.getMagnitudeSqd()));
        }
        for (TagMagnitudeVector tmv : tmList) {
            Map<Tag, TagMagnitude> tagMap = tmv.getTagMagnitudeMap();
            for (TagMagnitude tm : tagMap.values()) {
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
        for (Tag tag : uniqueTags.keySet()) {
            newList.add(new TagMagnitudeImpl(tag, Math.sqrt(uniqueTags.get(tag))));
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
            return new TagMagnitudeImpl(a.getTag(), magnitude);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<TagMagnitude> sortedList = getTagMagnitudes();
        double sumSqd = 0.;
        for (TagMagnitude tm : sortedList) {
            sb.append(tm);
            sumSqd += tm.getMagnitude() * tm.getMagnitude();
        }
        // sb.append("\nSumSqd = " + sumSqd);
        return sb.toString();
    }
}
