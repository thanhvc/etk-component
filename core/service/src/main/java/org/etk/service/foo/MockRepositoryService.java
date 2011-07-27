package org.etk.service.foo;

public class MockRepositoryService {

  private static ThreadLocal<String> currentRepository = new ThreadLocal<String>();
  
  public MockRepositoryService() {
    currentRepository.set("default");
  }
  
  public static void switchRepo(String repoName) {
    currentRepository.set(repoName);
  }
  
  public static String getCurrentRepo() {
    return currentRepository.get();
  }
  
  
}
