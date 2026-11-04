package insurance;

public interface Client {
    String getName();

    void accept(ClientVisitor visitor);
}
