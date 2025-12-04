package provider.contract;

import java.util.List;

import model.store.Service;

// Read-only shop catalog contract.
public interface ServiceCatalog {
    Service getService(String name);

    List<Service> getHealthServices();

    List<Service> getEnergyServices();
}
