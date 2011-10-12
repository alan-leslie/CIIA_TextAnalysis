/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alag.ci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author al
 */
public class CSVFile {
    public static List<String[]> getFileData(String fileName) {
        FileReader theReader = null;
        List<String[]> retVal = new ArrayList<String[]>();

        try {
            theReader = new FileReader(fileName);
            BufferedReader in = new BufferedReader(theReader);
            
            String theLine = null;
            
            while ((theLine = in.readLine()) != null) {
                String theLineArr[] = theLine.split(",");
                retVal.add(theLineArr);
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
        
        return retVal;
    }    
}
