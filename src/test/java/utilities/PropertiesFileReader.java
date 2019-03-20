package utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFileReader {

    public Properties getProperty() throws FileNotFoundException, IOException {

        Properties properties = new Properties();

        properties.load(new FileInputStream("Properties/configFile.properties"));

        return properties;

    }

}
