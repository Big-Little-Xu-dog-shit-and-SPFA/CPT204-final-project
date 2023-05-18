package ataxx;

import org.junit.Test;

import static ataxx.Move.*;
import static org.junit.Assert.*;

public class MoveTest {


    @Test
    public void testIsCloneTrue(){
        assertEquals(Boolean.valueOf("true"), Move.isClone("c1","d2"));
        assertEquals(Boolean.valueOf("true"), Move.isClone("b1","b2"));
    }

    @Test
    public void testIsCloneFalse(){
        assertEquals(Boolean.valueOf("false"), Move.isClone("c1","c1"));
        assertEquals(Boolean.valueOf("false"), Move.isClone("c1","c3"));
    }


    @Test
    public void testIsJumpTrue(){
        assertEquals(Boolean.valueOf("true"), Move.isJump("c1","d3"));
        assertEquals(Boolean.valueOf("true"), Move.isJump("c1","c3"));
    }

    @Test
    public void testIsJumpFalse(){
        assertEquals(Boolean.valueOf("false"), Move.isJump("c4","d3"));
        assertEquals(Boolean.valueOf("false"), Move.isJump("b2","b5"));
    }

    @Test
    public void testPass() {
        assertTrue("bad pass", pass() != null && pass().isPass());
        assertEquals("bad pass string", "-", pass().toString());
    }

    @Test
    public void testMove() {
        Move m = move('a', '3', 'b', '2');
        assertNotNull(m);
        assertFalse("move is pass", m.isPass());
        assertFalse("move is jump", m.isJump());

        Move m1 = move('g', '1', 'g', '2');
        assertNotNull(m1);
        assertFalse("move is pass", m1.isPass());
        assertFalse("move is jump", m1.isJump());

        Move m2 = move('a', '7', 'a', '4');
        assertNull(m2);
    }

    @Test
    public void testJump() {
        Move m = move('a', '3', 'a', '5');
        assertNotNull(m);
        assertFalse("move is pass", m.isPass());
        assertTrue("move not jump", m.isJump());

        Move m1 = move('g', '1', 'e', '3');
        assertNotNull(m1);
        assertFalse("move is pass", m1.isPass());
        assertTrue("move not jump", m1.isJump());
    }

    @Test
    public void testToString() {
        Move m = move('a', '3', 'a', '5');
        assertEquals("wrong string for a3-a5", "a3-a5", m.toString());

        Move m1 = move('g', '1', 'e', '3');
        assertEquals("wrong string for g1-e3", "g1-e3", m1.toString());

        assertEquals("wrong string for pass", "-", pass().toString());
    }
}
