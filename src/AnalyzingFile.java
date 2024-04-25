import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class AnalyzingFile {
    public static void main(String[] args) {

        BufferedReader reader = null;
        Map<Integer, String> outputs = new HashMap<>();
        int counter = 1;

        try {
            reader = new BufferedReader(new FileReader("src/tester.txt"));
            String currentLine;

            while((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split("\\$\\s+");
                if (parts.length > 1) {
                    outputs.put(counter++, parts[parts.length - 1]);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    
}
