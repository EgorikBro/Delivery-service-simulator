package delivery.model;

public class Courier {

    private final int id;
    private final String name;
    private final String phone;
    private boolean available;

    public Courier(int id, String name, String phone) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя курьера не может быть пустым");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Телефон курьера не может быть пустым");
        }
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.available = true;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isAvailable() {
        return available;
    }

    public void occupy() {
        if (!available) {
            throw new IllegalStateException("Курьер " + name + " уже занят другим заказом");
        }
        available = false;
    }

    public void release() {
        available = true;
    }
}
