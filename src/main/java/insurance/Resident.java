package insurance;

import java.util.Objects;

public final class Resident implements Client {
    private final String name;
    private final int familyMembers;
    private final String coverageTier;

    public Resident(String name, int familyMembers, String coverageTier) {
        this.name = requireNonBlank(name, "Resident name must not be blank");
        if (familyMembers <= 0) {
            throw new IllegalArgumentException("Family members must be positive");
        }
        this.familyMembers = familyMembers;
        this.coverageTier = requireNonBlank(coverageTier, "Coverage tier must not be blank");
    }

    @Override
    public String getName() {
        return name;
    }

    public int getFamilyMembers() {
        return familyMembers;
    }

    public String getCoverageTier() {
        return coverageTier;
    }

    @Override
    public void accept(ClientVisitor visitor) {
        Objects.requireNonNull(visitor, "Client visitor cannot be null");
        visitor.visitResident(this);
    }

    private static String requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
