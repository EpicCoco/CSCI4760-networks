import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TracerouteParser {

    public static void main(String[] args) {
        String traceFile = "sampletcpdump.txt";
        List<String> results = parseTrace(traceFile);
        for (String result : results) {
            System.out.println(result);
        }
    }

    public static List<String> parseTrace(String traceFile) {
        List<String> result = new ArrayList<>();
        int currentTTL = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(traceFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                
            } //while
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
