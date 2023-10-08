package chess;

import java.util.ArrayList;
import java.util.HashMap;

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
	static HashMap<Player, String> playerTurnMap; //HashMap to store the the Player color as a key, and then "W" or "B" as a value, since those are the first characters in a piecetype of White or Black's pieces
	static Player currentPlayer; // to keep track of which player's turn it is

	public static ReturnPlay play(String move) {

		/* FILL IN THIS METHOD */
        String[] moveParts = move.split(" "); //split the move into initial location and end location (source and destination)
		String source = moveParts[0];

		ReturnPlay curr = new ReturnPlay();
		curr.piecesOnBoard = initialPieces; //ReturnPlay object needs an arraylist of ReturnPieces, so assign it to initialPieces
		//If there is only one token in the moveParts array, it's a resign move
		if(moveParts.length == 1){
			//If white resigns, black wins
			if(currentPlayer == Player.white) curr.message = ReturnPlay.Message.RESIGN_BLACK_WINS;
			//Otherwise white wins since black resigned.
			else curr.message = ReturnPlay.Message.RESIGN_WHITE_WINS;
			return curr;
		}
		
		String destination = moveParts[1];

		//If source location is out of bounds.
		if ((source.charAt(0) < 'a' || source.charAt(0) > 'h') || (source.charAt(1) < '1' || source.charAt(1) > '8')){
			curr.message = ReturnPlay.Message.ILLEGAL_MOVE;
			return curr;
		}

		boolean foundPiece = false; //Variable that tells if you found the piece of the FileRank pos or not.

		for (ReturnPiece piece : curr.piecesOnBoard){
			if (piece.pieceFile.name().equalsIgnoreCase(source.substring(0, 1)) &&
            piece.pieceRank == Integer.parseInt(source.substring(1)))  { //this checks if pieceFile and pieceRank of piece being visited match the source of the move
				//The moment you found the piece, just set foundPiece to be true.
				foundPiece = true;
				//check to see if correct player (white or black) is the one making the move
				if (!piece.pieceType.name().substring(0, 1).equals(playerTurnMap.get(currentPlayer))){
					curr.message = ReturnPlay.Message.ILLEGAL_MOVE;
					return curr;
				}
				
				//Based on the type of the piece, create an instance of the piece type
				if(piece.pieceType == ReturnPiece.PieceType.WP || piece.pieceType == ReturnPiece.PieceType.BP){
					//Create a Pawn object
					Pawn pawn = new Pawn(source, piece.pieceType.name().substring(0, 1));
					if (!pawn.isValidMove(destination, initialPieces)){
						curr.message = ReturnPlay.Message.ILLEGAL_MOVE;
						return curr;
					}
				} else if(piece.pieceType == ReturnPiece.PieceType.WR || piece.pieceType == ReturnPiece.PieceType.BR){
					//Create a Rook object
					Rook rook = new Rook(source, piece.pieceType.name().substring(0, 1));
					if (!rook.isValidMove(destination, initialPieces)){
						curr.message = ReturnPlay.Message.ILLEGAL_MOVE;
						return curr;
					}
				} else if(piece.pieceType == ReturnPiece.PieceType.WN || piece.pieceType == ReturnPiece.PieceType.BN){
					//Create a Knight object
					Knight knight = new Knight(source, piece.pieceType.name().substring(0, 1));
					if (!knight.isValidMove(destination, initialPieces)){
						curr.message = ReturnPlay.Message.ILLEGAL_MOVE;
						return curr;
					}
				} else if(piece.pieceType == ReturnPiece.PieceType.WQ || piece.pieceType == ReturnPiece.PieceType.BQ){
					//Create a Queen object
					Queen queen = new Queen(source, piece.pieceType.name().substring(0, 1));
					if (!queen.isValidMove(destination, initialPieces)){
						curr.message = ReturnPlay.Message.ILLEGAL_MOVE;
						return curr;
					}
				} else if(piece.pieceType == ReturnPiece.PieceType.WB || piece.pieceType == ReturnPiece.PieceType.BB){
					//Create a Bishop object
					Bishop bishop = new Bishop(source, piece.pieceType.name().substring(0, 1));
					if (!bishop.isValidMove(destination, initialPieces)){
						curr.message = ReturnPlay.Message.ILLEGAL_MOVE;
						return curr;
					}
				} else{
					//Create a King object
					King king = new King(source, piece.pieceType.name().substring(0, 1));
					if (!king.isValidMove(destination, initialPieces)){
						curr.message = ReturnPlay.Message.ILLEGAL_MOVE;
						return curr;
					}
				}
				//Update the piece's position to the destination square (This is just for moving the pawn, not killing it.)
				piece.pieceFile = ReturnPiece.PieceFile.valueOf(destination.charAt(0) + "");
				piece.pieceRank = Integer.parseInt(destination.charAt(1) + "");
				break;
        	}
		}

		//If you didn't find the piece, then it's an illegal move, because there is no piece that exists at that source position.
		if(!foundPiece){
			curr.message = ReturnPlay.Message.ILLEGAL_MOVE;
			return curr;
		}

		//If there are three tokens in the array, then the third token has to be a draw? message after playing the move.
			//Just return the ReturnPlay object containing draw message, and the autograder will take care of that.
		if(moveParts.length == 3){
			curr.message = ReturnPlay.Message.DRAW;
		} else{
			//Alternate the player for the next move (Only executes this is the move is valid)
			if (currentPlayer == Player.white) currentPlayer = Player.black;
			else currentPlayer = Player.white;
		}

		return curr;
		
		/* FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY */
		/* WHEN YOU FILL IN THIS METHOD, YOU NEED TO RETURN A ReturnPlay OBJECT */
	}
	

	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		/* FILL IN THIS METHOD */
		
		initialPieces = new ArrayList<>();

		//Making a separate method for all of the pieces of each type of piece that you add to the initial chess board of pieces.

		//Add the pawns.
		addPawns();

		//Add the rooks.
		addRooks();

		//Add the knights.
		addKnights();

		//Add the bishops.
		addBishops();
		
		//Add the kings.
		addKings();

		//Add the queens.
		addQueens();

		currentPlayer = Player.white;
 		//Populate the HashMap of player's turn at the start of the game for the player to keep track of the pieces
		//allowed to move.
		playerTurnMap = new HashMap<>();
		playerTurnMap.put(Player.white, "W");
		playerTurnMap.put(Player.black, "B");
	}


	public static void addQueens() {
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


	public static void addKings() {
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
	}


	public static void addBishops() {
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
	}

	public static void addKnights() {
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
	}

	public static void addPawns(){
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
	}

	public static void addRooks(){
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
	}
}
