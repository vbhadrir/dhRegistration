package com.ibm.cpo.dreamhome;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class DhApplication extends Application 
{
  @Override
  public Set<Class<?>> getClasses() 
  {
    Set<Class<?>> classes = new HashSet<Class<?>>();
    classes.add(DhRegistration.class);
    return classes;
  }

}
