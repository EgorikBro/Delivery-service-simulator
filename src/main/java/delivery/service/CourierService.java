package delivery.service;

import delivery.model.Courier;

import java.util.ArrayList;
import java.util.List;

public class CourierService {

    private final List<Courier> couriers = new ArrayList<>();

    public CourierService() {
        couriers.add(new Courier(1, "Иван Петров", "+79001112233"));
        couriers.add(new Courier(2, "Анна Смирнова", "+79004445566"));
        couriers.add(new Courier(3, "Олег Кузнецов", "+79007778899"));
    }

    public List<Courier> getAvailableCouriers() {
        List<Courier> result = new ArrayList<>();
        for (Courier courier : couriers) {
            if (courier.isAvailable()) {
                result.add(courier);
            }
        }
        return result;
    }

    public Courier findCourier(int id) {
        for (Courier courier : couriers) {
            if (courier.getId() == id) {
                return courier;
            }
        }
        throw new IllegalArgumentException("Курьер с номером " + id + " не найден");
    }
}
