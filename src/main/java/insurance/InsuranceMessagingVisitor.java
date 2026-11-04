package insurance;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class InsuranceMessagingVisitor implements ClientVisitor {
    private final List<String> messages = new ArrayList<>();

    @Override
    public void visitResident(Resident resident) {
        messages.add(String.format(
            Locale.ROOT,
            "Resident [%s]: sending medical insurance offer (%s tier for %d family members).",
            resident.getName(),
            resident.getCoverageTier(),
            resident.getFamilyMembers()
        ));
    }

    @Override
    public void visitBank(Bank bank) {
        messages.add(String.format(
            Locale.ROOT,
            "Bank [%s]: sending theft and vault insurance offer (%d branches, vault $%.2f).",
            bank.getName(),
            bank.getBranchesCount(),
            bank.getInsuredVaultValue()
        ));
    }

    @Override
    public void visitCompany(Company company) {
        messages.add(String.format(
            Locale.ROOT,
            "Company [%s]: sending equipment and liability insurance offer (%d employees, equipment $%.2f).",
            company.getName(),
            company.getEmployeesCount(),
            company.getEquipmentValue()
        ));
    }

    @Override
    public void visitRestaurant(Restaurant restaurant) {
        String kitchenType = restaurant.hasOpenFlameKitchen() ? "open-flame kitchen" : "electric kitchen";
        messages.add(String.format(
            Locale.ROOT,
            "Restaurant [%s]: sending fire and flood insurance offer (%d seats, %s).",
            restaurant.getName(),
            restaurant.getSeatingCapacity(),
            kitchenType
        ));
    }

    public List<String> getMessages() {
        return List.copyOf(messages);
    }
}
