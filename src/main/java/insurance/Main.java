package insurance;

import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        ClientPortfolio portfolio = new ClientPortfolio(List.of(
            new Resident("Alikhan Residence", 4, "Family Plus"),
            new Bank("Astana Trust Bank", 3, 500_000.0),
            new Company("Qazaq Tech Solutions", 40, 150_000.0),
            new Restaurant("Steppe Bistro", 80, true)
        ));

        InsuranceMessagingVisitor messagingVisitor = new InsuranceMessagingVisitor();
        portfolio.accept(messagingVisitor);

        System.out.println("=== Insurance Policy Offers ===");
        for (String message : messagingVisitor.getMessages()) {
            System.out.println(message);
        }

        PremiumCalculatorVisitor premiumVisitor = new PremiumCalculatorVisitor();
        portfolio.accept(premiumVisitor);

        System.out.printf(
            Locale.ROOT,
            "%n=== Annual Premium Summary ===%nTotal annual premium: $%.2f%n",
            premiumVisitor.getTotalPremium()
        );

        RiskAuditVisitor riskAuditVisitor = new RiskAuditVisitor();
        portfolio.accept(riskAuditVisitor);

        System.out.println("\n=== Underwriting Risk Audit ===");
        for (String flaggedClient : riskAuditVisitor.getFlaggedClients()) {
            System.out.println(flaggedClient);
        }
    }
}
