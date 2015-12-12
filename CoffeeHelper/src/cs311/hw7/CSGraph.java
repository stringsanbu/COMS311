package cs311.hw7;
/**
 * MasonGraph.java
 * @author Mason
 * This is the implementation of the Graph interface received from Blackboard.
 */

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;


public class CSGraph implements Graph<Object, Object> {
	Map<String, Vertex> vertices = new HashMap<String, Vertex>();
	Map<Integer, Edge> edges = new HashMap<Integer, Edge>();
	List<Edge> listOfEdges = new ArrayList<Edge>();
	List<String> noPredecessors = new ArrayList<String>();
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
		if(isDirected) noPredecessors.add(vertexLabel);
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
			listOfEdges.remove(edge);
		}
		noPredecessors.remove(vertexLabel);
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
		if(!isDirected) 
			targetVertex.addNeighbor(sourceLabel, this.edgeID);
		else
			noPredecessors.remove(targetLabel);
		this.edgeID++;
		this.numEdges--;
		listOfEdges.add(edge);
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
		// If it isn't directed
		if(!isDirected || noPredecessors.size() == 0) return null;
		if(noPredecessors.size() == 1 || noPredecessors.size() == vertices.size()) return noPredecessors;
		
		// To make this work, we'll be following Kahn's algorithm and need a copy of the edges
		// We can either make a list (which will make it slower) or just copy the map.
		Map<Integer, Edge> edgeCopy = new HashMap<Integer, Edge>();
		edgeCopy.putAll(edges);
		List<String> verticesToProcess = new ArrayList<String>(noPredecessors);	
		List<String> sortedVertices = new ArrayList<String>();
		
		// Time for the actual work
		// Process: 
		for(String vertexLabel : verticesToProcess){
			sortedVertices.add(vertexLabel);
			Vertex currentVertex = vertices.get(vertexLabel);
			for(Integer edgeID : currentVertex.getEdges()){
				edgeCopy.remove(edgeID);
				Vertex targetVertex = vertices.get(edgeCopy.get(edgeID).getTargetLabel());
				List<Integer> targetVertexEdges = targetVertex.getEdges();
				boolean noMoreEdgesFlag = true;
				// There might be a more elegant way of doing this
				for(Integer eID : targetVertexEdges){
					if(edgeCopy.containsKey(eID)){
						noMoreEdgesFlag = false;
						break;
					}
				}
				if(noMoreEdgesFlag) verticesToProcess.add(targetVertex.getVertexLabel());
			}
		}
		if(edgeCopy.size() > 0) return null;
		return sortedVertices;
	}

	@Override
	public List<String> shortestPath(String startLabel, String destLabel, EdgeMeasure<Object> measure) {
		// Basically implementing Dijkstras
		// First things first, we need to go through every vertex and reset their values
		List<String> path = new ArrayList<String>();
		List<Vertex> unvisted = new ArrayList<Vertex>();
		if(startLabel == destLabel){
			path.add(startLabel);
			return path;
		}
		if(edges.isEmpty()) return null;
		for(Entry<String, Vertex> entry : vertices.entrySet()){
			if(entry.getValue().getVertexLabel() == startLabel) 
				entry.getValue().setDistance(0);
			else
				entry.getValue().setDistance(Double.MAX_VALUE);
			entry.getValue().setPred(null);
			entry.getValue().setVisited(false);
			unvisted.add(entry.getValue());
		}
		Collections.sort(unvisted, new VertexComparator());
		Vertex currentVertex = unvisted.get(0);
		while(currentVertex != null){
			if(currentVertex.getVertexLabel() == destLabel) break;
			List<String> neighbors = currentVertex.getNeighbors();
			for(String neighborString : neighbors){
				Vertex neighbor = vertices.get(neighborString);
				if(!neighbor.isVisited()){
					Edge connectingEdge = edges.get(currentVertex.getNeighborEdge(neighborString));
					Double tentativeDistance = currentVertex.getDistance() + measure.getCost(connectingEdge);
					if(tentativeDistance < neighbor.getDistance()){
						neighbor.setDistance(tentativeDistance);
						neighbor.setPred(currentVertex);
					}
				}
			}
			currentVertex.setVisited(true);
			unvisted.remove(0);
			Collections.sort(unvisted, new VertexComparator());
			currentVertex = unvisted.get(0);
		}
		
		if(currentVertex.getVertexLabel() != destLabel) return null;
		// Time to go through the preds and make the list
		Vertex pred = currentVertex.getPred();
		while(pred != null){
			path.add(0, pred.getVertexLabel());
			pred = pred.getPred();
		}
		return path;
	}

	@Override
	public Graph<Object, Object> minimumSpanningTree(EdgeMeasure<Object> measure) {
		if(isDirected) return null;
		// Going to follow Kruskal's Algorithm... Because that's the only one I can remember from heart (no Internet on plane)
		// Hashmap<VERTEXDATA, EDGEDATA>
		HashMap<Vertex, Set<Vertex>> forest = new HashMap<Vertex, Set<Vertex>>();
		for(Entry<String, Vertex> entry : vertices.entrySet()){
			Vertex vertex = entry.getValue();
			Set<Vertex> vSet = new HashSet<Vertex>();
			vSet.add(vertex);
			forest.put(vertex, vSet);
		}
		EdgeMeasureComparator comparator = new EdgeMeasureComparator(measure);
		Collections.sort(listOfEdges, comparator);
		ArrayList<Edge> mst = new ArrayList<Edge>();
		while(true){
			Edge check = listOfEdges.remove(0);
			
			Set<Vertex> visited1 = forest.get(vertices.get(check.getSourceLabel()));
			Set<Vertex> visited2 = forest.get(vertices.get(check.getSourceLabel()));
			if(visited1.equals(visited2)) continue;
			mst.add(check);
			visited1.addAll(visited2);
			for(Vertex i : visited1){
				forest.put(i, visited1);
			}
			if(visited1.size() == vertices.size()) break;
		}
		return (Graph) mst;
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
