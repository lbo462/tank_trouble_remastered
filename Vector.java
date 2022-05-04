
// Vector type, a simple element with an x and y component
// very useful
public class Vector {
  public int x, y;

  public Vector() {} // creates an empty vector

  public Vector(int x, int y) {
    this.x = x; this.y = y;
  }

  public Vector copy() {
    return new Vector(this.x, this.y);
  }

  public String toString() {
    return "x="+x+" y="+y;
  }
} 
