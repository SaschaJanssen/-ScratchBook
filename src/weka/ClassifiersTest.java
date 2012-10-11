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

    @Before
    public void setUp() throws Exception {
        masterJ48 = j48ClassificationString.split(",");
        masterBayes = bayesClassificationString.split(",");
    }

    @Test
    public void testWekaClassifierBayes() throws Exception {
        List<TrainWrapper> trainingData = readLearningData();

        List<String> testData = readTestDataFile();

        BaseClassifier classifier = new BayesClassifier();
        classifier.train(trainingData);
        List<String> result = classifier.classify(testData);

        // result = classifier.run(testData);
        assertEquals(masterBayes.length, result.size());

        for (int i = 0; i < masterBayes.length; i++) {
            assertEquals(masterBayes[i], result.get(i));
        }
    }

    @Test
    public void testWekaClassifierJ48() {
        List<TrainWrapper> trainingData = readLearningData();

        List<String> testData = readTestDataFile();

        BaseClassifier classifier = new J48Classifier();
        classifier.train(trainingData);
        List<String> result = classifier.classify(testData);

        assertEquals(masterJ48.length, result.size());

        for (int i = 0; i < masterJ48.length; i++) {
            assertEquals(masterJ48[i], result.get(i));
        }
    }

    private List<TrainWrapper> readLearningData() {
        List<TrainWrapper> result = new ArrayList<TrainWrapper>();

        Reader reader = null;
        try {
            reader = new FileReader(new File("bayes/sentimentLearningTestData"));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        BufferedReader br = new BufferedReader(reader);

        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split("�");

                TrainWrapper wrapper = new TrainWrapper();
                wrapper.message = splitted[0];
                wrapper.classification = splitted[1];

                result.add(wrapper);
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return result;
    }

    private List<String> readTestDataFile() {
        List<String> testData = new ArrayList<String>();

        Reader reader = null;
        try {
            reader = new FileReader(new File("bayes/testData"));
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        BufferedReader br = new BufferedReader(reader);

        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                testData.add(line);
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return testData;
    }

}
