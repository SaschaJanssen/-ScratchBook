package weka;

import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.tokenizers.Tokenizer;

public class J48Classifier extends BaseClassifier {

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

}
