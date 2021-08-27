# [Part 1 - Introduction to the operation of web apps and the basics of the Internet](https://web-palvelinohjelmointi-21.mooc.fi/osa-1)

Objectives:

- Familiar with the concepts of URI, DNS, HTTP and HTML
- Familiar with the main methods of HTTP protocol GET and POST
- Understand what the client-server model means
- Able to implement a simple server and browser in Java
- Create multiple web applications that respond to requests using the Spring API

____

### The Basics of the Internet

The internet is based on the **Uniform Resource Identifier (URI)**  to identify services, server software and resources. The **Domain Name Services (DNS)** converts these string addresses to web addresses. **HyperText Transfer Protocol (HTTP)** is the message format used to communicate between browsers and servers, with **HyperText Markup Language (HTML)** being the most common document presentation language.

URIs are structured as such:

```
protocol://host:port/
```

- protocol - the protocol used in the query such as HTTP or HTTPS
- host - the machine or server to connect to. Can either be an IP address or a text
- port - if the port is omitted from the address, a request is typically made to the default port, 80 for HTTP, 443 for HTTPS

IP addresses identify computers and allow machines to be found across a network. The conenction to a machine defined by an IP address is opened using the **HTTP protocol of the application layer over the TCP protocol of the transport layer**. The purpose of the TCP protocol is to ensure that messages arrive, as the browser does not directly contact the server software but typically there are several messaging servers in between to help messages get across.

##### HTTP

HTTP is an application layer protocol used by web servers and browsers to communicate. It is based on a client-server model with one request-response paradigm for each request, which means that each request is processed as a separate entity. HTTP protocol messages are in text format. They consist of lines that form a header, as well as lines that form the body of the message. 

Below is an example of a simple request

```
METHOD / PATHTODESIREDRESOURCE HTTP / VERSION
heading-1: value

GET /index.html HTTP / 1.0
Host: www.munpalvelin.net
```

Because a single IP address can contain multiple servers, a mere path to a desired resource is not enough to find the right resource since the resource could be on any virtual server associated with the machine. For HTTP / 1.1 protocol, requests must include a **Host header** indicating the address of the server used.

On the other hand, below is an example of an HTTP server response

```
HTTP / VERSION STATUSCODE CLARIFICATION
heading-1:value

HTTP / 1.1 200 OK
Date: Mon, 01 Sep 2014 03:12:45 GMT
Server: Apache / 2.2.14 (Ubuntu)
Vary: Accept-Encoding
Content-Length: 973
Connection: close
Content-Type: text / html; charset = UTF-8
```

When a server receives a request related to a particular resource, it performs the action related to the resource and eventually returns a response. When the browser receives a response, it checks the status code and related information in the response and then determines what to do based on this response.

HTTP includes five categories for status codes:

| Code | Class                                                      | Examples                                   |
| ---- | ---------------------------------------------------------- | ------------------------------------------ |
| 1**  | Informational response                                     | 100 "Continue"                             |
| 2**  | Successfull events                                         | 200 "OK"                                   |
| 3**  | Additional functions are required from the client software | 301 "Moved Permanently" 304 "Not Modified" |
| 4**  | Error in request, bad syntax or cannot be fulfilled        | 401 "Not Authorized" 404 "Not Found"       |
| 5**  | Server failed to fulfill an apparently valid request       | 500 "Internal Server Error"                |

There are eight separate HTTP protocol request methods, and the most commonly used of them are **GET** and **POST**. GET is used to retrieve documents, and POST is used to send information. With POST requests, query parameters are appended to the request body. 

```
POST /page.html HTTP / 1.1
Host: server-address.net
Content-Type: application / x-www-form-urlencoded
Content-Length: 14

parameter = value
```

___

### Web Server Operation

Servers typically follow the **client-server model** and webservers and browser communicate using the HTTP protocol. In the client-server model, clients use the services provided by the server. In practice, the browser displays the user interface of the software. When a user performs an action, the browser requests additional information from the server related to the user's need if necessary. This enables distributed software: the end users can be located around the globe while the server is located in a specific location.

##### Connecting to a server in Java

In Java, connecting to another machine is established using the **Socket** class. Once connected, the message sent to the other machine is written to the **OutputStream** interface provided by the socket. The response is then read via the **InputStream** interface provided by the socket.

