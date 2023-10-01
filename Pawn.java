package chess;

import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn(String location, String color){
        super(location, color);
    }

    public boolean isValidMove(String destination, ArrayList<ReturnPiece> pieces){
        int newRank = Integer.parseInt(destination.substring(1));
        char newFile = destination.charAt(0);
        if (newFile < 'a' || newFile > 'h' || newRank < 1 || newRank > 8) return false;
        if (this.rank > 2 && ((newRank- this.rank) > 1)) return false;
        if ((newRank - this.rank) > 2) return false;
        return true;
    }
}
