package com.telenav.dsr;

import java.util.Hashtable;
import java.util.Vector;

import com.telenav.app.CommManager;
import com.telenav.audio.AudioRecorder;
import com.telenav.audio.IRecorderListener;
import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.DsrServerDrivenParamsDao;
import com.telenav.data.dao.serverproxy.ServerDrivenParamsDao;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.impl.IDsrStreamProxy;
import com.telenav.data.serverproxy.impl.IMissingAudioProxy;
import com.telenav.data.serverproxy.impl.ServerProxyFactory;
import com.telenav.dsr.amr.AmrRecordStreamer;
import com.telenav.dsr.impl.AbstractDsrJob;
import com.telenav.dsr.impl.AbstractRecordStreamer;
import com.telenav.dsr.impl.IDsrConstants;
import com.telenav.dsr.impl.NetworkStreamWrapper;
import com.telenav.dsr.impl.RecognizeRequester;
import com.telenav.dsr.pcm.PcmRecordStreamer;
import com.telenav.module.media.IAudioConstants;
import com.telenav.module.media.MediaManager;
import com.telenav.threadpool.ThreadPool;
import com.telenav.util.PrimitiveTypeCache;

/**
 * 
 * Dsr manager who response for <br>
 * 
 * 1) Record voice <br>
 * 
 * 2) Send voice to DSR server for recognition<br>
 * 
 * @author zhdong@telenav.cn
 * 
 */
public class DsrManager implements IRecorderListener, IAudioConstants, IRecordEventListener
{
    public static final int PCM_FREQUENCY = 16000;  //16k sample rate    
    public static final int PCM_FRAME_IN_BYTE = PCM_FREQUENCY * 20 * 2 / 1000; //16 bit, 20 ms in bytes
    

    private static final String DEFAULT_SPEECH_END_THRESHOLD = "0$30::50$30::110$0";
    
    protected String type;
    /**
     * The session id for current active DSR request. We add this session id in order to ignore canceled previous
     * requests.
     */
    protected long session;

    protected int status;
    
    protected int networkErrorTimes;
    
    protected int networkType = COMM_TYPE_TCP;
    
    protected ThreadPool recordPool;
    
    protected IMetaInfoProvider metaProvider;
    
    protected String speechEndThreshold;
    
    protected Vector recordStatusListener;
    
    protected RecognizeRequester requester;
    
    protected NetworkStreamWrapper wrapper;
    
    protected IDsrStreamProxy dsrStreamProxy;
    
    protected AbstractRecordStreamer recordStreamer;
    
    protected IVolumeChangeListener volumeChangeListener;
    
    protected boolean isRunning = false;
    
    private boolean isPrefetched;
    
    private IRecognizeListener recognizeListener;
    
    private static DsrManager dsrManager = new DsrManager();

    private DsrManager()
    {
        
    }
    
    public void init(ThreadPool recordPool)
    {
        this.recordPool = recordPool;
    }

    public static DsrManager getInstance()
    {
        return dsrManager;
    }

    public void addRecordStatusListener(IRecordEventListener listener)
    {
        if(recordStatusListener == null)
            recordStatusListener = new Vector();
        
        if(!recordStatusListener.contains(listener))
        {
            recordStatusListener.addElement(listener);
        }
    }
    
    public boolean isRunning()
    {
        return isRunning;
    }
    
    public Vector getRecordStatusListeners()
    {
        if(this.recordStatusListener == null || this.recordStatusListener.size() == 0)
            return null;
        
        int size = recordStatusListener.size();
        
        Vector tempCancelListeners = new Vector();
        for(int i = 0; i < size ; i ++)
        {
            tempCancelListeners.addElement(recordStatusListener.elementAt(i));
        }
        return tempCancelListeners;
    }
    
    public void clearRecordStatusListeners()
    {
        if(recordStatusListener != null)
        {
            recordStatusListener.removeAllElements();
            recordStatusListener = null;
        }
    }
    
    public Comm getDsrComm()
    {
        return CommManager.getInstance().getApacheComm();
    }
    
