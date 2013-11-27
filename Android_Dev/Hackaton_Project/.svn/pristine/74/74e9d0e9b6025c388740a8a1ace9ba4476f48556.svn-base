/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TxNodePoiSerializable.java
 *
 */
package com.telenav.data.serializable.txnode;

import java.util.Vector;

import com.telenav.data.datatypes.poi.Ad;
import com.telenav.data.datatypes.poi.BizPoi;
import com.telenav.data.datatypes.poi.Coupon;
import com.telenav.data.datatypes.poi.OneBoxSearchBean;
import com.telenav.data.datatypes.poi.OpenTableInfo;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.datatypes.poi.PoiCategory;
import com.telenav.data.datatypes.poi.ReviewDetail;
import com.telenav.data.datatypes.poi.SupplimentInfo;
import com.telenav.data.serializable.IPoiSerializable;
import com.telenav.data.serializable.SerializableManager;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
class TxNodePoiSerializable implements IPoiSerializable
{
    public Ad createAd(byte[] data)
    {
        return createAd(new Node(data, 0));
    }
    
    static Ad createAd(Node node)
    {
        if (node == null)
            return null;

        Ad ad = new Ad();
        int strSize = node.getStringsSize();

        if (strSize > 0)
        {
            ad.setAdTag(node.getStringAt(0));
        }

        if (strSize > 1)
        {
            ad.setAdLine(node.getStringAt(1));
        }

        if (strSize > 2)
        {
            ad.setAdSource(node.getStringAt(2));
        }

        if (strSize > 3)
            ad.setAdID(node.getStringAt(3));

        return ad;
    }
    
    public byte[] toBytes(Ad ad)
    {
        return toNode(ad).toBinary();
    }

    static Node toNode(Ad ad)
    {
        Node node = new Node();
        node.addValue(Poi.TYPE_AD);

        node.addString(ad.getAdTag());
        node.addString(ad.getAdLine());
        node.addString(ad.getAdSource());
        node.addString(ad.getAdID());

        return node;
    }
    
    public BizPoi createBizPoi(byte[] data)
    {
        return createBizPoi(new Node(data, 0));
    }
    
    static BizPoi createBizPoi(Node node)
    {
        if (node == null)
            return null;

        BizPoi bizPoi = new BizPoi();

        int strSize = node.getStringsSize();

        if (strSize > 0)
        {
            bizPoi.setDistance(node.getStringAt(0));
        }

        if (strSize > 1)
        {
            bizPoi.setPhoneNumber(node.getStringAt(1));
        }

        if (strSize > 2)
        {
            bizPoi.setPrice(node.getStringAt(2));
        }

        if (strSize > 3)
        {
            bizPoi.setBrand(node.getStringAt(3));
        }

        if (strSize > 4)
        {
            bizPoi.setPoiId(node.getStringAt(4));
        }

        if (strSize > 5)
        {
            bizPoi.setCategoryId(node.getStringAt(5));
        }

        if (strSize > 6)
        {
            bizPoi.setCategoryName(node.getStringAt(6));
        }

        if(node.getChildrenSize() > 0)
        {
            bizPoi.setStop(SerializableManager.getInstance().getAddressSerializable().createStop(node.getChildAt(0).toBinary()));
        }
        
        return bizPoi;
    }
    
    public byte[] toBytes(BizPoi bizPoi)
    {
        return toNode(bizPoi).toBinary();
    }
    
    static Node toNode(BizPoi bizPoi)
    {
        Node node = new Node();
        node.addValue(Poi.TYPE_BUSINESS_DETAIL);
        
        node.addString(bizPoi.getDistance());
        node.addString(bizPoi.getPhoneNumber());
        node.addString(bizPoi.getPrice());
        node.addString(bizPoi.getBrand());
        node.addString(bizPoi.getPoiId());
        node.addString(bizPoi.getCategoryId());
        node.addString(bizPoi.getCategoryName());
        
        if(bizPoi.getStop() != null)
        {
            Node stopNode = new Node(SerializableManager.getInstance().getAddressSerializable().toBytes(bizPoi.getStop()), 0);
            node.addChild(stopNode);
        }
        
        return node;
    }
    
    public Coupon createCoupon(byte[] data)
    {
        return createCoupon(new Node(data, 0));
    }
    
    static Coupon createCoupon(Node node)
    {
        if (node == null)
            return null;

        Coupon coupon = new Coupon();

        int strSize = node.getStringsSize();

        if (strSize > 0)
        {
            coupon.setDescription(node.getStringAt(0));
        }

        if (strSize > 1)
        {
            coupon.setEndDate(node.getStringAt(1));
        }

        coupon.setImageData(node.getBinaryData());

        return coupon;
    }
    
