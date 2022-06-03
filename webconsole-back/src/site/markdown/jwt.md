#JWT 

## JWT format description

The JSON Web Token have only 1 custom claim : accessright.

This claim contain a list of value separated by a semicolon value (;).
There are 2 kinds of format possible :

1. value admin, when the user have the profile admin
2. or an accessright. This is string containting 3 string value separated by :
  - the first value is the project type 
  - the second value is the project code
  - thr third value is the role
