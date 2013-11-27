/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AddFavoriteModel.java
 *
 */
package com.telenav.module.ac.favorite.editcategory;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.FavoriteCatalog;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.module.ac.SyncStopsExecutor;
import com.telenav.module.sync.IMigrationListener;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.res.IStringEditCategory;
import com.telenav.res.ResourceManager;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-10-28
 */
class EditCategoryModel extends AbstractCommonNetworkModel implements IEditCategoryConstants, IMigrationListener
{

    public EditCategoryModel()
    {
    }

    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_INIT:
            {
                if(get(KEY_O_CATEGORY) != null)
                {
                    this.postModelEvent(EVENT_MODEL_LAUNCH_EDIT_CATEGORY);
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_LAUNCH_NEW_CATEGORY);
                }
                break;
            }
            case ACTION_DELETE_CATEGORY:
            {
                FavoriteCatalog category = (FavoriteCatalog) get(KEY_O_CATEGORY);
//                category.setCatType(FavoriteCatalog.CATEGORY_DELETED_WITHOUT_CHILDREN);
                DaoManager.getInstance().getAddressDao().removefavoriteCatalog(category, true);
                DaoManager.getInstance().getAddressDao().store();
                break;
            }
            case ACTION_CHECK_CATEGORY:
            {
                String newName = getString(KEY_S_RENAMED_CATEGORY);
                FavoriteCatalog category = (FavoriteCatalog) get(KEY_O_CATEGORY);
                String oldName = category.getName();
                Vector categories = DaoManager.getInstance().getAddressDao().getFavoriteCatalog();
                if (oldName.equalsIgnoreCase(newName.trim()))
                {
                    put(KEY_S_RENAMED_CATEGORY, newName);
                    postModelEvent(EVENT_MODEL_RENAME_CATEGORY);
                }
                else if("".equals(newName.trim()))
                {
                    put(KEY_S_COMMON_MESSAGE, ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringEditCategory.RES_MESSAGEBOX_ALERT_EMPTY_NAME, IStringEditCategory.FAMILY_EDIT_CATEGORY));
                    postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
                }
                else
                {
                    boolean isSame = false;
                    for (int i = 0; i < categories.size(); i++)
                    {
                        FavoriteCatalog catalog = (FavoriteCatalog) categories.elementAt(i);
                        String name = catalog.getName();
                        if (name.equalsIgnoreCase(newName.trim()))
                        {
                            isSame = true;
                            put(KEY_S_COMMON_MESSAGE, ResourceManager.getInstance().getCurrentBundle()
                                .getString(IStringEditCategory.RES_LABEL_MESSAGE_BOX, IStringEditCategory.FAMILY_EDIT_CATEGORY));
                            postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
                            break;
                        }
                    }
                    if (!isSame)
                    {
                        put(KEY_S_RENAMED_CATEGORY, newName);
                        postModelEvent(EVENT_MODEL_RENAME_CATEGORY);
                    }
                }
                break;
            }
            case ACTION_RENAME_CATEGORY:
            {
                FavoriteCatalog category = (FavoriteCatalog) get(KEY_O_CATEGORY);
                String newName = getString(KEY_S_RENAMED_CATEGORY);
                DaoManager.getInstance().getAddressDao().renamefavoriteCatalog(category, newName);
                DaoManager.getInstance().getAddressDao().store();
                break;
            }
            case ACTION_CHECK_CATEGORY_EXIST:
            {
                String newName = getString(KEY_S_NEW_CATEGROY_NAME);
                if ("".equalsIgnoreCase(newName.trim()))
                {
                    this.put(
                        KEY_S_COMMON_MESSAGE,
                        ResourceManager.getInstance().getCurrentBundle()
                                .getString(IStringEditCategory.RES_MESSAGEBOX_ALERT_EMPTY_NAME, IStringEditCategory.FAMILY_EDIT_CATEGORY));
                    this.postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
                }
                else if (DaoManager.getInstance().getAddressDao().isExistInFavoriteCatalog(newName))
                {
                    this.put(
                        KEY_S_COMMON_MESSAGE,
                        ResourceManager.getInstance().getCurrentBundle()
                                .getString(IStringEditCategory.RES_LABEL_MESSAGE_BOX, IStringEditCategory.FAMILY_EDIT_CATEGORY));
                    this.postModelEvent(EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
                }
                else
                {
                    this.postModelEvent(EVENT_MODEL_ADD_CATEGORY);
                }
                break;
            }
            case ACTION_ADD_CATEGORY:
            {
                FavoriteCatalog newCat = new FavoriteCatalog();
                newCat.setCreateTime(System.currentTimeMillis());
                newCat.setName(getString(KEY_S_NEW_CATEGROY_NAME));
                newCat.setCatType(FavoriteCatalog.TYPE_CUSTOM_CATEGROY);
                DaoManager.getInstance().getAddressDao().addFavoriteCatalog(newCat);
                DaoManager.getInstance().getAddressDao().store();
                syncStops();
                break;
            }
        }
    }
    
    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        // TODO Auto-generated method stub
        
    }

    public void onMigrationFinished()
    {
        
    }
    
    protected void releaseDelegate()
    {
        SyncStopsExecutor.getInstance().removeListener(this);
    }
}
