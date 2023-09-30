package chess;

import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn(String location, String color){
        super(location, color);
    }

    public boolean isValidMove(String destination, ArrayList<ReturnPiece> pieces){
        if (this.rank > 2 && ((Integer.parseInt(destination.substring(1)) - this.rank) > 1)) return false;
        if ((Integer.parseInt(destination.substring(1)) - this.rank) > 2) return false;
        return true;
    }
}
