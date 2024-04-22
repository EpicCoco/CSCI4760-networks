import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TracerouteParser {

    public static void main(String[] args) {
        String traceFile = "sampletcpdump.txt";
        List<String> results = parseTrace(traceFile);
        for (String result : results) {
            System.out.println(result);
        }
    }

    public static List<String> parseTrace(String traceFile) {
        DecimalFormat df = new DecimalFormat("#.###");
        List<String> result = new ArrayList<>();
        int currentTTL = 0;
        HashMap<Integer, Double> idToTime = new HashMap<>(); 
        HashMap<String, ArrayList<Double>> ipToTime = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(traceFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("proto TCP")) {
                    String line2 = br.readLine();
                    String[] parsed = line.split(" ");
                    double time = Double.parseDouble(parsed[0]);
                    int id = Integer.parseInt(parsed[7].substring(0, parsed[7].length() - 1));
                    idToTime.put(id, time);
                } else if (line.contains("proto ICMP")) {
                    String line2 = br.readLine();
                    String line3 = br.readLine();
                    br.readLine();

                    String[] parsed1 = line.split(" ");
                    double time = Double.parseDouble(parsed1[0]);

                    String[] parsed2 = line2.split(" ");
                    //System.out.println(Arrays.toString(parsed2));
                    String ip = parsed2[4];

                    String[] parsed3 = line3.split(" ");
                    int id = Integer.parseInt(parsed3[6].substring(0, parsed3[6].length() - 1));

                    double timeDiff = Double.parseDouble(df.format((time - idToTime.get(id)) * 1000));
                    if (!ipToTime.containsKey(ip)) ipToTime.put(ip, new ArrayList<>());
                    ArrayList<Double> temp = ipToTime.get(ip);
                    temp.add(timeDiff);
                } //if
            } //while

            System.out.println("PRINTING RESULT");
            for (Map.Entry<String, ArrayList<Double>> entry : ipToTime.entrySet()) {
                String ip = entry.getKey();
                ArrayList<Double> times = entry.getValue();
                System.out.println("TTL");
                System.out.println(ip);
                for (double time : times) {
                    System.out.println(time + " ms");
                } //for
            } //for
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
