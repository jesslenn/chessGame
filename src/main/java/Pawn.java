public class Pawn extends Piece {
    public Pawn(PieceColor color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board) {
        if (newPosition.equals(this.position)) {
            return false;
        }

        int forwardDirection = color == PieceColor.WHITE ? -1 : 1;
        int rowDiff = (newPosition.getRow() - position.getRow()) * forwardDirection;
        int colDiff = (newPosition.getColumn() - position.getColumn());

        //forward move
        if (colDiff == 0 && rowDiff == 1 && board[newPosition.getRow()][newPosition.getColumn()] == null) {
            //we can execute this move
            return true;
        }

        boolean isStartingPosition = (color == PieceColor.WHITE && position.getRow() == 6 || color == PieceColor.BLACK && position.getRow() == 1);

        //behavior if this is an initial forward 2-square move
        if (colDiff == 0 && rowDiff == 2 && isStartingPosition && board[newPosition.getRow()][newPosition.getColumn()] == null) {
            //check for blocking pieces
            int middleRow = position.getRow() + forwardDirection;
            if (board[middleRow][position.getColumn()] == null) {
                return true;
            }
        }

        //diagonal capture
        if (Math.abs(colDiff) == 1 && rowDiff == 1 && board[newPosition.getRow()][newPosition.getColumn()].color != this.color) {
            //capture opponent's piece
            return true;
        }
    //if none of these are true, it's not a valid move
        return false;
    }
}
