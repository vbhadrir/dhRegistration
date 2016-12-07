# Dream Home Java Registration REST service

##To execute a registration send a HTTP POST request with this format:
## ----------------------------------------------------------------------------
###	http://host:port/dhJavaServices-1.0.0-SNAPSHOT/rest/Registration
## passing the input json data in the body of the POST request
## json data example: { clientId:1002, agentId:1003 }
## ----------------------------------------------------------------------------
## Optional HTTP POST format, with args at the end of the http req
###	http://host:port/dhJavaServices-1.0.0-SNAPSHOT/rest/Registration/{clientId}/{agentId}
## example:
###	http://registration-dreamhome.ose.cpo.com:80/dhJavaServices-1.0.0-SNAPSHOT/rest/Registration/1001/1002
