package weka;

import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.tokenizers.Tokenizer;

public class BayesClassifier extends BaseClassifier {

    private Instances train = null;
    private Classifier classifier = null;
    
    @Override
    public List<String> run(List<String> testData) {
        return null;
    }

    @Override
    public void train() {
        try {
            train = readTrainingDataFromFile();
            Tokenizer tokenizer = createTokenizer();
            filter = createStringToWordVectorFilter(train, tokenizer);
            
            Instances fillteredTrainData = filterInstances(train);
            
            classifier = new NaiveBayesUpdateable();
            classifier.buildClassifier(fillteredTrainData);
            evaluateClassification(train, classifier);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> classify(List<String> testData) {
        if (classifier == null) {
            return null;
        }
        
        List<String> result = new ArrayList<String>();
        
        Instances dataset = createInstacesForClassification(testData, train);

        Attribute classAttributes = train.classAttribute();
        try {
            result = classifyDataset(classifier, dataset, classAttributes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
}
