package insurance;

import java.util.Objects;

public final class Company implements Client {
    private final String name;
    private final int employeesCount;
    private final double equipmentValue;

    public Company(String name, int employeesCount, double equipmentValue) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Company name must not be blank");
        }
        if (employeesCount <= 0) {
            throw new IllegalArgumentException("Employees count must be positive");
        }
        if (!Double.isFinite(equipmentValue) || equipmentValue < 0.0) {
            throw new IllegalArgumentException("Equipment value must be finite and non-negative");
        }
        this.name = name.trim();
        this.employeesCount = employeesCount;
        this.equipmentValue = equipmentValue;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getEmployeesCount() {
        return employeesCount;
    }

    public double getEquipmentValue() {
        return equipmentValue;
    }

    @Override
    public void accept(ClientVisitor visitor) {
        Objects.requireNonNull(visitor, "Client visitor cannot be null");
        visitor.visitCompany(this);
    }
}
