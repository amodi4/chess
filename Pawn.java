package chess;

import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn(String location, String color){
        super(location, color);
    }

    public boolean isValidMove(String destination, ArrayList<ReturnPiece> pieces){
        //If the pawn is in the middle 4 rows and is moved more than 1 row, then that's not valid.
        if (this.rank > 2 && ((Integer.parseInt(destination.substring(1)) - this.rank) > 1)) return false;
        //If you move the pawn more than two pieces from where the pawn started off with, then that's not valid.
        if ((Integer.parseInt(destination.substring(1)) - this.rank) > 2) return false;
        return true;
    }
}
