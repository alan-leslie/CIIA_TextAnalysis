package com.alag.ci.textanalysis.lucene.impl;

import java.io.*;
import java.util.*;

import org.apache.lucene.analysis.*;

import com.alag.ci.tagcloud.TagCloud;
import com.alag.ci.tagcloud.TagCloudElement;
import com.alag.ci.tagcloud.impl.HTMLTagCloudDecorator;
import com.alag.ci.tagcloud.impl.LinearFontSizeComputationStrategy;
import com.alag.ci.tagcloud.impl.TagCloudElementImpl;
import com.alag.ci.tagcloud.impl.TagCloudImpl;
import com.alag.ci.textanalysis.*;
import com.alag.ci.textanalysis.termvector.impl.*;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public final class LuceneTextAnalyzer implements TextAnalyzer {
    private TagCache tagCache = null;
    private InverseDocFreqEstimator inverseDocFreqEstimator = null;
    
    public LuceneTextAnalyzer(TagCache tagCache, 
            InverseDocFreqEstimator inverseDocFreqEstimator) {
        this.tagCache = tagCache;
        this.inverseDocFreqEstimator = inverseDocFreqEstimator;
    }
    
    public List<Tag> analyzeText(String text) throws IOException {
        Reader reader = new StringReader(text);
        Analyzer analyzer = getAnalyzer();
        List<Tag> tags = new ArrayList<Tag>();
        TokenStream tokenStream = analyzer.tokenStream(null, reader) ;
//        Token token = tokenStream.next();
//        while ( token != null) {
//            Tag tag = getTag(token.termText());
//            tags.add(tag);
//            token = tokenStream.next();
//        }
        
        CharTermAttribute termAttr = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            String theTerm = termAttr.toString();
            Tag tag = getTag(theTerm);
            tags.add(tag);
        }
        
        return tags;
    }
    
    public TagMagnitudeVector createTagMagnitudeVector(String text) throws IOException {
        List<Tag> tagList =  analyzeText(text);
        Map<Tag,Integer> tagFreqMap = computeTermFrequency(tagList);
        return applyIDF(tagFreqMap);
    }
    
  
    private Map<Tag,Integer> computeTermFrequency(List<Tag> tagList) {
        Map<Tag,Integer> tagFreqMap = new HashMap<Tag,Integer>();
        for (Tag tag: tagList) {
            Integer count = tagFreqMap.get(tag);
            if (count == null) {
                count = new Integer(1);
            } else {
                count = new Integer(count.intValue() + 1);
            }
            tagFreqMap.put(tag, count);
        }
        return tagFreqMap;
    }
    
    private TagMagnitudeVector applyIDF(Map<Tag,Integer> tagFreqMap) {
        List<TagMagnitude> tagMagnitudes = new ArrayList<TagMagnitude>();
        for (Tag tag: tagFreqMap.keySet()) {
            double idf = this.inverseDocFreqEstimator.estimateInverseDocFreq(tag);
            double tf = tagFreqMap.get(tag);
            double wt = tf*idf;
            tagMagnitudes.add(new TagMagnitudeImpl(tag,wt));
        }
        return new TagMagnitudeVectorImpl(tagMagnitudes);
    }
    
    
    private Tag getTag(String text) throws IOException {
        return this.tagCache.getTag(text);
    }
    
    protected Analyzer getAnalyzer() throws IOException {
        return new SynonymPhraseStopWordAnalyzer(new SynonymsCacheImpl(),
                new PhrasesCacheImpl());
    }
    
    public static TagCloud createTagCloud(TagMagnitudeVector tmVector) {
        List<TagCloudElement> elements = new ArrayList<TagCloudElement>();
        for (TagMagnitude tm: tmVector.getTagMagnitudes()) {
            TagCloudElement element = new TagCloudElementImpl(tm.getDisplayText(), tm.getMagnitude());
            elements.add(element);
        }
        
        System.out.println("No of Elements is :" + Integer.toString(elements.size()));
        return new TagCloudImpl(elements, new LinearFontSizeComputationStrategy(3,"font-size: "));
    }
    
    public static String visualizeTagCloud(TagCloud tagCloud) {
        HTMLTagCloudDecorator decorator = new HTMLTagCloudDecorator();
        String html = decorator.decorateTagCloud(tagCloud);
        System.out.println(html);
        return html;
    }

    public static void main(String [] args) throws IOException {
        // test ci = coolective int - simple synonym
        // collective inteli = ci - phrase synonym
        // intelli = intel - simple synonym
        // first word part fo phrase second not
        // first word part of phrase second null
        // first word not part of phrase
        // first word part of phrase 
        // second not
        // first word synonym
        // as above but second is a synonym
        // second word start of phrase
        String title = "Collective Intelligence and Web2.0";
        String body = "Web2.0 is all about connecting a user to users, "  +
                " inviting users to participate and applying their collects" +
                " intelligence to improve the application. Collective intelligence" +
                " enhances the user experience" ;
//        
//        
// "Advertisement" + 
// "I think a lot of us CSS authors are doing it wrong. We are selfish by nature; we get into our little bubbles, writing CSS (as amazing as it may be) with only ourselves in mind. How many times have you inherited a CSS file that's made you say WTF at least a dozen times?" +
//"(Image: Toca Boca)" +
//"HTML has a standard format and syntax that everyone understands. For years, programmers have widely agreed on standards for their respective languages. CSS doesn't seem to be there yet: everyone has their own favorite format, their own preference between single-line and multi-line, their own ideas on organization, and so on." +
//"New new Way"; // of Thinking" ; //+
//"Recently, I have begun to think that CSS authors could take a leaf from the programmers' book. We need to write CSS that others can understand and use with ease. Programmers have been writing sharable code since day one, and it's high time that CSS be written with as much organization and openness." +
//"In writing inuit.css and working on a huge front-end framework at my job, it has become more apparent to me that writing code that can be easily picked up by others is extremely important. I wouldn't say that I've nailed everything yet, but I'll share with you some things that I think are vital when writing code, specifically CSS, that will be used by others." +
//"First, the reasoning: my number one tip for developers is to always code like you're working in a team, even when you're not. You may be the only developer on your project right now, but it might not stay that way:" +
//"Your project could be taken to another developer, agency or team. Even though this is not the best situation to find yourself in, handing over your work smoothly and professionally to others is ideal." +
//"If you're doing enough work to warrant employing someone else or expanding the team at all, then your code ceases to be yours and becomes the team's." +
//"You could leave the company, take a vacation or be off sick, at which point someone else will inherit your code, even if only temporarily." +
//"Someone will inevitably poke through your source code, and if they've never met you, this could be the only basis on which they judge your work. First impressions count!" +
//"Comments Are King!" +
//"One thing I've learned from building a massive front-end framework at work and from producing inuit.css is that comments are vital. Comments, comments, comments. Write one line of code, then write about it. N.B. This is not meant to mean write about every line of code, as that would be overkill. Only comment where it helps/is useful." +
//"It might seem like overkill at first, but write about everything you do. The code might look simple to you, but there's bound to be someone out there who has no idea what it does. Write it down. I had already gotten into this habit when I realized that this was the same technique that a good friend and incredibly talented developer, Nick Payne, told me about. That technique is called rubber-duck debugging:" +
//" an unnamed expert programmer would keep a rubber duck by his desk at all times, and debug his code by forcing himself to explain it, line by line, to the duck." +
//"Write comments like you're talking to a rubber duck!" +
//"Good comments take care of 99% of what you hand over and more importantly take care of your documentation. Your code should be the documentation." +
//"Comments are also an excellent way to show off. Ever wanted to tell someone how awesome a bit of your code is but never found the chance? This is that chance! Explain how clever it is, and just wait for people to read it." +
//"Egos aside, though, comments do force you to write nicer code. I've found that writing extensive comments has made me a better developer. I write cleaner code, because writing comments reminds me that I'm intending for others to read the code." +
//"Multi-Line CSS" +
//"This issue really divides developers: single-line versus multi-line CSS. I've always written multi-line CSS. I love it and despise single-line notation. But others think the opposite and they're no more right or wrong than I am. Taste is taste, and consistency is what matters." +
//"Having said that, when working on a team, I firmly believe that multi-line CSS is the way to go. Multi-line ensures that each CSS declaration is accounted for. One line represents one piece of functionality (and can often be attributed to one person)." +
//"As a result, each line will show up individually on a diff between two versions. If you change, say, only one hex value in a color declaration, then that is all that needs to be flagged. A diff on a single-line document would flag an entire rule set as having been changed, even when it hasn't." +
//"Take the following example:" +
//"Above, we just changed a color value in a rule set, but because it was a single-line CSS file, the entire rule set appears to have changed. This is very misleading, and also not very readable or obvious. At first glance, it appears that a whole rule set has been altered. But look closely and you'll see that only #333 has been changed to #666. We can make this distinction far more obvious by using multi-line CSS, like so:" +
//"Having said all this, I am by no means a version-control expert. I've only just started using GitHub for inuit.css, so I'm very new to it all. Instead, I'll leave you with Jason Cale's excellent article on the subject." +
//"Furthermore, single-line CSS makes commenting harder. Either you end up with one comment per rule set (which means your comments might be less specific than had they been done per line), or you get a messy single line of comment, then code, then comment again, as shown here:" +
//"With multi-line CSS, you have a much neater comment structure:" +
//"Ordering CSS Properties" +
//"Likewise, the order in which people write their CSS properties is very personal." +
//"Many people opt for alphabetized CSS, but this is counter-intuitive. I commented briefly on the subject on GitHub; my reasoning is that ordering something by a meaningless metric makes no sense; the initial letter of a declaration has no bearing on the declaration itself. Ordering CSS alphabetically makes as much sense as ordering CDs by how bright their covers are." + 
//"A more sensible approach is to order by type and relevance. That is, group your color declarations together, your box-model declarations together, your font declarations together and so on. Moreover, order each grouping according to its relevance to the selector. If you are styling an h1, then put font-related declarations first, followed by the others. For example:";

                
        TagCacheImpl t = new TagCacheImpl();
        InverseDocFreqEstimator idfEstimator = new EqualInverseDocFreqEstimator();
        LuceneTextAnalyzer lta = new LuceneTextAnalyzer(t, idfEstimator);
//        System.out.print("Analyzing the title .... \n");
//        lta.displayTextAnalysis(title);
//        System.out.print("\nAnalyzing the body .... \n");
//        lta.displayTextAnalysis(body);
        TagMagnitudeVector tmTitle = lta.createTagMagnitudeVector(title);
        TagMagnitudeVector tmBody = lta.createTagMagnitudeVector(body);
        TagMagnitudeVector tmCombined = tmTitle.add(tmBody);
        System.out.println(tmCombined);    
        TagCloud tagCloud = LuceneTextAnalyzer.createTagCloud(tmCombined);
        String html = LuceneTextAnalyzer.visualizeTagCloud(tagCloud);
        
    }
    
    private void displayTextAnalysis(String text) throws IOException {
        List<Tag> tags = analyzeText(text);
        for (Tag tag: tags) {
            System.out.print(tag + " ");
        }      
    }
}
