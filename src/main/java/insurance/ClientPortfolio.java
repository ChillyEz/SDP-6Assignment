package insurance;

import java.util.List;
import java.util.Objects;

public final class ClientPortfolio {
    private final List<Client> clients;

    public ClientPortfolio(List<Client> clients) {
        Objects.requireNonNull(clients, "Client list cannot be null");
        this.clients = List.copyOf(clients);
    }

    public List<Client> getClients() {
        return clients;
    }

    public void accept(ClientVisitor visitor) {
        Objects.requireNonNull(visitor, "Client visitor cannot be null");
        for (Client client : clients) {
            client.accept(visitor);
        }
    }
}