    public Host getDsrHost()
    {
        Host requestHost = null;
        if(networkType == COMM_TYPE_TCP)
        {
            requestHost = getDsrComm().getHostProvider().createHost(IServerProxyConstants.ACT_DSR);
        }
        else
        {
            requestHost = getDsrComm().getHostProvider().createHost(IServerProxyConstants.ACT_DSR_HTTP);
        }
        return requestHost;
    }
    
    public void setMetaProvider(IMetaInfoProvider provider)
    {
        this.metaProvider = provider;
    }
    
    /**
     * Prefetch resources for DSR.
     * 
     * @param type
     * @param timeout
     * @param maxSpeechTime
     * @param usePreviousCache
     */
    public void prefetch(int format, IDsrStreamProxy dsrStreamProxy, long timeout, long maxSpeechTime, boolean usePreviousCache)
    {
        if (isPrefetched)
        {
            return;
        }

        this.session = System.currentTimeMillis();

        // If the audio player is not closed and we start Record job then, it will cause system crash!
        MediaManager.getInstance().stopPlayAudio();
        
        cancelPreviousRequest(false);

        wrapper = new NetworkStreamWrapper(dsrStreamProxy, recordPool, timeout);
        requester = new RecognizeRequester(wrapper, recordPool, dsrStreamProxy, 0);
        
        this.addRecordStatusListener(this);
        this.addRecordStatusListener(wrapper);
        
        initRecordStreamer(format, maxSpeechTime);
        
        this.dsrStreamProxy = dsrStreamProxy;
        
        startComm();

        isPrefetched = true;
    }

    public void start(String type, int format, IDsrStreamProxy dsrStreamProxy, long timeOut,
            long maxSpeechTime, boolean usePreviousCache, IRecognizeListener recognizeListener)
    {
        prefetch(format, dsrStreamProxy, timeOut, maxSpeechTime, usePreviousCache);
        this.recordStreamer.start(timeOut + maxSpeechTime + 5000, this);
        this.isPrefetched = false;
        this.isRunning = true;
        this.recognizeListener = recognizeListener;
    }

