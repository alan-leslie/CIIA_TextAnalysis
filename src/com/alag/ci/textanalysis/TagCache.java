package com.alag.ci.textanalysis;

import java.io.IOException;
import java.util.Collection;

public interface TagCache {
    public Tag getTag(String text) throws IOException  ;
    public Collection<Tag> getAllTags();
}
