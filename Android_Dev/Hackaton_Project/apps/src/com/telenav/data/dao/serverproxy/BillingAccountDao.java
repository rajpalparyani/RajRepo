/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * BillingAccountDao.java
 *
 */
package com.telenav.data.dao.serverproxy;

import java.text.SimpleDateFormat;

import com.telenav.data.dao.AbstractDao;
import com.telenav.data.datatypes.primitive.StringMap;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.logger.Logger;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 19, 2010
 */
public class BillingAccountDao extends AbstractDao
{
    //account type
    public static final String ACCOUNT_TYPE_NAV = "NAV";
    public static final String ACCOUNT_TYPE_MAP = "MAP";
    
    public static final String PTN_TYPE_ENCRYPTED = "0";
    public static final String PTN_TYPE_SIM = "1";
    public static final String PTN_TYPE_TYPEIN = "2";
    
    //account status
    public static final int ACCOUNT_STATUS_UNKNOWN = -1;
    public static final int ACCOUNT_STATUS_NORMAL = 10;//Normal account
    public static final int ACCOUNT_STATUS_EXPIRED = 11;//Expired account
    public static final int ACCOUNT_STATUS_NO_FOUND = 12;//Type not found
    public static final int ACCOUNT_STATUS_TRIAL_EXPIRED = 13;//Trial expired account
    public static final int ACCOUNT_STATUS_NEW = 14;//New Account
    public static final int ACCOUNT_STATUS_PIN_MISMATCH = 15;//Pin Mismatch
    public static final int ACCOUNT_STATUS_EMAIL_REGISTERED = 20;//Existed email
    public static final int ACCOUNT_STATUS_PASSWORD_MISMATCH = 22;//Password incorrect
    public static final int ACCOUNT_STATUS_EMAIL_MISMATCH = 23;//EMail Mismatch
    public static final int ACCOUNT_STATUS_PREM_EXPIRED  = 26;//Premium account expire
    
    public static final int ACCOUNT_ENTRY_TYPE_MAYBE_LATER = 1;
    public static final int ACCOUNT_ENTRY_TYPE_SIGN_UP = 2;
    public static final int ACCOUNT_ENTRY_TYPE_SIGN_IN = 3;
    public static final int ACCOUNT_ENTRY_TYPE_UNKNOWN = -1;
    
    private static final int ACCOUNT_TYPE_INDEX = 20001;
    private static final int ACCOUNT_STATUS_INDEX = 20002;
    private static final int ACCOUNT_SOC_TYPE = 20003;
    private static final int ACCOUNT_PTN = 20004;
    private static final int ACCOUNT_SIM_ID = 20005;
    
    //In Titan, TOS is not a mandatory step.
    //private static final int ACCOUNT_TOC = 20006;
    private static final int ACCOUNT_UPSELL_DISPLAY_TIMES = 20008;
    private static final int HAS_HACK_PTN = 20009;
    private static final int ACCOUNT_SUPPORTED_REGION= 20010;
    private static final int ACCOUNT_PTN_CARRIER = 20011;
    private static final int ACCOUNT_PTN_CARRIER_STATUS = 20012;
    private static final int ACCOUNT_ENTRY_INDEX = 20013;
    private static final int PURCHASE_ORDER_NAME_INDEX = 20014;
    private static final int PURCHASE_OFFER_PRIZE_INDEX = 20015;
    private static final int PURCHASE_OFFER_CURRENCY_INDEX = 20016;
    private static final int PURCHASE_OFFER_EXPIRE_DATE_INDEX = 20017;
    
    //Cancel Subscription
    private static final int SUBSCRIPTION_OFFERCODE = 20018;
    private static final int SUBSCRIPTION_CANCELLABLE = 20019; 
    private static final int ACCOUNT_NEED_PURCHASE = 20020;
    private static final int NEED_SHOW_UPSELL = 20021;
    
    
    private static final int ACCOUNT_DEFAULT_STATUS = -10000; 
    
    private static final int PURCHASE_CODE_UNKNOWN = -10000; 
    private static final int PURCHASE_CODE_UNPURCHASE = 0; 
    private static final int PURCHASE_CODE_PURCHASED = 1; 
    
    //carrier status
    public static final int CARRIER_STATUS_FRESH = -1;
    public static final int CARRIER_STATUS_UPDATED = 1; //carrier != null && accountStaus == expired

    
    protected String accountType = null;
    protected int accountStatus = ACCOUNT_DEFAULT_STATUS;
    protected int purchaseCode = PURCHASE_CODE_UNKNOWN;
    
