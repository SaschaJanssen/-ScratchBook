package weka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class WekaTest {
    
    private NGramTokenizer ngt = new NGramTokenizer();
    private StringToWordVector filter = null;
    
    public List<String> run() {
        List<String> result = new ArrayList<String>();
        
        Instances train = readTrainingDataFromFile();
        Instances fillteredTrainData = filterTrainingData(train);

        Classifier bayes = new NaiveBayesUpdateable();
        try {
            bayes.buildClassifier(fillteredTrainData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        evaluateClassification(train, bayes);

        List<String> testData = readTestDataFile();
        Instances dataset = createInstacesForTest(testData, train);

        System.out.println("Testing " + dataset.numInstances() + " Instances");
        for (int i = 0; i < dataset.numInstances(); i++) {

            try {
                
                Instance instanceUnderTest = dataset.instance(i);

                // filter.setStopwords(new File("/home/u179995/english"));
                filter.setTokenizer(ngt);
                filter.input(instanceUnderTest);
                filter.setIDFTransform(true);
                filter.setLowerCaseTokens(true);
                filter.setTFTransform(true);
                Instance filteredInstance = filter.output();

                double predictedValue = bayes.classifyInstance(filteredInstance);
                Attribute classAttribute = train.classAttribute();
                String predictString = classAttribute.value((int) predictedValue) + " (" + predictedValue + ")";

                System.out.println("\n" + instanceUnderTest);
                System.out.println(predictString + " ");
                // System.out.println(classAttribute.value((int) predictedValue));
                result.add(classAttribute.value((int) predictedValue));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        
        return result;
    }

    private Instances createInstacesForTest(List<String> testData, Instances train) {
        // Defining structure for weka instance.
        Attribute string = new Attribute("text", (FastVector) null);

        FastVector attributes = new FastVector();
        attributes.addElement(string);

        Instances dataset = train.stringFreeStructure();
        Attribute attrText = dataset.attribute("text");

        for (String dataForTest : testData) {

            dataForTest = UtilLucene.standardsAnalyzer(dataForTest);
            
            double[] values = new double[dataset.numAttributes()];
            
            values[0] = attrText.addStringValue(dataForTest);

            Instance inst = new Instance(1.0, values);
            dataset.add(inst);
        }
        return dataset;
    }

    private void evaluateClassification(Instances train, Classifier bayes) {
        try {
            Evaluation evaluation = new Evaluation(train);
            evaluation.evaluateModel(bayes, train);
            System.out.println("evaluation.toMatrixString():\n" + evaluation.toMatrixString());
            System.out.println("\nevaluation.toClassDetailsString():\n" + evaluation.toClassDetailsString("Details"));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    
    private Instances filterTrainingData (Instances unfilteredTrainingData) {
        ngt.setNGramMaxSize(3);
        
        Instances fillteredTrainData = null;
        try {

            filter = new StringToWordVector();
            // filter.setStopwords(new File("/home/u179995/english"));
            filter.setTokenizer(ngt);
            filter.setIDFTransform(true);
            filter.setLowerCaseTokens(true);
            filter.setTFTransform(true);
            filter.setInputFormat(unfilteredTrainingData);
            fillteredTrainData = Filter.useFilter(unfilteredTrainingData, filter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fillteredTrainData;
    }

    private static Instances readTrainingDataFromFile() {
        Instances train = null;
        try {
            DataSource source = new DataSource("bayes/data.arff");
            train = source.getDataSet();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (train.classIndex() == -1) {
            train.setClassIndex(train.numAttributes() - 1);
        }
        return train;
    }

    private static List<String> readTestDataFile() {
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

    public static Instances filterText(Instances theseInstances) {

        NGramTokenizer ngt = new NGramTokenizer();
        ngt.setNGramMaxSize(3);

        StringToWordVector filter = null;

        Instances filtered = null;

        try {

            filter = new StringToWordVector();
            filter.setStopwords(new File("/home/u179995/english"));
            filter.setTokenizer(ngt);
            filter.setIDFTransform(true);
            filter.setLowerCaseTokens(true);
            filter.setTFTransform(true);
            filter.setInputFormat(theseInstances);
            filtered = Filter.useFilter(theseInstances, filter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return filtered;

    } // end filterText

}
