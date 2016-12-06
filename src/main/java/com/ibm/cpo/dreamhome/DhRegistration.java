package com.ibm.cpo.dreamhome;

import java.io.*;
import java.net.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

@Path("/Registration")
public class DhRegistration 
{
	// Add new registration record for the registrationId passed over the HTTP URL
	// data will be in JSON format in the body of the request as follows
	// example body data: { clientId:1002, agentId:1003 }
	@javax.ws.rs.POST
	@Path("") // clientId and agentId will be sent in the body of the request
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response updateRegistrationRecord( String reqBody )
	{
		Response res 		= null;
		Integer clientId 	= null;
		Integer agentId  	= null;
		
		try
		{
			// convert the request body string into a JSON object
			JSONObject json = new JSONObject(reqBody);
					
			// get the input parameters from the json  
			clientId = Integer.valueOf( json.getInt("clientId") ); 
			agentId  = Integer.valueOf( json.getInt("agentId") ); 
System.out.println("DEBUG: cid=" + clientId + " aid=" + agentId);

			// send a notification by calling the dhNotification REST service.
			res = sendNotification(clientId, agentId);
		}
		catch( Exception e )
		{ // json parsing has failed! Bad json data in the request's body
			res = Response.ok("Invalid json! valid format is: { clientId:1002, agentId:1003 }").build();
			
		}
		 
		return res;
	}
		 
	 // Add new registration record for the registrationId passed over the HTTP URL
	 @javax.ws.rs.POST 
	 @Produces({MediaType.APPLICATION_JSON})
	 @Path("/{cid:[0-9]*}/{aid:[0-9]*}") // clientId and agentId, numeric 0-9 only data
	 public Response updateRegistrationRecord(@PathParam("cid") String cid, @PathParam("aid") String aid) 
	 {
		 // get the input parameters
		 Integer clientId = Integer.valueOf(cid);
		 Integer agentId  = Integer.valueOf(aid);
		 
		 // send a notification by calling the dhNotification REST service.
		 Response res = sendNotification(clientId, agentId);
		 
		 return res;
	 }

	 //-------------------------------------------------------------------------
	 // Private methods and code start here
	 //-------------------------------------------------------------------------
	 
	 // invokes the dhNotification REST service
	 // sends a dh-notification  
 	 private Response sendNotification(Integer clientId, Integer agentId)
	 {
		 Response res = null;
		 
		 try 
		 {
			String notificationURL = getNotificationServiceEndPoint(); 
			String urlName = notificationURL + "?clientId=" + clientId + "&agentId=" + agentId;
			System.out.println("urlName = " + urlName);
			
			URL url = new URL(urlName);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			//conn.setRequestProperty("Host", notificationHostName);
			
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
				
				// we are done with the connection, close it!
				conn.disconnect();
				conn = null;
				
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
 	 
	 // obtains the base url for the registration-dreamhome service
 	 // final url looks like this:
 	 //   http://host:port/notify
	 // this registration-dreamhome service calls the notification-dreamhome service
	 private String getNotificationServiceEndPoint()
	 {
	   String endPoint = null;
	   
	   // README!
	   // NOTE - NOTE - NOTE - NOTE
	   // NOTE: This code will need to be altered for different cloud providers
	   //
	   // specific code for OpenShift endpoint discovery using env vars	 
	   // read system environment variables to obtain host:port endpoint
	   // for the notification-dreamhome service
	   // First, default to using the public external endpoint
	   String host = "notification-dreamhome.ose.cpo.com";
	   String port = "80";
	   
	   // Next, try fetching the more efficient internal end point
	   String varRead = null;
	   varRead = System.getenv("NOTIFICATION_SERVICE_HOST");
	   if(varRead!=null && varRead.length()>0)
		   host = varRead;
	   varRead = System.getenv("NOTIFICATION_SERVICE_PORT");	 
	   if(varRead!=null && varRead.length()>0)
		   port = varRead;
	   
	   // build the endpoint url
	   // 	http://host:port/notify
       endPoint = "http://" + host + ":" + port + "/notify"; 		   
		 
	   return(endPoint);	 
	 }	 
}
