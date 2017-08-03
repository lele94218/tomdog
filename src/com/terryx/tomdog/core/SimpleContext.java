package com.terryx.tomdog.core;

import com.terryx.tomdog.*;

import javax.naming.directory.DirContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author taoranxue on 8/3/17 3:38 PM.
 */
public class SimpleContext implements Context, Pipeline {


    protected HashMap children = new HashMap();
    protected Loader loader = null;
    protected SimplePipeline pipeline = new SimplePipeline(this);
    protected HashMap servletMappings = new HashMap();
    protected Mapper mapper = null;
    protected HashMap mappers = new HashMap();
    private Container parent = null;

    public SimpleContext() {
        pipeline.setBasic(new SimpleContextValve());
    }

    public void addMapper(Mapper mapper) {
        // this method is adopted from addMapper in ContainerBase
        // the first mapper added becomes the default mapper
        mapper.setContainer((Container) this);      // May throw IAE
        this.mapper = mapper;
        synchronized (mappers) {
            if (mappers.get(mapper.getProtocol()) != null)
                throw new IllegalArgumentException("addMapper:  Protocol '" +
                        mapper.getProtocol() + "' is not unique");
            mapper.setContainer((Container) this);      // May throw IAE
            mappers.put(mapper.getProtocol(), mapper);
            if (mappers.size() == 1)
                this.mapper = mapper;
            else
                this.mapper = null;
        }
    }

    public Mapper findMapper(String protocol) {
        // the default mapper will always be returned, if any,
        // regardless the value of protocol
        if (mapper != null)
            return (mapper);
        else
            synchronized (mappers) {
                return ((Mapper) mappers.get(protocol));
            }
    }

    @Override
    public Mapper[] findMappers() {
        return new Mapper[0];
    }


    // method implementations of Pipeline
    public Valve getBasic() {
        return pipeline.getBasic();
    }

    public void setBasic(Valve valve) {
        pipeline.setBasic(valve);
    }

    public synchronized void addValve(Valve valve) {
        pipeline.addValve(valve);
    }

    public Valve[] getValves() {
        return pipeline.getValves();
    }

    public void removeValve(Valve valve) {
        pipeline.removeValve(valve);
    }

    @Override
    public Object[] getApplicationListeners() {
        return new Object[0];
    }

    @Override
    public void setApplicationListeners(Object[] listeners) {

    }

    @Override
    public boolean getAvailable() {
        return false;
    }

    @Override
    public void setAvailable(boolean available) {

    }

    @Override
    public boolean getConfigured() {
        return false;
    }

    @Override
    public void setConfigured(boolean configured) {

    }

    @Override
    public boolean getCookies() {
        return false;
    }

    @Override
    public void setCookies(boolean cookies) {

    }

    @Override
    public boolean getCrossContext() {
        return false;
    }

