public class Vector {
  public int x, y;

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
