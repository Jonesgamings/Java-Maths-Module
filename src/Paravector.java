public class Paravector
{
    // Geometric Vector Product
    double scalar;
    Bivector bivector;

    @Override
    public String toString() {
        return scalar + " + " + this.bivector;
    }

    public Paravector(double scalar, Bivector bivector)
    {
        this.scalar = scalar;
        this.bivector = bivector;
    }

    public Paravector(VectorND v1, VectorND v2)
    {
        this.scalar = v1.dot(v2);
        this.bivector = v1.exteriorProduct(v2);
    }
}
