package cs311.hw7;
/**
 * Edge.java
 * @author Mason
 * This is the class that will be used for every EDGE in the program.
 * It is written so if someone wants to create a subclass for directed
 * edges that it should work without a problem. The choice to only
 * use this class instead of a subclass is because we can use one 
 * vertex ID as the start and one as the end. If the graph is not directed
 * then we don't care which is the start and which is the end.
 */
public class Edge {
	private String sourceLabel;
	private String targetLabel;
	// Going to assume whatever the "data" attached to an edge is the weight..... Um ya.
	// protected int weight;
	private Object edgeData;
	// Adding a unique ID to all of the edges just for future use
	private int edgeID;
	
	/**
	 * Constructor for Edge class. The data for an edge will be an int for the weight.
	 * @param sourceLabel
	 * @param targetLabel
	 * @param weight
	 */
	public Edge(String sourceLabel, String targetLabel, Object edgeData, int edgeID) {
		this.sourceLabel = sourceLabel;
		this.targetLabel = targetLabel;
		this.edgeData = edgeData;
		this.edgeID = edgeID;
	}
	
	/* /**
	 * Returns the weight of the edge.
	 * @return The weight of the edge.
	 * /
	public int getWeight() {
		return this.weight;
	}*/
	
	/**
	 * Use to obtain the data stored in the edge.
	 * @return The edge's data.
	 */
	public Object getData() {
		return this.edgeData;
	}
	
	/**
	 * Returns the label of the source vertex of this edge.
	 * @return The label of the source vertex.
	 */
	public String getSourceLabel() {
		return this.sourceLabel;
	}
	
	/**
	 * Returns the label of the target vertex of this edge.
	 * @return The label of the target vertex.
	 */
	public String getTargetLabel() {
		return this.targetLabel;
	}
	
	/**
	 * Returns the id of the edge.
	 * @return The id of the edge.
	 */
	public int getID() {
		return this.edgeID;
	}
}
