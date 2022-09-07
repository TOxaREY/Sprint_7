import java.util.List;

public class Order {

    public final String firstName;
    public final String lastName;
    public final String address;
    public final Integer metroStation;
    public final String phone;
    public final Integer rentTime;
    public final String deliveryDate;
    public final String comment;
    public final List<String> color;

    public Order(String firstName, String lastName, String address, Integer metroStation, String phone, Integer rentTime, String deliveryDate, String comment, List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }
}
