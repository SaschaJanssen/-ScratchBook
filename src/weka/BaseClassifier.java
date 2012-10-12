package weka;

import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.tokenizers.NGramTokenizer;
import weka.core.tokenizers.Tokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public abstract class BaseClassifier {

    protected Instances train = null;
    protected Classifier classifier = null;

    private StringToWordVector filter = null;

    protected abstract void buildClassifier(Instances fillteredTrainData) throws Exception;

    public void train(List<TrainingDataValueObject> trainData) {
        try {
            train = createInstancesFromDataList(trainData);
            Tokenizer tokenizer = createTokenizer();
            filter = createStringToWordVectorFilter(train, tokenizer);

            Instances fillteredTrainData = filterInstances(train);
            buildClassifier(fillteredTrainData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Instances createInstancesFromDataList(List<TrainingDataValueObject> trainData) {
        Attribute stringAttribute = new Attribute("text", (FastVector) null);

        FastVector classVal = new FastVector(3);
        classVal.addElement("POSITIVE");
        classVal.addElement("NEGATIVE");
        classVal.addElement("NEUTRAL");
        Attribute classAttribute = new Attribute("class", classVal);

        FastVector wekaAttributes = new FastVector(2);
        wekaAttributes.addElement(stringAttribute);
        wekaAttributes.addElement(classAttribute);

        Instances trainingSet = new Instances("Rel", wekaAttributes, 10);
        trainingSet.setClassIndex(1);

        for (TrainingDataValueObject trainWrapper : trainData) {
            Instance instance = new Instance(2);
            instance.setValue((Attribute) wekaAttributes.elementAt(0), trainWrapper.message);
            instance.setValue((Attribute) wekaAttributes.elementAt(1), trainWrapper.classification);

            trainingSet.add(instance);
        }

        return trainingSet;
    }

    public List<String> classify(List<String> testData) {
        if (classifier == null) {
            throw new IllegalStateException(
                    "The classifier ist not trained. Run the train method with your training data first.");
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

    private Tokenizer createTokenizer() {
        NGramTokenizer tokenizer = new NGramTokenizer();
        tokenizer.setNGramMaxSize(3);
        return tokenizer;
    }

    private StringToWordVector createStringToWordVectorFilter(Instances instancesForInputFormat, Tokenizer tokenizer)
            throws Exception {
        StringToWordVector filter = new StringToWordVector();
        // filter.setStopwords(new File("/home/u179995/english"));

        filter.setTokenizer(tokenizer);
        filter.setIDFTransform(true);
        filter.setLowerCaseTokens(true);
        filter.setTFTransform(true);
        filter.setInputFormat(instancesForInputFormat);

        return filter;
    }

    private List<String> classifyDataset(Classifier classifier, Instances datasetForClassification,
            Attribute classAttributes) throws Exception {
        List<String> result = new ArrayList<String>();

        for (int i = 0; i < datasetForClassification.numInstances(); i++) {
            Instance instanceUnderTest = filterInstance(datasetForClassification.instance(i));
            double predictedValue = classifier.classifyInstance(instanceUnderTest);
            result.add(classAttributes.value((int) predictedValue));
        }
        return result;
    }

    private Instances createInstacesForClassification(List<String> testData, Instances train) {
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

    protected void evaluateClassification(Instances train, Classifier bayes) throws Exception {
        Evaluation evaluation = new Evaluation(train);
        evaluation.evaluateModel(bayes, train);
    }

    private Instance filterInstance(Instance unfilteredData) throws Exception {
        filter.input(unfilteredData);
        return filter.output();
    }

    private Instances filterInstances(Instances unfilteredTrainingData) throws Exception {
        return Filter.useFilter(unfilteredTrainingData, filter);
    }
}
