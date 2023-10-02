package chess;

public abstract class Piece implements Cloneable {
    String file;
    int rank;
    String color;

    public Piece(String location, String color){
        //can split location of a piece into file and rank over here
        this.file = location.substring(0, 1);
        this.rank = Integer.parseInt(location.substring(1));
        this.color = color; //Color of piece you pass in anyways will be W or B
    }
}
