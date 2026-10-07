import java.util.Set;
public class IndividualCustomer extends Customer {
    public IndividualCustomer(String id, String name, Set<String> licences) {
        super(id, name, licences);
        if (!getId().matches("[0-9]{13}")) throw new IllegalArgumentException("El DPI debe tener 13 dígitos.");
    }
    @Override
    public double getDiscount(double subtotal, int previousConfirmedRentals) {

        return previousConfirmedRentals >= 3 ? subtotal * 0.05 : 0;
    }
    @Override
    public int getActiveRentalLimit() {

        return 1;
    }
}
