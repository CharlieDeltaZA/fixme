# Fix-Me

### Score 119/100
#### Mandatory
100/100

#### Bonus
19/25

#### Project Overview:
3rd Java project @ WeThinkCode_

Summary: A stock exchange simulation using trading algorithms, networking and socket implementations

### Installing & Running:
Ensure you have the latest JRE & JDK (14 at time of development), as well as Maven and MongoDB Community Edition v4.4.

**Build with Maven:**
```
mvn clean package
```

**Optional - Setup a MongoDB instance to save transactions**

Install MongoDB:

https://docs.mongodb.com/manual/administration/install-community/

Start up a local MongoDB instance on MacOS (See relevant documentation for different OS):
```
brew services start mongodb-community@4.4
```
Verify that Mongo is running:
```
ps aux | grep -v grep | grep mongod
```

Run as follows (You'll need at least 3 terminal windows). It is possible to have up to 8 Brokers connected at a time.

Start in the following order: Router -> Market -> Broker(s)

**Window 1 - Router:**
```
cd ./router
java -jar ./target/router-1.0.0.jar
```
**Window 2 - Market:**

The Market has an optional flag `--save` to allow all transactions to be saved to a mongo database. The mongoDB must be configured in the step above.
```
cd ./market
java -jar ./target/market-1.0.0.jar [--save]
```
**Window X - Broker(s):**
```
cd ./broker
java -jar ./target/broker-1.0.0.jar
```

**To Verify content in Database:**

Open a new terminal, and run `mongo` to enter the mongo shell. 
Thereafter:
```
use fixme
db.transactions.find().pretty()
```