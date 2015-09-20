# ESI Exercises

This repository contains the code base for the exercises of the ESI subject inside the DGSS itinerary.

## Environment

The environment is based on Maven 3, MySQL 5.5, Wildfly 8.2 and Eclipse Mars JEE.

We will work with Git to get updates of these exercises.

We will work with two Git repositories inside the http://sing.ei.uvigo.es/dt/gitlab server

1. The main repository (read-only for students)

    Git url: `http://sing.ei.uvigo.es/dt/gitlab/dgss/esi-exercises.git`

2. The student's solution repository. Surf to [our Gitlab server](http://sing.ei.uvigo.es/dt/gitlab) and create a user
with your @esei.ei.uvigo.es email account. If your username is `bob`, create a **PRIVATE** project `bob-esi-solutions`

    Git url: `http://sing.ei.uvigo.es/dt/gitlab/bob/bob-esi-solutions.git`

## Exercise 1: JPA

### Task 1.
Inside the **domain project**, create a set of JPA entities given the ER model
you can find in the ER.png file.

Use this package for your entities: `es.uvigo.esei.dgss.exercises.domain`

### Task 2.
Inside the **web project**, create a Facade class containing one method 
per each query in the following list.

Use this package: `es.uvigo.esei.dgss.exercises.web`

1. Create a new user given its login, name, password and picture
2. Create a friendship between two given users
3. Get all friends of a given user
4. Get all posts of the friends of a given user
5. Get the posts that have been commented by the friends of a given user after a given date
6. Get the users which are friends of a given user who like a given post
7. Give me all the pictures a given user likes
8. Create a list of potential friends for a given user (feel free to create you own "algorithm")


## Exercise 2: EJB
Coming soon...

## Exercise 3: JAX-RS
Coming soon...

## Exercise 4: AngularJS
Coming soon...

## Exercise 5: JSF
Coming soon...

## Appendix 2: working with git

### Clone the remote repository  
    git clone http://sing.ei.uvigo.es/dt/gitlab/dgss/esi-exercises.git
    cd esi-exercises

### Prepare for pushing into your own repository
    git remote set-url origin http://sing.ei.uvigo.es/dt/gitlab/bob/bob-esi-solutions.git

### Start coding your solution
Create a branch for your solution:

    git checkout -b solution

### Commit your changes
    git add .
    git commit   

### Pushing your changes to your remote repository
   git push origin solution

  
