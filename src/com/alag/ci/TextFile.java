
package com.alag.ci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author al
 */
public class TextFile {
    public static String getFileData(String fileName) {
        FileReader theReader = null;
        StringBuilder theTextBuilder = new StringBuilder();

        try {
            theReader = new FileReader(fileName);
            BufferedReader in = new BufferedReader(theReader);
            
            String str;
            while ((str = in.readLine()) != null) {
                theTextBuilder.append(str);
                // for some cases (see test kasteel) lucene is not seeing
                // different lines as being separated so jions the words on 
                // different lines - force it to see white space
                theTextBuilder.append(" ");
            }
        } catch (IOException e) {
            // ...
        } finally {
            if (null != theReader) {
                try {
                    theReader.close();
                } catch (IOException e) {
                    /* .... */
                }
            }
        }
        
        return theTextBuilder.toString();
    }    
}
