package com.alag.ci.textanalysis;

import com.alag.ci.MetaDataVector;

public interface MetaDataExtractor {
    public MetaDataVector extractMetaData(String title, String body);
}
