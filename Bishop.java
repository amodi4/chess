package chess;

import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(String location, String color){
        super(location, color);
    }

    public boolean isValidMove(String destination, ArrayList<ReturnPiece> pieces){
        return false;
    }
}
