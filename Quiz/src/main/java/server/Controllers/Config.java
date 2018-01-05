package server.Controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.utility.Log;

import java.io.*;

public final class Config {

    private static String DATABASE_HOST;
    private static Integer DATABASE_PORT;
    private static String DATABASE_USERNAME;
    private static String DATABASE_PASSWORD;
    private static String DATABASE_NAME;
    private static boolean ENCRYPTION;
    private static Log log = new Log();



    public void initConfig() throws IOException {

    //Initialize variables
    JsonObject json = new JsonObject();
    JsonParser parser = new JsonParser();

    log.writeLog(this.getClass().getName(), this, "We are now including the config file", 0);

    //Reference to config.json file from resources
    InputStream input = this.getClass().getResourceAsStream("/config.json");
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));

    //Initialise string variables
    StringBuffer stringBuffer = new StringBuffer();
    String str = "";

    //concatenating multiple strings 
    while((str = reader.readLine()) != null) {
        stringBuffer.append(str);
    }

    //convert json to variables
    json = (JsonObject) parser.parse(stringBuffer.toString());

    //initialize class variables 
    DATABASE_HOST = json.get("DATABASE_HOST").toString().replace("\"", "");
    DATABASE_PORT = Integer.parseInt(json.get("DATABASE_PORT").toString().replace("\"", ""));
    DATABASE_USERNAME = json.get("DATABASE_USERNAME").toString().replace("\"", "");
    DATABASE_PASSWORD = json.get("DATABASE_PASSWORD").toString().replace("\"", "");
    DATABASE_NAME = json.get("DATABASE_NAME").toString().replace("\"", "");
    ENCRYPTION = json.get("ENCRYPTION").getAsBoolean();

}

    public static String getDatabaseHost() {
        return DATABASE_HOST;
    }

    public static Integer getDatabasePort() {
      return DATABASE_PORT;
    }

    public static String getDatabaseUsername() {
     return DATABASE_USERNAME;
    }

    public static String getDatabasePassword() {
     return DATABASE_PASSWORD;
    }

    public static String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static Boolean getEncryption() {return ENCRYPTION; }
}
