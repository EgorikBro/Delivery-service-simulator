package delivery.ui;

import delivery.delivery.DeliveryMethod;
import delivery.model.Courier;
import delivery.model.Order;
import delivery.model.OrderItem;

import java.util.List;

public class OrderPrinter {

    public void printOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("Заказов пока нет");
            return;
        }
        for (Order order : orders) {
            System.out.println("Заказ №" + order.getId()
                    + " | " + order.getClient().getName()
                    + " | " + order.getStatus().getTitle()
                    + " | итого " + formatMoney(order.getTotalCost()));
        }
    }

    public void printOrder(Order order) {
        System.out.println("===== Заказ №" + order.getId() + " =====");
        System.out.println("Клиент: " + order.getClient().getName() + ", тел. " + order.getClient().getPhone());
        System.out.println("Адрес: " + order.getAddress());
        System.out.println("Статус: " + order.getStatus().getTitle());

        System.out.println("Товары:");
        if (order.getItems().isEmpty()) {
            System.out.println("  нет товаров");
        }
        for (OrderItem item : order.getItems()) {
            System.out.println("  " + item.getProductName() + " x" + item.getQuantity()
                    + " по " + formatMoney(item.getPrice())
                    + " = " + formatMoney(item.getTotal()));
        }

        DeliveryMethod method = order.getDeliveryMethod();
        if (method == null) {
            System.out.println("Доставка: не выбрана");
        } else {
            System.out.println("Доставка: " + method.getName()
                    + ", " + formatMoney(order.getDeliveryCost())
                    + ", срок: " + formatDays(method.getDeliveryDays()));
        }

        Courier courier = order.getCourier();
        if (courier != null) {
            System.out.println("Курьер: " + courier.getName() + ", тел. " + courier.getPhone());
        }

        System.out.println("Сумма товаров: " + formatMoney(order.getItemsTotal()));
        System.out.println("Итого к оплате: " + formatMoney(order.getTotalCost()));
    }

    public void printDeliveryMethods(List<DeliveryMethod> methods, double itemsTotal) {
        for (int i = 0; i < methods.size(); i++) {
            DeliveryMethod method = methods.get(i);
            System.out.println((i + 1) + ". " + method.getName()
                    + " — " + formatMoney(method.calculateCost(itemsTotal))
                    + ", срок: " + formatDays(method.getDeliveryDays()));
        }
    }

    public void printCouriers(List<Courier> couriers) {
        if (couriers.isEmpty()) {
            System.out.println("Свободных курьеров нет");
            return;
        }
        for (Courier courier : couriers) {
            System.out.println(courier.getId() + ". " + courier.getName() + ", тел. " + courier.getPhone());
        }
    }

    private String formatMoney(double amount) {
        return String.format("%.2f руб.", amount);
    }

    private String formatDays(int days) {
        if (days == 0) {
            return "в день заказа";
        }
        return days + " дн.";
    }
}
