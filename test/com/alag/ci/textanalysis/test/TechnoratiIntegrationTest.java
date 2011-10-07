package com.alag.ci.textanalysis.test;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * REf: Sax parsing We use the Apache Xerces-J parser in this book. See
 * http://xml.apache.org/xerces-j/. This parser comes with the complete SAX API
 * in Javadoc format.
 * http://www.phptr.com/articles/article.asp?p=26351&seqNum=4&rl=1
 * 
 * @author Satnam Alag
 * 
 */
public class TechnoratiIntegrationTest extends TestCase {
    private static final String TECHNORATI_API_KEY = "aa2d0294b0b14fff42f68b81d399d0e1";

    private static final String TECHNORATI_URL = "http://api.technorati.com/tag";

    /**
     * Reference http://technorati.com/developers/api/
     * 
     */
    public void testHttpClient() {
        String url = TECHNORATI_URL;
        String tag = "collective intelligence";
        int limit = 2;

        try {
            HttpClient client = new HttpClient();
            PostMethod method = new PostMethod(url);
            // Configure form parameters
            method.addParameter("key", TECHNORATI_API_KEY);
            method.addParameter("tag", tag);
            method.addParameter("limit", "" + limit);
            int statusCode = client.executeMethod(method);
            System.out.println("Status = " + statusCode);
            if (statusCode != 1) {
                String contents = method.getResponseBodyAsString();
                method.releaseConnection();
                System.out.println(contents);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testSAXParsing() throws Exception {
        // See http://www.phptr.com/articles/article.asp?p=26351&seqNum=4&rl=1
        // Tell the system which parser you want to use
        String jaxpPropertyName = "javax.xml.parsers.SAXParserFactory";
        if (System.getProperty(jaxpPropertyName) == null) {
            String apacheXercesPropertyValue = "org.apache.xerces.jaxp.SAXParserFactoryImpl";
            System.setProperty(jaxpPropertyName, apacheXercesPropertyValue);
        }
        // First make an instance of a parser factory,
        // then use that to create a parser object.
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        // Need a handler
        // parser.parse(filename, handler);
    }
    // public void testDigg() {
    // String url = "http://digg.com/submit";
    //  
    // try {
    // HttpClient client = new HttpClient();
    // PostMethod method = new PostMethod(url);
    // method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
    // new DefaultHttpMethodRetryHandler(3,false));
    //
    // //Configure form parameters
    // method.addParameter("phase", "2");
    // method.addParameter("url",
    // "http://utilitycomputing.itworld.com/4594/061201bonsite/page_1.html");
    // method.addParameter("title","Title");
    // method.addParameter("bodytext","Text");
    // method.addParameter("topic","tech_news");
    // int statusCode = client.executeMethod(method);
    // System.out.println("Status = " + statusCode);
    // if (statusCode != HttpStatus.SC_OK) {
    // System.err.println("Method failed: " + method.getStatusLine());
    // }
    //
    // String redirectLocation = "";
    // Header locationHeader = method.getResponseHeader("location");
    // if (locationHeader != null) {
    // redirectLocation = locationHeader.getValue();
    // } else {
    // // The response is invalid and did not provide the new location for
    // // the resource. Report an error or possibly handle the response
    // // like a 404 Not Found error.
    // }
    // System.out.println("redirectLocation="+redirectLocation);
    //      
    // if (statusCode != 1) {
    // String contents = method.getResponseBodyAsString();
    // method.releaseConnection();
    // System.out.println(contents);
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
}
