package weka;

import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.tokenizers.Tokenizer;

public class J48Classifier extends BaseClassifier {

    private Instances train = null;
    private Classifier classifier = null;
    
    @Override
    public List<String> run(List<String> testData) {
        List<String> result = new ArrayList<String>();

        try {
            Instances train = readTrainingDataFromFile();

            Tokenizer tokenizer = createTokenizer();
            filter = createStringToWordVectorFilter(train, tokenizer);

            Instances fillteredTrainData = filterInstances(train);

            Classifier classifier = new J48();
            classifier.buildClassifier(fillteredTrainData);

            Instances dataset = createInstacesForClassification(testData, train);

            Attribute classAttributes = train.classAttribute();
            result = classifyDataset(classifier, dataset, classAttributes);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void train() {
        try {
            train = readTrainingDataFromFile();
            Tokenizer tokenizer = createTokenizer();
            filter = createStringToWordVectorFilter(train, tokenizer);
            
            Instances fillteredTrainData = filterInstances(train);
            
            classifier = new J48();
            classifier.buildClassifier(fillteredTrainData);
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
