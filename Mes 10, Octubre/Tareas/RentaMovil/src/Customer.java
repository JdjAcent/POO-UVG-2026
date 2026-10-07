import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public abstract class Customer {
    private final String id;
    private final String name;
    private final Set<String> licences;

    protected Customer(String id, String name, Set<String> licences) {
        if (id == null || id.isBlank() || name == null || name.isBlank())
            throw new IllegalArgumentException("Identificador y nombre obligatorios.");
        if (licences == null || licences.isEmpty())
            throw new IllegalArgumentException("Se requiere al menos una licencia.");
        Set<String> valid = Set.of("A", "B", "C", "M");
        this.licences = new HashSet<>();
        for (String licence : licences) {
            if (licence == null) throw new IllegalArgumentException("Licencia inválida.");
            String normalized = licence.trim().toUpperCase(Locale.ROOT);
            if (!valid.contains(normalized)) throw new IllegalArgumentException("Licencia inválida: " + licence);
            this.licences.add(normalized);
        }
        this.id = id.trim();
        this.name = name.trim();
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public Set<String> getLicences() { return new HashSet<>(licences); }
    public abstract double getDiscount(double subtotal, int previousConfirmedRentals);
    public abstract int getActiveRentalLimit();
    @Override
    public String toString() { return id + " | " + name + " | Licencias: " + licences; }
}
