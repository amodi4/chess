package chess;

import java.util.ArrayList;

class ReturnPiece {
	static enum PieceType {WP, WR, WN, WB, WQ, WK, 
		            BP, BR, BN, BB, BK, BQ};
	static enum PieceFile {a, b, c, d, e, f, g, h};
	
	PieceType pieceType;
	PieceFile pieceFile;
	int pieceRank;  // 1..8
	public String toString() {
		return ""+pieceFile+pieceRank+":"+pieceType;
	}
	public boolean equals(Object other) {
		if (other == null || !(other instanceof ReturnPiece)) {
			return false;
		}
		ReturnPiece otherPiece = (ReturnPiece)other;
		return pieceType == otherPiece.pieceType &&
				pieceFile == otherPiece.pieceFile &&
				pieceRank == otherPiece.pieceRank;
	}
}

class ReturnPlay {
	enum Message {ILLEGAL_MOVE, DRAW, 
				  RESIGN_BLACK_WINS, RESIGN_WHITE_WINS, 
				  CHECK, CHECKMATE_BLACK_WINS,	CHECKMATE_WHITE_WINS, 
				  STALEMATE};
	
	ArrayList<ReturnPiece> piecesOnBoard;
	Message message;
}

public class Chess {

	
	
	enum Player { white, black }
	
	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for details of
	 *         the contents of the returned ReturnPlay instance.
	 */
	
	static ArrayList<ReturnPiece> initialPieces; //created global static arraylist of ReturnPiece, so that it could be accessed from both methods without having to create a Chess object

	public static ReturnPlay play(String move) {

		/* FILL IN THIS METHOD */
        
		
		/* FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY */
		/* WHEN YOU FILL IN THIS METHOD, YOU NEED TO RETURN A ReturnPlay OBJECT */
		String[] moveParts = move.split(" ");
		String source = moveParts[0];
		String destination = moveParts[1];

		ReturnPlay curr = new ReturnPlay();
		curr.piecesOnBoard = initialPieces; //ReturnPlay object needs an arraylist of ReturnPieces, so assign it to initialPieces

		for (ReturnPiece piece : curr.piecesOnBoard){
			if (piece.pieceFile.name().equalsIgnoreCase(source.substring(0, 1)) &&
            piece.pieceRank == Integer.parseInt(source.substring(1))) {
            // Update the piece's position to the destination square
            piece.pieceFile = ReturnPiece.PieceFile.valueOf(destination.substring(0, 1));
            piece.pieceRank = Integer.parseInt(destination.substring(1));
            break;
        	}
		}

		return curr;
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		/* FILL IN THIS METHOD */
		
		initialPieces = new ArrayList<>();

		for (int i = 0; i < 8; i++){
			ReturnPiece whitePawn = new ReturnPiece();
			whitePawn.pieceType = ReturnPiece.PieceType.WP;
			whitePawn.pieceFile = ReturnPiece.PieceFile.values()[i]; //pawns stretch across whole row, so a-h
			whitePawn.pieceRank = 2;
			initialPieces.add(whitePawn);

			ReturnPiece blackPawn = new ReturnPiece();
			blackPawn.pieceType = ReturnPiece.PieceType.BP;
			blackPawn.pieceFile = ReturnPiece.PieceFile.values()[i];
			blackPawn.pieceRank = 7;
			initialPieces.add(blackPawn);
		}

		for (int i = 0; i < 2; i++){
			ReturnPiece whiteRook = new ReturnPiece();
			whiteRook.pieceType = ReturnPiece.PieceType.WR;
			whiteRook.pieceFile = ReturnPiece.PieceFile.values()[i == 0 ? 0 : 7]; //rook is either at a or h
			whiteRook.pieceRank = 1;
			initialPieces.add(whiteRook);

			ReturnPiece blackRook = new ReturnPiece();
			blackRook.pieceType = ReturnPiece.PieceType.BR;
			blackRook.pieceFile = ReturnPiece.PieceFile.values()[i == 0 ? 0 : 7];
			blackRook.pieceRank = 8;
			initialPieces.add(blackRook);
		}

		for (int i = 0; i < 2; i++){
			ReturnPiece whiteKnight = new ReturnPiece();
			whiteKnight.pieceType = ReturnPiece.PieceType.WN;
			whiteKnight.pieceFile = ReturnPiece.PieceFile.values()[i == 0 ? 1 : 6]; //knight is either at b or g
			whiteKnight.pieceRank = 1;
			initialPieces.add(whiteKnight);

			ReturnPiece blackKnight = new ReturnPiece();
			blackKnight.pieceType = ReturnPiece.PieceType.BN;
			blackKnight.pieceFile = ReturnPiece.PieceFile.values()[i == 0 ? 1 : 6];
			blackKnight.pieceRank = 8;
			initialPieces.add(blackKnight);
		}

		for (int i = 0; i < 2; i++){
			ReturnPiece whiteBishop = new ReturnPiece();
			whiteBishop.pieceType = ReturnPiece.PieceType.WB;
			whiteBishop.pieceFile = ReturnPiece.PieceFile.values()[i == 0 ? 2 : 5]; //bishop is either at c or f
			whiteBishop.pieceRank = 1;
			initialPieces.add(whiteBishop);

			ReturnPiece blackBishop = new ReturnPiece();
			blackBishop.pieceType = ReturnPiece.PieceType.BB;
			blackBishop.pieceFile = ReturnPiece.PieceFile.values()[i == 0 ? 2 : 5];
			blackBishop.pieceRank = 8;
			initialPieces.add(blackBishop);
		}
		
		ReturnPiece whiteKing = new ReturnPiece();
		whiteKing.pieceType = ReturnPiece.PieceType.WK;
		whiteKing.pieceFile = ReturnPiece.PieceFile.values()[4]; //king is at e
		whiteKing.pieceRank = 1;
		initialPieces.add(whiteKing);

		ReturnPiece blackKing = new ReturnPiece();
		blackKing.pieceType = ReturnPiece.PieceType.BK;
		blackKing.pieceFile = ReturnPiece.PieceFile.values()[4];
		blackKing.pieceRank = 8;
		initialPieces.add(blackKing);

		ReturnPiece whiteQueen = new ReturnPiece();
		whiteQueen.pieceType = ReturnPiece.PieceType.WQ;
		whiteQueen.pieceFile = ReturnPiece.PieceFile.values()[3]; //queen is at d
		whiteQueen.pieceRank = 1;
		initialPieces.add(whiteQueen);

		ReturnPiece blackQueen = new ReturnPiece();
		blackQueen.pieceType = ReturnPiece.PieceType.BQ;
		blackQueen.pieceFile = ReturnPiece.PieceFile.values()[3];
		blackQueen.pieceRank = 8;
		initialPieces.add(blackQueen);

	}
}
