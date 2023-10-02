package chess;

import java.util.ArrayList;

public class Knight extends Piece{
    public Knight(String location, String color){
        super(location, color);
    }

    public boolean isValidMove(String destination, ArrayList<ReturnPiece> pieces){
        return false;
    }
}
