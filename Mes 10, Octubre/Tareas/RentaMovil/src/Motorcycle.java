import java.util.Set;

public class Motorcycle extends Vehicle {
    private final int engineCc;
    public Motorcycle(String plate, String model, String brand, double dailyPrice, int engineCc) {
        super(plate, model, brand, dailyPrice);
        if (engineCc <= 0) throw new IllegalArgumentException("Cilindraje debe ser positivo.");
        this.engineCc = engineCc;
        extraInfo.add("Cilindraje: " + engineCc + " cc");
    }
    @Override
    public double getCharge(int days) {
        if (days <= 0) throw new IllegalArgumentException("Días debe ser positivo.");

        return days * dailyPrice + (engineCc > 250 ? 75 : 0);
    }
    @Override
    public boolean licenseValidation(Set<String> licences) {

        if (licences == null) throw new IllegalArgumentException("Licencias obligatorias.");
        return licences.contains("M");
    }
    @Override
    public int getMaintenanceThreshold() { return 20; }
    @Override
    public String getExtraInfo() { return String.join(", ", extraInfo); }
    @Override
    public String getCategory() { return "Motocicleta"; }
}
