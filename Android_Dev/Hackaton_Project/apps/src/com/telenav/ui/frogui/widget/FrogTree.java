/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogTree.java
 *
 */
package com.telenav.ui.frogui.widget;

import java.util.Vector;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.ui.frogui.text.FrogTextHelper;

/**
 * Tree component which can show the content as tree architecture.
 * 
 *@author bduan
 *@date 2010-7-12
 */
public class FrogTree extends FrogList
{
    public final static int METHOD_INITIALIZE_TREE = 10000001;
    public final static int METHOD_SET_FG_COLOR = 10000002;
    public final static int METHOD_TREE_NODE_CLICKED = 10000003;
    public final static int KEY_EXPAND_IMAGE_FOCUS = -20000010;
    public final static int KEY_EXPAND_IMAGE_BLUR = -20000011;
    public final static int KEY_COLLAPSE_IMAGE_FOCUS = -20000012;
    public final static int KEY_COLLAPSE_IMAGE_BLUR = -20000013;
    
    protected int cursorIndex = -1;
    protected FrogTreeNode rootNode;
    protected Vector list;//for data
    protected FrogAdapter adapter;
    
    /**
     * Constructor of FrogTree 
     * 
     * @param id component id.
     */
    public FrogTree(int id)
    {
        super(id);
        initDefaultStyle();
    }
    

    protected void initDefaultStyle()
    {
        //no need to init common style for this component.
    }
    
    protected void initailizeDataList()
    {
        if(list == null)
        {
            list = new Vector();
        }
        else
        {
            list.removeAllElements();
        }
        
        if(rootNode == null || rootNode.getChildrenSize() == 0 )
            return;
        
        int size = rootNode.getChildrenSize();
        for(int i = 0 ; i < size; i++)
        {
            list.addElement(rootNode.getChildAt(i));
        }
    }
    
    protected void initializeTree()
    {
        initailizeDataList();
        
        if(adapter == null)
        {
            adapter = new FrogTreeAdapter();
            this.setAdapter(adapter);
        }
        else
        {
            this.setAdapter(adapter);
        }
    }
    
    /**
     * initialize the tree with {@link FrogTreeNode}
     * @param rootNode
     */
    public void initializeTree(FrogTreeNode rootNode)
    {
        this.rootNode = rootNode;
        initializeTree();
    }
    
    /**
     * get the data node bind to this tree. 
     * @return node
     */
    public FrogTreeNode getTreeNode()
    {
        return rootNode;
    }
    
