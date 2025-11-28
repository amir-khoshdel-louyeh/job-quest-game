package provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.store.Service;

// Holds all food and hospital services the player can buy.
// Extends AbstractCatalog plus a name map for fast lookup.
// Injected into EconomyController.
public class ServiceProvider extends AbstractCatalog<Service> {

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
        services.add(new Service("Water Bottle", "+500 Energy", 5, 0, 500));
        services.add(new Service("Apple", "+800 Energy", 8, 0, 800));
        services.add(new Service("Banana", "+1,200 Energy", 10, 0, 1200));
        services.add(new Service("Granola Bar", "+1,800 Energy", 15, 0, 1800));
        services.add(new Service("Coffee", "+3,000 Energy", 20, 0, 3000));
        services.add(new Service("Energy Bar", "+2,500 Energy", 25, 0, 2500));
        services.add(new Service("Soda Can", "+3,500 Energy", 30, 0, 3500));
        services.add(new Service("Quick Meal", "+5,000 Energy", 50, 0, 5000));
        services.add(new Service("Sandwich", "+6,000 Energy", 60, 0, 6000));
        services.add(new Service("Pizza Slice", "+8,500 Energy", 85, 0, 8500));
        services.add(new Service("Pasta Bowl", "+10,000 Energy", 90, 0, 10000));
        services.add(new Service("Full Meal", "+12,000 Energy", 100, 0, 12000));
        services.add(new Service("Protein Shake", "+15,000 Energy", 125, 0, 15000));
        services.add(new Service("Basic Check-up", "+5 Health", 50, 5, 0));
        services.add(new Service("Stitches", "+10 Health", 80, 10, 0));
        services.add(new Service("Antibiotics Course", "+15 Health", 120, 15, 0));
        services.add(new Service("X-Ray & Diagnosis", "+20 Health", 200, 20, 0));
        services.add(new Service("Minor Surgery", "+30 Health", 500, 30, 0));
        services.add(new Service("Physical Therapy", "+40 Health", 800, 40, 0));
        services.add(new Service("Advanced Diagnostics", "+50 Health", 1200, 50, 0));
        services.add(new Service("Major Surgery", "+65 Health", 2500, 65, 0));
        services.add(new Service("Intensive Care", "+80 Health", 5000, 80, 0));
        services.add(new Service("Full Recovery Program", "+100 Health", 10000, 100, 0));
        services.add(new Service("Gourmet Meal", "+25,000 Energy, +5 Health", 200, 5, 25000));
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
        ArrayList<Service> result = new ArrayList<Service>();
        List<Service> all = snapshot();
        for (int i = 0; i < all.size(); i++) {
            Service s = all.get(i);
            if (s.isHealthService()) {
                result.add(s);
            }
        }
        return result;
    }

    // Returns only energy services.
    public List<Service> getEnergyServices() {
        ArrayList<Service> result = new ArrayList<Service>();
        List<Service> all = snapshot();
        for (int i = 0; i < all.size(); i++) {
            Service s = all.get(i);
            if (s.isEnergyService()) {
                result.add(s);
            }
        }
        return result;
    }
}
