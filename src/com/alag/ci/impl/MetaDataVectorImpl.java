package com.alag.ci.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alag.ci.MetaDataVector;
import com.alag.ci.TagMagnitude;

public class MetaDataVectorImpl implements MetaDataVector {
    private static final double PRECISION = 0.0001;
    private List<TagMagnitude> tagMagnitudeList = null;
    private Map<Long, TagMagnitude> tagMagnitudeMap = null;
	
	/**
	 * Constructor
	 * @param tagMagnitudeList
	 */
    public MetaDataVectorImpl(List<TagMagnitude> tagMagnitudeList) {
        this.tagMagnitudeList = tagMagnitudeList;
        this.tagMagnitudeMap = new HashMap<Long, TagMagnitude>();
        for (TagMagnitude tm : this.tagMagnitudeList) {
            this.tagMagnitudeMap.put(tm.getTagId(), tm);
        }
        normalize(this.tagMagnitudeList);
        Collections.sort(this.tagMagnitudeList);
	}
	
	/**
	 * Normalize the vector
	 * @param tmList
	 * @return
	 */
    protected List<TagMagnitude> normalize(List<TagMagnitude> tmList) {
        if (tmList.size() > 0) {
            double sumSqd = 0.;
            for (TagMagnitude tm : tmList) {
                sumSqd += tm.getMagnitude()*tm.getMagnitude();
            }
            //See if the magnitude == 1
            if (Math.abs(sumSqd - 1.) > PRECISION) {
                //If all the entries were 0, then change them to 
                //equal magnitude
                double factor ;
                if (sumSqd < PRECISION) {
                    factor = 1./Math.sqrt(tmList.size());
                    for (TagMagnitude tm : tmList) {
                        tm.setMagnitude(factor);
                    }
                } else {
                    factor = 1./Math.sqrt(sumSqd);
                    for (TagMagnitude tm : tmList) {
                        tm.setMagnitude(tm.getMagnitude()*factor);
                    }
                }
            }
        }
        return tmList;
	}
	
	/**
	 * Get the list of tag magnitudes
	 */
    public List<TagMagnitude> getTagMetaDataMagnitude() {
        return this.tagMagnitudeList;
    }
	
	/**
	 * Returns the dot product of this vector with another
	 * It assumes that the other vector is normalized
	 * 
	 */
    public double dotProduct(MetaDataVector other) {
        double result = 0.;
        List<TagMagnitude> otherTagMagnitudeList = other.getTagMetaDataMagnitude();
        for (TagMagnitude otm: otherTagMagnitudeList) {
            TagMagnitude tm = this.tagMagnitudeMap.get(otm.getTagId());
            if (tm != null) {
                result += tm.getMagnitude()*otm.getMagnitude();
            }
        }
        return result;
    }
	
	/**
	 * Computes the distance between two vectors. The number is between 0 and 1.41
	 * The smaller the distance closer the two vectors are to each other.
	 */
    public double distance(MetaDataVector other) {
        double result = 0.;
        Map<Long, TagMagnitude> localMap = new HashMap<Long, TagMagnitude>(this.tagMagnitudeMap);
        List<TagMagnitude> otherTagMagnitudeList = other.getTagMetaDataMagnitude();
		for (TagMagnitude otm: otherTagMagnitudeList) {
		    TagMagnitude tm = this.tagMagnitudeMap.get(otm.getTagId());
		    double diff = otm.getMagnitude();
		    if (tm != null) {
		        diff = otm.getMagnitude() - tm.getMagnitude();
		        //Remove it from the local map
		        localMap.remove(otm.getTagId());
		    } 
		    result += diff*diff;
		}
		for (TagMagnitude tm: localMap.values()) {
		    result += tm.getMagnitude()*tm.getMagnitude();
		}
		return result;
    }
    /**
     *  Returns a new normalized vector that is the addition of this
     *  and the other passed in vector
     *  @param  other   the other passed in vector
     *  @return a new normalized vector
     */ 
    public MetaDataVector add(MetaDataVector other) {
        return add(other,1);
    }
	
	/**
	 *  Returns a new normalized vector that is the addition of this
	 *  and the other passed in vector
	 *  @param	other	the other passed in vector
	 *  @return	a new normalized vector
	 */	
    public MetaDataVector add(MetaDataVector other, double otherScale) {
        List<TagMagnitude> otherList = other.getTagMetaDataMagnitude();
        Map<Long, TagMagnitude> mergeMap = new HashMap<Long, TagMagnitude>();
        for (TagMagnitude tm: otherList) {
            TagMagnitudeImpl newTm = new TagMagnitudeImpl(tm);
            newTm.setMagnitude(tm.getMagnitude()*otherScale);
            mergeMap.put(newTm.getTagId(), newTm);
        }

        for (Long tagId: this.tagMagnitudeMap.keySet()) {
            TagMagnitude thisTm = this.tagMagnitudeMap.get(tagId);
            TagMagnitude otherTm = mergeMap.get(tagId);
            if (otherTm == null) {
                otherTm = new TagMagnitudeImpl(thisTm);
                mergeMap.put(tagId, otherTm);
            } else {
                otherTm.setMagnitude(otherTm.getMagnitude()+ thisTm.getMagnitude());
            }
        }
 
        List<TagMagnitude> resultList = new ArrayList<TagMagnitude>(mergeMap.values());
        return new MetaDataVectorImpl(resultList);
    }
	
	/**
	 * toString
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer();
		for (TagMagnitude tm : this.tagMagnitudeList) {
		    sb.append("[").append(tm).append("],");
		}
		return sb.toString();
    }
}
