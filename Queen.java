package chess;

import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(String location, String color){
        super(location, color);
    }

    public boolean isValidMove(String destination, ArrayList<ReturnPiece> pieces){
        int newRank = Integer.parseInt(destination.substring(1));
        char newFile = destination.charAt(0);
        //If file or rank is out of bounds
        if (newFile < 'a' || newFile > 'h' || newRank < 1 || newRank > 8) {
            return false;
        }
        return false;
    }
}
