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
 * Eingabeformat: [m|s]a1,[m|s]a2,[m|s]e1,[m|s]e2,[m|s]tb,[m|s]maxDuration
 * returns:
 * int[a1, a2, e1, e2, t, maxDuration] if input ok
 * else returns:
 * standard values: [60, 300, 600, 1200, 60, 120]
 */

public class IOManager {
    private final File logFile;
    private final String filePath;
    
    /**
     * Erstellt den IOManager zur Ausgabe ins Logfile und auf die Konsole
     * Erzeugt die Ausgabedatei
     * @param filePath der Pfad zur Eingabedatei relativ zum src-Verzeichnis
     */
    public IOManager(String filePath) {
    	this.filePath = filePath;
        Timestamp tstamp = new Timestamp(System.currentTimeMillis());
        logFile = new File("SimResult_" + tstamp.toString().replace(":", "-") + ".txt");
    }

    /**
     * Liest die Einstellungen aus der im Konstruktor übergebenen Eingabedatei und 
     * @return int[6]-Array, welches die einzelnen Werte als Sekunden enthält
     */
    int[] readSimFile() {
        int[] inputVars = new int[6];
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
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

    /**
     * Schreibt den String ins Logfile
     * @param logString der String, der ins Logfile geschrieben werden soll
     */
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

    /**
     * Gibt einen String auf der Konsole aus
     * @param logString der String, der auf der Konsole ausgegeben werden soll
     */
    public void logToConsole(String logString) {
        System.out.println(logString);
    }

    /**
     * Prüft das aus der Datei ausgelesene Array auf korrekte Werte
     * Diese Prüfung wird on readSimFile() aufgerufen
     * @param ary das in readSimFile() erzeugte int[]-Array
     * @return true, falls das Array gültig ist, ansonsten false
     */
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