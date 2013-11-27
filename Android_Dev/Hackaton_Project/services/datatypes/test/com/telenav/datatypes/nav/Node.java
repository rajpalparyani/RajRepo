/**
 * @author alexg
 */
package com.telenav.datatypes.nav;


import com.telenav.datatypes.DataUtil;
import com.telenav.logger.Logger;

/**
 * @author alexg
 *
 */
class State
{
    protected int step = 0;
    protected Node node;
    protected int offset;
    protected int currMsg = 0;
    protected int currChild = 0;
    protected int depth = 0;
}

class Node
{
    public static final int VERSION_TNT         = 31;
    public static final int VERSION_50          = 50;
    public static final int VERSION_55          = 55;
    public static final int VERSION_551         = 551;

    // 50 version does not have a header
    public static final int HEADER_55           = 983276848;
    public static final int HEADER_551          = 983276849;

    final static private int INIT_MSG_SIZE = 10;
    final static private int INIT_CHILDREN_SIZE = 10;
    
    protected byte [] binData = null;
    
    protected short   nValues = 0;
    protected short nMsgs = 0;
    protected short nChildren = 0;

    protected short valuesBuffOffset = 0;
    protected byte[] valuesBuff;

    // collapsed objects
    protected byte[][] msgsBuff = null;
    protected byte[][] childrenBuff = null;
    
    //expanded message
    protected String[] msgs = null;
    // expanded children
    protected Node[] children = null;
//  private String[] msgs = null;
    protected Node parent;

    //private Object mutex = new Object();
    
    protected int[] offsetValue = new int [1];
    
    protected int version = VERSION_55;
    private static String UTF_8 = "UTF-8";
    
    public interface INodeListener
    {
        public void startNode(int depth);
        public void endNode(int depth, Node node);
    }
    
    Node()
    {
        
    }
    
    Node(byte[] binary, int startIndex)
    {
        if (binary.length >= startIndex + 4)
        {
            int headerCode = DataUtil.readInt(binary, startIndex);
            if (headerCode == HEADER_55)
            {
                fromByteArray55(binary, startIndex + 4, VERSION_55, null);
            }
            else if (headerCode == HEADER_551)
            {
                fromByteArray55(binary, startIndex + 4, VERSION_551, null);
            }
        }
    }

    Node(byte[] binary, int startIndex, int version)
    {
        fromByteArray55(binary, startIndex + 4, version, null);
    }

    public int getVersion()
    {
        return version;
    }
    
    public void setVersion(int version)
    {
        this.version = version;
    }

    
    /* (non-Javadoc)
     * @see com.telenav.framework.data.INode#addValue(long)
     */
    public void addValue(long value)
    {
        synchronized (this)
        {
            nValues++;
            checkValueBufferSize(value);
            valuesBuffOffset += DataUtil.writeValue(valuesBuff, value, valuesBuffOffset);
        }
    }
    
    private void checkValueBufferSize(long value)
    {
        if (valuesBuff == null || valuesBuff.length < valuesBuffOffset + DataUtil.getValueLength(value))
        {
            byte[] temp = new byte[valuesBuffOffset + 20];
            if (valuesBuff != null)
            {
                System.arraycopy(valuesBuff, 0, temp, 0, valuesBuffOffset);
            }
            valuesBuff = temp;
        }       
    }
   
    /* (non-Javadoc)
     * @see com.telenav.framework.data.INode#getValueAt(int)
     */
    public long getValueAt(int index)
    {
        if (index >= nValues) return 0;
        
        offsetValue[0] = 0;
        
        //int[] offset = new int[] { 0 };
        for (int i = 0; i < index; i++)
        {
            DataUtil.readOffset(valuesBuff, offsetValue);
        }
        return DataUtil.readValue(valuesBuff, offsetValue);
    }

    public void setValueAt(long value, int index)
    {
        synchronized (this)
        {
            int oldSize = getValuesSize();
            for (int i = oldSize; i < index + 1; i++)
            {
                addValue(0);
            }
            
            int[] offset = new int[1];
            for (int i = 0; i < index; i++)
            {
                DataUtil.readOffset(valuesBuff, offset);
            }
    
            int startOffset = offset[0];
            DataUtil.readOffset(valuesBuff, offset);
            int endOffset = offset[0];
            
            checkValueBufferSize(value);
            int newValLength = DataUtil.getValueLength(value);
            int oldValLength = endOffset - startOffset;
            if (newValLength != oldValLength)
            {
                // need to move old values
                short delta = (short) (newValLength - oldValLength);
                int len = valuesBuffOffset - endOffset;
                System.arraycopy(valuesBuff, endOffset, valuesBuff, endOffset + delta, len);                
                valuesBuffOffset = (short) (valuesBuffOffset + delta);
            }
            DataUtil.writeValue(valuesBuff, value, startOffset);
        }
    }

