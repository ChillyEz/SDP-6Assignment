# Clean Code examples from Chapters 9–13

This insurance portfolio visitor project demonstrates five concrete Clean Code practices. Each example points to observable behavior and responsibilities in the codebase.

## 1. Expressive Design Without Type-Checking Chains — Chapter 12, Emergence (Rule 3: Expresses Intent)

**Evidence:** class and method names state both the domain concept and the pattern role (`Client`, `ClientVisitor`, `InsuranceMessagingVisitor`, `PremiumCalculatorVisitor`, `RiskAuditVisitor`), and double dispatch replaces `instanceof` branching.

```java
// Before (hypothetical procedural branching without Visitor):
if (client instanceof Resident r) {
    return r.getFamilyMembers() * 250.0;
} else if (client instanceof Bank b) {
    return b.getBranchesCount() * 1500.0 + b.getInsuredVaultValue() * 0.005;
}

// After (type-specific forwarding in Bank and polymorphic accept in ClientPortfolio):
@Override
public void accept(ClientVisitor visitor) {
    Objects.requireNonNull(visitor, "Client visitor cannot be null");
    visitor.visitBank(this);
}
```

Standard pattern nomenclature (`ClientVisitor`, `visitResident`, `visitBank`, `visitCompany`, `visitRestaurant`) communicates the design immediately (Chapter 12, *Standard Nomenclature*). Inside `Bank.accept`, `this` has static type `Bank`, so the compiler binds `visitor.visitBank(this)` without runtime casting or `instanceof` checks. Named constants in `PremiumCalculatorVisitor` and `RiskAuditVisitor` (`RESIDENT_RATE_PER_MEMBER`, `HIGH_VAULT_VALUE_THRESHOLD`) also eliminate magic numbers.

## 2. Single Responsibility and No Duplication of Traversal — Chapter 10 (SRP) & Chapter 12 (Rule 2: No Duplication)

**Evidence:** each concrete visitor implements one operation across all four element types, while `ClientPortfolio` owns element storage and iteration.

```java
// ClientPortfolio: centralizes traversal so visitors do not duplicate loops.
public void accept(ClientVisitor visitor) {
    Objects.requireNonNull(visitor, "Client visitor cannot be null");
    for (Client client : clients) {
        client.accept(visitor);
    }
}
```

Changing policy message wording affects `InsuranceMessagingVisitor`, not `PremiumCalculatorVisitor`. Changing underwriting risk thresholds affects `RiskAuditVisitor`, not `Bank` or `Restaurant`. Keeping iteration inside `ClientPortfolio.accept` avoids duplicating collection loops across every visitor.

## 3. Deliberate Encapsulation Contract and Defensive Copies — Chapter 10 (Encapsulation) & Chapter 13 (Use Copies of Data)

**Evidence:** element fields are `private final` and validated in constructors; visitors read only the domain accessors they need, and collection getters return immutable copies via `List.copyOf`.

```java
// ClientPortfolio: defensive copy on construction.
public ClientPortfolio(List<Client> clients) {
    Objects.requireNonNull(clients, "Client list cannot be null");
    this.clients = List.copyOf(clients);
}

// InsuranceMessagingVisitor: returns an unmodifiable snapshot of collected messages.
public List<String> getMessages() {
    return List.copyOf(messages);
}
```

Visitor requires elements to expose enough state for external operations (`getBranchesCount()`, `getInsuredVaultValue()`, `hasOpenFlameKitchen()`). Instead of exposing mutable fields or raw internal lists, the elements remain immutable after construction and the visitors return read-only snapshots.

## 4. Scoped Mutable State and Independent Visitor Lifecycles — Chapter 13, Concurrency Defense Principles

**Evidence:** elements (`Resident`, `Bank`, `Company`, `Restaurant`) and `ClientPortfolio` are immutable, while mutable accumulation state (`totalPremium`, `messages`, `flaggedClients`) is confined to a single visitor instance per traversal.

```java
// Main: each operation uses its own visitor instance over the same immutable portfolio.
InsuranceMessagingVisitor messagingVisitor = new InsuranceMessagingVisitor();
portfolio.accept(messagingVisitor);

PremiumCalculatorVisitor premiumVisitor = new PremiumCalculatorVisitor();
portfolio.accept(premiumVisitor);
```

Chapter 13 emphasizes *Limit the Scope of Data* and *Threads Should Be as Independent as Possible*, matching Lecture 9's rule that *mutable visitor state needs a lifecycle*. Because the element structure never mutates during traversal, separate traversals (or separate threads) can safely inspect the same `ClientPortfolio` as long as each traversal uses its own fresh visitor instance.

## 5. Verifiable Design and Demonstrated Extensibility — Chapter 12 (Rule 1: Runs All the Tests) & Chapter 9 (F.I.R.S.T.)

**Evidence:** adding a new operation requires only a new `ClientVisitor` implementation, verified by simple, focused JUnit 5 tests in `ClientPortfolioVisitorTest`.

```java
ClientVisitor nameCollector = new ClientVisitor() {
    @Override
    public void visitResident(Resident resident) { visitedNames.add(resident.getName()); }

    @Override
    public void visitBank(Bank bank) { visitedNames.add(bank.getName()); }

    @Override
    public void visitCompany(Company company) { visitedNames.add(company.getName()); }

    @Override
    public void visitRestaurant(Restaurant restaurant) { visitedNames.add(restaurant.getName()); }
};
portfolio.accept(nameCollector);
```

`RiskAuditVisitor` in production code and `nameCollector` in `ClientPortfolioVisitorTest` add new operations without editing `Resident`, `Bank`, `Company`, `Restaurant`, or `ClientPortfolio`.

The five tests in `ClientPortfolioVisitorTest` follow **F.I.R.S.T.** properties:
- **Fast:** in-memory domain objects with zero file or network I/O.
- **Independent:** each test creates its own portfolio and visitor instance.
- **Repeatable:** deterministic inputs and `Locale.ROOT` formatting.
- **Self-validating:** JUnit 5 assertions check exact values, list contents, and thrown exceptions.
