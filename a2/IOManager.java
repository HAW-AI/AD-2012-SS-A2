package a2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;

/**
 * returns:
 * int[a1, a2, e1, e2, t] if input ok
 * else returns:
 * int[]
 */

class IOManager {
    private final File logFile;
    public IOManager() {
        Timestamp tstamp = new Timestamp(System.currentTimeMillis());
        logFile = new File("SimResult_" + tstamp.toString().replace(":", "-") + ".txt");
    }

    public int[] readSimFile(String path) {
        int[] inputVars = new int[6];
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            try {
                String[] sAry = input.readLine().split(",");
                for (int i = 0; i < sAry.length; i++) {
                    if (sAry[i].startsWith("m"))
                        inputVars[i] = Integer.valueOf(sAry[i].substring(1)) * 60;
                    else
                        inputVars[i] = Integer.valueOf(sAry[i].substring(1));
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
        if(!testArray(inputVars))
            return new int[] { 60, 300, 600, 1200, 60, 120 };
        return inputVars;
    }

    public void logToFile(String logString) {
        try {
            // new FileWriter(file ,true) - falls die Datei bereits existiert
            // werden die Bytes an das Ende der Datei geschrieben
            FileWriter writer = new FileWriter(logFile, true);
            // Text wird in den Stream geschrieben
            writer.write(logString);
            // Platformunabh�ngiger Zeilenumbruch wird in den Stream geschrieben
            writer.write(System.getProperty("line.separator"));
            // Schreibt den Stream in die Datei
            // Sollte immer am Ende ausgef�hrt werden, sodass der Stream
            // leer ist und alles in der Datei steht.
            writer.flush();
            // Schlie�t den Stream
            writer.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    public void logToConsole(String logString) {
        System.out.println(logString);
    }

    private boolean testArray(int[] ary) {
        int[] aryCopy = ary.clone();
        // 1. Auf CLOSETIME prüfen und positive Werte
        for (int elem : aryCopy) {
            if (elem < 0 || elem >= Timer.CLOSETIME)
                    return false;
        }
        // 2. Intervalle prüfen
        if (aryCopy[0] >= aryCopy[1] || aryCopy[2] >= aryCopy[3] || aryCopy[4] == 0 || aryCopy[5] < 1)
            return false;
        return true;
    }
}