package insurance;

import java.util.Objects;

public final class Restaurant implements Client {
    private final String name;
    private final int seatingCapacity;
    private final boolean openFlameKitchen;

    public Restaurant(String name, int seatingCapacity, boolean openFlameKitchen) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Restaurant name must not be blank");
        }
        if (seatingCapacity <= 0) {
            throw new IllegalArgumentException("Seating capacity must be positive");
        }
        this.name = name.trim();
        this.seatingCapacity = seatingCapacity;
        this.openFlameKitchen = openFlameKitchen;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public boolean hasOpenFlameKitchen() {
        return openFlameKitchen;
    }

    @Override
    public void accept(ClientVisitor visitor) {
        Objects.requireNonNull(visitor, "Client visitor cannot be null");
        visitor.visitRestaurant(this);
    }
}
