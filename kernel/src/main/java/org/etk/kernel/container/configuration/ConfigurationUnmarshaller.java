package org.etk.kernel.container.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collections;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.etk.kernel.container.xml.Configuration;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class ConfigurationUnmarshaller {
  private static final String[] KERNEL_NAMESPACES = Namespaces.getKernelNamespaces();
  
  private class Reporter implements ErrorHandler {
    private final URL url;
    private boolean valid;
    private Reporter(URL url) {
      this.url = url;
      this.valid = true;
    }
    
    @Override
    public void warning(SAXParseException exception) throws SAXException {
      // TODO Auto-generated method stub
      
    }
    @Override
    public void error(SAXParseException exception) throws SAXException {
      // TODO Auto-generated method stub
      
    }
    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
      // TODO Auto-generated method stub
      
    }
  }
  
  private final Set<String> profiles;
  
  public ConfigurationUnmarshaller(Set<String> profiles) {
    this.profiles = profiles;
  }
  
  public ConfigurationUnmarshaller() {
    this.profiles = Collections.emptySet();
  }
  
  /**
   * Returns true if the configuration file is valid according to its schema
   * declaration. If the file does not have any schema declaration, the file
   * will be reported as valid.
   * 
   * @param url the url of the configuration to validate
   * @return true if the configuration file is valid
   * @throws IOException any IOException thrown by using the provided URL
   * @throws NullPointerException if the provided URL is null
   */
  public boolean isValid(final URL url) throws NullPointerException, IOException {
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                         "http://www.w3.org/2001/XMLSchema");
    factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", KERNEL_NAMESPACES);
    factory.setNamespaceAware(true);
    factory.setValidating(true);

    try {
      DocumentBuilder builder = null;
      try {
        builder = AccessController.doPrivileged(new PrivilegedExceptionAction<DocumentBuilder>() {
          public DocumentBuilder run() throws Exception {
            return factory.newDocumentBuilder();
          }
        });
      } catch (PrivilegedActionException pae) {
        Throwable cause = pae.getCause();
        if (cause instanceof ParserConfigurationException) {
          throw (ParserConfigurationException) cause;
        } else if (cause instanceof RuntimeException) {
          throw (RuntimeException) cause;
        } else {
          throw new RuntimeException(cause);
        }
      }

      Reporter reporter = new Reporter(url);
      builder.setErrorHandler(reporter);
      builder.setEntityResolver(Namespaces.resolver);
      /*
      builder.parse(SecurityHelper.doPrivilegedIOExceptionAction(new PrivilegedExceptionAction<InputStream>() {
        public InputStream run() throws Exception {
          return url.openStream();
        }
      }));
      */
      return reporter.valid;
    } catch (ParserConfigurationException e) {
      return false;
    } /*catch (SAXException e) {
      return false;
    }*/
  }

  public Configuration unmarshall(final URL url) throws Exception {
    
    DocumentBuilderFactory factory = null;
    try {
      // With Java 6, it's safer to precise the builder factory class name as it
      // may result:
      // java.lang.AbstractMethodError:
      // org.apache.xerces.dom.DeferredDocumentImpl.getXmlStandalone()Z
      // at
      // com.sun.org.apache.xalan.internal.xsltc.trax.DOM2TO.setDocumentInfo(Unknown
      // Source)
      Method dbfniMethod = DocumentBuilderFactory.class.getMethod("newInstance",
                                                                  String.class,
                                                                  ClassLoader.class);
      factory = (DocumentBuilderFactory) dbfniMethod.invoke(null,
                                                            "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl",
                                                            Thread.currentThread()
                                                                  .getContextClassLoader());
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      if (cause instanceof FactoryConfigurationError) {
        // do nothing and let try to instantiate later
        
      } else {
        // Rethrow
        throw e;
      }
    } catch (NoSuchMethodException e) {
      // Java < 6
    }

    //
    if (factory == null) {
      factory = DocumentBuilderFactory.newInstance();
    }

		//
		factory.setNamespaceAware(true);

		final DocumentBuilderFactory builderFactory = factory;

		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document doc = builder.parse(url.openStream());

      // Filter DOM
      //ProfileDOMFilter filter = new ProfileDOMFilter(profiles);
      //filter.process(doc.getDocumentElement());

      // SAX event stream -> String
      StringWriter buffer = new StringWriter();
      SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
      TransformerHandler hd = tf.newTransformerHandler();
      StreamResult result = new StreamResult(buffer);
      hd.setResult(result);
      Transformer serializer = tf.newTransformer();
      serializer.setOutputProperty(OutputKeys.ENCODING, "UTF8");
      serializer.setOutputProperty(OutputKeys.INDENT, "yes");

      // Transform -> SAX event stream
      SAXResult saxResult = new SAXResult(new NoKernelNamespaceSAXFilter(hd));

      // DOM -> Transform
      serializer.transform(new DOMSource(doc), saxResult);

      // Reuse the parsed document
      String document = buffer.toString();

      // Debug
      //if (log.isTraceEnabled())
      //log.trace("About to parse configuration file " + document);

      //
      IBindingFactory bfact = BindingDirectory.getFactory(Configuration.class);
      IUnmarshallingContext uctx = bfact.createUnmarshallingContext();

      Configuration conf = (Configuration) uctx.unmarshalDocument(new StringReader(document), null);
      
      return conf;
  }

}
