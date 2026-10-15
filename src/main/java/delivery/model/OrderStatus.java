package delivery.model;

public enum OrderStatus {

    CREATED("Создан"),
    CONFIRMED("Подтверждён"),
    IN_DELIVERY("Передан курьеру"),
    READY_FOR_PICKUP("Готов к выдаче"),
    DELIVERED("Доставлен"),
    CANCELLED("Отменён");

    private final String title;

    OrderStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public boolean canTransitionTo(OrderStatus next) {
        switch (this) {
            case CREATED:
                return next == CONFIRMED || next == CANCELLED;
            case CONFIRMED:
                return next == IN_DELIVERY || next == READY_FOR_PICKUP || next == CANCELLED;
            case IN_DELIVERY:
            case READY_FOR_PICKUP:
                return next == DELIVERED;
            default:
                return false;
        }
    }
}