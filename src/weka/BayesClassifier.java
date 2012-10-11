package weka;

import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instances;

public class BayesClassifier extends BaseClassifier {

    @Override
    protected void buildClassifier(Instances fillteredTrainData) throws Exception {
        classifier = new NaiveBayesUpdateable();
        classifier.buildClassifier(fillteredTrainData);
        evaluateClassification(train, classifier);
    }
}
