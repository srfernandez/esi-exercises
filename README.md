# ESI Exercises

This repository contains the code base for the exercises of the ESI subject
inside the DGSS itinerary.

## Deployment Environment

The environment is based on Maven 3, MySQL 5.5, WildFly 8.2.1 and Eclipse Mars 
for JEE.

### Java JDK 8
Download and install Java JDK 8, preferably the Oracle version (the commands 
`java` and `javac` must be available).

### Maven
Install Maven 3 in your system, if it was not installed (the `mvn` command must 
be available)

### Git
First, install git in your system if it was not installed (the `git` command 
must be available). We will work with Git to get updates of these exercises, 
as well as to deliver the student solution. Concretely, we will work with 2 Git 
repositories inside the [our Gitlab server](http://sing.ei.uvigo.es/dt/gitlab)

1. The main repository (read-only for students)

    Git url: `http://sing.ei.uvigo.es/dt/gitlab/dgss/esi-exercises.git`

2. The student's solution repository. Surf to
[our Gitlab server](http://sing.ei.uvigo.es/dt/gitlab) and create a user with 
your @esei.ei.uvigo.es email account. If your username is `bob`, create 
a **PRIVATE** project `bob-esi-solutions`

    Git url: `http://sing.ei.uvigo.es/dt/gitlab/bob/bob-esi-solutions.git`

### MySQL
Download and install MySQL 5.5 locally.

Connect to the MySQL client console as root.

    mysql -u root -p
    
Inside the MySQL console, create the database `dgss`
    
    create database dgss;

Create the MySQL user `dgssuser` with password `dgsspass` and grant him all
privileges on the `dgss` database

    grant all privileges on dgss.* to dgssuser@localhost identified by "dgsspass";
    
Exit the MySQL console
    exit
    
### WildFly
Download 
[WildFly 8.2.1.Final](http://download.jboss.org/wildfly/8.2.1.Final/wildfly-8.2.1.Final.zip)

Uncompress the downloaded zip in any folder on your computer.

#### Configure the MySQL driver and the dgss datasource in WildFly
Download [mysql driver 5.1.21](http://central.maven.org/maven2/mysql/mysql-connector-java/5.1.21/mysql-connector-java-5.1.21.jar)
and copy it inside the `standalone/deployments` folder of widlfly

Create the mysql-ds.xml file with the following content and place it inside 
the `standalone/deployments` folder of widlfly

```xml
<datasources xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns="http://www.ironjacamar.org/doc/schema"
  xsi:schemaLocation="http://www.ironjacamar.org/doc/schema http://www.ironjacamar.org/doc/schema/datasources_1_1.xsd">

  <datasource jndi-name="datasource/dgss" pool-name="MySQLPool">
  
      <connection-url>jdbc:mysql://localhost:3306/dgss</connection-url>
      <driver>mysql-connector-java-5.1.21.jar</driver>
      <pool>
          <max-pool-size>30</max-pool-size>
      </pool>
      <security>
          <user-name>dgssuser</user-name>
          <password>dgsspass</password>
      </security>
  </datasource>
</datasources>
```

#### Start WildFly
Run the following command

    /path/to/wildfly/bin/standalone.sh

Check if WildFly is running by browsing to [http://localhost:8080](http://localhost:8080)
    
## Developing Environment
### Clone the remote repository  
    git clone http://sing.ei.uvigo.es/dt/gitlab/dgss/esi-exercises.git
    cd esi-exercises

### Prepare for pushing into your own repository
**WARNING: be careful to replace "bob" with your username**

    git remote set-url origin http://sing.ei.uvigo.es/dt/gitlab/bob/bob-esi-solutions.git

### Start coding your solution
Create a branch for your solution:

    git checkout -b solution
    
### Building and running the project (every time you make changes in your code)
With the WildFly server up and running, you have to go inside your source code folder

    cd /path/to/bob-esi-solutions
    mvn install
    cd web
    mvn wildfly:deploy
    cd ..

Surf to [http://localhost:8080/web-0.0.1-SNAPSHOT ](http://localhost:8080/web-0.0.1-SNAPSHOT) to see
your web (you have to create a index.html, or a Servlet inside the web
subproject in order to see something)

### Commit your changes    
    git add .
    # or
    git add <concrete_files>
    
    git commit

### Pushing your changes to your remote repository
    # the first time after creating the branch solution
    git push -u origin solution 
    # the rest of the times
    git push

### Eclipse
You can use any other IDE, such as IntelliJ IDEA or NetBeans, as long as they 
are compatible with Maven projects.

Before continue, you have **to patch Eclipse Mars**, concretely the m2e-wtp 
plugin. Go to `Help -> Install New Software`. Work with repository located 
at `http://download.eclipse.org/m2e-wtp/snapshots/mars/` and then select and 
install "Maven Integration for WTP". Restart Eclipse.

Open Eclipse Mars JEE and import your Maven project with 
`File -> Import -> Maven -> Existing Maven Projects`

Select your source code folder (where the `pom.xml` should be placed)

Eclipse should then import 3 projects (`esi-exercise`, `web` and `domain`)

You can run, if you want the project by:
1. Right click on `esi-exercise` project and `Run As -> Maven install`
2. Right click on `web` project and `Run As -> Maven build...`. 
Put `wildfly:deploy` as Goal.

## Exercise 1: JPA

### Task 1.
Inside the **domain project**, create a set of JPA entities given the ER model
you can find in the ER.png file. You will need also to create the java source
folder.

    mkdir -p domain/src/main/java

![Entity-Relationship diagram](ER.png)

Use this package for your entities: `es.uvigo.esei.dgss.exercises.domain`

### Task 2.
Inside the **Web project**, create a Facade class containing one method 
per each query (use JPA QL) in the following list.

Use this package: `es.uvigo.esei.dgss.exercises.web`

You will also need to create the source folders in this project:

    mkdir -p web/src/main/java
    mkdir -p web/src/main/webapp

1. Create a new user given its login, name, password and picture
2. Create a friendship between two given users
3. Get all friends of a given user
4. Get all posts of the friends of a given user
5. Get the posts that have been commented by the friends of a given user after 
a given date
6. Get the users which are friends of a given user who like a given post
7. Give me all the pictures a given user likes
8. Create a list of potential friends for a given user (feel free to create 
you own "algorithm")

### Sample files
A simple Sample Facade working with a `User` entity (not shown) would be:

```java
package es.uvigo.esei.dgss.exercises.web;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.uvigo.esei.dgss.exercises.domain.User;

@Dependent
public class Facade {

	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	public User addUser(String login, String name, String password, byte[] picture) {
		User user = new User(login);
		
		user.setName(name);
		user.setPassword(password);
		user.setPicture(picture);
		
		em.persist(user);
		
		return user;
	}
}
```

In order to test the facade, an easy solution would be to create a 
"Simple Servlet" as this one:

```java
package es.uvigo.esei.dgss.exercises.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import es.uvigo.esei.dgss.exercises.domain.User;

@WebServlet("/SimpleServlet")
public class SimpleServlet extends HttpServlet {

	@Inject
	private Facade facade;
	
	@Resource
	private UserTransaction transaction;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		PrintWriter writer = resp.getWriter();
		
		writer.println("<html>");
		writer.println("<body>");
		writer.println("<h1>Facade tests</h1>");
		
		// work with Facade
		
		try {
			transaction.begin();
			
			User u = facade.addUser(UUID.randomUUID().toString(), "name", "password", new byte[]{});	
			writer.println("User "+u.getLogin()+" created successfully");
			
			transaction.commit();
			
		} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
			try {
				transaction.rollback();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		writer.println("</body>");
		writer.println("</html>");
		
	}
}
```

Now, you can surf to 
[http://localhost:8080/web-0.0.1-SNAPSHOT/SimpleServlet](http://localhost:8080/web-0.0.1-SNAPSHOT/SimpleServlet)
to run the Servlet.

## Exercise 2: EJB
In this exercise, we will create a simple EJB layer. We will use the 
**Service project** for this purpose.

Use the package `es.uvigo.esei.dgss.exercise.service`.

### Task 1.
Create two EJB for general management of the social network.

- UserEJB, for retrieving, creating updating and removing users, as well as to
create friendships between them and to like posts.
- PostEJB, for retrieving, creating updating and retrieving posts, as well as to
add comments to them.

In order to test your EJBs, you can re-use your `SimpleServlet`. Inject your
EJBs inside the Servlet with the `@EJB` annotation.

### Task 2.
Create a StatisticsEJB, allowing you to retrieve the number of users and posts
in the social network. It should be very efficient (do not access to the DB everytime
it is queried) and shared for all users of the system (think in Singleton). That
is:

- Create a singleton EJB, which ONLY when it is started accesses the
database and counts users and posts to a private variable.
- When a user or a post is added, removed, you should call a singleton method
to notify this. The singleton updates its internal count.
- Give getter methods for the user and post counts.

Note: Take into account concurrency issues!

### Task 3.
Add an EmailService EJB. This EJB allow you to send an email to a given User:
`sendEmail(User u, String subject, String body)`. 

- This service should send emails asynchronously.
- In order to use this EJB, send an email to the post's author everytime a user
likes his post.
- Implement this service [using Java Mail inside Wildfly](http://khozzy.blogspot.com.es/2013/10/how-to-send-mails-from-jboss-wildfly.html).

## Exercise 3: JAX-RS
Coming soon...

## Exercise 4: AngularJS
Coming soon...

## Exercise 5: JSF
Coming soon...



  
