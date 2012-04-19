package a2;

import java.util.Observer;

public interface Autofactory extends Observer {
    //Keine Public-Methoden.
    //Im update() wird geprüft, ob in diesem Step ein neues Auto erstellt werden muss und in die
    //Queue des Leitsystems eingereiht werden soll.
}
