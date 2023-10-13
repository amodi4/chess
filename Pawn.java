package chess;

import java.util.ArrayList;

public class Pawn extends Piece {
    String promotionPiece;
    public Pawn(String location, String color, String promotionPiece){
        super(location, color);
        this.promotionPiece = promotionPiece;
    }

    public boolean isValidMove(String destination, ArrayList<ReturnPiece> pieces){
        int newRank = Integer.parseInt(destination.substring(1));
        char newFile = destination.charAt(0);
        //If file or rank is out of bounds
        if (newFile < 'a' || newFile > 'h' || newRank < 1 || newRank > 8) {
            return false;
        }
        //If the pawn is in one of the middle 4 rows, can only move one space forward ->
        //Represents second move of pawn onwards --> white cannot move backwards or more than 2 spaces up, black cannot move backwards or more than 2 spaces down
        else if (this.color.charAt(0) == 'W' && ((this.rank > 2 && ((newRank- this.rank) > 1)) || newRank < this.rank)) return false;
        else if (this.color.charAt(0) == 'B' && ((this.rank < 7 && ((this.rank - newRank) > 1)) || newRank > this.rank)) return false;
        //Pawn's first move -> Can only move one or two tiles forward.
        else if (Math.abs(newRank - this.rank) > 2) return false;
        //Pawn went up and/or moves sideways or not moved at all.
        else if (newRank == this.rank || (newFile-this.file > 1)) return false;
        //Check to see if there are any pieces you will jump over on the path of source piece
        //moving to destination.
        int fileUpdate = Integer.compare(newFile, file);
        int rankUpdate = Integer.compare(newRank, rank);

        //Counters used to see whether you can move the piece at source along the path to destination.
        int currentFile = this.file;
        int currentRank = this.rank;
        
        //Keep doing while you reach the destination position.
        while(currentFile != newFile || currentRank != newRank){
            //Update the currentFile and rank to reach a new position
            currentFile += fileUpdate;
            currentRank += rankUpdate;

            //If you are at the destination position, you reached it.
            if(currentFile == newFile && currentRank == newRank) break;

            for (ReturnPiece piece : pieces) {
                if (piece.pieceFile.name().charAt(0) == currentFile && piece.pieceRank == currentRank) {
                    //Piece is being jumped over on the path from moving source piece to destination.
                    return false;
                }
            }
        }
        //Get destination piece.
        ReturnPiece destinationPiece = null; //Have a variable that acts as a reference to the destination piece.
        for(ReturnPiece piece : pieces){
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
             if(Math.abs(newRank-this.rank) >= 1 && Math.abs(newFile-this.file) >= 1) return false;
        }
        return true;
    }

    //Method that takes in a destination piece and figures out whether you can kill that piece or not.
    public boolean isCanKill(ReturnPiece destination, ArrayList<ReturnPiece> boardPieces){
        //If both source and destination pieces make a diagonal (destination is one up, one down from source) AND are of different colors
            //Then you can kill.
            int diffInRank = Math.abs(destination.pieceRank-this.rank);
            int diffInFile = Math.abs(destination.pieceFile.name().charAt(0)-this.file);

            if(diffInRank == 1 && diffInFile == 1 && destination.pieceType.name().charAt(0) != this.color.charAt(0)){
            //Remove destination piece from the ArrayList
            boardPieces.remove(destination);
            //Return true to allow the piece to move at that position.
            return true;
        }
        //Otherwise you can't.
        return false;
    }

    public boolean isEligibleForPromotion(String destination){
        int newRank = Integer.parseInt(destination.substring(1));
        if ((this.color.charAt(0) == 'W' && newRank == 8) || (this.color.charAt(0) == 'B' && newRank == 1)) return true;
        else return false;
    }
}