    public byte[] toBytes(Coupon coupon)
    {
        return toNode(coupon).toBinary();
    }
    
    static Node toNode(Coupon coupon)
    {
        Node node = new Node();
        node.addValue(Poi.TYPE_COUPON);

        node.addString(coupon.getDescription());
        node.addString(coupon.getEndDate());
        node.setBinaryData(coupon.getImageData());
        
        return node;
    }
    
    public OpenTableInfo createOpenTableInfo(byte[] data)
    {
        return createOpenTableInfo(new Node(data, 0));
    }
    
    static OpenTableInfo createOpenTableInfo(Node node)
    {
        if (node == null)
            return null;

        OpenTableInfo openTableInfo = new OpenTableInfo();

        int valueSize = node.getValuesSize();
        if (valueSize > 1)
        {
            openTableInfo.setIsReservable((((int) node.getValueAt(1)) == 1 ? true : false));
        }
        int strSize = node.getStringsSize();
        if (strSize > 0)
        {
            openTableInfo.setPartnerPoiId(node.getStringAt(0));
        }
        if (strSize > 1)
        {
            openTableInfo.setPartner(node.getStringAt(1));
        }

        return openTableInfo;
    }
    
    public byte[] toBytes(OpenTableInfo openTableInfo)
    {
        return toNode(openTableInfo).toBinary();
    }
    
    static Node toNode(OpenTableInfo openTableInfo)
    {
        Node node = new Node();
        node.addValue(Poi.TYPE_OPENTABLEINFO);
        node.addValue(openTableInfo.getIsReservable() ? 1 : 0);
        node.addString(openTableInfo.getPartnerPoiId());
        node.addString(openTableInfo.getPartner());

        return node;
    }
    
    public ReviewDetail createReviewDetail(byte[] data)
    {
        return createReviewDetail(new Node(data, 0));
    }
    
    static ReviewDetail createReviewDetail(Node node)
    {
        if (node == null)
            return null;

        ReviewDetail reviewDetail = new ReviewDetail();
        int strSize = node.getStringsSize();

        if (strSize > 0)
        {
            reviewDetail.setRating(node.getStringAt(0));
        }

        if (strSize > 1)
        {
            reviewDetail.setReviewerName(node.getStringAt(1));
        }

        if (strSize > 2)
        {
            reviewDetail.setReviewText(node.getStringAt(2));
        }

        if (strSize > 3)
        {
            reviewDetail.setUpdateDate(node.getStringAt(3));
        }
        int valueSize = node.getValuesSize();
        if (valueSize > 0)
        {
            reviewDetail.setType((int) node.getValueAt(0));
        }

        if (valueSize > 1)
        {
            reviewDetail.setPoiId((int) node.getValueAt(1));
        }

        if (valueSize > 2)
        {
            reviewDetail.setReviewId((int) node.getValueAt(2));
        }

        return reviewDetail;
    }
    
    public byte[] toBytes(ReviewDetail reviewDetail)
    {
        return toNode(reviewDetail).toBinary();
    }
    
    static Node toNode(ReviewDetail reviewDetail)
    {
        Node node = new Node();
        node.addValue(Poi.TYPE_REVIEW_DETAIL);
        
        node.addString(reviewDetail.getRating());
        node.addValue(reviewDetail.getPoiId());
        node.addValue(reviewDetail.getReviewId());
        node.addString(reviewDetail.getReviewerName());
        node.addString(reviewDetail.getReviewText());
        node.addString(reviewDetail.getUpdateDate());
        
        return node;
    }
    
    public SupplimentInfo createSupplimentInfo(byte[] data)
    {
        return createSupplimentInfo(new Node(data, 0));
    }
    
    static SupplimentInfo createSupplimentInfo(Node node)
    {
        if (node == null)
            return null;
        
        SupplimentInfo supplimentInfo = new SupplimentInfo();

        int strSize = node.getStringsSize();

        if (strSize > 0)
        {
            supplimentInfo.setPrice(node.getStringAt(0));
        }

        if (strSize > 1)
        {
            supplimentInfo.setSupportInfo(node.getStringAt(1));
        }
        
        return supplimentInfo;
    }
    
    public byte[] toBytes(SupplimentInfo supplimentInfo)
    {
        return toNode(supplimentInfo).toBinary();
    }
    
    static Node toNode(SupplimentInfo supplimentInfo)
    {
        Node node = new Node();
        node.addValue(Poi.TYPE_SUPPLIMENT_INFO);
        
        node.addString(supplimentInfo.getPrice());
        node.addString(supplimentInfo.getSupportInfo());
        
        return node;
    }
    
