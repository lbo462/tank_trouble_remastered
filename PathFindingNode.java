import java.util.ArrayList;


// used for BFS, defines a node inside the queue
// not used on this version, not enough time to finish the implementation of Djikstra
public class PathFindingNode {
  public int id;
  public ArrayList<Integer> neighbours = new ArrayList<Integer>();

  public PathFindingNode(int id) {
    this.id = id;
  }

  public String toString() {
    String str = id + " : ";
    for(int i: neighbours)
      str += i + " ";
    return str;
  }
}
