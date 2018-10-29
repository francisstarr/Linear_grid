package starrf;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class MainFormController implements Initializable
{
    // constants
    private static final int PADDING = 10;      // pixels
    private static final int UNIT_COUNT = 10;   // # of units only on positive side

    // member vars
    private int width;                  // width of drawing area
    private int height;                 // height of drawing area
    private int centerX;                // center X in screen space
    private int centerY;                // center y in screen space
    private double mouseX;              // screen coordinate x
    private double mouseY;              // screen coordinate y
    private double coordRatio;          // map screen coord to logical coord, s/l
    private double coordX;              // logical coordinate x
    private double coordY;              // logical coordinate y
    private Line[] hLines;              // horizontal grid lines
    private Line[] vLines;              // vertical grid lines

    // JavaFX controls
    private Rectangle rectClip;         // clipping rectangle
    @FXML
    private Pane paneView;
    @FXML
    private Pane paneControl;
    @FXML
    private Label labelCoord;
    @FXML//middle line segment for line 1
    private Line line1a;
    @FXML//edge linge segment for line 1, see updateLines()
    private Line line1b;
    @FXML//edge linge segment for line 1, see updateLines()
    private Line line1c;

    @FXML//middle line segment for line 2
    private Line line2a;
    @FXML//edge linge segment for line 2, see updateLines()
    private Line line2b;
    @FXML//edge linge segment for line 2, see updateLines()
    private Line line2c;
    
    @FXML
    private Circle point1a;
    @FXML
    private Circle point1b;
    @FXML
    private Circle point2a;
    @FXML
    private Circle point2b;
    @FXML
    private Circle pointIntersect;
    
    @FXML//slider for x-value of point1a
    private Slider sliderX1;
    @FXML//label to display x-value of point1a
    private Label labelX1;
    @FXML//slider for y-value of point1a
    private Slider sliderY1;
    @FXML//label to display y-value of point1a
    private Label labelY1;
    
    @FXML//slider for x-value of point1b
    private Slider sliderX2;
    @FXML//label to display x-value of point1b
    private Label labelX2;
    @FXML//slider for y-value of point1b
    private Slider sliderY2;
    @FXML//label to display y-value of point1b
    private Label labelY2;
    
    @FXML//slider for x-value of point2a
    private Slider slider2X1;
    @FXML//label to display x-value of point2a
    private Label label2X1;
    @FXML//slider for y-value of point2a
    private Slider slider2Y1;
    @FXML//label to display y-value of point2a
    private Label label2Y1;
    
    @FXML//slider for x-value of point2b
    private Slider slider2X2;
    @FXML//label to display x-value of point2b
    private Slider slider2Y2;
    @FXML//slider for y-value of point2a
    private Label label2X2;
    @FXML//label to display y-value of point2a
    private Label label2Y2;
    
    @FXML//label to display the point of intersection
    private Label labelBlack;
    @FXML//label to display line equation formed by point1a & point1b, see updateLines()
    private TitledPane title1;
    @FXML//label to display line equation formed by point2a & point2b, see updateLines()
    private TitledPane title2;
   
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
       resetAll();
       
       //binding the labels to its respective slider value for the sliders of point1a & point1b
       labelX1.textProperty().bind(sliderX1.valueProperty().asString("%.1f"));
       labelY1.textProperty().bind(sliderY1.valueProperty().asString("%.1f"));
       labelX2.textProperty().bind(sliderX2.valueProperty().asString("%.1f"));
       labelY2.textProperty().bind(sliderY2.valueProperty().asString("%.1f"));
    
        //binding the line segment that connects point1a & point1b to its respective (x,y) position
      line1a.startXProperty().bind(point1a.centerXProperty());
       line1a.startYProperty().bind(point1a.centerYProperty());
       line1a.endXProperty().bind(point1b.centerXProperty());
      line1a.endYProperty().bind(point1b.centerYProperty());
        
       //binding the labels to its respective slider value for the sliders of point2a & point2b
       label2X1.textProperty().bind(slider2X1.valueProperty().asString("%.1f"));
       label2Y1.textProperty().bind(slider2Y1.valueProperty().asString("%.1f"));
       label2X2.textProperty().bind(slider2X2.valueProperty().asString("%.1f")); 
       label2Y2.textProperty().bind(slider2Y2.valueProperty().asString("%.1f"));
      
       //binding the line segment that connects point2a & point2b to its respective (x,y) position
       line2a.startXProperty().bind(point2a.centerXProperty());
       line2a.startYProperty().bind(point2a.centerYProperty());
       line2a.endXProperty().bind(point2b.centerXProperty());
       line2a.endYProperty().bind(point2b.centerYProperty());
       
       ChangeListener listener = (ov, oldV, newV) -> updateLines(); 
       sliderX1.valueProperty().addListener(listener);
       sliderY1.valueProperty().addListener(listener);
       sliderX2.valueProperty().addListener(listener);
       sliderY2.valueProperty().addListener(listener);
       slider2X1.valueProperty().addListener(listener);
       slider2Y1.valueProperty().addListener(listener);
       slider2X2.valueProperty().addListener(listener);
       slider2Y2.valueProperty().addListener(listener);
       initGrid();
        // set clip region for drawing area
        rectClip = new Rectangle(500, 500);
        paneView.setClip(rectClip);

        // update width and height of drawing area
        ChangeListener resizeListener = (ov, oldV, newV) -> handleViewResized();
        paneView.widthProperty().addListener(resizeListener);
        paneView.heightProperty().addListener(resizeListener);
    }



    ///////////////////////////////////////////////////////////////////////////
    @FXML
    private void handleMouseMoved(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
        coordX = (mouseX - centerX) / coordRatio;
        coordY = (height - mouseY - centerY) / coordRatio;
        labelCoord.setText(String.format("(%.1f, %.1f)", coordX, coordY));
         
    }

    ///////////////////////////////////////////////////////////////////////////
    @FXML
    private void handleMouseDragged(MouseEvent event){
        mouseX = event.getX();
        mouseY = event.getY();
        coordX = (mouseX - centerX) / coordRatio;
        coordY = (height - mouseY - centerY) / coordRatio;
        labelCoord.setText(String.format("(%.1f, %.1f)", coordX, coordY));
    }



    ///////////////////////////////////////////////////////////////////////////
    @FXML
    private void handleMousePressed(MouseEvent event){}
    ///////////////////////////////////////////////////////////////////////////
    @FXML
    private void handleMouseReleased(MouseEvent event){}

    ///////////////////////////////////////////////////////////////////////////
    @FXML
    private void handleMouseExited(MouseEvent event){       
        labelCoord.setText("");
    }

    ///////////////////////////////////////////////////////////////////////////
    private void initGrid() {
        int lineCount = UNIT_COUNT * 2 + 1; // both side plus 1 at enter
        hLines = new Line[lineCount];
        vLines = new Line[lineCount];

        // create line objects
        for(int i = 0; i < lineCount; ++i){
            hLines[i] = new Line();
            hLines[i].setStrokeWidth(0.2);
            hLines[i].setStroke(Color.GRAY);
            paneView.getChildren().add(hLines[i]);
            hLines[i].toBack();

            vLines[i] = new Line();
            vLines[i].setStrokeWidth(0.2);
            vLines[i].setStroke(Color.GRAY);
            paneView.getChildren().add(vLines[i]);
            vLines[i].toBack();
        }

        // for center line
        hLines[lineCount / 2].setStroke(Color.BLACK);
        hLines[lineCount / 2].setStrokeWidth(0.4);
        vLines[lineCount / 2].setStroke(Color.BLACK);
        vLines[lineCount / 2].setStrokeWidth(0.4);

        // layout grid lines
        updateGrid();
    }
    ///////////////////////////////////////////////////////////////////////////
    private void handleViewResized(){
        width = (int)paneView.getWidth();
        height = (int)paneView.getHeight();

        // compute the ratio of scrren to virtual = s / v
        double dim = Math.min(width, height) - (PADDING * 2);
        coordRatio = dim / (UNIT_COUNT * 2.0);

        centerX = (int)(width * 0.5 + 0.5);
        centerY = (int)(height * 0.5 + 0.5);
        
        // update clipping region
        rectClip.setWidth(width);
        rectClip.setHeight(height);

        updateGrid();
        updateLines();
    }
    //////////////////////////////////////////////////////////////////////////
    private void updateGrid(){
        int dim;    // square dimension
        int xGap, yGap;

        if(width > height)  {
            dim = height - (PADDING * 2);
            xGap = (int)((width - dim) * 0.5 + 0.5);
            yGap = PADDING;
        }
        else {
            dim = width - (PADDING * 2);
            xGap = PADDING;
            yGap = (int)((height - dim) * 0.5 + 0.5);
        }
        double step = dim / (UNIT_COUNT * 2.0);

        for(int i = 0; i < hLines.length; ++i){
            hLines[i].setStartX(xGap);
            hLines[i].setStartY(yGap + (int)(step * i + 0.5));
            hLines[i].setEndX(width - xGap);
            hLines[i].setEndY(yGap + (int)(step * i + 0.5));

            vLines[i].setStartX(xGap + (int)(step * i + 0.5));
            vLines[i].setStartY(yGap);
            vLines[i].setEndX(xGap + (int)(step * i + 0.5));
            vLines[i].setEndY(height - yGap);
        }
    }



    ///////////////////////////////////////////////////////////////////////////
    private void updateLines()
    {
        double l1x1 = (double)sliderX1.getValue(); 
        double l1y1 = (double)sliderY1.getValue();
        double l1x2 = (double)sliderX2.getValue(); 
        double l1y2 = (double)sliderY2.getValue();
   
        double l2x1 = (double)slider2X1.getValue(); 
        double l2y1 = (double)slider2Y1.getValue();
        double l2x2 = (double)slider2X2.getValue(); 
        double l2y2 = (double)slider2Y2.getValue();
        /////////
        Vector2 p1a = new Vector2(l1x1,l1y1);
        point1a.setCenterX(p1a.x * coordRatio + centerX);
        point1a.setCenterY(-p1a.y * coordRatio + centerY);
        
        Vector2 p1b = new Vector2(l1x2,l1y2);
        point1b.setCenterX(p1b.x * coordRatio + centerX);
        point1b.setCenterY(-p1b.y * coordRatio + centerY);
       
        Vector2 p2a = new Vector2(l2x1,l2y1);
        point2a.setCenterX(p2a.x * coordRatio + centerX);
        point2a.setCenterY(-p2a.y * coordRatio + centerY);
        
        Vector2 p2b = new Vector2(l2x2,l2y2);
        point2b.setCenterX(p2b.x * coordRatio + centerX);
        point2b.setCenterY(-p2b.y * coordRatio + centerY);

     ///////////// 
    
     //this unit vector represents the direction of line1
        Vector2 v =new Vector2();
        v.x=l1x1-l1x2;
        v.y=l1y1-l1y2;
        v.normalize();
        
        //this vector represents point1a
        Vector2 p1 =new Vector2(l1x1,l1y1);
        p1.add(v.clone().scale(50));
        line1b.setStartX(point1a.getCenterX()); 
        line1b.setStartY(point1a.getCenterY());
        line1b.setEndX(p1.x * coordRatio + centerX); 
        line1b.setEndY(-p1.y * coordRatio + centerY);
        
        //this vector represents point1b
        Vector2 p2 =new Vector2(l1x2,l1y2);
        p2.add(v.clone().scale(-50));
        line1c.setStartX(point1b.getCenterX()); 
        line1c.setStartY(point1b.getCenterY());
        line1c.setEndX(p2.x * coordRatio + centerX); 
        line1c.setEndY(-p2.y * coordRatio + centerY);
               
        //this unit vector represents the direction of line2
        Vector2 w =new Vector2();
        w.x=l2x1-l2x2;
        w.y=l2y1-l2y2;
        w.normalize();
       
         //this vector represents point2a
        Vector2 p3 =new Vector2(l2x1,l2y1);
        p3.add(w.clone().scale(50));
        line2b.setStartX(point2a.getCenterX()); 
        line2b.setStartY(point2a.getCenterY());
        line2b.setEndX(p3.x * coordRatio + centerX); 
        line2b.setEndY(-p3.y * coordRatio + centerY);
        
         //this vector represents point2b
        Vector2 p4 =new Vector2(l2x2,l2y2);
        p4.add(w.clone().scale(-50));
        line2c.setStartX(point2b.getCenterX()); 
        line2c.setStartY(point2b.getCenterY());
        line2c.setEndX(p4.x * coordRatio + centerX); 
        line2c.setEndY(-p4.y * coordRatio + centerY);
        
        Line2 lin1= new Line2(l1x1,l1y1,l1x2,l1y2);
        Line2 lin2 = new Line2(l2x1,l2y1,l2x2,l2y2);
        
        //this vector represents the point of intersection between lin1 & lin2
        Vector2 blackPoint= lin1.intersect(lin2);
        pointIntersect.setCenterX(blackPoint.x * coordRatio + centerX);
        pointIntersect.setCenterY(-blackPoint.y * coordRatio + centerY);
        
        //this is the label to display point of intersection between lin1 & lin2 
        //if there is no singular point of intersection (infinite or none) 
        //then that will be displayed instead
        labelBlack.setText(lin1.displayIntersect(lin2));
        
        
        title1.setText("Line One: "+ lin1.displayEq());
        title2.setText("Line Two: "+ lin2.displayEq());
    }
    
    //resets all slider values to their original state
   //invoked both at launch & when the reset button is clicked
    private void resetAll(){
       sliderX1.setValue(5.0);
       sliderY1.setValue(5.0);
       sliderX2.setValue(-5.0);
       sliderY2.setValue(-5.0);
       slider2X1.setValue(5.0);
       slider2Y1.setValue(-5.0);
       slider2X2.setValue(-5.0);
       slider2Y2.setValue(5.0);
    }
    
    @FXML
    private void resetAll(ActionEvent event) {
        resetAll();
    }

    
}