    public Poi createPoi(byte[] data)
    {
        return createPoi(new Node(data, 0));
    }
    
    static Poi createPoi(Node node)
    {
        if(node == null)
            return null;
        
        Poi poi = new Poi();
        
        poi.setType((int)node.getValueAt(0));
        
        int valueSize = node.getValuesSize();

        if(valueSize > 1)
        {
            poi.setRating((int) node.getValueAt(1));
        }
         
        if(valueSize > 2)
        {
            poi.setPopularity((int) node.getValueAt(2));
        }
        
        if(valueSize > 3)
        {
            poi.setRatingNumber((int) node.getValueAt(3));
        }
        
        if(valueSize > 4)
        {
            poi.setUsePreviousRating((int)node.getValueAt(4));
        }
        
        if(valueSize > 5)
        {
            int isRatingEnableValue = (int)node.getValueAt(5);
            poi.setIsRatingEnable(isRatingEnableValue == 1 ? true : false);
        }
        
        if(valueSize > 6)
        {
            int isHasPoiSourceAdId = (int) node.getValueAt(6);
            poi.setIsHasPoiSourceAdId(isHasPoiSourceAdId == 1 ? true : false);
        }
        
        int strSize = node.getStringsSize();
        if(strSize > 0)
        {
            poi.setPriceRange(node.getStringAt(0));
        }
        if(strSize > 1)
        {
            poi.setMenu(node.getStringAt(1));
        }
        if(strSize > 2)
        {
            poi.setSourceAdId(node.getStringAt(2));
        }
        
        int childrenSize = node.getChildrenSize();
        for(int i = 0 ; i < childrenSize ; i ++)
        {
            Node child = node.getChildAt(i);
            int childType = (int)child.getValueAt(0);
            switch (childType)
            {
                case Poi.TYPE_STOP:
                {
                    poi.setStop(TxNodeAddressSerializable.createStop(child));
                    break;
                }
                case Poi.TYPE_BUSINESS_DETAIL:
                {
                    poi.setBizPoi(createBizPoi(child));
                    break;
                }
                case Poi.TYPE_SUPPLIMENT_INFO:
                {
                    SupplimentInfo supplimentInfo = createSupplimentInfo(child);

                    Vector supplimentInfos = poi.getSupplimentInfos() == null ? new Vector() : poi.getSupplimentInfos();

                    supplimentInfos.addElement(supplimentInfo);
                    
                    poi.setSupplimentInfos(supplimentInfos);
                    break;
                }
                case Poi.TYPE_REVIEW_DETAIL:
                {
                    ReviewDetail reviewDetail = createReviewDetail(child);

                    Vector reviewDetails = poi.getReviewDetails() == null ? new Vector() : poi.getReviewDetails();

                    reviewDetails.addElement(reviewDetail);
                    poi.setReviewDetails(reviewDetails);
                    break;
                }
                case Poi.TYPE_AD:
                {
                    poi.setAd(createAd(child));
                    break;
                }
                case Poi.TYPE_OPENTABLEINFO:
                {
                    poi.setOpenTableInfo(createOpenTableInfo(child));
                    break;
                }
                case Poi.TYPE_COUPON:
                {
                    Coupon coupon = createCoupon(child);

                    Vector coupons = poi.getCoupons() == null ? new Vector() : poi.getCoupons();

                    coupons.addElement(coupon);
                    poi.setCoupons(coupons);
                    break;
                }
                default:
                    break;
            }
        }
        
        return poi;
    }
    
    public byte[] toBytes(Poi poi)
    {
        return toNode(poi).toBinary();
    }
    
