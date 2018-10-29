package starrf;
public class Vector2 {
        
    public double x;
    public double y;
    
    public Vector2(){}
    public Vector2(double x, double y){
        set(x,y);
    }
    
    public void set (double x, double y){
        this.x = x;
        this.y = y;
    }
    public void set (Vector2 v){
        set(v.x,v.y);
    }

    public String toString(){
        return "Vector2(" + x + ", " + y + ")";
    }
    
    public Vector2 clone(){
        Vector2 cloned = new Vector2(x,y);
        return cloned;
    }
    
    //step 7 of task 2
    public Vector2 add(Vector2 rhs) {
        Vector2 added = new Vector2(this.x + rhs.x, this.y + rhs.y);
        this.set(added);
        return this;
    }
    
    //step 8 of task 2
    public Vector2 subtract(Vector2 rhs){
        Vector2 subtracted = new Vector2(this.x - rhs.x, this.y - rhs.y);
        this.set(subtracted);
        return this;
    }
    
    //step 9 of task 2
    public Vector2 scale(double scalar){
        Vector2 scaled = new Vector2(x * scalar, y * scalar);
        this.set(scaled);
        return this;
    }
    
    //step 10 of task 2
    public double dot(Vector2 rhs){
        double dotted = rhs.x * this.x + rhs.y * this.y;
        return dotted;
    }
    
    //step 11 of task 2
    public double getLength(){
        double length = Math.sqrt(this.dot(this));
        return length;
    }
    
    //step 12 of task 2
    public Vector2 normalize(){
        Vector2 v = new Vector2();
        double length = this.getLength();
        v = this.scale(1/length);
        this.set(v);
        return this;
    }
    
}
