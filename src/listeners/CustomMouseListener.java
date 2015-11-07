package listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Interface of MouseListener that contains default methods.
 * @see java.awt.event.MouseListener
 */
public interface CustomMouseListener extends MouseListener {
    void mouseClicked(MouseEvent e);
    default void mousePressed(MouseEvent e) {}
    default void mouseReleased(MouseEvent e) {}
    default void mouseEntered(MouseEvent e) {}
    default void mouseExited(MouseEvent e) {}

}
