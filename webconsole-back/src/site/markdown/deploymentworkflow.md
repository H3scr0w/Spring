# Deployment workflow

## Introduction
This workflow manage all deployment done by website4SG platform.

## Workflow

### Principle
The principle of the workflow is :
Anyone could create a request deployment, but only an SG Employee can validate it and launch the deployment.

### Activity Diagram and Workflow status

![alt text](./images/ActivityDiagramDeployment.png "Deployment workflow : Activity Diagram")

Deployment status

* Requested : This is the entry point of the workflow, any deployment must have been requested. Could be aborted.
* Accepted : When someone has validated the deployment request. Could be aborted.
* In Progress : When the deployment is in progress (In our case, when rundeck has started the deployment)
* Succeeded : When a deployment has finised succesfully
* Failed : When a deploymand failed
* Aborted : When a deployment has been aborted.

