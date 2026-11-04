package insurance;

import java.util.Objects;

public final class Bank implements Client {
    private final String name;
    private final int branchesCount;
    private final double insuredVaultValue;

    public Bank(String name, int branchesCount, double insuredVaultValue) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Bank name must not be blank");
        }
        if (branchesCount <= 0) {
            throw new IllegalArgumentException("Branches count must be positive");
        }
        if (!Double.isFinite(insuredVaultValue) || insuredVaultValue < 0.0) {
            throw new IllegalArgumentException("Insured vault value must be finite and non-negative");
        }
        this.name = name.trim();
        this.branchesCount = branchesCount;
        this.insuredVaultValue = insuredVaultValue;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getBranchesCount() {
        return branchesCount;
    }

    public double getInsuredVaultValue() {
        return insuredVaultValue;
    }

    @Override
    public void accept(ClientVisitor visitor) {
        Objects.requireNonNull(visitor, "Client visitor cannot be null");
        visitor.visitBank(this);
    }
}
