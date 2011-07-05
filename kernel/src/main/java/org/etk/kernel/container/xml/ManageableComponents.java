package org.etk.kernel.container.xml;

import java.util.ArrayList;

public class ManageableComponents
{

   private ArrayList componentsType = new ArrayList(3);

   public ArrayList getComponentsType()
   {
      return componentsType;
   }

   public void setComponentsType(ArrayList values)
   {
      this.componentsType = values;
   }
}