    @Override
    public void setCrossContext(boolean crossContext) {

    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public void setDisplayName(String displayName) {

    }

    @Override
    public boolean getDistributable() {
        return false;
    }

    @Override
    public void setDistributable(boolean distributable) {

    }

    @Override
    public String getDocBase() {
        return null;
    }

    @Override
    public void setDocBase(String docBase) {

    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public void setPath(String path) {

    }

    @Override
    public String getPublicId() {
        return null;
    }

    @Override
    public void setPublicId(String publicId) {

    }

    @Override
    public boolean getReloadable() {
        return false;
    }

    @Override
    public void setReloadable(boolean reloadable) {

    }

    @Override
    public boolean getOverride() {
        return false;
    }

    @Override
    public void setOverride(boolean override) {

    }

    @Override
    public boolean getPrivileged() {
        return false;
    }

    @Override
    public void setPrivileged(boolean privileged) {

    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public int getSessionTimeout() {
        return 0;
    }

    @Override
    public void setSessionTimeout(int timeout) {

    }

    @Override
    public String getWrapperClass() {
        return null;
    }

    @Override
    public void setWrapperClass(String wrapperClass) {

    }

    @Override
    public void addApplicationListener(String listener) {

    }

    @Override
    public void addInstanceListener(String listener) {

    }

    @Override
    public void addMimeMapping(String extension, String mimeType) {

    }

    @Override
    public void addParameter(String name, String value) {

    }

    @Override
    public void addResourceEnvRef(String name, String type) {

    }

    @Override
    public void addRoleMapping(String role, String link) {

    }

    @Override
    public void addSecurityRole(String role) {

    }

    @Override
    public void addServletMapping(String pattern, String name) {
        synchronized (servletMappings) {
            servletMappings.put(pattern, name);
        }
    }

    @Override
    public void addTaglib(String uri, String location) {

    }

    @Override
    public void addWelcomeFile(String name) {

    }

    @Override
    public void addWrapperLifecycle(String listener) {

    }

    @Override
    public void addWrapperListener(String listener) {

    }

    @Override
    public Wrapper createWrapper() {
        return null;
    }

    @Override
    public String[] findApplicationListeners() {
        return new String[0];
    }

    @Override
    public String[] findInstanceListeners() {
        return new String[0];
    }

    @Override
    public String findMimeMapping(String extension) {
        return null;
    }

    @Override
    public String[] findMimeMappings() {
        return new String[0];
    }

    @Override
    public String findParameter(String name) {
        return null;
    }

    @Override
    public String[] findParameters() {
        return new String[0];
    }

    @Override
    public String findResourceEnvRef(String name) {
        return null;
    }

    @Override
    public String[] findResourceEnvRefs() {
        return new String[0];
    }

    @Override
    public String findRoleMapping(String role) {
        return null;
    }

    @Override
    public boolean findSecurityRole(String role) {
        return false;
    }

    @Override
    public String[] findSecurityRoles() {
        return new String[0];
    }

    @Override
    public String findServletMapping(String pattern) {
        synchronized (servletMappings) {
            return ((String) servletMappings.get(pattern));
        }
    }

    @Override
    public String[] findServletMappings() {
        return new String[0];
    }

    @Override
    public String findStatusPage(int status) {
        return null;
    }

    @Override
    public int[] findStatusPages() {
        return new int[0];
    }

    @Override
    public String findTaglib(String uri) {
        return null;
    }

    @Override
    public String[] findTaglibs() {
        return new String[0];
    }

    @Override
    public boolean findWelcomeFile(String name) {
        return false;
    }

    @Override
    public String[] findWelcomeFiles() {
        return new String[0];
    }

    @Override
    public String[] findWrapperLifecycles() {
        return new String[0];
    }

    @Override
    public String[] findWrapperListeners() {
        return new String[0];
    }

    @Override
    public void reload() {

    }

    @Override
    public void removeApplicationListener(String listener) {

    }

    @Override
    public void removeApplicationParameter(String name) {

    }

    @Override
    public void removeEjb(String name) {

    }

    @Override
    public void removeEnvironment(String name) {

    }

    @Override
    public void removeInstanceListener(String listener) {

    }

    @Override
    public void removeLocalEjb(String name) {

    }

    @Override
    public void removeMimeMapping(String extension) {

    }

    @Override
    public void removeParameter(String name) {

    }

    @Override
    public void removeResource(String name) {

    }

    @Override
    public void removeResourceEnvRef(String name) {

    }

    @Override
    public void removeResourceLink(String name) {

    }

    @Override
    public void removeRoleMapping(String role) {

    }

    @Override
    public void removeSecurityRole(String role) {

    }

    @Override
    public void removeServletMapping(String pattern) {

    }

    @Override
    public void removeTaglib(String uri) {

    }

    @Override
    public void removeWelcomeFile(String name) {

    }

    @Override
    public void removeWrapperLifecycle(String listener) {

    }

    @Override
    public void removeWrapperListener(String listener) {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public Loader getLoader() {
        if (loader != null)
            return (loader);
        if (parent != null)
            return (parent.getLoader());
        return (null);
    }

    @Override
    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public Container getParent() {
        return null;
    }

    @Override
    public void setParent(Container container) {

    }

    @Override
    public ClassLoader getParentClassLoader() {
        return null;
    }

    @Override
    public void setParentClassLoader(ClassLoader parent) {

    }

    @Override
    public DirContext getResources() {
        return null;
    }

    @Override
    public void setResources(DirContext resources) {

    }

    @Override
    public void addChild(Container child) {
        child.setParent((Container) this);
        children.put(child.getName(), child);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }

    @Override
    public Container findChild(String name) {
        if (name == null)
            return (null);
        synchronized (children) {       // Required by post-start changes
            return ((Container) children.get(name));
        }
    }

    @Override
    public Container[] findChildren() {
        synchronized (children) {
            Container results[] = new Container[children.size()];
            return ((Container[]) children.values().toArray(results));
        }
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        pipeline.invoke(request, response);
    }

    @Override
    public Container map(Request request, boolean update) {
        //this method is taken from the map method in org.apache.cataline.core.ContainerBase
        //the findMapper method always returns the default mapper, if any, regardless the
        //request's protocol
        Mapper mapper = findMapper(request.getRequest().getProtocol());
        if (mapper == null)
            return (null);

        // Use this Mapper to perform this mapping
        return (mapper.map(request, update));
    }

    @Override
    public void removeChild(Container child) {

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }
}
