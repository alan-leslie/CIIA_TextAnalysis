package com.alag.ci.textanalysis;

import java.io.IOException;
import java.util.List;

public interface SynonymsCache {
    public List<String> getSynonym(String text) throws IOException ;
}
