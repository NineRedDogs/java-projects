# eventsourcing-example
Example in-memory Event Sourcing use implementation for banking domain. 


## exercise 1
![alt tag](https://raw.githubusercontent.com/michal-lipski/eventsourcing-example/master/event_store_exercise_1.png)
- Provide simple in-memory implementation of Event Store
- Make all test passing using event sourcing 

## excercise 1a (optional)
- Implement [Unit of Work](https://martinfowler.com/eaaCatalog/unitOfWork.html) pattern

## exercise 1b (optional)
- Implement Projections on Account to get number of transactions performed on account

## exercise 2
![alt tag](https://raw.githubusercontent.com/michal-lipski/eventsourcing-example/master/event_store_exercise_2.png)
- Refactor to move all money transfer related stuff to separate aggregate
- New aggregate will be also using Event Store

## exercise 3
![alt tag](https://raw.githubusercontent.com/michal-lipski/eventsourcing-example/master/event_store_exercise_3.png)
- Apply CQRS rule and separate the command and reading side
- Solution will use Eventual Consistency approach

## exercise 5
- Provide additional (not transient) implementation of Event Store. (https://geteventstore.com/)

