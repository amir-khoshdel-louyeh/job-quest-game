package provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.Service;

/**
 * Provides a centralized catalog of all available services in the game.
 * This decouples the service data from the controllers and views.
 */
public class ServiceProvider {

    private static final Map<String, Service> SERVICES_BY_NAME = new HashMap<>();

    static {
        // To add or modify a service, just change it here.
        // Supermarket / Energy Services
        addService(new Service("Water Bottle", "+500 Energy", 5, 0, 500));
        addService(new Service("Apple", "+800 Energy", 8, 0, 800));
        addService(new Service("Banana", "+1,200 Energy", 10, 0, 1200));
        addService(new Service("Granola Bar", "+1,800 Energy", 15, 0, 1800));
        addService(new Service("Coffee", "+3,000 Energy", 20, 0, 3000));
        addService(new Service("Energy Bar", "+2,500 Energy", 25, 0, 2500));
        addService(new Service("Soda Can", "+3,500 Energy", 30, 0, 3500));
        addService(new Service("Quick Meal", "+5,000 Energy", 50, 0, 5000));
        addService(new Service("Sandwich", "+6,000 Energy", 60, 0, 6000));
        addService(new Service("Pizza Slice", "+8,500 Energy", 85, 0, 8500));
        addService(new Service("Pasta Bowl", "+10,000 Energy", 90, 0, 10000));
        addService(new Service("Full Meal", "+12,000 Energy", 100, 0, 12000));
        addService(new Service("Protein Shake", "+15,000 Energy", 125, 0, 15000));
        // Hospital / Health Services
        addService(new Service("Basic Check-up", "+5 Health", 50, 5, 0));
        addService(new Service("Stitches", "+10 Health", 80, 10, 0));
        addService(new Service("Antibiotics Course", "+15 Health", 120, 15, 0));
        addService(new Service("X-Ray & Diagnosis", "+20 Health", 200, 20, 0));
        addService(new Service("Minor Surgery", "+30 Health", 500, 30, 0));
        addService(new Service("Physical Therapy", "+40 Health", 800, 40, 0));
        addService(new Service("Advanced Diagnostics", "+50 Health", 1200, 50, 0));
        addService(new Service("Major Surgery", "+65 Health", 2500, 65, 0));
        addService(new Service("Intensive Care", "+80 Health", 5000, 80, 0));
        addService(new Service("Full Recovery Program", "+100 Health", 10000, 100, 0));
        // Hybrid Service (e.g., for a fancy restaurant, not the hospital)
        addService(new Service("Gourmet Meal", "+25,000 Energy, +5 Health", 200, 5, 25000));
    }

    private static void addService(Service service) {
        SERVICES_BY_NAME.put(service.getName(), service);
    }

    /** Get a service by its name */
    public static Service getService(String name) { return SERVICES_BY_NAME.get(name); }

    public static List<Service> getAvailableServices() {
        return new ArrayList<>(SERVICES_BY_NAME.values());
    }

    /** Get health-related services */
    public static List<Service> getHealthServices() {
        return SERVICES_BY_NAME.values().stream()
                .filter(s -> s.getHealthEffect() > 0 && s.getEnergyEffect() == 0).collect(Collectors.toList());
    }

    /** Get energy-related services */
    public static List<Service> getEnergyServices() {
        return SERVICES_BY_NAME.values().stream()
                .filter(s -> s.getEnergyEffect() > 0 && s.getHealthEffect() == 0)
                .collect(Collectors.toList());
    }
}
