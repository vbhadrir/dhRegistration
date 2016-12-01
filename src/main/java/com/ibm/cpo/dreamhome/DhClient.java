package com.ibm.cpo.dreamhome;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/Client/{id}")
public class DhClient 
{
  @javax.ws.rs.GET
  // Get client record for clientId passed over the HTTP URL
  public String getClientRecord(@PathParam("id") String clientId) 
  {
	String data = dbReadClientRecord(clientId);
	
	return "Get Client ("+clientId+") data is -> " + data ;
  }
  
  
  private String dbReadClientRecord(String id)
  {
    // toDo later
	return "Client Record Data";             
  }    
}
