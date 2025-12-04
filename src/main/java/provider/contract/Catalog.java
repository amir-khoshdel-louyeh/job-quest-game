package provider.contract;

import java.util.List;

// The one read every catalog has: its full contents, as a defensive copy.
// NamedCatalog adds lookup on top of this.
public interface Catalog<T> {
    List<T> getAll();
}
