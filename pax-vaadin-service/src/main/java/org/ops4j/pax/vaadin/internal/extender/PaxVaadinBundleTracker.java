/*
 * Copyright 2012 Achim Nierbeck.
 *
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.vaadin.internal.extender;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import org.ops4j.pax.vaadin.AbstractApplicationFactory;
import org.ops4j.pax.vaadin.VaadinResourceService;
import org.ops4j.pax.vaadin.internal.servlet.VaadinOSGiServlet;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.BundleTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.UI;

public class PaxVaadinBundleTracker extends BundleTracker {
    
    private static class ApplicationFactoryWrapper extends AbstractApplicationFactory{
        
        private UI m_application;

        public ApplicationFactoryWrapper(UI application) {
            m_application = application;
        }
        
        @Override
        public Class<? extends UI> getUIClass() {
            return m_application.getClass();
        }

        @Override
        public UI getUI() {
            return m_application;
        }

        @Override
        public Map<String, String> getAdditionalHeaders() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getAdditionalHeadContent() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getAdditionalBodyStartContent() {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
    
	public static final String ALIAS = "alias";

	private static final String VAADIN_PATH = "/VAADIN";

	private final Logger logger = LoggerFactory
			.getLogger(PaxVaadinBundleTracker.class.getName());

	private final Map<Bundle, ServiceRegistration> registeredServlets = new HashMap<Bundle, ServiceRegistration>();

	public PaxVaadinBundleTracker(BundleContext context) {
		super(context, Bundle.ACTIVE, null);
	}

	@Override
	public Object addingBundle(Bundle bundle, BundleEvent event) {

		if (isApplicationBundle(bundle)) {
			logger.debug("found a vaadin-app bundle: {}", bundle);
			String applicationClass = (String) bundle.getHeaders().get(
					org.ops4j.pax.vaadin.Constants.VAADIN_APPLICATION);
			String alias = (String) bundle.getHeaders().get("Vaadin-Alias");
			UI application = null;
			try {
				Class<?> appClazz = bundle.loadClass(applicationClass);

				Constructor<?>[] ctors = appClazz.getDeclaredConstructors();
				Constructor<?> ctor = null;
				for (int i = 0; i < ctors.length; i++) {
					ctor = ctors[i];
					if (ctor.getGenericParameterTypes().length == 0)
						break;
				}
				ctor.setAccessible(true);
				application = (UI) ctor.newInstance();

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			final String widgetset = findWidgetset(bundle);

			if (application != null) {
			    VaadinOSGiServlet servlet = new VaadinOSGiServlet(new ApplicationFactoryWrapper(application));
				//VaadinApplicationServlet servlet = new VaadinApplicationServlet(application);

				Map<String, Object> props = new Hashtable<String, Object>();
				props.put(ALIAS, alias);

				if (widgetset != null) {
					props.put("widgetset", widgetset);
				}

				ServiceRegistration registeredServlet = bundle
						.getBundleContext().registerService(
								HttpServlet.class.getName(), servlet,
								(Dictionary<?,?>) props);

				registeredServlets.put(bundle, registeredServlet);
			}

		}

		if (isThemeBundle(bundle)) {
			logger.debug("found a vaadin-resource bundle: {}", bundle);
			// TODO do VAADIN Themese handling
			ServiceReference serviceReference = bundle.getBundleContext()
					.getServiceReference(VaadinResourceService.class.getName());
			VaadinResourceService service = (VaadinResourceService) bundle
					.getBundleContext().getService(serviceReference);
			service.addResources(bundle);
		}

		return super.addingBundle(bundle, event);
	}

	@SuppressWarnings("unchecked")
	protected String findWidgetset(Bundle bundle) {
		Enumeration<URL> widgetEntries = bundle.findEntries("", "*.gwt.xml", true);
//		Enumeration widgetEntries = bundle.getEntryPaths(VAADIN_PATH);
		if (widgetEntries == null || !widgetEntries.hasMoreElements())
			return null;

		/*
		while (widgetEntries.hasMoreElements()) {

			String path = (String) widgetEntries.nextElement();

			if (path.indexOf("widgetsets") != -1) {
				Enumeration entryPaths = bundle.getEntryPaths(path);
				while (entryPaths.hasMoreElements()){
					path = (String) entryPaths.nextElement();
					if (path.contains(".")) {
						if (path.endsWith("/")) {
							path = path.substring(0, path.length() - 1);
						}
						path = path.substring(path.lastIndexOf("/")+1);
						return path;
					}
				}
			}
		}
		*/
		URL widgetUrl = widgetEntries.nextElement();
		String path = widgetUrl.getPath();
		path = path.substring(1,path.length()-8);
		path = path.replace("/", ".");
		return path;
	}

	@Override
	public void removedBundle(Bundle bundle, BundleEvent event, Object object) {

		ServiceRegistration registeredServlet = registeredServlets.get(bundle);
		if (registeredServlet != null)
			registeredServlet.unregister();

		super.removedBundle(bundle, event, object);
	}

	private boolean isApplicationBundle(Bundle bundle) {
		if (!isVaadinBundle(bundle))
			return false;

		String applicationClass = (String) bundle.getHeaders().get(
				org.ops4j.pax.vaadin.Constants.VAADIN_APPLICATION);

		if (applicationClass != null && !applicationClass.isEmpty())
			return true;

		return false;
	}

	private boolean isThemeBundle(Bundle bundle) {
		if ("com.vaadin".equals(bundle.getSymbolicName()))
			return false;

		Enumeration<?> vaadinPaths = bundle.getEntryPaths(VAADIN_PATH);
		if (vaadinPaths == null || !vaadinPaths.hasMoreElements())
			return false;

		return true;
	}

	private boolean isVaadinBundle(Bundle bundle) {
		String importedPackages = (String) bundle.getHeaders().get(
				Constants.IMPORT_PACKAGE);
		if (importedPackages == null) {
			return false;
		}

		if (importedPackages.contains("com.vaadin")) {
			return true;
		}

		return false;
	}

	/*
	 * private class VaadinServletConfig implements ServletConfig {
	 *
	 * private HttpContext httpContext; private String widgetSets;
	 *
	 * public VaadinServletConfig(String widgetSets, HttpContext httpContext) {
	 * this.widgetSets = widgetSets; this.httpContext = httpContext; }
	 *
	 * @Override public String getServletName() { return null; }
	 *
	 * @Override public ServletContext getServletContext() { return httpContext;
	 * }
	 *
	 * @Override public Enumeration getInitParameterNames() { // TODO
	 * Auto-generated method stub Vector<String> initParamNames = new
	 * Vector<String>(); initParamNames.add("Widgetset"); return
	 * initParamNames.elements(); }
	 *
	 * @Override public String getInitParameter(String name) { if
	 * ("Widgetset".equalsIgnoreCase(name)) return widgetSets; return null; } }
	 */

}
