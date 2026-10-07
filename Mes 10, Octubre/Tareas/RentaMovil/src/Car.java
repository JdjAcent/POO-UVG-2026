import java.util.Set;

public class Car extends Vehicle {
    private final int passengers;
    private final boolean automatic;
    public Car(String plate, String model, String brand, double dailyPrice, int passengers, boolean automatic) {
        super(plate, model, brand, dailyPrice);
        if (passengers <= 0) throw new IllegalArgumentException("Pasajeros debe ser positivo.");
        this.passengers = passengers;
        this.automatic = automatic;
        extraInfo.add("Pasajeros: " + passengers);
        extraInfo.add("Transmisión: " + (automatic ? "automática" : "manual"));
    }
    @Override
    public double getCharge(int days) {
        if (days <= 0) throw new IllegalArgumentException("Días debe ser positivo.");

        return days * (dailyPrice + (automatic ? 50 : 0));
    }
    @Override
    public boolean licenseValidation(Set<String> licences) {

        if (licences == null) throw new IllegalArgumentException("Licencias obligatorias.");
        return licences.contains("A") || licences.contains("B") || licences.contains("C");
    }
    @Override
    public int getMaintenanceThreshold() { return 30; }
    @Override
    public String getExtraInfo() { return String.join(", ", extraInfo); }
    @Override
    public String getCategory() { return "Automóvil"; }
}
