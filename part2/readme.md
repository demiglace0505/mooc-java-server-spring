# [Part 2 - Views, Databases and Database Abstractions](https://web-palvelinohjelmointi-21.mooc.fi/osa-1)

> Key Takeaways:
>
> - Create views that are returned to the user using Thymeleaf
> - Send and process information sent to a server using forms
> - Familiar with the PRG POST-Redirect-Get model
> - Utilize database abstraction provided by Java and Spring Boot to process information using ORM Object Relational Mapping libraries
> - Using path variables
>

____

### Views and Data

Views are typically created using auxiliary libraries, with the programmer creating HTML views and embedding library-specific commands in the HTML code which allows operations such as adding information to pages. These HTML pages embedded with library-specific information are called **view templates**. **Thymeleaf** provides tools for adding data to HTML pages. To use Thymeleaf, we need to add a new dependency in the pom.xml file:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

The following example below creates an application that listens for the root path. The method returns a string "index" wherein Spring searches the folder `src/main/resources/templates/` for **index.html**. When a page is found, it is then processed by Thymeleaf, and afterwards, a page will be returned to the user.

Thymeleaf pages (or "templates") are located in `src/main/resources/templates`, which is located in the **Other Sources** folder in NetBeans.

```java
package thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymeleafController {

    @GetMapping("/")
    public String home() {
        return "index";
    }
}
```

The code is very similar to the previous part, except there is no annotation for @ResponseBody this time. If the method has an annotation @ResponseBody, the string "index" would be returned. Since there is no @ResponseBody annotation specified for this method, Spring knows that the returned string is related to the view displayed to the user. Spring also knows that this is a view for the Thymeleaf library, so it looks for the file associated with the "index" string and makes it available for Thymeleaf.

##### Adding information to a view using the Model class

Data created or retrieved in the server software is added to the view using a **Model** object. In the example below, a Model object is defined for the method. We add a key *"text"* with value from a request parameter. Suppose that the url is `https://localhost:8080/?myText=DOGE`, then the value for key *"text"* will be *DOGE*. The string index is then returned, which Spring directs to Thymeleaf for processing. 

```java
package thymeleafdata;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymeleafJaDataController {

    @GetMapping("/")
    public String index(Model model, @RequestParam String myText) {
        model.addAttribute("text", myText);
        return "index";
    }
}
```

Thymeleaf has access to the Model object and its values, as well as information about the page being displayed. If for example the index.html source code is as follows:

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
    <head>
        <title>Title</title>
    </head>

    <body>
        <h1>Hello World!</h1>

        <h2 th:text="${text}">testing</h2>
    </body>
</html>
```

When Thymeleaf processes an HTML page, it looks for element attributes beginning with `th:`. For the above example, there is an h2 element with attribute **th:text**. This attribute tells Thymeleaf that the text value of the element (testing) should be replaced by a variable. The variable for th:text is `${text}`, so Thymeleaf looks in the model object for the value of the key text. In this case, `testing` is replaced by **DOGE**.

##### Displaying List Collections with Thymeleaf

We can also use Thymeleaf's Model object to set value for collections. In the below example, we will create a list that will be processed by Thymeleaf.

```java
package thymeleafdata;

import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ListController {
    private List<String> list;

    public ListController() {
        this.list = new ArrayList<>();
        this.list.add("Hello world!");
        this.list.add("+[-[<<[+[--->]-[<<<]]]>>>-]>-.---.>..>.<<<<-.<+.>>>>>.>.<<.<-.");
    }

    @GetMapping(value = "/")
    public String home(Model model) {
        model.addAttribute("list", list);
        return "index";
    }
}
```

The list object is accessed using the attribute `th:each`. It takes the object to be iterated over, and then its value assigned to a key. The basic syntax is as follows:

```html
<ul>
    <li th:each="myItem : ${list}">
        <span th:text="${myItem}">hello world!</span>
    </li>
</ul>
```

##### Lombok library

Lombok is a library that provides the ability to automatically create getters and setters without needing to write or define them. Take the below code for example, event a simple class contains a lot of boilerplate.

```java
public class Event {

    private String name;

    public Event() {
    }

