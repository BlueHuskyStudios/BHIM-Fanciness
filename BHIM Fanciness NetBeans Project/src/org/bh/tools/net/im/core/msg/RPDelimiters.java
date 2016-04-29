package org.bh.tools.net.im.core.msg;

import org.bh.tools.net.im.core.msg.RPMessageSegment;
import org.bh.tools.net.im.core.msg.RPMessageSegment.Type;
import static org.bh.tools.net.im.core.msg.RPMessageSegment.Type.*;
import static org.bh.tools.util.Do.S.s;


/**
 * RPDelimiters, made for BHIM, is copyright Blue Husky Programming ©2015 BH-1-PS <hr>
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0  <pre>
 *      - 2015-04-22 (1.0.0) - Kyli created Delimiters
 * </pre>
 *
 * @since 2015-04-22
 */
public class RPDelimiters {

    public static final RPDelimiters EMPTY = new RPDelimiters(null, null);
    public static final RPDelimiters GENERIC_SPEECH = new RPDelimiters(SPEECH.openTag(), SPEECH.closeTag(), SPEECH);
    public static final RPDelimiters GENERIC_MOVE = new RPDelimiters(MOVE.openTag(), MOVE.closeTag(), MOVE);
    public static final RPDelimiters GENERIC_OOC = new RPDelimiters(OOC.openTag(), OOC.closeTag(), OOC);
    public static final RPDelimiters GENERIC_THOUGHT = new RPDelimiters(THOUGHT.openTag(), THOUGHT.closeTag(), THOUGHT);


    private CharSequence open, close;
    private RPMessageSegment.Type implication;

    public RPDelimiters(CharSequence initOpen, RPMessageSegment.Type initImplication) {
        this(initOpen, null, initImplication);
    }

    public RPDelimiters(CharSequence initOpen, CharSequence initClose, RPMessageSegment.Type initImplication) {
        this.open = initOpen == null ? "" : initOpen;
        this.close = initClose == null ? "" : initClose;
        implication = initImplication;
    }

    public CharSequence getOpen() {
        return open;
    }

    public void setOpen(CharSequence newOpen) {
        open = newOpen == null ? "" : newOpen;
    }

    public CharSequence getClose() {
        return close;
    }

    public void setClose(CharSequence newClose) {
        close = newClose == null ? "" : newClose;
    }

    public Type getImplication() {
        return implication;
    }

    public void setImplication(Type newImplication) {
        implication = newImplication;
    }

    public RPMessageSegment extractSegment(CharSequence text, int startIndex) {
        // if it's impossible to find something starting and ending with these delimiters
        if (text.length() < startIndex + open.length() + close.length()) {
            return null;
        }
        String textStr = s(text), openStr = s(open), closeStr = s(close);

        // find & save first occurrence of open from startIndex
        int openLoc = textStr.indexOf(openStr, startIndex) + open.length(), closeLoc;
        // If the delimiter has an end
        if (close != null && !closeStr.isEmpty()) {
            // Find first occurrence of close from startIndex
            closeLoc = textStr.indexOf(closeStr, openLoc);
        } // Else
        else {
            // Use length
            closeLoc = text.length();
        }
        // Save that location as endIndex
        // Return substring from startIndex to endIndex
        return new RPMessageSegment(text.subSequence(openLoc, closeLoc), this);
    }

    /**
     * Determines whether this delimiter is a prefix for the given text, looking from the given start index
     *
     * @param text       The text to search
     * @param startIndex Where to start searching
     *
     * @return {@code true} iff {@code this} is a prefix for {@code text}
     */
    public boolean isPrefixFor(CharSequence text, int startIndex) {
        if (text.length() < startIndex + open.length()) {
            return false;
        }
        return text.subSequence(startIndex, startIndex + open.length()).toString().equalsIgnoreCase(open.toString());
    }

    @Override
    public String toString() {
        if (implication == null) {
            return "plaintext";
        }
        return open + "<" + implication.name() + ">" + close;
    }

    boolean closes() {
        return close != null && !s(close).isEmpty();
    }
}
