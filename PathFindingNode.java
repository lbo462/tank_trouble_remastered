import java.util.ArrayList;


<<<<<<< HEAD
// used for BFS, defines a node inside the queue
// not used on this version, not enough time to finish the implementation of Djikstra
=======

>>>>>>> 09aa4aede69fc6280c09466c78f7a7c7c6706e7a
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
