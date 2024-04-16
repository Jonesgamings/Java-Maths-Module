public class Bivector
{
    private static final String wedgeProduct = "∧";
    // Wedge product of two vectors
    VectorND vector1;
    VectorND vector2;

    @Override
    public String toString() {
        return vector1 + wedgeProduct + vector2;
    }

    public Bivector(VectorND v1, VectorND v2)
    {
        this.vector1 = v1;
        this.vector2 = v2;
    }

    public Bivector(Vector3D v1, Vector3D v2)
    {
        this.vector1 = v1.toVectorND();
        this.vector2 = v2.toVectorND();
    }

    public Bivector(Vector2D v1, Vector2D v2)
    {
        this.vector1 = v1.toVectorND();
        this.vector2 = v2.toVectorND();
    }
}
