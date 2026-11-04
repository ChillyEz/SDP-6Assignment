package insurance;

public interface ClientVisitor {
    void visitResident(Resident resident);

    void visitBank(Bank bank);

    void visitCompany(Company company);

    void visitRestaurant(Restaurant restaurant);
}
