package util;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * @param <A> First object of the quadruple
 * @param <B> Second object of the quadruple
 * @param <C> Third object of the quadruple
 * @param <D> Forth object of the quadruple
 */
public class Quadruple <A, B, C, D>{
    private A a;
    private B b;
    private C c;
    private D d;

    /**
     * @param a First object of the quadruple
     * @param b Second object of the quadruple
     * @param c Third object of the quadruple
     * @param d Forth object of the quadruple
     */
    public Quadruple(A a, B b, C c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * @return return the first object of the quadruple
     */
    public A getA() {
        return a;
    }

    /**
     * @param a param to set the first object of the quadruple
     */
    public void setA(A a) {
        this.a = a;
    }

    /**
     * @return return the second object of the quadruple
     */
    public B getB() {
        return b;
    }

    /**
     * @param b param to set the second object of the quadruple
     */
    public void setB(B b) {
        this.b = b;
    }

    /**
     * @return return the third object of the quadruple
     */
    public C getC() {
        return c;
    }

    /**
     * @param c param to set the third object of the quadruple
     */
    public void setC(C c) {
        this.c = c;
    }

    /**
     * @return return the forth object of the quadruple
     */
    public D getD() {
        return d;
    }

    /**
     * @param d param to set the forth object of the quadruple
     */
    public void setD(D d) {
        this.d = d;
    }
}
