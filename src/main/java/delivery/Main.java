package delivery;

import delivery.delivery.DeliveryMethod;
import delivery.delivery.ExpressDelivery;
import delivery.delivery.PickupDelivery;
import delivery.delivery.StandardDelivery;
import delivery.service.CourierService;
import delivery.service.OrderService;
import delivery.ui.ConsoleUI;
import delivery.ui.InputReader;
import delivery.ui.OrderPrinter;

import java.util.List;

// Точка входа: создаёт все объекты программы и запускает меню
public class Main {

    public static void main(String[] args) {
        CourierService courierService = new CourierService();
        OrderService orderService = new OrderService(courierService);
        List<DeliveryMethod> deliveryMethods = List.of(
                new StandardDelivery(),
                new ExpressDelivery(),
                new PickupDelivery()
        );

        ConsoleUI ui = new ConsoleUI(orderService, courierService, deliveryMethods,
                new InputReader(), new OrderPrinter());
        ui.run();
    }
}
