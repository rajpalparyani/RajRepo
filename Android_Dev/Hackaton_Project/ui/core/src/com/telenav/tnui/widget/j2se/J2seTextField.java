package com.telenav.tnui.widget.j2se;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.INativeUiComponent;
import com.telenav.tnui.core.j2se.J2seBasicListener;
import com.telenav.tnui.core.j2se.J2seUiMethodHandler;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnTextField;

public class J2seTextField extends JFormattedTextField implements INativeUiComponent, ActionListener
{

    /**
     * 
     */
    private static final long serialVersionUID = 1296080864642066685L;

    protected TnTextField textField;
    
    public J2seTextField(TnTextField textField)
    {
        super();
        //TODO
//        this.setInputVerifier(new J2seInputVerify());
//        this.setDocument(new J2seDocument());
        this.addActionListener(this);
        
        this.textField = textField;
        
        J2seBasicListener listener = new J2seBasicListener(this.textField);
        this.addFocusListener(listener);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.addKeyListener(listener);
    }
    
    public Dimension getPreferredSize()
    {
        this.textField.sublayout(this.getWidth(), this.getHeight());
        
        int prefWidth = this.textField.getPreferredWidth();
        int prefHeight = this.textField.getPreferredHeight();
        
        prefHeight = this.textField.getFont().getHeight() + 4;
        if(prefWidth > 0 || prefHeight > 0)
        {
            if(prefWidth <= 0)
            {
                prefWidth = this.getRootPane().getWidth() - 2;
            }
        }
        
        return new Dimension(prefWidth, prefHeight);
    }
    
    public Dimension getMinimumSize()
    {
        return this.getPreferredSize();
    }
    
    public Dimension getMaximumSize()
    {
        return this.getPreferredSize();
    }
    
    public void setBounds(int x, int y, int width, int height)
    {
        if(this.getWidth() != width || this.getHeight() != height)
        {
            this.textField.dispatchSizeChanged(width, height, this.getWidth(), this.getHeight());
        }
        
        super.setBounds(x, y, width, height);
    }
    
    public void paint(Graphics g)
    {
        Font font = g.getFont();
        Color color = g.getColor();
        
        AbstractTnGraphics.getInstance().setGraphics(g);
        textField.draw(AbstractTnGraphics.getInstance());
        
        g.setFont(font);
        g.setColor(color);
        
        super.paint(g);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        //TODO
        if(textField != null)
        {
            textField.notifyTextChanged(false, this.getText().toString());
        }    
    }
    
    public Object callUiMethod(int eventMethod, Object[] args)
    {
        Object obj = J2seUiMethodHandler.callUiMethod(textField, this, eventMethod, args);

        if (!J2seUiMethodHandler.NO_HANDLED.equals(obj))
            return obj;
        
        switch(eventMethod)
        {
            case TnTextField.METHOD_SET_TEXT_SIZE:
            {
                this.setFont((Font)this.textField.getFont().getNativeFont());
                break;
            }
            case TnTextField.METHOD_GET_CURSOR_INDEX:
            {
                return Integer.valueOf(this.getSelectionStart());
            }
            case TnTextField.METHOD_GET_TEXT:
            {
                return this.getText().toString();
            }
            case TnTextField.METHOD_SET_CURSOR_INDEX:
            {
                if(((Integer)args[0]).intValue() > this.getText().length())
                {
                    this.setSelectionStart(this.getText().length());
                }
                else
                {
                    this.setSelectionStart(((Integer)args[0]).intValue());
                }
                break;
            }
            case TnTextField.METHOD_SET_HINT:
            {
                this.setToolTipText((String)args[0]);
                break;
            }
            case TnTextField.METHOD_SET_TEXT:
            {
                this.setText((String)args[0]);
                break;
            }
            case TnTextField.METHOD_SET_ENABLE:
            {
                this.setEnabled(((Boolean)args[0]).booleanValue());
                break;
            }
            case TnTextField.METHOD_SELECT_ALL:
            {
                this.selectAll();
                break;
            }
            case TnTextField.METHOD_SET_PADDING:
            {
                if(args != null && args.length > 3)
                {
                    int leftPadding = ((Integer)args[0]).intValue();
                    int rightPadding = ((Integer)args[1]).intValue();
                    int topPadding = ((Integer)args[2]).intValue();
                    int bottomPadding = ((Integer)args[3]).intValue();
                    this.setBorder(new EmptyBorder(topPadding, leftPadding, bottomPadding, rightPadding));
                }
                break;
            }
            case TnTextField.METHOD_SET_SINGLELINE:
            {
                break;
            }
            case TnTextField.METHOD_SET_INPUTBOX_WIDTH:
            {
                if(args != null && args.length > 0)
                {
                    this.setSize(((Integer)args[0]).intValue(), this.getHeight());
                }
                break;
            }
            case TnTextField.METHOD_SET_INPUTBOX_HEIGHT:
            {
                if(args != null && args.length > 0)
                {
                    this.setSize(this.getWidth(), ((Integer)args[0]).intValue());
                }
                break;
            }
            case TnTextField.METHOD_CLOSE_VIRTUAL_KEYBOARD:
            {
                //
                break;
            }
            case TnTextField.METHOD_GET_SCROLLX:
            {
                return Integer.valueOf(getScrollOffset());
            }
            case TnTextField.METHOD_GET_SCROLLY:
            {
                return Integer.valueOf(0);
            }
        }
        return null;
    }

