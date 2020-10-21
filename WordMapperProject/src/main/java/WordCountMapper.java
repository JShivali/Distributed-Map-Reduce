import generated.KVStore;
import generated.KVStoreServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WordCountMapper {

    private static final String intermediateKeyIdentifier = "Intermediate_";
    private static final String reducerKeyIdentifier = "R_";
    static Logger log = Logger.getLogger(WordCountMapper.class);



    public static void main(String[] args) {

        String mapperNo= args[0];
        String wordsList= args[5];
        String outputfile=args[4];
        String inputFileLocation= args[6];

        log.debug("Mapper for  "+ mapperNo +" started at : "+ new Date());
        System.out.println("Mapper for  "+ mapperNo +" started at : "+ new Date());
        String[] wordlist=wordsList.replaceAll("\\[", "").replaceAll("\\]", "").toLowerCase().split(",");
            for(String word: wordlist) {
                System.out.println(inputFileLocation+"\\"+"Intermediate_"+word.split("_")[1].trim());
                writeToFile(inputFileLocation+"\\"+"Intermediate_"+word.split("_")[1].trim());
            }
        log.debug("Mapper for "+ mapperNo +" finished at : "+ new Date());
        System.out.println("Mapper for "+ mapperNo +" finished at : "+ new Date());
        System.exit(0);
    }

    public static void writeToFile( String file){
        try{

            // create files as intermediate_word and emit 1 appending in it
            File outputFile = new File(file);
            if (!outputFile.exists()) {
                if (!outputFile.createNewFile()) {
                    throw new IOException("Cannot create file KVStore.txt");
                }
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile.getAbsoluteFile(), true));

                try {
                    bw.write("1");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
