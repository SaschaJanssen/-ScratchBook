package weka;

import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instances;
import weka.core.tokenizers.Tokenizer;

public class BayesClassifier extends BaseClassifier {

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
}
