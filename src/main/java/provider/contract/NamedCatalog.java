package provider.contract;

// A catalog whose entries can also be found by their display name.
// Implemented by AbstractCatalog; contracts that need lookup extend this.
public interface NamedCatalog<T> extends Catalog<T> {
    T findByName(String name);
}
