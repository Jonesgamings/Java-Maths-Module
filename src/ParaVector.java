public class ParaVector
{
    // Geometric Vector Product
    double scalar;
    BiVector bivector;

    @Override
    public String toString() {
        return scalar + " + " + this.bivector;
    }

    public ParaVector(double scalar, BiVector bivector)
    {
        this.scalar = scalar;
        this.bivector = bivector;
    }

    public ParaVector(VectorND v1, VectorND v2)
    {
        this.scalar = v1.dot(v2);
        this.bivector = v1.exteriorProduct(v2);
    }
}
