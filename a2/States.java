package a2;

enum States {	
    IN("In"),
    OUT("Out"),
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
