package com.telenav.module.media;

import java.util.Vector;

import com.telenav.app.ThreadManager;
import com.telenav.audio.AudioPlayer;
import com.telenav.audio.AudioRecorder;
import com.telenav.audio.IPlayerListener;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.preference.Preference;
import com.telenav.data.serverproxy.AbstractServerProxy;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.datatypes.audio.AudioData;
import com.telenav.datatypes.audio.AudioDataFactory;
import com.telenav.datatypes.audio.AudioDataNode;
import com.telenav.media.ITnMediaListener;
import com.telenav.media.TnMediaManager;
import com.telenav.media.TnMediaPlayer;
import com.telenav.telephony.ITnPhoneListener;

/**
 * 
 * Audio manager who response for <br>
 * 
 * 1) Play audio from INode <br>
 * 
 * 2) Stop play audio <br>
 * 
 * 3) Get missing resources <br>
 * 
 * @author zhdong@telenav.cn
 * 
 */
public class MediaManager implements IAudioConstants, ITnMediaListener, IServerProxyListener, ITnPhoneListener
{
    public String audioPlayerFormat = TnMediaManager.FORMAT_MP3;
//    public String audioRecorderFormat = TnMediaManager.FORMAT_AMR;
    public String audioRecorderFormat = TnMediaManager.FORMAT_PCM;
    
    private static MediaManager instance = new MediaManager();

    private static Object mutex = new Object();

    private static int DEFAULT_TIMEOUT = 32 * 1000;

    private ITnMediaListener listener;

  //TODO: clean up codes, has been move to dsr logic.
//    private IMissingAudioProxy missingAudioProxy;
//
//    private Vector currentAudioPlayList;
//
//    private Vector allAudioPlayList;
//
//    private int currentAudioPlayIndex;
//
//    private Vector missingAudioIds;
//
//    private boolean isBrokenItemMeet;
//
//    private boolean isOutsideRange;
//
//    boolean isMissingResourcesGot;
    
    private AudioPlayer audioPlayer;
    
    private AudioRecorder recordPlayer;
    
    private int phoneState = ITnPhoneListener.STATE_DISCONNECTED;
    
    public static synchronized MediaManager getInstance()
    {
        return instance;
    }

    private MediaManager()
    {

    }

    public void setMediaPlayerListener(ITnMediaListener listener)
    {
        this.listener = listener;
    }

    public AudioPlayer getAudioPlayer()
    {
        synchronized (mutex)
        {
            if (audioPlayer == null)
            {
                audioPlayer = new AudioPlayer(TnMediaManager.getInstance(), ThreadManager.getPool(ThreadManager.TYPE_AUDIO_PLAYER), AudioDataProvider.getInstance(),
                        audioPlayerFormat);
            }
        }
        
        int audioDuringPhone = ((DaoManager)DaoManager.getInstance()).getPreferenceDao().getIntValue(Preference.ID_PREFERENCE_INPHONE_AUDIO);
        if(audioDuringPhone == Preference.AUDIO_SUSPEND)
        {
            if(phoneState == ITnPhoneListener.STATE_RINGING || phoneState == ITnPhoneListener.STATE_CONNECTED)
            {
                audioPlayer.enable(false);
            }
            else
            {
                audioPlayer.enable(true);
            }
        }
        else
        {
            audioPlayer.enable(true);
        }
        return audioPlayer;
    }
    
    public AudioRecorder getRecordPlayer()
    {
        synchronized (mutex)
        {
            if (recordPlayer == null)
            {
                recordPlayer = new AudioRecorder(TnMediaManager.getInstance(), ThreadManager.getPool(ThreadManager.TYPE_RECORD_PLAYER), audioRecorderFormat);
            }
        }

        return recordPlayer;
    }

    public void stopPlayAudio()
    {
        synchronized (this)
        {
            listener = null;
//            currentAudioPlayIndex = 0;
//            currentAudioPlayList = null;
//            allAudioPlayList = null;
//            missingAudioIds = null;
//            isBrokenItemMeet = false;
//            isOutsideRange = false;
//            isMissingResourcesGot = false;
        }
        waitUntilPlayerFinish();
    }

