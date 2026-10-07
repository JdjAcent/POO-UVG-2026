import java.util.Set;

public class CargoVan extends Vehicle {
    private final double capacityTons;
    public CargoVan(String plate, String model, String brand, double dailyPrice, double capacityTons) {
        super(plate, model, brand, dailyPrice);
        if (!Double.isFinite(capacityTons) || capacityTons <= 0) throw new IllegalArgumentException("Capacidad inválida.");
        this.capacityTons = capacityTons;
        extraInfo.add("Capacidad: " + capacityTons + " toneladas");
    }
    @Override
    public double getCharge(int days) {
        if (days <= 0) throw new IllegalArgumentException("Días debe ser positivo.");

        return days * (dailyPrice + 100 * capacityTons);
    }
    @Override
    public boolean licenseValidation(Set<String> licences) {

        if (licences == null) throw new IllegalArgumentException("Licencias obligatorias.");
        return licences.contains("A") || licences.contains("B");
    }
    @Override
    public int getMaintenanceThreshold() { return 15; }
    @Override
    public String getExtraInfo() { return String.join(", ", extraInfo); }
    @Override
    public String getCategory() { return "Camioneta de carga"; }
}
