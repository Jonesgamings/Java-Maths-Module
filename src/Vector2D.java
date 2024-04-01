public class Vector2D {
    double x;
    double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
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

    public Matrix toMatrix() {
        return new Matrix(2, 1).setMatrix(new double[][]{{this.x}, {this.y}});
    }

    public Polar2D toPolar()
    {
        return new Polar2D(this.angle(), this.magnitude());
    }
}
