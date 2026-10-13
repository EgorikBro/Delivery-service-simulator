package delivery.delivery;

// Самовывоз: бесплатно, курьер не нужен
public class PickupDelivery implements DeliveryMethod {

    @Override
    public String getName() {
        return "Самовывоз";
    }

    @Override
    public double calculateCost(double itemsTotal) {
        return 0;
    }

    @Override
    public int getDeliveryDays() {
        return 0;
    }

    @Override
    public boolean requiresCourier() {
        return false;
    }
}
