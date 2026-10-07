import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

public abstract class Vehicle {
    private final String plate;
    private final String model;
    private final String brand;
    protected final double dailyPrice;
    private boolean rented;
    private boolean outOfService;
    private int totalRentedDays;
    protected final ArrayList<String> extraInfo = new ArrayList<>();

    protected Vehicle(String plate, String model, String brand, double dailyPrice) {
        if (plate == null || plate.isBlank()) throw new IllegalArgumentException("Placa vacía.");
        if (model == null || model.isBlank() || brand == null || brand.isBlank())
            throw new IllegalArgumentException("Marca y modelo obligatorios.");
        if (!Double.isFinite(dailyPrice) || dailyPrice <= 0)
            throw new IllegalArgumentException("La tarifa debe ser positiva y finita.");
        this.plate = plate.trim().toUpperCase(Locale.ROOT);
        this.model = model.trim();
        this.brand = brand.trim();
        this.dailyPrice = dailyPrice;
    }

    public String getPlate() { return plate; }
    public double getDailyPrice() { return dailyPrice; }
    public boolean isRented() { return rented; }
    public boolean isOutOfService() { return outOfService; }
    public int getTotalRentedDays() { return totalRentedDays; }
    public boolean availability() { return !rented && !outOfService; }
    public String getState() {
        if (rented) return "Alquilado";
        if (outOfService) return "En mantenimiento";
        return "Disponible";
    }
    public void markRented() {
        if (!availability()) throw new IllegalStateException("Vehículo no disponible.");
        rented = true;
    }
    public void registerReturn(int days) {

        // Acumular días, quitar rented y decidir mantenimiento con el umbral polimórfico.
        if (!rented) throw new IllegalStateException("El vehículo no está alquilado.");
        if (days <= 0) throw new IllegalArgumentException("Días debe ser positivo.");
        int accumulated = Math.addExact(totalRentedDays, days);
        totalRentedDays = accumulated;
        rented = false;
        outOfService = accumulated >= getMaintenanceThreshold();
    }
    public void finishMaintenance() {

        if (!outOfService) throw new IllegalStateException("El vehículo no está en mantenimiento.");
        outOfService = false;
        totalRentedDays = 0;
    }
    // Solo para la precarga de demostración, antes de operar con el vehículo.
    void initializeRentedDays(int days) {
        if (!availability() || totalRentedDays != 0 || days < 0 || days >= getMaintenanceThreshold())
            throw new IllegalArgumentException("Acumulado inicial inválido.");
        totalRentedDays = days;
    }
    public abstract double getCharge(int days);
    public abstract boolean licenseValidation(Set<String> licences);
    public abstract int getMaintenanceThreshold();
    public abstract String getExtraInfo();
    public abstract String getCategory();

    @Override
    public String toString() {
        return String.format(Locale.US,
            "%s | %s | %s %s | Q%.2f/día | %s | %s", plate, getCategory(), brand,
            model, dailyPrice, getState(), getExtraInfo());
    }
}
