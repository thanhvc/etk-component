package org.exoplatform.container.test;

import org.exoplatform.container.component.ExecutionContext;
import org.exoplatform.container.component.ExecutionUnit;
import org.exoplatform.test.BasicTestCase;

/**
 * Created by the Exo Development team. Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */
public class TestChaining extends BasicTestCase {
  public void testChain() throws Throwable {
    TextExecutionUnit chain = new TextExecutionUnit("unit-1");
    chain.addExecutionUnit(new TextExecutionUnit("unit-2"));
    chain.addExecutionUnit(new TextExecutionUnit("unit-3"));
    TextExcutionContext context = new TextExcutionContext("context");
    context.setCurrentExecutionUnit(chain);
    context.execute();
  }

  static class TextExecutionUnit extends ExecutionUnit {
    String name_;

    public TextExecutionUnit(String name) {
      name_ = name;
    }

    public Object execute(ExecutionContext context) throws Throwable {
      System.out.println("start execution: " + name_);
      Object result = context.executeNextUnit();
      System.out.println("end execution: " + name_);
      return result;
    }
  }

  static class TextExcutionContext extends ExecutionContext {
    String name_;

    public TextExcutionContext(String name) {
      name_ = name;
    }
  }
}
