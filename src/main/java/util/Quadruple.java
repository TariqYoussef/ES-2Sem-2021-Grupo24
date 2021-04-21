package util;

/**
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 */
public class Quadruple <A, B, C, D>{
    private A a;
    private B b;
    private C c;
    private D d;

    /**
     * @param a
     * @param b
     * @param c
     * @param d
     */
    public Quadruple(A a, B b, C c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * @return
     */
    public A getA() {
        return a;
    }

    /**
     * @param a
     */
    public void setA(A a) {
        this.a = a;
    }

    /**
     * @return
     */
    public B getB() {
        return b;
    }

    /**
     * @param b
     */
    public void setB(B b) {
        this.b = b;
    }

    /**
     * @return
     */
    public C getC() {
        return c;
    }

    /**
     * @param c
     */
    public void setC(C c) {
        this.c = c;
    }

    /**
     * @return
     */
    public D getD() {
        return d;
    }

    /**
     * @param d
     */
    public void setD(D d) {
        this.d = d;
    }
}