    private TnStore accountDataWrapper;
    
    private static class SingletonHolder 
    {
        public static final BillingAccountDao INSTANCE = new BillingAccountDao();
    }
    
    private BillingAccountDao()
    {
    	//use database because it's small and critical
    	accountDataWrapper = TnPersistentManager.getInstance().createStore(TnPersistentManager.DATABASE, "billing_account");
    	accountDataWrapper.load();
    }

    public static BillingAccountDao getInstance() 
    {
        return SingletonHolder.INSTANCE;
    }
    
    /*
     * this method is for unit test only!
     */
    void setStore(TnStore startupStore)
    {
    	this.accountDataWrapper = startupStore;
    }
    
    public String getSocType()
    {
        if (this.accountDataWrapper != null)
        {
            byte[] socTypeData = this.accountDataWrapper.get(ACCOUNT_SOC_TYPE);
            if (socTypeData != null)
            {
                return new String(socTypeData);
            }
        }

        return null;
    }
    
    public String getAccountType()
    {
        if (accountType != null && accountType.trim().length() > 0)
        {
            return accountType;
        }
        
        if (this.accountDataWrapper != null)
        {
            byte[] accountTypeData = this.accountDataWrapper.get(ACCOUNT_TYPE_INDEX);
            if (accountTypeData != null)
            {
                accountType = new String(accountTypeData);
            }
        }

        return accountType;
    }
    
    public void setSocType(String socType)
    {
        if(socType == null)
            return;
        
        if(socType.equals(this.getSocType()))
        {
            return;
        }
        
        if(this.accountDataWrapper != null)
        {
            this.accountDataWrapper.put(ACCOUNT_SOC_TYPE, socType.getBytes());
        }
    }
    
    public void setAccountType(String accountType)
    {
        if(accountType == null)
            return;
        
        if(accountType.equals(this.accountType))
        {
            return;
        }
        
        this.accountType = accountType;
        
        if(this.accountDataWrapper != null)
        {
            this.accountDataWrapper.put(ACCOUNT_TYPE_INDEX, accountType.getBytes());
        }
    }
    
    public String getPtn()
    {
        if (this.accountDataWrapper != null)
        {
            byte[] accountTypeData = this.accountDataWrapper.get(ACCOUNT_PTN);
            if (accountTypeData != null)
            {
                String s = new String(accountTypeData);
                
                return s;
            }
        }
        return "";
    }
    
    public void setPtn(String ptn)
    {
        if(ptn == null)
            return;
        
        if(ptn.equals(this.getPtn()))
        {
            return;
        }
        
        if(this.accountDataWrapper != null)
        {
            this.accountDataWrapper.put(ACCOUNT_PTN, ptn.getBytes());
        }
    }
    
    public String getCarrier()
    {
        if(this.accountDataWrapper != null)
        {
            byte[] carrierData = this.accountDataWrapper.get(ACCOUNT_PTN_CARRIER);
            if(carrierData != null)
            {
                String carrier = new String(carrierData);
                
                return carrier;
            }
        }
        return "";
    }
    
    public void setCarrier(String carrier)
    {
        if(carrier == null)
        {
            return;
        }
        if(carrier.equals(this.getCarrier()))
        {
            return;
        }
        if(this.accountDataWrapper != null)
        {
            this.accountDataWrapper.put(ACCOUNT_PTN_CARRIER, carrier.getBytes());
        }
        
        Logger.log(Logger.INFO, this.getClass().getName(), "Dim - setCarrier - carrier:" + carrier);
    }
    
    public int getCarrierStatus()
    {
        if(this.accountDataWrapper != null)
        {
            byte[] carrierStatusData = this.accountDataWrapper.get(ACCOUNT_PTN_CARRIER_STATUS);
            if(carrierStatusData != null)
            {
                return Integer.parseInt(new String(carrierStatusData));
            }
        }
        
        return CARRIER_STATUS_FRESH;
    }
    
    public void setCarrierStatus(int carrierStatus)
    {
        if(carrierStatus == this.getAccountStatus())
        {
            return;
        }
        if(this.accountDataWrapper != null)
        {
            this.accountDataWrapper.put(ACCOUNT_PTN_CARRIER_STATUS, (carrierStatus + "").getBytes());
        }
    }
    
