<p align="center">
    <img src="assets/communikey-logo-light.svg.png"/>
</p>

<p align="center">
    <img src="https://api.travis-ci.org/communicode/communikey-backend.svg?branch=master"/>
    <img src="https://img.shields.io/badge/release-0.17.2-blue.svg"/>
</p>

# communikey

A simple, centralized, teambased, cross-platform credential manager using GPG encryption.

## Prerequisites

You will need the following things setup & ready for communikey-backend to work:

- [Java8/OpenJdk8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven](https://maven.apache.org/install.html)
- [Mysql](https://dev.mysql.com/doc/en/installing.html)

If you need a detailed guide on how to setup a new user & database follow this [link](https://www.digitalocean.com/community/tutorials/how-to-create-a-new-user-and-grant-permissions-in-mysql)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

- Clone the project using your favorable way of cloning a github project.

- Setup a database preferably named "communikey", with a password & username of your choice. See [prerequisites](#prerequisites) for detailed notes.

- Edit **application-dev.yml** in the **/src/main/resources** folder to reflect your new database, password & username you just created.

- Move into the project root folder and start it with maven
  
-  ``` mvn spring-boot run ```

- *Please ensure that port 8080 is free, since its used by the application*.

## Running tests

- Move into the project root folder and run the following command
  
-  ``` mvn integration-tests ```

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/communicode/communikey-backend/tags). 

## Built With

* [Spring Boot](https://projects.spring.io/spring-boot/) - The application framework used
* [Maven](https://maven.apache.org/) - Dependency Management 

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## License

This project is licensed under the GPLv3 license see the [LICENSE.md](LICENSE.md) file for details.


