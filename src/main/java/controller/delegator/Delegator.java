package controller.delegator;

/**
 * Generic contract for delegators that forward work to handler components.
 */
public interface Delegator<H> {
    
    H getHandler();
}
