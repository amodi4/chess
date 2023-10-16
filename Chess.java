package chess;

import java.util.ArrayList;
import java.util.HashMap;

import chess.ReturnPiece.PieceType;

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
	static HashMap<ReturnPiece, Integer> piecesMoves; //Used to store the Piece as the key and the number of moves made by the piece used for castling

	static boolean[] enpassantSide; //Array that checks to see if you can perform enpassant on the pawns in a side.

	public static ReturnPlay play(String move) {

		/* FILL IN THIS METHOD */

        String[] moveParts = move.split(" "); //split the move into initial location and end location (source and destination)
		String source = moveParts[0];

		ReturnPlay curr = new ReturnPlay();
		curr.piecesOnBoard = initialPieces; //ReturnPlay object needs an arraylist of ReturnPieces, so assign it to initialPieces
		//If there is only one token in the moveParts array
		if(moveParts.length == 1){
			if(source.equals("resign")){
				//If white resigns, black wins
				if(currentPlayer == Player.white) curr.message = ReturnPlay.Message.RESIGN_BLACK_WINS;
				//Otherwise white wins since black resigned.
				else curr.message = ReturnPlay.Message.RESIGN_WHITE_WINS;
			}else{ //If the first token iss not resign, then not a valid move
				curr.message = ReturnPlay.Message.ILLEGAL_MOVE;
			}
			return curr;
		}
		//Finding out if player wants to promote a pawn
		String promotionPiece = "";
		if ((moveParts.length == 3 || moveParts.length == 4) && !moveParts[2].equals("draw?")) promotionPiece = moveParts[2];
		boolean promotionHappened = false;
		
		String destination = moveParts[1];

		//If source location is out of bounds.
		if ((source.charAt(0) < 'a' || source.charAt(0) > 'h') || (source.charAt(1) < '1' || source.charAt(1) > '8')){
			curr.message = ReturnPlay.Message.ILLEGAL_MOVE;
			return curr;
		}

		boolean foundPiece = false; //Variable that tells if you found the piece of the FileRank pos or not.
		Pawn isPassant = null;

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
					Pawn pawn = new Pawn(source, piece.pieceType.name().substring(0, 1), enpassantSide);
					isPassant = pawn;
					pawn.setActualPiece(piece);

					//Reset enpassantAbility for current Player to be false
					if(currentPlayer == Chess.Player.white) enpassantSide[0] = false;
					else enpassantSide[1] = false;

					if (!pawn.isValidMove(destination, initialPieces)){
						curr.message = ReturnPlay.Message.ILLEGAL_MOVE;
						return curr;
					}
					
					if (pawn.isEligibleForPromotion(destination)){
						promotionHappened = true;
						//4 statements below determine what the piece will be converted to--> can be assumed as Queen if promotionPiece string is null
						if (promotionPiece.equals("N")) piece.pieceType = (playerTurnMap.get(currentPlayer).equals("W")) ? ReturnPiece.PieceType.WN : ReturnPiece.PieceType.BN;
						else if (promotionPiece.equals("R")) piece.pieceType = (playerTurnMap.get(currentPlayer).equals("W")) ? ReturnPiece.PieceType.WR : ReturnPiece.PieceType.BR;
						else if (promotionPiece.equals("B")) piece.pieceType = (playerTurnMap.get(currentPlayer).equals("W")) ? ReturnPiece.PieceType.WB : ReturnPiece.PieceType.BB;
						else if (promotionPiece.equals("") || promotionPiece.equals("Q")) piece.pieceType = (playerTurnMap.get(currentPlayer).equals("W")) ? ReturnPiece.PieceType.WQ : ReturnPiece.PieceType.BQ;
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
					King king = new King(source, piece.pieceType.name().substring(0, 1), piecesMoves.get(piece));
					King.piecesMoves = piecesMoves; //Set the piecesMoves variable in King to see if the king and rook in isValidMove() in King has moved 0 times.
					king.setRp(piece);
					if (!king.isValidMove(destination, initialPieces)){
						curr.message = ReturnPlay.Message.ILLEGAL_MOVE;
						return curr;
					}
					if(king.isCastle() == true) break;
				}
				//find the piece, if any, that exists at destination. Other classes already do this, but this is done so that piece can be potentially added back if own king is in check after move
				ReturnPiece pieceAtDestination = getPieceAtSquare(destination, initialPieces);
				if (pieceAtDestination != null) initialPieces.remove(pieceAtDestination); //better to remove pieces in this method rather than in other classes, in case a piece has to be added back
				//Update the piece's position to the destination square 
				piece.pieceFile = ReturnPiece.PieceFile.valueOf(destination.charAt(0) + "");
				piece.pieceRank = Integer.parseInt(destination.charAt(1) + "");
				

				//checking if own king is in check, if not then revert piece back to original spot, and potentially add back previously removed piece
				if (isOwnKingInCheck(initialPieces)){
					//must first move piece back to original spot
					piece.pieceFile = ReturnPiece.PieceFile.valueOf(source.substring(0, 1));
					piece.pieceRank = Integer.parseInt(source.substring(1));
					//checks to see if promotion happened, and if it did then must set piece type back to original type
					if (promotionHappened) piece.pieceType = (playerTurnMap.get(currentPlayer).equals("W")) ? ReturnPiece.PieceType.WP : ReturnPiece.PieceType.BP;
					//if a piece was killed, have to add that back as well
					if (pieceAtDestination!= null) initialPieces.add(pieceAtDestination);
					curr.message = ReturnPlay.Message.ILLEGAL_MOVE;
					return curr;
				}

				//As long as enpassant doesn't put your own king in check, just delete the pawn that is enpassanted.
				if(Pawn.getActualPasssant() != null && isPassant != null && isPassant.isValidEnpassant()){
					initialPieces.remove(Pawn.getActualPasssant());
					Pawn.setActualPasssant(null);
				}

				//checking if opponent king is in check once current move is made, so CHECK message can be displayed
				if (isOpponentKingInCheck(initialPieces)) curr.message = ReturnPlay.Message.CHECK;
				

				piecesMoves.replace(piece, piecesMoves.get(piece) + 1); //Add the number of moves for that piece by 1.
				break;
        	}
		}

		//If you didn't find the piece, then it's an illegal move, because there is no piece that exists at that source position.
		if(!foundPiece){
			curr.message = ReturnPlay.Message.ILLEGAL_MOVE;
			return curr;
		}

		//If there are three or four tokens in the array, then check if the third/fourth token is a draw? message after playing the move.
			//Just return the ReturnPlay object containing draw message, and the autograder will take care of that.
		if((moveParts.length == 3 || moveParts.length == 4)){
			if (moveParts[2].equals("draw?") || (moveParts.length == 4 && moveParts[3].equals("draw?"))) {
				curr.message = ReturnPlay.Message.DRAW;
				return curr;
			}
		} 

		//Alternate the player for the next move (Only executes this is the move is valid)
		if (currentPlayer == Player.white) currentPlayer = Player.black;
		else currentPlayer = Player.white;
		

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
		piecesMoves = new HashMap<>();

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
		enpassantSide = new boolean[2]; //Two sides -> White and black
	}


	public static void addQueens() {
		ReturnPiece whiteQueen = new ReturnPiece();
		whiteQueen.pieceType = ReturnPiece.PieceType.WQ;
		whiteQueen.pieceFile = ReturnPiece.PieceFile.values()[3]; //queen is at d
		whiteQueen.pieceRank = 1;
		initialPieces.add(whiteQueen);
		//We add the piece with the number of moves made to 0.
		piecesMoves.put(whiteQueen, 0);

		ReturnPiece blackQueen = new ReturnPiece();
		blackQueen.pieceType = ReturnPiece.PieceType.BQ;
		blackQueen.pieceFile = ReturnPiece.PieceFile.values()[3];
		blackQueen.pieceRank = 8;
		initialPieces.add(blackQueen);
		piecesMoves.put(blackQueen, 0);
	}


	public static void addKings() {
		ReturnPiece whiteKing = new ReturnPiece();
		whiteKing.pieceType = ReturnPiece.PieceType.WK;
		whiteKing.pieceFile = ReturnPiece.PieceFile.values()[4]; //king is at e
		whiteKing.pieceRank = 1;
		initialPieces.add(whiteKing);
		piecesMoves.put(whiteKing, 0);

		ReturnPiece blackKing = new ReturnPiece();
		blackKing.pieceType = ReturnPiece.PieceType.BK;
		blackKing.pieceFile = ReturnPiece.PieceFile.values()[4];
		blackKing.pieceRank = 8;
		initialPieces.add(blackKing);
		piecesMoves.put(blackKing, 0);
	}


	public static void addBishops() {
		for (int i = 0; i < 2; i++){
			ReturnPiece whiteBishop = new ReturnPiece();
			whiteBishop.pieceType = ReturnPiece.PieceType.WB;
			whiteBishop.pieceFile = ReturnPiece.PieceFile.values()[i == 0 ? 2 : 5]; //bishop is either at c or f
			whiteBishop.pieceRank = 1;
			initialPieces.add(whiteBishop);
			piecesMoves.put(whiteBishop, 0);

			ReturnPiece blackBishop = new ReturnPiece();
			blackBishop.pieceType = ReturnPiece.PieceType.BB;
			blackBishop.pieceFile = ReturnPiece.PieceFile.values()[i == 0 ? 2 : 5];
			blackBishop.pieceRank = 8;
			initialPieces.add(blackBishop);
			piecesMoves.put(blackBishop, 0);
		}
	}

	public static void addKnights() {
		for (int i = 0; i < 2; i++){
			ReturnPiece whiteKnight = new ReturnPiece();
			whiteKnight.pieceType = ReturnPiece.PieceType.WN;
			whiteKnight.pieceFile = ReturnPiece.PieceFile.values()[i == 0 ? 1 : 6]; //knight is either at b or g
			whiteKnight.pieceRank = 1;
			initialPieces.add(whiteKnight);
			piecesMoves.put(whiteKnight, 0);

			ReturnPiece blackKnight = new ReturnPiece();
			blackKnight.pieceType = ReturnPiece.PieceType.BN;
			blackKnight.pieceFile = ReturnPiece.PieceFile.values()[i == 0 ? 1 : 6];
			blackKnight.pieceRank = 8;
			initialPieces.add(blackKnight);
			piecesMoves.put(blackKnight, 0);
		}
	}

	public static void addPawns(){
		for (int i = 0; i < 8; i++){
			ReturnPiece whitePawn = new ReturnPiece();
			whitePawn.pieceType = ReturnPiece.PieceType.WP;
			whitePawn.pieceFile = ReturnPiece.PieceFile.values()[i]; //pawns stretch across whole row, so a-h
			whitePawn.pieceRank = 2;
			initialPieces.add(whitePawn);
			piecesMoves.put(whitePawn, 0);

			ReturnPiece blackPawn = new ReturnPiece();
			blackPawn.pieceType = ReturnPiece.PieceType.BP;
			blackPawn.pieceFile = ReturnPiece.PieceFile.values()[i];
			blackPawn.pieceRank = 7;
			initialPieces.add(blackPawn);
			piecesMoves.put(blackPawn, 0);
		}
	}

	public static void addRooks(){
		for (int i = 0; i < 2; i++){
			ReturnPiece whiteRook = new ReturnPiece();
			whiteRook.pieceType = ReturnPiece.PieceType.WR;
			whiteRook.pieceFile = ReturnPiece.PieceFile.values()[i == 0 ? 0 : 7]; //rook is either at a or h
			whiteRook.pieceRank = 1;
			initialPieces.add(whiteRook);
			piecesMoves.put(whiteRook, 0);

			ReturnPiece blackRook = new ReturnPiece();
			blackRook.pieceType = ReturnPiece.PieceType.BR;
			blackRook.pieceFile = ReturnPiece.PieceFile.values()[i == 0 ? 0 : 7];
			blackRook.pieceRank = 8;
			initialPieces.add(blackRook);
			piecesMoves.put(blackRook, 0);
		}
	}

	//method to see if current player's king is in check after current player makes a certain move
	public static boolean isOwnKingInCheck(ArrayList<ReturnPiece> pieces){
		String ownKingDestination = "";
		//traverse through board to find king of current player
		for (ReturnPiece piece : pieces){
			if (piece.pieceType.name().substring(0, 1).equals(playerTurnMap.get(currentPlayer)) && piece.pieceType.name().substring(1).equals("K")){
				ownKingDestination += piece.pieceFile.toString();
				ownKingDestination += Integer.toString(piece.pieceRank);
			}
		}
		//if current player is black, then try to find any white pieces that put black king in check
		if (playerTurnMap.get(currentPlayer).equals("B")){
			for (ReturnPiece piece : pieces){
				String sourceOfPiece = "";
				sourceOfPiece += piece.pieceFile.toString();
				sourceOfPiece += Integer.toString(piece.pieceRank);
				if (piece.pieceType == ReturnPiece.PieceType.WN){
					Knight knight = new Knight(sourceOfPiece, piece.pieceType.name().substring(0, 1));
					if (knight.isValidMove(ownKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.WB){
					Bishop bishop = new Bishop(sourceOfPiece, piece.pieceType.name().substring(0, 1));
					if (bishop.isValidMove(ownKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.WR){
					Rook rook = new Rook(sourceOfPiece, piece.pieceType.name().substring(0, 1));
					if (rook.isValidMove(ownKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.WP){
					Pawn pawn = new Pawn(sourceOfPiece, piece.pieceType.name().substring(0, 1), enpassantSide);
					if (pawn.isValidMove(ownKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.WQ){
					Queen queen = new Queen(sourceOfPiece, piece.pieceType.name().substring(0, 1));
					if (queen.isValidMove(ownKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.WK){
					King king = new King(sourceOfPiece, piece.pieceType.name().substring(0, 1), piecesMoves.get(piece));
					if (king.isValidMove(ownKingDestination, pieces)) return true;
				}
			}
		}
		//else if current player is white, then try to find black pieces that put white king in check, but otherwise same implementation
		else{
			for (ReturnPiece piece : pieces){
				String sourceOfPiece = "";
				sourceOfPiece += piece.pieceFile.toString();
				sourceOfPiece += Integer.toString(piece.pieceRank);
				if (piece.pieceType == ReturnPiece.PieceType.BN){
					Knight knight = new Knight(sourceOfPiece, piece.pieceType.name().substring(0, 1));
					if (knight.isValidMove(ownKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.BB){
					Bishop bishop = new Bishop(sourceOfPiece, piece.pieceType.name().substring(0, 1));
					if (bishop.isValidMove(ownKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.BR){
					Rook rook = new Rook(sourceOfPiece, piece.pieceType.name().substring(0, 1));
					if (rook.isValidMove(ownKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.BP){
					Pawn pawn = new Pawn(sourceOfPiece, piece.pieceType.name().substring(0, 1), enpassantSide);
					if (pawn.isValidMove(ownKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.BQ){
					Queen queen = new Queen(sourceOfPiece, piece.pieceType.name().substring(0, 1));
					if (queen.isValidMove(ownKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.BK){
					King king = new King(sourceOfPiece, piece.pieceType.name().substring(0, 1), piecesMoves.get(piece));
					if (king.isValidMove(ownKingDestination, pieces)) return true;
				}
			}
		}
		return false;
	}

	//method to see if opponent's king is in check after current player makes a move
	public static boolean isOpponentKingInCheck(ArrayList<ReturnPiece> pieces){
		String opponentKingDestination = "";
		//traverse through board to find opponent's king's square
		for (ReturnPiece piece : pieces){
			if (!piece.pieceType.name().substring(0, 1).equals(playerTurnMap.get(currentPlayer)) && piece.pieceType.name().substring(1).equals("K")){
				opponentKingDestination += piece.pieceFile.toString();
				opponentKingDestination += Integer.toString(piece.pieceRank);
			}
		}

		//if current player is white, then see if any white pieces put the opposing king in check
		if (playerTurnMap.get(currentPlayer).equals("W")){
			for (ReturnPiece piece : pieces){
				String sourceOfPiece = "";
				sourceOfPiece += piece.pieceFile.toString();
				sourceOfPiece += Integer.toString(piece.pieceRank);
				if (piece.pieceType == ReturnPiece.PieceType.WN){
					Knight knight = new Knight(sourceOfPiece, piece.pieceType.name().substring(0, 1));
					if (knight.isValidMove(opponentKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.WB){
					Bishop bishop = new Bishop(sourceOfPiece, piece.pieceType.name().substring(0, 1));
					if (bishop.isValidMove(opponentKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.WR){
					Rook rook = new Rook(sourceOfPiece, piece.pieceType.name().substring(0, 1));
					if (rook.isValidMove(opponentKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.WP){
					Pawn pawn = new Pawn(sourceOfPiece, piece.pieceType.name().substring(0, 1), enpassantSide);
					if (pawn.isValidMove(opponentKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.WQ){
					Queen queen = new Queen(sourceOfPiece, piece.pieceType.name().substring(0, 1));
					if (queen.isValidMove(opponentKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.WK){
					King king = new King(sourceOfPiece, piece.pieceType.name().substring(0, 1), piecesMoves.get(piece));
					if (king.isValidMove(opponentKingDestination, pieces)) return true;
				}
			}
		}
		//else if current player is black, find any black pieces that put white king in check
		else{
			for (ReturnPiece piece : pieces){
				String sourceOfPiece = "";
				sourceOfPiece += piece.pieceFile.toString();
				sourceOfPiece += Integer.toString(piece.pieceRank);
				if (piece.pieceType == ReturnPiece.PieceType.BN){
					Knight knight = new Knight(sourceOfPiece, piece.pieceType.name().substring(0, 1));
					if (knight.isValidMove(opponentKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.BB){
					Bishop bishop = new Bishop(sourceOfPiece, piece.pieceType.name().substring(0, 1));
					if (bishop.isValidMove(opponentKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.BR){
					Rook rook = new Rook(sourceOfPiece, piece.pieceType.name().substring(0, 1));
					if (rook.isValidMove(opponentKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.BP){
					Pawn pawn = new Pawn(sourceOfPiece, piece.pieceType.name().substring(0, 1), enpassantSide);
					if (pawn.isValidMove(opponentKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.BQ){
					Queen queen = new Queen(sourceOfPiece, piece.pieceType.name().substring(0, 1));
					if (queen.isValidMove(opponentKingDestination, pieces)) return true;
				}
				else if (piece.pieceType == ReturnPiece.PieceType.BK){
					King king = new King(sourceOfPiece, piece.pieceType.name().substring(0, 1), piecesMoves.get(piece));
					if (king.isValidMove(opponentKingDestination, pieces)) return true;
				}
			}
		}
		return false;
	}

	/**
	 * Given the destination position, returns the piece in the Arraylist of pieces/the board at that position.
	 * @param destination ,a FileRank position
	 * @param pieces ,an arraylist of pieces used to find the piece at that destination from that arraylist
	 * @return the pointer to the piece you found or null if you didn't find the piece
	*/
	public static ReturnPiece getPieceAtSquare(String destination, ArrayList<ReturnPiece> pieces){
		for (ReturnPiece piece : pieces){
			if (piece.pieceFile.name().equalsIgnoreCase(destination.substring(0, 1)) &&
            piece.pieceRank == Integer.parseInt(destination.substring(1))) return piece;
		}
		return null;
	}
}
