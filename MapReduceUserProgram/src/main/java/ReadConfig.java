import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadConfig {
    InputStream inputStream;

    public ConfigParams getPropValues() throws IOException {

        ConfigParams params= new ConfigParams();
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property value and set it to ConfigParams object
            params.setkVStoreIp(prop.getProperty("kVStoreIPAddress"));
            params.setkVStorePort(Integer.parseInt(prop.getProperty("kvstorePort")));
            params.setFileLocation(prop.getProperty("fileLocation"));
            params.setMapperCount(Integer.parseInt(prop.getProperty("mapperCount")));
            params.setReducerCount(Integer.parseInt(prop.getProperty("reducerCount")));
            params.setMasterIP(prop.getProperty("masterIPAddress"));
            params.setMasterPort(Integer.parseInt(prop.getProperty("masterPort")));
            params.setMapperFunction(prop.getProperty("mapperFunction"));
            params.setReducerFunction(prop.getProperty("reducerFunction"));
            params.setOutputFile(prop.getProperty("outputFile"));

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return params;
    }

}
