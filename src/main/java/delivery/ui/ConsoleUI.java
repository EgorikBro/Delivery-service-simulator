package delivery.ui;

import delivery.delivery.DeliveryMethod;
import delivery.model.Client;
import delivery.model.Courier;
import delivery.model.Order;
import delivery.model.OrderItem;
import delivery.model.OrderStatus;
import delivery.service.CourierService;
import delivery.service.OrderService;

import java.util.ArrayList;
import java.util.List;

public class ConsoleUI {

    private final OrderService orderService;
    private final CourierService courierService;
    private final List<DeliveryMethod> deliveryMethods;
    private final InputReader input;
    private final OrderPrinter printer;

    public ConsoleUI(OrderService orderService, CourierService courierService,
                     List<DeliveryMethod> deliveryMethods, InputReader input, OrderPrinter printer) {
        this.orderService = orderService;
        this.courierService = courierService;
        this.deliveryMethods = deliveryMethods;
        this.input = input;
        this.printer = printer;
    }

    public void run() {
        System.out.println("Добро пожаловать в симулятор службы доставки!");
        while (true) {
            printMenu();
            int choice = input.readInt("Выберите пункт: ");
            if (choice == 0) {
                System.out.println("До свидания!");
                return;
            }
            try {
                switch (choice) {
                    case 1:
                        createOrder();
                        break;
                    case 2:
                        printer.printOrders(orderService.getAllOrders());
                        break;
                    case 3:
                        chooseDelivery();
                        break;
                    case 4:
                        assignCourier();
                        break;
                    case 5:
                        changeStatus();
                        break;
                    default:
                        System.out.println("Такого пункта нет, выберите от 0 до 5");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("===== Меню =====");
        System.out.println("1. Создать заказ");
        System.out.println("2. Показать все заказы");
        System.out.println("3. Выбрать способ доставки");
        System.out.println("4. Назначить курьера");
        System.out.println("5. Изменить статус заказа");
        System.out.println("0. Выход");
    }

    private void createOrder() {
        String name = input.readText("Имя клиента: ");
        String phone = input.readText("Телефон клиента: ");
        String address = input.readText("Адрес доставки: ");
        Order order = orderService.createOrder(new Client(name, phone), address);
        System.out.println("Создан заказ №" + order.getId() + ". Добавьте товары.");

        boolean addMore = true;
        while (addMore) {
            addItem(order);
            String answer = input.readText("Добавить ещё товар? (да/нет): ");
            addMore = answer.equalsIgnoreCase("да");
        }
        printer.printOrder(order);
    }

    private void addItem(Order order) {
        String productName = input.readText("Название товара: ");
        int quantity = input.readInt("Количество: ");
        double price = input.readDouble("Цена за штуку: ");
        try {
            orderService.addItem(order.getId(), new OrderItem(productName, quantity, price));
            System.out.println("Товар добавлен");
        } catch (IllegalArgumentException | IllegalStateException e) { // Ошибка в одной позиции не должна отменять весь заказ, поэтому ловим её здесь
            System.out.println("Товар не добавлен: " + e.getMessage());
        }
    }

    private void chooseDelivery() {
        Order order = orderService.findOrder(input.readInt("Номер заказа: "));
        System.out.println("Способы доставки для суммы " + String.format("%.2f", order.getItemsTotal()) + " руб.:");
        printer.printDeliveryMethods(deliveryMethods, order.getItemsTotal());

        int number = input.readInt("Выберите способ доставки: ");
        if (number < 1 || number > deliveryMethods.size()) {
            System.out.println("Такого способа доставки нет");
            return;
        }
        orderService.setDeliveryMethod(order.getId(), deliveryMethods.get(number - 1));
        System.out.println("Способ доставки выбран");
        printer.printOrder(order);
    }

    private void assignCourier() {
        Order order = orderService.findOrder(input.readInt("Номер заказа: "));
        DeliveryMethod method = order.getDeliveryMethod();
        if (method != null && !method.requiresCourier()) {
            System.out.println("Для способа «" + method.getName() + "» курьер не нужен");
            return;
        }
        List<Courier> couriers = courierService.getAvailableCouriers();
        System.out.println("Свободные курьеры:");
        printer.printCouriers(couriers);
        if (couriers.isEmpty()) {
            return;
        }
        int courierId = input.readInt("Номер курьера: ");
        orderService.assignCourier(order.getId(), courierId);
        System.out.println("Курьер назначен");
        printer.printOrder(order);
    }

    private void changeStatus() {
        Order order = orderService.findOrder(input.readInt("Номер заказа: "));
        printer.printOrder(order);

        // Показываем только статусы, в которые можно перейти из текущего
        List<OrderStatus> nextStatuses = new ArrayList<>();
        for (OrderStatus status : OrderStatus.values()) {
            if (order.getStatus().canTransitionTo(status)) {
                nextStatuses.add(status);
            }
        }
        if (nextStatuses.isEmpty()) {
            System.out.println("Заказ завершён, статус больше менять нельзя");
            return;
        }
        System.out.println("Доступные статусы:");
        for (int i = 0; i < nextStatuses.size(); i++) {
            System.out.println((i + 1) + ". " + nextStatuses.get(i).getTitle());
        }

        int number = input.readInt("Выберите новый статус: ");
        if (number < 1 || number > nextStatuses.size()) {
            System.out.println("Такого статуса нет в списке");
            return;
        }
        orderService.changeStatus(order.getId(), nextStatuses.get(number - 1));
        System.out.println("Статус изменён");
        printer.printOrder(order);
    }
}

