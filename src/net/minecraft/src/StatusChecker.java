package net.minecraft.src;

import argo.jdom.*;
import argo.saj.InvalidSyntaxException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

// Class to check and periodically update the status of the ActionMC services
public class StatusChecker {
    public static HashMap<String, Integer> statusList = new HashMap<String, Integer>();
    public static HashMap<String, Integer> shortStatusList = new HashMap<String, Integer>();
    public static String[] prettyStatusNames = {
        "Online",
        "Undergoing Maintenance",
        "Experiencing Issues",
        "Offline",
        "Unknown"
    };
    public static String[] prettyStatusColors = {
        "\u00a7a", "\u00a76✕", "\u00a76", "\u00a74", "\u00a77"
    };
    public static String[] prettyStatusSymbols = {
        "✔", "✕", "✕", "✕", "?"
    };
    public static HashMap<String, String> shortServiceNames = new HashMap<String, String>();

    static {
        // Shortened names for services
        shortServiceNames.put("actionmc.ml", "Site");
        shortServiceNames.put("id.actionmc.ml", "ID");
        shortServiceNames.put("skins.actionmc.ml", "Skins");
        shortServiceNames.put("ppc.actionmc.ml", "Server");
        refreshStatus();
    }

    // Returns the status of a service in a human readable format
    public static String getPrettyStatus(String serviceName) {
        if (statusList.containsKey(serviceName)) {
            int status = statusList.get(serviceName) - 1;
            return prettyStatusSymbols[status] + " " + prettyStatusNames[status];
        } else if (shortStatusList.containsKey(serviceName)) {
            int status = shortStatusList.get(serviceName) - 1;
            return prettyStatusSymbols[status] + " " + prettyStatusNames[status];
        } else {
            return prettyStatusSymbols[4] + " " + prettyStatusNames[4];
        }
    }

    // Returns the status of a service in a prettily formatted string wrapped up in a bow
    public static String getColoredPrettyStatus(String serviceName) {
        if (statusList.containsKey(serviceName)) {
            int status = statusList.get(serviceName) - 1;
            return prettyStatusColors[status] + prettyStatusSymbols[status] + " " + prettyStatusNames[status];
        } else if (shortStatusList.containsKey(serviceName)) {
            int status = shortStatusList.get(serviceName) - 1;
            return prettyStatusColors[status] + prettyStatusSymbols[status] + " " + prettyStatusNames[status];
        } else {
            return prettyStatusColors[4] + prettyStatusSymbols[4] + " " + prettyStatusNames[4];
        }
    }

    // Returns just the color formatting for a status
    public static String getStatusColor(String serviceName) {
        if (statusList.containsKey(serviceName)) {
            return prettyStatusColors[statusList.get(serviceName) - 1];
        } else if (shortStatusList.containsKey(serviceName)) {
            return prettyStatusColors[shortStatusList.get(serviceName) - 1];
        } else {
            return prettyStatusColors[4];
        }
    }

    // Returns just the color formatting for a status
    public static String getStatusSymbol(String serviceName) {
        if (statusList.containsKey(serviceName)) {
            return prettyStatusSymbols[statusList.get(serviceName) - 1];
        } else if (shortStatusList.containsKey(serviceName)) {
            return prettyStatusSymbols[shortStatusList.get(serviceName) - 1];
        } else {
            return prettyStatusSymbols[4];
        }
    }

    // Gets all existing services
    public static Set<String> getServices() {
        return statusList.keySet();
    }

    // Gets a shortened version of all existing services
    public static Set<String> getShortServices() {
        return shortStatusList.keySet();
    }

    // Retrieves current service statuses from server
    public static void refreshStatus() {
        (new Thread() {
            public void run() {
                while (true) {
                    try {
                        System.out.println("Updating ActionMC service statuses...");
                        JsonRootNode jsonRootNode = (new JdomParser()).parse(getText("https://actionmc.ml/api/status"));
                        List list = jsonRootNode.getElements();

                        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                            JsonNode jsonNode = (JsonNode) iterator.next();

                            String serviceName = jsonNode.getStringValue(new Object[] { "serviceName" });
                            String serviceIP = jsonNode.getStringValue(new Object[] { "serviceIP" });
                            int rawStateID = Integer.parseInt(jsonNode.getStringValue(new Object[] { "rawStateID" }));
                            // Argo should have JsonNode.getNumberValue() but I guess Mojang gimped the shit out of it so yay it's a string now
                            
                            statusList.put(serviceName, rawStateID);
                            if (shortServiceNames.containsKey(serviceIP))
                                shortStatusList.put(shortServiceNames.get(serviceIP), rawStateID);
                        }
                        System.out.println("Done!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    try {
                        Thread.sleep(60 * 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private static String getText(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        connection.setRequestProperty("User-Agent", "ActionMC JAR Mod/0.0.1");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            response.append(inputLine);

        in.close();

        return response.toString();
    }
}