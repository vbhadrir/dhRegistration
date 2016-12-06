package com.ibm.cpo.dreamhome;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class dhRegDataRec 
{
    @XmlElement public String clientId;
    @XmlElement public String agentId;
}
