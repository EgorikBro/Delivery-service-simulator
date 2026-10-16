package delivery.model;

public class Client {

    private final String name;
    private final String phone;

    public Client(String name, String phone) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя клиента не может быть пустым");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Телефон клиента не может быть пустым");
        }
        if (!isValidPhone(phone)) {
            throw new IllegalArgumentException("Некорректный телефон. Допустимы цифры, пробелы и символы + - ( ),"
                    + " всего от 10 до 15 цифр. Например: +7 (900) 123-45-67");
        }
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    private boolean isValidPhone(String phone) {
        int digits = 0;
        for (int i = 0; i < phone.length(); i++) {
            char c = phone.charAt(i);
            if (Character.isDigit(c)) {
                digits++;
            } else if (c != '+' && c != '-' && c != '(' && c != ')' && c != ' ') {
                return false;
            }
        }
        return digits >= 10 && digits <= 15;
    }
}