    public void setSimCardId(String simCardId)
    {
        if(simCardId == null)
            return;
        
        if(simCardId.equals(this.getSimCardId()))
        {
            return;
        }
        
        if(this.accountDataWrapper != null)
        {
            this.accountDataWrapper.put(ACCOUNT_SIM_ID, simCardId.getBytes());
        }
    }
    
    public String getSimCardId()
    {
        if (this.accountDataWrapper != null)
        {
            byte[] simCardIdData = this.accountDataWrapper.get(ACCOUNT_SIM_ID);
            if (simCardIdData != null)
            {
                return new String(simCardIdData);
            }
        }

        return "";
    }
    
    public synchronized void store()
    {
		this.accountDataWrapper.save();
	}
    
    public synchronized void clear()
    {
        accountType = null;
        accountStatus = ACCOUNT_DEFAULT_STATUS;
        purchaseCode = PURCHASE_CODE_UNKNOWN;
        this.accountDataWrapper.clear();
    }
    
    public int getAccountStatus()
    {
        if (accountStatus != ACCOUNT_DEFAULT_STATUS)
        {
            return accountStatus;
        }
        
        if (this.accountDataWrapper != null)
        {
            byte[] accountStatusData = this.accountDataWrapper.get(ACCOUNT_STATUS_INDEX);
            if (accountStatusData != null)
            {
                accountStatus = Integer.parseInt(new String(accountStatusData));
                return accountStatus;
            }
        }

        return ACCOUNT_STATUS_UNKNOWN;
    }
    
    public void setAccountStatus(int accountStatus)
    {
        if (accountStatus == this.accountStatus)
            return;
        
        if (ACCOUNT_STATUS_NORMAL == accountStatus)
        {
            this.setUpsellDisplayTimes(0);
        }
        
        if(this.accountDataWrapper != null)
        {
            this.accountDataWrapper.put(ACCOUNT_STATUS_INDEX, (accountStatus + "").getBytes());
            this.accountStatus = accountStatus;
        }
    }
    
    public int getUpsellDisplayTimes()
    {
        if (this.accountDataWrapper != null)
        {
            byte[] upsellDisplayTimeData = this.accountDataWrapper.get(ACCOUNT_UPSELL_DISPLAY_TIMES);
            if (upsellDisplayTimeData != null)
            {
            	Logger.log(Logger.INFO, this.getClass().getName(), "BillingAccountDao -- getUpsellDisplayTimes ACCOUNT_UPSELL_DISPLAY_TIMES: " + Integer.parseInt(new String(upsellDisplayTimeData))); 
                return Integer.parseInt(new String(upsellDisplayTimeData));
            }
        }
        
        return 0;
    }
    
    public void setUpsellDisplayTimes(int displayTimes)
    {
        if (displayTimes == this.getUpsellDisplayTimes())
            return;
        
        if(this.accountDataWrapper != null)
        {
        	Logger.log(Logger.INFO, this.getClass().getName(), "BillingAccountDao -- setUpsellDisplayTimes ACCOUNT_UPSELL_DISPLAY_TIMES: " + displayTimes); 
            this.accountDataWrapper.put(ACCOUNT_UPSELL_DISPLAY_TIMES, (displayTimes + "").getBytes());
        }
    }
    
    public boolean isLogin()
    {
        int accountStatus = getAccountStatus();
        boolean isLogin = accountStatus == ACCOUNT_STATUS_NORMAL
                || accountStatus == ACCOUNT_STATUS_NEW 
                || accountStatus == ACCOUNT_STATUS_PREM_EXPIRED;
        
        return isLogin;
    }
    
    public boolean isNeedPurchase()
    {
        return getPurchaseCode() != PURCHASE_CODE_PURCHASED;
    }
    
    protected int getPurchaseCode()
    {
        if(Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "getPurchaseCode -- purchaseCode: " + purchaseCode);
        }
        
        if (purchaseCode != PURCHASE_CODE_UNKNOWN)
        {
            return purchaseCode;
        }