```java
        System.out.println("=====Where to?=====");
        String host = "www.helsinki.fi";
        int port = 80;
        
        Socket socket = new Socket(host, port);
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.println("GET / HTTP/1.1");
        writer.println("Host: " + host);
        writer.println();
        writer.flush();
        
        System.out.println("=====Response=====");
        
        Scanner output = new Scanner(socket.getInputStream());
        while (output.hasNextLine()) {
            System.out.println(output.nextLine());
        }
```

The above program contacts the server associated with `www.helsinki.fi` and connects to port 80 of the server and sends the following message to the server:

```
GET / HTTP / 1.1
Host: www.helsinki.fi
```

The program then prints the response from the server.

```
=====Where to?=====
www.helsinki.fi
=====Response=====
HTTP/1.1 301 Moved Permanently
Date: Thu, 26 Aug 2021 12:27:04 GMT
Server: Varnish
X-Varnish: 370410179
Location: https://www.helsinki.fi/
Content-Length: 0
Connection: keep-alive

```

##### Server operation in Java

Unlike the previous example of connecting to another machine, implementing a server creates a **ServerSocket** object that listens to a specific port on the machine. When another machine connects to the server, a Socket object is enabled that provides read and write access.

### Spring application framework

**Inversion of Control** is a principle in software frameworks, where the responsibility for creating program components and communicating between program components is transferred to the software framework. In practice, this means that control over what is created and the execution of the program is the responsibility of the application framework.

In the case of the Spring framework, the programmer implements classes within the application framework, but does not create objects from them. Creating objects is primarily the responsibility of the Spring API.

**Dependency Injection** is a design model where the dependencies are injected in to the application. This means that the class object variables are not created in constructors, but are given as constructor parameters.

Spring API thus reduces unnecessary dependencies on objects because the API creates objects from classes and injects the dependency for use by the application.

##### Hello World Spring Boot application

The class contains a **main** method required to start the application and the **home** method which processes the requests. The method **SpringApplication.run** is given a class parameter that includes the @SpringBootApplication annotation, in this case, *SandboxApplication*. This annotation is used to configure the application with the example providing the default settings.

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class SandboxApplication {
    
    @GetMapping("*")
    @ResponseBody
    public String home() {
        return "Hello World!";
    }
    
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SandboxApplication.class, args);
    }
    
}
```

Classes marked with the **@Controller** annotation indicates that the class is a Spring controller and contains method for handling incoming requests to the server, and Spring takes the responsibility for redirecting requests to the class methods.

The annotation **@GetMapping** is used to define the path to be listened to and the request type of the HTTP protocol is GET. All GET requests are routed to that method because @GetMapping has an additional parameter defined by *****. Instead of an asterisk, we can also make use of a path. If we make use of a path, then the requests to the web server address will be routed to the method the annotation is in. In the example below, if we try to access /path, we will be greeted with a string "Path /path!". Multiple paths can also be defined for a single program.

The **@ResponseBody** annotation is for annotating request handler methods. It indicates that the result type should be written straight into the response body. In other words, it binds the return value to the response body.

```java
@Controller
public class HelloPathController {

    @GetMapping("/path")
    @ResponseBody
    public String path() {
        return "Path /path!";
    }
    
    @GetMapping("/route")
    @ResponseBody
    public String route() {
        return "Path /route!";
    }
}
```

##### Request parameters

Information can be sent over the server as request parameters. Parameters are added to the request by adding a question mark after the address, followed by the parameter name, equals symbol, and the value assigned to the parameter. Parameters in the request can be accessed using the **@RequestParam** annotation. This annotation is used to annotate request handler method arguments. In the example below, the request has a parameter named animal whose value is dog.

```
https://localhost:8080/secret?animal=dog
```

We can also use multiple parameters, and also we can define the parameter types. The controller below sends a greeting in response to a request. 

```java
@Controller
public class HelloRequestParamController {
    
    @GetMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam String name,
                        @RequestParam String breed
                        @RequestParam Integer year
                       ) {
        return "Hello " + name + " you are a " + breed + " you are of age " + year ;
    }
}
```

When visiting `http://localhost:8080/hello?name=doge&breed=shibe&year=5`, the output will be

```
Hello doge you are a shibe you are of age 5
```

The Map data structure can be used as well. The logic has already been implemented by the framework, and it is able to identify the parameters in the request and add them as parameters to the method.

```java
@Controller
public class HelloRequestParamsController {
    @GetMapping("/params")
    @ResponseBody
    public String hellorequestparams(@RequestParam Map<String, String> params) {
        return params.keySet().toString();
    }
}
```

The following url `http://localhost:8080/params?hello=world&it=works` will give an output:

```
[hello, it]
```

