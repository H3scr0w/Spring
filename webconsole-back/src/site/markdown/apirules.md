# API Rules

## rule 1: POST or PUT
the following rules is applied on all API using POST or PUT Verb (except for rundeck callback) :

* Use POST, only if the uri of the ressources is not know.
Example: 
POST /user/ 
Body {"name":"jb", "lastname":"fournier"}
return /user/jbfournier

If the Id id kown, then use PUT Verb.
Example:
PUT /user/jbfournier

## rule 2: For all endpoints returning a list:
 * set a fetchLimit and fetchstart parameter (optional). The default value of fetchLimit is 25.
 * If in the persistence layer, a enable attribute exist, set a showDisable parameter.





