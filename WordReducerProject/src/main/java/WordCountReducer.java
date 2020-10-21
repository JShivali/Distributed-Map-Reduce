
import generated.KVStore;
import generated.KVStoreServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WordCountReducer {


    static String outputFile="WordCountOutput";

    static ConcurrentMap<String,Integer> reducerData = new ConcurrentHashMap<>();
    static Logger log = Logger.getLogger(WordCountReducer.class);
    public static void main(String[] args) {
        String mapperNo=args[0];
        String outputfile = args[4];
        String inputFileLocation=args[5];
        outputFile=outputfile+"\\"+outputFile+mapperNo;
        String[] intermdiateKeysList = args[6].replaceAll("\\[", "").replaceAll("\\]","").split(",");

        log.debug("Reducer  "+ args[0]+" started at : "+ new Date());
        System.out.println("Reducer  "+ args[0]+" started at : "+ new Date());

        reduceInputs(inputFileLocation,intermdiateKeysList);

        // read intermediate files list. For each file find the file and add the word and count(contenrts of intermdiate_word) to map and write to output file
        log.debug("Reducer for val "+ args[0]+" finished at : "+ new Date());
        System.out.println("Reducer "+ args[0]+" finished at : "+ new Date());
        System.exit(0);
    }

    public static void reduceInputs(String inputFileLocation,String[] intermdiateKeysList) {
        // Split inputs
        ConcurrentMap<String,Integer> map = new ConcurrentHashMap<>();
        for(String intermediateKey:intermdiateKeysList){
            readFromFile(new File(inputFileLocation+"\\"+intermediateKey.trim()),intermediateKey.split("_")[1]);
        }

        writeToFile(reducerData, outputFile);

        System.out.println("The aggregated values are: " + Arrays.asList(reducerData));
        log.debug("The aggregated values are: " + Arrays.asList(map));

    }

    // Write output to file specified by use
    public static void writeToFile(ConcurrentMap<String,Integer> output, String file){
        try{
            File outputFile = new File(file);
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile.getAbsoluteFile(), true));
            output.forEach((k, v) -> {
                try {
                    bw.write(k + "-" + v + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void readFromFile(File file,String word){
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IOException("Cannot create file KVStore.txt");
                }
            }
            BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String line;
            while ((line = br.readLine()) != null) {
                String input = line;
                try {
                    reducerData.put(word, input.length());
                } catch (Exception e) {
                    System.out.println( "buffered reader exception");
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

