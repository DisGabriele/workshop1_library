package it.paa;

public enum Roles {
    ADMIN("1"),
    USER("2");
    private final String value;

    Roles(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
