------------------------------------------
Workflow Server REST API example requests
------------------------------------------
At "DDMoRe-BWF-Main\workflow-server-rest-requests" and "DDMoRe-BWF-Main\workflow-server-rest-api"

For all queries 'repo_id' is the only mandatory field.
All other fields are restricting the result.


-------------------------
Building the application
-------------------------
The project is using Maven to build the application.

There are 6 projectes for DDMore-BWF:
  - DDMoRe-BWF-Main
  
  - DDMoRe-BWF-Client
  
  - DDMoRe-BWF-Core
  
  - DDMoRe-BWF-Rest
  
  - DDMoRe-BWF-WorkflowServer-Rest
  
  - DDMoRe-BWF-Web


-------------------
Application design
-------------------  

DDMoRe-WorkflowServer-Rest	     REST	    DDMoRe-BWF-Rest				REST		DDMoRe-BWF-Web
  - DDMoRe-BWF-Client		 <---------->	  - DDMoRe-BWF-Client   <---------->      - DDMoRe-BWF-Client
					                          - DDMoRe-BWF-Core

Users
-----
scientist,scientist
manager,manager
admin,admin
