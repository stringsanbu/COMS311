package cs311.hw7;
/**
 * MasonGraph.java
 * @author Mason
 * This is the implementation of the Graph interface received from Blackboard.
 */

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


public class CSGraph implements Graph<Object, Object> {
	Map<String, Vertex> vertices = new HashMap<String, Vertex>();
	Map<Integer, Edge> edges = new HashMap<Integer, Edge>();
	private boolean isDirected;
	private int vertexID = 0,
				edgeID = 0,
				numEdges = 0, 
				numVertices = 0;

	public CSGraph(boolean isDirected) {
		this.isDirected = isDirected;
	}
	
	@Override
	public boolean isDirected() {
		return this.isDirected;
	}

	@Override
	public void addVertex(String vertexLabel, Object vertexData) {
		Vertex vertex = new Vertex(this.vertexID, vertexData, vertexLabel);
		vertices.put(vertexLabel, vertex);
		this.vertexID++;
		this.numVertices++;
	}

	@Override
	public void removeVertex(String vertexLabel) {
		/*
		 * Process is like so:
		 * 1) Grab vertex being removed
		 * 2) Grab the associated edgeIDs
		 * 3) Iterate through each ID
		 * 3a) For each ID, get the vertex whose label is NOT vertexLabel
		 * 3b) Remove the ID from the associated IDs of that vertex
		 * 3c) Remove the edge from edges using the ID
		 * 3d) Remove the vertex from the opposite's neighbors
		 * 4) Remove the vertex from vertices.
		 */
		Vertex vertex = vertices.get(vertexLabel);
		ArrayList<Integer> edgeIDs = vertex.getEdges();
		for(Integer eID : edgeIDs){
			Edge edge = edges.get(eID);
			// This vertex is the one which will be existing still.
			String oppositeLabel = edge.getSourceLabel() != vertexLabel ? vertexLabel : edge.getTargetLabel(); 
			Vertex oppositeVertex = vertices.get(oppositeLabel);
			oppositeVertex.removeEdge(eID);
			oppositeVertex.removeNeighbor(vertexLabel);
			edges.remove(eID);
		}
		vertices.remove(vertexLabel);
		this.numVertices--;
	}

	@Override
	public void addEdge(String sourceLabel, String targetLabel, Object edgeData) {
		/*
		 * Process:
		 * 1) Create the edge
		 * 2) Add the edge to the hashmap
		 * 3) Add the edge ID to the associated IDs of each vertex
		 * 4a) If it is undirected, add each vertex as neighbors of each other
		 * 4b) If directed, the targetLabel is added as a neighbor of the source vertex.
		 */
		Edge edge = new Edge(sourceLabel, targetLabel, edgeData, this.edgeID);
		edges.put(this.edgeID, edge);
		Vertex sourceVertex = vertices.get(sourceLabel);
		Vertex targetVertex = vertices.get(sourceLabel);
		sourceVertex.addEdge(this.edgeID);
		targetVertex.addEdge(this.edgeID);
		sourceVertex.addNeighbor(targetLabel, this.edgeID);
		if(!isDirected) targetVertex.addNeighbor(sourceLabel, this.edgeID);
		this.edgeID++;
		this.numEdges--;
	}

	@Override
	public Object getEdgeData(String sourceLabel, String targetLabel) {
		Vertex sourceVertex = vertices.get(sourceLabel);
		Integer eID = sourceVertex.getNeighborEdge(targetLabel);
		return eID == null ? null : edges.get(eID).getData();
	}

	@Override
	public Object getVertexData(String label) {
		try{
			return vertices.get(label).getVertexData();
		}
		catch (Exception e){
			return null;	
		}
	}

	@Override
	public int getNumVertices() {
		return numVertices;
	}

	@Override
	public int getNumEdges() {
		return numEdges;
	}

	@Override
	public Collection<String> getVertices() {
		return vertices.keySet();
	}

	@Override
	public Collection<String> getNeighbors(String label) {
		try{
			return vertices.get(label).getNeighbors();
		}
		catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<String> topologicalSort() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> shortestPath(String startLabel, String destLabel, EdgeMeasure<Object> measure) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph<Object, Object> minimumSpanningTree(EdgeMeasure<Object> measure) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getTotalCost(EdgeMeasure<Object> measure) {
		double totalCost = 0;
		for(Entry<Integer, Edge> entry : edges.entrySet()){
			Edge edge = entry.getValue();
			totalCost += measure.getCost(edge.getData());
		}
		return totalCost;
	}
}