import java.util.ArrayList;

public class PathFinding_Node {
  public int id;
  public ArrayList<Integer> neighbours = new ArrayList<Integer>();

  public PathFinding_Node(int id) {
    this.id = id;
  }

  public String toString() {
    String str = id + " : ";
    for(int i: neighbours)
      str += i + " ";
    return str;
  }
}
