# Fix-Me

### Score TBC/100
#### Mandatory
TBC/100

#### Bonus
TBC/25

#### Project Overview:
3rd Java project @ WeThinkCode_

Summary: A stock exchange simulatio using trading algorithms, networking and socket implementations

### Installing & Running:
Ensure you have the latest JRE & JDK (14 at time of development), as well as Maven.

Build with Maven:
```
mvn clean package
```

Run as follows (You'll need at least 3 terminal windows). It is possible to have up to 8 Brokers connected at a time.

Start in the following order: Router > Market > Brokers

**Window 1 - Router:**
```
cd ./router
java -jar ./target/router-1.0.0.jar
```
**Window 2 - Market:**
```
cd ./market
java -jar ./target/market-1.0.0.jar
```
**Window X - Broker(s):**
```
cd ./broker
java -jar ./target/broker-1.0.0.jar
```
