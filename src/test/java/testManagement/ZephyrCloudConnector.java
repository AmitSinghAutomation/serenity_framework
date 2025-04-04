package testManagement;

import net.thucydides.core.util.SystemEnvironmentVariables;

public class ZephyrCloudConnector {

    static String zephyrBaseUrl = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("zephyrBaseUrl");
    static String zephyrAccessKey = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("zapiAccessKey");
    static String zephyrSecretKey = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("zapiSecretKey");
    static String accountId = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("accountId");
    static String testProjectId = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("zephyr.projectId");
    static String testVersionId = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("zephyr.versionId");
    static String testCycleName = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("zephyr.cycleName");
    static String testFolderName = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("zephyr.folderName");

}
