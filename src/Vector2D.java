public class Vector2D
{
    double x;
    double y;

    public Vector2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double magnitude()
    {
        return Math.pow(Math.pow(this.x, 2) + Math.pow(this.y, 2), 0.5);
    }

    public double angle(Vector2D vector)
    {
        return Math.atan2(this.x - vector.x, this.y - vector.y);
    }

    public Vector2D add(Vector2D vector)
    {
        return new Vector2D(this.x + vector.x, this.y + vector.y);
    }

    public Vector2D subtract(Vector2D vector)
    {
        return new Vector2D(this.x - vector.x, this.y - vector.y);
    }

    public double dot(Vector2D vector)
    {
        return this.x * vector.x + this.y * vector.y;
    }

    public Vector2D multiply(double factor)
    {
        return new Vector2D(this.x*factor, this.y*factor);
    }

    public Vector2D divide(double factor)
    {
        return new Vector2D(this.x/factor, this.y/factor);
    }

    public Matrix toMatrix()
    {
        return new Matrix(2, 1).setMatrix(new double[][] {{this.x}, {this.y}});
    }
}
