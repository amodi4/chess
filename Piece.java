package chess;

public abstract class Piece implements Cloneable {
    String file;
    int rank;
    //boolean white;

    public Piece(String location, String color){
        //can split location of a piece into file and rank over here
        this.file = location.substring(0, 1);
        this.rank = Integer.parseInt(location.substring(1));
        //this.white = color.equals("W");
    }
}
