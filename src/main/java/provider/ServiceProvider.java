package provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.store.Service;
import utils.Lists;

// Holds all food and hospital services the player can buy.
// Extends AbstractCatalog plus a name map for fast lookup.
// Injected into EconomyController.
public final class ServiceProvider extends AbstractCatalog<Service> implements ServiceCatalog {

    // map name -> service, for quick lookup
    private final HashMap<String, Service> servicesByName = new HashMap<String, Service>();

    // ----- Construction -----
    public ServiceProvider() {
        this(buildDefaultCatalog());
    }

    // Custom catalog for tests.
    public ServiceProvider(List<Service> services) {
        super(services);
        List<Service> all = snapshot();
        for (int i = 0; i < all.size(); i++) {
            addService(all.get(i));
        }
    }

    private static List<Service> buildDefaultCatalog() {
        ArrayList<Service> services = new ArrayList<Service>();
        services.add(new Service("Water Bottle", 5, 0, 500));
        services.add(new Service("Apple", 8, 0, 800));
        services.add(new Service("Banana", 10, 0, 1200));
        services.add(new Service("Granola Bar", 15, 0, 1800));
        services.add(new Service("Coffee", 20, 0, 3000));
        services.add(new Service("Energy Bar", 25, 0, 2500));
        services.add(new Service("Soda Can", 30, 0, 3500));
        services.add(new Service("Quick Meal", 50, 0, 5000));
        services.add(new Service("Sandwich", 60, 0, 6000));
        services.add(new Service("Pizza Slice", 85, 0, 8500));
        services.add(new Service("Pasta Bowl", 90, 0, 10000));
        services.add(new Service("Full Meal", 100, 0, 12000));
        services.add(new Service("Protein Shake", 125, 0, 15000));
        services.add(new Service("Basic Check-up", 50, 5, 0));
        services.add(new Service("Stitches", 80, 10, 0));
        services.add(new Service("Antibiotics Course", 120, 15, 0));
        services.add(new Service("X-Ray & Diagnosis", 200, 20, 0));
        services.add(new Service("Minor Surgery", 500, 30, 0));
        services.add(new Service("Physical Therapy", 800, 40, 0));
        services.add(new Service("Advanced Diagnostics", 1200, 50, 0));
        services.add(new Service("Major Surgery", 2500, 65, 0));
        services.add(new Service("Intensive Care", 5000, 80, 0));
        services.add(new Service("Full Recovery Program", 10000, 100, 0));
        services.add(new Service("Gourmet Meal", 200, 5, 25000));
        return services;
    }

    private void addService(Service service) {
        if (service != null) {
            servicesByName.put(service.getName(), service);
        }
    }

    // ----- Queries -----
    public Service getService(String name) {
        return servicesByName.get(name);
    }

    // Returns only health services.
    public List<Service> getHealthServices() {
        return Lists.filter(snapshot(), Service::isHealthService);
    }

    // Returns only energy services.
    public List<Service> getEnergyServices() {
        return Lists.filter(snapshot(), Service::isEnergyService);
    }
}
