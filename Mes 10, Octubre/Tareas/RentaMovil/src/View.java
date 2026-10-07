import java.util.Scanner;
import java.util.NoSuchElementException;

public class View {
    private final Scanner sc = new Scanner(System.in);
    public void showMessage(String message) { System.out.println(message); }
    public String readString(String message) {
        System.out.print(message);
        if (!sc.hasNextLine()) throw new NoSuchElementException("Entrada cerrada.");
        return sc.nextLine().trim();
    }
    public int readInt(String message) {
        while (true) {
            try { return Integer.parseInt(readString(message)); }
            catch (NumberFormatException e) { showMessage("Ingresa un entero válido."); }
        }
    }
    public double readDouble(String message) {
        while (true) {
            try {
                double value = Double.parseDouble(readString(message));
                if (!Double.isFinite(value)) throw new NumberFormatException();
                return value;
            } catch (NumberFormatException e) { showMessage("Ingresa un decimal válido (ejemplo: 1.5)."); }
        }
    }
    public boolean confirm(String message) {
        while (true) {
            String answer = readString(message + " [s/n]: ");
            if (answer.equalsIgnoreCase("s")) return true;
            if (answer.equalsIgnoreCase("n")) return false;
            showMessage("Responde s o n.");
        }
    }
    public int showMenu() {
        showMessage("\n1. Registrar vehículo\n2. Registrar cliente\n3. Ver flota\n4. Ver clientes"
            + "\n5. Cotizar\n6. Alquilar\n7. Devolver vehículo\n8. Finalizar mantenimiento"
            + "\n9. Reportes\n10. Alquileres activos\n11. Historial de cliente\n0. Salir");
        return readInt("Opción: ");
    }
}
