/**
 * @author alexg
 */
package com.telenav.datatypes.audio;

import com.telenav.logger.Logger;

/**
 * @author alexg
 * 
 */
public class AudioData
{
    protected byte[] audioData;

    protected int resourceId;

    protected String resourceUri;

    protected String category;

    protected IAudioDataProvider dataProvider;

    protected AudioData(int resourceId)
    {
        this.resourceId = resourceId;
    }

    protected AudioData(byte[] audioData)
    {
        this.audioData = audioData;
    }

    protected AudioData(String resourceUri)
    {
        this.resourceUri = resourceUri;
    }

    public void setAudio(byte[] audioData)
    {
        this.audioData = audioData;
    }

    public void setAudio(int resourceId)
    {
        this.resourceId = resourceId;
    }

    public void setAudio(String resourceUri)
    {
        this.resourceUri = resourceUri;
    }

    public byte[] getData()
    {
        if (this.audioData != null)
            return this.audioData;

        if (this.dataProvider != null)
        {
            try
            {
                return this.dataProvider.getData(this);
            }
            catch (Throwable e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
        
        return null;
    }

    public int getResourceId()
    {
        return this.resourceId;
    }

    public String getResourceUri()
    {
        return this.resourceUri;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getCategory()
    {
        return this.category;
    }

    public void setDataProvider(IAudioDataProvider dataProvider)
    {
        this.dataProvider = dataProvider;
    }

    public IAudioDataProvider getDataProvider()
    {
        return this.dataProvider;
    }

    public interface IAudioDataProvider
    {
        public byte[] getData(AudioData clipData) throws Throwable;
    }
}
