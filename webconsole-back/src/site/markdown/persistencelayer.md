# Persistence Layer

## Referential package
![alt text](./images/referential.png "Referential Persistence Model")

The referential package manage all following notion:

### CMS
This table contain one entry per CMS managed by the platform. Example: Drupal 7, Drupal 8, Wordpress, Static.

### Website
This table contain one entry per website managed by the platform.
Example: saint-gobain.com, saint-gobain.it, isover.fr ...

### Docroot
This table contain one entry per docroot managed by the platform. A Docroot (also know as document root) is the location where we put the website code source, used by the webserver. In our case, this a logical view, because a docroot have many environment. And on each environment, there is a folder corresponding to the docroot of the webserver.

### Environment
This table contain one entry per environment. 
Example: dev, uat, prod.

### DocrootEnvironment
This table is the link between Docroot and environment. There is one entry for each Environment attached a to docroot.

### WebsiteDeployed
This table is the link betwwen Website and DocrootEnvironment. There is one entry for each website deployed on each DocrootEnvironment.

### Server
This table contain one entry per server managed by the server

### HostingProvider
This table contain one entry per Hosting Provider. A Hosting Provider is a provider of infrastructure (like server, LoadBalancer, ...)

### Registar
This table contain one entry per Registar. Registar design Domain Name Registar (like Nameshield ...)

### Certificate
This table contain one entry per certificate needed by website.

### Domain
This table contain one entry per FQDN used by websites.

### LoadBalancer
This table contain one entry per LoadBalancer.

### DomainLoadBalancer
This table is the link betwwen Domain and LoadBalancer. There is one entry per Domain declared on a LoadBalancer. 

### DrupalDocrootCore
This table contain one entry per DrupalDocrootCore. A DrupalDocrootCore is related to each Drupal docroot core managed by the platform. There is one drupal core per docroot.


## Deployment package
![alt text](./images/deployment.png "Deployment Persistence Model")

### DeploymentStatus
This table contain one entry per deployment status. 
Example of deplpoyment status : requested, in progress, done, failed ...

### DeploymentType
This table contain one entry per deployment type.
Example of deployment type : WEBSITE, DRUPALDOCROOTCORE, CORE, ...

### Deployment
This table contain one entry per deployment. 

### DeploymentStep
This table contain one entry per deployment Step.
Example of deployment step: POST (=after deployment), PREDEPLOYMENT (=before deployment) 

### DeploymentCommand
This table contain one entry per deployment command. A deployment command is a command executed on the website during the deployment phase. A deployment could have many comannd to execute, in this case many result should be returned. A deployment command is perform during a deployment step.

# User package
![alt text](./images/users.png "Persistence Model")

### Roles
This table contain one entry per User Role.
Exemple of Role : Admin, External, Owner ...

### Users
This table contain one entry per user. The isAdmin attribute is set to give the Admin Profile to the user. if the user have one or more entry in Accessright table, the the user have a User profile.

### Project
This table contain one entry per Project. A project is a logical notion, to manage all kind of project like : website, drupaldocrootcore. There are 1 project of each kinf of project.
Exemple: Each entry in WEBSITE table must have one corresponding entry in the PROJECT table 

### Accessright
This table contain one entry per Project, User, Role.

