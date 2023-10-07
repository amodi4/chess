package chess;

import java.util.ArrayList;

public class King extends Piece {
    public King(String location, String color){
        super(location, color);
    }

    public boolean isValidMove(String destination, ArrayList<ReturnPiece> pieces){
        int newRank = Integer.parseInt(destination.substring(1));
        char newFile = destination.charAt(0);
        //If file or rank is out of bounds
        if (newFile < 'a' || newFile > 'h' || newRank < 1 || newRank > 8) {
            return false;
        }
        //If file and rank difference is less than or equal to 1 => Valid move so far
        if(Math.abs(newFile - this.file) <= 1 && Math.abs(newRank - this.rank) <= 1){
             //Get destination piece.
             ReturnPiece destinationPiece = null; //Have a variable that acts as a reference to the destination piece.
             for(ReturnPiece piece : pieces){
                 //If destination piece file and rank is the same as the newFile and newRank respectively
                 if(piece.pieceFile.name().equalsIgnoreCase(newFile + "") && piece.pieceRank == newRank){
                     //Looked for the right destination piece then.
                     destinationPiece = piece;
                 }
             }
             //If the piece at destination exists
             if(destinationPiece != null){
                 //Check to see if you can kill the piece by calling isCanKill() method.
                 //If you can kill
                 if(isCanKill(destinationPiece)){
                     //Just remove the destination piece from the arraylist
                     pieces.remove(destinationPiece);
                     return true;
                 }
                 //Otherwise, you can't kill your own piece, that is an illegal move
                 else return false;
             }else{
             //Otherwise
                 //You are free to move the rook at that position.
             return true;
        }
    }
        //For all other cases, it's an invalid move.
        return false;
    }

    //Method that takes in a destination piece to see if you can kill the destination piece 
    //using the source piece.
    public boolean isCanKill(ReturnPiece destinationPiece){
        //Check to see if destinationPiece color is different than sourcePiece color(Piece that you are trying to move.)
        return (destinationPiece.pieceType + "").charAt(0) != color.charAt(0);
    }
}