    protected void paint(AbstractTnGraphics graphics)
    {

    }
    
    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_KEY_EVENT:
            {
                if (tnUiEvent.getKeyEvent() != null
                        && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_UP
                        && (tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_ENTER || tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_PAD_CENTER))
                {
                    nativeUiComponent.callUiMethod(METHOD_TREE_NODE_CLICKED, null);
                    return true;
                }
                break;
            }
        }
        return false;
    }
    
    private void handleNodeClicked(FrogTreeNodeItem treeNodeItem)
    {
        FrogTreeNode data = treeNodeItem.getTreeNode();
        if (data != null && data.isExpandable())
        {
            int index = list.indexOf(data);
            if (index > -1)
            {
                if (data.isExpanded())
                {
                    collapseNode(data);
                    updateAllItems(index);
                }
                else
                {
                    expandNode(data);
                    updateAllItems(index);
                }
            }
        }
    }
    
    private void expandNode(FrogTreeNode node)
    {
        cursorIndex = list.indexOf(node);
        for(int i = 0 ; i < node.getChildrenSize(); i ++)
        {
            FrogTreeNode child = (FrogTreeNode)node.getChildAt(i);
            list.insertElementAt(child, ++cursorIndex);
            if( child.isExpanded() )
            {
                expandNode(child);
            }
        }
        node.setExpanded(true);
    }
    
    protected void collapseNode(FrogTreeNode node)
    {
        int firstChildIndex = list.indexOf(node) + 1;
        
        if(firstChildIndex > -1)
        {
            int size = list.size();
            for(int i = firstChildIndex ; i < size ; i ++)
            {
                FrogTreeNode current = (FrogTreeNode)list.elementAt(firstChildIndex);
                if(current == null)
                {
                    break;
                }

                FrogTreeNode root = current.getParent();
                
                while(root != null && !root.equals(node))
                {
                    root = root.getParent();
                }
                
                if(root == null)
                {
                    break;
                }
                else
                {
                    list.removeElementAt(firstChildIndex);
                }
                
            }
            
            node.setExpanded(false);
        }
    }

    class FrogTreeAdapter implements FrogAdapter
    {
        public AbstractTnComponent getComponent(int position, AbstractTnComponent convertItem, AbstractTnComponent parent)
        {
            if (list != null && position >= 0 && position < list.size())
            {
                FrogTreeNode node = (FrogTreeNode)list.elementAt(position);
                if(convertItem == null)
                {
                    FrogTreeNodeItem treeNodeItem = new FrogTreeNodeItem(0);
                    treeNodeItem.setTnUiArgs(FrogTree.this.getTnUiArgs());
                    
                    treeNodeItem.setTreeNode(node);
                    TnMenu menu = new TnMenu();
                    menu.add("", position);
                    treeNodeItem.setMenu(menu, TYPE_CLICK);
                    treeNodeItem.setCommandEventListener(new ITnUiEventListener()
                    {
                        public boolean handleUiEvent(TnUiEvent tnUiEvent)
                        {
                            if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
                            {
                                handleNodeClicked((FrogTreeNodeItem) tnUiEvent.getComponent());
                            }
                            return false;
                        }
                    });
                    
                    return treeNodeItem;
                }
                else
                {
                    if(convertItem instanceof FrogTreeNodeItem)
                    {
                        ((FrogTreeNodeItem)convertItem).setTreeNode(node);
                    }
                    
                    return convertItem;
                }
            }
            return null;
        }

        public int getCount()
        {
            return list == null ? 0 : list.size();
        }

        public int getItemType(int position)
        {
            // TODO Auto-generated method stub
            return 0;
        }

    }
    
    static class FrogTreeNodeItem extends FrogListItem
    {
        protected FrogTreeNode treeNodeData;
        
        protected AbstractTnImage expandImage;
        protected AbstractTnImage expandImageFocus;
        protected AbstractTnImage collapseImage;
        protected AbstractTnImage collapseImageFocus;
        protected int gap;
        
        public FrogTreeNodeItem(int id)
        {
            super(id);
        }
        
        protected void initDefaultStyle()
        {
            if(font == null)
            {
                this.font = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).createDefaultFont();
            }
            
            if (boldFont == null)
            {
                boldFont = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).createDefaultBoldFont();
            }
            
            leftPadding = font.getMaxWidth() /2;
            rightPadding = leftPadding;
            topPadding = font.getHeight() / 2;
            bottomPadding = topPadding;
            gap = font.getMaxWidth();
        }
        
        public void setTnUiArgs(TnUiArgs tnUiArgs)
        {
            this.getTnUiArgs().copy(tnUiArgs);
            
            if(tnUiArgs.get(FrogTree.KEY_EXPAND_IMAGE_FOCUS) != null)
            {
                expandImageFocus = tnUiArgs.get(FrogTree.KEY_EXPAND_IMAGE_FOCUS).getImage();
            }
            
            if(tnUiArgs.get(FrogTree.KEY_EXPAND_IMAGE_BLUR) != null)
            {
                expandImage = tnUiArgs.get(FrogTree.KEY_EXPAND_IMAGE_BLUR).getImage();
            }
            
            if(tnUiArgs.get(FrogTree.KEY_COLLAPSE_IMAGE_FOCUS) != null)
            {
                collapseImageFocus = tnUiArgs.get(FrogTree.KEY_COLLAPSE_IMAGE_FOCUS).getImage();
            }
            
            if(tnUiArgs.get(FrogTree.KEY_COLLAPSE_IMAGE_BLUR) != null)
            {
                collapseImage = tnUiArgs.get(FrogTree.KEY_COLLAPSE_IMAGE_BLUR).getImage();
            }
        }
        
        public void sublayout(int width, int height)
        {
            this.setPreferredWidth(width);
            
            int iconHeight = 0;
            
            if(expandImage != null)
                iconHeight = expandImage.getHeight();
            
            if (iconHeight > font.getHeight())
            {
                preferHeight = iconHeight + topPadding + bottomPadding;
            }
            else
            {
                preferHeight = font.getHeight() + topPadding + bottomPadding;
            }
        }
        
        protected void initLeftPadding()
        {
            leftPadding = font.getMaxWidth() /2;
            FrogTreeNode temp = treeNodeData;
            while(temp.getParent() != null && temp.getParent().getText() != null)
            {
                temp = temp.getParent();
                leftPadding += font.getMaxWidth() * 2;
            }
        }
        
        protected void paint(AbstractTnGraphics graphics)
        {
            int y = 0;
            AbstractTnImage icon = null;
            if(treeNodeData.isExpandable())
            {
                if (isFocused())
                {
                    graphics.setColor(focusColor);
                    if(treeNodeData.isExpanded())
                    {
                        icon = collapseImageFocus;
                    }
                    else
                    {
                        icon = expandImageFocus;
                    }
                }
                else
                {
                    graphics.setColor(unfocusColor);
                    if(treeNodeData.isExpanded())
                    {
                        icon = collapseImage;
                    }
                    else
                    {
                        icon = expandImage;
                    }
                }
            }
            
            graphics.setFont(font);
            int left;
            initLeftPadding();
            if (icon != null)
            {
                left = leftPadding;
                y = (this.getHeight() - icon.getHeight()) / 2;
                if (y < topPadding)
                    y = topPadding;
                graphics.drawImage(icon, left, y, AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
                left = leftPadding + icon.getWidth() + gap;
                y = (this.getHeight() - font.getHeight()) / 2;
                int scrollWidth = this.getWidth() - icon.getWidth() - leftPadding - rightPadding - gap;
                graphics.pushClip(left, y, scrollWidth, this.getHeight());
                FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused
                        || !isScrollable);
                graphics.popClip();
            }
            else
            {
                if(isFocused())
                {
                    graphics.setColor(focusColor);
                }
                else
                {
                    graphics.setColor(unfocusColor);
                }
                
                left = leftPadding;
                y = (this.getHeight() - font.getHeight()) / 2;
                if (y < topPadding)
                    y = topPadding;
                int scrollWidth = this.getWidth() - leftPadding - rightPadding;
                graphics.pushClip(left, y, scrollWidth, this.getHeight());
                FrogTextHelper.paint(graphics, left - this.scrollX, y, this.textLine, font, boldFont, scrollWidth, !isFocused
                    || !isScrollable);
                graphics.popClip();
            }
        
        }
        
        /**
         * set treeNode data into item, and clear orginal data.
         * @param treeNode
         */
        public void setTreeNode(FrogTreeNode treeNode)
        {
            boolean isHasIndicator = false;
            if(collapseImageFocus != null && collapseImage != null && expandImageFocus != null && expandImage != null)
                isHasIndicator = true;
            
            this.setText(isHasIndicator ? treeNode.getText() : getDisplayStr(treeNode));
            this.initDefaultStyle();
            this.setPadding(this.getLeftPadding() << 1, this.getTopPadding(), this.getRightPadding(), this.getBottomPadding());
            this.treeNodeData = treeNode;
        }
        
        protected String getDisplayStr(FrogTreeNode treeNode)
        {
            String str = treeNode.getText();
            if(treeNode.isExpandable())
            {
                if(treeNode.isExpanded())
                {
                    str = " - " + str;
                }
                else
                {
                    str = " + " + str;
                }
            }
            return str;
        }
        
        /**
         * return the treeNode data for this item.
         * @return treeNode.
         */
        public FrogTreeNode getTreeNode()
        {
            return treeNodeData;
        }
        
        public FrogListItem getUiComponent()
        {
            return this;
        }
    }
}
