package insurance;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class RiskAuditVisitor implements ClientVisitor {
    private static final int HIGH_FAMILY_SIZE_THRESHOLD = 5;
    private static final double HIGH_VAULT_VALUE_THRESHOLD = 400_000.0;
    private static final double HIGH_EQUIPMENT_VALUE_THRESHOLD = 200_000.0;

    private final List<String> flaggedClients = new ArrayList<>();

    @Override
    public void visitResident(Resident resident) {
        if (resident.getFamilyMembers() >= HIGH_FAMILY_SIZE_THRESHOLD) {
            flaggedClients.add(String.format(
                Locale.ROOT,
                "Resident [%s]: large household medical coverage (%d members)",
                resident.getName(),
                resident.getFamilyMembers()
            ));
        }
    }

    @Override
    public void visitBank(Bank bank) {
        if (bank.getInsuredVaultValue() >= HIGH_VAULT_VALUE_THRESHOLD) {
            flaggedClients.add(String.format(
                Locale.ROOT,
                "Bank [%s]: high vault exposure ($%.2f across %d branches)",
                bank.getName(),
                bank.getInsuredVaultValue(),
                bank.getBranchesCount()
            ));
        }
    }

    @Override
    public void visitCompany(Company company) {
        if (company.getEquipmentValue() >= HIGH_EQUIPMENT_VALUE_THRESHOLD) {
            flaggedClients.add(String.format(
                Locale.ROOT,
                "Company [%s]: high equipment exposure ($%.2f)",
                company.getName(),
                company.getEquipmentValue()
            ));
        }
    }

    @Override
    public void visitRestaurant(Restaurant restaurant) {
        if (restaurant.hasOpenFlameKitchen()) {
            flaggedClients.add(String.format(
                Locale.ROOT,
                "Restaurant [%s]: open-flame kitchen fire hazard (%d seats)",
                restaurant.getName(),
                restaurant.getSeatingCapacity()
            ));
        }
    }

    public List<String> getFlaggedClients() {
        return List.copyOf(flaggedClients);
    }
}
