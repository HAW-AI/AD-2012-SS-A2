
package a2;

import java.util.Map;

/**
 * Da Java keine public Implementierung von Entry anbietet haben wir diese selbst
 * implementiert, da wir ein Paar-Objekt für die Baustellendurchfahrt(constructionRoad) benötigten
 */
public class Pair<A, B> implements Map.Entry<A,B> {
    private A a;
    private B b;
    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }
    
    @Override
    public A getKey() {
        return a;
    }

    @Override
    public B getValue() {
        return b;
    }

    @Override
    public B setValue(B value) {
        b = value;
        return b;
    }
    
}
