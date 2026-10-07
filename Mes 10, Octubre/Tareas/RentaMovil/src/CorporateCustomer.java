import java.util.Set;
public class CorporateCustomer extends Customer {
    private final String companyName;
    private final String contactName;
    public CorporateCustomer(String id, String name, Set<String> licences, String companyName, String contactName) {
        super(id, name, licences);
        if (companyName == null || companyName.isBlank() || contactName == null || contactName.isBlank())
            throw new IllegalArgumentException("Empresa y contacto obligatorios.");
        this.companyName = companyName.trim();
        this.contactName = contactName.trim();
    }
    @Override
    public double getDiscount(double subtotal, int previousConfirmedRentals) {

        return subtotal * 0.10;
    }
    @Override
    public int getActiveRentalLimit() {

        return 3;
    }
    @Override
    public String toString() {
        return super.toString() + " | Empresa: " + companyName + " | Contacto: " + contactName;
    }
}
