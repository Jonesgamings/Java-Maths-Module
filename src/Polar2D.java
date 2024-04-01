public class Polar2D
{
    double angle;
    double distance;

    public Polar2D(double angle, double distance)
    {
        this.angle = angle;
        this.distance = distance;
    }

    public Vector2D toVector()
    {
        return new Vector2D(Math.cos(this.angle) * distance, Math.sin(this.angle) * distance);
    }
}
