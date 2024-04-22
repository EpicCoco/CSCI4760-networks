import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TracerouteParser {

    public static void main(String[] args) {
        String traceFile = "sampletcpdump.txt";
        List<String> results = parseTrace(traceFile);
        for (String result : results) {
            System.out.println(result);
        } //for
    } //main

    public static List<String> parseTrace(String traceFile) {
        DecimalFormat df = new DecimalFormat("0.000");
        List<String> result = new ArrayList<>();
        int numLines = 0;
        HashMap<Integer, Double> idToTime = new HashMap<>(); 
        HashMap<String, ArrayList<Double>> ipToTime = new HashMap<>();
        HashMap<Double, Integer> timeToTTL = new HashMap<>();
        HashMap<Integer, String> TTLtoIp = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(traceFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                numLines++;
                if (line.contains("proto TCP")) {
                    String line2 = br.readLine();
                    String[] parsed = line.split(" ");
                    double time = Double.parseDouble(parsed[0]);
                    int id = Integer.parseInt(parsed[7].substring(0, parsed[7].length() - 1));
                    int TTL = Integer.parseInt(parsed[5].substring(0, parsed[5].length() - 1));
                    idToTime.put(id, time);
                    timeToTTL.put(time, TTL);
                } else if (line.contains("proto ICMP")) {
                    String line2 = br.readLine();
                    String line3 = br.readLine();
                    br.readLine();

                    String[] parsed1 = line.split(" ");
                    double time = Double.parseDouble(parsed1[0]);

                    String[] parsed2 = line2.split(" ");
                    String ip = parsed2[4];

                    String[] parsed3 = line3.split(" ");
                    int id = Integer.parseInt(parsed3[6].substring(0, parsed3[6].length() - 1));

                    double timeDiff = (time - idToTime.get(id)) * 1000;
                    if (!ipToTime.containsKey(ip)) ipToTime.put(ip, new ArrayList<>());
                    ArrayList<Double> temp = ipToTime.get(ip);
                    temp.add(timeDiff);
                    TTLtoIp.put(timeToTTL.get(idToTime.get(id)), ip);
                    
                } //if
            } //while

            //printing
            for (int i = 0; i < numLines; i++) {
                if (!TTLtoIp.containsKey(i)) continue;
                result.add("TTL " + i);
                String ip = TTLtoIp.get(i);
                result.add(ip);
                ArrayList<Double> times = ipToTime.get(ip);
                for (double time : times) {
                    result.add(df.format(time) + " ms");
                } //for
            } //for

        } catch (IOException e) {
            e.printStackTrace();
        } //try

        return result;
    } //parseTrace
} //TracerouteParser
