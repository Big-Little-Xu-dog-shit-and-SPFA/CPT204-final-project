package ataxx;

import java.util.ArrayList;

// Final Project Part A.2 Ataxx AI Player (A group project)

/** A Player that computes its own moves. */
class AIPlayer extends Player {

    
    /** A new AIPlayer for GAME that will play MYCOLOR.
     *  SEED is used to initialize a random-number generator,
	 *  increase the value of SEED would make the AIPlayer move automatically.
     *  Identical seeds produce identical behaviour. */
    AIPlayer(Game game, PieceState myColor, long seed) {
        super(game, myColor);
    }

    @Override
    boolean isAuto() {
        return true;
    }

    @Override
    String getAtaxxMove() {
        Move move = findMove();
        getAtaxxGame().reportMove(move, getMyState());
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(getAtaxxBoard());
        lastFoundMove = null;

        // Here we just have the simple AI to randomly move.
        // However, it does not meet with the requirements of Part A.2.
        // Therefore, the following codes should be modified
        // in order to meet with the requirements of Part A.2.
        // You can create add your own method and put your method here.
		
        ArrayList<Move> listOfMoves = possibleMoves(b, b.nextMove());
        int moveArrayLength = listOfMoves.size();
        int randomIndex = (int) (Math.random() * moveArrayLength);
        for(int i = 0; i < moveArrayLength; i++){
            if (i == randomIndex){
                b.createMove(listOfMoves.get(i));
                lastFoundMove = listOfMoves.get(i);
            }
        }



        // Please do not change the codes below
        if (lastFoundMove == null) {
            lastFoundMove = Move.pass();
        }
        return lastFoundMove;
    }


    /** The move found by the last call to the findMove method above. */
    private Move lastFoundMove;


    /** Return all possible moves for a color.
     * @param board the current board.
     * @param myColor the specified color.
     * @return an ArrayList of all possible moves for the specified color. */
    private ArrayList<Move> possibleMoves(Board board, PieceState myColor) {
        double[] globalVal = new double[2]; // 记录每一步得分的最大值
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for (char row = '7'; row >= '1'; row--) {
            for (char col = 'a'; col <= 'g'; col++) {
                int index = Board.index(col, row);
                if (board.getContent(index) == myColor) {
                    ArrayList<Move> addMoves
                            = assistPossibleMoves(board, row, col, globalVal);
                    if (globalVal[0] < globalVal[1]) {
                        globalVal[0] = globalVal[1];
                        possibleMoves.clear();
                        possibleMoves.addAll(addMoves);
                    } else if (globalVal[0] == globalVal[1])
                        possibleMoves.addAll(addMoves);
                }
            }
        }
        return possibleMoves;
    }

    /** Returns an Arraylist of legal moves.
     * @param board the board for testing
     * @param row the row coordinate of the center
     * @param col the col coordinate of the center */
    private ArrayList<Move>
    assistPossibleMoves(Board board, char row, char col, double[] globalVal) {
        ArrayList<Move> assistPossibleMoves = new ArrayList<>();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i != 0 || j != 0) {
                    char row2 = (char) (row + j);
                    char col2 = (char) (col + i);
                    Move currMove = Move.move(col, row, col2, row2);
                    if (board.moveLegal(currMove)) {
                        // 计算走完这一步之后（自己棋子数量 - 对手棋子数量）值
                        Board b = new Board(board);
                        int val1 = calValue(b, currMove);
                        b.createMove(currMove);
                        int val2 = calOppoValue(b, b.nextMove());
                        // 越往棋盘中间走，分越高
                        double val3 = Math.pow('d' - col2, 2) + Math.pow('4' - row2, 2) + 1;
                        double val = (val1 * 1.5) / (val2 * val3);
                        if (globalVal[1] < val) {
                            globalVal[1] = val;
                            assistPossibleMoves.clear();
                            assistPossibleMoves.add(currMove);
                        } else if (globalVal[1] == val) {
                            assistPossibleMoves.add(currMove);
                        }
                    }
                }
            }
        }
        return assistPossibleMoves;
    }

    /**
     * 计算当前玩家棋子增加的数量，作为这一步的得分
     * @param b
     * @param currMove
     * @return the value of this movement.
     */
    private int calValue(Board b, Move currMove) {
        PieceState myColor = b.nextMove();
        int orgVal = b.getColorNums(myColor);
        b.createMove(currMove);
        int val = b.getColorNums(myColor);
        return val - orgVal;
    }

    private int calOppoValue(Board b, PieceState myColor) {
        int[] globalVal = new int[2]; // 记录每一步得分的最大值
        for (char row = '7'; row >= '1'; row--) {
            for (char col = 'a'; col <= 'g'; col++) {
                int index = Board.index(col, row);
                if (b.getContent(index) == myColor) {
                    assistPossibleOppo(b, row, col, globalVal);
                }
            }
        }
        return globalVal[0];
    }

    private void assistPossibleOppo(Board board, char row, char col, int[] globalVal) {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i != 0 || j != 0) {
                    char row2 = (char) (row + j);
                    char col2 = (char) (col + i);
                    Move currMove = Move.move(col, row, col2, row2);
                    if (board.moveLegal(currMove)) {
                        // 计算走完这一步之后（自己棋子数量 - 对手棋子数量）值
                        int val = calValue(new Board(board), currMove);
                        if (globalVal[0] < val) {
                            globalVal[0] = val;
                        }
                    }
                }
            }
        }
    }
}
