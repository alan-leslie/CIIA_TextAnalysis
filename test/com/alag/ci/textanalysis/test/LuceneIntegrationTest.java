package com.alag.ci.textanalysis.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
//import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import junit.framework.TestCase;

public class LuceneIntegrationTest extends TestCase {
    private static final String INDEX_DIR = "/home/al/Lucene/index";
    private static final String DATA_DIR = "/home/al/Lucene/data";
    
    public void testLuceneAnalyzers() throws Exception {
        String text = "This is dummy text";
        System.out.println("Displaying ... ");
//        displayTokens(new StandardAnalyzer(),text);
//        List<Token> tokenList = tokensFromAnalysis(new StandardAnalyzer(), text);
//        assert(tokenList.size() == 2);
//        assert(tokenList.get(0).termText().equalsIgnoreCase("dummy"));
//        assert(tokenList.get(1).termText().equalsIgnoreCase("text"));
    }
//    
//    private static List<Token> tokensFromAnalysis(Analyzer analyzer, String text) 
//        throws IOException {
//        TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));
//        List<Token> tokenList = new ArrayList<Token>();
//        boolean condition =  true;
//        while (condition) {
//            Token token = stream.next();
//            if (token == null) {
//                condition = false;
//            } else {
//                tokenList.add(token);
//            }
//        }
//        return tokenList;
//    }
//    
//    private static void displayTokens(Analyzer analyzer, String text) 
//        throws IOException {
//        List<Token> tokenList = tokensFromAnalysis(analyzer, text);
//        for (Token t: tokenList) {
//            System.out.println("[" + t.termText() + "] ");
//        }
//    }
//    
//    /**
//     * Stop words http://www.onjava.com/onjava/2003/01/15/examples/EnglishStopWords.txt
//     * Lucene article http://www.onjava.com/pub/a/onjava/2003/01/15/lucene.html?page=2
//     *
//     */
//    public void testLuceneIndexing() {
//        try {
//            //Index all the files
//            File indexDir = new File(INDEX_DIR);
//            File dataDir = new File(DATA_DIR);
//            IndexWriter writer = new IndexWriter(indexDir,
//                    new StandardAnalyzer(),true);
//            indexDirectory(writer,dataDir);
//            
//            int numIndexed = writer.docCount();
//            System.out.println("numIndexed=" + numIndexed);
//            assert(numIndexed == 13);
//            writer.optimize();
//            writer.close();
//            
//            //Now search for relevant terms
//            String q ="Collective Intelligence";
//            Directory dir = FSDirectory.getDirectory(indexDir, false);
//            IndexSearcher is = new IndexSearcher(dir);
//            
//            QueryParser theParser = new QueryParser("content", new StandardAnalyzer());       
//            Query query = theParser.parse(q);
//            Hits search = is.search(query);
//            int noOfHits = search.length();
//            System.out.println("no of hits:" + Integer.toString(noOfHits));  
//            assert(noOfHits == 1);
//        } catch (Exception e) {
//            System.out.println(e);
//            e.printStackTrace();
//        }
//        
//    }
//    
//    private static void indexDirectory(IndexWriter writer, File dir) 
//        throws IOException {
//        File[] files = dir.listFiles();
//        
//        for (File f: files) {
//            if (f.isDirectory()) {
//                indexDirectory(writer,f);
//            } else if(f.getName().endsWith(".txt")){
//                indexFile(writer,f);
//            }
//        }
//    }
//    
//    private static void indexFile(IndexWriter writer, File f) 
//        throws IOException {
//        System.out.println("Indexing ... " + f.getCanonicalPath());
//        Document doc = new Document();
//        String text = readFile(f);
//     //   doc.add(Field.Text("contents",content));
//        doc.add(new Field("content", text,Field.Store.YES, Field.Index.TOKENIZED));
//        writer.addDocument(doc);
//       // System.out.println(text);
//
//    }
    
    private static String readFile(File f) throws IOException {
        String sTheFileContent = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader(f));
            String nextLine = "";
            StringBuffer sb = new StringBuffer();
            while ((nextLine = br.readLine()) != null) {
              sb.append(nextLine);
            }
            sTheFileContent = sb.toString();
        } catch(Exception e)  {
            sTheFileContent = null;
        }
        return sTheFileContent;
     }

}
