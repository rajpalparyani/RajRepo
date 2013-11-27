/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * J2seFrogTab.java
 *
 */
package com.telenav.ui.frogui.widget.j2se;

import com.telenav.tnui.core.INativeUiContainer;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.j2se.J2seLinearLayout;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 5, 2010
 */
public class J2seFrogTab extends J2seLinearLayout implements INativeUiContainer
{
    /**
     * 
     */
    private static final long serialVersionUID = 2154299994453488802L;

    public J2seFrogTab(TnLinearContainer tnContainer)
    {
        super(tnContainer);
    }

   /* public View focusSearch(View focused, int direction)
    {
        View nextFocus = FocusFinder.getInstance().findNextFocus(this, focused, direction);
        if (nextFocus == null && this.tnContainer.getParent() instanceof FrogTabContainer)
        {
            FrogTabContainer tabContainer = (FrogTabContainer) this.tnContainer.getParent();
            int currentTabIndex = tabContainer.getCurrentTabIndex();
            switch (direction)
            {
                case View.FOCUS_UP:
                {
                    ((AbstractTnContainer) tabContainer.get(FrogTabContainer.INDEX_TAB_HEADER)).get(tabContainer.getCurrentTabIndex())
                            .requestFocus();
                    return null;
                }
                case View.FOCUS_RIGHT:
                {
                    if (currentTabIndex < tabContainer.getTabCount() - 1)
                    {
                        int newTabIndex = currentTabIndex + 1;
                        FrogTab tab = tabContainer.getTabAt(newTabIndex);
                        while (tab != null && (!tab.isVisible()))
                        {
                            newTabIndex++;
                            tab = tabContainer.getTabAt(newTabIndex);
                        }
                        if (newTabIndex >= tabContainer.getTabCount())
                        {
                            newTabIndex = 0;
                        }
                        tabContainer.setCurrentTabIndex(newTabIndex);
                        ((AbstractTnContainer) tabContainer.get(FrogTabContainer.INDEX_TAB_HEADER)).get(tabContainer.getCurrentTabIndex())
                                .requestFocus();
                        tabContainer.show();
                    }
                    break;
                }
                case View.FOCUS_LEFT:
                {
                    if (currentTabIndex > 0)
                    {
                        currentTabIndex--;
                        FrogTab tab = tabContainer.getTabAt(currentTabIndex);
                        if (!tab.isVisible())
                        {
                            currentTabIndex--;
                        }
                        if (currentTabIndex < 0)
                        {
                            currentTabIndex = tabContainer.getTabCount() - 1;
                        }
                        tabContainer.setCurrentTabIndex(currentTabIndex);
                        ((AbstractTnContainer) tabContainer.get(FrogTabContainer.INDEX_TAB_HEADER)).get(tabContainer.getCurrentTabIndex())
                                .requestFocus();
                        tabContainer.show();
                    }
                    break;
                }
            }
        }
        return super.focusSearch(focused, direction);
    }*/
}
