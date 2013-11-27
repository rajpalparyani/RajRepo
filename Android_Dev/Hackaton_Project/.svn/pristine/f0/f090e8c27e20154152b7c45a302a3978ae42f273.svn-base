/**
 * @author lksun
 */
package com.telenav.data.datatypes.address;

import com.telenav.data.dao.serverproxy.AddressDao;

public class FavoriteCatalog extends CommonDBdata
{
    public static final int TYPE_FAVORITE_CATEGORY       = 195;
    public static final byte CATEGORY_DELETED_WITHOUT_CHILDREN      = 4;
    public static final byte CATEGORY_DELETED_WITH_CHILDREN         = 7;

    //category type
    public static final byte TYPE_ROOT_CATEGORY         = 0;    //root category
    public static final byte TYPE_RESERVERD_CATEGORY    = 1;    //such as Received Addresse, can not delete
    public static final byte TYPE_CUSTOM_CATEGROY       = 2;    //user customized category

    public static final byte delMask = 0x0003;                  

    protected long   createTime;
    protected long   updateTime;

    protected byte catType = TYPE_CUSTOM_CATEGROY;
    protected String name = "";

    public FavoriteCatalog()
    {
        super();
    }
    
    public long getCreateTime()
    {
        return this.createTime;
    }
    
    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
    }
    
    public long getUpdateTime()
    {
        return this.updateTime;
    }
    
    public void setUpdateTime(long updateTime)
    {
        this.updateTime = updateTime;
    }

    public byte getCatType() 
    {
        return this.catType;
    }

    public long getId() 
    {
        return this.id;
    }

    public String getName() 
    {
        return this.name;
    }

    public byte getStatus() 
    {
        return this.status;
    }

    public void setCatType(byte catType) 
    {
        this.catType = catType;

    }

    public void setId(long id) 
    {
        this.id = id;

    }

    public void setName(String name) 
    {
        this.name = name;
    }


    public boolean isDeleteWChildren()
    {
        if((status & delMask) == CommonDBdata.DELETED  && (status | delMask) == CATEGORY_DELETED_WITH_CHILDREN)
        {
            return true;
        }
        return false;
    }
    
    
    public boolean isReceivedCatalog()
    {
        boolean isReceivedCatalog=AddressDao.RECEIVED_ADDRESSES_CATEGORY.equalsIgnoreCase(this.name);
        return isReceivedCatalog;
    }
    

}
