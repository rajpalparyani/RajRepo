/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ReviewDetail.java
 *
 */
package com.telenav.data.datatypes.poi;

//import com.telenav.datatypes.Node;

/**
 *@author bduan
 *@date 2010-11-11
 */
public class ReviewDetail
{
    protected int poiId;
    protected int reviewId;
    protected int type;
    
    protected String rating;
    protected String reviewerName;
    protected String reviewText;
    protected String updateDate;
    
    public String getRating()
    {
        return rating;
    }
    
    public void setRating(String rating)
    {
        this.rating = rating;
    }

    public int getPoiId()
    {
        return poiId;
    }
    
    public void setPoiId(int poiId)
    {
        this.poiId = poiId;
    }

    public int getReviewId()
    {
        return reviewId;
    }
    
    public void setReviewId(int reviewId)
    {
        this.reviewId = reviewId;
    }

    public String getReviewerName()
    {
        return reviewerName;
    }
    
    public void setReviewerName(String reviewerName)
    {
        this.reviewerName = reviewerName;
    }

    public String getReviewText()
    {
        return reviewText;
    }
    
    public void setReviewText(String reviewText)
    {
        this.reviewText = reviewText;
    }

    public String getUpdateDate()
    {
        return updateDate;
    }
    
    public void setUpdateDate(String updateDate)
    {
        this.updateDate = updateDate;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    public int getType()
    {
        return this.type;
    }
}
