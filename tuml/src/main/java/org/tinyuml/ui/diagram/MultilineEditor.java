/**
 * Copyright 2007 Wei-ju Wu
 *
 * This file is part of TinyUML.
 *
 * TinyUML is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * TinyUML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TinyUML; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.tinyuml.ui.diagram;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.tinyuml.draw.Defaults;
import org.tinyuml.draw.Label;
import org.tinyuml.draw.MultilineLayouter;
import org.tinyuml.draw.MultilineLayouter.MultilineLayout;

/**
 * An editor component to edit multiline labels. It is derived from JTextArea.
 *
 * @author Wei-ju Wu
 * @version 1.0
 */
public class MultilineEditor extends JTextArea
implements DocumentListener, TextEditor {

  private Label currentLabel;
  private static final int MAGIC_OFFSET = 7;

  /**
   * Constructor. Initially hidden.
   */
  public MultilineEditor() {
    setBorder(null);
    hideEditor();
    setWrapStyleWord(true);
    setLineWrap(true);
    getDocument().addDocumentListener(this);
  }

  /**
   * {@inheritDoc}
   */
  public Label getLabel() { return currentLabel; }

  /**
   * {@inheritDoc}
   */
  public void hideEditor() {
    setEditable(false);
    setEnabled(false);
    setVisible(false);
  }

  /**
   * {@inheritDoc}
   */
  public void showEditor(Label aLabel, Graphics g) {
    currentLabel = aLabel;
    String text = currentLabel.getText();
    int width = (int) currentLabel.getParent().getSize().getWidth()
      - (int) Defaults.getInstance().getMarginSide();
    int height = (int) currentLabel.getSize().getHeight() + MAGIC_OFFSET;
    // patch in a minimum size until something better is found
    height = Math.max(height, 20);
    setText(text);
    Dimension size = new Dimension(width, height);
    setPreferredSize(size);
    setSize(size);
    setMinimumSize(size);
    setLocation((int) aLabel.getAbsoluteX1(), (int) aLabel.getAbsoluteY1());
    setEditable(true);
    setEnabled(true);
    setVisible(true);
    requestFocusInWindow();
    selectAll();
  }

  /**
   * {@inheritDoc}
   */
  public void insertUpdate(DocumentEvent e) {
    // Here we need to implement the resizing method
    MultilineLayout layout = MultilineLayouter.getInstance().calculateLayout(
      ((Graphics2D) getGraphics()).getFontRenderContext(), getFont(),
      getText(), getSize().getWidth());
    Dimension2D size2D = layout.getSize();
    Dimension size = new Dimension((int) size2D.getWidth(),
      (int) size2D.getHeight() + MAGIC_OFFSET);
    setSize(size);
  }

  /**
   * {@inheritDoc}
   */
  public void removeUpdate(DocumentEvent e) { }

  /**
   * {@inheritDoc}
   */
  public void changedUpdate(DocumentEvent e) { }
}
