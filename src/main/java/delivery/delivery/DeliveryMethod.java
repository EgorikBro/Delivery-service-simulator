package delivery.delivery;

public interface DeliveryMethod {

    String getName();

    double calculateCost(double itemsTotal);

    int getDeliveryDays();

    boolean requiresCourier();
}
