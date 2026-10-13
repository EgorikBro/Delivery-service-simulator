package delivery.delivery;

public class ExpressDelivery implements DeliveryMethod {

    private static final double BASE_COST = 500;
    private static final double PERCENT = 0.05;
    private static final int DAYS = 1;

    @Override
    public String getName() {
        return "Экспресс-доставка";
    }

    // 500 руб. + 5% от суммы заказа
    @Override
    public double calculateCost(double itemsTotal) {
        return BASE_COST + itemsTotal * PERCENT;
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
