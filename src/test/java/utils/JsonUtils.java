package utils;

import logger.Log;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonUtils {

    private static HashMap<String, String> tempHashMap = new HashMap<String, String>();

    public static void loadEnvironmentProperties(String envFileName)
    {
        try
        {
           String envFile = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("environment.filepath");
           String fileString = new String(Files.readAllBytes(Paths.get(envFile+envFileName+".json")), StandardCharsets.UTF_8);
            JSONObject parentNode = new JSONObject(fileString);
            Iterator<String> keys = parentNode.keys();

            while(keys.hasNext())
            {
              String key = keys.next();
              System.setProperty(key,parentNode.get(key).toString());
            }
        }catch(IOException e)
        {

        }
    }

    public String readJsonFile(String keyFromJson)
    {
        String valueFromJson = (System.getProperty(keyFromJson));
        if(valueFromJson == null)
        {
          valueFromJson = keyFromJson;
        }
        return valueFromJson;
    }

    public String getValueFromSerenityproperties(String propertyName)
    {
        return SystemEnvironmentVariables.createEnvironmentVariables().getProperty(propertyName);
    }

    public HashMap<String, String> listJson(JSONObject json)
    {
        JsonUtils.tempHashMap = null;
        JsonUtils.tempHashMap = new HashMap<String, String>();
        listJsonObject("",json);
        Log.info("HashMap created with key count as: "+ JsonUtils.tempHashMap.keySet().size());
        return JsonUtils.tempHashMap;
    }

    public void listJsonObject(String parent, JSONObject json)
    {
        Iterator iterator = json.keys();
        while (iterator.hasNext())
        {
            String key = (String)iterator.next();
            Object child = json.get(key);
            String childKey = parent.isEmpty() ? key : parent + "." + key;
            listObject(childKey, child);
        }
    }

    public void listObject(String parent, Object data)
    {
        if(data instanceof JSONObject)
        {
           listJsonObject(parent, (JSONObject) data);
        }else if(data instanceof JSONArray)
        {
           listJsonArray(parent, (JSONArray) data);
        }else
        {
            listPrimitive(parent, data);
        }
    }

    public void listJsonArray(String parent, JSONArray json)
    {
        for(int i = 0; i < json.length(); i++)
        {
          Object data = json.get(i);
          listObject(parent + "[" + i + "]", data);
        }
    }

    public void listPrimitive(String parent, Object obj)
    {
        JsonUtils.tempHashMap.put(parent, String.valueOf(obj));
    }

    public boolean compareHashMaps(Map<String, String> hashMap1, Map<String, String> hashMap2, String stringsToBeCompared)
    {
       boolean flag = false;
       if(hashMap1.toString().trim().equals(hashMap2.toString().trim()) && stringsToBeCompared.equals(""))
       {
           flag = true;
           Log.info("Expected Json \n" + hashMap1.toString());
           Log.info("Actual Json \n" + hashMap2.toString());
       }
       else if(hashMap1.toString().trim() != (hashMap2.toString().trim()) && stringsToBeCompared.equals(""))
       {
           flag = false;
           Log.info("Expected Json \n" + hashMap1.toString());
           Log.info("Actual Json \n" + hashMap2.toString());
       }
       else
       {
           flag = true;
           if(stringsToBeCompared.equals(""))
           {
             Log.info("Strings to be compared is null");
           }else
           {
               if(hashMap1.size() != hashMap2.size())
               {
                   Log.error("HashMap size does not match: " + hashMap1.size() + " " + hashMap2.size());
               }else
               {
                   Log.info("Number of keys matched: " + hashMap1.size() + " " + hashMap2.size());
                   for(String key : hashMap1.keySet())
                   {
                       if(!hashMap1.get(key).equals(hashMap2.get(key)) && !stringsToBeCompared.contains(key))
                       {
                          flag = false;
                          Log.error("Values for key: " + key + "does not match");
                       }else if(hashMap1.get(key).equals(hashMap2.get(key)))
                       {
                         Log.info("Matching for key: " + key);
                       }else
                       {
                           Log.info("Not compared for key: " + key);
                       }
                   }
               }
           }
       }
       if(flag == false)
       {
           return false;
       }else
       {
           return true;
       }
    }

}
