# DirectedWeightedGraphs

## Graph Implementation

Graph representation using a hashmap, the keys of the map are the vertice's id and the values are the vertices themselfs, each vertice v holds an adjancency list with a tweak, each map contains edges corresponding to the vertices adjacent to v, the keys of the adjancency map are pairs p <i,j> representing the source and destination of the edge. For example:

![image](https://i.imgur.com/eYPKpw7.png?1)

## Classes

| Class | Description |
| ------ | ------ |
| Ex2  | The main class for Ex2. |
| DirectedWeightedGraphAlgoImpl | Implementation of various graph algorithms |
| DirectedWeightedGraphImpl | Represents a Directional Weighted Graph. |
| MyNode | Represents the set of operations applicable on a node (vertex) in a (directional) weighted graph. |
| MyEdge | Represents the set of operations applicable on a directional edge(src,dest) in a (directional) weighted graph. |
| GeoLocationImpl | Represents a geo location <x,y,z>, (aka Point3D data). |
| MyNodeIterator | Implements the interface Iterator, throws exception if the graph was changed during an iteration |
| MyEdgeIterator | Implements the interface Iterator, throws exception if the graph was changed during an iteration |
| MyPair | Object representing the Edge source and destination nodes, holding two integers, used as key for Hashmap<MyPair, MyEdge>. |
| GUI | Running the GUI |
| MyFrame | Setup the window for the gui including the menu bar |
| MyPanel | Paint the graph |

# GUI - Features

##### File:
- Save/Load a json file.
##### Graph functions: 
- Add node.
- Remove node/edge.
- Connect (connect two nodes with an edge).
- Get node/edge (paint the requested node/edge red).
##### Algorithm functions:
- IsConnected - returns true/false if the graph is connected (all n*(n-1) pairs).
- Center - returns the key of the center node, also paints it green.
- Shortest Path - returns the list representing the path, if there is no path return -1, also paint the nodes of the path green
- Shortest Path Dist - return the distance between two nodes.
- TSP - return the list representing the path, also paint the nodes of the path green.

##### GUI Example:

![image](https://i.imgur.com/pmVtwLF.png)

# Algorithms Running Times:

> Note: Java ran out of space when creating graph with Million nodes.

| Algorithm \ Graph size | 1000 nodes | 10000 nodes | 100000 nodes | 1000000 nodes |
| ------ | ------ | ------ | ------ | ------ |
| isConnected | 38 ms | 294 ms | 7 sec 286 ms | 0 |
| TSP | 48 ms | 1 sec 597 ms | 1 min 34 sec (3 cities) | 0 |
| Center | 2 sec 613 ms | 10 min 37 sec | timeout | 0 |
| Shortest Path | 31 ms | 499 ms | 44 sec 763 ms | 0 |

# UML:

![image](https://i.imgur.com/gcpykQ9.png)

## How to Run:

Step 1: open cmd

Step 2: copy absolute path to jar file

![image](https://i.imgur.com/UgZZ9Fn.png)

Step 3: change directory to the path you just copied

Step 4: insert the following command:

```sh
java -jar Ex2.jar path\filename.json  
```
> Note: After downloading the project you should have 3 graphs in the jar folder, G1.json / G2.json / G3.json, you run them by entering their name, if you want to run another graph please provide the path to the json file directory as well as the name, for example: "out/artifacts/Ex2_jar/G1.json".

running the tester in terminal/cmd example:

![image](https://i.imgur.com/Asv0Ikj.png)

> Note:If having trouble with save/load functions after downloading the project, please make sure you have Gson 2.8.6 library or simply add it by going to Project Structure - Libraries - gson-2.8.6 and press apply.
