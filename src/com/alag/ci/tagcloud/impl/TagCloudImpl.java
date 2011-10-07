/**
 * 
 */
package com.alag.ci.tagcloud.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alag.ci.MetaDataVector;
import com.alag.ci.TagMagnitude;
import com.alag.ci.tagcloud.FontSizeComputationStrategy;
import com.alag.ci.tagcloud.TagCloud;
import com.alag.ci.tagcloud.TagCloudElement;

/**
 * @author Satnam Alag
 *
 */
public class TagCloudImpl implements TagCloud {
    
    private List<TagCloudElement> elements = null;
    
    public TagCloudImpl(List<TagCloudElement> elements,
                        FontSizeComputationStrategy strategy) {
        this.elements = elements;
        strategy.computeFontSize(this.elements);
        Collections.sort(this.elements);
    }
    
    public TagCloudImpl(MetaDataVector metaDataVector,
                        FontSizeComputationStrategy strategy) {
      this(getTagCloudElements(metaDataVector),strategy);
    }
    
    private static List<TagCloudElement> getTagCloudElements(MetaDataVector metaDataVector) {
        ArrayList<TagCloudElement> list = new ArrayList<TagCloudElement>();
        for (TagMagnitude tm : metaDataVector.getTagMetaDataMagnitude()) {
            list.add(new TagCloudElementImpl(tm.getTagText(),
                    tm.getMagnitude()));
        }    
        return list;
    }

    public List<TagCloudElement> getTagCloudElements() {
       return this.elements;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (TagCloudElement tce: this.elements) {
            sb.append("[" + tce + "] ");
        }
        return sb.toString();
    }
}
