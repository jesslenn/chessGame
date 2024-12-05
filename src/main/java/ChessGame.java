public class ChessGame {
    private Chessboard board;
    private boolean whiteTurn = true;

    public ChessGame() {
        this.board = new Chessboard();
    }

    public boolean makeMove(Position start, Position end) {
        Piece movingPiece = board.getPiece(start.getRow(), start.getColumn());
        if (movingPiece == null || movingPiece.getColor() != (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
            return false;
        }

        if (movingPiece.isValidMove(end, board.getBoard())) {
            board.movePiece(start, end);
            whiteTurn = !whiteTurn;
            return true;
        }
        return false;
    }

    public boolean isInCheck(PieceColor kingColor) {
        Position kingPosition = findKingPosition(kingColor);
        for (int row = 0; row < board.getBoard().length; row++) {
            for (int column = 0; column < board.getBoard().length; column++) {
                Piece piece = board.getPiece(row, column);
                if (piece != null && piece.getColor() != kingColor && piece.isValidMove(kingPosition,
                    board.getBoard())) {
                    return true;
                }
            }
        }
        return false;
    }

    private Position findKingPosition(PieceColor color) {
        for (int row = 0; row < board.getBoard().length; row++) {
            for (int column = 0; column < board.getBoard().length; column++) {
                Piece piece = board.getPiece(row, column);
                if (piece instanceof King && piece.getColor() == color) {
                    return new Position(row, column);
                }
            }
        }
        throw new RuntimeException("There is no king of this color on the board. Something has gone seriously wrong.");
    }

    public boolean isCheckmate(PieceColor kingColor) {
        if (!isInCheck(kingColor)) {
            return false; // Not in check, so cannot be checkmate
        }

        Position kingPosition = findKingPosition(kingColor);
        King king = (King) board.getPiece(kingPosition.getRow(), kingPosition.getColumn());

        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int colOffset = -1; colOffset <= 1; colOffset++) {
                if (rowOffset == 0 && colOffset == 0) {
                    continue;
                }
                Position newPosition = new Position(kingPosition.getRow() + rowOffset,
                    kingPosition.getColumn() + colOffset);

                if (isPositionOnBoard(newPosition) && king.isValidMove(newPosition, board.getBoard())
                    && !wouldBeInCheckAfterMove(kingColor, kingPosition, newPosition)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isPositionOnBoard(Position newPosition) {
        return newPosition.getRow() >= 0 && newPosition.getRow() < board.getBoard().length
               && newPosition.getColumn() >= 0 && newPosition.getColumn() < board.getBoard().length;
    }

    private boolean wouldBeInCheckAfterMove(PieceColor kingColor, Position from, Position to) {
        Piece temp = board.getPiece(to.getRow(), to.getColumn());
        board.setPiece(to.getRow(), to.getColumn(), board.getPiece(from.getRow(), from.getColumn()));
        board.setPiece(from.getRow(), from.getColumn(), null);

        boolean inCheck = isInCheck(kingColor);
        board.setPiece(from.getRow(), from.getColumn(), board.getPiece(to.getRow(), to.getColumn()));
        board.setPiece(to.getRow(), to.getColumn(), temp);

        return inCheck;
    }

    public Chessboard getBoard() {
        return this.board;
    }
}
