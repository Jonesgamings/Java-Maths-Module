public class Vector2D {
    double x;
    double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "{" + x + " , " + y + "}";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {return false;}

        if (obj.getClass() != this.getClass()) {return false;}

        final Vector2D other = (Vector2D) obj;
        if (this.x != other.x) { return false;}
        return  (this.y == other.y);
    }

    public Vector2D midpoint(Vector2D point) {
        double newX = (this.x + point.x) / 2;
        double newY = (this.y + point.y) / 2;
        return new Vector2D(newX, newY);
    }

    public Vector2D project(Vector2D a)
    {
        return this.multiply((this.dot(a) / this.dot(this)));
    }

    public Vector2D reject(Vector2D a)
    {
        return a.subtract(this.project(a));
    }

    public static Vector2D projection(Vector2D a, Vector2D b)
    {
        return b.multiply((b.dot(a) / b.dot(b)));
    }

    public static Vector2D rejection(Vector2D a, Vector2D b)
    {
        return a.subtract(Vector2D.projection(a, b));
    }

    public Vector2D normalise()
    {
        return this.divide(this.magnitude());
    }

    public double magnitude() {
        return Math.pow(Math.pow(this.x, 2) + Math.pow(this.y, 2), 0.5);
    }

    public double angle() {
        return Math.atan2(this.y, this.x);
    }

    public double angle(Vector2D vector) {
        return Math.atan2(this.y - vector.y, this.x - vector.x);
    }

    public Vector2D add(Vector2D vector) {
        return new Vector2D(this.x + vector.x, this.y + vector.y);
    }

    public Vector2D subtract(Vector2D vector) {
        return new Vector2D(this.x - vector.x, this.y - vector.y);
    }

    public double dot(Vector2D vector) {
        return this.x * vector.x + this.y * vector.y;
    }

    public Vector2D multiply(double factor) {
        return new Vector2D(this.x * factor, this.y * factor);
    }

    public Vector2D divide(double factor) {
        return new Vector2D(this.x / factor, this.y / factor);
    }

    public Vector2D inverse()
    {
        return this.divide(Math.pow(this.magnitude(),2));
    }

    public Matrix outerProduct(Vector2D v)
    {
        return this.toMatrix().multiply(v.toMatrix().transpose());
    }

    public BiVector wedgeProduct(Vector2D vector)
    {
        return new BiVector(this, vector);
    }

    public BiVector exteriorProduct(Vector2D vector)
    {
        return new BiVector(this, vector);
    }

    public ParaVector geometricProduct(Vector2D vector)
    {
        return new ParaVector(this.toVectorND(), vector.toVectorND());
    }

    public Vector2D hadamardProduct(Vector2D v)
    {
        return new Vector2D(x * v.x, y * v.y);
    }

    public static Vector2D xAxis()
    {
        return new Vector2D(1, 0);
    }

    public static Vector2D YAxis()
    {
        return new Vector2D(0, 1);
    }
    public VectorND toVectorND()
    {
        return new VectorND(new double[] {this.x, this.y});
    }

    public Matrix toMatrix() {
        return new Matrix(2, 1).setMatrix(new double[][]{{this.x}, {this.y}});
    }

    public Polar2D toPolar()
    {
        return new Polar2D(this.angle(), this.magnitude());
    }

    public ComplexNumber toComplex()
    {
        return new ComplexNumber(x, y);
    }

    public static void main(String[] args) {
        Vector2D v1 = new Vector2D(1, 1);
        Vector2D v2 = new Vector2D(2, 2);
        System.out.println(v1.geometricProduct(v2));
    }
}
