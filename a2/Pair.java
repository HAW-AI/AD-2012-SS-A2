
package a2;

import java.util.Map;

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
