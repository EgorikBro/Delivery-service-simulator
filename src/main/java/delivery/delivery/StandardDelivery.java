package delivery.delivery;

public class StandardDelivery implements DeliveryMethod {

    private static final double COST = 300;
    private static final double FREE_FROM = 5000;
    private static final int DAYS = 3;

    @Override
    public String getName() {
        return "Стандартная доставка";
    }

    @Override
    public double calculateCost(double itemsTotal) {
        if (itemsTotal >= FREE_FROM) {
            return 0;
        }
        return COST;
    }

    @Override
    public int getDeliveryDays() {
        return DAYS;
    }

    @Override
    public boolean requiresCourier() {
        return true;
    }
}
