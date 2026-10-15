package delivery.service;

import delivery.delivery.DeliveryMethod;
import delivery.model.Client;
import delivery.model.Courier;
import delivery.model.Order;
import delivery.model.OrderItem;
import delivery.model.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final List<Order> orders = new ArrayList<>();
    private final CourierService courierService;
    private int nextId = 1;

    public OrderService(CourierService courierService) {
        this.courierService = courierService;
    }

    public Order createOrder(Client client, String address) {
        Order order = new Order(nextId, client, address);
        orders.add(order);
        nextId++;
        return order;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public Order findOrder(int id) {
        for (Order order : orders) {
            if (order.getId() == id) {
                return order;
            }
        }
        throw new IllegalArgumentException("Заказ с номером " + id + " не найден");
    }

    public void addItem(int orderId, OrderItem item) {
        findOrder(orderId).addItem(item);
    }

    public void setDeliveryMethod(int orderId, DeliveryMethod deliveryMethod) {
        findOrder(orderId).setDeliveryMethod(deliveryMethod);
    }

    public void assignCourier(int orderId, int courierId) {
        Order order = findOrder(orderId);
        Courier courier = courierService.findCourier(courierId);
        order.assignCourier(courier);
    }

    public void changeStatus(int orderId, OrderStatus status) {
        findOrder(orderId).changeStatus(status);
    }
}