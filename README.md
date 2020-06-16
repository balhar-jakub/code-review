# zaas-client

- [zaas-client](#zaas-client)
  - [Introduction](#introduction)
  - [Pre-requisites](#pre-requisites)
  - [Getting Started](#getting-started)
  - [Commands to Setup PassTickets for Your Service](#commands-to-setup-passtickets-for-your-service)

## Introduction

This is a native java library developed on the top of API ML login, query and pass ticket API. It is developed with apache http Client version 4.5.11.

## Pre-requisites

1) Java SDK version 1.8.
2) Gateway Service of APIML layer should be up and running as a service.
3) Property file which defines the keystore or truststore certificates.

### Getting Started

1) In order to use this library you can create your API(RestController in case of Spring API) for login, query.

2) Then add zaas-client as a dependency in your project. This library provides you the following interface:

```java
public interface TokenService {
    static String COOKIE_PREFIX = "apimlAuthenticationToken";
    void init(ConfigProperties configProperties);
    String login(String userId, String password) throws ZaasClientException;
    String login(String authorizationHeader) throws ZaasClientException;
    ZaasToken query(String token) throws ZaasClientException;
}
```
3) In order to use zaas-client, you need to provide a property file to initialize ConfigProperties used 
in the token service which includes the path to your truststore and keystore files and their following 
configuration parameters:

```java
public class ConfigProperties {
    private String apimlHost;
    private String apimlPort;
    private String apimlBaseUrl;
    private String keyStoreType;
    private String keyStorePath;
    private String keyStorePassword;
    private String trustStoreType;
    private String trustStorePath;
    private String trustStorePassword;
}
```

4)'zaas-client' allows you to have following functionality in your application:

a) Login:
In order to integrate login you just have to call either of the method provided for login in the 'TokenService' interface
based on how you want the user to provide the credentials.

If user provides credentials in the request body then you can call the following method from your API:
```java
String login(String userId, String password) throws ZaasClientException;
 ``` 
In case, user is providing credentials as Basic Auth then following method can be used:
```java
String login(String authorizationHeader) throws ZaasClientException;
 ```    
These methods will return the JWT token in return as a String. This token can be further used to authenticate the user
in rest of the API's.

This method will automatically use the truststore file to add a security layer which you have configured using ConfigProperties class.

b) Query:
Query method is used to provide you the details embedded in the token which includes creation time of the token, expiration time 
of the token and the user to whom the token has been issued and so on.

To use this method simply call this method from your API.
```java
ZaasToken query(String token) throws ZaasClientException;
 ``` 
In return you will get the 'ZaasToken' Object which has the following JSON format.

This method will automatically use the truststore file to add a security layer which you have configured using ConfigProperties class.
