import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller();
        DemoData.load(controller);
        view.showMessage("RentaMovil - gestión de alquileres");
        boolean running = true;
        while (running) {
            try {
                switch (view.showMenu()) {
                    case 1: registerVehicle(view, controller); break;
                    case 2: registerCustomer(view, controller); break;
                    case 3: controller.getVehicles().forEach(v -> view.showMessage(v.toString())); break;
                    case 4: controller.getCustomers().forEach(c -> view.showMessage(c.toString())); break;
                    case 5: quoteOrRent(view, controller, false); break;
                    case 6: quoteOrRent(view, controller, true); break;
                    case 7:
                        controller.returnVehicle(view.readString("Placa: "));
                        view.showMessage("Devolución registrada."); break;
                    case 8:
                        controller.finishMaintenance(view.readString("Placa: "));
                        view.showMessage("Mantenimiento finalizado."); break;
                    case 9:
                        view.showMessage(controller.getFleetReport());
                        view.showMessage(controller.getIncomeReport()); break;
                    case 10: controller.getActiveRentals().forEach(r -> view.showMessage(r.toString())); break;
                    case 11:
                        String id = view.readString("DPI/NIT: ");
                        controller.getCustomerHistory(id).forEach(r -> view.showMessage(r.toString()));
                        view.showMessage(String.format(Locale.US, "Total pagado: Q%.2f", controller.getCustomerTotalPaid(id)));
                        break;
                    case 0: running = false; break;
                    default: view.showMessage("Opción inválida.");
                }
            } catch (ArithmeticException e) {
                view.showMessage("El acumulado de días excede el rango permitido.");
            } catch (IllegalArgumentException | IllegalStateException e) {
                view.showMessage("No se completó la operación: " + e.getMessage());
            } catch (NoSuchElementException e) {
                view.showMessage("Entrada cerrada. Hasta luego.");
                running = false;
            }
        }
    }
    private static void quoteOrRent(View view, Controller controller, boolean rent) {
        String plate = view.readString("Placa: ");
        String id = view.readString("DPI/NIT: ");
        int days = view.readInt("Días: ");
        view.showMessage(controller.newQuote(plate, days, id));
        if (!rent) return;
        if (!controller.getRentalObstacles(plate, id).isEmpty()) return;
        if (view.confirm("¿Confirmar alquiler?")) {
            view.showMessage(controller.newRent(plate, days, id).toString());
        } else {
            view.showMessage("Operación cancelada. No se modificó ningún dato.");
        }
    }
    private static void registerVehicle(View view, Controller controller) {
        int type = view.readInt("1. Automóvil  2. Motocicleta  3. Carga  4. Microbús: ");
        if (type < 1 || type > 4) throw new IllegalArgumentException("Categoría inválida.");
        String plate = view.readString("Placa: ");
        String model = view.readString("Modelo: ");
        String brand = view.readString("Marca: ");
        double price = view.readDouble("Tarifa diaria: ");
        Vehicle vehicle;
        // Este switch es SOLO para construcción, la excepción permitida por el enunciado.
        switch (type) {
            case 1: vehicle = new Car(plate, model, brand, price, view.readInt("Pasajeros: "), view.confirm("¿Automático?")); break;
            case 2: vehicle = new Motorcycle(plate, model, brand, price, view.readInt("Cilindraje cc: ")); break;
            case 3: vehicle = new CargoVan(plate, model, brand, price, view.readDouble("Capacidad toneladas: ")); break;
            default: vehicle = new Minibus(plate, model, brand, price, view.readInt("Pasajeros: "), view.confirm("¿Incluye piloto fijo?"));
        }
        controller.addVehicle(vehicle);
        view.showMessage("Vehículo registrado.");
    }
    private static void registerCustomer(View view, Controller controller) {
        int type = view.readInt("1. Individual  2. Corporativo: ");
        if (type != 1 && type != 2) throw new IllegalArgumentException("Tipo inválido.");
        String id = view.readString("DPI/NIT: ");
        String name = view.readString("Nombre: ");
        Set<String> licences = new HashSet<>(Arrays.asList(
            view.readString("Licencias separadas por comas (A,B,C,M): ").split(",", -1)));
        Customer customer;
        if (type == 1) customer = new IndividualCustomer(id, name, licences);
        else customer = new CorporateCustomer(id, name, licences,
            view.readString("Empresa: "), view.readString("Contacto: "));
        controller.addCustomer(customer);
        view.showMessage("Cliente registrado.");
    }
}
