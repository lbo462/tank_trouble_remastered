import java.util.ArrayList;



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
