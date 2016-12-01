package com.ibm.cpo.dreamhome;

import java.io.*;
import java.net.*;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

//@Path("/Registration")
@Path("/")
public class DhRegistration 
{
	// If called via this url http://host:port/dhJavaServices 
    // we will simply return the index.jsp	
	@javax.ws.rs.GET
	public Response getRegistrationRecord() 
	{
		Response response = null; 
		 
		try 
		{
			URI uri = new URI("/index.jsp");
			response = Response.temporaryRedirect(uri).build();
		} 
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
		}
		 
		return response;
	 }
	 
     // Add new registration record for the registrationId passed over the HTTP URL
	 @javax.ws.rs.POST 
	 @Path("Registration/{cid:[0-9]*}/{aid:[0-9]*}")
	 public Response updateRegistrationRecord(@PathParam("cid") String cid, @PathParam("aid") String aid) 
	 {
		 // get the input parameters
		 Integer clientId = Integer.valueOf(cid);
		 Integer agentId  = Integer.valueOf(aid);
		 
		 // send a notification by calling the dhNotification REST service.
		 Response res     = sendNotification(clientId, agentId);
		 
		 return res;
	 }
/* 
  // Get registration record for the registrationId passed over the HTTP URL
  @javax.ws.rs.GET
  @Path("/{nid:[0-9]*}")
  public String getRegistrationRecord(@PathParam("nid") String nid) 
  {
	return "Get Registration ("+nid+")";
  }
	
  // Delete registration record for the registrationId passed over the HTTP URL
  @javax.ws.rs.DELETE
  public String deleteRegistrationRecord(@PathParam("id") String regId) 
  {

	return "Delete record (" + regId + ")";
  }

  // Updates registration record for the registrationId passed over the HTTP URL
  @javax.ws.rs.PUT
  public String addRegistrationRecord(@PathParam("id") String regId) 
  {
	  // call the REST notification service
	  
	  
	  return "Add record (" + regId + ")";
  }
*/  

	 // invokes the dhNotification REST service
	 // sends a dh-notification  
	 private static final String notificationHostIP   = "10.40.46.194";
	 private static final String notificationHostName = "notification-dreamhome.ose.cpo.com";
	 private static final String notificationURL      = "http://" + notificationHostName + "/notify"; 
 	 private Response sendNotification(Integer clientId, Integer agentId)
	 {
		 Response res = null;
		 
		 try 
		 {
			String urlName = notificationURL + "?clientId=" + clientId + "&agentId=" + agentId;
			System.out.println("urlName = " + urlName);
			
			URL url = new URL(urlName);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Host", notificationHostName);
			
			// test for good response code
			int rc = conn.getResponseCode();
			if( rc < 400 )
			{ // call was successful, HTTP response code less than 400.
				// establish a reader to read the reply data
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				// read the replyData
				String replyData = ""; // set as empty string
				String lineRead;
				do
				{
					lineRead = br.readLine();
					if(lineRead != null)
					{
						replyData = replyData + lineRead;
					}
				} while (lineRead!=null);
				
				System.out.println("ReplyData = " + replyData );
				
				// create the Response object
				res = Response.ok(replyData).build();
			}
			else
			{ // bad HTTP response code, code is 400 or above
				throw new RuntimeException("ERROR: DhRegistration:sendNotification() Failed : HTTP response code : " + rc);
			}
		 } 
		 catch (Exception e) 
		 {
			e.printStackTrace();
			// re-throw the exception as a runtime exception
			throw new RuntimeException(e);
		 }
		 
		 return(res);
	 }
}
