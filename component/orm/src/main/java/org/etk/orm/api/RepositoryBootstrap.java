package org.etk.orm.api;

import java.net.URL;

import javax.jcr.Repository;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.jcr.RepositoryService;

public class RepositoryBootstrap {
  /** . */
  private Repository repository;

  /** . */
  private String repositoryName;

  public void bootstrap() throws Exception {
    bootstrap(Thread.currentThread().getContextClassLoader().getResource("conf/chromattic/configuration.xml"));
  }

  public void bootstrap(URL url) throws Exception {
    // JCR configuration
    String containerConf = url.toString();
    StandaloneContainer.addConfigurationURL(containerConf);

    //
    String loginConf = Thread.currentThread().getContextClassLoader().getResource("conf/chromattic/login.conf").toString();
    System.setProperty("java.security.auth.login.config", loginConf);

    //
    StandaloneContainer container = StandaloneContainer.getInstance();
    RepositoryService repositoryService = (RepositoryService)container.getComponentInstanceOfType(RepositoryService.class);

    //
    this.repository = repositoryService.getDefaultRepository();
    this.repositoryName = repositoryService.getDefaultRepository().getConfiguration().getName();
  }

  public Repository getRepository() {
    return repository;
  }

  public String getRepositoryName() {
    return repositoryName;
  }
  
  
}
