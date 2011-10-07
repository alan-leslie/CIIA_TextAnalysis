package com.alag.ci.textanalysis.lucene.impl;

import java.io.IOException;
import java.util.*;

import com.alag.ci.textanalysis.*;

public class TagCacheImpl extends CacheImpl implements TagCache {    
    private Map<String,Tag> tagMap = null;
 
    public TagCacheImpl() {
        this.tagMap = new HashMap<String,Tag>();
    }
    
    public Tag getTag(String text) throws IOException {
        Tag tag = this.tagMap.get(text);
        if (tag == null ) {
            String stemmedText = getStemmedText(text);
            if (stemmedText.equals("?")) {
                System.out.println("Stemmed text is ?");
            }
             tag = new TagImpl(text, stemmedText);
             this.tagMap.put(stemmedText, tag);
        }
        return tag;
    }
    
    public Collection<Tag> getAllTags() {
        List<String> l = new ArrayList<String>();
        for (Tag tag: this.tagMap.values()) {
            System.out.println(tag);
            l.add(tag.getStemmedText());
        }
        Collections.sort(l);
        for (String s: l) {
            System.out.println(s);
        }
        return this.tagMap.values();
    }
 
    public static void main(String [] args) throws IOException {
        String text = "Collective Intelligence and Web2.0";
        TagCacheImpl t = new TagCacheImpl();
        System.out.println(t.getTag(text));
    }
}
