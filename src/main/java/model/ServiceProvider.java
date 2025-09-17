package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides a centralized catalog of all available services in the game.
 * This decouples the service data from the controllers and views.
 */
public class ServiceProvider {

    private static final Map<String, Service> SERVICES_BY_NAME = new HashMap<>();

    static {
        // To add or modify a service, just change it here.
        addService(new Service("Energy Bar", "+2,500 Energy", 25, 0, 2500));
        addService(new Service("Quick Meal", "+5,000 Energy", 50, 0, 5000));
        addService(new Service("Full Meal", "+12,000 Energy", 100, 0, 12000));
        addService(new Service("Doctor's Visit", "+20 Health", 100, 20, 0));
        addService(new Service("Gourmet Meal", "+25,000 Energy, +5 Health", 200, 5, 25000));
    }

    private static void addService(Service service) {
        SERVICES_BY_NAME.put(service.getName(), service);
    }

    /**
     * Retrieves a service by its unique name.
     * @param name The name of the service.
     * @return The Service object, or null if not found.
     */
    public static Service getService(String name) { return SERVICES_BY_NAME.get(name); }

    public static List<Service> getAvailableServices() {
        return Collections.unmodifiableList(new ArrayList<>(SERVICES_BY_NAME.values()));
    }
}