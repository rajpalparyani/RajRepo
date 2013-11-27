package com.telenav.dsr.amr.endpointor;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author yueyulin Mar 5, 2009
 */
public class interf_dec extends sp_dec
{
    /*
     * definition of constants
     */

    public static final short EHF_MASK = 0x0008;/* encoder homing frame pattern */

    public static class dec_interface_State
    {

        int reset_flag_old; /* previous was homing frame */

        int prev_ft; /* previous frame type */

        int prev_mode; /* previous mode */

        Speech_Decode_FrameState decoder_State; /* Points decoder state */

    }

    /*
     * DecoderMMS
     * 
     * 
     * Parameters: param O: AMR parameters stream I: input bitstream frame_type O: frame type speech_mode O: speech mode
     * in DTX
     * 
     * Function: AMR file storage format frame to decoder parameters
     * 
     * Returns: mode used mode
     */
    public static int decoderMMS(short[] param, int paramOffset, byte[] stream, int streamOffset, int[] frame_type, int frame_typeOffset,
            int[] speech_mode, int speech_modeOffset, short[] q_bit, int q_bitOffset)
    {
        int mode;
        int j;
        short[] mask;
        int maskIndex;

        memset(param, 0, (short) 0, PRMNO_MR122);
        q_bit[q_bitOffset] = (short) (0x01 & (stream[streamOffset] >> 2));
        mode = 0x0F & (stream[streamOffset] >> 3);
        streamOffset++;

        if (mode == MRDTX)
        {
            mask = order_MRDTX;
            maskIndex = 0;
            for (j = 1; j < 36; j++)
            {
                if ((stream[streamOffset] & 0x80) != 0)
                {
                    param[mask[maskIndex]] = (short) (param[mask[maskIndex]] + mask[maskIndex + 1]);
                }
                maskIndex += 2;

                if ((j % 8) != 0)
                {
                    stream[streamOffset] <<= 1;
                }
                else
                {
                    streamOffset++;
                }
            }

            /* get SID type bit */

            frame_type[frame_typeOffset] = RX_SID_FIRST;
            if ((stream[streamOffset] & 0x80) != 0)
            {
                frame_type[frame_typeOffset] = RX_SID_UPDATE;
            }

            /* since there is update, use it */
            /*  *frame_type = RX_SID_UPDATE; */

            /* speech mode indicator */
            speech_mode[speech_modeOffset] = (stream[streamOffset] >> 4) & 0x07;
            speech_mode[speech_modeOffset] = ((speech_mode[speech_modeOffset] & 0x0001) << 2) | (speech_mode[speech_modeOffset] & 0x0002)
                    | ((speech_mode[speech_modeOffset] & 0x0004) >> 2);

        }
        else if (mode == 15)
        {
            frame_type[frame_typeOffset] = RX_NO_DATA;
        }
        else if (mode == MR475)
        {
            mask = order_MR475;
            maskIndex = 0;
            for (j = 1; j < 96; j++)
            {
                if ((stream[streamOffset] & 0x80) != 0)
                {
                    param[mask[maskIndex]] = (short) (param[mask[maskIndex]] + mask[maskIndex + 1]);
                }
                maskIndex += 2;

                if ((j % 8) != 0)
                {
                    stream[streamOffset] <<= 1;
                }
                else
                {
                    streamOffset++;
                }
            }
            frame_type[frame_typeOffset] = RX_SPEECH_GOOD;
        }
        else if (mode == MR515)
        {
            mask = order_MR515;
            maskIndex = 0;
            for (j = 1; j < 104; j++)
            {
                if ((stream[streamOffset] & 0x80) != 0)
                {
                    param[mask[maskIndex]] = (short) (param[mask[maskIndex]] + mask[maskIndex + 1]);
                }
                maskIndex += 2;

                if ((j % 8) != 0)
                {
                    stream[streamOffset] <<= 1;
                }
                else
                {
                    streamOffset++;
                }
            }
            frame_type[frame_typeOffset] = RX_SPEECH_GOOD;
        }
        else if (mode == MR59)
        {
            mask = order_MR59;
            maskIndex = 0;
            for (j = 1; j < 119; j++)
            {
                if ((stream[streamOffset] & 0x80) != 0)
                {
                    param[mask[maskIndex]] = (short) (param[mask[maskIndex]] + mask[maskIndex + 1]);
                }
                maskIndex += 2;

                if ((j % 8) != 0)
                {
                    stream[streamOffset] <<= 1;
                }
                else
                {
                    streamOffset++;
                }
            }
            frame_type[frame_typeOffset] = RX_SPEECH_GOOD;
        }
        else if (mode == MR67)
        {
            mask = order_MR67;
            maskIndex = 0;
            for (j = 1; j < 135; j++)
            {
                if ((stream[streamOffset] & 0x80) != 0)
                {
                    param[mask[maskIndex]] = (short) (param[mask[maskIndex]] + mask[maskIndex + 1]);
                }
                maskIndex += 2;

                if ((j % 8) != 0)
                {
                    stream[streamOffset] <<= 1;
                }
                else
                {
                    streamOffset++;
                }
            }
            frame_type[frame_typeOffset] = RX_SPEECH_GOOD;
        }
        else if (mode == MR74)
        {
            mask = order_MR74;
            maskIndex = 0;
            for (j = 1; j < 149; j++)
            {
                if ((stream[streamOffset] & 0x80) != 0)
                {
                    param[mask[maskIndex]] = (short) (param[mask[maskIndex]] + mask[maskIndex + 1]);
                }
                maskIndex += 2;

                if ((j % 8) != 0)
                {
                    stream[streamOffset] <<= 1;
                }
                else
                {
                    streamOffset++;
                }
            }
            frame_type[frame_typeOffset] = RX_SPEECH_GOOD;
        }
        else if (mode == MR795)
        {
            mask = order_MR795;
            maskIndex = 0;
            for (j = 1; j < 160; j++)
            {
                if ((stream[streamOffset] & 0x80) != 0)
                {
                    param[mask[maskIndex]] = (short) (param[mask[maskIndex]] + mask[maskIndex + 1]);
                }
                maskIndex += 2;

                if ((j % 8) != 0)
                {
                    stream[streamOffset] <<= 1;
                }
                else
                {
                    streamOffset++;
                }
            }
            frame_type[frame_typeOffset] = RX_SPEECH_GOOD;
        }
        else if (mode == MR102)
        {
            mask = order_MR102;
            maskIndex = 0;
            for (j = 1; j < 205; j++)
            {
                if ((stream[streamOffset] & 0x80) != 0)
                {
                    param[mask[maskIndex]] = (short) (param[mask[maskIndex]] + mask[maskIndex + 1]);
                }
                maskIndex += 2;

                if ((j % 8) != 0)
                {
                    stream[streamOffset] <<= 1;
                }
                else
                {
                    streamOffset++;
                }
            }
            frame_type[frame_typeOffset] = RX_SPEECH_GOOD;
        }
        else if (mode == MR122)
        {
            mask = order_MR122;
            maskIndex = 0;
            for (j = 1; j < 245; j++)
            {
                if ((stream[streamOffset] & 0x80) != 0)
                {
                    param[mask[maskIndex]] = (short) (param[mask[maskIndex]] + mask[maskIndex + 1]);
                }
                maskIndex += 2;

                if ((j % 8) != 0)
                {
                    stream[streamOffset] <<= 1;
                }
                else
                {
                    streamOffset++;
                }
            }
            frame_type[frame_typeOffset] = RX_SPEECH_GOOD;
        }
        else
        {
            frame_type[frame_typeOffset] = RX_SPEECH_BAD;
        }
        return mode;
    }

