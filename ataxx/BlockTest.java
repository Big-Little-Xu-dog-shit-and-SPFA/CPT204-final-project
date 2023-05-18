package ataxx;

import org.junit.Test;

import static ataxx.PieceState.*;
import static org.junit.Assert.*;

public class BlockTest {
    private static final char[] COLS = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
    private static final char[] ROWS = {'1', '2', '3', '4', '5', '6', '7'};


    void checkBoard(Board b, PieceState[][] expectedColors) {
        assertEquals(Board.ONESIDE, expectedColors.length);
        assertEquals(Board.ONESIDE, expectedColors[0].length);
        for (int r = 0; r < expectedColors.length; r++) {
            for (int c = 0; c < expectedColors[0].length; c++) {
                assertEquals("incorrect color at "
                                + COLS[c] + ROWS[ROWS.length - 1 - r],
                        expectedColors[r][c],
                        b.getContent(COLS[c], ROWS[ROWS.length - 1 - r]));
            }
        }
    }




    @Test
    public void testSettingBlocks() {
        Board b = new Board();
        b.setBlock('a', '2');
        final PieceState[][] BLOCK = {
                {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
                {BLOCKED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLOCKED},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {BLOCKED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLOCKED},
                {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
        };
        checkBoard(b, BLOCK);
    }


    @Test
    public void testSettingBlocks1() {
        Board b1 = new Board();
        b1.setBlock('c','3');
        b1.setBlock('d','6');
        final PieceState[][] BLOCK1 = {
                {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
                {EMPTY, EMPTY, EMPTY, BLOCKED, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, BLOCKED, EMPTY, BLOCKED, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                {EMPTY, EMPTY, BLOCKED, EMPTY, BLOCKED, EMPTY, EMPTY},
                {EMPTY, EMPTY, EMPTY, BLOCKED, EMPTY, EMPTY, EMPTY},
                {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
        };
        checkBoard(b1,BLOCK1);
    }


    @Test
    public void testConstructor() {
        Board b = new Board();
        assertEquals("bottom right not RED", RED, b.getContent('g', '1'));
        assertEquals("bottom left not BLUE", BLUE, b.getContent('a', '1'));
        assertEquals("top right not BLUE", BLUE, b.getContent('g', '7'));
        assertEquals("top left not RED", RED, b.getContent('a', '7'));
        checkBoard(b, INITIAL);
        assertNull("winner not null", b.getWinner());
        assertEquals("redPieces not 2", 2, b.getColorNums(RED));
        assertEquals("bluePieces not 2", 2, b.getColorNums(BLUE));
        for (int x = 0; x < COLS.length; x++) {
            for (int y = 0; y < ROWS.length; y++) {
                if (x == 0 & y == 0) {
                    assertEquals("bottom left not BLUE", BLUE, b.getContent('a', '1'));
                } else if (x == 0 & y == 6) {
                    assertEquals("top left not RED", RED, b.getContent('a', '7'));
                } else if (x == 6 & y == 6) {
                    assertEquals("top right not BLUE", BLUE, b.getContent('g', '7'));
                } else if (x == 6 & y == 0) {
                    assertEquals("bottom right not RED", RED, b.getContent('g', '1'));
                } else {
                    assertEquals(EMPTY, b.getContent(COLS[x], ROWS[y]));
                }
            }
        }
        assertEquals("not red's move", RED, b.nextMove());
        assertEquals("num moves not 0", 0, b.moveNums());
        assertEquals("num jumps not 0", 0, b.getConsecJumpNums());
    }


    @Test
    public void testSimpleExtends() {
        Board b = new Board();
        b.createMove('a', '7', 'a', '6');
        assertEquals("top left not red", RED, b.getContent('a', '7'));
        assertEquals("a6 not red", RED, b.getContent('a', '6'));
        assertEquals("redPieces not 3", 3, b.getColorNums(RED));
        assertEquals("moveNums not 1", 1, b.moveNums());
        assertEquals("getConsecJumpNums not 0", 0, b.getConsecJumpNums());
        assertEquals("not BLUE's move", BLUE, b.nextMove());
        checkBoard(b, REDEXTEND1);

        b.createMove('a', '1', 'b', '2');
        assertEquals("bottom left not blue", BLUE, b.getContent('a', '1'));
        assertEquals("b2 not blue", BLUE, b.getContent('b', '2'));
        assertEquals("bluePieces not 3", 3, b.getColorNums(BLUE));
        assertEquals("moveNums not 2", 2, b.moveNums());
        assertEquals("getConsecJumpNums not 0", 0, b.getConsecJumpNums());
        assertEquals("not RED's move", RED, b.nextMove());
        checkBoard(b, BLUEEXTEND1);
    }

    @Test
    public void testSimpleJumps() {
        Board b = new Board();
        b.createMove('a', '7', 'c', '7');
        assertEquals("top left not empty", EMPTY, b.getContent('a', '7'));
        assertEquals("c7 not red", RED, b.getContent('c', '7'));
        assertEquals("redPieces not 2", 2, b.getColorNums(RED));
        assertEquals("moveNums not 1", 1, b.moveNums());
        assertEquals("getConsecJumpNums not 1", 1, b.getConsecJumpNums());
        assertEquals("not BLUE's move", BLUE, b.nextMove());
        checkBoard(b, REDJUMP1);

        b.createMove('a', '1', 'c', '3');
        assertEquals("bottom left not empty", EMPTY, b.getContent('a', '1'));
        assertEquals("c3 not blue", BLUE, b.getContent('c', '3'));
        assertEquals("bluePieces not 2", 2, b.getColorNums(BLUE));
        assertEquals("moveNums not 2", 2, b.moveNums());
        assertEquals("getConsecJumpNums not 2", 2, b.getConsecJumpNums());
        assertEquals("not RED's move", RED, b.nextMove());
        checkBoard(b, BLUEJUMP1);
    }

    @Test
    public void testGradescope() {
        Board b = new Board();
        b.setBlock('b', '1');
        b.setBlock('c', '1');
        b.setBlock('a', '2');
        b.setBlock('c', '2');
        b.setBlock('a', '3');
        b.setBlock('b', '3');
        b.setBlock('a', '4');
        b.setBlock('b', '4');
        b.setBlock('c', '4');
        b.createMove('a', '7', 'b', '6');
        b.createMove('g', '7', 'f', '6');
        b.createMove('g', '1', 'f', '2');
        b.createMove('a', '1', 'b', '2');
        b.createMove('b', '6', 'c', '5');
        b.createMove('f', '6', 'e', '5');
        b.createMove('f', '2', 'e', '3');
        b.createMove('b', '2', 'c', '3');
        b.createMove('c', '5', 'd', '4');

        b.createMove('f', '6', 'd', '6');
        b.createMove('d', '4', 'd', '5');
        b.createMove('g', '7', 'f', '6');
        b.createMove('d', '6', 'd', '7');
        b.createMove('b', '2', 'd', '1');
        b.createMove('c', '3', 'b', '2');
        b.createMove('d', '1', 'd', '2');
        System.out.println(b.couldMove(BLUE));
        System.out.println(b.couldMove(RED));
        System.out.println(b);
        System.out.println(b.getWinner());
        b.createMove('d', '4', 'd', '3');
        System.out.println(b.getWinner());
    }

    private static void createMoves(Board b, String[] moves) {
        for (String s : moves) {
            b.createMove(s.charAt(0), s.charAt(1),
                    s.charAt(3), s.charAt(4));
        }
    }

    @Test
    public void testBlocks() {
        Board b = new Board();
        int originalTotalOpen = b.unblockedNum();
        assertTrue("blocks not legal at a2", b.blockLegal('a', '2'));
        assertTrue("blocks not legal at d4", b.blockLegal('d', '4'));

        b.setBlock('a', '2');
        assertEquals("wrong total open after block at a2",
                originalTotalOpen - 4, b.unblockedNum());
        b.setBlock('d', '4');
        assertEquals("wrong total open after block at d4",
                originalTotalOpen - 5, b.unblockedNum());
        checkBoard(b, BLOCKS1);

        assertFalse("blocks not placeable on pieces", b.blockLegal('a', '1'));
        assertFalse("blocks not placeable on other blocks",
                b.blockLegal('g', '6'));

        b.createMove('a', '7', 'b', '7');
        assertFalse("blocks not placeable once game starts",
                b.blockLegal('d', '5'));
    }

    @Test
    public void testGame() {
        Board b = new Board();
        String[] moves = {
                "a7-a6", "a1-b1",
                "g1-f1", "g7-f6",
                "a6-a5", "a1-a2",
                "g1-g2", "b1-b2"
        };
        createMoves(b, moves);
        checkBoard(b, GAMEEXTENDS);
        assertEquals("not 8 moves", 8, b.moveNums());
        assertEquals("not 6 red pieces", 6, b.getColorNums(RED));
        assertEquals("not 6 blue pieces", 6, b.getColorNums(BLUE));
        assertEquals("not RED's move", RED, b.nextMove());
        assertNull("no winner yet", b.getWinner());

        b.createMove('a', '5', 'a', '3');
        checkBoard(b, GAMEJUMP1);
        assertEquals("not 8 red pieces", 8, b.getColorNums(RED));
        assertEquals("not 4 blue pieces", 4, b.getColorNums(BLUE));
        assertEquals("not BLUE's move", BLUE, b.nextMove());

        b.createMove('a', '1', 'b', '3');
        checkBoard(b, GAMEJUMP2);
        assertEquals("not 5 red pieces", 5, b.getColorNums(RED));
        assertEquals("not 7 blue pieces", 7, b.getColorNums(BLUE));
        assertEquals("not RED's move", RED, b.nextMove());
        String[] moreMoves = {
                "a6-a4", "b2-b4",
                "a7-a5", "b3-b5",
                "g1-f2", "b5-c4",
                "f2-d3", "b4-c3",
                "f1-e1"
        };
        createMoves(b, moreMoves);
        checkBoard(b, GAMEJUMP3);
        assertNull("no winner yet", b.getWinner());
        assertEquals("not BLUE's move", BLUE, b.nextMove());

        b.createMove('d', '3', 'f', '2');
        checkBoard(b, GAMEJUMP4);
        assertEquals("no red pieces", 0, b.getColorNums(RED));
        assertEquals("blue should win", BLUE, b.getWinner());
    }

    @Test
    public void testJumps() {
        Board b = new Board();
        b.createMove('g', '1', 'g', '2');
        b.createMove('g', '7', 'g', '6');
        b.createMove('g', '2', 'g', '3');
        b.createMove('g', '6', 'g', '5');
        checkBoard(b, JUMPSINITIAL);
        b.createMove('g', '3', 'g', '4');
        checkBoard(b, JUMPS1);
        b.createMove('g', '6', 'f', '4');
        checkBoard(b, JUMPS2);
    }

    @Test
    public void testCopyAndClear() {
        Board b0 = new Board();
        createMoves(b0, GAME1);
        b0.clear();
        checkBoard(b0, INITIAL);
        assertNull("winner not null", b0.getWinner());
        assertEquals("redPieces not 2", 2, b0.getColorNums(RED));
        assertEquals("bluePieces not 2", 2, b0.getColorNums(BLUE));

        assertEquals("not red's move", RED, b0.nextMove());
        assertEquals("num moves not 0", 0, b0.moveNums());
        assertEquals("num jumps not 0", 0, b0.getConsecJumpNums());

        Board copyOfCleared = new Board(b0);
        createMoves(b0, GAME1);
        Board b1 = new Board(b0);
        checkBoard(b0, GAME1RESULT);
        checkBoard(b1, GAME1RESULT);
        checkBoard(copyOfCleared, INITIAL);
        assertNull("winner not null", copyOfCleared.getWinner());
        assertEquals("redPieces not 2", 2, copyOfCleared.getColorNums(RED));
        assertEquals("bluePieces not 2", 2, copyOfCleared.getColorNums(BLUE));

        assertEquals("not red's move", RED, copyOfCleared.nextMove());
        assertEquals("num moves not 0", 0, copyOfCleared.moveNums());
        assertEquals("num jumps not 0", 0, copyOfCleared.getConsecJumpNums());

        b0.createMove('b', '7', 'b', '5');
        checkBoard(b1, GAME1RESULT);
        assertNull("winner not null", b1.getWinner());
        assertEquals("redPieces not 4", 4, b1.getColorNums(RED));
        assertEquals("bluePieces not 6", 6, b1.getColorNums(BLUE));

        assertEquals("not red's move", RED, b1.nextMove());
        assertEquals("num jumps not 0", 0, b1.getConsecJumpNums());
    }



    private static final String[] GAME1 = {
            "a7-b7", "a1-a2",
            "a7-a6", "a2-a3",
            "a6-a5", "a3-a4"
    };

    private static final String[] UNDO2MOVES = {
            "a7-a6", "a1-b1",
            "g1-f1", "g7-f6",
            "a6-a5", "a1-a2",
            "g1-g2", "b1-b2",
            "a5-a3", "a1-b3",
            "a6-a4", "b2-b4",
            "a7-a5", "b3-b5",
            "g1-f2", "b5-c4",
            "f2-d3", "b4-c3",
            "f1-e1"
    };

    private static final PieceState[][] GAME1RESULT = {
            {RED, RED, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceState[][] INITIAL = {
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceState[][] REDEXTEND1 = {
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceState[][] BLUEEXTEND1 = {
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceState[][] REDJUMP1 = {
            {EMPTY, EMPTY, RED, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceState[][] BLUEJUMP1 = {
            {EMPTY, EMPTY, RED, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceState[][] BLOCKS1 = {
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {BLOCKED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLOCKED},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, BLOCKED, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLOCKED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLOCKED},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceState[][] GAMEEXTENDS = {
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, RED},
            {BLUE, BLUE, EMPTY, EMPTY, EMPTY, RED, RED}
    };

    private static final PieceState[][] GAMEJUMP1 = {
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {RED, RED, EMPTY, EMPTY, EMPTY, EMPTY, RED},
            {BLUE, BLUE, EMPTY, EMPTY, EMPTY, RED, RED}
    };

    private static final PieceState[][] GAMEJUMP2 = {
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, RED},
            {EMPTY, BLUE, EMPTY, EMPTY, EMPTY, RED, RED}
    };

    private static final PieceState[][] GAMEJUMP3 = {
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
            {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, BLUE, BLUE, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
            {EMPTY, BLUE, EMPTY, EMPTY, RED, RED, RED}
    };

    private static final PieceState[][] GAMEJUMP3UNDO = {
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
            {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, BLUE, BLUE, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
            {EMPTY, BLUE, EMPTY, EMPTY, EMPTY, RED, RED}
    };

    private static final PieceState[][] GAMEJUMP3ALT = {
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
            {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, BLUE, BLUE, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, RED, RED},
            {EMPTY, BLUE, EMPTY, EMPTY, EMPTY, RED, RED}
    };

    private static final PieceState[][] GAMEJUMP3ALT2 = {
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
            {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, BLUE, BLUE, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, BLUE, EMPTY, EMPTY, RED, RED, RED}
    };

    private static final PieceState[][] GAMEJUMP4 = {
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
            {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, BLUE},
            {EMPTY, BLUE, EMPTY, EMPTY, BLUE, BLUE, BLUE}
    };

    private static final PieceState[][] GAMEJUMP4ALT = {
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
            {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
            {EMPTY, BLUE, EMPTY, EMPTY, BLUE, BLUE, BLUE}
    };

    private static final PieceState[][] JUMPSINITIAL = {
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceState[][] JUMPS1 = {
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceState[][] JUMPS2 = {
            {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
            {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };
}
