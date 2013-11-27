/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogTreeNode.java
 *
 */
package com.telenav.ui.frogui.widget;

import java.util.Vector;

/**
 * 
 * It's a data structure for saving tree node.
 * <br>
 * Can be recursive.
 * 
 *@author bduan
 *@date 2010-7-12
 */
public class FrogTreeNode
{
    private FrogTreeNode parent;
        
    private Vector children;
    
    private boolean isExpanded;
    
    private String text;
    
    private int id = -1;
    
    /**
     * default constructor for FrogTreeNode
     */
    public FrogTreeNode()
    {
    }
    
    /**
     * Constructor
     * 
     * @param text
     * @param id
     */
    public FrogTreeNode(String text, int id)
    {
        this.text = text;
        this.id = id;
    }

    /**
     * set text for tree node.
     * @param str
     */
    public void setText(String str)
    {
        this.text = str;
    }
    
    /**
     * get current tree node text.
     * @return text of tree node.
     */
    public String getText()
    {
        return text;
    }
    
    /**
     * get root node.
     * @return root node. maybe null
     */
    public FrogTreeNode getParent()
    {
        return parent;
    }
    
    /**
     * set root of current node
     * @param parent
     */
    public void setParent(FrogTreeNode parent)
    {
        this.parent = parent;
    }
    
    /**
     * get child base on index if exist.
     * @param index
     * @return child, or null if not exist.
     */
    public FrogTreeNode getChildAt(int index)
    {
        if(children != null && index > -1 && index < children.size())
        {
            return (FrogTreeNode)children.elementAt(index);
        }
        return null;
    }
    
    /**
     * remove the child.
     * @param index
     */
    public void removeChildAt(int index)
    {
        if(children != null && index > -1 && index < children.size())
        {
            children.removeElementAt(index);
        }
    }
    
    /**
     * Removes all components from this Node and sets its size to zero.
     */
    public void removeAllChildren()
    {
        if(children != null)
            children.removeAllElements();
    }
    
    /**
     * get children vector
     * @return the vector that contains all children, maybe null.
     */
    public int getChildrenSize()
    {
        if(children == null)
            return 0;
        return children.size();
    }
    
    /**
     * set the state of current node.
     * @param isExpanded
     */
    public void setExpanded(boolean isExpanded)
    {
        this.isExpanded = isExpanded;
    }
    
    /**
     * get current state of current node.
     * @return ifExpanded. if there's no children, it always be false.
     */
    public boolean isExpanded()
    {
        return isExpandable() && isExpanded;
    }
    
    /**
     * check if this node can be expand.
     * @return true if there's at least one child, false if no any. 
     */
    public boolean isExpandable()
    {
        if(children != null && children.size() > 0)
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * set the id for current node.
     * @param id
     */
    public void setId(int id)
    {
        this.id = id;
    }
    
    /**
     * get the id of current node.
     * @return id
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * add a child
     * @param childNode
     */
    public void addChild(FrogTreeNode childNode)
    {
        if(children == null)
        {
            children = new Vector();
        }
        
        childNode.setParent(this);
        children.addElement(childNode);
    }
    
    /**
     * clear the tree node, including all its children.
     */
    public void clear()
    {
        parent = null;
        text = null;
        isExpanded = false;
        
        if(isExpandable())
        {
            for(int i = 0 ; i < children.size() ; i ++)
            {
                FrogTreeNode FrogTreeNode = (FrogTreeNode)children.elementAt(i);
                FrogTreeNode.clear();
                FrogTreeNode = null;
            }
        }
        
        if(children != null)
            children.removeAllElements();
        children = null;
    }
    
}
