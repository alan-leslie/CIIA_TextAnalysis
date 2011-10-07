package com.alag.ci.textanalysis.termvector.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alag.ci.textanalysis.Tag;
import com.alag.ci.textanalysis.TagCache;
import com.alag.ci.textanalysis.TagMagnitude;
import com.alag.ci.textanalysis.TagMagnitudeVector;
import com.alag.ci.textanalysis.lucene.impl.TagCacheImpl;
import com.alag.ci.textanalysis.termvector.impl.TagMagnitudeImpl;
import com.alag.ci.textanalysis.termvector.impl.TagMagnitudeVectorImpl;

public class TagMagnitudeVectorTest extends TestCase {

    public void testNullTagMagnitude() {
        TagMagnitudeVector tmVector1 = new TagMagnitudeVectorImpl(null);
        System.out.println(tmVector1);
    }

    public void testSelfDotProduct() throws Exception {
        TagMagnitudeVector tmv = getTagMagnitudeVector();
        double dot = tmv.dotProduct(tmv);
        System.out.println("Self dot product =" + dot);
        assertTrue("Self dot product should be 1 and not " + dot,
                Math.abs(1 - dot) < 0.0001);
    }

    public void testCreateVectors() throws Exception {
        TagMagnitudeVector tmv = getTagMagnitudeVector();
        List<TagMagnitude> tmList = tmv.getTagMagnitudes();
        assertTrue("There should be 3 elements not " + tmList.size(),
                (tmList.size() == 3));
        System.out.println(tmv);
        //First tag should be brian, and last should be alan
        Tag tag1 = tmList.get(0).getTag();
        Tag tag3 = tmList.get(2).getTag();
        assertTrue("First tag should be brian " + tag1,
                "brian".equals(tag1.getStemmedText()));

        assertTrue("First tag should be chri" + tag3,
                "chri".equals(tag3.getStemmedText()));
    }

    public void testBasicOperations() throws Exception {
        TagCache tagCache = new TagCacheImpl();
        List<TagMagnitude> tmList = new ArrayList<TagMagnitude>();
        tmList.add(new TagMagnitudeImpl(tagCache.getTag("alan"), -5.));
        tmList.add(new TagMagnitudeImpl(tagCache.getTag("brian"), 1.));
        tmList.add(new TagMagnitudeImpl(tagCache.getTag("chris"), 2.));
        tmList.add(new TagMagnitudeImpl(tagCache.getTag("brian"), 2.));

        TagMagnitudeVector tmVector1 = new TagMagnitudeVectorImpl(tmList);
        List<TagMagnitude> tmv1 = tmVector1.getTagMagnitudes();
        double totalMagnitude1 = 0.0;
        for (TagMagnitude tm : tmv1) {
            totalMagnitude1 += tm.getMagnitudeSqd();
        }

        assertTrue("Total magnitude of normalised vector should be 1 not " + totalMagnitude1,
                Math.abs(1.0 - totalMagnitude1) < 0.0001);

        System.out.println(tmVector1);
        tmList = new ArrayList<TagMagnitude>();
        tmList.add(new TagMagnitudeImpl(tagCache.getTag("alan"), -4.));
        tmList.add(new TagMagnitudeImpl(tagCache.getTag("brian"), -2.));
        tmList.add(new TagMagnitudeImpl(tagCache.getTag("chris"), 1.));
        tmList.add(new TagMagnitudeImpl(tagCache.getTag("alan"), -3.));
        TagMagnitudeVector tmVector2 = new TagMagnitudeVectorImpl(tmList);
        double dot = tmVector1.dotProduct(tmVector2);
        double cosVal = 0.68434172;

        assertTrue("Dot product should be approx 0.68434172 not " + dot,
                Math.abs(dot - cosVal) < 0.0001);

        TagMagnitudeVector tmVector3 = tmVector1.add(tmVector2);
        System.out.println(tmVector3);
        //A should be first in tmVector3
        tmList = tmVector3.getTagMagnitudes();
        Tag tag = tmList.get(0).getTag();
        assertTrue("First tag should be alan " + tag,
                "alan".equals(tag.getStemmedText()));

        //Add the 2 vectors together
        List<TagMagnitudeVector> tmvList = new ArrayList<TagMagnitudeVector>();
        // tmvList.add(tmVector1);
        tmvList.add(tmVector2);

        TagMagnitudeVector tmVector4 = tmVector1.add(tmvList);
        System.out.println(tmVector4);
        tmvList.add(tmVector3);
        tmVector4 = tmVector1.add(tmvList);
        System.out.println(tmVector4);

    }

    private TagMagnitudeVector getTagMagnitudeVector() throws IOException {
        TagCache tagCache = new TagCacheImpl();
        List<TagMagnitude> tmList = new ArrayList<TagMagnitude>();
        tmList.add(new TagMagnitudeImpl(tagCache.getTag("alan"), 1.));
        tmList.add(new TagMagnitudeImpl(tagCache.getTag("brian"), 2.5));
        tmList.add(new TagMagnitudeImpl(tagCache.getTag("chris"), 1.5));
        tmList.add(new TagMagnitudeImpl(tagCache.getTag("alan"), 1.));
        TagMagnitudeVector tmVector = new TagMagnitudeVectorImpl(tmList);
        return tmVector;
    }
}