    private Hashtable initConfigs(long maxSpeechTime)
    {
        int headerReadCount = 6;
        
        int threshold = 60;
        if (DSR_RECOGNIZE_ONE_SHOT_ADDRESS.equals(type) || DSR_RECOGNIZE_ONE_SHOT_INTERSECTION.equals(type))
        {
            threshold = 75;
        }
        else if (DSR_RECOGNIZE_POI.equals(type))
        {
            threshold = 60;
        }
        else if (DSR_RECOGNIZE_AIRPORT.equals(type))
        {
            threshold = 60;
        }
        else if (DSR_RECOGNIZE_COMMAND_CONTROL.equals(type))
        {
            threshold = 60;
        }
        else if (DSR_RECOGNIZE_NUMBER.equals(type))
        {
            threshold = 30;
        }

        Hashtable map = new Hashtable();
        map.put(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_HEADER_SIZE), 
                PrimitiveTypeCache.valueOf(headerReadCount));
        map.put(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_NON_VOICE_FRAME_THRESHOLD), 
                PrimitiveTypeCache.valueOf(threshold));
        map.put(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_MAX_SPEECH_TIME),
                PrimitiveTypeCache.valueOf(maxSpeechTime));
        

        //below to initialize the pcm end pointer configuration.
        ServerDrivenParamsDao serverDrivenParamsDao = DaoManager.getInstance().getDsrServerDrivenParamsDao();
        
        speechEndThreshold = serverDrivenParamsDao.getValue(DsrServerDrivenParamsDao.DSR_SPEECH_END_THRESHOLD);
        if (speechEndThreshold == null)
        {
            speechEndThreshold = DEFAULT_SPEECH_END_THRESHOLD;
        }
        map.put(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_SPEECH_END_THRESHOLD),
                speechEndThreshold);
        
        int samplingRate =  serverDrivenParamsDao.getIntValue(DsrServerDrivenParamsDao.samplingRate);
        if(samplingRate != -1)
        {
            map.put(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_SAMPLINGRATE), 
                    PrimitiveTypeCache.valueOf(samplingRate));
        }
        
        int frameLengthInMs =  serverDrivenParamsDao.getIntValue(DsrServerDrivenParamsDao.frameLengthInMs);
        if(frameLengthInMs != -1)
        {
            map.put(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_FRAME_LENGTH_IN_MS), 
                    PrimitiveTypeCache.valueOf(frameLengthInMs));
        }
        
        int frameShiftInMs =  serverDrivenParamsDao.getIntValue(DsrServerDrivenParamsDao.frameShiftInMs);
        if(frameShiftInMs != -1)
        {
            map.put(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_FRAME_SHIFT_IN_MS), 
                    PrimitiveTypeCache.valueOf(frameShiftInMs));
        }
        
        float engFactorSOU = serverDrivenParamsDao.getIntValue(DsrServerDrivenParamsDao.engFactorSOU);
        if(engFactorSOU != -1)
        {
            map.put(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_ENG_FACTOR_SOU), 
                    new Float(engFactorSOU));
        }
        
        float engFactorEOU = serverDrivenParamsDao.getIntValue(DsrServerDrivenParamsDao.engFactorEOU);
        if(engFactorEOU != -1)
        {
            map.put(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_ENG_FACTOR_EOU), 
                    new Float(engFactorEOU));
        }
        
        int engRank = serverDrivenParamsDao.getIntValue(DsrServerDrivenParamsDao.engRank);
        if(engRank != -1)
        {
            map.put(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_ENG_RANK), 
                    PrimitiveTypeCache.valueOf(engRank));
        }
        
        int maxPauseMs = serverDrivenParamsDao.getIntValue(DsrServerDrivenParamsDao.maxIWPauseMs);
        if(maxPauseMs != -1)
        {
            map.put(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_MAX_IW_PAUSE_MS), 
                    PrimitiveTypeCache.valueOf(maxPauseMs));
        }
        
        int startOffsetMs = serverDrivenParamsDao.getIntValue(DsrServerDrivenParamsDao.startOffsetMs);
        if(startOffsetMs != -1)
        {
            map.put(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_START_OFFSET_MS), 
                    PrimitiveTypeCache.valueOf(startOffsetMs));
        }
        
        int endOffsetMs = serverDrivenParamsDao.getIntValue(DsrServerDrivenParamsDao.endOffsetMs);
        if(endOffsetMs != -1)
        {
            map.put(PrimitiveTypeCache.valueOf(IDsrConstants.CONFIG_END_OFFSET_MS), 
                    PrimitiveTypeCache.valueOf(endOffsetMs));
        }
        
        return map;
    }
    
    private void initRecordStreamer(int format, long maxSpeechTime)
    {
        Hashtable map = initConfigs(maxSpeechTime);
        
        if (format == IAudioConstants.RECORD_FORMAT_AMR)
        {
            this.recordStreamer = new AmrRecordStreamer(map, this.recordPool, this.wrapper);
        }
        else if (format == IAudioConstants.RECORD_FORMAT_PCM)
        {
            this.recordStreamer = new PcmRecordStreamer(map, this.recordPool, this.wrapper);
            AbstractDsrJob endpointerJob = this.recordStreamer.getEndpointerJob();
            if(endpointerJob instanceof IRecordEventListener)
            {
                addRecordStatusListener((IRecordEventListener)endpointerJob);
            }
        }
        
    }
    
    private void startComm()
    {
        this.getDsrComm().cancelJob(RecognizeRequester.DSR_REQUEST);
        
        this.status = DSR_STATUS_RECORD_START;
        this.requester.sendRequest();
    }

    public void invalidSession()
    {
        this.session = System.currentTimeMillis();
        this.isPrefetched = false;
    }

    private void cancelPreviousRequest(boolean isSync)
    {
        this.getDsrComm().cancelJob(RecognizeRequester.DSR_REQUEST);
        
        AudioRecorder recorder = MediaManager.getInstance().getRecordPlayer();

        if (recorder.isRecording())
        {
            recorder.cancel();
        }

            int i = 0;
            while (recorder.isRecording() && i < 20 && isSync)
            {
                i++;
                try
                {
                    Thread.sleep(50);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
    }

    public void beforeRecorder()
    {
        
    }

    public void finishRecorder()
    {

    }

    public void notifyVolumeChanged(int volume)
    {
        if(volumeChangeListener != null)
        {
            volumeChangeListener.volumeChanged(volume);            
        }
    }
    
    public void cancel()
    {
        if(requester != null)
        {
            requester.cancel();
        }
        this.isRunning = false;
        
        MediaManager.getInstance().getRecordPlayer().finish();
    }

    public void recordStatusUpdate(int event, Object eventData)
    {
        switch(event)
        {
            case IRecordEventListener.EVENT_TYPE_SPEECH_STOP:
            {
                if (recognizeListener != null)
                {
                    recognizeListener.recognizeStatusUpdate(event, null);
                }
                break;
            }
            case IRecordEventListener.EVENT_TYPE_STOP:
            {
                if (recognizeListener != null)
                {
                    recognizeListener.recognizeStatusUpdate(event, null);
                }
                MediaManager.getInstance().getRecordPlayer().finish();
                break;
            }
            case IRecordEventListener.EVENT_TYPE_RECO_FAIL:
            {
                if (recognizeListener != null)
                {
                    recognizeListener.recognizeStatusUpdate(IRecordEventListener.EVENT_TYPE_RECO_FAIL, null);
                }
                
                if(eventData instanceof Integer)
                {
                    int type = ((Integer)eventData).intValue();
                    if(type == EVENT_STOP_TYPE_NETWORK_ERROR)
                    {
                        checkNetworkType();
                    }
                }
                break;
            }
        }
    }
    
    protected void checkNetworkType()
    {
        if (networkErrorTimes < 2
                && (System.currentTimeMillis() - session) < 45000)
        {
            networkErrorTimes++;
        }
        else if (networkErrorTimes == 2
                && networkType == IAudioConstants.COMM_TYPE_TCP)
        {
            networkType = COMM_TYPE_HTTP;

            networkErrorTimes = 1; // reset retry times
        }
        else
        {
            networkType = COMM_TYPE_TCP;
            
            networkErrorTimes = 1; // reset retry times
        }
    }

    public void getMissingResources(int[] audioIds)
    {
        //TODO: handle to handle when there's response?
        IMissingAudioProxy missingAudioProxy = ServerProxyFactory.getInstance().createMissingAudioProxy(null, CommManager.getInstance().getComm(), null);
        missingAudioProxy.sendMissingAudioRequest(audioIds);
    }
    
    public void setVolumeChangeListener(IVolumeChangeListener volumeChangeListener)
    {
        this.volumeChangeListener = volumeChangeListener;
    }
    
    public void handleRespNode(byte[] responseBytes)
    {
        if(this.dsrStreamProxy != null)
        {
            int status = dsrStreamProxy.handleRespData(responseBytes);
            if(status == IServerProxyConstants.SUCCESS)
            {
                if (recognizeListener != null)
                {
                    recognizeListener.recognizeStatusUpdate(IRecordEventListener.EVENT_TYPE_RECO_SUCCESSFUL, dsrStreamProxy);
                }
                
                Vector missingAudios = dsrStreamProxy.getMissingAudios();
                if(missingAudios != null && missingAudios.size() > 0)
                {
                    int size = missingAudios.size();
                    int[] audiosIds = new int[size];
                    for(int i = 0 ; i < size ; i ++)
                    {
                        audiosIds[i] = ((Integer)missingAudios.elementAt(i)).intValue();
                    }
                    
                    getMissingResources(audiosIds);
                }
            }
            else
            {
                if (recognizeListener != null)
                    recognizeListener.recognizeStatusUpdate(IRecordEventListener.EVENT_TYPE_RECO_FAIL, dsrStreamProxy);
            }
        }
    }
}
