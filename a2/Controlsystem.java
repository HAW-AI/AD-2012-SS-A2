package a2;

public interface Controlsystem {
    public void addToExitQueue(Car auto);
    public void addToEntryQueue(Car auto);
    public void manageTraffic(); //Kümmert sich um die Schaltung der Ampeln
}
/*
 * KONSTANTE: apl = Ampelphasenlaenge, swmin = Schwellwertmin, swmax = Schwellwertmax
 * Vorbedingungen: Ampelstate, Eingangqueue, Ausgangqueue, Parkplatzbelegung
 * callAlgorithm()
 * if parkplatz voll -> setGreen(OUT,apl/2) ??
 * if parkplatz leer -> setGreen(IN,apl/2) ??
 * if inqueue.size == 0 AND outqueue.size == 0 -> setGreen(NONE,5)
 * if inqueue.size == 0 -> setGreen(OUT,apl/2) ??
 * if outqueue.size == 0 -> setGreen(IN,apl/2) ??
 * (if outqueue.size >> inqueue.size OR inqueue.size >> outqueue.size -> die benachteiligte seite nicht fuer immer warten lassen)
 * inCount = Math.min(inqueue.size,150-(outqueue.size+parkplatzbelegung))
 * 
 * zu beachten ist der State der Ampel ... beispiel:
 * [60|20] (g)<->(r) [20] (130)
 * tb+(20 Cars out)+tb+(40 Cars in) oder (20 Cars in)+tb+(20 Cars out)+tb+(20 Cars in)
 * 
 * [60|20] (r)<->(g) [20] (130)
 * (20 Cars out)+tb+(40 Cars in) oder tb+(20 Cars in)+tb+(20 Cars out)+tb+(20 Cars in)
 * 
 * FAKTOREN: Parkplatzbelegung+outqueue.size <-> inqueue.size, inqueue.size <-> outqueue.size, 
 * 
 * if as.state == IN 
 * 
 * 
 * 
 * 
 * 
 * falls beide Ampelphasen berechnet werden
 * int out = (outqueue.size/((inqueue.size+outqueue.size)/100.0))/100;
 * int in = (100.0 - out)/100;
 * if out < in -> outtime = Math.max(apl*out,swmin) AND intime = Math.min(apl*in,swmax)
 * else		   -> outtime = Math.min(apl*out,swmax) AND intime = Math.max(apl*in,swmin)
 * 
 */
