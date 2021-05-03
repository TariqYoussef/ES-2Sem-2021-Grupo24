package util;

/**
 * <p>Generic Quadruple class created to encapsulate any 4 objects into only one</p>
 * <p>Has four paramethers each with its own getter and setter</p>
 * <p>Each Quadruple can be instantiated with any kind of class as its four parameters</p>
 *
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

//  TODO abstract Quadruple and make a child that is specific for PackageDeclaration, ClassOrInterfaceDeclaration, MethodDeclaration, Integer
    // dont know if this is a good idea
    /**
     *
     */
    @Override
    public String toString() {
        return "Quadruple{" +
                this.a.getClass().getName() + "=" + a + ", " +
                this.b.getClass().getName() + "=" + b + ", " +
                this.c.getClass().getName() + "=" + c + ", " +
                this.d.getClass().getName() + "=" + d + ", " +
                "}";
    }

/*
/**
* @param quadruples
*/
/*
    //Print function
    public static void PrintQuad() {
        for (Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, MethodDeclaration, Integer> quadruple : quadruples) {
            System.out.println("Package:" + quadruple.getA().getNameAsString() + ";Class:" + quadruple.getB().getNameAsString() + ";Method:" + quadruple.getC().getNameAsString() + ";NOM_class:" + quadruple.getD());
            System.out.println("=================");
        }
    }
    */

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
