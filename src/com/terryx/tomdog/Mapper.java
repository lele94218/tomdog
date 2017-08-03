package com.terryx.tomdog;


/**
 * Interface defining methods that a parent Container may implement to select
 * a subordinate Container to process a particular Request, optionally
 * modifying the properties of the Request to reflect the selections made.
 * <p>
 * A typical Container may be associated with a single Mapper that processes
 * all requests to that Container, or a Mapper per request protocol that allows
 * the same Container to support multiple protocols at once.
 *
 * @author Craig R. McClanahan
 * @version $Revision: 466595 $ $Date: 2006-10-21 23:24:41 +0100 (Sat, 21 Oct 2006) $
 */

public interface Mapper {


    // ------------------------------------------------------------- Properties


    /**
     * Return the Container with which this Mapper is associated.
     */
    public Container getContainer();


    /**
     * Set the Container with which this Mapper is associated.
     *
     * @param container The newly associated Container
     *
     * @exception IllegalArgumentException if this Container is not
     *  acceptable to this Mapper
     */
    public void setContainer(Container container);


    /**
     * Return the protocol for which this Mapper is responsible.
     */
    public String getProtocol();


    /**
     * Set the protocol for which this Mapper is responsible.
     *
     * @param protocol The newly associated protocol
     */
    public void setProtocol(String protocol);


    // --------------------------------------------------------- Public Methods


    /**
     * Return the child Container that should be used to process this Request,
     * based upon its characteristics.  If no such child Container can be
     * identified, return <code>null</code> instead.
     *
     * @param request Request being processed
     * @param update Update the Request to reflect the mapping selection?
     */
    public Container map(Request request, boolean update);


}