    public void waitUntilPlayerFinish()
    {
        AudioPlayer player = getAudioPlayer();
        if (player != null)
        {
            player.cancelAll();

            int i = 0;
            // wait at most 200 ms until audio really finished.
            while (player.isPlaying() && i < 4)
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
    }

//    /**
//     * Fetch dynamic audio and save them to cache.
//     * 
//     * @param audioNode
//     */
//    public void fetchDynamicAudio(Node audioNode)
//    {
//        if (audioNode != null)
//        {
//            int type = (int) audioNode.getValueAt(0);
//            if (type == TYPE_MSG_AUDIO)
//            {
//                int id = (int) audioNode.getValueAt(1);
//
//                if (audioNode.getBinaryData() != null)
//                {
//                    DaoManager.getInstance().getResourceBarDao().putDynamicAudio(id, audioNode.getBinaryData());
//                }
//            }
//
//            int size = audioNode.getChildrenSize();
//
//            for (int i = 0; i < size; i++)
//            {
//                Node child = audioNode.getChildAt(i);
//                fetchDynamicAudio(child);
//            }
//        }
//    }
//
//    public void playAudio(int startIndex, int endIndex, Node audioNode)
//    {
//        this.playAudio(startIndex, endIndex, null, audioNode, null);
//    }
//
//    private void appendStaticAudios(Node staticAudioNode)
//    {
//        if (staticAudioNode != null)
//        {
//            int audioSize = staticAudioNode.getValuesSize();
//
//            for (int i = 0; i < audioSize; i++)
//            {
//                int id = (int) staticAudioNode.getValueAt(i);
//                Node audioNode = new Node();
//                audioNode.addValue(TYPE_MSG_AUDIO);
//                audioNode.addValue(id);
//                currentAudioPlayList.addElement(audioNode);
//                allAudioPlayList.addElement(audioNode);
//            }
//        }
//    }
//
//    public void playAudio(int startIndex, int endIndex, Node preStaticAudioNode, Node audioNode, Node afterStaticAudioNode)
//    {
//        reset();
//
//        // System.out.println("Response before rule.eval()");
//        // System.out.println(audioNode);
//
//        missingAudioIds = new Vector();
//
//        int type = (int) audioNode.getValueAt(0);
//        if (type == TYPE_AUDIO_SEQUENCE)
//        {
//            currentAudioPlayList = new Vector();
//            allAudioPlayList = new Vector();
//
//            currentAudioPlayIndex = 0;
//            isBrokenItemMeet = false;
//
//            appendStaticAudios(preStaticAudioNode);
//            for (int i = 0; i < audioNode.getChildrenSize(); i++)
//            {
//
//                if (i < startIndex || i > endIndex)
//                {
//                    isOutsideRange = true;
//                }
//                else
//                {
//                    isOutsideRange = false;
//                }
//
//                Node child = audioNode.getChildAt(i);
//
//                if (child.getValueAt(0) == TYPE_MSG_AUDIO)
//                {
//                    if (!isOutsideRange)
//                    {
//                        if (!isBrokenItemMeet)
//                        {
//                            currentAudioPlayList.addElement(child);
//                        }
//                        allAudioPlayList.addElement(child);
//                    }
//
//                    int id = (int) child.getValueAt(1);
//
//                    if (child.getBinaryData() != null)
//                    {
//                        DaoManager.getInstance().getResourceBarDao().putDynamicAudio(id, child.getBinaryData());
//                    }
//                    else if (DaoManager.getInstance().getResourceBarDao().getAudio(id) == null)
//                    {
//                        missingAudioIds.addElement(PrimitiveTypeCache.valueOf(id));
//                    }
//
//                }
//                else if (child.getValueAt(0) == TYPE_AUDIO_PROMPT)
//                {
//                    addPromptNode(child);
//                }
//                else if (child.getValueAt(0) == TYPE_AUDIO_ITEM)
//                {
//                    int previousPlayListSize = currentAudioPlayList.size();
//
//                    for (int x = 0; x < child.getChildrenSize(); x++)
//                    {
//                        Node node2 = child.getChildAt(x);
//
//                        if (node2.getValueAt(0) == TYPE_MSG_AUDIO)
//                        {
//                            handleMsgAudio(node2);
//                        }
//                        else if (node2.getValueAt(0) == TYPE_AUDIO_PROMPT)
//                        {
//                            addPromptNode(node2);
//                        }
//                    }
//
//                    if (isBrokenItemMeet)
//                    {
//                        // remove incorrect nodes!!!
//                        for (int x = currentAudioPlayList.size() - 1; x >= previousPlayListSize; x--)
//                        {
//                            currentAudioPlayList.removeElementAt(x);
//                        }
//                    }
//
//                }
//            }
//            appendStaticAudios(afterStaticAudioNode);
//
//            // play audio
//            playAudio();
//
//            // meanwhile, get missing audio
//            if (missingAudioIds.size() > 0)
//            {
//                int[] audioIds = new int[missingAudioIds.size()];
//                for (int i = 0; i < missingAudioIds.size(); i++)
//                {
//                    Integer idI = (Integer) missingAudioIds.elementAt(i);
//                    audioIds[i] = idI.intValue();
//                }
//                missingAudioIds = null;
//                this.getMissingResources(null, audioIds);
//            }
//
//        }
//
//    }
//
//    private void handleMsgAudio(Node msgAudioNode)
//    {
//        int id = (int) msgAudioNode.getValueAt(1);
//
//        if (id < 0)
//        {
//            for (int i = 0; i < msgAudioNode.getChildrenSize(); i++)
//            {
//                Node child = msgAudioNode.getChildAt(i);
//                if (child.getValueAt(0) == TYPE_MSG_AUDIO)
//                {
//                    handleMsgAudio(child);
//                }
//            }
//            return;
//        }
//
//        if (!isOutsideRange)
//        {
//            if (!isBrokenItemMeet)
//            {
//                currentAudioPlayList.addElement(msgAudioNode);
//            }
//            allAudioPlayList.addElement(msgAudioNode);
//        }
//
//        ResourceBarDao resDao = DaoManager.getInstance().getResourceBarDao();
//
//        if (msgAudioNode.getBinaryData() != null)
//        {
//            resDao.putDynamicAudio(id, msgAudioNode.getBinaryData());
//        }
//        else if (resDao.getAudio(id) == null)
//        {
//            isBrokenItemMeet = true;
//        }
//
//    }

    public void playStaticAudio(int audioId)
    {

        reset();

        byte[] audio = DaoManager.getInstance().getResourceBarDao().getAudio(audioId);

        if (audio != null)
        {
            AudioData data = AudioDataFactory.getInstance().createAudioData(audio);
            AudioData[] playList = new AudioData[]
            { data };

            AudioPlayer player = getAudioPlayer();
            player.play("", playList, DEFAULT_TIMEOUT);

        }
        else
        {
            if (listener != null)
            {
                listener.mediaUpdate(null, ON_CLOSE, null);
            }
        }
    }
    
    public void playStaticAudio(int audioIds[])
    {
        reset();

        AudioData[] playList = new AudioData[audioIds.length];

        for (int i = 0; i < audioIds.length; i++)
        {
            byte[] audio = DaoManager.getInstance().getResourceBarDao().getAudio(audioIds[i]);

            if (audio != null)
            {
                playList[i] = AudioDataFactory.getInstance().createAudioData(audio);
            }
            else
            {
                if (listener != null)
                {
                    listener.mediaUpdate(null, ON_CLOSE, null);
                }
                return;
            }
        }

        AudioPlayer player = getAudioPlayer();
        player.play("", playList, DEFAULT_TIMEOUT);
    }

    private void reset()
    {
        waitUntilPlayerFinish();
//        isMissingResourcesGot = false;
    }

    public void playStaticAudio(AudioDataNode staticAudioNode)
    {
        reset();
        int audioSize = staticAudioNode.getChildrenSize();
        if (audioSize > 0)
        {
            AudioData[] playList = new AudioData[audioSize];
            for (int i = 0; i < audioSize; i++)
            {
                AudioData data = staticAudioNode.getChild(i).getAudioData();
                if (data.getData() == null)
                {
                    // We should never go to this case !!!
                    System.err.println("Audio " + data.getResourceId() + " does not exist!!!");
                    try
                    {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    if (listener != null)
                    {
                        listener.mediaUpdate(null, ON_CLOSE, null);
                    }
                    return;
                }
                playList[i] = data;
            }

            AudioPlayer player = getAudioPlayer();
            player.play("", playList, DEFAULT_TIMEOUT);
        }
        else
        {
            if (listener != null)
            {
                listener.mediaUpdate(null, ON_CLOSE, null);
            }
        }
    }

//    public void playAudio(Node audioNode)
//    {
//        reset();
//
//        missingAudioIds = new Vector();
//        isOutsideRange = false;
//
//        int type = (int) audioNode.getValueAt(0);
//        if (type == TYPE_AUDIO_SEQUENCE)
//        {
//            currentAudioPlayList = new Vector();
//            allAudioPlayList = new Vector();
//
//            currentAudioPlayIndex = 0;
//            isBrokenItemMeet = false;
//
//            for (int i = 0; i < audioNode.getChildrenSize(); i++)
//            {
//                Node node1 = audioNode.getChildAt(i);
//
//                if (node1.getValueAt(0) == TYPE_MSG_AUDIO)
//                {
//                    if (!isBrokenItemMeet)
//                    {
//                        currentAudioPlayList.addElement(node1);
//                    }
//                    allAudioPlayList.addElement(node1);
//
//                    int id = (int) node1.getValueAt(1);
//
//                    if (node1.getBinaryData() != null)
//                    {
//                        DaoManager.getInstance().getResourceBarDao().putDynamicAudio(id, node1.getBinaryData());
//                    }
//                    else if (DaoManager.getInstance().getResourceBarDao().getAudio(id) == null)
//                    {
//                        missingAudioIds.addElement(PrimitiveTypeCache.valueOf(id));
//                    }
//
//                }
//                else if (node1.getValueAt(0) == TYPE_AUDIO_PROMPT)
//                {
//                    addPromptNode(node1);
//                }
//                else if (node1.getValueAt(0) == TYPE_AUDIO_ITEM)
//                {
//                    int previousPlayListSize = currentAudioPlayList.size();
//
//                    for (int x = 0; x < node1.getChildrenSize(); x++)
//                    {
//                        Node node2 = node1.getChildAt(x);
//
//                        if (node2.getValueAt(0) == TYPE_MSG_AUDIO)
//                        {
//                            handleMsgAudio(node2);
//                        }
//                        else if (node2.getValueAt(0) == TYPE_AUDIO_PROMPT)
//                        {
//                            addPromptNode(node2);
//                        }
//                    }
//
//                    if (isBrokenItemMeet)
//                    {
//                        // remove incorrect nodes!!!
//                        for (int x = currentAudioPlayList.size() - 1; x >= previousPlayListSize; x--)
//                        {
//                            currentAudioPlayList.removeElementAt(x);
//                        }
//                    }
//
//                }
//            }
//
//            // play audio
//            playAudio();
//
//            // meanwhile, get missing audio
//            if (missingAudioIds.size() > 0)
//            {
//                int[] audioIds = new int[missingAudioIds.size()];
//                for (int i = 0; i < missingAudioIds.size(); i++)
//                {
//                    Integer idI = (Integer) missingAudioIds.elementAt(i);
//                    audioIds[i] = idI.intValue();
//                }
//                missingAudioIds = null;
//                this.getMissingResources(null, audioIds);
//            }
//
//        }
//    }
//
//    private void addPromptNode(Node node1)
//    {
//        int ruleId = (int) node1.getValueAt(1);
//
//        RuleNode rule = DaoManager.getInstance().getResourceBarDao().getAudioRule(ruleId);
//
//        if (rule == null)
//        {
//            System.err.println("Missing rule " + ruleId);
//            return;
//        }
//
//        Vector intV = new Vector();
//        Vector nodeV = new Vector();
//
//        for (int x = 0; x < node1.getChildrenSize(); x++)
//        {
//            Node node2 = node1.getChildAt(x);
//
//            if (node2.getValueAt(0) == TYPE_INT_ARGUMENTS)
//            {
//                for (int y = 1; y < node2.getValuesSize(); y++)
//                {
//                    int value = (int) node2.getValueAt(y);
//                    intV.addElement(PrimitiveTypeCache.valueOf(value));
//                }
//
//            }
//            else if (node2.getValueAt(0) == TYPE_NODE_ARGUMENTS)
//            {
//                for (int y = 0; y < node2.getChildrenSize(); y++)
//                {
//                    nodeV.addElement(node2.getChildAt(y));
//                }
//            }
//        }
//
//        int[] intArgs = new int[intV.size()];
//        for (int x = 0; x < intV.size(); x++)
//        {
//            intArgs[x] = ((Integer) intV.elementAt(x)).intValue();
//        }
//        AudioDataNode[] nodeArgs = new AudioDataNode[nodeV.size()];
//
//        for (int x = 0; x < nodeV.size(); x++)
//        {
//            Node node = (Node)nodeV.elementAt(x);
//            nodeArgs[x] = convertArgNode(node);
//        }
//
//        AudioDataNode node = rule.eval(intArgs, nodeArgs);
//
//        addToResponseNode(node);
//    }
//    
//    protected AudioDataNode convertArgNode(Node node)
//    {
//        AudioDataNode audioDataNode = null;
//        if (node.getValueAt(0) == TYPE_MSG_AUDIO)
//        {
//            int id = (int) node.getValueAt(1);
//
//            byte[] binData = node.getBinaryData();
//            if(binData != null)
//            {
//                audioDataNode = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(binData));
//            }
//            else
//            {
//                audioDataNode = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(id));
//            }
//            
//            int size = node.getChildrenSize();
//            if(size > 0)
//            {
//                for(int i = 0 ; i < size ; i ++)
//                {
//                    AudioDataNode childAudioDataNode = convertArgNode(node.getChildAt(i));
//                    audioDataNode.addChild(childAudioDataNode);
//                }
//            }
//        }
//        
//        return audioDataNode;
//    }

    public void playAudio(Vector audioPlayList)
    {
        playAudio(audioPlayList, null);
    }
    
    public void playAudio(Vector audioPlayList, IPlayerListener playerListener)
    {
        Vector audios = new Vector();
        //Vector missingAudioIds = new Vector();

        if (audioPlayList == null)
        {
            return;
        }
        for (int i = 0; i < audioPlayList.size(); i++)
        {
            Object object = audioPlayList.elementAt(i);
            AudioDataNode audioDataNode = (AudioDataNode) object;
            audios.addElement(audioDataNode.getAudioData());
        }

        AudioData[] playList = new AudioData[audios.size()];
        for (int i = 0; i < audios.size(); i++)
        {
            playList[i] = (AudioData) audios.elementAt(i);
        }
        if (playList.length > 0)
        {
            AudioPlayer player = getAudioPlayer();
            player.play("", playList, DEFAULT_TIMEOUT, playerListener);
        }
        else
        {
            if (listener != null)
            {
                listener.mediaUpdate(null, ON_CLOSE, null);
            }
        }

    }

//    private void addToResponseNode(AudioDataNode subNode)
//    {
//        for (int i = 0; i < subNode.getChildrenSize(); i++)
//        {
//            AudioDataNode node = subNode.getChild(i);
//            if (node.getAudioData() != null)
//            {
//                if (node.getAudioData().getResourceId() == -1)
//                {
//                    addToResponseNode(node);
//                }
//                else
//                {
//                    int id = node.getAudioData().getResourceId();
//
//                    byte[] binData = node.getAudioData().getData();
//
//                    if (binData != null)
//                    {
//                        DaoManager.getInstance().getResourceBarDao().putDynamicAudio(id, binData);
//                    }
//                    else if (DaoManager.getInstance().getResourceBarDao().getAudio(id) == null)
//                    {
//                        isBrokenItemMeet = true;
//                        missingAudioIds.addElement(PrimitiveTypeCache.valueOf(id));
//                    }
//
//                    if (!isOutsideRange)
//                    {
//                        if (!isBrokenItemMeet)
//                        {
//                            currentAudioPlayList.addElement(node);
//                        }
//                        allAudioPlayList.addElement(node);
//                    }
//                }
//            }
//            else
//            {
//                addToResponseNode(node);
//            }
//        }
//    }

    //TODO: clean up codes, has been move to dsr logic.
//    public void getMissingResources(int[] ruleIds, int[] audioIds)
//    {
//        if (missingAudioProxy == null)
//        {
//            missingAudioProxy = ServerProxyFactory.getInstance().createMissingAudioProxy(null, CommManager.getInstance().getComm(), this);
//        }
//        missingAudioProxy.sendMissingAudioRequest(audioIds);
//    }

    public void mediaUpdate(TnMediaPlayer player, String event, Object eventData)
    {
      //TODO: clean up codes, has been move to dsr logic.
//        if (event.equals(ITnMediaListener.ON_CLOSE))
//        {
//            if (isMissingResourcesGot && listener != null)
//            {
//                // try to go on playing the missing audios.
//                this.currentAudioPlayList = this.allAudioPlayList;
//                this.playAudio(currentAudioPlayList);
//            }
//            else
//            {
//                ITnMediaListener listener = this.listener;
//                if (listener != null)
//                {
//                    // System.out.println("play audio finished ... ");
//                    listener.mediaUpdate(player, event, eventData);
//                }
//
//            }
//        }

    }

    public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
    {
        return true;
    }

    public void networkError(AbstractServerProxy proxy, byte statusCode, String jobId)
    {

    }

    public void transactionError(AbstractServerProxy proxy)
    {

    }

    public void transactionFinished(AbstractServerProxy proxy, String jobId)
    {
//        this.isMissingResourcesGot = true;
    }

    public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
    {
        // TODO Auto-generated method stub

    }

    public void onCallStateChanged(int state, String phoneNumber, String reasonString)
    {
        phoneState = state;
        
        getAudioPlayer();
    }
    
    /**
     * @param audioPlayerFormat the audioPlayerFormat to set
     */
    public void setAudioPlayerFormat(String audioPlayerFormat)
    {
        synchronized (mutex)
        {
            this.audioPlayerFormat = audioPlayerFormat;
            audioPlayer = null;
        }
    }
}
