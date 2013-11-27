/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AudioDataNode.java
 *
 */
package com.telenav.datatypes.audio;

import java.util.Vector;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-20
 */
public class AudioDataNode
{
    protected AudioData audioData;
    protected Vector children;
    
    protected AudioDataNode(AudioData audioData)
    {
        this.audioData = audioData;
    }
    
    public AudioData getAudioData()
    {
        return this.audioData;
    }
    
    public void addChild(AudioDataNode node)
    {
        if(children == null)
        {
            children = new Vector();
        }
        
        children.addElement(node);
    }
    
    public AudioDataNode getChild(int index)
    {
        if(children == null || children.size() <= index)
            return null;
        
        return (AudioDataNode)children.elementAt(index);
    }
    
    public int getChildrenSize()
    {
        return children == null ? 0 : children.size();
    }
}
