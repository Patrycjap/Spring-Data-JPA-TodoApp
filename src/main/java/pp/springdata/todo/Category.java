package pp.springdata.todo;

public enum Category {
    HOME("Dom"),
    WORK("Praca"),
    TRAVEL("Podróż");

    private String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}