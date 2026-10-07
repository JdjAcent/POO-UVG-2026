import java.util.Set;

public class Minibus extends Vehicle {
    private final int passengers;
    private final boolean withDriver;
    public Minibus(String plate, String model, String brand, double dailyPrice, int passengers, boolean withDriver) {
        super(plate, model, brand, dailyPrice);
        if (passengers <= 0) throw new IllegalArgumentException("Pasajeros debe ser positivo.");
        this.passengers = passengers;
        this.withDriver = withDriver;
        extraInfo.add("Pasajeros: " + passengers);
        extraInfo.add("Con piloto: " + withDriver);
    }
    @Override
    public double getCharge(int days) {
        if (days <= 0) throw new IllegalArgumentException("Días debe ser positivo.");

        return days * (dailyPrice + (withDriver ? 250 : 0));
    }
    @Override
    public boolean licenseValidation(Set<String> licences) {

        if (licences == null) throw new IllegalArgumentException("Licencias obligatorias.");
        return withDriver || licences.contains("A") || licences.contains("B");
    }
    @Override
    public int getMaintenanceThreshold() { return 25; }
    @Override
    public String getExtraInfo() { return String.join(", ", extraInfo); }
    @Override
    public String getCategory() { return "Microbús"; }
}
