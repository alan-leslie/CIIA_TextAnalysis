package com.alag.ci.textanalysis;

import java.io.IOException;

public interface PhrasesCache {
    public boolean isValidPhrase(String text) throws IOException;
    public boolean isStartOfPhrase(String text) throws IOException;
}
