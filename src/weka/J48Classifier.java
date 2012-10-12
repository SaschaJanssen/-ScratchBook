package weka;

import java.util.List;

import weka.classifiers.trees.J48;
import weka.core.Instances;

public class J48Classifier extends BaseClassifier {

    public J48Classifier(List<TrainingDataValueObject> trainingDataSet) {
        super(trainingDataSet);
    }

    @Override
    protected void buildClassifier(Instances fillteredTrainData) throws Exception {
        classifier = new J48();
        classifier.buildClassifier(fillteredTrainData);
    }

}