    static Node toNode(Poi poi)
    {
        Node node = new Node();

        node.addValue(poi.getType()); //0
        node.addValue(poi.getRating()); //1
        node.addValue(poi.getPopularity()); //2
        node.addValue(poi.getRatingNumber()); //3
        node.addValue(poi.getUsePreviousRating()); //4
        node.addValue(poi.getIsRatingEnable() ? 1 : 0); //5
        node.addValue(poi.hasPoiSourceAdId() ? 1 : 0); //6
        node.addString(poi.getPriceRange()); //0
        node.addString(poi.getMenu()); //1
        node.addString(poi.getSourceAdId()); //2

        Node bizNode = null;
        if (poi.getBizPoi() != null)
        {
            bizNode = toNode(poi.getBizPoi());
            node.addChild(bizNode);
        }

        if (poi.getStop() != null)
        {
            if(bizNode != null)
            {
                bizNode.addChild(TxNodeAddressSerializable.toNode(poi.getStop()));
            }
            else
            {
                node.addChild(TxNodeAddressSerializable.toNode(poi.getStop()));
            }
        }

        if (poi.getAd() != null)
        {
            node.addChild(toNode(poi.getAd()));
        }

        if (poi.getReviewDetails() != null && poi.getReviewDetails().size() > 0)
        {
            int size = poi.getReviewDetails().size();

            for (int i = 0; i < size; i++)
            {
                ReviewDetail reviewDetail = (ReviewDetail) poi.getReviewDetails().elementAt(i);
                node.addChild(toNode(reviewDetail));
            }
        }

        if (poi.getCoupons() != null && poi.getCoupons().size() > 0)
        {
            int size = poi.getCoupons().size();

            for (int i = 0; i < size; i++)
            {
                Coupon coupon = (Coupon) poi.getCoupons().elementAt(i);
                node.addChild(toNode(coupon));
            }
        }

        if (poi.getSupplimentInfos() != null && poi.getSupplimentInfos().size() > 0)
        {
            int size = poi.getSupplimentInfos().size();

            for (int i = 0; i < size; i++)
            {
                SupplimentInfo supplimentInfo = (SupplimentInfo) poi.getSupplimentInfos().elementAt(i);
                node.addChild(toNode(supplimentInfo));
            }
        }
        
        return node;
    }
    
    public PoiCategory createPoiCategory(byte[] poiCategory)
    {
        if(poiCategory == null)
            return null;
        
        Node node = new Node(poiCategory, 0);
        PoiCategory category = new PoiCategory();
        swap(node, category);
        
        return category;
    }
    
    private void swap(Node node, PoiCategory category)
    {
        if(node.getValuesSize() > 0)
        {
            category.setCategoryId((int)node.getValueAt(0));
        }
        if(node.getValuesSize() > 1)
        {
            category.setFlags((int)node.getValueAt(1));
        }
        if(node.getValuesSize() > 2)
        {
            category.setIsEvent((int)node.getValueAt(2) == 1 ? true : false);
        }
        if(node.getStringsSize() > 0)
        {
            category.setName(node.getStringAt(0));
        }
        if(node.getStringsSize() > 1)
        {
            category.setEventId(node.getStringAt(1));
        }
        if(node.getStringsSize() > 2)
        {
            category.setUnfocusedImagePath(node.getStringAt(2));
            if(node.getStringsSize() > 3)
            {
                category.setFocusedImagePath(node.getStringAt(3));
            }
            else
            {
                category.setFocusedImagePath(node.getStringAt(2));
            }
        }
        for(int i = 0; i < node.getChildrenSize(); i++)
        {
            PoiCategory tmpCategory = new PoiCategory();
            category.addChild(tmpCategory);
            swap(node.getChildAt(i), tmpCategory);
        }
    }
    
    public byte[] toBytes(PoiCategory poiCategory)
    {
        if(poiCategory == null)
            return null;
        
        Node node = new Node();
        swapx(node, poiCategory);
        
        return node.toBinary();
    }
    
    private void swapx(Node node, PoiCategory category)
    {
        node.addValue(category.getCategoryId());
        node.addValue(category.getFlags());
        node.addValue(category.isEvent() ? 1 : 0);
        node.addString(category.getName());
        node.addString(category.getEventId());
        boolean hasUnfocusedImagePath = category.getUnfocusedImagePath() != null && category.getUnfocusedImagePath().length() > 0;
        boolean hasFocusedImagePath = category.getFocusedImagePath() != null && category.getFocusedImagePath().length() > 0;
        if(hasUnfocusedImagePath || hasFocusedImagePath)
        {
            if(hasUnfocusedImagePath)
            {
                node.addString(category.getUnfocusedImagePath());
                if(hasFocusedImagePath)
                {
                    node.addString(category.getFocusedImagePath());
                }
                else
                {
                    node.addString(category.getUnfocusedImagePath());
                }
            }
            else
            {
                node.addString(category.getFocusedImagePath());
                node.addString(category.getFocusedImagePath());
            }
        }
        for(int i = 0; i < category.getChildrenSize(); i++)
        {
            Node tmpNode = new Node();
            swapx(tmpNode, category.getChildAt(i));
            node.addChild(tmpNode);
        }
    }
    
    public OneBoxSearchBean createOneBoxSearchBean(byte[] data)
    {
        if(data == null)
            return null;
        
        Node node = new Node(data, 0);
        OneBoxSearchBean bean = new OneBoxSearchBean();
        bean.setKey(node.getStringAt(0));
        bean.setContent(node.getStringAt(1));
        
        return bean;
    }
    
    public byte[] toBytes(OneBoxSearchBean bean)
    {
        if(bean == null)
            return null;
        
        Node node = new Node();
        node.addString(bean.getKey());
        node.addString(bean.getContent());
        
        return node.toBinary();
    }
}
