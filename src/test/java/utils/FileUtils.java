package utils;

import logger.Log;

import java.io.*;
import java.nio.charset.Charset;

public class FileUtils {

      public String readFileFromLocation(String filePath)
      {
          String line = "";
          String st, str = "";
          BufferedReader reader;
          try {
              reader = new BufferedReader(new FileReader(filePath));
              while ((st = reader.readLine()) != null)
              {
                  str = new String(st.getBytes(), Charset.forName("UTF-8"));
                  line = line + str;
              }
          }catch (IOException e)
          {
              Log.error("Error occurred while reading content from file: "+e.getMessage());
          }
          return line;
      }

}
