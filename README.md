# A Distributed Key Value Store
A basic implementation of a distributed key-value store where the nodes are sharded based on Hash Partitioning and there is peer-peer communication between the nodes of the distributed cluster. There is no master/slave node, every node assumes the same role in the cluster.

## Configuration File
The cluster is configured using a configuration file. The configuration file has 2 main sections/properties :
1. ShardGroups
2. MyGroup

### ShardGroups
The cluster are divided into groups of shards where every node within the shard group acts as a replica. The shard groups are meant for distributing the data across nodes. If all the nodes of a shard group go down then the cluster is rendered partially unusable as all the keys falling under the hash range of that particular shard group are rendered non-retrievable and also key-value pairs falling under that hash range cannot be written. 

The format of specifying a Shard Group in the configuration file is:

```
Shard_Group_1_Name:
  - Node1
  - Node2
  .
  .
  - NodeN
Shard_Group_2_Name:
  - Node1
  - Node2
  .
  .
  - NodeN
.
.
.
Shard_Group_N_Name:
  - Node1
  - Node2
  .
  .
  - NodeN
````

### MyGroup
This property of the configuration file saecifies the shard group of the node using the cluster configuration file.


## Building the Key Value Store
To build the project clone the project onto your local git and then run the following commands from within the base project folder.

```
cd distributed-key-value-store
```
```
mvn clean install
```
You'll find the <b>distributed-kv-store-1.0-SNAPSHOT.jar</b> artifact under the target/ folder. Use that to start individual nodes in the distributed cluster.




## Creating a cluster and running the Distributed Key Value Store

Perform the following steps to start a distributed cluster composed of 2 Shard Groups and 2 nodes in each group. We'll make use of the 2 configuration files on the base project folder.

1. Start the first node in the Shard Group 1 on the port 8900 using the following command:

For Windows (Using Cygwin),

``` 
java -jar target/distributed-kv-store-1.0-SNAPSHOT.jar --server.port=8900 --baseFolder=.\\data\\8900 --clusterConfig=..\\cluster-conf.yaml
```

For Linux,

``` 
java -jar target/distributed-kv-store-1.0-SNAPSHOT.jar --server.port=8900 --baseFolder=./data/8900 --clusterConfig=../cluster-conf.yaml
``` 

2. Start the second node in the Shard Group 1 on the port 8910 using the following command:

For Windows (Using Cygwin),

``` 
java -jar target/distributed-kv-store-1.0-SNAPSHOT.jar --server.port=8910 --baseFolder=.\\data\\8910 --clusterConfig=..\\cluster-conf.yaml
``` 

For Linux,

``` 
java -jar target/distributed-kv-store-1.0-SNAPSHOT.jar --server.port=8910 --baseFolder=./data/8910 --clusterConfig=../cluster-conf.yaml
``` 


3. Start the third node in the Shard Group 2 on the port 8800 using the following command:

For Windows (Using Cygwin),

``` 
java -jar target/distributed-kv-store-1.0-SNAPSHOT.jar --server.port=8800 --baseFolder=.\\data\\8800 --clusterConfig=..\\cluster-conf_2.yaml
``` 

For Linux,

``` 
java -jar target/distributed-kv-store-1.0-SNAPSHOT.jar --server.port=8800 --baseFolder=./data/8800 --clusterConfig=../cluster-conf_2.yaml
``` 



4. Start the fourth node in the Shard Group 2 on the port 8810 using the following command:

For Windows (Using Cygwin),

``` 
java -jar target/distributed-kv-store-1.0-SNAPSHOT.jar --server.port=8810 --baseFolder=.\\data\\8810 --clusterConfig=..\\cluster-conf_2.yaml
``` 

For Linux,

``` 
java -jar target/distributed-kv-store-1.0-SNAPSHOT.jar --server.port=8810 --baseFolder=./data/8810 --clusterConfig=../cluster-conf_2.yaml
``` 


Once all the nodes are started, you'll then have a fully functioning cluster.


## Using the Distributed Key Value Store

The endpoints definitions are :

To add data to key-value store ->  

<b>Method</b> - POST 
  <b>Path</b> - /set/{key} 
  <b>Payload</b> - value in the Request Body

To retreive data from key-value store ->  

<b>Method</b> - GET 
<b>Path</b> - /get/{key} 


Once the cluster is up and running, you can add data to the key value store using the following command:

```
curl -XPOST -H "Content-Type: text/plain" http://localhost:8900/set/foo --data "bar"
``` 

You can retreive data using the following command:

``` 
curl -XGET -H "Content-Type: text/plain" http://localhost:8800/get/foo
``` 