    public Event(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

But with Lombok, this can be shortened to

```java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Event {
    private String name;
}
```

The annotation **@NoArgsConstructor** creates a parameterless constructor of the class and the **@AllArgsConstructor** creates a constructor with all the attributes of a class. **@Data** creates the getters, setters, equals, hashcode, toString method for the attributes.

To use Lombok, we just need to declare it as a dependency in pom.xml

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <scope>provided</scope>
</dependency>
```

##### Handling Objects

In addition to collections, other Object types can also be added to the model.

```java
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Person {
    private String name;
}

@GetMapping("/")
public String home(Model model) {
    model.addAttribute("person", new Person("The Doge"));
    return "index";
}
```

The *"The Doge"* person is stored to the *"person"* key. We can access the object using the key. The **toString** method is used to return the value. We can also access the objects variables. If we want to print a *"name"* variable related to the Person object, we can call the method **getName** that Lombok automatically generated for us with the annotation @Data.

```html
<h2 th:text="${person.name}">Person Name</h2>
```

##### Objects in a list

The properties of the elements in the iterated set can be accessed in the same way as when accessing properties of objects. 

```java
package person;

import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PersonController {
    private List<Person> persons;

    public PersonController() {
        this.persons = new ArrayList<>();
        this.persons.add(new Person("James Gosling"));
        this.persons.add(new Person("Martin Odersky"));
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("list", persons);
        return "index";
    }
}
```

```html
<ol>
    <li th:each="person : ${list}">
        <span th:text="${person.name}">Insert Person Here</span>
    </li>
</ol>
```

After being processed by the server, the resulting HTML will be:

```html
<ol>
    <li><span>James Gosling</span></li>
    <li><span>Martin Odersky</span></li>
</ol>
```

### Sending information to the server

A form at its simplest can be defined by text fields and a button for submission. 

```html
<form action="/" method="POST">
    <input type="text" name="nimi"/>
    <input type="submit"/>
</form>
```

When the user submits the form, the information is sent as a POST-type request to the root (action= "/"). With Thymeleaf and Spring, forms can be made independent of the application's root path. This can be done by defining the form's *action* attribute using Thymeleaf `th:action=@{/}`. In this case, the path of the form is determined by the location of the application.

```html
<form th:action="@{/}" method="POST">
    <input type="text" name="nimi"/>
    <input type="submit"/>
</form>
```

The annotation **@PostMapping** receives the information that is sent using a form. For example, the data in the above form's parameter nimi can be processed by the following method. This method listens for POST-type requests to the root of the application.

```java
@PostMapping("/")
public String post(@RequestParam String nimi) {
    System.out.println(nimi);
    return "index";
}
```

##### Post Redirect Get Model

When information gets set to the server in a POST request, the parameters of the request are included in the body of the request. In the example above, for example, string "Hello World!" will have a HTTP request that looks like the following:

```http
POST / HTTP / 1.1
Host: localhost: 8080
// other headings

content = Hello world!
```

A **redirect** is necessary so that we can avoid resending the data when reloading a page. The functionality of a server that receives data must be implemented in such a way that, after processing transmitted information, a redirect request should be returned to the user in response. The browser then makes a new request to the path provided by the redirect request. This is called the **Post Redirect Get model** and its main purpose is to prevent retransmission of forms.

```java
import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RedirectOnPostController {

    private String message;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", message);
        return "index";
    }

    @PostMapping("/")
    public String post(@RequestParam String content) {
        this.message = content;
        return "redirect:/";
    }
}
```

The returned string `redirect:/` by the method tells us that a redirect request to the path should be sent in response to a request made to `/`. When the browser receives a redirect request, it makes a GET request to the address provided in the redirect request.

This [sequence diagram](https://web-palvelinohjelmointi-21.mooc.fi/static/dc3edd78bc27fff2fe4818b11377fb48/01dae/post-redirect-get.png) describes how to submit a form using the Post Redirect Get model. First the user types the desired address into the browser. Then the browser makes a GET request to the server. The server then returns an HTML page in response. The user fills out a form on the HTML page and submits it. The browser makes a POST type request to the server wherein the body of the request contains the information filled by the user on the form. The server receives the request, processes it, and then sends a redirect request in response. The browser automatically makes a GET request to the address indicated by the redirect request. Finally, the server returns an HTML page associated to this address that is display to the user.

### Database processing programmatically

Database tables and Classes can be considered to be very similar. Database tables define columns and reference keys, while Classes have attributes and references. Between relational databases and object-oriented programming, there is a need to convert objects to database rows and back. This is called **Object-Relational Mapping (ORM)**. ORM offers us functionality for creating database tables out of classes.

Oracle/Sun standardizes the storage of objects in a relational database using the [JPA](https://en.wikipedia.org/wiki/Jakarta_Persistence) Java Persistence API standard. Libraries that implement JPA such as Hibernate abstract the relational database and makes it easier to query directly from program code. It simplifies the development of Java applications for interacting with the database.

To add database functionality to a Spring application, we need to add dependencies into pom.xml. The definitions can be found in `src/main/resources` *application.properties* file

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
</dependency>
```

Each class stored in the database must have an annotation **@Entity** and an **@Id** attribute which serves as the main key for the database table. The master key is typically numeric (Long or Integer), and the class must implement the **Serializable** interface. The annotation **@GenerateValue(strategy = GenerationType.AUTO)** can be defined for the numeric key. This gives the responsibility for creating the master key values for the database.

```java
// package

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Person")
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    // getters and setters
```

The names of the columns and database tables can be modified using annotations **@Column** and **@Table**. The above example defines a database table named "Person" with columns "id" and "name". By default, the table and column names are created based on the variable name, so the definition above actually don't do anything. Column types are automatically inferred based on the variable type. 

We can make use of the **AbstractPersistable** class to make definitions a little shorter. This superclass defines the master key, and in addition, it also implements the Serializable interface.

```java
// packaging and imports

@Entity
@Table(name = "Person")
public class Person extends AbstractPersistable<Long> {

    @Column(name = "name")
    private String name;
    // getter and setter
```

Since we use Lombok, we don't need to write out getters and setters. Also, if the annotations for @Table and @Column are not explicitly defined, their names are inferred from the names of the class and variables.

```java
// packaging and imports

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Person extends AbstractPersistable<Long> {

    private String name;
}
```

The above defines a database table with **id** column as the main key. The master key is created automatically and its type is set to Long (...extends AbstractPersistable<Long>). In addition, the database table has a string column **name**. The class also includes constructors, getters, setters, hashCode, equals and toString methods.

##### Interface for database processing

When we have a class that describes a database table, we can create an interface for  handling the database. With Spring and the JPA standard, the database handling interface inherits a ready-made **JpaRepository** interface that defines CRUD functionality and other methods. 

The JpaRepository are given two parameters. The first is the class describing the database table and the second is the type of the database table master key. The example below interface is called `PersonRepository`, and it assumes that the class `Person` is in the package `domain`. Spring automatically creates an object that implements the interface when we start our application.

```java
// package

import domain.Henkilo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
```

##### Importing a database object into the controller

Adding the `PersonRepository` interface to our controller class is done by defining an object describing the database abstraction as an object variable of the controller. We then make use of the annotation **@Autowired**. This process is related to Inversion of Control and Dependency Injection. Autowiring enables us to inject the object dependency

```java
@Controller
public class PersonController {
  
  @Autowired
  private PersonRepository personRepository;
}
```

The database can now be accessed via the PersonRepository object. The [JpaRepository API](https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html) has the descriptions for the methods provided by the interface. JpaRepository also inherits the interface CrudRepository. We can now implement listing of objects in a database and adding individual objects via the following example:

```java
// ...

@Controller
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("list", personRepository.findAll());
        return "persons"; // here we assume a separate file persons.html
    }

    @PostMapping("/")
    public String create(@RequestParam String name) {
        personRepository.save(new Person(name));
        return "redirect:/";
    }
}
```

### Path Variables

Paths are used to identify resources. There are situations wherein the created resources are unique and we do not know their details yet before the application starts, so we cannot define an exact path that would identify an unknown resource. One solution is defining a method in the controller class so that the method always handles requests coming to a particular path and the parameter that accompanies the request. For example, a request to path `/person?id=3` would return the information of the person whose `id` column value is `3`.

We can define a path with curly braces using the **@GetMapping** annotation, as written in the example below. Path variables are accessed using the **@PathVariable** annotation

```java
@Controller
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("list", personRepository.findAll());
        return "person"; //assuming a file person.html
    }

    @PostMapping("/")
    public String create(@RequestParam String name) {
        personRepository.save(new Person(name));
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String getOne(Model model, @PathVariable Long id) {
        model.addAttribute("person", personRepository.getOne(id));
        return "person";
    }
}
```

The method **getOne** handles requests to an unspecified address `/{id}`. This means that Spring retrieves part of the path and places the variable named id for which @PathVariable annotation is defined. The method retrieves the Person object from the database with the table id based on the master key. The returned value is then added to the model, and finally displayed as part of the person.html file.