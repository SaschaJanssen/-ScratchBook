package weka;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WekaTestTest {

    private String classificationString = "NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEGATIVE,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,POSITIVE,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEGATIVE,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,POSITIVE,POSITIVE,NEUTRAL,NEUTRAL,POSITIVE,NEUTRAL,NEUTRAL,NEUTRAL,NEUTRAL,NEGATIVE,NEUTRAL,POSITIVE,NEUTRAL";
    private String[] master;

    @Before
    public void setUp() throws Exception {
        master = classificationString.split(",");
    }

    @Test
    public void testWekaClassifierBayes() {
        List<String> testData = readTestDataFile();

        WekaTest weka = new WekaTest();

        List<String> result;
        result = weka.run(testData);
        assertEquals(master.length, result.size());

        for (int i = 0; i < master.length; i++) {
            assertEquals(master[i], result.get(i));
        }

    }

    private List<String> readTestDataFile() {
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

}
