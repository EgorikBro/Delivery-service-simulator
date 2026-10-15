package delivery.model;

import delivery.delivery.DeliveryMethod;

import java.util.ArrayList;
import java.util.List;

// Заказ: клиент, адрес, позиции, способ доставки, курьер и статус
public class Order {

    private final int id;
    private final Client client;
    private final String address;
    private final List<OrderItem> items = new ArrayList<>();
    private DeliveryMethod deliveryMethod;
    private Courier courier;
    private OrderStatus status;

    public Order(int id, Client client, String address) {
        if (client == null) {
            throw new IllegalArgumentException("У заказа должен быть клиент");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Адрес доставки не может быть пустым");
        }
        this.id = id;
        this.client = client;
        this.address = address;
        this.status = OrderStatus.CREATED;
    }

    // Добавить позицию можно только в новый заказ
    public void addItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Позиция заказа не может быть пустой");
        }
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Добавлять товары можно только в новый заказ");
        }
        items.add(item);
    }

    // Выбрать способ доставки можно только до подтверждения заказа
    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        if (deliveryMethod == null) {
            throw new IllegalArgumentException("Способ доставки не может быть пустым");
        }
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Способ доставки можно выбрать только до подтверждения заказа");
        }
        this.deliveryMethod = deliveryMethod;
    }

    // Курьера назначают подтверждённому заказу, если способ доставки этого требует
    public void assignCourier(Courier courier) {
        if (courier == null) {
            throw new IllegalArgumentException("Курьер не может быть пустым");
        }
        if (status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Курьера можно назначить только подтверждённому заказу");
        }
        if (!deliveryMethod.requiresCourier()) {
            throw new IllegalStateException("Для способа «" + deliveryMethod.getName() + "» курьер не нужен");
        }
        if (this.courier != null) {
            throw new IllegalStateException("Курьер уже назначен");
        }
        courier.occupy();
        this.courier = courier;
    }

    // Изменить статус с проверкой всех правил
    public void changeStatus(OrderStatus next) {
        if (next == null) {
            throw new IllegalArgumentException("Статус не может быть пустым");
        }
        if (!status.canTransitionTo(next)) {
            throw new IllegalStateException("Нельзя перейти из статуса «" + status.getTitle()
                    + "» в статус «" + next.getTitle() + "»");
        }
        if (next == OrderStatus.CONFIRMED) {
            if (items.isEmpty()) {
                throw new IllegalStateException("Нельзя подтвердить заказ без товаров");
            }
            if (deliveryMethod == null) {
                throw new IllegalStateException("Сначала выберите способ доставки");
            }
        }
        if (next == OrderStatus.IN_DELIVERY) {
            if (!deliveryMethod.requiresCourier()) {
                throw new IllegalStateException("Заказ на самовывоз не передаётся курьеру");
            }
            if (courier == null) {
                throw new IllegalStateException("Сначала назначьте курьера");
            }
        }
        if (next == OrderStatus.READY_FOR_PICKUP && deliveryMethod.requiresCourier()) {
            throw new IllegalStateException("Статус «Готов к выдаче» бывает только у самовывоза");
        }
        // Заказ завершён, курьер снова свободен
        if ((next == OrderStatus.DELIVERED || next == OrderStatus.CANCELLED) && courier != null) {
            courier.release();
        }
        status = next;
    }

    // Сумма всех позиций
    public double getItemsTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getTotal();
        }
        return total;
    }

    // Стоимость доставки считает выбранный способ доставки
    public double getDeliveryCost() {
        if (deliveryMethod == null) {
            return 0;
        }
        return deliveryMethod.calculateCost(getItemsTotal());
    }

    public double getTotalCost() {
        return getItemsTotal() + getDeliveryCost();
    }

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public String getAddress() {
        return address;
    }

    // Возвращаем копию, чтобы товары нельзя было добавить в обход addItem
    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public Courier getCourier() {
        return courier;
    }

    public OrderStatus getStatus() {
        return status;
    }
}