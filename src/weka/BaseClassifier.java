package weka;

import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.tokenizers.NGramTokenizer;
import weka.core.tokenizers.Tokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public abstract class BaseClassifier {

    protected StringToWordVector filter = null;
    
    public abstract List<String> run(List<String> testData);
    public abstract void train();
    public abstract List<String> classify(List<String> testData);
    
    protected Tokenizer createTokenizer() {
        NGramTokenizer tokenizer = new NGramTokenizer();
        tokenizer.setNGramMaxSize(3);
        return tokenizer;
    }

    protected StringToWordVector createStringToWordVectorFilter(Instances instancesForInputFormat, Tokenizer tokenizer)
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

    protected List<String> classifyDataset(Classifier classifier, Instances datasetForClassification,
            Attribute classAttributes) throws Exception {
        List<String> result = new ArrayList<String>();

        for (int i = 0; i < datasetForClassification.numInstances(); i++) {
            Instance instanceUnderTest = filterInstance(datasetForClassification.instance(i));

            double predictedValue = classifier.classifyInstance(instanceUnderTest);

            // TODO
            // String predictString = classAttributes.value((int)
            // predictedValue) + " (" + predictedValue + ")";
            // System.out.println("\n" + datasetForClassification.instance(i));
            // System.out.println(predictString + " ");

            System.out.println(classAttributes.value((int) predictedValue));
            result.add(classAttributes.value((int) predictedValue));
        }
        return result;
    }

    protected Instances createInstacesForClassification(List<String> testData, Instances train) {
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

    protected void evaluateClassification(Instances train, Classifier bayes) throws Exception {
        Evaluation evaluation = new Evaluation(train);
        evaluation.evaluateModel(bayes, train);
    }

    private Instance filterInstance(Instance unfilteredData) throws Exception {
        filter.input(unfilteredData);
        return filter.output();
    }

    protected Instances filterInstances(Instances unfilteredTrainingData) throws Exception {
        return Filter.useFilter(unfilteredTrainingData, filter);
    }

    protected Instances readTrainingDataFromFile() throws Exception {
        DataSource source = new DataSource("bayes/data.arff");
        Instances train = source.getDataSet();

        if (train.classIndex() == -1) {
            train.setClassIndex(train.numAttributes() - 1);
        }
        return train;
    }
}