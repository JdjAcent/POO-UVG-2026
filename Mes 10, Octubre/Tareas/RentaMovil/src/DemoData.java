import java.util.Set;
public class DemoData {
    public static void load(Controller controller) {
        controller.addVehicle(new Car("P001", "Corolla", "Toyota", 200, 5, true));
        controller.addVehicle(new Car("P002", "Rio", "Kia", 180, 5, false));
        controller.addVehicle(new Motorcycle("M001", "CB", "Honda", 100, 250));
        controller.addVehicle(new Motorcycle("M002", "MT", "Yamaha", 150, 300));
        controller.addVehicle(new CargoVan("C001", "H100", "Hyundai", 200, 1.5));
        controller.addVehicle(new CargoVan("C002", "DMax", "Isuzu", 220, 2));
        controller.addVehicle(new Minibus("B001", "Hiace", "Toyota", 450, 15, true));
        controller.addVehicle(new Minibus("B002", "Urvan", "Nissan", 400, 12, false));
        controller.addCustomer(new IndividualCustomer("1234567890123", "Ana", Set.of("C")));
        controller.addCustomer(new IndividualCustomer("9876543210123", "Luis", Set.of("A", "M")));
        controller.addCustomer(new CorporateCustomer("12345-6", "Compras", Set.of("B"), "Empresa Uno", "Marta"));
        controller.addCustomer(new CorporateCustomer("67890-1", "Operaciones", Set.of("M"), "Empresa Dos", "Pedro"));
        controller.addHistoricalRental("P002", 1, "1234567890123");
        controller.addHistoricalRental("P002", 1, "1234567890123");
        controller.addHistoricalRental("P002", 1, "1234567890123");
        controller.findVehicle("P002").initializeRentedDays(3);
        controller.findVehicle("P001").initializeRentedDays(29);
    }
}
