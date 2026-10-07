import java.util.Locale;
public class Rental {
    private final int number;
    private final Customer customer;
    private final Vehicle vehicle;
    private final int days;
    private final double subtotal;
    private final double discount;
    private final double total;
    private boolean active = true;
    private boolean historical;

    public Rental(int number, Customer customer, Vehicle vehicle, int days, double subtotal, double discount) {
        if (number <= 0 || customer == null || vehicle == null || days <= 0)
            throw new IllegalArgumentException("Datos del alquiler inválidos.");
        if (!Double.isFinite(subtotal) || !Double.isFinite(discount) || subtotal < 0 || discount < 0 || discount > subtotal)
            throw new IllegalArgumentException("Importes inválidos.");
        this.number = number;
        this.customer = customer;
        this.vehicle = vehicle;
        this.days = days;
        this.subtotal = subtotal;
        this.discount = discount;
        this.total = subtotal - discount;
    }
    void markHistorical() {
        if (active) throw new IllegalStateException("El alquiler histórico debe estar finalizado.");
        historical = true;
    }
    public boolean isHistorical() { return historical; }
    public int getNumber() { return number; }
    public Customer getCustomer() { return customer; }
    public Vehicle getVehicle() { return vehicle; }
    public int getDays() { return days; }
    public double getSubtotal() { return subtotal; }
    public double getDiscount() { return discount; }
    public double getTotal() { return total; }
    public boolean isActive() { return active; }
    public void finishRental() {
        if (!active) throw new IllegalStateException("El alquiler ya finalizó.");
        active = false;
    }
    @Override
    public String toString() {
        return String.format(Locale.US,
            "Alquiler #%d | %s | %s | %d días | Subtotal Q%.2f | Descuento Q%.2f | Total Q%.2f | %s",
            number, customer.getName(), vehicle.getPlate(), days, subtotal, discount, total,
            historical ? "Histórico finalizado" : (active ? "Activo" : "Finalizado"));
    }
}
