# Quizies

The goal of this whole project is to have a game where pupils can learn and have fun while doing it.
It's been thought to be a Trivial like question game. It also has in mind to be a flexible tool to be adapted to other scenarios. This is why there are several projects so the whole project is built from pieces that can be reused or adapted to other scenarios.

<div align='center'>
  <img alt="Quizies logo" src="miscellaneous/logo/logo.png" width="250">
</div>


## Description

As commented, this project wants to be modular and flexible. That's why is built with subprojects.

There are several projects:
 * **[QuizzEngine](QuizzEngine)**: library to load questions from backend. Persistency options so far are: json (main/default option), csv, sqlite and xml files).
 * **[GameEngine](GameEngine)**: The engine to run the game. It's flexible enough to allow diferent kind of games but still pretty inmature.
 * **[chatGPTQuestions](chatGPTQuestions)**: this project was build to create the first batch of questions to work with. It's uploaded for educational purposes but it's not needed to run the game.
 * **[WebServer](WebServer)**: Spring Boot web server with STOMP protocol on top of web sockets.
   * **WebClient** -> Web client: Inside the WebServer project we have a small web client to start with and testing STOMP.
 * **[AndroidClient](AndroidClient)**: Android client on kotlin an using jetpack compose for the UI.


## System Diagram

In order to better understand who the architecture of the system let's show a System Diagram.

<div align='center'>
  <img alt="Quizies logo" src="miscellaneous/diagram/system_diagram.png" >
</div>

## Authors

Contributors names and contact info

* [Profesor Diego Rosado](https://github.com/ProfesorDiegoRosado)  


## Version History

* 0.1
    * Not released yet

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

