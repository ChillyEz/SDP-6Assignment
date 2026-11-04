package insurance.tests;

import insurance.Bank;
import insurance.ClientPortfolio;
import insurance.ClientVisitor;
import insurance.Company;
import insurance.InsuranceMessagingVisitor;
import insurance.PremiumCalculatorVisitor;
import insurance.Resident;
import insurance.Restaurant;
import insurance.RiskAuditVisitor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientPortfolioVisitorTest {
    @Test
    void sendsInsuranceMessagesForEachClientType() {
        ClientPortfolio portfolio = samplePortfolio();
        InsuranceMessagingVisitor visitor = new InsuranceMessagingVisitor();

        portfolio.accept(visitor);

        assertEquals(4, visitor.getMessages().size());
        assertEquals(
            "Resident [Alikhan Residence]: sending medical insurance offer (Family Plus tier for 4 family members).",
            visitor.getMessages().get(0)
        );
    }

    @Test
    void calculatesTotalAnnualPremiumAcrossPortfolio() {
        ClientPortfolio portfolio = samplePortfolio();
        PremiumCalculatorVisitor visitor = new PremiumCalculatorVisitor();

        portfolio.accept(visitor);

        assertEquals(17_800.0, visitor.getTotalPremium());
    }

    @Test
    void flagsHighRiskClientsDuringAudit() {
        ClientPortfolio portfolio = samplePortfolio();
        RiskAuditVisitor visitor = new RiskAuditVisitor();

        portfolio.accept(visitor);

        assertEquals(
            List.of(
                "Bank [Astana Trust Bank]: high vault exposure ($500000.00 across 3 branches)",
                "Restaurant [Steppe Bistro]: open-flame kitchen fire hazard (80 seats)"
            ),
            visitor.getFlaggedClients()
        );
    }

    @Test
    void acceptsNewVisitorWithoutChangingElements() {
        ClientPortfolio portfolio = samplePortfolio();
        List<String> visitedNames = new ArrayList<>();
        ClientVisitor nameCollector = new ClientVisitor() {
            @Override
            public void visitResident(Resident resident) {
                visitedNames.add(resident.getName());
            }

            @Override
            public void visitBank(Bank bank) {
                visitedNames.add(bank.getName());
            }

            @Override
            public void visitCompany(Company company) {
                visitedNames.add(company.getName());
            }

            @Override
            public void visitRestaurant(Restaurant restaurant) {
                visitedNames.add(restaurant.getName());
            }
        };

        portfolio.accept(nameCollector);

        assertEquals(
            List.of("Alikhan Residence", "Astana Trust Bank", "Qazaq Tech Solutions", "Steppe Bistro"),
            visitedNames
        );
    }

    @Test
    void rejectsNullVisitorAndBlankClientName() {
        Resident resident = new Resident("Alikhan Residence", 4, "Family Plus");

        assertThrows(NullPointerException.class, () -> resident.accept(null));
        assertThrows(IllegalArgumentException.class, () -> new Bank("   ", 2, 100_000.0));
    }

    private static ClientPortfolio samplePortfolio() {
        return new ClientPortfolio(List.of(
            new Resident("Alikhan Residence", 4, "Family Plus"),
            new Bank("Astana Trust Bank", 3, 500_000.0),
            new Company("Qazaq Tech Solutions", 40, 150_000.0),
            new Restaurant("Steppe Bistro", 80, true)
        ));
    }
}
