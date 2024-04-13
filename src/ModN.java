public class ModN
{
    double value;
    double mod;
    double modValue;

    public ModN(double value, double mod)
    {
        this.value = value;
        this.mod = mod;
        this.modValue = value % this.mod;
    }

    public ModN add()
    {

    }

    public ModN subtract()
    {

    }

    public ModN multiply()
    {

    }

    public ModN power(double k)
    {

    }
    @Override
    public String toString() {
        return this.modValue + " (mod " + this.mod + ")";
    }

    public static void main(String[] args) {
        ModN b = new ModN(13, 3);
    }
}
