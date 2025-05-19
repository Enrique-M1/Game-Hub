package application;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
public class Piece {
    Rectangle a ; 
    Rectangle b ; 
    Rectangle c ; 
    Rectangle d ; 
    Color color ; 
    private String name ;
    public int piece = 1 ; 

    public Piece (Rectangle a , Rectangle b, Rectangle c, Rectangle d) {
        this.a = a ;
        this.b = b ;
        this.c = c ;
        this.d = d ;

    }

    public Piece (Rectangle a , Rectangle b, Rectangle c, Rectangle d, String name) {
        this.a = a ;
        this.b = b ;
        this.c = c ;
        this.d = d ;
        this.name = name ; 


    switch(name) {
        case "j":
            color = Color.GREY ; 
            break ; 
        case "l":
            color = Color.DARKRED ; 
            break ; 
        case "o":
            color = Color.LIME ; 
            break ; 
        case "s":
            color = Color.BLACK ; 
            break ; 
        case "t":
            color = Color.FORESTGREEN ; 
            break ; 
        case "z":
            color = Color.DARKGOLDENROD ; 
            break ; 
        case "i":
            color = Color.INDIANRED; 
            break ; 
    }
    this.a.setFill(color);
    this.b.setFill(color);
    this.c.setFill(color);
    this.d.setFill(color);
    }

    public String getName() {
        return name ; 
    }

    public void transform() {
        if(piece !=4) {
            piece++ ; }
        else {
            piece = 1 ; }
    }


}