        if (this.accountDataWrapper != null)
        {
            byte[] purchaseStatusData = this.accountDataWrapper.get(ACCOUNT_NEED_PURCHASE);
            if (purchaseStatusData == null)
            {
                if(Logger.DEBUG)
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "getPurchaseCode -- purchaseStatusData is null");
                }
                purchaseCode = PURCHASE_CODE_UNKNOWN;
            }
            else
            {
                purchaseCode = Integer.parseInt(new String(purchaseStatusData));
            }
        }

        return purchaseCode;
    }
    
    public void setNeedPurchase(boolean isNeedPurchase)
    {
        if(Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "setNeedPurchase -- isNeedPurchase: " + isNeedPurchase);
            Logger.log(Logger.INFO, this.getClass().getName(), "setNeedPurchase -- This.isNeedPurchase: " +  this.isNeedPurchase());
        }
        
        if (isNeedPurchase == this.isNeedPurchase())
        {
            return;
        }
        
        if(Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "setNeedPurchase -- purchaseCode: " + purchaseCode);
        }
        
        if (purchaseCode == PURCHASE_CODE_PURCHASED && isNeedPurchase)
        {
            setNeedShowUpsell(true);
        }
        
        if (isNeedPurchase)
        {
            purchaseCode = PURCHASE_CODE_UNPURCHASE;
        }
        else
        {
            purchaseCode = PURCHASE_CODE_PURCHASED;
        }
        
    	if(this.accountDataWrapper != null)
        {
            this.accountDataWrapper.put(ACCOUNT_NEED_PURCHASE, (purchaseCode + "").getBytes());
        }
    }
    
    public boolean isNeedShowUpsell()
    {
         if (this.accountDataWrapper != null)
         {
             byte[] isShowUpsell = this.accountDataWrapper.get(NEED_SHOW_UPSELL);
             if (isShowUpsell != null)
             {
                 return Boolean.valueOf(new String(isShowUpsell));
             }
         }

         return false;
    }
    
    public void setNeedShowUpsell(boolean isShowUpsell)
    {
        if(Logger.DEBUG)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "setNeedShowUpsell -- isShowUpsell: " + isShowUpsell);
            Logger.log(Logger.INFO, this.getClass().getName(), "setNeedPurchase -- this.isNeedShowUpsell(): " + this.isNeedShowUpsell());
        }
        
        if (isShowUpsell == this.isNeedShowUpsell())
            return;
        
        if(this.accountDataWrapper != null)
        {
            this.accountDataWrapper.put(NEED_SHOW_UPSELL , (isShowUpsell + "").getBytes());
        }
    }
    
    public boolean getSubscriptionCancellable()
    {
         if (this.accountDataWrapper != null)
         {
             byte[] cancellableData = this.accountDataWrapper.get(SUBSCRIPTION_CANCELLABLE );
             if (cancellableData != null)
             {
                 return Boolean.valueOf(new String(cancellableData));
             }
         }

         return false;
    }
    
    public void setSubscriptionCancellable(boolean isSubscriptionCancellable)
    {
        if (isSubscriptionCancellable == this.getSubscriptionCancellable())
            return;
        
        if(this.accountDataWrapper != null)
        {
            this.accountDataWrapper.put(SUBSCRIPTION_CANCELLABLE , (isSubscriptionCancellable + "").getBytes());
        }
    }
    /**
     * judge whether use hard code ptn instead of read local.
     */
    public boolean hasHackPtn()
    {
         if (this.accountDataWrapper != null)
         {
             byte[] hasHackPtn = this.accountDataWrapper.get(HAS_HACK_PTN);
             if (hasHackPtn != null)
             {
                 return Boolean.valueOf(new String(hasHackPtn));
             }
         }

         return false;
    }
    
    /**
     * It's used for set hard code ptn instead of read local in secrete screen.
     */
    public void setHasHackPtn(boolean hasHackPtn)
    {
        if(this.accountDataWrapper != null)
        {
            this.accountDataWrapper.put(HAS_HACK_PTN, (hasHackPtn + "").getBytes());
        }
    }
    
    public void setSupportedRegion(StringMap supportedRegion)
    {
        if(this.accountDataWrapper != null)
        {
            accountDataWrapper.put(ACCOUNT_SUPPORTED_REGION,  SerializableManager.getInstance().getPrimitiveSerializable().toBytes(supportedRegion));
        }
    }
    
    public StringMap getSupportedRegion()
    {
        StringMap stringMap = null;
        byte[] byteData = accountDataWrapper.get(ACCOUNT_SUPPORTED_REGION);
        if (byteData != null)
        {
            stringMap = SerializableManager.getInstance().getPrimitiveSerializable().createStringMap(byteData);
        }
        return stringMap;
    }
    
    /**
     * I add this because I don't like to use a key to pass the entry type...
     * @return
     */
    public int getLoginType()
    {
        int accountEntryType = ACCOUNT_ENTRY_TYPE_UNKNOWN;
        byte[] byteData = accountDataWrapper.get(ACCOUNT_ENTRY_INDEX);
        if(byteData != null)
        {
            accountEntryType = Integer.parseInt(new String(byteData));
        }
        
        return accountEntryType;
    }
    
    /**
     * I add this because I don't like to use a key to pass the entry type...
     * @param accountEntryType
     */
    public void setLoginType(int accountEntryType)
    {
        if(this.accountDataWrapper != null)
        {
            accountDataWrapper.put(ACCOUNT_ENTRY_INDEX, (accountEntryType + "").getBytes());
        }
    }
 
    public String getOfferCode()
    {
        String offerCode =        null ;
        byte[] byteData = accountDataWrapper.get(SUBSCRIPTION_OFFERCODE);
        if(byteData != null)
        {
            offerCode = new String(byteData);
        }
        
        return offerCode;
    }
    

    public void setOfferCode(String offerCode)
    {
        
        if(this.accountDataWrapper != null)
        {
            if(offerCode !=null)
			{
            	accountDataWrapper.put(SUBSCRIPTION_OFFERCODE, offerCode.getBytes());
			}
        }
    }

    /**
     * I add this because my profile need purchase order name to display...
     * @return
     */
    public String getPurchaseOrderName()
    {
        String purchaseOrderName =        null ;
        byte[] byteData = accountDataWrapper.get(PURCHASE_ORDER_NAME_INDEX);
        if(byteData != null)
        {
            purchaseOrderName = new String(byteData);
        }
        
        return purchaseOrderName;
    }
    
    /**
     * I add this because my profile need purchase order name to display...
     * @return
     */
    public void setPurchaseOrderName(String purchaseOrderName)
    {
        
        if(this.accountDataWrapper != null)
        {
            if(purchaseOrderName !=null)
            accountDataWrapper.put(PURCHASE_ORDER_NAME_INDEX, purchaseOrderName.getBytes());
        }
    }
    
    /**
     * I add this because my profile need offer  prize to display...
     * @return
     */
    public String getOfferPrize()
    {
        String offerPrize =        null ;
        byte[] byteData = accountDataWrapper.get(PURCHASE_OFFER_PRIZE_INDEX);
        if(byteData != null)
        {
            offerPrize = new String(byteData);
        }
        
        return offerPrize;
    }
    
    /**
     * I add this because my profile need offer prize  to display...
     * @return
     */
    public void setOfferPrize(String offerPrize)
    {
        
        if(this.accountDataWrapper != null)
        {
            if(offerPrize !=null)
            accountDataWrapper.put(PURCHASE_OFFER_PRIZE_INDEX, offerPrize.getBytes());
        }
    }
    
    /**
     * I add this because my profile need offer currency to display...
     * 
     * @return
     */
    public String getOfferCurrency()
    {
        String offerPrize = null;
        byte[] byteData = accountDataWrapper.get(PURCHASE_OFFER_CURRENCY_INDEX);
        if (byteData != null)
        {
            offerPrize = new String(byteData);
        }

        return offerPrize;
    }

    /**
     * I add this because my profile need offer currency to display...
     * 
     * @return
     */
    public void setOfferCurrrency(String offerCurrency)
    {

        if (this.accountDataWrapper != null)
        {
            if (offerCurrency != null)
                accountDataWrapper.put(PURCHASE_OFFER_CURRENCY_INDEX,
                    offerCurrency.getBytes());
        }
    }
    
    /**
     * I add this because my profile need offer currency to display...
     * 
     * @return
     */
    public String getOfferExpireDate()
    {
        String offerPrize = null;
        byte[] byteData = accountDataWrapper.get(PURCHASE_OFFER_EXPIRE_DATE_INDEX);
        if (byteData != null)
        {
            offerPrize = new String(byteData);
        }

        return offerPrize;
    }

    /**
     * I add this because my profile need offer currency to display...
     * 
     * @return
     */
    public void setOfferExpireDate(long expireDate)
    {
        if (this.accountDataWrapper != null)
        {
            if (expireDate >0)
                accountDataWrapper.put(PURCHASE_OFFER_EXPIRE_DATE_INDEX,
                    convertToString(expireDate).getBytes());
        }
    }
    
    private String convertToString(long timestamp)
    {
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String str = df.format(timestamp);
        return str;
    }
    
    public boolean isPremiumAccount()
    {
        String accountType = getAccountType();
        
        if(accountType != null && accountType.indexOf("PAID") >= 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
}