    public void setStringAt(String str, int index)
    {
        synchronized (this)
        {
            int oldSize = getStringsSize();
            for (int i = oldSize; i < index + 1; i++)
            {
                addString(null);
            }
        
            try
            {
                if (str != null)
                {
                    msgsBuff[index] = str.getBytes(UTF_8);
                }
                else
                {
                    msgsBuff[index] = null;
                }
                
                if(msgs != null && index < msgs.length)
                {
                    msgs[index] = str;
                }
            }
            catch(Throwable e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }       
    }

    
    public boolean isEmpty()
    {
        return getValuesSize() == 0 && getStringsSize() == 0 && getChildrenSize() == 0 && getBinaryData() == null;
    }

    /* (non-Javadoc)
     * @see com.telenav.framework.data.INode#getValuesSize()
     */
    public int getValuesSize()
    {
        return nValues;
    }

    /* (non-Javadoc)
     * @see com.telenav.framework.data.INode#addString(java.lang.String)
     */
    public void addString(String str)
    {
        synchronized (this)
        {
            if (this.msgsBuff == null) 
            {
                this.msgsBuff = new byte[INIT_MSG_SIZE][];
            }
            else if (this.msgsBuff.length <= nMsgs)
            {
                // we copy only references here, the binaries themselves do not move
                byte[][] temp = new byte[nMsgs + INIT_MSG_SIZE][];
                System.arraycopy(msgsBuff, 0, temp, 0, msgsBuff.length);
                msgsBuff = temp;
            }
            
            try
            {
                if (str != null)
                {
                    msgsBuff[nMsgs] = str.getBytes(UTF_8);
                }
                else
                {
                    msgsBuff[nMsgs] = null;
                }
            }
            catch(Throwable e)
            {
                msgsBuff[nMsgs] = null;
                Logger.log(this.getClass().getName(), e);
            }
            
            nMsgs++;
        }
    }

    /* (non-Javadoc)
     * @see com.telenav.framework.data.INode#getStringAt(int)
     */
    public String getStringAt(int index)
    {
        if (this.msgs != null  && index < this.msgs.length && this.msgs[index] != null)
        {
            return this.msgs[index];
        }
        else
        {
            if (this.msgsBuff != null && index < this.msgsBuff.length)
            {
                synchronized (this)
                {
                    // move collapsed child to expanded
                    if (this.msgs == null)
                    {
                        this.msgs = new String[msgsBuff.length];
                    }
                    else if (this.msgs.length < this.msgsBuff.length)
                    {
                        String[] temp = new String[msgsBuff.length];
                        System.arraycopy(msgs, 0, temp, 0, msgs.length);
                        msgs = temp;
                    }
                    
                    if (msgsBuff[index] != null)
                    {
                        try
                        {
                            this.msgs[index] = new String(msgsBuff[index], UTF_8);
                        }
                        catch (Throwable e)
                        {
                            Logger.log(this.getClass().getName(), e);
                        }
                    }                   

                    return this.msgs[index];
                }
            }           
        }
        
        return null;
    }

    /* (non-Javadoc)
     * @see com.telenav.framework.data.INode#getStringsSize()
     */
    public int getStringsSize()
    {
        return nMsgs;
    }

    public void removeAllStrings()
    {
        this.msgs = null;
        this.msgsBuff = null;
        this.nMsgs = 0;
    }
    
    /* (non-Javadoc)
     * @see com.telenav.framework.data.INode#setBinaryData(byte[])
     */
    public void setBinaryData(byte[] binary)
    {
        synchronized (this)
        {
            this.binData = binary;
        }
    }

    /* (non-Javadoc)
     * @see com.telenav.framework.data.INode#getBinaryData()
     */
    public byte[] getBinaryData()
    {
        return this.binData;
    }

    /* (non-Javadoc)
     * @see com.telenav.framework.data.INode#addChild(java.lang.Object)
     */
    public Node addChild(Object child)
    {
        synchronized (this)
        {
            if (getChildrenSize() > 0)
            {
                // first, need to expand children
                getChildAt(getChildrenSize() - 1);
            }
            
            if (this.children == null) 
            {
                if (nChildren < INIT_CHILDREN_SIZE)
                {
                    this.children = new Node[INIT_CHILDREN_SIZE];
                }
                else
                {
                    this.children = new Node[nChildren + INIT_CHILDREN_SIZE];                   
                }
            }
            else if (this.children.length <= nChildren)
            {
                // we copy only references here, the binaries themselves do not move
                Node[] temp = new Node[nChildren + INIT_CHILDREN_SIZE];
                System.arraycopy(children, 0, temp, 0, children.length);
                children = temp;
            }
    
            children[nChildren] =  (Node) child;
            children[nChildren].parent = this;
            
//          if (child != null)
//          {
//              childrenBuff[childrenBuff.length - 1] = ((TxNode55) child).toBinary(-1);
//          }
//          else
//          {
//              childrenBuff[childrenBuff.length - 1] = new byte[0];
//          }
            
            nChildren++;
        }
        
        return this;
    }

    public void removeChildAt(int index)
    {
        synchronized (this)
        {
            // first call getChildAt() to expand children array
            getChildAt(index);
            if (this.children != null && index > -1 && index < nChildren)
            {
                for (int i = index; i < nChildren - 1; i++)
                {
                    this.children[i] = this.children[i+1];
                }
                nChildren--;
            }
        }
    }
    
    public void insertChildAt(Node node, int index)
    {
        synchronized (this)
        {
            if (index > -1 && index <= nChildren)
            {
                
                // NOTE:
                // before insert node, we must make sure all children buff are got.
                // this is a SAFE solution, but not an efficiency solution.
                // the truth is, we don't encourage to insert child at a serialized TxNode.
                // if we really want to change a big serialized TxNode in the future, let's enhance these codes.
                // for now, it is a SAFE solution, will cause little regression.
                // - zhdong
                
                if (this.childrenBuff != null)
                {
                    for (int i = 0; i < childrenBuff.length; i++)
                    {
                        this.getChildAt(i);
                    }
                    
                    // finally, destroy childrenBuff
                    childrenBuff = null;
                }
                
                // add last node to make a hole
                Node lastNode = getChildAt(getChildrenSize() - 1);
                addChild(lastNode);
                for (int i = nChildren - 2; i > index; i--)
                {
                    this.children[i] = this.children[i-1];
                }
                this.children[index] = (Node) node;
                this.children[index].parent = this;
            }
        }
    }

    public void setChildAt(Node node, int index)
    {
        synchronized (this)
        {
            // first call getChildAt() to expand children array
            getChildAt(index);      
            
            int oldSize = getChildrenSize();
            for (int i = oldSize; i < index + 1; i++)
            {
                addChild(null);
            }
            
            this.children[index] = (Node) node;
            this.children[index].parent = this;
        }
    }

    /* (non-Javadoc)
     * @see com.telenav.framework.data.INode#getChildAt(int)
     */
    public Node getChildAt(int index)
    {
        if (this.children != null  && index < this.children.length && this.children[index] != null)
        {
            // System.out.println("TxNode55::getChildAt: "+index);
            return this.children[index];
        }
        else
        {
            if (this.childrenBuff != null && index < this.childrenBuff.length)
            {
                synchronized (this)
                {
                    // move collapsed child to expanded
                    if (this.children == null)
                    {
                        this.children = new Node[childrenBuff.length];
                    }
                    else if (this.children.length < this.childrenBuff.length)
                    {
                        // make sure children array is not shorter then childrenBuff
                        Node[] temp = new Node[childrenBuff.length];
                        System.arraycopy(children, 0, temp, 0, children.length);
                        children = temp;
                    }
                    
                    if (childrenBuff[index] != null)
                    {
                        this.children[index] = new Node();
                        this.children[index].fromByteArray55(childrenBuff[index], 0, getVersion(), this);
                        this.children[index].parent = this;
                        // free-up binary slot
                        childrenBuff[index] = null;
                    }                   

                    // System.out.println("TxNode55::getChildAt - expand from binary: "+index);

                    return this.children[index];
                }
            }           
        }
        
        return null;
    }

    /* (non-Javadoc)
     * @see com.telenav.framework.data.INode#getChildBinaryAt(int)
     */
    public byte[] getChildBinaryAt(int index)
    {
        if (this.children != null  && index < this.children.length && this.children[index] != null)
        {
            return this.children[index].toBinary();
        }
        else
        {
            if (this.childrenBuff != null && index < this.childrenBuff.length)
            {
                return this.childrenBuff[index];
            }
            else
            {
                return null;
            }           
        }
    }

    /* (non-Javadoc)
     * @see com.telenav.framework.data.INode#getChildrenSize()
     */
    public int getChildrenSize()
    {
        return nChildren;
//      int buffLen = 0, len = 0;
//      if (this.childrenBuff != null) buffLen = this.childrenBuff.length;
//      if (this.children != null) len = this.children.length;
//      
//      return (buffLen > len) ? buffLen : len;
    }

    /*
    public byte[] toBinary()
    {
//      System.out.println("TxNode55::toBinary() - clone to TxNode");

        TxNode clone = fromNode55();
        
//      System.out.println("TxNode55::toBinary() - TxNode.toBinary()");
        
        byte[] buff = clone.toBinary();
        
//      System.out.println("TxNode55::toBinary() - buff: "+buff.length);
        
        return buff;
    }
    */
    
    public byte [] toBinary()
    {
        if (getVersion() == VERSION_55)
        {
            return toBinary55(HEADER_55, VERSION_55);
        }
        else if (getVersion() == VERSION_551)
        {
            return toBinary55(HEADER_551, VERSION_551);         
        }
        else
        {
            return null;
        }
    }
    
    /*
    //XXX: this is temp methods for migration from TxNode to TxNode55
    // when c-server is ready to receive 5.5 stream and produce 5.5 stream
    // we need to remove these two methods
    private TxNode fromNode55()
    {
        TxNode node = new TxNode();
//      System.out.println("TxNode55::fromNode55(), getValuesSize(): "+getValuesSize());
        for (int i = 0; i < getValuesSize(); i++)
        {
//          System.out.println("TxNode55::fromNode55(), "+getValueAt(i));
            node.addValue(getValueAt(i));
        }
        
//      System.out.println("TxNode55::fromNode55(), getStringsSize(): "+getStringsSize());
        for (int i = 0; i < getStringsSize(); i++)
        {
//          System.out.println("TxNode55::fromNode55(), getStringAt(i): "+getStringAt(i));
            node.addString(getStringAt(i));
        }
        
//      System.out.println("TxNode55::fromNode55(), getChildrenSize(): "+getChildrenSize());
        for (int i = 0; i < getChildrenSize(); i++)
        {
            TxNode55 child = (TxNode55) getChildAt(i);
            TxNode cloneChild = child.fromNode55();
            node.addChild(cloneChild);
        }
        
        node.setBinaryData(getBinaryData());
        
        return node;
    }

    
    private void toNode55(TxNode node)
    {
        for (int i = 0; i < node.getValuesSize(); i++)
        {
            addValue(node.getValueAt(i));
        }
        
        for (int i = 0; i < node.getStringsSize(); i++)
        {
            addString(node.getStringAt(i));
        }
        
        for (int i = 0; i < node.getChildrenSize(); i++)
        {
            TxNode child = (TxNode) node.getChildAt(i);
            TxNode55 child55 = new TxNode55();
            child55.toNode55(child);
            addChild(child55);
        }
        
        setBinaryData(node.getBinaryData());
    }
    */
    
    /* (non-Javadoc)
     * @see com.telenav.framework.data.INode#toBinary()
     */
    private byte[] toBinary55(int header, int ver)
    {
        int size = 0;
        if (ver == VERSION_55)
        {
            size = 1 + DataUtil.getValueLength(valuesBuffOffset) + valuesBuffOffset;    // 1 byte for nValues
        }
        else if (ver == VERSION_551)
        {
            size = DataUtil.getValueLength(nValues) + DataUtil.getValueLength(valuesBuffOffset) + valuesBuffOffset;
        }
    
//      int nMsgs = 0;
        
        /*
        if (nMsgs > 0)
        {
            if (msgsBuff == null)
            {
                msgsBuff = new byte[nMsgs][];
            }
            else if (msgsBuff.length < nMsgs)
            {
                byte[][] temp = new byte[nMsgs][];
                System.arraycopy(msgsBuff, 0, temp, 0, msgsBuff.length);
                msgsBuff = temp;
            }
        }
        */
        
        int len = 0;
        for (int i = 0; i < nMsgs; i++)
        {
            if (msgsBuff[i] != null)
            {
                len += (msgsBuff[i].length + DataUtil.getValueLength(msgsBuff[i].length));
            }
            else if (ver == VERSION_55)
            {
                len += DataUtil.getValueLength(0);                    
            }
            else if (ver == VERSION_551)
            {
                len += DataUtil.getValueLength(-1);
            }
        }           

        size += (DataUtil.getValueLength(nMsgs) + len);
        
        len = 0;
        
        if (nChildren > 0)
        {
            if (childrenBuff == null)
            {
                childrenBuff = new byte[nChildren][];
            }
            else if (childrenBuff.length < nChildren)
            {
                byte[][] temp = new byte[nChildren][];
                System.arraycopy(childrenBuff, 0, temp, 0, childrenBuff.length);
                childrenBuff = temp;
            }
        }
        
        for (int i = 0; i < nChildren; i++)
        {
            if (children != null && children[i] != null)
            {
                childrenBuff[i] = children[i].toBinary55(-1, ver);
            }
            
            if (childrenBuff[i] != null)
            {
                len += (childrenBuff[i].length + DataUtil.getValueLength(childrenBuff[i].length));
            }
            else
            {
                len += (0 + DataUtil.getValueLength(0));
            }
        }
        
        size += (DataUtil.getValueLength(nChildren) + len);
        
        int binLen = 0;
        if (binData != null)
        {
            binLen = binData.length;
        }

        size += (DataUtil.getValueLength(binLen) + binLen);

        if (header == HEADER_55 || header == HEADER_551) size += 4;
        
        byte[] buff = new byte[size];
        
        int offset = 0;
        if (header == HEADER_55 || header == HEADER_551) 
        {
            DataUtil.writeInt(buff, header, offset);
            offset += 4;
        }

        // do values
        
        if (ver == VERSION_55)
        {
            buff[offset++] = (byte) nValues;
        }
        else if (ver == VERSION_551)
        {
            offset += DataUtil.writeValue(buff, nValues, offset);
        }
        
        offset += DataUtil.writeValue(buff, valuesBuffOffset, offset);
        
        if (nValues > 0)
        {
            System.arraycopy(valuesBuff, 0, buff, offset, valuesBuffOffset);
            offset += valuesBuffOffset;
        }
        
        // do msgs
        offset += DataUtil.writeValue(buff, nMsgs, offset);
        for (int i = 0; i < nMsgs; i++)
        {
            if (msgsBuff[i] != null)
            {
                offset += DataUtil.writeValue(buff, msgsBuff[i].length, offset);
                System.arraycopy(msgsBuff[i], 0, buff, offset, msgsBuff[i].length);
                offset += msgsBuff[i].length;   
            }
            else
            {
                if (ver == VERSION_55)
                {
                    // 0 length indicate an empty string (relaxed protocol)
                    offset += DataUtil.writeValue(buff, 0, offset);
                }
                else if (ver == VERSION_551)
                {
                    // -1 length indicate a null string (to make it different from "" string)
                    offset += DataUtil.writeValue(buff, -1, offset);    
                }
            }
        }
        
        // do children
        offset += DataUtil.writeValue(buff, nChildren, offset);
        for (int i = 0; i < nChildren; i++)
        {
            if (childrenBuff[i] != null)
            {
                offset += DataUtil.writeValue(buff, childrenBuff[i].length, offset);
                System.arraycopy(childrenBuff[i], 0, buff, offset, childrenBuff[i].length);
                offset += childrenBuff[i].length;   
            }
            else
            {
                offset += DataUtil.writeValue(buff, 0, offset);
            }
        }
        
        // do binData
        offset += DataUtil.writeValue(buff, binLen, offset);
        if (binData != null)
        {
            System.arraycopy(binData, 0, buff, offset, binData.length);
            offset += binData.length;
        }
        
//      System.out.println(buff.length+" == "+offset);

        return buff;
    }

    /*
    static public TxNode55 fromByteArray(byte[] buff, int offsetInt)
    {
        TxNode node = TxNode.fromByteArray(buff, offsetInt);
        
//      System.out.println("TxNode55::fromByteArray - TxNode.fromByteArray");
        
        TxNode55 node55 = new TxNode55();
        node55.toNode55(node);

//      System.out.println("TxNode55::fromByteArray - clone to TxNode55");

        return node55;
    }
    */
    
    /*static private byte[] buff;
    static private int availableLen;
    static private Stack states = new Stack();
    static private int ver;

    static public void startParsing(int startIndex)
    {
        ver = -1;
        buff = null;
        availableLen = 0;
        states.empty();
        State s = new State();
        s.offset = startIndex;
        states.push(s);
    }*/

    /*static public void parse(byte[] newBuff, INodeListener nodeListener, Node parent, int callDepth)  throws Throwable
    {
        State s = (State) states.peek();

		if (newBuff != null)
		{
			if (buff == null)
			{
				buff = new byte[newBuff.length * 2];
			}
			
			// check if we need to expand buffer 
			if (buff.length < availableLen + newBuff.length)
			{
				byte[] temp = new byte[(availableLen + newBuff.length) * 2];
				System.arraycopy(buff, 0, temp, 0, buff.length);
				buff = temp;
				
				System.out.println("TxNode55, expand: "+buff.length);
			}
			
			// TODO: we can reduce it by offset[0] here
			
			System.arraycopy(newBuff, 0, buff, availableLen, newBuff.length);
			availableLen += newBuff.length;
			
			System.out.println("TxNode55, offset: "+s.offset+", step: "+s.step+", currChild: "+s.currChild+", states.size: "+states.size()+", availableLen: "+availableLen+", callDepth: "+callDepth);
		}
//        if (newBuff != null)
//        {
//            if (buff == null)
//            {
//                buff = new byte[newBuff.length * 2];
//            }
//            
//            // check if we need to expand buffer 
//            if (buff.length < availableLen + newBuff.length)
//            {
//                byte[] temp = new byte[buff.length * 2];
//                System.arraycopy(buff, 0, temp, 0, buff.length);
//                buff = temp;
//                
//                System.out.println("TxNode55, expand: "+buff.length);
//            }
//            
//            // TODO: we can reduce it by offset[0] here
//            
//            System.arraycopy(newBuff, 0, buff, availableLen, newBuff.length);
//            availableLen += newBuff.length;
//            
//            System.out.println("TxNode55, offset: "+s.offset+", step: "+s.step+", currChild: "+s.currChild+", states.size: "+states.size()+", availableLen: "+availableLen+", callDepth: "+callDepth);
//        }

        // System.out.println("TxNode55, offset: "+s.offset+", step: "+s.step+", currChild: "+s.currChild+", states.size: "+states.size()+", availableLen: "+availableLen+", callDepth: "+callDepth);

        int step = s.step;
        int[] offset = new int[1];
        offset[0] =  s.offset;

        if (ver == -1)
        {
            if (offset[0] + 4 <= availableLen)
            {
                int headerCode = DataUtil.readInt(buff, offset[0]);
                
                // System.out.println("TxNode55 - availableLen: "+availableLen+", "+headerCode+", "+INode.HEADER_55);
                
                if (headerCode == HEADER_55)
                {
                    ver = VERSION_55;
                }
                else if (headerCode == HEADER_551)
                {
                    ver = VERSION_551;                    
                }
                else
                {
                    throw new Throwable("unsupported TxNode stream");
                }
                
                offset[0] += 4;
                s.offset = offset[0];
            }
            else
            {
                return;
            }
        }
        
        if (step < 1)
        {
            if (nodeListener != null)
            {
                nodeListener.startNode(s.depth);
            }
            
            s.node = new Node();
            s.node.setVersion(ver);
            s.node.parent = parent;
            s.step++;
        }
        
        Node node = s.node;
        
        try
        {
            if (step < 2)
            {
                if (ver == VERSION_55)
                {
                    if (offset[0] + 1 <= availableLen)
                    {
                        node.nValues = buff[offset[0]++];
                    }
                    else
                    {
                        return;
                    }
                }
                else if (ver == VERSION_551)
                {
                    if (offset[0] + DataUtil.getValueLength(buff, offset[0]) <= availableLen)
                    {
                        node.nValues = (short) DataUtil.readValue(buff, offset);
                    }
                    else
                    {
                        return;
                    }
                }
                s.step++;
                s.offset = offset[0];
            }
            
            if (step < 3)
            {
                if (offset[0] + DataUtil.getValueLength(buff, offset[0]) <= availableLen)
                {
                    node.valuesBuffOffset = (short) DataUtil.readValue(buff, offset);
                }   
                else
                {
                    return;
                }
                s.step++;
                s.offset = offset[0];
            }
            
            if (step < 4)
            {
    //          System.out.println("TxNode55 - nValues: "+node.nValues);
    //          System.out.println("valuesBuffOffset: "+node.valuesBuffOffset);
                
                if (node.nValues > 0)
                {
                    if (offset[0] + node.valuesBuffOffset <= availableLen)
                    {
                        node.valuesBuff = new byte[node.valuesBuffOffset];
                        System.arraycopy(buff, offset[0], node.valuesBuff, 0, node.valuesBuffOffset);
                        offset[0] += node.valuesBuffOffset;
                    }
                    else
                    {
                        return;
                    }
                }
                s.step++;
                s.offset = offset[0];
            }
            
            if (step < 5)
            {
                // do msgs
                if (offset[0] + DataUtil.getValueLength(buff, offset[0]) <= availableLen)
                {
                    node.nMsgs = (short) DataUtil.readValue(buff, offset);
                    node.msgsBuff = new byte[node.nMsgs][];
                }
                else
                {
                    return;
                }
                s.step++;
                s.offset = offset[0];
            }

            if (step < 6)
            {
                
    //          System.out.println("nMsgs: "+nMsgs);
                
                int startMsg = s.currMsg;
                for (int i = startMsg; i < node.nMsgs; i++)
                {
                    int msgLen = -1;
                    if (offset[0] + DataUtil.getValueLength(buff, offset[0]) <= availableLen)
                    {
                        msgLen = (int) DataUtil.readValue(buff, offset);
                    }
                    else
                    {
                        return;
                    }
                    
                    if (msgLen != -1)
                    {
                        if (offset[0] + msgLen <= availableLen)
                        {
                            node.msgsBuff[i] = new byte[msgLen];
                            System.arraycopy(buff, offset[0], node.msgsBuff[i], 0, msgLen);             
                            offset[0] += msgLen;
                        }
                        else
                        {
                            return;
                        }
                    }
                    
                    s.currMsg++;
                    s.offset = offset[0];
                }
                
                s.step++;
                s.offset = offset[0];
            }
            
            if (step < 7)
            {
                // do children
                if (offset[0] + DataUtil.getValueLength(buff, offset[0]) <= availableLen)
                {
                    node.nChildren = (short) DataUtil.readValue(buff, offset);
                    node.childrenBuff = new byte[node.nChildren][];
                }
                else
                {
                    return;
                }
                s.step++;
                s.offset = offset[0];
            }
            
            if (step < 8)
            {
//              System.out.println("nChildren: "+nChildren);
                                
                int startChild = s.currChild;

                for (int i = startChild; i < node.nChildren; i++)
                {
                    int childLen = -1;
                    if (offset[0] + DataUtil.getValueLength(buff, offset[0]) <= availableLen)
                    {
                        childLen = (int) DataUtil.readValue(buff, offset);
                    }
                    else
                    {
                        return;
                    }
                    
                    // System.out.println("child: "+childLen+", i: "+i+", startChild: "+startChild);
                    
                    if (childLen > 0)
                    {
//                      if (offset[0] + childLen > copyOffset)
//                      {
//                          
//                      }
//                      else
//                      {                       
//                          node.childrenBuff[i] = new byte[childLen];
//                          System.arraycopy(buff, offset[0], node.childrenBuff[i], 0, childLen);
//                          offset[0] += childLen;
//                      }
                        
                        
                        State sChild = new State();
                        sChild.depth = s.depth + 1;
                        sChild.offset = offset[0];
                        states.push(sChild);
                        Node.parse(null, nodeListener, node, callDepth + 1);
                        
                        // states.pop();
                        
                        if (sChild.step == 9)
                        {
                            //System.out.println("states.size(): "+states.size());
                            
                            if (node.children == null)
                            {
                                node.children = new Node[node.childrenBuff.length];
                            }
                            node.children[i] = sChild.node;
                            // node.children[i].parent = node;
                        }
                        else
                        {
                            //System.out.println("sChild.step: "+sChild.step);
                            return;
                        }   
                        
                        states.pop();
                        
                        offset[0] = sChild.offset;
                        //System.out.println("after pop states.size(): "+states.size());
                    }
                    
                    s.currChild++;
                    s.offset = offset[0];
                }
                
                s.step++;
                s.offset = offset[0];
            }

            if (step < 9)
            {
                // do binData
                int binLen;
                if (offset[0] + DataUtil.getValueLength(buff, offset[0]) <= availableLen)
                {
                    binLen = (int) DataUtil.readValue(buff, offset);
                }
                else
                {
                    return;
                }
                
//              System.out.println("binLen: "+binLen);

                if (binLen > 0)
                {
                    if (offset[0] + binLen <= availableLen)
                    {
                        node.binData = new byte[binLen];                    
                        System.arraycopy(buff, offset[0], node.binData, 0, binLen);
                        offset[0] += binLen;
                    }
                    else
                    {
                        return;
                    }
                }
                
                if (nodeListener != null)
                {
                    nodeListener.endNode(s.depth, node);
                }
                
                s.step++;
                s.offset = offset[0];               
            }
            
            
            // XXX: this will emulate a call stack when we re-enter in middle
            if (callDepth == 0 && callDepth < states.size() - 1)
            {
                // System.out.println("-- TxNode55, offset: "+s.offset+", step: "+s.step+", currChild: "+s.currChild+", states.size: "+states.size()+", availableLen: "+availableLen+", callDepth: "+callDepth);

                states.pop();
                State prevState = (State) states.peek();
                prevState.currChild++;
                prevState.offset = offset[0];

                Node.parse(null, nodeListener, node, callDepth);
            }
        }
        catch (Exception e)
        {
            Logger.log(Node.class.getName(), e);
        }
        
        // System.out.println("--------- done child: "+node.getStringAt(0));
    }*/
    
    private void fromByteArray55(byte[] buff, int offsetInt, int ver, Node parent)
    {
        setVersion(ver);
        this.parent = parent;
        
        if (buff == null || buff.length == 0) return;

        int[] offset = new int[] { 1 };
        offset[0] = offsetInt;
        if (ver == VERSION_55)
        {
            this.nValues = buff[offset[0]++];
        }
        else if (ver == VERSION_551)
        {
            this.nValues = (short) DataUtil.readValue(buff, offset);
        }
        
        this.valuesBuffOffset = (short) DataUtil.readValue(buff, offset);

//      System.out.println("TxNode55 - nValues: "+node.nValues);
//      System.out.println("valuesBuffOffset: "+node.valuesBuffOffset);
        
        if (this.nValues > 0)
        {
            this.valuesBuff = new byte[this.valuesBuffOffset];
            System.arraycopy(buff, offset[0], this.valuesBuff, 0, this.valuesBuffOffset);
        }
        offset[0] += this.valuesBuffOffset;

        // do msgs
        this.nMsgs = (short) DataUtil.readValue(buff, offset);
        
        this.msgsBuff = new byte[this.nMsgs][];
        
        for (int i = 0; i < this.nMsgs; i++)
        {
            int msgLen = (int) DataUtil.readValue(buff, offset);
            if (msgLen != -1)
            {
                this.msgsBuff[i] = new byte[msgLen];
                System.arraycopy(buff, offset[0], this.msgsBuff[i], 0, msgLen);             
                offset[0] += msgLen;
            }
        }
        
        // do children
        this.nChildren = (short) DataUtil.readValue(buff, offset);
        
//      System.out.println("nChildren: "+nChildren);
        
        this.childrenBuff = new byte[this.nChildren][];

        for (int i = 0; i < this.nChildren; i++)
        {
            int childLen = (int) DataUtil.readValue(buff, offset);
            if (childLen > 0)
            {
                if (childLen + offset[0] > buff.length)
                {
                    throw new IllegalArgumentException(
                        "Bad data buffer for TxNode55: specified length is " + childLen + 
                        ", actually have just " + (buff.length - offset[0]) + " bytes left.");
                }

                this.childrenBuff[i] = new byte[childLen];
                System.arraycopy(buff, offset[0], this.childrenBuff[i], 0, childLen);
                offset[0] += childLen;
            }           
        }
        
        // do binData
        int binLen = (int) DataUtil.readValue(buff, offset);
        
//      System.out.println("binLen: "+binLen);

        if (binLen > 0)
        {
            this.binData = new byte[binLen];
            if (offset[0] < 0 ||
                offset[0] + binLen > buff.length
               ) {
                throw new ArrayIndexOutOfBoundsException("Bad index: " +
                   "buff size=" + buff.length + 
                   ", offset=" + offset[0] + "/" + offsetInt +
                   ", length=" + binLen);
            }
            System.arraycopy(buff, offset[0], this.binData, 0, binLen);
        }
    }
    
    public String toString()
    {
        return toString(0);
    }
    
    private String toString(int level)
    {
        StringBuffer buf = new StringBuffer();
        StringBuffer prefix = new StringBuffer();
        for (int i = 0; i < level; i++) prefix.append("\t");
        
        String offset = prefix.toString();
        
        try
        {

            buf.append(offset + "------------ TxNode55 -----------\n");
            buf.append(offset + "Values:\n");
            for (int i = 0; i < getValuesSize(); i++)
            {
                buf.append(offset + "[" + i + "]=" + getValueAt(i) + "\n");
            }
            
            buf.append(offset + "binary exists ? = " + (binData != null) + "\n");
            if (getStringsSize() > 0)
            {
                buf.append(offset + "------------ messages -----------\n");
                for (int j = 0; j < getStringsSize(); j++)
                    buf.append(offset + getStringAt(j) + "\n");
            }
            if (getChildrenSize() > 0)
            {
                buf.append(offset + "------------ children -----------\n"); 
                for (int j = 0; j < getChildrenSize(); j++)
                {
                    Node child = (Node) getChildAt(j);
                    if (child != null) buf.append(child.toString(level+1) + "\n");
                    else buf.append(offset + "\tNULL CHILD\n");
                }
            }
            buf.append(offset + "------------ end of TxNode55 ------\n");
            
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        return buf.toString();
    }

    public Node getParent()
    {
        return this.parent;
    }
}