    public int getNativeHeight()
    {
        return this.getHeight();
    }

    public int getNativeWidth()
    {
        return this.getWidth();
    }

    public int getNativeX()
    {
        return this.getX();
    }

    public int getNativeY()
    {
        return this.getY();
    }

    public AbstractTnComponent getTnUiComponent()
    {
        return this.textField;
    }

    public boolean isNativeFocusable()
    {
        return this.isFocusable();
    }

    public boolean isNativeVisible()
    {
        return this.isVisible();
    }

    public boolean requestNativeFocus()
    {
        return this.requestFocusInWindow();
    }

    public void requestNativePaint()
    {
        this.repaint();
    }

    public void setNativeFocusable(boolean isFocusable)
    {
        this.setFocusable(isFocusable);
    }

    public void setNativeVisible(boolean isVisible)
    {
        this.setVisible(isVisible);
    }

    class J2seInputVerify extends InputVerifier
    {

        public boolean verify(JComponent input)
        {
            if (input instanceof JFormattedTextField)
            {
                JFormattedTextField ftf = (JFormattedTextField) input;
                String text = ftf.getText();
                if(textField.getMaxSize() > 0 && text.length() > textField.getMaxSize())
                    return false;
                try
                {
                    switch(textField.getConstraints())
                    {
                        case TnTextField.NUMERIC:
                            NumberFormat.getNumberInstance().parse(text);
                            break;
                        case TnTextField.ADDRESS:
                            if(text.endsWith(" "))
                                return true;
                            NumberFormat.getNumberInstance().parse(text);
                            break;
                        case TnTextField.PHONENUMBER:
                            NumberFormat.getNumberInstance().parse(text);
                            break;
                    }
                    return true;
                }
                catch (ParseException pe)
                {
                    return false;
                }
            }
            return true;

        }
        
    }
    
    class J2seDocument extends PlainDocument
    {
        /**
         * 
         */
        private static final long serialVersionUID = 8720399642461186078L;

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
        {
            if (str == null)
            {
                return;
            }
            
            char[] upper = str.toCharArray();
            for (int i = 0; i < upper.length; i++)
            {
                switch (textField.getTextType())
                {
                    case TnTextField.TEXT_TYPE_CAPITALIZED_WORD:
                        if (i == 0)
                        {
                            upper[i] = Character.toUpperCase(upper[i]);
                        }
                        upper[i] = Character.toLowerCase(upper[i]);
                        break;
                    case TnTextField.TEXT_TYPE_LOWER_CASE:
                        upper[i] = Character.toLowerCase(upper[i]);
                        break;
                    case TnTextField.TEXT_TYPE_UPPER_CASE:
                        upper[i] = Character.toUpperCase(upper[i]);
                        break;
                }
            }
            
            super.insertString(offs, new String(upper), a);
        }

    }
}
