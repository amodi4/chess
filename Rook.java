package chess;

import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(String location, String color){
        super(location, color);
    }

    public boolean isValidMove(String destination, ArrayList<ReturnPiece> pieces){
        return false;
    }
}
