# Pcpeg

[![pipeline status](https://cerebro.digital-solutions.saint-gobain.com/delivery/peg/pcpeg-service/badges/develop/pipeline.svg)](https://cerebro.digital-solutions.saint-gobain.com/delivery/peg/pcpeg-service/-/commits/develop) [![coverage report](https://cerebro.digital-solutions.saint-gobain.com/delivery/peg/pcpeg-service/badges/develop/coverage.svg)](https://cerebro.digital-solutions.saint-gobain.com/delivery/peg/pcpeg-service/-/commits/develop)

The Pcpeg service allows to retrieve and store pcpeg data.
The endpoints which are exposed by this service can be tested through [Swagger](http://127.0.0.1:8080/swagger-ui.html).

## Pcpeg domain

### Endpoints

* `GET /pcpegs` : Retrieve all pcpegs
* `GET /pcpegs/{id}` : Retrieve pcpeg by Id
* `POST /pcpegs` : Create new pcpeg
* `PUT /pcpegs` : Update existing pcpeg
* `DELETE /pcpegs/{id}` : Delete existing pcpeg