    /*
     * Decoder_Interface_reset
     * 
     * 
     * Parameters: st O: state struct
     * 
     * Function: Reset homing frame counter
     * 
     * Returns: void
     */
    public static void Decoder_Interface_reset(dec_interface_State st)
    {
        st.reset_flag_old = 1;
        st.prev_ft = RX_SPEECH_GOOD;
        st.prev_mode = MR475; /* minimum bitrate */
    }

    /*
     * Decoder_Interface_init
     * 
     * 
     * Parameters: void
     * 
     * Function: Allocates state memory and initializes state memory
     * 
     * Returns: success : pointer to structure failure : NULL
     */
    public static dec_interface_State Decoder_Interface_init()
    {
        dec_interface_State s = new dec_interface_State();

        /* allocate memory */
        s.decoder_State = Speech_Decode_Frame_init();

        Decoder_Interface_reset(s);
        return s;
    }

    /*
     * Decoder_Interface_exit
     * 
     * 
     * Parameters: state I: state structure
     * 
     * Function: The memory used for state memory is freed
     * 
     * Returns: Void
     */
    public static void Decoder_Interface_exit(dec_interface_State state)
    {

        /* free memory */
        Speech_Decode_Frame_exit(state.decoder_State);
    }

    /*
     * Decoder_Interface_Decode
     * 
     * 
     * Parameters: st B: state structure bits I: bit stream synth O: synthesized speech bfi I: bad frame indicator
     * 
     * Function: Decode bit stream to synthesized speech
     * 
     * Returns: Void
     */
    public static void Decoder_Interface_Decode(dec_interface_State s, byte[] bits, int bitsOffset, short[] synth, int synthOffset, int bfi)
    {
        int mode; /* AMR mode */
        int[] speech_mode = new int[]
        { MR475 }; /* speech mode */

        short[] prm = new short[PRMNO_MR122]; /* AMR parameters */

        int[] frame_type = new int[1]; /* frame type */

        short[] homing; /* pointer to homing frame */
        int homingIndex;
        short homingSize; /* frame size for homing frame */
        int i; /* counter */
        int resetFlag = 1; /* homing frame */

        short[] q_bit = new short[1];

        /*
         * extract mode information and frametype, octets to parameters
         */
        mode = decoderMMS(prm, 0, bits, bitsOffset, frame_type, 0, speech_mode, 0, q_bit, 0);
        // System.out.println("Type:"+mode+":"+s.reset_flag_old);
        if (bfi == 0)
        {
            bfi = 1 - q_bit[0];
        }

        if (bfi == 1)
        {
            if (mode <= MR122)
            {
                frame_type[0] = RX_SPEECH_BAD;
            }
            else if (frame_type[0] != RX_NO_DATA)
            {
                frame_type[0] = RX_SID_BAD;
                mode = s.prev_mode;
            }
        }
        else
        {
            if (frame_type[0] == RX_SID_FIRST || frame_type[0] == RX_SID_UPDATE)
            {
                mode = speech_mode[0];
            }
            else if (frame_type[0] == RX_NO_DATA)
            {
                mode = s.prev_mode;
            }
            /*
             * if no mode information guess one from the previous frame
             */
            if (frame_type[0] == RX_SPEECH_BAD)
            {
                mode = s.prev_mode;
                if (s.prev_ft >= RX_SID_FIRST)
                {
                    frame_type[0] = RX_SID_BAD;
                }
            }
        }

        /* test for homing frame */
        if (s.reset_flag_old == 1)
        {
            homingIndex = 0;
            switch (mode)
            {
                case MR122:
                    homing = dhf_MR122;
                    homingSize = 18;
                    break;

                case MR102:
                    homing = dhf_MR102;
                    homingSize = 12;
                    break;

                case MR795:
                    homing = dhf_MR795;
                    homingSize = 8;
                    break;

                case MR74:
                    homing = dhf_MR74;
                    homingSize = 7;
                    break;

                case MR67:
                    homing = dhf_MR67;
                    homingSize = 7;
                    break;

                case MR59:
                    homing = dhf_MR59;
                    homingSize = 7;
                    break;

                case MR515:
                    homing = dhf_MR515;
                    homingSize = 7;
                    break;

                case MR475:
                    homing = dhf_MR475;
                    homingSize = 7;
                    break;

                default:
                    homing = null;
                    homingSize = 0;
                    break;
            }

            for (i = 0; i < homingSize; i++)
            {
                resetFlag = prm[i] ^ homing[i + homingIndex];

                if (resetFlag != 0)
                {
                    break;
                }
            }
        }

        if ((resetFlag == 0) && (s.reset_flag_old != 0))
        {
            // System.out.println("Using EHF_MASK");
            for (i = 0; i < 160; i++)
            {
                synth[i + synthOffset] = EHF_MASK;
            }
        }
        else
        {
            // System.out.println("Speech_Decode_Frame");
            Speech_Decode_Frame(s.decoder_State, mode, prm, 0, frame_type[0], synth, synthOffset);
        }

        if (s.reset_flag_old == 0)
        {
            /* check whole frame */
            homingIndex = 0;
            switch (mode)
            {
                case MR122:
                    homing = dhf_MR122;
                    homingSize = PRMNO_MR122;
                    break;

                case MR102:
                    homing = dhf_MR102;
                    homingSize = PRMNO_MR102;
                    break;

                case MR795:
                    homing = dhf_MR795;
                    homingSize = PRMNO_MR795;
                    break;

                case MR74:
                    homing = dhf_MR74;
                    homingSize = PRMNO_MR74;
                    break;

                case MR67:
                    homing = dhf_MR67;
                    homingSize = PRMNO_MR67;
                    break;

                case MR59:
                    homing = dhf_MR59;
                    homingSize = PRMNO_MR59;
                    break;

                case MR515:
                    homing = dhf_MR515;
                    homingSize = PRMNO_MR515;
                    break;

                case MR475:
                    homing = dhf_MR475;
                    homingSize = PRMNO_MR475;
                    break;

                default:
                    homing = null;
                    homingSize = 0;
            }

            for (i = 0; i < homingSize; i++)
            {
                resetFlag = prm[i] ^ homing[i + homingIndex];

                if (resetFlag != 0)
                {
                    break;
                }
            }
        }

        /* reset decoder if current frame is a homing frame */
        if (resetFlag == 0)
        {
            Speech_Decode_Frame_reset(s.decoder_State);
        }
        s.reset_flag_old = (resetFlag == 0) ? 1 : 0;
        s.prev_ft = frame_type[0];
        s.prev_mode = mode;
    }
}
