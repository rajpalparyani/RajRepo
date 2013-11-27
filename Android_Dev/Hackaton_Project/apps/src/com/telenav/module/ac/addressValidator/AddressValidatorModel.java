/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RecentModel.java
 *
 */
package com.telenav.module.ac.addressValidator;

import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.impl.IValidateAddressProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AddressMisLog;
import com.telenav.logger.Logger;
import com.telenav.mvc.AbstractCommonNetworkModel;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringSetHome;
import com.telenav.res.ResourceManager;

/**
 * @author wzhu (wzhu@telenav.cn)
 * @date 2010-12-26
 */
class AddressValidatorModel extends AbstractCommonNetworkModel implements IAddressValidatorConstants
{

    protected void doActionDelegate(int actionId)
    {
        switch (actionId)
        {
            case ACTION_VALIDATE_HOME:
            {
                String transactionId = this.getString(KEY_S_TRANSACTION_ID);
                if(transactionId == null || transactionId.length() == 0)
                {
                    transactionId = System.currentTimeMillis() + "";
                    this.put(KEY_S_TRANSACTION_ID, transactionId);
                }
                doValidate(transactionId);
                break;
            }
            case ACTION_CANCEL_VALIDATING:
            {
                CommManager.getInstance().getComm().cancelJob(IServerProxyConstants.ACT_VALIDATE_ADDRESS);
                break;
            }
        }
    }

    private void doValidate(String searchUid)
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider)get(KEY_O_USER_PROFILE_PROVIDER);
        IValidateAddressProxy proxy = ServerProxyFactory.getInstance().createValidateAddressProxy(null,
            
        CommManager.getInstance().getComm(), this, userProfileProvider);
        String country = getString(KEY_S_ADDRESS_VALIDATE_COUNTRY);
        if(country != null)
        {
            Stop stop = new Stop();
            stop.setCountry(country);
            proxy.validateAddress(getString(KEY_S_ADDRESS_LINE), getString(KEY_S_SECOND_LINE), searchUid, stop, IValidateAddressProxy.TYPE_INPUTTYPE_ANY);
        }
        else
        {
            proxy.validateAddress(getString(KEY_S_ADDRESS_LINE), getString(KEY_S_SECOND_LINE), searchUid);
        }
    }
    
    public void transactionError(AbstractServerProxy proxy)
    {
        if(proxy.getRequestAction().equalsIgnoreCase(IServerProxyConstants.ACT_VALIDATE_ADDRESS))
        {
            String errorMessage = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_NO_CELL_COVERAGE, IStringCommon.FAMILY_COMMON);
            postErrorMessage(errorMessage);            
        }
        else
        {
            super.transactionError(proxy);
        }
    }

    protected void transactionFinishedDelegate(AbstractServerProxy proxy, String jobId)
    {
        if (proxy instanceof IValidateAddressProxy)
        {
            Vector result = ((IValidateAddressProxy) proxy).getSimilarAddresses();
            if (result == null || result.size() == 0)
            {
                String firstLine = getString(KEY_S_ADDRESS_LINE);
                String secondLine = getString(KEY_S_SECOND_LINE);
                String addressMessage = getString(KEY_S_MESSAGE_ADDRESS);
                StringBuffer errorMessage = new StringBuffer();
                
                if(firstLine != null && firstLine.trim().length() > 0)
                {
                    String displayStr = firstLine.replaceAll(">=", ">");
                    displayStr = displayStr.replaceAll(";", ", ");
                    errorMessage.append(firstLine).append(" ");
                }
                if(secondLine != null && secondLine.trim().length() > 0)
                {
                    errorMessage.append(secondLine).append(" ");
                }
                
                errorMessage.append(ResourceManager.getInstance().getCurrentBundle().getString(IStringSetHome.RES_LABEL_INVALID_ADDRESS,
                            IStringSetHome.FAMILY_SET_HOME));
                
                put(KEY_S_ERROR_MESSAGE, errorMessage.toString());
                postModelEvent(EVENT_MODEL_POST_ERROR);
            }
            else
            {
                String transactionId = this.getString(KEY_S_TRANSACTION_ID);
                if(transactionId == null)
                {
                    transactionId = "";
                }

                MisLogManager misLogManager = MisLogManager.getInstance();

                if (result.size() == 1)
                {
                    Address address = (Address) result.elementAt(0);
                    if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_TWOBOX_ADDRESS_ONE_RESULT))
                    {
                        AddressMisLog log = misLogManager.getFactory().createTwoBoxAddressOneResultMisLog();
                        if(address.getStop() != null)
                        {
                            log.setzEndLat(address.getStop().getLat());
                            log.setzEndLon(address.getStop().getLon());
                        }
                        log.setzDisplayString(ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), false));
                        log.setTimestamp(System.currentTimeMillis());
                        log.setSearchUid(transactionId);
                        log.setPageNumber(0);
                        log.setPageIndex(0);
                        Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                        { log });
                    }
                    
                    String scoreStr = DaoManager.getInstance().getServerDrivenParamsDao().getValue(ServerDrivenParamsDao.ADDRESS_SCORE_THRESHOLD);
                    int score = 0;
                    if (scoreStr != null && scoreStr.trim().length() > 0)
                    {
                        try
                        {
                            score = (int) (Float.parseFloat(scoreStr) * 100);
                        }
                        catch(NumberFormatException e)
                        {
                            
                        }
                    }
                    if((address.getStop() != null && (address.getStop().getScore() >= score)))
                    {
                        put(KEY_O_SELECTED_ADDRESS, address);
                        postModelEvent(EVENT_MODEL_RETURN_ADDRESS);
                    }
                    else
                    {
                        put(KEY_V_ALTERNATIVE_ADDRESSES, result);
                        postModelEvent(EVENT_MODEL_CHOOSE_ADDRESS);
                    }
                    
                    if (address.getStop() != null)
                        Logger.log(Logger.INFO, this.getClass().getName(), "address validate -- score " + address.getStop().getScore() + " sdp score " + scoreStr);
             }
                else
                {
                    if(result.size() > 1)
                    {
                        if (misLogManager.getFilter().isTypeEnable(IMisLogConstants.TYPE_TWOBOX_ADDRESS_IMPRESSION))
                        {
                            AddressMisLog log = misLogManager.getFactory().createTwoBoxAddressImpressionMisLog();
                            log.setTimestamp(System.currentTimeMillis());
                            log.setResultNumber(result.size());
                            log.setSearchUid(transactionId);
                            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                            { log });
                        }
                    }
                    put(KEY_V_ALTERNATIVE_ADDRESSES, result);
                    postModelEvent(EVENT_MODEL_CHOOSE_ADDRESS);
                }
            }
        }
    }
}
