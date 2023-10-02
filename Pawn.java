package chess;

import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn(String location, String color){
        super(location, color);
    }

    public boolean isValidMove(String destination, ArrayList<ReturnPiece> pieces){
        int newRank = Integer.parseInt(destination.substring(1));
        char newFile = destination.charAt(0);
        //If file or rank is out of bounds
        if (newFile < 'a' || newFile > 'h' || newRank < 1 || newRank > 8) {
            return false;
        }
        //If the pawn is in one of the middle 4 rows, can only move one space forward ->
        //Represents second move of pawn
        else if (this.rank > 2 && ((newRank- this.rank) > 1)) return false;
        //Pawn's first move -> Can only move one or two tiles forward.
        else if ((newRank - this.rank) > 2) return false;
        //Pawn went up and/or moves sideways or not moved at all.
        else if (newRank == this.rank || (newFile-this.file.charAt(0) > 1)) return false;
        //Pawn moved backwards.
        else if (newRank > this.rank) return false;
        //Get destination piece.
        ReturnPiece destinationPiece = null; //Have a variable that acts as a reference to the destination piece.
        for(ReturnPiece piece: pieces){
            //If destination piece file and rank is the same as the newFile and newRank respectively
            if(piece.pieceFile.name().equalsIgnoreCase(newFile + "") && piece.pieceRank == newRank){
                //Looked for the right destination piece then.
                destinationPiece = piece;
            }
        }
        //If the destinationPiece is not null, that means it exists in the Chess board
        if(destinationPiece != null){
            //Call the isCanKill() method to figure out whether you can kill.
            return isCanKill(destinationPiece, pieces);
        }
                //If you can kill, then it's a valid move.
        //Otherwise
        else{
            //If the destination is diagonal to the source, you can't move the pawn diagonally at an empty position.
             if(destinationPiece.pieceRank-this.rank == 1 && destinationPiece.pieceFile.name().charAt(0)-this.file.charAt(0) == 1) return false;
        }
        return true;
    }

    //Method that takes in a destination piece and figures out whether you can kill that piece or not.
    public boolean isCanKill(ReturnPiece destination, ArrayList<ReturnPiece> boardPieces){
        //If both source and destination pieces make a diagonal (destination is one up, one down from source) AND are of different colors
            //Then you can kill.
        if(destination.pieceRank-this.rank == 1 && destination.pieceFile.name().charAt(0)-this.file.charAt(0) == 1 
        && destination.pieceType.name().charAt(0) != this.color.charAt(0)){
            //Remove destination from the ArrayList
            boardPieces.remove(destination);
            //Return true to allow the piece to move at that position.
            return true;
        }
        //Otherwise you can't.
        return false;
    }
}
