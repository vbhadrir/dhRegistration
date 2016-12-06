package com.ibm.cpo.dreamhome;

public class dhRegDataRec 
{
    String clientId;
    String agentId;
    
    public String getClientId()
    {
    	return(this.clientId);
    }
    
    public String getAgentId()
    {
    	return(this.agentId);
    }
    
    public void setClientId(String id)
    {
    	this.clientId = id;
    	
    	return;
    }
    
    public void setAgentId(String id)
    {
    	this.agentId = id;
    	
    	return;
    }
    
    @Override
	public String toString() 
    {
		return "RegDataRec [clientid=" + this.clientId + ", agentId=" + this.agentId + "]";
	}
}
