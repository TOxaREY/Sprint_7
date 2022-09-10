import java.util.List;

public class DataOrder {

    public Order dataTestCreateOrderScooterColor(List<String> color) {
        return new Order(
                "Иван",
                "Иванов",
                "Алтайская 45-89",
                10,
                "89771234567",
                2,
                "2022-09-15",
                "3-й подъезд",
                color);
    }
}
