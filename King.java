package chess;

import java.util.ArrayList;

public class King extends Piece {
    public King(String location, String color){
        super(location, color);
    }

    public boolean isValidMove(String destination, ArrayList<ReturnPiece> pieces){
        return false;
    }
}
