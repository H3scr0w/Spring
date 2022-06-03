# Website4sgCore

The Website4sgCore service allows to retrieve and store website4sgcore data.
The endpoints which are exposed by this service can be tested through [Swagger](http://127.0.0.1:8080/swagger-ui.html).

## Opendj local

* https://docs.docker.com/docker-for-windows/
* `docker pull harbor.digital-solutions.saint-gobain.com/ops-website4sg/wsip-opendj:2020-03` : Pull Opendj customized image
* `docker create -p 1389:1389 -p 4444:4444 -p 8080:8080 --name opendj harbor.digital-solutions.saint-gobain.com/ops-website4sg/wsip-opendj:2020-03` : Create Opendj container
* `docker start opendj` : Start Opendj app
* `GET https://localhost:8080/api/users/wsip-admin@saint-gobain.com` : Rest2ldap for Opendj

## MySQL local
### For new dev only:
* From (https://cerebro.digital-solutions.saint-gobain.com/delivery/website4sg/website4sg-core-service/-/tree/develop/sql)
* Run script V1.0.0__create.sql for init **website4sg** schema & data
* Then follow ascendant versions for migration:  V1.2.1__migrate.sql then V1.2.3__migrate.sql etc...