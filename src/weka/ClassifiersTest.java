package weka;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ClassifiersTest {

    private String bayesClassificationString = "NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEGATIVE,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,POSITIVE,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEGATIVE,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEGATIVE,NEUTRAL,POSITIVE,NEUTRAL";
    private String j48ClassificationString = "NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEGATIVE,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEGATIVE,NEGATIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEGATIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEGATIVE,NEUTRAL,POSITIVE,NEUTRAL";
    private String[] masterBayes;
    private String[] masterJ48;
    private List<String> testDataSet;
    private List<TrainingDataValueObject> trainingDataSet;

    @Before
    public void setUp() throws Exception {
        masterJ48 = j48ClassificationString.split(",");
        masterBayes = bayesClassificationString.split(",");
        
        trainingDataSet = readLearningData();
        testDataSet = readTestDataFile();
    }
    
    @Test(expected=IllegalStateException.class)
    public void testClassifierException() throws Exception {
        BaseClassifier classifier = new BayesClassifier();
        classifier.classify(testDataSet);
    }

    @Test
    public void testWekaClassifierBayes() throws Exception {
        BaseClassifier classifier = new BayesClassifier();
        classifier.train(trainingDataSet);
        List<String> result = classifier.classify(testDataSet);

        assertEquals(masterBayes.length, result.size());

        for (int i = 0; i < masterBayes.length; i++) {
            assertEquals(masterBayes[i], result.get(i));
        }
    }

    @Test
    public void testWekaClassifierJ48() {
        BaseClassifier classifier = new J48Classifier();
        classifier.train(trainingDataSet);
        List<String> result = classifier.classify(testDataSet);

        assertEquals(masterJ48.length, result.size());

        for (int i = 0; i < masterJ48.length; i++) {
            assertEquals(masterJ48[i], result.get(i));
        }
    }

    private List<TrainingDataValueObject> readLearningData() {
        List<TrainingDataValueObject> result = new ArrayList<TrainingDataValueObject>();

        BufferedReader br = getFileReader("bayes/sentimentLearningTestData");

        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split("�");

                TrainingDataValueObject wrapper = new TrainingDataValueObject();
                wrapper.message = splitted[0];
                wrapper.classification = splitted[1];

                result.add(wrapper);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private BufferedReader getFileReader(String filePath) {
        Reader reader = null;
        try {
            reader = new FileReader(new File(filePath));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        BufferedReader br = new BufferedReader(reader);
        return br;
    }

    private List<String> readTestDataFile() {
        List<String> testData = new ArrayList<String>();

        BufferedReader br = getFileReader("bayes/testData");

        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                testData.add(line);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return testData;
    }

}
