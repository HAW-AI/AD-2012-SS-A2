package a2;

/**
 * Dieses Enum repräsentiert aktuell erlaubte Fahrtrichtung
 * IN: Einfahrende Autos haben grün
 * OUT: Ausfahrende Autos haben grün
 * NONE: Beide Ampeln sind auf rot
 */
enum States {	
    IN("In  "), //Leerzeichen für die Ausrichtung in der Ausgabe
    OUT("Out "),
    NONE("None");
    String name;
    
    private States(String name) {
        this.name = name; 
    }
    
    @Override
    public String toString() {
        return name;
    }
}
