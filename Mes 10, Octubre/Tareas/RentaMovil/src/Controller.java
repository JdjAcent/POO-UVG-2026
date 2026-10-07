import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class Controller {
    private final ArrayList<Customer> customers = new ArrayList<>();
    private final ArrayList<Vehicle> vehicles = new ArrayList<>();
    private final ArrayList<Rental> rentals = new ArrayList<>();
    private int rentalsCounter = 1;

    public void addCustomer(Customer customer) {
        if (customer == null) throw new IllegalArgumentException("Cliente nulo.");
        if (findCustomer(customer.getId()) != null) throw new IllegalArgumentException("Identificador repetido.");
        customers.add(customer);
    }
    public void addVehicle(Vehicle vehicle) {
        if (vehicle == null) throw new IllegalArgumentException("Vehículo nulo.");
        if (findVehicle(vehicle.getPlate()) != null) throw new IllegalArgumentException("Placa repetida.");
        vehicles.add(vehicle);
    }
    public Customer findCustomer(String id) {
        for (Customer customer : customers) if (customer.getId().equals(id.trim())) return customer;
        return null;
    }
    public Vehicle findVehicle(String plate) {
        for (Vehicle vehicle : vehicles) if (vehicle.getPlate().equalsIgnoreCase(plate.trim())) return vehicle;
        return null;
    }
    public ArrayList<Customer> getCustomers() { return new ArrayList<>(customers); }
    public ArrayList<Vehicle> getVehicles() { return new ArrayList<>(vehicles); }
    public ArrayList<Rental> getActiveRentals() {
        ArrayList<Rental> result = new ArrayList<>();
        for (Rental rental : rentals) if (rental.isActive()) result.add(rental);
        return result;
    }
    public ArrayList<Rental> getCustomerHistory(String id) {
        Customer customer = requireCustomer(id);
        ArrayList<Rental> result = new ArrayList<>();
        for (Rental rental : rentals) if (rental.getCustomer() == customer) result.add(rental);
        return result;
    }
    private Customer requireCustomer(String id) {
        Customer customer = findCustomer(id);
        if (customer == null) throw new IllegalArgumentException("Cliente inexistente.");
        return customer;
    }
    private Vehicle requireVehicle(String plate) {
        Vehicle vehicle = findVehicle(plate);
        if (vehicle == null) throw new IllegalArgumentException("Vehículo inexistente.");
        return vehicle;
    }
    public ArrayList<String> getRentalObstacles(String plate, String id) {
        Vehicle vehicle = requireVehicle(plate);
        Customer customer = requireCustomer(id);
        ArrayList<String> reasons = new ArrayList<>();
        if (!vehicle.availability()) reasons.add("Vehículo no disponible: " + vehicle.getState());
        if (!vehicle.licenseValidation(customer.getLicences())) reasons.add("Licencia inadecuada.");
        int active = 0;
        for (Rental rental : getCustomerHistory(id)) if (rental.isActive()) active++;
        if (active >= customer.getActiveRentalLimit()) reasons.add("Límite de alquileres activos alcanzado.");
        return reasons;
    }
    public String newQuote(String plate, int days, String id) {
        if (days <= 0) throw new IllegalArgumentException("Días debe ser positivo.");
        Vehicle vehicle = requireVehicle(plate);
        Customer customer = requireCustomer(id);
        double subtotal = vehicle.getCharge(days);
        double discount = customer.getDiscount(subtotal, getCustomerHistory(id).size());
        ArrayList<String> reasons = getRentalObstacles(plate, id);
        return vehicle + String.format(Locale.US,
            "\nDías: %d | Subtotal Q%.2f | Descuento Q%.2f | Total Q%.2f\n%s",
            days, subtotal, discount, subtotal - discount,
            reasons.isEmpty() ? "Puede alquilarlo." : String.join("\n", reasons));
    }
    public Rental newRent(String plate, int days, String id) {

        // 1. Validar días y buscar objetos con requireVehicle/requireCustomer.
        // 2. Revalidar TODAS las condiciones usando getRentalObstacles.
        // 3. Calcular importes usando los métodos polimórficos (conteo ANTERIOR).
        // 4. Construir Rental con rentalsCounter; aún sin cambiar colecciones/estados.
        // 5. Marcar vehículo alquilado, añadir a rentals y aumentar correlativo.
        // Los reportes suman rentals: añadirlo ya registra el ingreso, no duplicarlo.
        if (days <= 0) throw new IllegalArgumentException("Días debe ser positivo.");
        Vehicle vehicle = requireVehicle(plate);
        Customer customer = requireCustomer(id);
        ArrayList<String> reasons = getRentalObstacles(plate, id);
        if (!reasons.isEmpty()) throw new IllegalStateException(String.join("\n", reasons));
        double subtotal = vehicle.getCharge(days);
        double discount = customer.getDiscount(subtotal, getCustomerHistory(id).size());
        Rental rental = new Rental(rentalsCounter, customer, vehicle, days, subtotal, discount);
        vehicle.markRented();
        rentals.add(rental);
        rentalsCounter++;
        return rental;
    }
    public void returnVehicle(String plate) {

        // Si no existe, rechazar sin cambios. Usar sus días para registerReturn.
        // Finalizar Rental. NO eliminarlo, descontar ingresos ni reducir el historial.
        Vehicle vehicle = requireVehicle(plate);
        if (!vehicle.isRented()) throw new IllegalStateException("El vehículo no está alquilado.");
        Rental activeRental = null;
        for (Rental rental : rentals) {
            if (rental.getVehicle() == vehicle && rental.isActive()) {
                activeRental = rental;
                break;
            }
        }
        if (activeRental == null) throw new IllegalStateException("No hay alquiler activo para la placa.");
        vehicle.registerReturn(activeRental.getDays());
        activeRental.finishRental();
    }
    public void finishMaintenance(String plate) { requireVehicle(plate).finishMaintenance(); }
    // Precarga: historial de un período anterior, sin ingresos en la sesión actual.
    void addHistoricalRental(String plate, int days, String id) {
        Vehicle vehicle = requireVehicle(plate);
        Customer customer = requireCustomer(id);
        double subtotal = vehicle.getCharge(days);
        double discount = customer.getDiscount(subtotal, getCustomerHistory(id).size());
        Rental rental = new Rental(rentalsCounter, customer, vehicle, days, subtotal, discount);
        rental.finishRental();
        rental.markHistorical();
        rentals.add(rental);
        rentalsCounter++;
    }
    public double getCustomerTotalPaid(String id) {
        double total = 0;
        for (Rental rental : getCustomerHistory(id)) total += rental.getTotal();
        return total;
    }
    public double getTotalDiscounts() {
        double total = 0;
        for (Rental rental : rentals) if (!rental.isHistorical()) total += rental.getDiscount();
        return total;
    }
    public String getIncomeReport() {
        Map<String, Double> amounts = new LinkedHashMap<>();
        for (Vehicle vehicle : vehicles) amounts.putIfAbsent(vehicle.getCategory(), 0.0);
        double total = 0;
        for (Rental rental : rentals) {
            if (rental.isHistorical()) continue;
            total += rental.getTotal();
            amounts.merge(rental.getVehicle().getCategory(), rental.getTotal(), Double::sum);
        }
        StringBuilder report = new StringBuilder();
        amounts.forEach((category, amount) -> report.append(String.format(Locale.US, "%s: Q%.2f%n", category, amount)));
        report.append(String.format(Locale.US, "TOTAL SESIÓN: Q%.2f | Descuentos sesión: Q%.2f", total, getTotalDiscounts()));
        return report.toString();
    }
    public String getFleetReport() {
        Map<String, int[]> counts = new LinkedHashMap<>();
        for (Vehicle vehicle : vehicles) {
            int[] row = counts.computeIfAbsent(vehicle.getCategory(), key -> new int[4]);
            row[0]++;
            if (vehicle.availability()) row[1]++;
            else if (vehicle.isRented()) row[2]++;
            else row[3]++;
        }
        StringBuilder report = new StringBuilder();
        counts.forEach((category, row) -> report.append(String.format(
            "%s: %d registrados, %d disponibles, %d alquilados, %d en mantenimiento%n",
            category, row[0], row[1], row[2], row[3])));
        return report.toString();
    }
}
