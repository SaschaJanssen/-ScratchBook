package weka;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleMatrixFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class UtilLucene {

    private static Version version = Version.LUCENE_36;

    public static List<String> tokenizeString(Analyzer analyzer, String phrase) {
        TokenStream stream = analyzer.tokenStream(null, new StringReader(phrase));

        List<String> result = streamAttributesToList(stream);
        return result;
    }
    
    public static String standardsAnalyzer(String phrase) {
        
        StandardAnalyzer sa = new StandardAnalyzer(version);
        TokenStream s = sa.tokenStream(" ", new StringReader(phrase));
    
        return streamAttributesToString(s);
    }

    public static List<String> ngramString(String phrase) {
        TokenStream inputTokenStream = new StandardTokenizer(version, new StringReader(phrase));
        
        @SuppressWarnings("deprecation")
        TokenStream shingleMatrixFilter = new ShingleMatrixFilter(inputTokenStream, 2, 2, ' ');
        
        //TokenStream n = new NGramTokenFilter(inputTokenStream, 2, 2);
        
        TokenFilter lowerCaseFilter = new LowerCaseFilter(version, shingleMatrixFilter);

        TokenStream stream = new StopFilter(version, lowerCaseFilter, StopAnalyzer.ENGLISH_STOP_WORDS_SET);

        List<String> result = streamAttributesToList(stream);

        return result;
    }

    private static List<String> streamAttributesToList(TokenStream stream) {
        List<String> result = new ArrayList<String>();

        try {
            while (stream.incrementToken()) {
                result.add(stream.getAttribute(CharTermAttribute.class).toString());
            }
        } catch (IOException e) {
            // not thrown b/c we're using a string reader...
            throw new RuntimeException(e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }
    
    private static String streamAttributesToString(TokenStream stream) {
        StringBuilder sb = new StringBuilder();
        
        try {
            while (stream.incrementToken()) {
                sb.append(stream.getAttribute(CharTermAttribute.class).toString());
                sb.append(" ");
            }
        } catch (IOException e) {
            // not thrown b/c we're using a string reader...
            throw new RuntimeException(e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        return sb.toString();
    }

}
