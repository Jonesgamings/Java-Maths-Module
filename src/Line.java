public class Line
{
    Vector3D direction;
    Vector3D point;

    public Line(Vector3D direction, Vector3D point)
    {
        this.direction = direction;
        this.point = point;
    }

    public Vector3D position(double t)
    {
        return point.add(direction.multiply(t));
    }
    public Vector3D intersection(Plane plane)
    {
        if (plane.normal.dot(this.direction) != 0) {
            double t = (plane.scalar_product - this.point.dot(plane.normal)) / (plane.normal.dot(this.direction));
            return this.position(t);
        }
        return null;
    }

}
