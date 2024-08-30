package logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

    private static Logger logger = Logger.getLogger("Logger");

    public static void info(String message)
    {
        Log.logger.log(Level.INFO,message);
    }

    public static void error(String message)
    {
        Log.logger.log(Level.SEVERE,message);
    }
}
