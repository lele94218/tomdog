package com.terryx.tomdog.core;

import com.terryx.tomdog.*;

import javax.naming.directory.DirContext;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import java.beans.PropertyChangeListener;
import java.io.IOException;

/**
 * @author taoranxue on 7/25/17 10:43 AM.
 */
public class SimpleWrapper implements Wrapper, Pipeline {

    // the servlet instance
    private Servlet instance = null;
    private String servletClass;
    private Loader loader;
    private String name;
    private SimplePipeline pipeline = new SimplePipeline(this);
    protected Container parent = null;


    public SimpleWrapper() {
        pipeline.setBasic(new SimpleWrapperValve());
    }

    @Override
    public Valve getBasic() {
        return pipeline.getBasic();
    }

    @Override
    public void setBasic(Valve valve) {
        pipeline.setBasic(valve);
    }

    @Override
    public synchronized void addValve(Valve valve) {
        pipeline.addValve(valve);
    }

    @Override
    public Valve[] getValves() {
        return new Valve[0];
    }

    @Override
    public void removeValve(Valve valve) {

    }

    @Override
    public long getAvailable() {
        return 0;
    }

    @Override
    public void setAvailable(long available) {

    }

    @Override
    public String getJspFile() {
        return null;
    }

    @Override
    public void setJspFile(String jspFile) {

    }

    @Override
    public int getLoadOnStartup() {
        return 0;
    }

    @Override
    public void setLoadOnStartup(int value) {

    }

    @Override
    public String getRunAs() {
        return null;
    }

    @Override
    public void setRunAs(String runAs) {

    }

    @Override
    public String getServletClass() {
        return null;
    }

    @Override
    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }

    @Override
    public boolean isUnavailable() {
        return false;
    }

    @Override
    public void addInitParameter(String name, String value) {

    }

    @Override
    public void addSecurityReference(String name, String link) {

    }

    @Override
    public Servlet allocate() throws ServletException {
        // Load and initialize our instance if necessary
        if (instance == null) {
            try {
                instance = loadServlet();
            } catch (ServletException e) {
                throw e;
            } catch (Throwable e) {
                throw new ServletException("Cannot allocate a servlet instance", e);
            }
        }
        return instance;
    }

    private Servlet loadServlet() throws ServletException {
        if (instance != null)
            return instance;

        Servlet servlet = null;
        String actualClass = servletClass;
        if (actualClass == null) {
            throw new ServletException("servlet class has not been specified");
        }

        Loader loader = getLoader();
        // Acquire an instance of the class loader to be used
        if (loader == null) {
            throw new ServletException("No loader.");
        }
        ClassLoader classLoader = loader.getClassLoader();

        // Load the specified servlet class from the appropriate class loader
        Class classClass = null;
        try {
            if (classLoader != null) {
                classClass = classLoader.loadClass(actualClass);
            }
        } catch (ClassNotFoundException e) {
            throw new ServletException("Servlet class not found");
        }
        // Instantiate and initialize an instance of the servlet class itself
        try {
            servlet = (Servlet) classClass.newInstance();
        } catch (Throwable e) {
            throw new ServletException("Failed to instantiate servlet");
        }

        // Call the initialization method of this servlet
        try {
            servlet.init(null);
        } catch (Throwable f) {
            throw new ServletException("Failed initialize servlet.");
        }
        return servlet;
    }

    @Override
    public void deallocate(Servlet servlet) throws ServletException {

    }

    @Override
    public String findInitParameter(String name) {
        return null;
    }

    @Override
    public String[] findInitParameters() {
        return new String[0];
    }

    @Override
    public String findSecurityReference(String name) {
        return null;
    }

    @Override
    public String[] findSecurityReferences() {
        return new String[0];
    }

    @Override
    public void load() throws ServletException {
        instance = loadServlet();
    }

    @Override
    public void removeInitParameter(String name) {

    }

    @Override
    public void removeSecurityReference(String name) {

    }

    @Override
    public void unavailable(UnavailableException unavailable) {

    }

    @Override
    public void unload() throws ServletException {

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
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Container getParent() {
        return this.parent;
    }

    @Override
    public void setParent(Container container) {
        this.parent = container;
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

    }

    @Override
    public void addMapper(Mapper mapper) {

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }

    @Override
    public Container findChild(String name) {
        return null;
    }

    @Override
    public Container[] findChildren() {
        return new Container[0];
    }

    @Override
    public Mapper findMapper(String protocol) {
        return null;
    }

    @Override
    public Mapper[] findMappers() {
        return new Mapper[0];
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
//        System.out.println("invoke");
        pipeline.invoke(request, response);
    }

    @Override
    public Container map(Request request, boolean update) {
        return null;
    }

    @Override
    public void removeChild(Container child) {

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }
}
