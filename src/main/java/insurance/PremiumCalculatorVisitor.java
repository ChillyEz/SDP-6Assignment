package insurance;

public final class PremiumCalculatorVisitor implements ClientVisitor {
    private static final double RESIDENT_RATE_PER_MEMBER = 250.0;
    private static final double BANK_RATE_PER_BRANCH = 1500.0;
    private static final double BANK_VAULT_RATE = 0.005;
    private static final double COMPANY_RATE_PER_EMPLOYEE = 100.0;
    private static final double COMPANY_EQUIPMENT_RATE = 0.02;
    private static final double RESTAURANT_RATE_PER_SEAT = 25.0;
    private static final double OPEN_FLAME_SURCHARGE = 800.0;
    private static final double STANDARD_KITCHEN_FEE = 200.0;

    private double totalPremium;

    @Override
    public void visitResident(Resident resident) {
        totalPremium += resident.getFamilyMembers() * RESIDENT_RATE_PER_MEMBER;
    }

    @Override
    public void visitBank(Bank bank) {
        totalPremium += bank.getBranchesCount() * BANK_RATE_PER_BRANCH
            + bank.getInsuredVaultValue() * BANK_VAULT_RATE;
    }

    @Override
    public void visitCompany(Company company) {
        totalPremium += company.getEmployeesCount() * COMPANY_RATE_PER_EMPLOYEE
            + company.getEquipmentValue() * COMPANY_EQUIPMENT_RATE;
    }

    @Override
    public void visitRestaurant(Restaurant restaurant) {
        double kitchenFee = restaurant.hasOpenFlameKitchen()
            ? OPEN_FLAME_SURCHARGE
            : STANDARD_KITCHEN_FEE;
        totalPremium += restaurant.getSeatingCapacity() * RESTAURANT_RATE_PER_SEAT + kitchenFee;
    }

    public double getTotalPremium() {
        return totalPremium;
    }
}
