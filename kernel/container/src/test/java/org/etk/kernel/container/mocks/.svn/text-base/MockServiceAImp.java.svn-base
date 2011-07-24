/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SAS
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Sep 15, 2003
 * Time: 2:33:02 PM
 */
package org.exoplatform.mocks;

public class MockServiceAImp implements MockServiceA {

  MockServiceB service;

  public MockServiceAImp(MockServiceB service) {
    System.out.println("MockServiceAImp constructor");
    this.service = service;
  }

  public void methodServiceA() {
    System.out.println("Method service A");
    service.methodServiceB();
  }

  public void testArguments(String test, int num) {
    System.out.println("testArguments, test = " + test + ", num = " + num);
  }
}
