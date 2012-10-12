package weka;

import java.util.List;

import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instances;

public class BayesClassifier extends BaseClassifier {

    public BayesClassifier(List<TrainingDataValueObject> trainingDataSet) {
        super(trainingDataSet);
    }

    @Override
    protected void buildClassifier(Instances fillteredTrainData) throws Exception {
        classifier = new NaiveBayesUpdateable();
        classifier.buildClassifier(fillteredTrainData);
        evaluateClassification(train, classifier);
    }
}
