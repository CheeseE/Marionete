
![My image](img/marionete_logo-1.svg)


## Description

In this assignment we would like from you, to implement a simple platform that can communicate between different backends in Sync/Async way.

All the logic you must implement have to be in ```service``` module.

The implementation can be done in whatever JVM language you want **(Java/Scala/Kotlin/Groovy)** using at least ```jdk-11.0.1```

### Restful 

For this platform, we would like an implementation of a simple **Restful** service which have to implement the endpoint:

```

POST /marionete/useraccount/

{
    "username":"bla",
    "password":"foo"
}

```
And response a json with this structure:
```
{
  "accountInfo": {
    "accountNumber": "12345-3346-3335-4456"
  },
  "userInfo": {
    "name": "John",
    "surname": "Doe",
    "sex": "male",
    "age": 32
  }
}

```

### gRPC

We would like you to implement the logic of client/server and service provided in the protobuf file [here](service/src/main/proto/login_service.proto).

To be invoked the gRPC server, once you receive the Rest call of ```/marionete/useraccount/```. 
So then you can obtain and keep the ```token``` for the next rest calls.

### Rest Connector

We would like from you to create a ```Rest connector``` to invoke two external service.

* **UserAccount**: It will return the user information
    ```
    Request:
        GET http://localhost:8898/marionete/user/
        
        HEADER Authorization: token
    
    Response:
    {
        "name":"John",
        "surname":"Doe",
        "sex":"male",
        "age": 32
    }
    ```

* **AccountInfo**: It will return the account information
    ```
    Request:
        GET http://localhost:8899/marionete/account/
        
        HEADER Authorization: token
    
    Response:
    {
        "accountNumber":"12345-3346-3335-4456"
    }
    ```

We provide implementation for both backends in ```backends``` module.

This mocks are not part of the exercise and only need to be invoked in your tests:

```
UserInfoMock.start()
AccountInfoMock.start()
```

**IMPORTANT** **AccountInfoMock** was implemented to fail the first two calls against him, so we expect an implementation in the Rest connector
 to deal with that.


## What we're looking for:
* How you approach the problem (are you writing everything from scratch? If not, what libraries are you suing to support/accelerate your work).
* How you structure your code/files 
* How you prioritise the sub-tasks involved (how much attention to pay to testing, what things do you automate?)

## Expectations 
* All test included the one in **backends** module must pass using ```mvn clean install```
* The program must run and return results invoking the endpoint of the API described before.
* The program must use the mocks servers we provide in **backends** module.
* The program must work efficiently expecting to receive big amount fo traffic.
* The code must include unit tests and be well-structured to support future maintenance and evolutions. 
  Try to use the best practices that you have learned from your experiences.
* Documentation to follow the architecture of the program is always good.
* You should be spending 2-3 hours on the task, but approaching 'production like' quality is more
  important than implementation speed and feature completeness.

