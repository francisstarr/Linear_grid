
package starrf;
public class Line2 {
     
    private Vector2 point = new Vector2();
    private Vector2 direction = new Vector2();
    
    
    public Line2(){}
    public Line2(Vector2 point, Vector2 direction) {
        set(point,direction);
    }
    public Line2(double slope, double intercept){
        set (slope,intercept);
    }
    public Line2(double x1, double y1, double x2, double y2){
        set(x1,y1,x2,y2);
    }
    
  
    public void set(Vector2 point, Vector2 direction){
       this.point = point;
       this.direction = direction;
    }
    public void set(double slope, double intercept){
        point.x = 0;
        point.y = intercept;
        direction.x = 1;
        direction.y = slope;
    }
    public void set(double x1, double y1, double x2, double y2){
        point.x = x1;
        point.y = y1;
        direction.x = x1-x2;
        direction.y = y1-y2;
    }
    
 
    public void setPoint(Vector2 point){
        this.point = point;
    }
    public Vector2 getPoint(){
        return point;
    }
    
    public void setDirection(Vector2 direction){
        this.direction=direction;
    }
    public Vector2 getDirection(){
        return direction;
    }
    
    //step 7 of task 3
     public String toString(){
        return "Line\n====\n    Point: (" + point.x + ", " + point. y 
 + ")\nDirection: (" + direction.x + ", " + direction.y + ")";
    }
    
    /*
     The following method was not part of the assignment requirements but it
     was created for its usefulness in steps 8 and 9 of task 3. 
     Any line object can be expressed as a linear equation in the form ax + by = c. 
     This method returns the values of a,b,c in the form of length-3 array 
     called q where a=q[0], b=q[1], c=q[2]
     */
     private double[] linEquation(Line2 l){
        double [] q=new double [3];
        q[0] = l.direction.y;
        q[1] = -l.direction.x;
        q[2] = l.direction.y * l.point.x - l.direction.x * l.point.y;
    return q;
    }
    
     /*
     The following method was not part of the assignment requirements but it
     was created for its usefulness in steps 8 and 9 of task 3. 
    Suppose you have a 2x2 matrix with values [nw ne; sw se]. This method 
     returns the determinant of said 2X2 matrix.
     */
    private double det(double nw, double ne, double sw, double se){
        return nw*se-ne*sw; 
    }
    
    public boolean isIntersected(Line2 line){
        double [] q1 =linEquation(this);
        double [] q2 =linEquation(line);
        return det(q1[0],q1[1],q2[0],q2[1]) != 0;
    } 
   
    
    public Vector2 intersect(Line2 line) {
        Vector2 w = new Vector2();
        if (this.isIntersected(line)){
            double [] q1 =linEquation(this);
            double [] q2 =linEquation(line);
            double div = det(q1[0],q1[1],q2[0],q2[1]);
            w.x = det(q1[2],q1[1],q2[2],q2[1])/div;
            w.y = det(q1[0],q1[2],q2[0],q2[2])/div;
        }
        else {
            w.x = Double.NaN;
            w.y = w.x;
        }
        return w;
    }
    
    //displays the equation of the line as a string but if direction=(0,0) then it displays a single point
    public String displayEq(){
        String r;
        
        //line is a single point since direction=(0,0), coordinates rounded to nearest 0.1
        if(direction.x==0 && direction.y==direction.x) r = String.format("(%.1f, %.1f)", point.x, point.y);
        //vertical line equation, coefficient is rounded to nearest 0.01
        else if (direction.x==0)  r = String.format("x = %.2f",point.x);
        //horizontal line equation, coefficient is rounded to nearest 0.01
        else if (direction.y==0) r = String.format("y = %.2f",point.y);
        //a non-vertical/non-horizontal line, coefficients rounded to nearest 0.01
        else { 
            double rounder = Math.pow(10.0,2.0);
            double m = direction.y/direction.x;//y=mx + b
            double b = point.y - m * point.x; 
            char sign; //displays the sign of variable b
            if (b < 0) sign = '-';
            else sign ='+';
            r = "y = " + Math.round(rounder * m)/rounder + "x "+ sign +" " + Math.round(rounder * Math.abs(b))/rounder;
        }
        return r;
    }
    
    //displays the point of intersection between two lines
    public String displayIntersect(Line2 line){
        String r;
        if (this.isIntersected(line)){
            Vector2 v = this.intersect(line);
            r = String.format("(%.1f, %.1f)", v.x, v.y);
        }
        else {
            String noSoln = "No points of intersection";
            String infSoln= "Infinite points of intersection";
            Vector2 v = new Vector2 (this.point.x-line.point.x,this.point.y-line.point.y);
            //two parallel lines
            if (this.direction.getLength() > 0 && line.direction.getLength() > 0 ){
                //both lines are exactly the same line
                if (det(v.x,v.y,this.direction.x,this.direction.y)==0)r = infSoln;
                //two distinct parallel lines
                else r = noSoln; 
            }
            //(Line2 this) is a line but (Line2 line) is a single point
            else if (this.direction.getLength() > 0){
                //the single point is part of the line
                if (det(v.x,v.y,this.direction.x,this.direction.y)==0)r = String.format("(%.1f, %.1f)", line.point.x, line.point.y);
                //the single point is not part of the line
                else r = noSoln; 
            }
            //(Line2 this) is a single point but (Line2 line) is a line
            else if (line.direction.getLength() > 0){
                 //the single point is part of the line
                if (det(v.x,v.y,line.direction.x,line.direction.y)==0)r = String.format("(%.1f, %.1f)", this.point.x, this.point.y);
                //the single point is not part of the line
                else r = noSoln; 
            }
            //both lines are single points
            else {
                //the two single points are exactly the same
                if (this.point.x==line.point.x && this.point.y==line.point.y ) r = String.format("(%.1f, %.1f)", this.point.x, this.point.y);
                 //the two single points are distinct
                else r = noSoln; 
            }
        }
        return r;
    }
}
