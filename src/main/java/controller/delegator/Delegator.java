package controller.delegator;

/**
 * Generic contract for delegators that forward work to handler components.
 */
public interface Delegator<H> {
    /**
     * @return handler responsible for executing the delegated work.
     */
    H getHandler();
}
