package components;

public enum Field {
    FIRST_NAME("first-name"),
    LAST_NAME("last-name"),
    DATE_OF_BIRTH("dob"),
    COUNTRY("country"),
    POSTAL_CODE("postal_code"),
    HOUSE_NUMBER("house_number"),
    STREET("street"),
    CITY("city"),
    STATE("state"),
    PHONE("phone"),
    EMAIL("email"),
    PASSWORD("password");

    private final String id;

    Field (String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
