package chess;

import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(String location, String color){
        super(location, color);
    }

    public boolean isValidMove(String destination, ArrayList<ReturnPiece> pieces){
        return false;
    }
}