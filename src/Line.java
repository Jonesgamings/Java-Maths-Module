public class Line
{
    Vector3D direction;
    Vector3D point;

    @Override
    public String toString(){
        return "(" + point.toString() + ") + " + "(" + direction.toString() + ")t";
    }

    public Line(Vector3D direction, Vector3D point)
    {
        this.direction = direction;
        this.point = point;
    }

    public double distance(Plane plane)
    {
        return plane.distance(point);
    }

    public double distance(Line line)
    {
        if (line.direction.cross(this.direction).equals(new Vector3D(0, 0, 0)))
        {
            Vector3D AP = line.point.subtract(this.point);
            return this.direction.cross(AP).magnitude() / this.direction.magnitude();
        }
        else
        {
            Vector3D cross = this.direction.cross(line.direction);
            return (cross.dot(line.point.subtract(this.point))) / cross.magnitude();
        }
    }

    public double distance (Vector3D point)
    {
        Vector3D AP = point.subtract(this.point);
        return AP.cross(this.direction).magnitude() / this.direction.magnitude();
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
