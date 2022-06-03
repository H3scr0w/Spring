# Profiles and roles


## Persistence layer
![alt text](./images/users.png "Persistence Model")


### Profiles
The application manage only 2 profiles :

- admin
- users

The profile admin is given by setting the properties isAdmin of Users Table to true.


### Roles
Roles is only needed for User profiles

Role list managed by website4SG-core:

 * Admin
 * External
 * LocalIT
 * Business
 * Owner

### Access Right
Each access right is given to:

 - 1 project (= 1 project type + project code)
 - 1 user
 - 1 role


