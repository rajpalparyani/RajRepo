package com.telenav.dsr.amr.endpointor;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author yueyulin Mar 3, 2009
 */
class sp_dec implements Constants, interf_rom, rom_dec, rom_dec1
{
    /*
     * Declare structure types
     */

    public static final int SPEECH = 0;

    public static final int DTX = 1;

    public static final int DTX_MUTE = 2;

    /**
     * A global temp integer
     */
    private static int temp;

    /*
     * Decoder memory structure
     */
    public static final class Bgn_scdState
    {
        /* history vector of past synthesis speech energy */

        int[] frameEnergyHist = new int[L_ENERGYHIST];

        /* state flags */
        short bgHangover; /* counter; number of frames after last speech frame */

    }

    public static final class Cb_gain_averageState
    {

        int hangCount; /* counter; */

        /* history vector of past synthesis speech energy */

        int[] cbGainHistory = new int[L_CBGAINHIST];

        short hangVar; /* counter; */

    }

    public static final class lsp_avgState
    {

        int[] lsp_meanSave = new int[M]; /* Averaged LSPs saved for efficiency */

    }

    public static final class D_plsfState
    {

        int[] past_r_q = new int[M]; /* Past quantized prediction error, Q15 */

        int[] past_lsf_q = new int[M]; /* Past dequantized lsfs, Q15 */

    }

    public static final class ec_gain_pitchState
    {

        int[] pbuf = new int[5];

        int past_gain_pit;

        int prev_gp;
    }

    public static final class ec_gain_codeState
    {

        int[] gbuf = new int[5];

        int past_gain_code;

        int prev_gc;
    }

    public static final class gc_predState
    {
        /*
         * normal MA predictor memory, Q10 (contains 20*log10(quaErr))
         */

        int[] past_qua_en = new int[4];

        /*
         * MA predictor memory for MR122 mode, Q10 (contains log2(quaErr))
         */
        int[] past_qua_en_MR122 = new int[4];
    }

    public static final class ph_dispState
    {

        int[] gainMem = new int[PHDGAINMEMSIZE];

        int prevCbGain;

        int prevState;

        short lockFull;

        short onset;
    }

    public static final class dtx_decState
    {

        int dtxGlobalState; /* contains previous state */

        int log_en;

        int old_log_en;

        int pn_seed_rx;

        int[] lsp = new int[M];

        int[] lsp_old = new int[M];

        int[] lsf_hist = new int[M * DTX_HIST_SIZE];

        int[] lsf_hist_mean = new int[M * DTX_HIST_SIZE];

        int[] log_en_hist = new int[DTX_HIST_SIZE];

        int true_sid_period_inv;

        short since_last_sid;

        short lsf_hist_ptr;

        short log_pg_mean;

        short log_en_hist_ptr;

        short log_en_adjust;

        short dtxHangoverCount;

        short decAnaElapsedCount;

        short sid_frame;

        short valid_data;

        short dtxHangoverAdded;

        /* updated in main decoder */
        short data_updated; /* marker to know if CNI data is ever renewed */

    }

    public static final class AgcState
    {

        int past_gain;
    }

    public static final class Decoder_amrState
    {
        /* Excitation vector */

        protected int[] old_exc = new int[L_SUBFR + PIT_MAX + L_INTERPOL];

        protected int[] exc;

        protected int excIndex;

        protected int[] lsp_old = new int[M];

        /* Filter's memory */
        protected int[] mem_syn = new int[M];

        /* pitch sharpening */
        protected int sharp;

        protected int old_T0;

        /* Variable holding received ltpLag, used in background noise and BFI */
        protected int T0_lagBuff;

        /* Variables for the source characteristic detector (SCD) */
        protected int inBackgroundNoise;

        protected int voicedHangover;

        protected int[] ltpGainHistory = new int[9];

        /* Memories for bad frame handling */
        protected int[] excEnergyHist = new int[9];

        protected short prev_bf;

        protected short prev_pdf;

        protected short state;

        protected short nodataSeed;

        Bgn_scdState background_state = new Bgn_scdState();

        Cb_gain_averageState Cb_gain_averState = new Cb_gain_averageState();

        lsp_avgState lsp_avg_st = new lsp_avgState();

        D_plsfState lsfState = new D_plsfState();

        ec_gain_pitchState ec_gain_p_st = new ec_gain_pitchState();

        ec_gain_codeState ec_gain_c_st = new ec_gain_codeState();

        gc_predState pred_state = new gc_predState();

        ph_dispState ph_disp_st = new ph_dispState();

        dtx_decState dtxDecoderState = new dtx_decState();
    }

    public static final class Post_FilterState
    {

        int[] res2 = new int[L_SUBFR];

        int[] mem_syn_pst = new int[M];

        int[] synth_buf = new int[M + L_FRAME];

        int preemph_state_mem_pre;

        AgcState agc_state = new AgcState();
    }

    public static final class Post_ProcessState
    {

        int y2_hi;

        int y2_lo;

        int y1_hi;

        int y1_lo;

        int x0;

        int x1;
    }

    public static final class Speech_Decode_FrameState
    {

        Decoder_amrState decoder_amrState = new Decoder_amrState();

        Post_FilterState post_state = new Post_FilterState();

        Post_ProcessState postHP_state = new Post_ProcessState();
    }

    static void memset(short[] shorts, int offset, short value, int length)
    {
        if (shorts == null || offset >= shorts.length)
        {
            return;
        }
        shorts[offset] = value;
        for (int i = 1; i < length; i = temp)
        {
            temp = i << 1;
            if (temp > length)
            {
                temp = length;
            }
            System.arraycopy(shorts, offset, shorts, offset + i, temp - i);
        }
    }

    static void memset(int[] ints, int offset, int value, int length)
    {
        if (ints == null || offset >= ints.length)
        {
            return;
        }
        ints[offset] = value;
        for (int i = 1; i < length; i = temp)
        {
            temp = i << 1;
            if (temp > length)
            {
                temp = length;
            }
            System.arraycopy(ints, offset, ints, offset + i, temp - i);
        }
    }

    static void memcpy(int[] dest, int destOffset, int[] src, int srcOffset, int length)
    {
        if (dest == null || src == null)
        {
            return;
        }
        // for (int i = 0; i < length && destOffset + i < dest.length && srcOffset + i < src.length; i++) {
        // dest[i + destOffset] = src[i + srcOffset];
        // }

        System.arraycopy(src, srcOffset, dest, destOffset, length);

    }

    /*
     * CodAmrReset
     * 
     * 
     * Parameters: state B: state structure mode I: AMR mode
     * 
     * Function: Resets state memory
     * 
     * Returns: void
     */
    static void Decoder_amr_reset(Decoder_amrState state, int mode)
    {
        int i;

        /* Cb_gain_average_reset */
        memset(state.Cb_gain_averState.cbGainHistory, 0, 0, L_CBGAINHIST);
        state.Cb_gain_averState.hangVar = 0;
        state.Cb_gain_averState.hangCount = 0;

        /* Initialize static pointer */
        state.exc = state.old_exc;
        state.excIndex = PIT_MAX + L_INTERPOL;

        /* Static vectors to zero */
        memset(state.old_exc, 0, 0, PIT_MAX + L_INTERPOL);

        if (mode != MRDTX)
        {
            memset(state.mem_syn, 0, 0, M);
        }

        /* initialize pitch sharpening */
        state.sharp = SHARPMIN;
        state.old_T0 = 40;

        /* Initialize state.lsp_old [] */
        if (mode != MRDTX)
        {
            state.lsp_old[0] = 30000;
            state.lsp_old[1] = 26000;
            state.lsp_old[2] = 21000;
            state.lsp_old[3] = 15000;
            state.lsp_old[4] = 8000;
            state.lsp_old[5] = 0;
            state.lsp_old[6] = -8000;
            state.lsp_old[7] = -15000;
            state.lsp_old[8] = -21000;
            state.lsp_old[9] = -26000;
        }

        /* Initialize memories of bad frame handling */
        state.prev_bf = 0;
        state.prev_pdf = 0;
        state.state = 0;
        state.T0_lagBuff = 40;
        state.inBackgroundNoise = 0;
        state.voicedHangover = 0;

        if (mode != MRDTX)
        {
            memset(state.excEnergyHist, 0, 0, 9);
        }
        memset(state.ltpGainHistory, 0, 0, 9);

        if (mode != MRDTX)
        {
            state.lsp_avg_st.lsp_meanSave[0] = 1384;
            state.lsp_avg_st.lsp_meanSave[1] = 2077;
            state.lsp_avg_st.lsp_meanSave[2] = 3420;
            state.lsp_avg_st.lsp_meanSave[3] = 5108;
            state.lsp_avg_st.lsp_meanSave[4] = 6742;
            state.lsp_avg_st.lsp_meanSave[5] = 8122;
            state.lsp_avg_st.lsp_meanSave[6] = 9863;
            state.lsp_avg_st.lsp_meanSave[7] = 11092;
            state.lsp_avg_st.lsp_meanSave[8] = 12714;
            state.lsp_avg_st.lsp_meanSave[9] = 13701;
        }
        memset(state.lsfState.past_r_q, 0, 0, M);

        /* Past dequantized lsfs */
        state.lsfState.past_lsf_q[0] = 1384;
        state.lsfState.past_lsf_q[1] = 2077;
        state.lsfState.past_lsf_q[2] = 3420;
        state.lsfState.past_lsf_q[3] = 5108;
        state.lsfState.past_lsf_q[4] = 6742;
        state.lsfState.past_lsf_q[5] = 8122;
        state.lsfState.past_lsf_q[6] = 9863;
        state.lsfState.past_lsf_q[7] = 11092;
        state.lsfState.past_lsf_q[8] = 12714;
        state.lsfState.past_lsf_q[9] = 13701;

        for (i = 0; i < 5; i++)
        {
            state.ec_gain_p_st.pbuf[i] = 1640;
        }
        state.ec_gain_p_st.past_gain_pit = 0;
        state.ec_gain_p_st.prev_gp = 16384;

        for (i = 0; i < 5; i++)
        {
            state.ec_gain_c_st.gbuf[i] = 1;
        }
        state.ec_gain_c_st.past_gain_code = 0;
        state.ec_gain_c_st.prev_gc = 1;

        if (mode != MRDTX)
        {
            for (i = 0; i < NPRED; i++)
            {
                state.pred_state.past_qua_en[i] = MIN_ENERGY;
                state.pred_state.past_qua_en_MR122[i] = MIN_ENERGY_MR122;
            }
        }
        state.nodataSeed = 21845;

        /* Static vectors to zero */
        memset(state.background_state.frameEnergyHist, 0, 0, L_ENERGYHIST);

        /* Initialize hangover handling */
        state.background_state.bgHangover = 0;

        /* phDispReset */
        memset(state.ph_disp_st.gainMem, 0, 0, PHDGAINMEMSIZE);
        state.ph_disp_st.prevState = 0;
        state.ph_disp_st.prevCbGain = 0;
        state.ph_disp_st.lockFull = 0;
        state.ph_disp_st.onset = 0; /* assume no onset in start */

        if (mode != MRDTX)
        {
            state.dtxDecoderState.since_last_sid = 0;
            state.dtxDecoderState.true_sid_period_inv = 8192;
            state.dtxDecoderState.log_en = 3500;
            state.dtxDecoderState.old_log_en = 3500;

            /* low level noise for better performance in DTX handover cases */
            state.dtxDecoderState.pn_seed_rx = PN_INITIAL_SEED;

            /* Initialize state.lsp [] */
            state.dtxDecoderState.lsp[0] = 30000;
            state.dtxDecoderState.lsp[1] = 26000;
            state.dtxDecoderState.lsp[2] = 21000;
            state.dtxDecoderState.lsp[3] = 15000;
            state.dtxDecoderState.lsp[4] = 8000;
            state.dtxDecoderState.lsp[5] = 0;
            state.dtxDecoderState.lsp[6] = -8000;
            state.dtxDecoderState.lsp[7] = -15000;
            state.dtxDecoderState.lsp[8] = -21000;
            state.dtxDecoderState.lsp[9] = -26000;

            /* Initialize state.lsp_old [] */
            state.dtxDecoderState.lsp_old[0] = 30000;
            state.dtxDecoderState.lsp_old[1] = 26000;
            state.dtxDecoderState.lsp_old[2] = 21000;
            state.dtxDecoderState.lsp_old[3] = 15000;
            state.dtxDecoderState.lsp_old[4] = 8000;
            state.dtxDecoderState.lsp_old[5] = 0;
            state.dtxDecoderState.lsp_old[6] = -8000;
            state.dtxDecoderState.lsp_old[7] = -15000;
            state.dtxDecoderState.lsp_old[8] = -21000;
            state.dtxDecoderState.lsp_old[9] = -26000;
            state.dtxDecoderState.lsf_hist_ptr = 0;
            state.dtxDecoderState.log_pg_mean = 0;
            state.dtxDecoderState.log_en_hist_ptr = 0;

            /* initialize decoder lsf history */
            state.dtxDecoderState.lsf_hist[0] = 1384;
            state.dtxDecoderState.lsf_hist[1] = 2077;
            state.dtxDecoderState.lsf_hist[2] = 3420;
            state.dtxDecoderState.lsf_hist[3] = 5108;
            state.dtxDecoderState.lsf_hist[4] = 6742;
            state.dtxDecoderState.lsf_hist[5] = 8122;
            state.dtxDecoderState.lsf_hist[6] = 9863;
            state.dtxDecoderState.lsf_hist[7] = 11092;
            state.dtxDecoderState.lsf_hist[8] = 12714;
            state.dtxDecoderState.lsf_hist[9] = 13701;

            for (i = 1; i < DTX_HIST_SIZE; i++)
            {
                memcpy(state.dtxDecoderState.lsf_hist, M * i, state.dtxDecoderState.lsf_hist, 0, M);
            }
            memset(state.dtxDecoderState.lsf_hist_mean, 0, 0, M * DTX_HIST_SIZE);

            /* initialize decoder log frame energy */
            for (i = 0; i < DTX_HIST_SIZE; i++)
            {
                state.dtxDecoderState.log_en_hist[i] = state.dtxDecoderState.log_en;
            }
            state.dtxDecoderState.log_en_adjust = 0;
            state.dtxDecoderState.dtxHangoverCount = DTX_HANG_CONST;
            state.dtxDecoderState.decAnaElapsedCount = 31;
            state.dtxDecoderState.sid_frame = 0;
            state.dtxDecoderState.valid_data = 0;
            state.dtxDecoderState.dtxHangoverAdded = 0;
            state.dtxDecoderState.dtxGlobalState = DTX;
            state.dtxDecoderState.data_updated = 0;
        }
        return;
    }

    /*
     * rx_dtx_handler
     * 
     * 
     * Parameters: st.dtxGlobalState I: DTX state st.since_last_sid B: Frames after last SID frame st.data_updated I:
     * SID update flag st.decAnaElapsedCount B: state machine that synch with the GSMEFR txDtx machine
     * st.dtxHangoverAdded B: DTX hangover st.sid_frame O: SID frame indicator st.valid_data O: Vaild data indicator
     * frame_type O: Frame type
     * 
     * Function: Find the new DTX state
     * 
     * Returns: DTXStateType DTX, DTX_MUTE or SPEECH
     */
    static int rx_dtx_handler(dtx_decState st, int frame_type)
    {
        int newState;
        int encState;

        /* DTX if SID frame or previously in DTX{_MUTE} and (NO_RX OR BAD_SPEECH) */
        if (table_SID[frame_type] != 0 || ((st.dtxGlobalState != SPEECH) && table_speech_bad[frame_type] != 0))
        {
            newState = DTX;

            /* stay in mute for these input types */
            if ((st.dtxGlobalState == DTX_MUTE) && table_mute[frame_type] != 0)
            {
                newState = DTX_MUTE;
            }

            /*
             * evaluate if noise parameters are too old since_last_sid is reset when CN parameters have been updated
             */
            st.since_last_sid += 1;

            /* no update of sid parameters in DTX for a long while */
            if ((frame_type != RX_SID_UPDATE) & (st.since_last_sid > DTX_MAX_EMPTY_THRESH))
            {
                newState = DTX_MUTE;
            }
        }
        else
        {
            newState = SPEECH;
            st.since_last_sid = 0;
        }

        /*
         * reset the decAnaElapsed Counter when receiving CNI data the first time, to robustify counter missmatch after
         * handover this might delay the bwd CNI analysis in the new decoder slightly.
         */
        if ((st.data_updated == 0) & (frame_type == RX_SID_UPDATE))
        {
            st.decAnaElapsedCount = 0;
        }

        /*
         * update the SPE-SPD DTX hangover synchronization to know when SPE has added dtx hangover
         */
        st.decAnaElapsedCount += 1;
        st.dtxHangoverAdded = 0;
        encState = SPEECH;

        if (table_DTX[frame_type] != 0)
        {
            encState = DTX;
            if ((frame_type == RX_NO_DATA) & (newState == SPEECH))
            {
                encState = SPEECH;
            }
        }

        if (encState == SPEECH)
        {
            st.dtxHangoverCount = DTX_HANG_CONST;
        }
        else
        {
            if (st.decAnaElapsedCount > DTX_ELAPSED_FRAMES_THRESH)
            {
                st.dtxHangoverAdded = 1;
                st.decAnaElapsedCount = 0;
                st.dtxHangoverCount = 0;
            }
            else if (st.dtxHangoverCount == 0)
            {
                st.decAnaElapsedCount = 0;
            }
            else
            {
                st.dtxHangoverCount -= 1;
            }
        }

        if (newState != SPEECH)
        {
            /*
             * DTX or DTX_MUTE CN data is not in a first SID, first SIDs are marked as SID_BAD but will do backwards
             * analysis if a hangover period has been added according to the state machine above
             */
            st.sid_frame = 0;
            st.valid_data = 0;

            if (frame_type == RX_SID_FIRST)
            {
                st.sid_frame = 1;
            }
            else if (frame_type == RX_SID_UPDATE)
            {
                st.sid_frame = 1;
                st.valid_data = 1;
            }
            else if (frame_type == RX_SID_BAD)
            {
                st.sid_frame = 1;

                /* use old data */
                st.dtxHangoverAdded = 0;
            }
        }

        /* newState is used by both SPEECH AND DTX synthesis routines */
        return newState;
    }

    /*
     * Lsf_lsp
     * 
     * 
     * Parameters: lsf I: vector of LSFs lsp O: vector of LSPs
     * 
     * Function: Transformation lsf to lsp, order M
     * 
     * Returns: void
     */
    static void Lsf_lsp(int lsf[], int lsfOffset, int lsp[], int lspOffset)
    {
        int i, ind, offset, tmp;

        for (i = 0; i < M; i++)
        {
            /* ind = b8-b15 of lsf[i] */
            ind = lsf[i + lsfOffset] >> 8;

            /* offset = b0-b7 of lsf[i] */
            offset = lsf[i + lsfOffset] & 0x00ff;

            /* lsp[i] = table[ind]+ ((table[ind+1]-table[ind])*offset) / 256 */
            tmp = ((cos_table[ind + 1] - cos_table[ind]) * offset) << 1;
            lsp[i + lspOffset] = cos_table[ind] + (tmp >> 9);
        }
        // System.out.printf("[Lsf_lsp]sumLsf:%d,sumLsp:%d\n", sumLsf, sumLsp);
        return;
    }

    /*
     * D_plsf_3
     * 
     * 
     * Parameters: st.past_lsf_q I: Past dequantized LFSs st.past_r_q B: past quantized residual mode I: AMR mode bfi B:
     * bad frame indicator indice I: quantization indices of 3 submatrices, Q0 lsp1_q O: quantized 1st LSP vector
     * 
     * Function: Decodes the LSP parameters using the received quantization indices. 1st order MA prediction and split
     * by 3 vector quantization (split-VQ)
     * 
     * Returns: void
     */
    static void D_plsf_3(D_plsfState st, int mode, int bfi, short[] indice, int indiceIndex, int[] lsp1_q)
    {
        int[] lsf1_r = new int[M];
        int[] lsf1_q = new int[M];
        int i = 0;
        int index = 0;
        temp = 0;
        int[] p_cb1 = null;
        int p_cb1Index = 0;
        int[] p_cb2 = null;
        int p_cb2Index = 0;
        int[] p_cb3 = null;
        int p_cb3Index = 0;
        int[] p_dico = null;
        int p_dicoIndex = 0;

        /* if bad frame */
        if (bfi != 0)
        {
            /* use the past LSFs slightly shifted towards their mean */
            for (i = 0; i < M; i++)
            {
                /* lsfi_q[i] = ALPHA*past_lsf_q[i] + ONE_ALPHA*meanLsf[i]; */
                lsf1_q[i] = ((st.past_lsf_q[i] * ALPHA) >> 15) + ((mean_lsf_3[i] * ONE_ALPHA) >> 15);
            }

            /* estimate past quantized residual to be used in next frame */
            if (mode != MRDTX)
            {
                for (i = 0; i < M; i++)
                {
                    /* temp = meanLsf[i] + pastR2_q[i] * pred_fac; */
                    temp = mean_lsf_3[i] + ((st.past_r_q[i] * pred_fac[i]) >> 15);
                    st.past_r_q[i] = lsf1_q[i] - temp;
                }
            }
            else
            {
                for (i = 0; i < M; i++)
                {
                    /* temp = meanLsf[i] + pastR2_q[i]; */
                    temp = mean_lsf_3[i] + st.past_r_q[i];
                    st.past_r_q[i] = lsf1_q[i] - temp;
                }
            }
        } /* if good LSFs received */
        else
        {
            if ((mode == MR475) | (mode == MR515))
            {
                /* MR475, MR515 */
                p_cb1 = dico1_lsf_3;
                p_cb2 = dico2_lsf_3;
                p_cb3 = mr515_3_lsf;
            }
            else if (mode == MR795)
            {
                /* MR795 */
                p_cb1 = mr795_1_lsf;
                p_cb2 = dico2_lsf_3;
                p_cb3 = dico3_lsf_3;
            }
            else
            {
                /* MR59, MR67, MR74, MR102, MRDTX */
                p_cb1 = dico1_lsf_3;
                p_cb2 = dico2_lsf_3;
                p_cb3 = dico3_lsf_3;
            }

            /* decode prediction residuals from 3 received indices */
            index = indice[indiceIndex++];
            // p_dico = &p_cb1[index + index + index];
            p_dico = p_cb1;
            p_dicoIndex = index + index + index;
            index = indice[indiceIndex++];
            lsf1_r[0] = p_dico[p_dicoIndex++];
            lsf1_r[1] = p_dico[p_dicoIndex++];
            lsf1_r[2] = p_dico[p_dicoIndex++];

            if ((mode == MR475) | (mode == MR515))
            {
                /* MR475, MR515 only using every second entry */
                index = index << 1;
            }
            // p_dico = &p_cb2[index + index + index];
            p_dico = p_cb2;
            p_dicoIndex = index + index + index;
            index = indice[indiceIndex++];
            lsf1_r[3] = p_dico[p_dicoIndex++];
            lsf1_r[4] = p_dico[p_dicoIndex++];
            lsf1_r[5] = p_dico[p_dicoIndex++];
            // p_dico = &p_cb3[index << 2];
            p_dico = p_cb3;
            p_dicoIndex = index << 2;
            lsf1_r[6] = p_dico[p_dicoIndex++];
            lsf1_r[7] = p_dico[p_dicoIndex++];
            lsf1_r[8] = p_dico[p_dicoIndex++];
            lsf1_r[9] = p_dico[p_dicoIndex++];

            /* Compute quantized LSFs and update the past quantized residual */
            if (mode != MRDTX)
            {
                for (i = 0; i < M; i++)
                {
                    lsf1_q[i] = lsf1_r[i] + (mean_lsf_3[i] + ((st.past_r_q[i] * pred_fac[i]) >> 15));
                }
                memcpy(st.past_r_q, 0, lsf1_r, 0, M);
            }
            else
            {
                for (i = 0; i < M; i++)
                {
                    lsf1_q[i] = lsf1_r[i] + (mean_lsf_3[i] + st.past_r_q[i]);
                }
                memcpy(st.past_r_q, 0, lsf1_r, 0, M);
            }
        }

        /* verification that LSFs has minimum distance of LSF_GAP Hz */
        temp = LSF_GAP;

        for (i = 0; i < M; i++)
        {
            if (lsf1_q[i] < temp)
            {
                lsf1_q[i] = temp;
            }
            temp = lsf1_q[i] + LSF_GAP;
        }
        memcpy(st.past_lsf_q, 0, lsf1_q, 0, M);

        /* convert LSFs to the cosine domain */
        Lsf_lsp(lsf1_q, 0, lsp1_q, 0);
        return;
    }

    /*
     * pseudonoise
     * 
     * 
     * Parameters: shift_reg B: Old CN generator shift register state no_bits I: Number of bits
     * 
     * Function: pseudonoise
     * 
     * Returns: noise_bits
     */
    static int pseudonoise(int[] shift_reg, int offset, int no_bits)
    {
        int noise_bits, Sn, i;
        int s_reg;

        s_reg = shift_reg[offset];
        noise_bits = 0;

        for (i = 0; i < no_bits; i++)
        {
            /* State n == 31 */
            Sn = s_reg & 0x00000001;

            /* State n == 3 */
            if ((s_reg & 0x10000000) != 0)
            {
                Sn = Sn ^ 0x1;
            }
            noise_bits = (noise_bits << 1) | (s_reg & 1);
            s_reg = s_reg >> 1;

            if ((Sn & 1) != 0)
            {
                s_reg = s_reg | 0x40000000;
            }
        }
        shift_reg[offset] = s_reg;
        return noise_bits;
    }

    /*
     * Lsp_lsf
     * 
     * 
     * Parameters: lsp I: LSP vector (range: -1<=val<1) lsf O: LSF vector Old CN generator shift register state
     * 
     * Function: Transformation lsp to lsf, LPC order M lsf[i] = arccos(lsp[i])/(2*pi)
     * 
     * Returns: void
     */
    static void Lsp_lsf(int lsp[], int lspOffset, int lsf[], int lsfOffset)
    {
        int i, ind = 63; /* begin at end of table -1 */

        for (i = M - 1; i >= 0; i--)
        {
            /* find value in table that is just greater than lsp[i] */
            while (cos_table[ind] < lsp[i + lspOffset])
            {
                ind--;
            }
            lsf[i + lsfOffset] = ((((lsp[i + lspOffset] - cos_table[ind]) * acos_slope[ind]) + 0x800) >> 12) + (ind << 8);
        }
        return;
    }

    /*
     * Reorder_lsf
     * 
     * 
     * Parameters: lsf B: vector of LSFs (range: 0<=val<=0.5) min_dist I: minimum required distance
     * 
     * Function: Make sure that the LSFs are properly ordered and to keep a certain minimum distance between adjacent
     * LSFs. LPC order = M.
     * 
     * Returns: void
     */
    static void Reorder_lsf(int[] lsf, int lsfOffset, int min_dist)
    {
        int lsf_min, i;

        lsf_min = min_dist;

        for (i = 0; i < M; i++)
        {
            if (lsf[i + lsfOffset] < lsf_min)
            {
                lsf[i + lsfOffset] = lsf_min;
            }
            lsf_min = lsf[i + lsfOffset] + min_dist;
        }
        // System.out.printf("[Recorder_lsf]:lsfSum:%d,mid_dist:%d\n",lsfSum,min_dist);
    }

    /*
     * Get_lsp_pol
     * 
     * 
     * Parameters: lsp I: line spectral frequencies f O: polynomial F1(z) or F2(z)
     * 
     * Function: Find the polynomial F1(z) or F2(z) from the LSPs.
     * 
     * F1(z) = product ( 1 - 2 lsp[i] z^-1 + z^-2 ) i=0,2,4,6,8 F2(z) = product ( 1 - 2 lsp[i] z^-1 + z^-2 ) i=1,3,5,7,9
     * 
     * where lsp[] is the LSP vector in the cosine domain.
     * 
     * The expansion is performed using the following recursion:
     * 
     * f[0] = 1 b = -2.0 * lsp[0] f[1] = b for i=2 to 5 do b = -2.0 * lsp[2*i-2]; f[i] = b*f[i-1] + 2.0*f[i-2]; for
     * j=i-1 down to 2 do f[j] = f[j] + b*f[j-1] + f[j-2]; f[1] = f[1] + b;
     * 
     * Returns: void
     */
    static void Get_lsp_pol(int[] lsp, int lspOffset, int[] f, int fOffset)
    {
        int f0, f1, f2, f3, f4, f5;
        int l1, l2, l3, l4;

        /* f[0] = 1.0; */
        f0 = 16777216;

        /* f1 = *lsp * -1024; */
        f1 = -lsp[0 + lspOffset] << 10;
        l1 = lsp[2 + lspOffset];
        l2 = lsp[4 + lspOffset];
        l3 = lsp[6 + lspOffset];
        l4 = lsp[8 + lspOffset];
        f2 = f0 << 1;
        f2 -= (((f1 >> 16) * l1) + (((f1 & 0xFFFE) * l1) >> 16)) << 2;
        f1 -= l1 << 10;
        f3 = f1 << 1;
        f3 -= (((f2 >> 16) * l2) + (((f2 & 0xFFFE) * l2) >> 16)) << 2;
        f2 += f0;
        f2 -= (((f1 >> 16) * l2) + (((f1 & 0xFFFE) * l2) >> 16)) << 2;
        f1 -= l2 << 10;
        f4 = f2 << 1;
        f4 -= (((f3 >> 16) * l3) + (((f3 & 0xFFFE) * l3) >> 16)) << 2;
        f3 += f1;
        f3 -= (((f2 >> 16) * l3) + (((f2 & 0xFFFE) * l3) >> 16)) << 2;
        f2 += f0;
        f2 -= (((f1 >> 16) * l3) + (((f1 & 0xFFFE) * l3) >> 16)) << 2;
        f1 -= l3 << 10;
        f5 = f3 << 1;
        f5 -= (((f4 >> 16) * l4) + (((f4 & 0xFFFE) * l4) >> 16)) << 2;
        f4 += f2;
        f4 -= (((f3 >> 16) * l4) + (((f3 & 0xFFFE) * l4) >> 16)) << 2;
        f3 += f1;
        f3 -= (((f2 >> 16) * l4) + (((f2 & 0xFFFE) * l4) >> 16)) << 2;
        f2 += f0;
        f2 -= (((f1 >> 16) * l4) + (((f1 & 0xFFFE) * l4) >> 16)) << 2;
        f1 -= l4 << 10;
        f[0 + fOffset] = f0;
        f[1 + fOffset] = f1;
        f[2 + fOffset] = f2;
        f[3 + fOffset] = f3;
        f[4 + fOffset] = f4;
        f[5 + fOffset] = f5;
        return;
    }

    /*
     * Lsp_Az
     * 
     * 
     * Parameters: lsp I: Line spectral frequencies a O: Predictor coefficients
     * 
     * Function: Converts from the line spectral pairs (LSP) to LP coefficients, for a 10th order filter.
     * 
     * Find the coefficients of F1(z) and F2(z) Multiply F1(z) by 1+z^{-1} and F2(z) by 1-z^{-1} A(z) = ( F1(z) + F2(z)
     * ) / 2
     * 
     * Returns: void
     */
    static void Lsp_Az(int lsp[], int lspOffset, int a[], int aOffset)
    {
        int[] f1 = new int[6];
        int[] f2 = new int[6];
        int T0, i, j;

        Get_lsp_pol(lsp, 0, f1, 0);
        Get_lsp_pol(lsp, 1, f2, 0);

        for (i = 5; i > 0; i--)
        {
            f1[i] += f1[i - 1];
            f2[i] -= f2[i - 1];
        }
        a[aOffset] = 4096;

        for (i = 1, j = 10; i <= 5; i++, j--)
        {
            T0 = f1[i] + f2[i];
            a[i + aOffset] = (short) (T0 >> 13); /* emulate fixed point bug */
            if ((T0 & 4096) != 0)
            {
                a[i + aOffset]++;
            }
            T0 = f1[i] - f2[i];
            a[j + aOffset] = (short) (T0 >> 13); /* emulate fixed point bug */

            if ((T0 & 4096) != 0)
            {
                a[j + aOffset]++;
            }
        }
        return;
    }

    /*
     * A_Refl
     * 
     * 
     * Parameters: a I: Directform coefficients refl O: Reflection coefficients
     * 
     * Function: Converts from the directform coefficients to reflection coefficients
     * 
     * Returns: void
     */
    static void A_Refl(int a[], int aOffset, int refl[], int reflOffset)
    {
        /* local variables */
        int normShift;
        int[] aState = new int[M];
        int[] bState = new int[M];
        int normProd, acc, temp, mult, scale, i, j;

        /* initialize states */
        memcpy(aState, 0, a, aOffset, M);

        boolean isMemset = false;

        /* backward Levinson recursion */
        for (i = M - 1; i >= 0; i--)
        {
            if (Math.abs(aState[i]) >= 4096)
            {
                memset(refl, 0, 0, M);
                return;
            }
            refl[i] = aState[i] << 3;
            temp = (refl[i] * refl[i]) << 1;
            acc = (MAX_32 - temp);
            normShift = 0;
            if (acc != 0)
            {
                temp = acc;
                while ((temp & 0x40000000) == 0)
                {
                    normShift++;
                    temp = temp << 1;
                }
            }
            else
            {
                normShift = 0;
            }
            scale = 15 - normShift;
            acc = (acc << normShift);
            temp = (acc + (int) 0x00008000L);

            if (temp > 0)
            {
                normProd = temp >> 16;
                mult = 0x20000000 / normProd;
            }
            else
            {
                mult = 16384;
            }

            for (j = 0; j < i; j++)
            {
                acc = aState[j] << 16;
                acc -= (refl[i] * aState[i - j - 1]) << 1;
                temp = (acc + (int) 0x00008000L) >> 16;
                temp = (mult * temp) << 1;

                if (scale > 0)
                {
                    if ((temp & ((int) 1 << (scale - 1))) != 0)
                    {
                        temp = (temp >> scale) + 1;
                    }
                    else
                    {
                        temp = (temp >> scale);
                    }
                }
                else
                {
                    temp = (temp >> scale);
                }

                if (Math.abs(temp) > 32767)
                {
                    memset(refl, 0, 0, M);
                    return;
                }
                bState[j] = temp;
            }
            memcpy(aState, 0, bState, 0, i);
        }
    }

    /*
     * Log2_norm
     * 
     * 
     * Parameters: x I: input value exp I: exponent exponent O: Integer part of Log2. (range: 0<=val<=30) fraction O:
     * Fractional part of Log2. (range: 0<=val<1)
     * 
     * Function: Computes log2
     * 
     * Computes log2(L_x, exp), where L_x is positive and normalized, and exp is the normalisation exponent If L_x is
     * negative or zero, the result is 0.
     * 
     * The function Log2(L_x) is approximated by a table and linear interpolation. The following steps are used to
     * compute Log2(L_x)
     * 
     * exponent = 30-normExponent i = bit25-b31 of L_x; 32<=i<=63 (because of normalization). a = bit10-b24 i -=32
     * fraction = table[i]<<16 - (table[i] - table[i+1]) * a * 2
     * 
     * Returns: void
     */
    static void Log2_norm(int x, int exp, int[] exponent, int exponentOffset, int[] fraction, int fractionOffset)
    {
        int y, i, a;

        if (x <= 0)
        {
            exponent[exponentOffset] = 0;
            fraction[fractionOffset] = 0;
            return;
        }

        /* Extract b25-b31 */
        i = x >> 25;
        i = i - 32;

        /* Extract b10-b24 of fraction */
        a = x >> 9;
        a = a & 0xFFFE; /* 2a */

        /* fraction */
        y = (log2_table[i] << 16) - a * (log2_table[i] - log2_table[i + 1]);
        fraction[fractionOffset] = y >> 16;
        exponent[exponentOffset] = 30 - exp;
        return;
    }

    /*
     * Log2
     * 
     * 
     * Parameters: x I: input value exponent O: Integer part of Log2. (range: 0<=val<=30) fraction O: Fractional part of
     * Log2. (range: 0<=val<1)
     * 
     * Function: Computes log2(L_x) If x is negative or zero, the result is 0.
     * 
     * Returns: void
     */
    static void Log2(int x, int[] exponent, int exponentOffset, int[] fraction, int fractionOffset)
    {
        int tmp, exp = 0;

        if (x != 0)
        {
            tmp = x;
            while (((tmp & 0x80000000) ^ ((tmp & 0x40000000) << 1)) == 0)
            {
                exp++;
                tmp = tmp << 1;
            }
        }
        Log2_norm(x << exp, exp, exponent, exponentOffset, fraction, fractionOffset);
        // System.out.printf("x:%d[exponent:%d][fraction:%d]\n",x,exponent[exponentOffset],fraction[fractionOffset] );
    }

    /*
     * Pow2
     * 
     * 
     * Parameters: exponent I: Integer part. (range: 0<=val<=30) fraction O: Fractional part. (range: 0.0<=val<1.0)
     * 
     * Function: pow(2.0, exponent.fraction)
     * 
     * The function Pow2(L_x) is approximated by a table and linear interpolation.
     * 
     * i = bit10-b15 of fraction, 0 <= i <= 31 a = biT0-b9 of fraction x = table[i]<<16 - (table[i] - table[i+1]) * a *
     * 2 x = L_x >> (30-exponent) (with rounding)
     * 
     * Returns: result (range: 0<=val<=0x7fffffff)
     */
    static int Pow2(int exponent, int fraction)
    {
        int i, a, tmp, x, exp;

        /* Extract b10-b16 of fraction */
        i = fraction >> 10;

        /* Extract b0-b9 of fraction */
        a = (fraction << 5) & 0x7fff;

        /* table[i] << 16 */
        x = pow2_table[i] << 16;

        /* table[i] - table[i+1] */
        tmp = pow2_table[i] - pow2_table[i + 1];

        /* L_x -= tmp*a*2 */
        x -= (tmp * a) << 1;

        if (exponent >= -1)
        {
            exp = (30 - exponent);

            /* Rounding */
            if ((x & ((int) 1 << (exp - 1))) != 0)
            {
                x = (x >> exp) + 1;
            }
            else
            {
                x = x >> exp;
            }
        }
        else
        {
            x = 0;
        }
        // System.out.printf("exponent:%d,fraction:%d,x:%d\n",exponent,fraction,x);
        return (x);
    }

    /*
     * Build_CN_code
     * 
     * 
     * Parameters: seed B: Old CN generator shift register state cod O: Generated CN fixed codebook vector
     * 
     * Function: Generate CN fixed codebook vector
     * 
     * Returns: void
     */
    static void Build_CN_code(int[] seed, int seedOffset, int[] cod)
    {
        int i, j, k;

        memset(cod, 0, 0, L_SUBFR);

        for (k = 0; k < 10; k++)
        {
            i = pseudonoise(seed, 0, 2); /* generate pulse position */
            i = (i * 20) >> 1;
            i = (i + k);
            j = pseudonoise(seed, 0, 1); /* generate sign */

            if (j > 0)
            {
                cod[i] = 4096;
            }
            else
            {
                cod[i] = -4096;
            }
        }
        return;
    }

    /*
     * Build_CN_param
     * 
     * 
     * Parameters: seed B: Old CN generator shift register state nParam I: number of params paramSizeTable I: size of
     * params parm O: CN Generated params
     * 
     * Function: Generate parameters for comfort noise generation
     * 
     * Returns: void
     */
    static void Build_CN_param(short[] seed, int seedOffset, int mode, short[] parm)
    {
        int i;
        int[] p;
        int pIndex;

        seed[seedOffset] = (short) ((seed[seedOffset] * 31821) + 13849);
        // p = &window_200_40[ * seed & 0x7F];
        p = window_200_40;
        pIndex = seed[seedOffset] & 0x7F;

        switch (mode)
        {
            case MR122:
                for (i = 0; i < PRMNO_MR122; i++)
                {
                    parm[i] = (short) (p[pIndex++] & ~(0xFFFF << bitno_MR122[i]));
                }
                break;

            case MR102:
                for (i = 0; i < PRMNO_MR102; i++)
                {
                    parm[i] = (short) (p[pIndex++] & ~(0xFFFF << bitno_MR102[i]));
                }
                break;

            case MR795:
                for (i = 0; i < PRMNO_MR795; i++)
                {
                    parm[i] = (short) (p[pIndex++] & ~(0xFFFF << bitno_MR795[i]));
                }
                break;

            case MR74:
                for (i = 0; i < PRMNO_MR74; i++)
                {
                    parm[i] = (short) (p[pIndex++] & ~(0xFFFF << bitno_MR74[i]));
                }
                break;

            case MR67:
                for (i = 0; i < PRMNO_MR67; i++)
                {
                    parm[i] = (short) (p[pIndex++] & ~(0xFFFF << bitno_MR67[i]));
                }
                break;

            case MR59:
                for (i = 0; i < PRMNO_MR59; i++)
                {
                    parm[i] = (short) (p[pIndex++] & ~(0xFFFF << bitno_MR59[i]));
                }
                break;

            case MR515:
                for (i = 0; i < PRMNO_MR515; i++)
                {
                    parm[i] = (short) (p[pIndex++] & ~(0xFFFF << bitno_MR515[i]));
                }
                break;

            case MR475:
                for (i = 0; i < PRMNO_MR475; i++)
                {
                    parm[i] = (short) (p[pIndex++] & ~(0xFFFF << bitno_MR475[i]));
                }
                break;
        }
    }

    /*
     * static void printSum(int b[],int offset,int length){ int sum = 0; for(int i = 0;i < length;i ++){ sum += b[offset
     * + i]; } System.out.printf("Sum is %d\n",sum); }
     */

    /*
     * Syn_filt
     * 
     * 
     * Parameters: a I: prediction coefficients [M+1] x I: input signal y O: output signal lg I: size of filtering mem
     * B: memory associated with this filtering update I: 0=no update, 1=update of memory.
     * 
     * Function: Perform synthesis filtering through 1/A(z).
     * 
     * Returns: void
     */
    static int Syn_filt(int a[], int aOffset, int x[], int xOffset, int y[], int yOffset, int lg, int mem[], int memOffset, int update)
    {
        int[] tmp = new int[50]; /* malloc is slow */
        int s, a0, overflow = 0;
        int[] yy;
        int yyIndex;
        int yy_limitIndex;

        /* Copy mem[] to yy[] */
        memcpy(tmp, 0, mem, 0, 10);
        yy = tmp;
        yyIndex = M;
        yy_limitIndex = lg + M;
        a0 = a[aOffset];

        /* Do the filtering. */
        while (yyIndex < yy_limitIndex)
        {

            s = x[xOffset++] * a0;
            s -= yy[yyIndex - 1] * a[1 + aOffset];
            s -= yy[yyIndex - 2] * a[2 + aOffset];
            s -= yy[yyIndex - 3] * a[3 + aOffset];
            s -= yy[yyIndex - 4] * a[4 + aOffset];
            s -= yy[yyIndex - 5] * a[5 + aOffset];
            s -= yy[yyIndex - 6] * a[6 + aOffset];
            s -= yy[yyIndex - 7] * a[7 + aOffset];
            s -= yy[yyIndex - 8] * a[8 + aOffset];
            s -= yy[yyIndex - 9] * a[9 + aOffset];
            s -= yy[yyIndex - 10] * a[10 + aOffset];
            if (Math.abs(s) < 0x7ffffff)
            {
                yy[yyIndex] = (s + 0x800) >> 12;
            }
            else if (s > 0)
            {
                yy[yyIndex] = 32767;
                overflow = 1;
            }
            else
            {
                yy[yyIndex] = -32768;
                overflow = 1;
            }
            yyIndex++;
        }
        memcpy(y, yOffset, tmp, M, lg);

        /* Update of memory if update==1 */
        if (update != 0)
        {
            memcpy(mem, memOffset, y, yOffset + lg - M, 10);
        }
        // printSum(y, yOffset, lg);
        // printSum(mem, memOffset, 10);
        // printSum(a,aOffset,10);System.out.println("aOffset is "+aOffset);
        return overflow;
    }

    /*
     * Syn_filt_overflow
     * 
     * 
     * Parameters: a I: prediction coefficients [M+1] x I: input signal y O: output signal lg I: size of filtering mem
     * B: memory associated with this filtering update I: 0=no update, 1=update of memory.
     * 
     * Function: Perform synthesis filtering through 1/A(z). Saturate after every multiplication. Returns: void
     */
    static void Syn_filt_overflow(int a[], int aOffset, int x[], int xOffset, int y[], int yOffset, int lg, int mem[], int memOffset, int update)
    {
        int[] tmp = new int[50]; /* malloc is slow */
        int i, j, s, a0;
        int[] yy;
        int yyIndex;

        /* Copy mem[] to yy[] */
        memcpy(tmp, 0, mem, memOffset, 10);
        yy = tmp;
        yyIndex = M;
        a0 = a[aOffset];

        /* Do the filtering. */
        for (i = 0; i < lg; i++)
        {
            s = x[i + xOffset] * a0;

            for (j = 1; j <= M; j++)
            {
                s -= a[j + aOffset] * yy[yyIndex - j];
                if (s > 1073741823)
                {
                    s = 1073741823;
                }
                else if (s < -1073741824)
                {
                    s = -1073741824;
                }
            }

            if (Math.abs(s) < 0x7FFE800)
            {
                yy[yyIndex] = (s + 0x800) >> 12;
            }
            else if (s > 0)
            {
                yy[yyIndex] = 32767;
            }
            else
            {
                yy[yyIndex] = -32768;
            }
            yyIndex++;
        }
        memcpy(y, yOffset, tmp, M, lg);

        /* Update of memory if update==1 */
        if (update != 0)
        {
            memcpy(mem, memOffset, y, yOffset + lg - M, 10);
        }
        return;
    }

    /*
     * dtx_dec
     * 
     * 
     * Parameters: st B: DTX state struct mem_syn I: AMR decoder state lsfState B: LSF state struct
     * pred_state.past_qua_en O: table of past quantized energies pred_state.past_qua_en_MR122 O: table of past
     * quantized energies MR122 averState.hangVar O: averState.hangCount O: hangover variable new_state I: new DTX state
     * mode I: AMR mode parm I: vector of synthesis parameters synth O: synthesised speech A_t O: decoded LP filter in 4
     * subframes
     * 
     * Function: DTX
     * 
     * Returns: void
     */
    static void dtx_dec(dtx_decState st, int mem_syn[], int mem_synOffset, D_plsfState lsfState, gc_predState pred_state,
            Cb_gain_averageState averState, int new_state, int mode, short parm[], int synth[], int A_t[])
    {
        int[] ex = new int[L_SUBFR];
        int[] acoeff = new int[11];
        int[] acoeff_variab = new int[M + 1];
        int[] lsp_int = new int[M];
        int[] refl = new int[M];
        int[] lsf = new int[M];
        int[] lsf_int = new int[M];
        int[] lsf_int_variab = new int[M];
        int[] lsp_int_variab = new int[M];
        int i, j, int_fac, log_en_int, pred_err, log_pg_e = 0, log_pg_m = 0, log_pg;
        int negative, lsf_mean, lsf_variab_index, lsf_variab_factor, ptr;
        short log_en_index, log_en_int_e, log_en_int_m, level, ma_pred_init, tmp_int_length;

        if ((st.dtxHangoverAdded != 0) & (st.sid_frame != 0))
        {
            /*
             * sidFirst after dtx hangover period or sidUpd after dtxhangover
             */
            /* set log_en_adjust to correct value */
            st.log_en_adjust = dtx_log_en_adjust[mode];
            ptr = st.lsf_hist_ptr + M;

            if (ptr == 80)
            {
                ptr = 0;
            }
            memcpy(st.lsf_hist, ptr, st.lsf_hist, st.lsf_hist_ptr, M);
            ptr = st.log_en_hist_ptr + 1;

            if (ptr == DTX_HIST_SIZE)
            {
                ptr = 0;
            }
            st.log_en_hist[ptr] = st.log_en_hist[st.log_en_hist_ptr]; /* Q11 */

            /*
             * compute mean log energy and lsp from decoded signal (SID_FIRST)
             */
            st.log_en = 0;
            memset(lsf, 0, 0, M);

            /* average energy and lsp */
            for (i = 0; i < DTX_HIST_SIZE; i++)
            {
                st.log_en = st.log_en + (st.log_en_hist[i] >> 3);

                for (j = 0; j < M; j++)
                {
                    lsf[j] += st.lsf_hist[i * M + j];
                }
            }

            for (j = 0; j < M; j++)
            {
                lsf[j] = lsf[j] >> 3; /* divide by 8 */
            }
            Lsf_lsp(lsf, 0, st.lsp, 0);

            /*
             * make log_en speech coder mode independent added again later before synthesis
             */
            st.log_en = st.log_en - st.log_en_adjust;

            /* compute lsf variability vector */
            memcpy(st.lsf_hist_mean, 0, st.lsf_hist, 0, 80);

            for (i = 0; i < M; i++)
            {
                lsf_mean = 0;

                /* compute mean lsf */
                for (j = 0; j < 8; j++)
                {
                    lsf_mean += st.lsf_hist_mean[i + j * M];
                }
                lsf_mean = lsf_mean >> 3;

                /*
                 * subtract mean and limit to within reasonable limits moreover the upper lsf's are attenuated
                 */
                for (j = 0; j < 8; j++)
                {
                    /* subtract mean */
                    st.lsf_hist_mean[i + j * M] = st.lsf_hist_mean[i + j * M] - lsf_mean;

                    /* attenuate deviation from mean, especially for upper lsf's */
                    st.lsf_hist_mean[i + j * M] = (st.lsf_hist_mean[i + j * M] * lsf_hist_mean_scale[i]) >> 15;

                    /* limit the deviation */
                    if (st.lsf_hist_mean[i + j * M] < 0)
                    {
                        negative = 1;
                    }
                    else
                    {
                        negative = 0;
                    }
                    st.lsf_hist_mean[i + j * M] = Math.abs(st.lsf_hist_mean[i + j * M]);

                    /* apply soft limit */
                    if (st.lsf_hist_mean[i + j * M] > 655)
                    {
                        st.lsf_hist_mean[i + j * M] = 655 + ((st.lsf_hist_mean[i + j * M] - 655) >> 2);
                    }

                    /* apply hard limit */
                    if (st.lsf_hist_mean[i + j * M] > 1310)
                    {
                        st.lsf_hist_mean[i + j * M] = 1310;
                    }

                    if (negative != 0)
                    {
                        st.lsf_hist_mean[i + j * M] = -st.lsf_hist_mean[i + j * M];
                    }
                }
            }
        }

        if (st.sid_frame != 0)
        {
            /*
             * Set old SID parameters, always shift even if there is no new valid_data
             */
            memcpy(st.lsp_old, 0, st.lsp, 0, M);
            st.old_log_en = st.log_en;

            if (st.valid_data != 0) /* new data available (no CRC) */
            {
                /*
                 * Compute interpolation factor, since the division only works for values of since_last_sid < 32 we have
                 * to limit the interpolation to 32 frames
                 */
                tmp_int_length = st.since_last_sid;
                st.since_last_sid = 0;

                if (tmp_int_length > 32)
                {
                    tmp_int_length = 32;
                }

                if (tmp_int_length >= 2)
                {
                    st.true_sid_period_inv = 0x2000000 / (tmp_int_length << 10);
                }
                else
                {
                    st.true_sid_period_inv = 16384; /* 0.5 it Q15 */
                }
                memcpy(lsfState.past_r_q, 0, past_rq_init, parm[0] * M, M);
                D_plsf_3(lsfState, MRDTX, 0, parm, 1, st.lsp);

                /* reset for next speech frame */
                memset(lsfState.past_r_q, 0, 0, M);
                log_en_index = parm[4];

                /* Q11 and divide by 4 */
                st.log_en = (short) (log_en_index << 9);

                /* Subtract 2.5 in Q11 */
                st.log_en = (short) (st.log_en - 5120);

                /* Index 0 is reserved for silence */
                if (log_en_index == 0)
                {
                    st.log_en = MIN_16;
                }

                /*
                 * no interpolation at startup after coder reset or when SID_UPD has been received right after SPEECH
                 */
                if ((st.data_updated == 0) || (st.dtxGlobalState == SPEECH))
                {
                    memcpy(st.lsp_old, 0, st.lsp, 0, M);
                    st.old_log_en = st.log_en;
                }
            } /* endif valid_data */

            /* initialize gain predictor memory of other modes */
            ma_pred_init = (short) ((st.log_en >> 1) - 9000);

            if (ma_pred_init > 0)
            {
                ma_pred_init = 0;
            }

            if (ma_pred_init < -14436)
            {
                ma_pred_init = -14436;
            }
            pred_state.past_qua_en[0] = ma_pred_init;
            pred_state.past_qua_en[1] = ma_pred_init;
            pred_state.past_qua_en[2] = ma_pred_init;
            pred_state.past_qua_en[3] = ma_pred_init;

            /* past_qua_en for other modes than MR122 */
            ma_pred_init = (short) ((5443 * ma_pred_init) >> 15);

            /* scale down by factor 20*log10(2) in Q15 */
            pred_state.past_qua_en_MR122[0] = ma_pred_init;
            pred_state.past_qua_en_MR122[1] = ma_pred_init;
            pred_state.past_qua_en_MR122[2] = ma_pred_init;
            pred_state.past_qua_en_MR122[3] = ma_pred_init;
        } /* endif sid_frame */

        /*
         * CN generation recompute level adjustment factor Q11 st.log_en_adjust = 0.9*st.log_en_adjust +
         * 0.1*dtx_log_en_adjust[mode]);
         */
        // System.out.println("CN generation:mode is " + mode);
        st.log_en_adjust = (short) (((st.log_en_adjust * 29491) >> 15) + (((dtx_log_en_adjust[mode] << 5) * 3277) >> 20));
        // System.out.printf("st.log_en_adjust:%d\n", st.log_en_adjust);
        /* Interpolate SID info */
        /* Q10 */
        if (st.since_last_sid > 30)
        {
            int_fac = 32767;
        }
        else
        {
            int_fac = (short) ((st.since_last_sid + 1) << 10);
        }

        /* Q10 * Q15 . Q10 */
        int_fac = (int_fac * st.true_sid_period_inv) >> 15;

        /* Maximize to 1.0 in Q10 */
        if (int_fac > 1024)
        {
            int_fac = 1024;
        }

        /* Q10 . Q14 */
        int_fac = (short) (int_fac << 4);

        /* Q14 * Q11.Q26 */
        log_en_int = (int_fac * st.log_en) << 1;

        for (i = 0; i < M; i++)
        {
            /* Q14 * Q15 . Q14 */
            lsp_int[i] = (int_fac * st.lsp[i]) >> 15;
        }

        /* 1-k in Q14 */
        int_fac = 16384 - int_fac;

        /* (Q14 * Q11 . Q26) + Q26 . Q26 */
        log_en_int += (int_fac * st.old_log_en) << 1;

        for (i = 0; i < M; i++)
        {
            /* Q14 + (Q14 * Q15 . Q14) . Q14 */
            lsp_int[i] = lsp_int[i] + ((int_fac * st.lsp_old[i]) >> 15);

            /* Q14 . Q15 */
            lsp_int[i] = lsp_int[i] << 1;
        }

        /* compute the amount of lsf variability */
        /* -0.6 in Q12 */
        lsf_variab_factor = st.log_pg_mean - 2457;

        /*  *0.3 Q12*Q15 . Q12 */
        lsf_variab_factor = 4096 - ((lsf_variab_factor * 9830) >> 15);

        /* limit to values between 0..1 in Q12 */
        if (lsf_variab_factor >= 4096)
        {
            lsf_variab_factor = 32767;
        }
        else if (lsf_variab_factor < 0)
        {
            lsf_variab_factor = 0;
        }
        else
        {
            lsf_variab_factor = lsf_variab_factor << 3; /* . Q15 */
        }

        /* get index of vector to do variability with */
        int[] tmpInts = new int[1];
        tmpInts[0] = st.pn_seed_rx;
        lsf_variab_index = pseudonoise(tmpInts, 0, 3);
        st.pn_seed_rx = tmpInts[0];

        /* convert to lsf */
        Lsp_lsf(lsp_int, 0, lsf_int, 0);

        /* apply lsf variability */
        memcpy(lsf_int_variab, 0, lsf_int, 0, M);

        for (i = 0; i < M; i++)
        {
            lsf_int_variab[i] = lsf_int_variab[i] + ((lsf_variab_factor * st.lsf_hist_mean[i + lsf_variab_index * M]) >> 15);
        }

        /* make sure that LSP's are ordered */
        Reorder_lsf(lsf_int, 0, LSF_GAP);
        Reorder_lsf(lsf_int_variab, 0, LSF_GAP);

        /* copy lsf to speech decoders lsf state */
        memcpy(lsfState.past_lsf_q, 0, lsf_int, 0, M);

        /* convert to lsp */
        Lsf_lsp(lsf_int, 0, lsp_int, 0);
        Lsf_lsp(lsf_int_variab, 0, lsp_int_variab, 0);

        /*
         * Compute acoeffs Q12 acoeff is used for level normalization and Post_Filter, acoeff_variab is used for
         * synthesis filter by doing this we make sure that the level in high frequenncies does not jump up and down
         */
        Lsp_Az(lsp_int, 0, acoeff, 0);
        Lsp_Az(lsp_int_variab, 0, acoeff_variab, 0);

        /* For use in Post_Filter */
        memcpy(A_t, 0, acoeff, 0, MP1);
        memcpy(A_t, MP1, acoeff, 0, MP1);
        memcpy(A_t, MP1 << 1, acoeff, 0, MP1);
        memcpy(A_t, MP1 + MP1 + MP1, acoeff, 0, MP1);

        /* Compute reflection coefficients Q15 */
        A_Refl(acoeff, 1, refl, 0);

        // System.out.printf("acoeffSum:%d,refl:%d\n",acoeffSum,reflSum);
        /* Compute prediction error in Q15 */
        /* 0.99997 in Q15 */
        pred_err = MAX_16;

        for (i = 0; i < M; i++)
        {
            pred_err = (pred_err * (MAX_16 - ((refl[i] * refl[i]) >> 15))) >> 15;
        }

        /* compute logarithm of prediction gain */
        int[] tmpLogPgE = new int[]
        { log_pg_e };
        int[] tmpLogPgM = new int[]
        { log_pg_m };
        Log2(pred_err, tmpLogPgE, 0, tmpLogPgM, 0);
        log_pg_e = tmpLogPgE[0];
        log_pg_m = tmpLogPgM[0];

        /* convert exponent and mantissa to short Q12 */
        /* Q12 */
        log_pg = (log_pg_e - 15) << 12;
        /* saturate */
        if (log_pg < -32768)
        {
            log_pg = -32768;
        }
        log_pg = (-(log_pg + (log_pg_m >> 3))) >> 1;
        st.log_pg_mean = (short) (((29491 * st.log_pg_mean) >> 15) + ((3277 * log_pg) >> 15));

        /* Compute interpolated log energy */
        /* Q26 . Q16 */
        log_en_int = log_en_int >> 10;

        /* Add 4 in Q16 */
        log_en_int += 262144L;

        /* subtract prediction gain */
        log_en_int = log_en_int - (log_pg << 4);

        /* adjust level to speech coder mode */
        log_en_int += st.log_en_adjust << 5;
        log_en_int_e = (short) (log_en_int >> 16);
        log_en_int_m = (short) ((log_en_int - (log_en_int_e << 16)) >> 1);

        /* Q4 */
        level = (short) (Pow2(log_en_int_e, log_en_int_m));
        int k = 0;
        for (i = 0; i < 4; i++)
        {
            /* Compute innovation vector */
            tmpInts[0] = st.pn_seed_rx;
            Build_CN_code(tmpInts, 0, ex);
            st.pn_seed_rx = tmpInts[0];

            for (j = 0; j < L_SUBFR; j++)
            {
                ex[j] = (level * ex[j]) >> 15;
            }
            // System.out.printf("st.pn_seed_rx:%d,sumEx:%d,level:%d\n", st.pn_seed_rx,sumEx,level);
            /* Synthesize */
            Syn_filt(acoeff_variab, 0, ex, 0, synth, i * L_SUBFR, L_SUBFR, mem_syn, mem_synOffset, 1);
        } /* next i */
        // System.out.printf("synth[");
        // for(i = 0;i < 160;i ++){
        // System.out.printf("%d,",synth[i]);
        // }
        // System.out.printf("]\n");
        /* reset codebook averaging variables */
        averState.hangVar = 20;
        averState.hangCount = 0;

        if (new_state == DTX_MUTE)
        {
            /*
             * mute comfort noise as it has been quite a long time since last SID update was performed
             */
            int num, denom;

            tmp_int_length = st.since_last_sid;

            if (tmp_int_length > 32)
            {
                tmp_int_length = 32;
            }

            if (tmp_int_length == 1)
            {
                st.true_sid_period_inv = MAX_16;
            }
            else
            {
                num = 1024;
                denom = (tmp_int_length << 10);
                st.true_sid_period_inv = 0;

                for (i = 0; i < 15; i++)
                {
                    st.true_sid_period_inv <<= 1;
                    num <<= 1;

                    if (num >= denom)
                    {
                        num = num - denom;
                        st.true_sid_period_inv += 1;
                    }
                }
            }
            st.since_last_sid = 0;
            memcpy(st.lsp_old, 0, st.lsp, 0, M);
            st.old_log_en = st.log_en;

            /* subtract 1/8 in Q11 i.e -6/8 dB */
            st.log_en = st.log_en - 256;
            if (st.log_en < -32768)
            {
                st.log_en = -32768;
            }
        }

        /*
         * reset interpolation length timer if data has been updated.
         */
        if ((st.sid_frame != 0) & ((st.valid_data != 0) || ((st.valid_data == 0) & (st.dtxHangoverAdded != 0))))
        {
            st.since_last_sid = 0;
            st.data_updated = 1;
        }
        return;
    }

    /*
     * lsp_avg
     * 
     * 
     * Parameters: st.lsp_meanSave B: LSP averages lsp I: LSPs
     * 
     * Function: Calculate the LSP averages
     * 
     * Returns: void
     */
    static void lsp_avg(lsp_avgState st, int[] lsp, int lspOffset)
    {
        int i, tmp;

        for (i = 0; i < M; i++)
        {
            /* mean = 0.84*mean */
            tmp = (st.lsp_meanSave[i] << 16);
            tmp -= (EXPCONST * st.lsp_meanSave[i]) << 1;

            /* Add 0.16 of newest LSPs to mean */
            tmp += (EXPCONST * lsp[i + lspOffset]) << 1;

            /* Save means */
            tmp += 0x00008000L;
            st.lsp_meanSave[i] = tmp >> 16;
        }
        // System.out.printf("[lsp_avg]sumLsp:%d,sumlsp_meanSave:%d\n", sumLsp, sumlsp_meanSave);
        return;
    }

    /*
     * Int_lpc_1and3
     * 
     * 
     * Parameters: lsp_old I: LSP vector at the 4th subfr. of past frame [M] lsp_mid I: LSP vector at the 2nd subframe
     * of present frame [M] lsp_new I: LSP vector at the 4th subframe of present frame [M] Az O: interpolated LP
     * parameters in subframes 1 and 3 [AZ_SIZE]
     * 
     * Function: Interpolates the LSPs and converts to LPC parameters to get a different LP filter in each subframe.
     * 
     * The 20 ms speech frame is divided into 4 subframes. The LSPs are quantized and transmitted at the 2nd and 4th
     * subframes (twice per frame) and interpolated at the 1st and 3rd subframe.
     * 
     * Returns: void
     */
    static void Int_lpc_1and3(int lsp_old[], int lsp_oldOffset, int lsp_mid[], int lsp_midOffset, int lsp_new[], int lsp_newOffset, int Az[],
            int AzOffset)
    {
        int[] lsp = new int[M];
        int i;

        /* lsp[i] = lsp_mid[i] * 0.5 + lsp_old[i] * 0.5 */
        for (i = 0; i < 10; i++)
        {
            lsp[i] = (lsp_mid[i + lsp_midOffset] >> 1) + (lsp_old[i + lsp_oldOffset] >> 1);
        }

        /* Subframe 1 */
        Lsp_Az(lsp, 0, Az, AzOffset);
        AzOffset += MP1;

        /* Subframe 2 */
        Lsp_Az(lsp_mid, lsp_midOffset, Az, AzOffset);
        AzOffset += MP1;

        for (i = 0; i < 10; i++)
        {
            lsp[i] = (lsp_mid[i + lsp_midOffset] >> 1) + (lsp_new[i + lsp_newOffset] >> 1);
        }

        /* Subframe 3 */
        Lsp_Az(lsp, 0, Az, AzOffset);
        AzOffset += MP1;

        /* Subframe 4 */
        Lsp_Az(lsp_new, lsp_newOffset, Az, AzOffset);
        return;
    }

    /*
     * Int_lpc_1to3
     * 
     * 
     * Parameters: lsp_old I: LSP vector at the 4th subframe of past frame [M] lsp_new I: LSP vector at the 4th subframe
     * of present frame [M] Az O: interpolated LP parameters in all subframes [AZ_SIZE]
     * 
     * Function: Interpolates the LSPs and converts to LPC parameters to get a different LP filter in each subframe.
     * 
     * The 20 ms speech frame is divided into 4 subframes. The LSPs are quantized and transmitted at the 4th subframes
     * (once per frame) and interpolated at the 1st, 2nd and 3rd subframe.
     * 
     * Returns: void
     */
    static void Int_lpc_1to3(int lsp_old[], int lsp_oldOffset, int lsp_new[], int lsp_newOffset, int Az[], int AzOffset)
    {
        int[] lsp = new int[M];
        int i;

        for (i = 0; i < 10; i++)
        {
            lsp[i] = (lsp_new[i + lsp_newOffset] >> 2) + (lsp_old[i + lsp_oldOffset] - (lsp_old[i + lsp_oldOffset] >> 2));
        }

        /* Subframe 1 */
        Lsp_Az(lsp, 0, Az, AzOffset);
        AzOffset += MP1;

        for (i = 0; i < 10; i++)
        {
            lsp[i] = (lsp_old[i + lsp_oldOffset] >> 1) + (lsp_new[i + lsp_newOffset] >> 1);
        }

        /* Subframe 2 */
        Lsp_Az(lsp, 0, Az, AzOffset);
        AzOffset += MP1;

        for (i = 0; i < 10; i++)
        {
            lsp[i] = (lsp_old[i + lsp_oldOffset] >> 2) + (lsp_new[i + lsp_newOffset] - (lsp_new[i + lsp_newOffset] >> 2));
        }

        /* Subframe 3 */
        Lsp_Az(lsp, 0, Az, AzOffset);
        AzOffset += MP1;

        /* Subframe 4 */
        Lsp_Az(lsp_new, lsp_newOffset, Az, AzOffset);
        return;
    }

    /*
     * D_plsf_5
     * 
     * 
     * Parameters: st.past_lsf_q I: Past dequantized LFSs st.past_r_q B: past quantized residual bfi B: bad frame
     * indicator indice I: quantization indices of 3 submatrices, Q0 lsp1_q O: quantized 1st LSP vector lsp2_q O:
     * quantized 2nd LSP vector
     * 
     * Function: Decodes the 2 sets of LSP parameters in a frame using the received quantization indices.
     * 
     * Returns: void
     */
    static void D_plsf_5(D_plsfState st, short bfi, short[] indice, int indiceOffset, int[] lsp1_q, int lsp1_1Offset, int[] lsp2_q, int lsp2_qOffset)
    {
        int[] lsf1_r = new int[M];
        int[] lsf2_r = new int[M];
        int[] lsf1_q = new int[M];
        int[] lsf2_q = new int[M];
        int i, temp1, temp2, sign;
        int[] p_dico;
        int p_dicoIndex;

        /* if bad frame */
        if (bfi != 0)
        {
            /* use the past LSFs slightly shifted towards their mean */
            for (i = 0; i < M; i += 2)
            {
                /* lsfi_q[i] = ALPHA*st.past_lsf_q[i] + ONE_ALPHA*meanLsf[i]; */
                lsf1_q[i] = ((st.past_lsf_q[i] * ALPHA_122) >> 15) + ((mean_lsf_5[i] * ONE_ALPHA_122) >> 15);
                lsf1_q[i + 1] = ((st.past_lsf_q[i + 1] * ALPHA_122) >> 15) + ((mean_lsf_5[i + 1] * ONE_ALPHA_122) >> 15);
            }
            memcpy(lsf2_q, 0, lsf1_q, 0, M);

            /* estimate past quantized residual to be used in next frame */
            for (i = 0; i < M; i += 2)
            {
                /* temp = meanLsf[i] + st.past_r_q[i] * LSPPpred_facMR122; */
                temp1 = mean_lsf_5[i] + ((st.past_r_q[i] * LSP_PRED_FAC_MR122) >> 15);
                temp2 = mean_lsf_5[i + 1] + ((st.past_r_q[i + 1] * LSP_PRED_FAC_MR122) >> 15);
                st.past_r_q[i] = lsf2_q[i] - temp1;
                st.past_r_q[i + 1] = lsf2_q[i + 1] - temp2;
            }
        } /* if good LSFs received */
        else
        {
            /* decode prediction residuals from 5 received indices */
            // p_dico = &dico1_lsf_5[indice[0] << 2];
            p_dico = dico1_lsf_5;
            p_dicoIndex = indice[indiceOffset] << 2;
            lsf1_r[0] = p_dico[p_dicoIndex++];
            lsf1_r[1] = p_dico[p_dicoIndex++];
            lsf2_r[0] = p_dico[p_dicoIndex++];
            lsf2_r[1] = p_dico[p_dicoIndex++];
            // p_dico = &dico2_lsf_5[indice[1] << 2];
            p_dico = dico2_lsf_5;
            p_dicoIndex = indice[indiceOffset + 1] << 2;
            lsf1_r[2] = p_dico[p_dicoIndex++];
            lsf1_r[3] = p_dico[p_dicoIndex++];
            lsf2_r[2] = p_dico[p_dicoIndex++];
            lsf2_r[3] = p_dico[p_dicoIndex++];
            sign = (short) (indice[2 + indiceOffset] & 1);
            i = indice[2 + indiceOffset] >> 1;
            // p_dico = &dico3_lsf_5[i << 2];
            p_dico = dico3_lsf_5;
            p_dicoIndex = i << 2;
            if (sign == 0)
            {
                lsf1_r[4] = p_dico[p_dicoIndex++];
                lsf1_r[5] = p_dico[p_dicoIndex++];
                lsf2_r[4] = p_dico[p_dicoIndex++];
                lsf2_r[5] = p_dico[p_dicoIndex++];
            }
            else
            {
                lsf1_r[4] = (short) (-(p_dico[p_dicoIndex++]));
                lsf1_r[5] = (short) (-(p_dico[p_dicoIndex++]));
                lsf2_r[4] = (short) (-(p_dico[p_dicoIndex++]));
                lsf2_r[5] = (short) (-(p_dico[p_dicoIndex++]));
            }
            // p_dico = &dico4_lsf_5[( indice[3]<<2 )];
            p_dico = dico4_lsf_5;
            p_dicoIndex = (indice[3 + indiceOffset] << 2);
            lsf1_r[6] = p_dico[p_dicoIndex++];
            lsf1_r[7] = p_dico[p_dicoIndex++];
            lsf2_r[6] = p_dico[p_dicoIndex++];
            lsf2_r[7] = p_dico[p_dicoIndex++];
            // p_dico = &dico5_lsf_5[( indice[4]<<2 )];
            p_dico = dico5_lsf_5;
            p_dicoIndex = (indice[4 + indiceOffset] << 2);
            lsf1_r[8] = p_dico[p_dicoIndex++];
            lsf1_r[9] = p_dico[p_dicoIndex++];
            lsf2_r[8] = p_dico[p_dicoIndex++];
            lsf2_r[9] = p_dico[p_dicoIndex++];

            /* Compute quantized LSFs and update the past quantized residual */
            for (i = 0; i < M; i++)
            {
                temp1 = mean_lsf_5[i] + ((st.past_r_q[i] * LSP_PRED_FAC_MR122) >> 15);
                lsf1_q[i] = lsf1_r[i] + temp1;
                lsf2_q[i] = lsf2_r[i] + temp1;
                st.past_r_q[i] = lsf2_r[i];
            }
        }

        /* verification that LSFs have minimum distance of LSF_GAP Hz */
        Reorder_lsf(lsf1_q, 0, LSF_GAP);
        Reorder_lsf(lsf2_q, 0, LSF_GAP);
        memcpy(st.past_lsf_q, 0, lsf2_q, 0, M);

        /* convert LSFs to the cosine domain */
        Lsf_lsp(lsf1_q, 0, lsp1_q, lsp1_1Offset);
        Lsf_lsp(lsf2_q, 0, lsp2_q, lsp2_qOffset);
        return;
    }

    /*
     * Dec_lag3
     * 
     * 
     * Parameters: index I: received pitch index t0_min I: minimum of search range t0_max I: maximum of search range
     * i_subfr I: subframe flag T0_prev I: integer pitch delay of last subframe used in 2nd and 4th subframes T0 O:
     * integer part of pitch lag T0_frac O : fractional part of pitch lag flag4 I : flag for encoding with 4 bits
     * Function: Decoding of fractional pitch lag with 1/3 resolution. Extract the integer and fraction parts of the
     * pitch lag from the received adaptive codebook index.
     * 
     * The fractional lag in 1st and 3rd subframes is encoded with 8 bits while that in 2nd and 4th subframes is
     * relatively encoded with 4, 5 and 6 bits depending on the mode.
     * 
     * Returns: void
     */
    static void Dec_lag3(int index, int t0_min, int t0_max, int i_subfr, int T0_prev, int[] T0, int T0Offset, int[] T0_frac, int T0_fracOffset,
            int flag4)
    {
        int i, tmp_lag;

        /* if 1st or 3rd subframe */
        if (i_subfr == 0)
        {
            if (index < 197)
            {
                T0[T0Offset] = (((index + 2) * 10923) >> 15) + 19;
                i = T0[T0Offset] + T0[T0Offset] + T0[T0Offset];
                T0_frac[T0_fracOffset] = (index - i) + 58;
            }
            else
            {
                T0[T0Offset] = index - 112;
                T0_frac[T0_fracOffset] = 0;
            }
        } /* 2nd or 4th subframe */
        else
        {
            if (flag4 == 0)
            {
                /* 'normal' decoding: either with 5 or 6 bit resolution */
                i = (((index + 2) * 10923) >> 15) - 1;
                T0[T0Offset] = i + t0_min;
                i = i + i + i;
                T0_frac[T0_fracOffset] = (index - 2) - i;
            }
            else
            {
                /* decoding with 4 bit resolution */
                tmp_lag = T0_prev;

                if ((tmp_lag - t0_min) > 5)
                {
                    tmp_lag = t0_min + 5;
                }

                if ((t0_max - tmp_lag) > 4)
                {
                    tmp_lag = t0_max - 4;
                }

                if (index < 4)
                {
                    i = (tmp_lag - 5);
                    T0[T0Offset] = i + index;
                    T0_frac[T0_fracOffset] = 0;
                }
                else
                {
                    if (index < 12)
                    {
                        i = (((index - 5) * 10923) >> 15) - 1;
                        T0[T0Offset] = i + tmp_lag;
                        i = i + i + i;
                        T0_frac[T0_fracOffset] = (index - 9) - i;
                    }
                    else
                    {
                        i = (index - 12) + tmp_lag;
                        T0[T0Offset] = i + 1;
                        T0_frac[T0_fracOffset] = 0;
                    }
                }
            } /* end if (decoding with 4 bit resolution) */
        }
        return;
    }

    /*
     * Pred_lt_3or6_40
     * 
     * 
     * Parameters: exc B: excitation buffer T0 I: integer pitch lag frac I: fraction of lag flag3 I: if set, upsampling
     * rate = 3 (6 otherwise)
     * 
     * Function: Compute the result of long term prediction with fractional interpolation of resolution 1/3 or 1/6.
     * (Interpolated past excitation).
     * 
     * Once the fractional pitch lag is determined, the adaptive codebook vector v(n) is computed by interpolating the
     * past excitation signal u(n) at the given integer delay k and phase (fraction) :
     * 
     * 9 9 v(n) = SUM[ u(n-k-i) * b60(t+i*6) ] + SUM[ u(n-k+1+i) * b60(6-t+i*6) ], i=0 i=0 n = 0, ...,39, t = 0, ...,5.
     * 
     * The interpolation filter b60 is based on a Hamming windowed sin(x)/x function truncated at 59 and padded with
     * zeros at 60 (b60(60)=0)). The filter has a cut-off frequency (-3 dB) at 3 600 Hz in the over-sampled domain.
     * 
     * Returns: void
     */
    static void Pred_lt_3or6_40(int exc[], int excOffset, int T0, int frac, int flag3)
    {
        int s, i;
        int[] x0;
        int[] x1;
        int[] x2;
        int[] c1;
        int[] c2;

        int x0Index;
        // x0 = &exc[ - T0];
        x0 = exc;
        x0Index = excOffset - T0;
        frac = -frac;

        if (flag3 != 0)
        {
            frac <<= 1; /* inter_3l[k] = inter6[2*k] . k' = 2*k */
        }

        if (frac < 0)
        {
            frac += 6;
            x0Index--;
        }

        int c1Index;
        // c1 = &inter6[frac];
        c1 = inter6;
        c1Index = frac;
        int c2Index;
        // c2 = &inter6[6 - frac];
        c2 = inter6;
        c2Index = 6 - frac;
        int x1Index;
        int x2Index;
        for (i = 0; i < 40; i++)
        {
            x1 = x0;
            x1Index = x0Index++;
            x2 = x0;
            x2Index = x0Index;
            s = x1[x1Index] * c1[c1Index];
            s += x1[x1Index - 1] * c1[6 + c1Index];
            s += x1[x1Index - 2] * c1[12 + c1Index];
            s += x1[x1Index - 3] * c1[18 + c1Index];
            s += x1[x1Index - 4] * c1[24 + c1Index];
            s += x1[x1Index - 5] * c1[30 + c1Index];
            s += x1[x1Index - 6] * c1[36 + c1Index];
            s += x1[x1Index - 7] * c1[42 + c1Index];
            s += x1[x1Index - 8] * c1[48 + c1Index];
            s += x1[x1Index - 9] * c1[54 + c1Index];
            s += x2[0 + x2Index] * c2[0 + c2Index];
            s += x2[1 + x2Index] * c2[6 + c2Index];
            s += x2[2 + x2Index] * c2[12 + c2Index];
            s += x2[3 + x2Index] * c2[18 + c2Index];
            s += x2[4 + x2Index] * c2[24 + c2Index];
            s += x2[5 + x2Index] * c2[30 + c2Index];
            s += x2[6 + x2Index] * c2[36 + c2Index];
            s += x2[7 + x2Index] * c2[42 + c2Index];
            s += x2[8 + x2Index] * c2[48 + c2Index];
            s += x2[9 + x2Index] * c2[54 + c2Index];
            exc[i + excOffset] = (s + 0x4000) >> 15;
        }
        // System.out.println("excSum:"+excSum+",excOffset:"+excOffset);
    }

    /*
     * Dec_lag6
     * 
     * 
     * Parameters: index I: received pitch index pit_min I: minimum pitch lag pit_max I: maximum pitch lag i_subfr I:
     * subframe flag T0 B: integer part of pitch lag T0_frac O : fractional part of pitch lag
     * 
     * Function: Decoding of fractional pitch lag with 1/6 resolution. Extract the integer and fraction parts of the
     * pitch lag from the received adaptive codebook index.
     * 
     * The fractional lag in 1st and 3rd subframes is encoded with 9 bits while that in 2nd and 4th subframes is
     * relatively encoded with 6 bits. Note that in relative encoding only 61 values are used. If the decoder receives
     * 61, 62, or 63 as the relative pitch index, it means that a transmission error occurred. In this case, the pitch
     * lag from previous subframe (actually from previous frame) is used.
     * 
     * Returns: void
     */
    static void Dec_lag6(int index, int pit_min, int pit_max, int i_subfr, int[] T0, int T0Offset, int[] T0_frac, int T0_fracOffset)
    {
        int t0_min, t0_max, i;

        /* if 1st or 3rd subframe */
        if (i_subfr == 0)
        {
            if (index < 463)
            {
                /* T0 = (index+5)/6 + 17 */
                T0[T0Offset] = (index + 5) / 6 + 17;
                i = T0[T0Offset] + T0[T0Offset] + T0[T0Offset];

                /*  *T0_frac = index - T0*6 + 105 */
                T0_frac[T0_fracOffset] = (index - (i + i)) + 105;
            }
            else
            {
                T0[T0Offset] = index - 368;
                T0_frac[T0_fracOffset] = 0;
            }
        } /* second or fourth subframe */
        else
        {
            /* find t0_min and t0_max for 2nd (or 4th) subframe */
            t0_min = T0[T0Offset] - 5;

            if (t0_min < pit_min)
            {
                t0_min = pit_min;
            }
            t0_max = t0_min + 9;

            if (t0_max > pit_max)
            {
                t0_max = pit_max;
                t0_min = t0_max - 9;
            }

            /* i = (index+5)/6 - 1 */
            i = (index + 5) / 6 - 1;
            T0[T0Offset] = i + t0_min;
            i = i + i + i;
            T0_frac[T0_fracOffset] = (index - 3) - (i + i);
        }
    }

    /*
     * decompress10
     * 
     * 
     * Parameters: MSBs I: MSB part of the index LSBs I: LSB part of the index index1 I: index for first pos in posIndex
     * index2 I: index for second pos in posIndex index3 I: index for third pos in posIndex pos_indx O: position of 3
     * pulses (decompressed) Function: Decompression of the linear codeword
     * 
     * Returns: void
     */
    static void decompress10(int MSBs, int LSBs, int index1, int index2, int index3, int pos_indx[], int pos_indxOffset)
    {
        int divMSB;

        if (MSBs > 124)
        {
            MSBs = 124;
        }
        /*
         * pos_indx[index1] = ((MSBs-25*(MSBs/25))%5)*2 + (LSBs-4*(LSBs/4))%2; pos_indx[index2] =
         * ((MSBs-25*(MSBs/25))/5)*2 + (LSBs-4*(LSBs/4))/2; pos_indx[index3] = (MSBs/25)*2 + LSBs/4;
         */
        divMSB = MSBs / 25;
        pos_indx[index1 + pos_indxOffset] = (((MSBs - 25 * (divMSB)) % 5) << 1) + (LSBs & 0x1);
        pos_indx[index2 + pos_indxOffset] = (((MSBs - 25 * (divMSB)) / 5) << 1) + ((LSBs & 0x2) >> 1);
        pos_indx[index3 + pos_indxOffset] = (divMSB << 1) + (LSBs >> 2);
        return;
    }

    /*
     * decompress_codewords
     * 
     * 
     * Parameters: indx I: position of 8 pulses (compressed) pos_indx O: position index of 8 pulses (position only)
     * 
     * Function: Decompression of the linear codewords to 4+three indeces one bit from each pulse is made robust to
     * errors by minimizing the phase shift of a bit error.
     * 
     * i0,i4,i1 => one index (7+3) bits, 3 LSBs more robust i2,i6,i5 => one index (7+3) bits, 3 LSBs more robust i3,i7
     * => one index (5+2) bits, 2-3 LSbs more robust
     * 
     * Returns: void
     */
    static void decompress_codewords(short indx[], int indxOffset, int pos_indx[], int pos_indxOffset)
    {
        int ia, ib, MSBs, LSBs, MSBs0_24, tmp;

        /*
         * First index: 10x10x10 . 2x5x2x5x2x5. 125x2x2x2 . 7+1x3 bits MSBs = indx[NB_TRACK]/8; LSBs = indx[NB_TRACK]%8;
         */
        MSBs = indx[indxOffset] >> 3;
        LSBs = indx[indxOffset] & 0x7;
        decompress10(MSBs, LSBs, 0, 4, 1, pos_indx, pos_indxOffset);

        /*
         * Second index: 10x10x10 . 2x5x2x5x2x5. 125x2x2x2 . 7+1x3 bits MSBs = indx[NB_TRACK+1]/8; LSBs =
         * indx[NB_TRACK+1]%8;
         */
        MSBs = indx[1 + indxOffset] >> 3;
        LSBs = indx[1 + indxOffset] & 0x7;
        decompress10(MSBs, LSBs, 2, 6, 5, pos_indx, pos_indxOffset);

        /*
         * Third index: 10x10 . 2x5x2x5. 25x2x2 . 5+1x2 bits MSBs = indx[NB_TRACK+2]/4; LSBs = indx[NB_TRACK+2]%4;
         * MSBs0_24 = (MSBs*25+12)/32; if ((MSBs0_24/5)%2==1) pos_indx[3] = (4-(MSBs0_24%5))*2 + LSBs%2; else
         * pos_indx[3] = (MSBs0_24%5)*2 + LSBs%2; pos_indx[7] = (MSBs0_24/5)*2 + LSBs/2;
         */
        MSBs = indx[2 + indxOffset] >> 2;
        LSBs = indx[2 + indxOffset] & 0x3;
        MSBs0_24 = (((MSBs * 25) + 12) >> 5);
        tmp = (MSBs0_24 * 6554) >> 15;
        ia = tmp & 0x1;
        ib = (MSBs0_24 - (tmp * 5));

        if (ia == 1)
        {
            ib = 4 - ib;
        }
        pos_indx[3 + pos_indxOffset] = (ib << 1) + (LSBs & 0x1);
        pos_indx[7 + pos_indxOffset] = (tmp << 1) + (LSBs >> 1);
    }

    /*
     * decode_2i40_9bits
     * 
     * 
     * Parameters: subNr I: subframe number sign I: signs of 2 pulses index I: Positions of the 2 pulses cod O:
     * algebraic (fixed) codebook excitation
     * 
     * Function: Algebraic codebook decoder
     * 
     * Returns: void
     */
    static void decode_2i40_9bits(int subNr, int sign, int index, int cod[], int codOffset)
    {
        int[] pos = new int[2];
        int i, j, k;

        /* Decode the positions */
        /* table bit is the MSB */
        j = (index & 64) >> 6;
        i = index & 7;

        /* pos0 =i*5+startPos[j*8+subNr*2] */
        i = (i + (i << 2));
        k = startPos[(j << 3) + (subNr << 1)];
        pos[0] = i + k;
        index = index >> 3;
        i = index & 7;

        /* pos1 =i*5+startPos[j*8+subNr*2+1] */
        i = (i + (i << 2));
        k = startPos[((j << 3) + (subNr << 1)) + 1];
        pos[1] = (short) (i + k);

        /* decode the signs and build the codeword */
        memset(cod, codOffset, 0, L_SUBFR);

        for (j = 0; j < 2; j++)
        {
            i = sign & 1;
            sign = sign >> 1;

            if (i != 0)
            {
                cod[pos[j] + codOffset] = 8191; /* +1.0 */
            }
            else
            {
                cod[pos[j] + codOffset] = -8192; /* -1.0 */
            }
        }
        return;
    }

    /*
     * decode_2i40_11bits
     * 
     * 
     * Parameters: sign I: signs of 2 pulses index I: Positions of the 2 pulses cod O: algebraic (fixed) codebook
     * excitation
     * 
     * Function: Algebraic codebook decoder
     * 
     * Returns: void
     */
    static void decode_2i40_11bits(int sign, int index, int cod[], int codOffset)
    {
        int[] pos = new int[2];
        int i, j;

        /* Decode the positions */
        j = index & 1;
        index = index >> 1;
        i = index & 7;

        /* pos0 =i*5+1+j*2 */
        i = (i + (i << 2));
        i = (i + 1);
        j = (j << 1);
        pos[0] = i + j;
        index = index >> 3;
        j = index & 3;
        index = index >> 2;
        i = index & 7;

        if (j == 3)
        {
            /* pos1 =i*5+4 */
            i = (i + (i << 2));
            pos[1] = i + 4;
        }
        else
        {
            /* pos1 =i*5+j */
            i = (i + (i << 2));
            pos[1] = i + j;
        }

        /* decode the signs and build the codeword */
        memset(cod, codOffset, 0, L_SUBFR << 2);

        for (j = 0; j < 2; j++)
        {
            i = sign & 1;
            sign = sign >> 1;

            if (i != 0)
            {
                cod[pos[j] + codOffset] = 8191; /* +1.0 */
            }
            else
            {
                cod[pos[j] + codOffset] = -8192; /* -1.0 */
            }
        }
        return;
    }

    /*
     * decode_3i40_14bits
     * 
     * 
     * Parameters: sign I: signs of 3 pulses index I: Positions of the 3 pulses cod O: algebraic (fixed) codebook
     * excitation
     * 
     * Function: Algebraic codebook decoder
     * 
     * Returns: void
     */
    static void decode_3i40_14bits(int sign, int index, int cod[], int codOffset)
    {
        int[] pos = new int[3];
        int i, j;

        /* Decode the positions */
        i = index & 7;

        /* pos0 =i*5 */
        pos[0] = i + (i << 2);
        index = index >> 3;
        j = index & 1;
        index = index >> 1;
        i = index & 7;

        /* pos1 =i*5+1+j*2 */
        i = (i + (i << 2));
        i = (i + 1);
        j = (j << 1);
        pos[1] = i + j;
        index = index >> 3;
        j = index & 1;
        index = index >> 1;
        i = index & 7;

        /* pos2 =i*5+2+j*2 */
        i = (i + (i << 2));
        i = (i + 2);
        j = (j << 1);
        pos[2] = i + j;

        /* decode the signs and build the codeword */
        memset(cod, codOffset, 0, L_SUBFR << 2);

        for (j = 0; j < 3; j++)
        {
            i = sign & 1;
            sign = sign >> 1;

            if (i > 0)
            {
                cod[pos[j] + codOffset] = 8191; /* +1.0 */
            }
            else
            {
                cod[pos[j] + codOffset] = -8192; /* -1.0 */
            }
        }
        return;
    }

    /*
     * decode_3i40_14bits
     * 
     * 
     * Parameters: sign I: signs of 4 pulses index I: Positions of the 4 pulses cod O: algebraic (fixed) codebook
     * excitation
     * 
     * Function: Algebraic codebook decoder
     * 
     * Returns: void
     */
    static void decode_4i40_17bits(int sign, int index, int cod[], int codOffset)
    {
        int[] pos = new int[4];
        int i, j;

        /* Decode the positions */
        i = index & 7;
        i = dgray[i];

        /* pos0 =i*5 */
        pos[0] = i + (i << 2);
        index = index >> 3;
        i = index & 7;
        i = dgray[i];

        /* pos1 =i*5+1 */
        i = (i + (i << 2));
        pos[1] = i + 1;
        index = index >> 3;
        i = index & 7;
        i = dgray[i];

        /* pos2 =i*5+1 */
        i = (i + (i << 2));
        pos[2] = i + 2;
        index = index >> 3;
        j = index & 1;
        index = index >> 1;
        i = index & 7;
        i = dgray[i];

        /* pos3 =i*5+3+j */
        i = (i + (i << 2));
        i = (i + 3);
        pos[3] = i + j;

        /* decode the signs and build the codeword */
        memset(cod, codOffset, 0, L_SUBFR << 2);

        for (j = 0; j < 4; j++)
        {
            i = sign & 1;
            sign = sign >> 1;

            if (i != 0)
            {
                cod[pos[j] + codOffset] = 8191;
            }
            else
            {
                cod[pos[j] + codOffset] = -8192;
            }
        }
        return;
    }

    /*
     * decode_8i40_31bits
     * 
     * 
     * Parameters: index I: index of 8 pulses (sign+position) cod O: algebraic (fixed) codebook excitation
     * 
     * Function: Algebraic codebook decoder
     * 
     * Returns: void
     */
    static void decode_8i40_31bits(short index[], int indexOffset, int cod[], int codOffset)
    {
        int[] linear_codewords = new int[8];
        int i, j, pos1, pos2, sign;

        memset(cod, codOffset, 0, L_CODE);
        decompress_codewords(index, NB_TRACK_MR102 + indexOffset, linear_codewords, 0);

        /* decode the positions and signs of pulses and build the codeword */
        for (j = 0; j < NB_TRACK_MR102; j++)
        {
            /* compute index i */
            i = linear_codewords[j];
            i <<= 2;

            /* position of pulse "j" */
            pos1 = i + j;

            if (index[j + indexOffset] == 0)
            {
                sign = POS_CODE; /* +1.0 */
            }
            else
            {
                sign = -NEG_CODE; /* -1.0 */
            }

            /* compute index i */
            i = linear_codewords[j + 4];
            i = i << 2;

            /* position of pulse "j+4" */
            pos2 = i + j;
            cod[pos1 + codOffset] = sign;

            if (pos2 < pos1)
            {
                sign = -(sign);
            }
            cod[pos2 + codOffset] = cod[pos2 + codOffset] + sign;
        }
        return;
    }

    /*
     * decode_10i40_35bits
     * 
     * 
     * Parameters: index I: index of 10 pulses (sign+position) cod O: algebraic (fixed) codebook excitation
     * 
     * Function: Algebraic codebook decoder
     * 
     * Returns: void
     */
    static void decode_10i40_35bits(short index[], int indexOffset, int cod[], int codOffset)
    {
        int i, j, pos1, pos2, sign, tmp;

        memset(cod, codOffset, 0, L_CODE);

        /* decode the positions and signs of pulses and build the codeword */
        for (j = 0; j < 5; j++)
        {
            /* compute index i */
            tmp = index[j + indexOffset];
            i = tmp & 7;
            i = dgray[i];
            i = (i * 5);

            /* position of pulse "j" */
            pos1 = (i + j);
            i = (tmp >> 3) & 1;

            if (i == 0)
            {
                sign = 4096; /* +1.0 */
            }
            else
            {
                sign = -4096; /* -1.0 */
            }

            /* compute index i */
            i = index[j + 5 + indexOffset] & 7;
            i = dgray[i];
            i = i * 5;

            /* position of pulse "j+5" */
            pos2 = (i + j);
            cod[pos1 + codOffset] = sign;

            if (pos2 < pos1)
            {
                sign = -(sign);
            }
            cod[pos2 + codOffset] = cod[pos2 + codOffset] + sign;
        }
        return;
    }

    /*
     * gmed_n
     * 
     * 
     * Parameters: ind I: values n I: The number of gains (odd)
     * 
     * Function: Calculates N-point median.
     * 
     * Returns: index of the median value
     */
    static int gmed_n(int ind[], int indOffset, int n)
    {
        int[] tmp = new int[NMAX];
        int[] tmp2 = new int[NMAX];
        int max, medianIndex, i, j, ix = 0;

        for (i = 0; i < n; i++)
        {
            tmp2[i] = ind[i + indOffset];
        }

        for (i = 0; i < n; i++)
        {
            max = -32767;

            for (j = 0; j < n; j++)
            {
                if (tmp2[j] >= max)
                {
                    max = tmp2[j];
                    ix = j;
                }
            }
            tmp2[ix] = -32768;
            tmp[i] = ix;
        }
        medianIndex = tmp[(n >> 1)];
        return (ind[medianIndex + indOffset]);
    }

    /*
     * ec_gain_pitch
     * 
     * 
     * Parameters: st.pbuf I: last five gains st.past_gain_pit I: past gain state I: state of the state machine
     * gain_pitch O: pitch gain
     * 
     * Function: Calculates pitch from previous values.
     * 
     * Returns: void
     */
    static void ec_gain_pitch(ec_gain_pitchState st, short state, int[] gain_pitch, int gain_pitchOffset)
    {
        int tmp;

        /* calculate median of last five gains */
        tmp = gmed_n(st.pbuf, 0, 5);

        /* new gain = minimum(median, past_gain) * pdown[state] */
        if (tmp > st.past_gain_pit)
        {
            tmp = st.past_gain_pit;
        }
        gain_pitch[gain_pitchOffset] = (tmp * pdown[state]) >> 15;
    }

    /*
     * d_gain_pitch
     * 
     * 
     * Parameters: mode I: AMR mode index I: index of quantization
     * 
     * Function: Decodes the pitch gain using the received index
     * 
     * Returns: gain
     */
    static int d_gain_pitch(int mode, int index)
    {
        int gain;

        if (mode == MR122)
        {
            /* clear 2 LSBits */
            gain = (qua_gain_pitch[index] >> 2) << 2;
        }
        else
        {
            gain = qua_gain_pitch[index];
        }
        return gain;
    }

    /*
     * ec_gain_pitch_update
     * 
     * 
     * Parameters: st.prev_gp B: previous pitch gain st.past_gain_pit O: past gain st.pbuf B: past gain buffer bfi I:
     * bad frame indicator prev_bf I: previous frame was bad gain_pitch B: pitch gain
     * 
     * Function: Update the pitch gain concealment state Limit gain_pitch if the previous frame was bad
     * 
     * Returns: gain
     */
    static void ec_gain_pitch_update(ec_gain_pitchState st, int bfi, int prev_bf, int[] gain_pitch, int gain_pitchOffset)
    {
        if (bfi == 0)
        {
            if (prev_bf != 0)
            {
                if (gain_pitch[gain_pitchOffset] > st.prev_gp)
                {
                    gain_pitch[gain_pitchOffset] = st.prev_gp;
                }
            }
            st.prev_gp = gain_pitch[gain_pitchOffset];
        }
        st.past_gain_pit = gain_pitch[gain_pitchOffset];

        /* if (st.past_gain_pit > 1.0) */
        if (st.past_gain_pit > 16384)
        {
            st.past_gain_pit = 16384;
        }
        st.pbuf[0] = st.pbuf[1];
        st.pbuf[1] = st.pbuf[2];
        st.pbuf[2] = st.pbuf[3];
        st.pbuf[3] = st.pbuf[4];
        st.pbuf[4] = st.past_gain_pit;
    }

    /*
     * gc_pred (366)
     * 
     * 
     * Parameters: st.past_qua_en I: MA predictor st.past_qua_en_MR122 I: MA predictor MR122 mode I: AMR mode code I:
     * innovative codebook vector exp_gcode0 O: predicted gain factor (exponent) frac_gcode0 O: predicted gain factor
     * (fraction) exp_en I: innovation energy (MR795) (exponent) frac_en I: innovation energy (MR795) (fraction)
     * 
     * Function: MA prediction of the innovation energy
     * 
     * Mean removed innovation energy (dB) in subframe n N-1 E(n) = 10*log(gc*gc * SUM[(code(i) * code(i)]/N) - EMean
     * i=0 N=40
     * 
     * Mean innovation energy (dB) N-1 Ei(n) = 10*log(SUM[(code(i) * code(i)]/N) i=0
     * 
     * Predicted energy 4 Ep(n) = SUM[b(i) * R(n-i)] i=1 b = [0.68 0.58 0.34 0.19] R(k) is quantified prediction error
     * at subframe k
     * 
     * E_Mean = 36 dB (MR122)
     * 
     * Predicted gain gc is found by
     * 
     * gc = POW[10, 0.05 * (Ep(n) + EMean - Ei)]
     * 
     * Returns: void
     */
    static void gc_pred(gc_predState st, int mode, int[] code, int codeOffset, int[] exp_gcode0, int exp_gcode0Offset, int[] frac_gcode0,
            int frac_gcode0Offset, int[] exp_en, int exp_enOffset, int[] frac_en, int frac_enOffset)
    {
        int[] exp = new int[1], frac = new int[1];
        int ener_code = 0, i = 0;

        /*
         * energy of code: ener_code = sum(code[i]^2)
         */
        while (i < L_SUBFR)
        {
            ener_code += code[i + codeOffset] * code[i + codeOffset];
            i++;
        }

        if ((0x3fffffff <= ener_code) | (ener_code < 0))
        {
            ener_code = MAX_32;
        }
        else
        {
            ener_code <<= 1;
        }

        if (mode == MR122)
        {
            int ener;

            /* ener_code = ener_code / lcode; lcode = 40; 1/40 = 26214 Q20 */
            ener_code = ((ener_code + 0x00008000) >> 16) * 52428;

            /* Q9 * Q20 . Q30 */
            /*
             * energy of code: ener_code(Q17) = 10 * Log10(energy) / constant = 1/2 * Log2(energy) constant =
             * 20*Log10(2)
             */
            /* ener_code = 1/2 * Log2(ener_code); Note: Log2=log2+30 */
            Log2(ener_code, exp, 0, frac, 0);
            ener_code = ((exp[0] - 30) << 16) + (frac[0] << 1);

            /* Q16 for log(), .Q17 for 1/2 log() */
            /*
             * predicted energy: ener(Q24) = (Emean + sum{pred[i]*pastEn[i]})/constant = MEAN_ENER +
             * sum(pred[i]*past_qua_en[i]) constant = 20*Log10(2)
             */
            ener = 0;
            i = 0;

            while (i < 4)
            {
                ener += st.past_qua_en_MR122[i] * pred_MR122[i];
                i++;
            }
            ener <<= 1;
            ener += MEAN_ENER_MR122;

            /*
             * predicted codebook gain
             * 
             * gc0 = Pow10( (ener*constant - ener_code*constant) / 20 ) = Pow2(ener-ener_code) = Pow2(int(d)+frac(d))
             */
            ener = (ener - ener_code) >> 1; /* Q16 */
            exp_gcode0[exp_gcode0Offset] = ener >> 16;
            frac_gcode0[frac_gcode0Offset] = (ener >> 1) - (exp_gcode0[exp_gcode0Offset] << 15);
        } /* all modes except 12.2 */
        else
        {
            int tmp, gcode0;
            int exp_code;

            /*
             * Compute: meansEner - 10log10(ener_code/ LSufr)
             */
            exp_code = 0;
            if (ener_code != 0)
            {
                while ((ener_code & 0x40000000) == 0)
                {
                    exp_code++;
                    ener_code = ener_code << 1;
                }
            }

            /* Log2 = log2 + 27 */
            Log2_norm(ener_code, exp_code, exp, 0, frac, 0);

            /* fact = 10/log2(10) = 3.01 = 24660 Q13 */
            /* Q0.Q15 * Q13 . Q14 */
            tmp = (exp[0] * (-49320)) + (((frac[0] * (-24660)) >> 15) << 1);

            /*
             * tmp = meansEner - 10log10(ener_code/L_SUBFR) = meansEner - 10log10(ener_code) + 10log10(L_SUBFR) = K -
             * fact * Log2(ener_code) = K - fact * log2(ener_code) - fact*27
             * 
             * ==> K = meansEner + fact*27 + 10log10(L_SUBFR)
             * 
             * meansEner = 33 = 540672 Q14 (MR475, MR515, MR59) meansEner = 28.75 = 471040 Q14 (MR67) meansEner = 30 =
             * 491520 Q14 (MR74) meansEner = 36 = 589824 Q14 (MR795) meansEner = 33 = 540672 Q14 (MR102)
             * 10log10(L_SUBFR) = 16.02 = 262481.51 Q14 fact * 27 = 1331640 Q14
             * ----------------------------------------- (MR475, MR515, MR59) K = 2134793.51 Q14 ~= 16678 * 64 * 2
             * (MR67) K = 2065161.51 Q14 ~= 32268 * 32 * 2 (MR74) K = 2085641.51 Q14 ~= 32588 * 32 * 2 (MR795) K =
             * 2183945.51 Q14 ~= 17062 * 64 * 2 (MR102) K = 2134793.51 Q14 ~= 16678 * 64 * 2
             */
            if (mode == MR102)
            {
                /* mean = 33 dB */
                tmp += 2134784; /* Q14 */
            }
            else if (mode == MR795)
            {
                /* mean = 36 dB */
                tmp += 2183936; /* Q14 */

                /*
                 * ener_code = <xn xn> * 2^27*2^exp_code frac_en = ener_code / 2^16 = <xn xn> * 2^11*2^exp_code <xn xn>
                 * = <xn xn>*2^11*2^exp * 2^exp_en := frac_en * 2^exp_en
                 * 
                 * ==> exp_en = -11-exp_code;
                 */
                frac_en[frac_enOffset] = ener_code >> 16;
                exp_en[exp_enOffset] = -11 - exp_code;
            }
            else if (mode == MR74)
            {
                /* mean = 30 dB */
                tmp += 2085632; /* Q14 */
            }
            else if (mode == MR67)
            {
                /* mean = 28.75 dB */
                tmp += 2065152; /* Q14 */
            }
            else
            /* MR59, MR515, MR475 */{
                /* mean = 33 dB */
                tmp += 2134784; /* Q14 */
            }

            /*
             * Compute gcode0 = Sum(i=0,3) pred[i]*past_qua_en[i] - ener_code + meanEner
             */
            tmp = tmp << 9; /* Q23 */

            /* Q13 * Q10 . Q23 */
            i = 0;

            while (i < 4)
            {
                tmp += pred[i] * st.past_qua_en[i];
                i++;
            }
            gcode0 = tmp >> 15; /* Q8 */

            /*
             * gcode0 = pow(10.0, gcode0/20) = pow(2, 3.3219*gcode0/20) = pow(2, 0.166*gcode0)
             */
            /* 5439 Q15 = 0.165985 */
            /* (correct: 1/(20*log10(2)) 0.166096 = 5443 Q15) */
            /* For IS641 bitexactness */
            if (mode == MR74)
            {
                /* Q8 * Q15 . Q24 */
                tmp = gcode0 * 10878;
            }
            else
            {
                /* Q8 * Q15 . Q24 */
                tmp = gcode0 * 10886;
            }
            tmp = tmp >> 9; /* . Q15 */

            /* . Q0.Q15 */
            exp_gcode0[exp_gcode0Offset] = tmp >> 15;
            frac_gcode0[frac_gcode0Offset] = tmp - (exp_gcode0[exp_gcode0Offset] * 32768);
        }
    }

    /*
     * gc_pred_update
     * 
     * 
     * Parameters: st.past_qua_en B: MA predictor st.past_qua_en_MR122 B: MA predictor MR122 qua_ener_MR122 I: quantized
     * energy for update (log2(quaErr)) qua_ener I: quantized energy for update (20*log10(quaErr))
     * 
     * Function: Update MA predictor with last quantized energy
     * 
     * Returns: void
     */
    static void gc_pred_update(gc_predState st, int qua_ener_MR122, int qua_ener)
    {
        int i;

        for (i = 3; i > 0; i--)
        {
            st.past_qua_en[i] = st.past_qua_en[i - 1];
            st.past_qua_en_MR122[i] = st.past_qua_en_MR122[i - 1];
        }
        st.past_qua_en_MR122[0] = qua_ener_MR122; /* log2 (quaErr), Q10 */
        st.past_qua_en[0] = qua_ener; /* 20*log10(quaErr), Q10 */
    }

    /*
     * Dec_gain
     * 
     * 
     * Parameters: pred_state.past_qua_en B: MA predictor pred_state.past_qua_en_MR122 B: MA predictor MR122 mode I: AMR
     * mode index I: index of quantization code I: Innovative vector evenSubfr I: Flag for even subframes gain_pit O:
     * Pitch gain gain_cod O: Code gain
     * 
     * Function: Decode the pitch and codebook gains
     * 
     * Returns: void
     */
    static void Dec_gain(gc_predState pred_state, int mode, int index, int code[], int codeOffset, int evenSubfr, int[] gain_pit, int gain_pitOffset,
            int[] gain_cod, int gain_codOffset)
    {
        int[] exp = new int[1];
        int[] frac = new int[1];
        int gcode0, qua_ener, qua_ener_MR122, g_code, tmp;
        int[] p;
        int pIndex;

        /* Read the quantized gains (table depends on mode) */
        index = index << 2;

        if ((mode == MR102) || (mode == MR74) || (mode == MR67))
        {
            // p = &table_gain_highrates[index];
            p = table_gain_highrates;
            pIndex = index;
            gain_pit[gain_pitOffset] = p[pIndex++];
            g_code = p[pIndex++];
            qua_ener_MR122 = p[pIndex++];
            qua_ener = p[pIndex];
        }
        else
        {
            if (mode == MR475)
            {
                index = index + ((1 - evenSubfr) << 1);
                // p = &table_gain_MR475[index];
                p = table_gain_MR475;
                pIndex = index;
                gain_pit[gain_pitOffset] = p[pIndex++];
                g_code = p[pIndex++];

                /*
                 * calculate predictor update values (not stored in 4.75 quantizer table to save space): qua_ener =
                 * log2(g) qua_ener_MR122 = 20*log10(g)
                 */
                /* Log2(x Q12) = log2(x) + 12 */
                Log2(g_code, exp, 0, frac, 0);
                exp[0] = exp[0] - 12;
                tmp = frac[0] >> 5;

                if ((frac[0] & ((int) 1 << 4)) != 0)
                {
                    tmp++;
                }
                qua_ener_MR122 = tmp + (exp[0] << 10);

                /* 24660 Q12 ~= 6.0206 = 20*log10(2) */
                tmp = exp[0] * 49320;
                tmp += (((frac[0] * 24660) >> 15) << 1);

                /* Q12 * Q0 = Q13 . Q10 */
                qua_ener = ((tmp << 13) + 0x00008000) >> 16;
            }
            else
            {
                // p = &table_gain_lowrates[index];
                p = table_gain_lowrates;
                pIndex = index;
                gain_pit[gain_pitOffset] = p[pIndex++];
                g_code = p[pIndex++];
                qua_ener_MR122 = p[pIndex++];
                qua_ener = p[pIndex++];
            }
        }

        /*
         * predict codebook gain gc0 = Pow2(int(d)+frac(d)) = 2^exp + 2^frac gcode0 (Q14) = 2^14*2^frac = gc0 *
         * 2^(14-exp)
         */
        gc_pred(pred_state, mode, code, codeOffset, exp, 0, frac, 0, null, 0, null, 0);
        gcode0 = Pow2(14, frac[0]);

        /*
         * read quantized gains, update table of past quantized energies st.past_qua_en(Q10) = 20 * Log10(gFac) /
         * constant = Log2(gFac) = qua_ener constant = 20*Log10(2)
         */
        if (exp[0] < 11)
        {
            gain_cod[gain_codOffset] = (g_code * gcode0) >> (25 - exp[0]);
        }
        else
        {
            tmp = ((g_code * gcode0) << (exp[0] - 9));

            if ((tmp >> (exp[0] - 9)) != (g_code * gcode0))
            {
                gain_cod[gain_codOffset] = 0x7FFF;
            }
            else
            {
                gain_cod[gain_codOffset] = tmp >> 16;
            }
        }

        /* update table of past quantized energies */
        gc_pred_update(pred_state, qua_ener_MR122, qua_ener);
        return;
    }

    /*
     * gc_pred_average_limited
     * 
     * 
     * Parameters: st.past_qua_en I: MA predictor st.past_qua_en_MR122 I: MA predictor MR122 ener_avg_MR122 O: everaged
     * quantized energy (log2(quaErr)) ener_avg O: averaged quantized energy (20*log10(quaErr))
     * 
     * Function: Compute average limited quantized energy Returns: void
     */
    static void gc_pred_average_limited(gc_predState st, int[] ener_avg_MR122, int ener_avg_MR122Offset, int[] ener_avg, int ener_avgOffset)
    {
        int av_pred_en, i;

        /* do average in MR122 mode (log2() domain) */
        av_pred_en = 0;

        for (i = 0; i < NPRED; i++)
        {
            av_pred_en = (av_pred_en + st.past_qua_en_MR122[i]);
        }

        /* av_pred_en = 0.25*av_pred_en */
        av_pred_en = (av_pred_en * 8192) >> 15;

        /* if (av_pred_en < -14/(20Log10(2))) av_pred_en = .. */
        if (av_pred_en < MIN_ENERGY_MR122)
        {
            av_pred_en = MIN_ENERGY_MR122;
        }
        ener_avg_MR122[ener_avg_MR122Offset] = (short) av_pred_en;

        /* do average for other modes (20*log10() domain) */
        av_pred_en = 0;

        for (i = 0; i < NPRED; i++)
        {
            av_pred_en = (av_pred_en + st.past_qua_en[i]);
            if (av_pred_en < -32768)
            {
                av_pred_en = -32768;
            }
            else if (av_pred_en > 32767)
            {
                av_pred_en = 32767;
            }
        }

        /* av_pred_en = 0.25*av_pred_en */
        av_pred_en = (av_pred_en * 8192) >> 15;

        ener_avg[ener_avgOffset] = av_pred_en;
    }

    /*
     * ec_gain_code
     * 
     * 
     * Parameters: st.gbuf I: last five gains st.past_gain_code I: past gain pred_state B: MA predictor state state I:
     * state of the state machine gain_code O: decoded innovation gain
     * 
     * Function: Conceal the codebook gain
     * 
     * Returns: void
     */
    static void ec_gain_code(ec_gain_codeState st, gc_predState pred_state, short state, int[] gain_code, int gain_codeOffset)
    {
        int tmp;
        int[] qua_ener_MR122 = new int[1];
        int[] qua_ener = new int[1];

        /* calculate median of last five gain values */
        tmp = gmed_n(st.gbuf, 0, 5);

        /* new gain = minimum(median, past_gain) * cdown[state] */
        if (tmp > st.past_gain_code)
        {
            tmp = st.past_gain_code;
        }
        tmp = (tmp * cdown[state]) >> 15;
        gain_code[gain_codeOffset] = tmp;

        /*
         * update table of past quantized energies with average of current values
         */
        gc_pred_average_limited(pred_state, qua_ener_MR122, 0, qua_ener, 0);
        gc_pred_update(pred_state, qua_ener_MR122[0], qua_ener[0]);
    }

    /*
     * ec_gain_code_update
     * 
     * 
     * Parameters: st.gbuf B: last five gains st.past_gain_code O: past gain st.prev_gc B previous gain bfi I: bad
     * indicator prev_bf I: previous frame bad indicator gain_code O: decoded innovation gain
     * 
     * Function: Update the codebook gain concealment state
     * 
     * Returns: void
     */
    static void ec_gain_code_update(ec_gain_codeState st, short bfi, short prev_bf, int[] gain_code, int gain_codeOffset)
    {
        /* limit gain_code by previous good gain if previous frame was bad */
        if (bfi == 0)
        {
            if (prev_bf != 0)
            {
                if (gain_code[gain_codeOffset] > st.prev_gc)
                {
                    gain_code[gain_codeOffset] = st.prev_gc;
                }
            }
            st.prev_gc = gain_code[gain_codeOffset];
        }

        /* update EC states: previous gain, gain buffer */
        st.past_gain_code = gain_code[gain_codeOffset];
        st.gbuf[0] = st.gbuf[1];
        st.gbuf[1] = st.gbuf[2];
        st.gbuf[2] = st.gbuf[3];
        st.gbuf[3] = st.gbuf[4];
        st.gbuf[4] = gain_code[gain_codeOffset];
        return;
    }

    /*
     * d_gain_code
     * 
     * 
     * Parameters: pred_state B: MA predictor state mode I: AMR mode (MR795 or MR122) index I: received quantization
     * index code I: innovation codevector gain_code O: decoded innovation gain
     * 
     * Function: Decode the fixed codebook gain using the received index
     * 
     * Returns: void
     */
    static void d_gain_code(gc_predState pred_state, int mode, int index, int code[], int codeOffset, int[] gain_code, int gain_codeOffset)
    {
        int[] exp = new int[1];
        int[] frac = new int[1];
        int g_code0, qua_ener_MR122, qua_ener;
        int[] exp_inn_en = new int[1];
        int[] frac_inn_en = new int[1];
        int tmp, tmp2, i;
        int[] p;
        int pIndex;

        /*
         * Decode codebook gain
         */
        gc_pred(pred_state, mode, code, codeOffset, exp, 0, frac, 0, exp_inn_en, 0, frac_inn_en, 0);
        // p = &qua_gain_code[( ( index + index )+ index )];
        p = qua_gain_code;
        pIndex = ((index + index) + index);

        /* Different scalings between MR122 and the other modes */
        if (mode == MR122)
        {
            /* predicted gain */
            g_code0 = Pow2(exp[0], frac[0]);

            if (g_code0 <= 2047)
            {
                g_code0 = g_code0 << 4;
            }
            else
            {
                g_code0 = 32767;
            }
            gain_code[gain_codeOffset] = ((g_code0 * p[pIndex++]) >> 15) << 1;
            if ((gain_code[gain_codeOffset] & 0xFFFF8000) != 0)
            {
                gain_code[gain_codeOffset] = 32767;
            }

        }
        else
        {
            g_code0 = Pow2(14, frac[0]);
            tmp = (p[pIndex++] * g_code0) << 1;
            exp[0] = 9 - exp[0];

            if (exp[0] > 0)
            {
                tmp = tmp >> exp[0];
            }
            else
            {
                for (i = exp[0]; i < 0; i++)
                {
                    tmp2 = tmp << 1;
                    if (((tmp ^ tmp2) & 0x80000000) != 0)
                    {
                        tmp = ((tmp & 0x80000000) != 0) ? 0x80000000 : 0x7FFFFFFF;
                        break;
                    }
                    else
                    {
                        tmp = tmp2;
                    }
                }
            }
            gain_code[gain_codeOffset] = tmp >> 16;
            if ((gain_code[gain_codeOffset] & 0xFFFF8000) != 0)
            {
                gain_code[gain_codeOffset] = 32767;
            }
        }

        /*
         * update table of past quantized energies
         */
        qua_ener_MR122 = p[pIndex++];
        qua_ener = p[pIndex++];
        gc_pred_update(pred_state, qua_ener_MR122, qua_ener);
        return;
    }

    /*
     * Int_lsf
     * 
     * 
     * Parameters: lsf_old I: LSF vector at the 4th subframe of past frame lsf_new I: LSF vector at the 4th subframe of
     * present frame i_subfr I: current subframe lsf_out O: interpolated LSF parameters for current subframe
     * 
     * Function: Interpolates the LSFs for selected subframe
     * 
     * The LSFs are interpolated at the 1st, 2nd and 3rd ubframe and only forwarded at the 4th subframe.
     * 
     * sf1: 3/4 F0 + 1/4 F1 sf2: 1/2 F0 + 1/2 F1 sf3: 1/4 F0 + 3/4 F1 sf4: F1
     * 
     * Returns: void
     */
    static void Int_lsf(int lsf_old[], int lsf_oldOffset, int lsf_new[], int lsf_newOffset, int i_subfr, int lsf_out[], int lsf_outOffset)
    {
        int i;

        switch (i_subfr)
        {
            case 0:
                for (i = 0; i < 10; i++)
                {
                    lsf_out[i + lsf_outOffset] = lsf_old[i + lsf_oldOffset] - (lsf_old[i + lsf_oldOffset] >> 2) + (lsf_new[i + lsf_newOffset] >> 2);
                }
                break;

            case 40:
                for (i = 0; i < 10; i++)
                {
                    lsf_out[i + lsf_outOffset] = (lsf_old[i + lsf_oldOffset] >> 1) + (lsf_new[i + lsf_newOffset] >> 1);
                }
                break;

            case 80:
                for (i = 0; i < 10; i++)
                {
                    lsf_out[i + lsf_outOffset] = (lsf_old[i + lsf_oldOffset] >> 2) - (lsf_new[i + lsf_newOffset] >> 2) + lsf_new[i + lsf_newOffset];
                }
                break;

            case 120:
                memcpy(lsf_out, lsf_outOffset, lsf_new, lsf_newOffset, M);
                break;
        }
    }

    /*
     * Cb_gain_average
     * 
     * 
     * Parameters: st.cbGainHistory B: codebook gain history st.hangCount B: hangover counter mode I: AMR mode gain_code
     * I: codebook gain lsp I: The LSP for the current frame lspAver I: The average of LSP for 8 frames bfi I: bad frame
     * indication prev_bf I: previous bad frame indication pdfi I: potential degraded bad frame indication prev_pdf I:
     * previous potential degraded bad frame indication inBackgroundNoise I: background noise decision voicedHangover I:
     * number of frames after last voiced frame
     * 
     * Function: The mixed codebook gain, used to make codebook gain more smooth in background
     * 
     * 
     * Returns: void
     */
    static int Cb_gain_average(Cb_gain_averageState st, int mode, int gain_code, int lsp[], int lspOffset, int lspAver[], int lspAverOffset,
            short bfi, short prev_bf, short pdfi, short prev_pdf, int inBackgroundNoise, int voicedHangover)
    {
        int[] tmp = new int[M];
        int i, cbGainMix, tmp_diff, bgMix, cbGainMean, sum, diff, tmp1, tmp2;
        int shift1, shift2, shift;

        /* set correct cbGainMix for MR74, MR795, MR122 */
        cbGainMix = gain_code;

        /*
         * Store list of CB gain needed in the CB gain averaging *
         */
        st.cbGainHistory[0] = st.cbGainHistory[1];
        st.cbGainHistory[1] = st.cbGainHistory[2];
        st.cbGainHistory[2] = st.cbGainHistory[3];
        st.cbGainHistory[3] = st.cbGainHistory[4];
        st.cbGainHistory[4] = st.cbGainHistory[5];
        st.cbGainHistory[5] = st.cbGainHistory[6];
        st.cbGainHistory[6] = gain_code;

        /* compute lsp difference */
        for (i = 0; i < M; i++)
        {
            tmp1 = Math.abs(lspAver[i + lspAverOffset] - lsp[i + lspOffset]);
            shift1 = 0;
            if (tmp1 != 0)
            {
                while ((tmp1 & 0x2000) == 0)
                {
                    shift1++;
                    tmp1 = tmp1 << 1;
                }
            }
            tmp2 = lspAver[i + lspAverOffset];
            shift2 = 0;
            if (tmp2 != 0)
            {
                while ((tmp2 & 0x4000) == 0)
                {
                    shift2++;
                    tmp2 = tmp2 << 1;
                }
            }
            tmp[i] = (tmp1 << 15) / tmp2;
            shift = 2 + shift1 - shift2;

            if (shift >= 0)
            {
                tmp[i] = tmp[i] >> shift;
            }
            else
            {
                tmp[i] = tmp[i] << -(shift);
            }
        }
        diff = tmp[0] + tmp[1] + tmp[2] + tmp[3] + tmp[4] + tmp[5] + tmp[6] + tmp[7] + tmp[8] + tmp[9];

        /* saturate */
        if (diff > 32767)
        {
            diff = 32767;
        }

        /* Compute hangover */
        st.hangVar += 1;

        if (diff <= 5325)
        {
            st.hangVar = 0;
        }

        if (st.hangVar > 10)
        {
            /* Speech period, reset hangover variable */
            st.hangCount = 0;
        }

        /* Compute mix constant (bgMix) */
        bgMix = 8192;

        /* MR475, MR515, MR59, MR67, MR102 */
        if ((mode <= MR67) | (mode == MR102))
        {
            /* disable mix if too short time since */
            if ((st.hangCount >= 40) & (diff <= 5325)) /* 0.65 in Q13 */
            {
                /* if errors and presumed noise make smoothing probability stronger */
                if (((((pdfi != 0) & (prev_pdf != 0)) | (bfi != 0) | (prev_bf != 0)) & ((voicedHangover > 1)) & (inBackgroundNoise != 0) & (mode < MR67)))
                {
                    /* bgMix = min(0.25, max(0.0, diff-0.55)) / 0.25; */
                    tmp_diff = diff - 4506; /* 0.55 in Q13 */

                    /* max(0.0, diff-0.55) */
                    tmp1 = 0;

                    if (tmp_diff > 0)
                    {
                        tmp1 = tmp_diff;
                    }

                    /* min(0.25, tmp1) */
                    if (2048 >= tmp1)
                    {
                        bgMix = tmp1 << 2;
                    }
                }
                else
                {
                    /* bgMix = min(0.25, max(0.0, diff-0.40)) / 0.25; */
                    tmp_diff = diff - 3277; /* 0.4 in Q13 */

                    /* max(0.0, diff-0.40) */
                    tmp1 = 0;

                    if (tmp_diff > 0)
                    {
                        tmp1 = tmp_diff;
                    }

                    /* min(0.25, tmp1) */
                    if (2048 >= tmp1)
                    {
                        bgMix = tmp1 << 2;
                    }
                }
            }

            /*
             * Smoothen the cb gain trajectory smoothing depends on mix constant bgMix
             */
            sum = st.cbGainHistory[2] + st.cbGainHistory[3] + st.cbGainHistory[4] + st.cbGainHistory[5] + st.cbGainHistory[6];

            if (sum > 163822)
            {
                cbGainMean = 32767;
            }
            else
            {
                cbGainMean = (3277 * sum + 0x00002000) >> 14; /* Q1 */
            }

            /* more smoothing in error and bg noise (NB no DFI used here) */
            if (((bfi != 0) | (prev_bf != 0)) & (inBackgroundNoise != 0) & (mode < MR67))
            {
                sum = 9362 * (st.cbGainHistory[0] + st.cbGainHistory[1] + st.cbGainHistory[2] + st.cbGainHistory[3] + st.cbGainHistory[4]
                        + st.cbGainHistory[5] + st.cbGainHistory[6]);
                cbGainMean = (sum + 0x00008000) >> 16; /* Q1 */
            }

            /* cbGainMix = bgMix*cbGainMix + (1-bgMix)*cbGainMean; */
            sum = bgMix * cbGainMix; /* sum in Q14 */
            sum += cbGainMean << 13;
            sum -= bgMix * cbGainMean;
            cbGainMix = (sum + 0x00001000) >> 13;

            /* Q1 */
        }
        st.hangCount += 1;
        if ((st.hangCount & 0x80000000) != 0)
        {
            st.hangCount = 40;
        }
        return cbGainMix;
    }

    /*
     * ph_disp
     * 
     * 
     * Parameters: state.gainMem B: LTP gain memory state.prevCbGain B: Codebook gain memory mode I: AMR mode x B: LTP
     * excitation signal . total excitation signal cbGain I: Codebook gain ltpGain I: LTP gain inno B: Innovation vector
     * pitch_fac I: pitch factor used to scale the LTP excitation tmp_shift I: shift factor applied to sum of scaled LTP
     * ex & innov. before rounding
     * 
     * Function: Adaptive phase dispersion; forming of total excitation
     * 
     * 
     * Returns: void
     */
    static void ph_disp(ph_dispState state, int mode, int x[], int xOffset, int cbGain, int ltpGain, int inno[], int innoOffset, int pitch_fac,
            int tmp_shift)
    {
        int[] inno_sav = new int[L_SUBFR];
        int[] ps_poss = new int[L_SUBFR];
        int i, i1, impNr, temp1, temp2, j, nze, nPulse, ppos;
        int[] ph_imp; /* Pointer to phase dispersion filter */

        /* Update LTP gain memory */
        state.gainMem[4] = state.gainMem[3];
        state.gainMem[3] = state.gainMem[2];
        state.gainMem[2] = state.gainMem[1];
        state.gainMem[1] = state.gainMem[0];
        state.gainMem[0] = ltpGain;

        /* basic adaption of phase dispersion */
        /* no dispersion */
        impNr = 2;

        /* if (ltpGain < 0.9) */
        if (ltpGain < PHDTHR2LTP)
        {
            /* maximum dispersion */
            impNr = 0;

            /* if (ltpGain > 0.6 */
            if (ltpGain > PHDTHR1LTP)
            {
                /* medium dispersion */
                impNr = 1;
            }
        }

        /* onset indicator */
        /* onset = (cbGain > onFact * cbGainMem[0]) */
        temp1 = ((state.prevCbGain * ONFACTPLUS1) + 0x1000) >> 13;

        if (cbGain > temp1)
        {
            state.onset = ONLENGTH;
        }
        else
        {
            if (state.onset > 0)
            {
                state.onset--;
            }
        }

        /*
         * if not onset, check ltpGain buffer and use max phase dispersion if half or more of the ltpGain-parameters say
         * so
         */
        if (state.onset == 0)
        {
            /* Check LTP gain memory and set filter accordingly */
            i1 = 0;

            for (i = 0; i < PHDGAINMEMSIZE; i++)
            {
                if (state.gainMem[i] < PHDTHR1LTP)
                {
                    i1++;
                }
            }

            if (i1 > 2)
            {
                impNr = 0;
            }
        }

        /* Restrict decrease in phase dispersion to one step if not onset */
        if ((impNr > (state.prevState + 1)) & (state.onset == 0))
        {
            impNr--;
        }

        /* if onset, use one step less phase dispersion */
        if ((impNr < 2) & (state.onset > 0))
        {
            impNr++;
        }

        /* disable for very low levels */
        if (cbGain < 10)
        {
            impNr = 2;
        }

        if (state.lockFull == 1)
        {
            impNr = 0;
        }

        /* update static memory */
        state.prevState = impNr;
        state.prevCbGain = cbGain;

        /*
         * do phase dispersion for all modes but 12.2 and 7.4; don't modify the innovation if impNr >=2 (= no phase
         * disp)
         */
        if ((mode != MR122) & (mode != MR102) & (mode != MR74) & (impNr < 2))
        {
            /*
             * track pulse positions, save innovation, and initialize new innovation
             */
            nze = 0;

            for (i = 0; i < L_SUBFR; i++)
            {
                if (inno[i] != 0)
                {
                    ps_poss[nze] = i;
                    nze++;
                }
            }
            memcpy(inno_sav, 0, inno, innoOffset, L_SUBFR);
            memset(inno, innoOffset, 0, L_SUBFR);

            /* Choose filter corresponding to codec mode and dispersion criterium */
            ph_imp = ph_imp_mid;

            if (impNr == 0)
            {
                ph_imp = ph_imp_low;
            }

            if (mode == MR795)
            {
                ph_imp = ph_imp_mid_MR795;

                if (impNr == 0)
                {
                    ph_imp = ph_imp_low_MR795;
                }
            }

            /* Do phase dispersion of innovation */
            for (nPulse = 0; nPulse < nze; nPulse++)
            {
                ppos = ps_poss[nPulse];

                /* circular convolution with impulse response */
                j = 0;

                for (i = ppos; i < L_SUBFR; i++)
                {
                    /* inno[i1] += inno_sav[ppos] * ph_imp[i1-ppos] */
                    temp1 = (inno_sav[ppos] * ph_imp[j++]) >> 15;
                    inno[i + innoOffset] = inno[i + innoOffset] + temp1;
                }

                for (i = 0; i < ppos; i++)
                {
                    /* inno[i] += inno_sav[ppos] * ph_imp[L_SUBFR-ppos+i] */
                    temp1 = (inno_sav[ppos] * ph_imp[j++]) >> 15;
                    inno[i + innoOffset] = inno[i + innoOffset] + temp1;
                }
            }
        }

        /*
         * compute total excitation for synthesis part of decoder (using modified innovation if phase dispersion is
         * active)
         */
        for (i = 0; i < L_SUBFR; i++)
        {
            /* x[i] = gain_pit*x[i] + cbGain*code[i]; */
            temp1 = x[i + xOffset] * pitch_fac + inno[i + innoOffset] * cbGain;
            temp2 = temp1 << tmp_shift;
            x[i + xOffset] = (temp2 + 0x4000) >> 15;
            if (Math.abs(x[i + xOffset]) > 32767)
            {
                if (((temp1 ^ temp2) & 0x80000000) != 0)
                {
                    x[i + xOffset] = ((temp1 & 0x80000000) != 0) ? -32768 : 32767;
                }
                else
                {
                    x[i + xOffset] = ((temp2 & 0x80000000) != 0) ? -32768 : 32767;
                }
            }
        }
        return;
    }

    /*
     * sqrt_l_exp
     * 
     * 
     * Parameters: x I: input value exp O: right shift to be applied to result
     * 
     * Function: Sqrt with exponent value.
     * 
     * y = sqrt(x) x = f * 2^-e, 0.5 <= f < 1 (normalization) y = sqrt(f) * 2^(-e/2)
     * 
     * a) e = 2k -. y = sqrt(f) * 2^-k (k = e div 2, 0.707 <= sqrt(f) < 1) b) e = 2k+1 -. y = sqrt(f/2) * 2^-k (k = e
     * div 2, 0.5 <= sqrt(f/2) < 0.707)
     * 
     * 
     * Returns: y output value
     */
    static int sqrt_l_exp(int x, int[] exp, int expOffset)
    {
        int y, a, i, tmp;
        int e;

        if (x <= (int) 0)
        {
            exp[expOffset] = 0;
            return (int) 0;
        }
        e = 0;
        if (x != 0)
        {
            tmp = x;
            while ((tmp & 0x40000000) == 0)
            {
                e++;
                tmp = tmp << 1;
            }
        }
        e = e & 0xFFFE;
        x = (x << e);
        exp[expOffset] = (short) e;
        x = (x >> 9);
        i = (short) (x >> 16);
        x = (x >> 1);
        a = x & (short) 0x7fff;
        i = (i - 16);
        y = (sqrt_table[i] << 16);
        tmp = (sqrt_table[i] - sqrt_table[i + 1]);
        y -= (tmp * a) << 1;
        return (y);
    }

    /*
     * Ex_ctrl
     * 
     * 
     * Parameters: excitation B: Current subframe excitation excEnergy I: Exc. Energy, sqrt(totEx*totEx) exEnergyHist I:
     * History of subframe energies voicedHangover I: number of frames after last voiced frame prevBFI I: Set i previous
     * bad frame indicators carefulFlag I: Restrict dymamic in scaling
     * 
     * Function: Charaterice synthesis speech and detect background noise
     * 
     * Returns: background noise decision; 0 = no bgn, 1 = bgn
     */
    static short Ex_ctrl(int excitation[], int excitationOffset, int excEnergy, int exEnergyHist[], int exEnergyHistOffset, int voicedHangover,
            short prevBFI, short carefulFlag)
    {
        int i, testEnergy, scaleFactor, avgEnergy, prevEnergy, T0;
        int exp;

        /* get target level */
        avgEnergy = gmed_n(exEnergyHist, exEnergyHistOffset, 9);
        prevEnergy = (exEnergyHist[7 + exEnergyHistOffset] + exEnergyHist[8 + exEnergyHistOffset]) >> 1;

        if (exEnergyHist[8 + exEnergyHistOffset] < prevEnergy)
        {
            prevEnergy = exEnergyHist[8 + exEnergyHistOffset];
        }

        /* upscaling to avoid too rapid energy rises for some cases */
        if ((excEnergy < avgEnergy) & (excEnergy > 5))
        {
            /* testEnergy = 4*prevEnergy; */
            testEnergy = prevEnergy << 2;

            if ((voicedHangover < 7) || prevBFI != 0)
            {
                /* testEnergy = 3*prevEnergy */
                testEnergy = testEnergy - prevEnergy;
            }

            if (avgEnergy > testEnergy)
            {
                avgEnergy = testEnergy;
            }

            /* scaleFactor=avgEnergy/excEnergy in Q0 */
            exp = 0;
            if (excEnergy != 0)
            {
                while ((excEnergy & 0x4000) == 0)
                {
                    exp++;
                    excEnergy = excEnergy << 1;
                }
            }
            excEnergy = 536838144 / excEnergy;
            T0 = (avgEnergy * excEnergy) << 1;
            T0 = (T0 >> (20 - exp));

            if (T0 > 32767)
            {
                /* saturate */
                T0 = 32767;
            }
            scaleFactor = T0;

            /* test if scaleFactor > 3.0 */
            if ((carefulFlag != 0) & (scaleFactor > 3072))
            {
                scaleFactor = 3072;
            }

            /* scale the excitation by scaleFactor */
            for (i = 0; i < L_SUBFR; i++)
            {
                T0 = (scaleFactor * excitation[i + excitationOffset]) << 1;
                T0 = (T0 >> 11);
                excitation[i + excitationOffset] = T0;
            }
        }
        return 0;
    }

    /*
     * Inv_sqrt
     * 
     * 
     * Parameters: x I: input value
     * 
     * Function: 1/sqrt(x)
     * 
     * Returns: y 1/sqrt(x)
     */
    static int Inv_sqrt(int x)
    {
        int i, a, tmp, exp;
        int y;

        if (x <= (int) 0)
        {
            return ((int) 0x3fffffffL);
        }
        exp = 0;
        while ((x & 0x40000000) == 0)
        {
            exp++;
            x = x << 1;
        }

        /* x is normalized */
        exp = (30 - exp);

        /* If exponent even . shift right */
        if ((exp & 1) == 0)
        {
            x = (x >> 1);
        }
        exp = (exp >> 1);
        exp = (exp + 1);
        x = (x >> 9);

        /* Extract b25-b31 */
        i = (short) (x >> 16);

        /* Extract b10-b24 */
        x = (x >> 1);
        a = x & (short) 0x7fff;
        i = (i - 16);

        /* table[i] << 16 */
        y = inv_sqrt_table[i] << 16;

        /* table[i] - table[i+1]) */
        tmp = (inv_sqrt_table[i] - inv_sqrt_table[i + 1]);

        /* y -= tmp*a*2 */
        y -= (tmp * a) << 1;

        /* denormalization */
        y = (y >> exp);
        return (y);
    }

    /*
     * energy_old
     * 
     * 
     * Parameters: in I: input value
     * 
     * Function: Energy of signal
     * 
     * Returns: Energy
     */
    static int energy_old(int in[], int inOffset)
    {
        int temp, i, sum = 0;

        for (i = 0; i < L_SUBFR; i += 8)
        {
            temp = in[i + inOffset] >> 2;
            sum += temp * temp;
            temp = in[i + 1 + inOffset] >> 2;
            sum += temp * temp;
            temp = in[i + 2 + inOffset] >> 2;
            sum += temp * temp;
            temp = in[i + 3 + inOffset] >> 2;
            sum += temp * temp;
            temp = in[i + 4 + inOffset] >> 2;
            sum += temp * temp;
            temp = in[i + 5 + inOffset] >> 2;
            sum += temp * temp;
            temp = in[i + 6 + inOffset] >> 2;
            sum += temp * temp;
            temp = in[i + 7 + inOffset] >> 2;
            sum += temp * temp;
        }

        if ((sum & 0xC0000000) != 0)
        {
            return 0x7FFFFFFF;
        }
        return (sum << 1);
    }

    /*
     * energy_new
     * 
     * 
     * Parameters: in I: input value
     * 
     * Function: Energy of signal
     * 
     * Returns: Energy
     */
    static int energy_new(int in[], int inOffset)
    {
        int i, s = 0, overflow = 0;

        s += in[0 + inOffset] * in[0 + inOffset];
        for (i = 1; i < L_SUBFR; i += 3)
        {
            s += in[i + inOffset] * in[i + inOffset];
            s += in[i + 1 + inOffset] * in[i + 1 + inOffset];
            s += in[i + 2 + inOffset] * in[i + 2 + inOffset];

            if ((s & 0xC0000000) != 0)
            {
                overflow = 1;
                break;
            }
        }

        /* check for overflow */
        if (overflow != 0)
        {
            s = energy_old(in, inOffset);
        }
        else
        {
            s = (s >> 3);
        }
        return s;
    }

    /*
     * agc2
     * 
     * 
     * Parameters: sig_in I: Post_Filter input signal sig_out B: Post_Filter output signal
     * 
     * Function: Scales the excitation on a subframe basis
     * 
     * Returns: Energy
     */
    static void agc2(int[] sig_in, int sig_inOffset, int[] sig_out, int sig_outOffset)
    {
        int s;
        int i, exp;
        short gain_in, gain_out, g0;

        // int sumIn = 0;
        // int sumOut = 0;
        /* calculate gain_out with exponent */
        s = energy_new(sig_out, sig_outOffset);
        // System.out.println("out engergy is "+s);
        if (s == 0)
        {
            return;
        }
        exp = 0;
        while ((s & 0x20000000) == 0)
        {
            exp++;
            s = s << 1;
        }

        gain_out = (short) ((s + 0x00008000L) >> 16);

        /* calculate gain_in with exponent */
        s = energy_new(sig_in, sig_inOffset);
        // System.out.println("in engergy is "+s);
        if (s == 0)
        {
            g0 = 0;
        }
        else
        {
            i = 0;
            while ((s & 0x40000000) == 0)
            {
                i++;
                s = s << 1;
            }

            if (s < 0x7fff7fff)
            {
                gain_in = (short) ((s + 0x00008000L) >> 16);
            }
            else
            {
                gain_in = 32767;
            }
            exp = (exp - i);

            /*
             * g0 = sqrt(gain_in/gain_out);
             */
            /* s = gain_out / gain_in */
            s = (gain_out << 15) / gain_in;
            s = (s << 7);

            if (exp > 0)
            {
                s = (s >> exp);
            }
            else
            {
                s = (s << (-exp));
            }
            s = Inv_sqrt(s);
            g0 = (short) (((s << 9) + 0x00008000L) >> 16);
        }

        /* sig_out(n) = gain(n) * sig_out(n) */
        for (i = 0; i < L_SUBFR; i++)
        {
            sig_out[i + sig_outOffset] = (sig_out[i + sig_outOffset] * g0) >> 12;
            // sumOut += sig_out[i + sig_outOffset];
        }
        // System.out.println("SumOut in argc2 is "+sumOut+" g0 is "+g0);
        return;
    }

    /*
     * Bgn_scd
     * 
     * 
     * Parameters: st.frameEnergyHist B: Frame Energy memory st.bgHangover B: Background hangover counter ltpGainHist I:
     * LTP gain history speech I: synthesis speech frame voicedHangover O: number of frames after last voiced frame
     * 
     * Function: Charaterice synthesis speech and detect background noise
     * 
     * Returns: inbgNoise background noise decision; 0 = no bgn, 1 = bgn
     */
    static short Bgn_scd(Bgn_scdState st, int ltpGainHist[], int ltpGainHistOffset, int speech[], int speechOffset, int[] voicedHangover,
            int voicedHangoverOffset)
    {
        int temp, ltpLimit, frame_energyMin, currEnergy, noiseFloor, maxEnergy, maxEnergyLastPart, s, i;
        short prevVoiced, inbgNoise;

        /*
         * Update the inBackgroundNoise flag (valid for use in next frame if BFI) it now works as a energy detector
         * floating on top not as good as a VAD.
         */
        s = 0;

        for (i = 0; i < L_FRAME; i++)
        {
            s += speech[i + speechOffset] * speech[i + speechOffset];
        }

        if ((s < 0xFFFFFFF) & (s >= 0))
        {
            currEnergy = s >> 13;
        }
        else
        {
            currEnergy = 32767;
        }
        frame_energyMin = 32767;

        for (i = 0; i < L_ENERGYHIST; i++)
        {
            if (st.frameEnergyHist[i] < frame_energyMin)
            {
                frame_energyMin = st.frameEnergyHist[i];
            }
        }

        /* Frame Energy Margin of 16 */
        noiseFloor = frame_energyMin << 4;
        maxEnergy = st.frameEnergyHist[0];

        for (i = 1; i < L_ENERGYHIST - 4; i++)
        {
            if (maxEnergy < st.frameEnergyHist[i])
            {
                maxEnergy = st.frameEnergyHist[i];
            }
        }
        maxEnergyLastPart = st.frameEnergyHist[2 * L_ENERGYHIST / 3];

        for (i = 2 * L_ENERGYHIST / 3 + 1; i < L_ENERGYHIST; i++)
        {
            if (maxEnergyLastPart < st.frameEnergyHist[i])
            {
                maxEnergyLastPart = st.frameEnergyHist[i];
            }
        }

        /* false */
        inbgNoise = 0;

        /*
         * Do not consider silence as noise Do not consider continuous high volume as noise Or if the current noise
         * level is very low Mark as noise if under current noise limit OR if the maximum energy is below the upper
         * limit
         */
        if ((maxEnergy > LOWERNOISELIMIT) & (currEnergy < FRAMEENERGYLIMIT) & (currEnergy > LOWERNOISELIMIT)
                & ((currEnergy < noiseFloor) || (maxEnergyLastPart < UPPERNOISELIMIT)))
        {
            if ((st.bgHangover + 1) > 30)
            {
                st.bgHangover = 30;
            }
            else
            {
                st.bgHangover += 1;
            }
        }
        else
        {
            st.bgHangover = 0;
        }

        /* make final decision about frame state, act somewhat cautiosly */
        if (st.bgHangover > 1)
        {
            inbgNoise = 1; /* true */
        }

        for (i = 0; i < L_ENERGYHIST - 1; i++)
        {
            st.frameEnergyHist[i] = st.frameEnergyHist[i + 1];
        }
        st.frameEnergyHist[L_ENERGYHIST - 1] = currEnergy;

        /*
         * prepare for voicing decision; tighten the threshold after some time in noise
         */
        ltpLimit = 13926; /* 0.85 Q14 */

        if (st.bgHangover > 8)
        {
            ltpLimit = 15565; /* 0.95 Q14 */
        }

        if (st.bgHangover > 15)
        {
            ltpLimit = 16383; /* 1.00 Q14 */
        }

        /* weak sort of voicing indication. */
        prevVoiced = 0; /* false */

        if (gmed_n(ltpGainHist, 4 + ltpGainHistOffset, 5) > ltpLimit)
        {
            prevVoiced = 1; /* true */
        }

        if (st.bgHangover > 20)
        {
            if (gmed_n(ltpGainHist, 0, 9) > ltpLimit)
            {
                prevVoiced = 1; /* true */
            }
            else
            {
                prevVoiced = 0; /* false */
            }
        }

        if (prevVoiced != 0)
        {
            voicedHangover[voicedHangoverOffset] = 0;
        }
        else
        {
            temp = voicedHangover[voicedHangoverOffset] + 1;

            if (temp > 10)
            {
                voicedHangover[voicedHangoverOffset] = 10;
            }
            else
            {
                voicedHangover[voicedHangoverOffset] = temp;
            }
        }
        return inbgNoise;
    }

    /*
     * dtx_dec_activity_update
     * 
     * 
     * Parameters: st.lsf_hist_ptr B: LSF history pointer st.lsf_hist B: LSF history lsf I: lsf frame I: noise frame
     * 
     * Function: Update lsp history and compute log energy.
     * 
     * Returns: void
     */
    static void dtx_dec_activity_update(dtx_decState st, int lsf[], int lsfOffset, int frame[], int frameOffset)
    {
        int frame_en;
        int[] log_en_e = new int[1];
        int[] log_en_m = new int[1];
        int log_en, i;

        /* update lsp history */
        st.lsf_hist_ptr += M;

        if (st.lsf_hist_ptr == 80)
        {
            st.lsf_hist_ptr = 0;
        }
        memcpy(st.lsf_hist, st.lsf_hist_ptr, lsf, lsfOffset, M);

        /* compute log energy based on frame energy */
        frame_en = 0; /* Q0 */

        for (i = 0; (i < L_FRAME); i++)
        {
            frame_en += frame[i + frameOffset] * frame[i + frameOffset];
            if ((frame_en & 0x80000000) != 0)
            {
                break;
            }
        }

        log_en = ((frame_en & 0xC0000000) != 0) ? 0x7FFFFFFE : (int) frame_en << 1;

        Log2(log_en, log_en_e, 0, log_en_m, 0);

        /* convert exponent and mantissa to short Q10 */
        log_en = log_en_e[0] << 10; /* Q10 */
        log_en = log_en + (log_en_m[0] >> 5);

        /* divide with L_FRAME i.e subtract with log2(L_FRAME) = 7.32193 */
        log_en = log_en - 8521;

        /*
         * insert into log energy buffer, no division by two as log_en in decoder is Q11
         */
        st.log_en_hist_ptr += 1;

        if (st.log_en_hist_ptr == DTX_HIST_SIZE)
        {
            st.log_en_hist_ptr = 0;
        }
        st.log_en_hist[st.log_en_hist_ptr] = log_en; /* Q11 */
    }

    /*
     * static void printCode(int code[]){ int sumCode = 0; for(int i = 0;i < 40;i ++){ sumCode += code[i]; }
     * System.out.println("sumCode:"+sumCode); }
     */
    /*
     * Decoder_amr
     * 
     * 
     * Parameters: st B: State variables mode I: AMR mode parm I: vector of synthesis parameters frame_type I: received
     * frame type synth O: synthesis speech A_t O: decoded LP filter in 4 subframes
     * 
     * Function: Speech decoder routine
     * 
     * Returns: void
     */
    static void Decoder_amr(Decoder_amrState st, int mode, short parm[], int parmOffset, int frame_type, int synth[], int synthOffset, int A_t[],
            int A_tOffset)
    {
        /* LSPs */
        int[] lsp_new = new int[M];
        int[] lsp_mid = new int[M];

        /* LSFs */
        int[] prev_lsf = new int[M];
        int[] lsf_i = new int[M];

        /* Algebraic codevector */
        int[] code = new int[L_SUBFR];

        /* excitation */
        int[] excp = new int[L_SUBFR];
        int[] exc_enhanced = new int[L_SUBFR];

        /* Scalars */
        int[] T0_frac = new int[1];
        int[] T0 = new int[1];
        int[] temp = new int[1];
        int i, i_subfr, overflow, index, temp2, subfrNr, excEnergy;
        int[] gain_code = new int[1];
        int gain_code_mix, pit_sharp, pit_flag, pitch_fac, t0_min, t0_max;
        int[] gain_pit = new int[1];
        int evenSubfr = 0, index_mr475 = 0;
        int[] Az; /* Pointer on A_t */
        int AzIndex;
        short flag4, carefulFlag;
        short delta_frc_low, delta_frc_range, tmp_shift;
        short bfi = 0, pdfi = 0;
        /* bad frame indication flag, potential degraded bad frame flag */

        int newDTXState; /* SPEECH , DTX, DTX_MUTE */

        /* find the new DTX state SPEECH OR DTX */
        newDTXState = rx_dtx_handler(st.dtxDecoderState, frame_type);

        /* DTX actions */
        if (newDTXState != SPEECH)
        {
            Decoder_amr_reset(st, MRDTX);
            // System.out.println("dtx_dec");
            dtx_dec(st.dtxDecoderState, st.mem_syn, 0, st.lsfState, st.pred_state, st.Cb_gain_averState, newDTXState, mode, parm, synth, A_t);

            /* update average lsp */
            Lsf_lsp(st.lsfState.past_lsf_q, 0, st.lsp_old, 0);
            lsp_avg(st.lsp_avg_st, st.lsfState.past_lsf_q, 0);
            st.dtxDecoderState.dtxGlobalState = newDTXState;
            return;
        }

        /* SPEECH action state machine */
        if (table_speech_bad[frame_type] != 0)
        {
            bfi = 1;

            if (frame_type != RX_SPEECH_BAD)
            {
                short[] seed = new short[1];
                Build_CN_param(seed, 0, mode, parm);
                st.nodataSeed = seed[0];
            }
        }
        else if (frame_type == RX_SPEECH_DEGRADED)
        {
            pdfi = 1;
        }

        if (bfi != 0)
        {
            st.state += 1;
        }
        else if (st.state == 6)
        {
            st.state = 5;
        }
        else
        {
            st.state = 0;
        }

        if (st.state > 6)
        {
            st.state = 6;
        }

        /*
         * If this frame is the first speech frame after CNI period, set the BFH state machine to an appropriate state
         * depending on whether there was DTX muting before start of speech or not If there was DTX muting, the first
         * speech frame is muted. If there was no DTX muting, the first speech frame is not muted. The BFH state machine
         * starts from state 5, however, to keep the audible noise resulting from a SID frame which is erroneously
         * interpreted as a good speech frame as small as possible (the decoder output in this case is quickly muted)
         */
        if (st.dtxDecoderState.dtxGlobalState == DTX)
        {
            st.state = 5;
            st.prev_bf = 0;
        }
        else if (st.dtxDecoderState.dtxGlobalState == DTX_MUTE)
        {
            st.state = 5;
            st.prev_bf = 1;
        }

        /* save old LSFs for CB gain smoothing */
        memcpy(prev_lsf, 0, st.lsfState.past_lsf_q, 0, M);

        /*
         * decode LSF parameters and generate interpolated lpc coefficients for the 4 subframes
         */
        if (mode != MR122)
        {
            D_plsf_3(st.lsfState, mode, bfi, parm, parmOffset, lsp_new);

            /* Advance synthesis parameters pointer */
            parmOffset += 3;
            Int_lpc_1to3(st.lsp_old, 0, lsp_new, 0, A_t, A_tOffset);
        }
        else
        {
            D_plsf_5(st.lsfState, bfi, parm, parmOffset, lsp_mid, 0, lsp_new, 0);

            /* Advance synthesis parameters pointer */
            parmOffset += 5;
            Int_lpc_1and3(st.lsp_old, 0, lsp_mid, 0, lsp_new, 0, A_t, A_tOffset);
        }

        /* update the LSPs for the next frame */
        memcpy(st.lsp_old, 0, lsp_new, 0, M);

        /*
         * Loop for every subframe in the analysis frame
         * 
         * The subframe size is L_SUBFR and the loop is repeated L_FRAME/L_SUBFR times * - decode the pitch delay -
         * decode algebraic code - decode pitch and codebook gains - find the excitation and compute synthesis speech
         */
        /* pointer to interpolated LPC parameters */
        Az = A_t;
        AzIndex = A_tOffset;
        // System.out.print("A_t sum:");
        // printSum(A_t,A_tOffset, AZ_SIZE);
        evenSubfr = 0;
        subfrNr = -1;

        for (i_subfr = 0; i_subfr < L_FRAME; i_subfr += L_SUBFR)
        {

            subfrNr += 1;
            evenSubfr = 1 - evenSubfr;

            /* flag for first and 3th subframe */
            pit_flag = i_subfr;

            if (i_subfr == L_FRAME_BY2)
            {
                if ((mode != MR475) & (mode != MR515))
                {
                    pit_flag = 0;
                }
            }

            /* pitch index */
            index = parm[parmOffset++];

            /*
             * decode pitch lag and find adaptive codebook vector.
             */
            if (mode != MR122)
            {
                /*
                 * flag4 indicates encoding with 4 bit resolution; this is needed for mode MR475, MR515, MR59 and MR67
                 */
                flag4 = 0;

                if ((mode == MR475) || (mode == MR515) || (mode == MR59) || (mode == MR67))
                {
                    flag4 = 1;
                }

                /*
                 * get ranges for the t0_min and t0_max only needed in delta decoding
                 */
                delta_frc_low = 5;
                delta_frc_range = 9;

                if (mode == MR795)
                {
                    delta_frc_low = 10;
                    delta_frc_range = 19;
                }
                t0_min = st.old_T0 - delta_frc_low;

                if (t0_min < PIT_MIN)
                {
                    t0_min = PIT_MIN;
                }
                t0_max = t0_min + delta_frc_range;

                if (t0_max > PIT_MAX)
                {
                    t0_max = PIT_MAX;
                    t0_min = t0_max - delta_frc_range;
                }
                Dec_lag3(index, t0_min, t0_max, pit_flag, st.old_T0, T0, 0, T0_frac, 0, flag4);
                // System.out.printf("T0:%d,T0_frac:%d\n",T0[0],T0_frac[0]);
                st.T0_lagBuff = T0[0];

                if (bfi != 0)
                {
                    if (st.old_T0 < PIT_MAX)
                    {
                        /* Graceful pitch degradation */
                        st.old_T0 += 1;
                    }
                    T0[0] = st.old_T0;
                    T0_frac[0] = 0;

                    if ((st.inBackgroundNoise != 0) & (st.voicedHangover > 4) & ((mode == MR475) || (mode == MR515) || (mode == MR59)))
                    {
                        T0[0] = st.T0_lagBuff;
                    }
                }
                Pred_lt_3or6_40(st.exc, st.excIndex, T0[0], T0_frac[0], 1);
            }
            else
            {
                Dec_lag6(index, PIT_MIN_MR122, PIT_MAX, pit_flag, T0, 0, T0_frac, 0);

                if ((bfi != 0) || ((pit_flag != 0) & (index > 60)))
                {
                    st.T0_lagBuff = T0[0];
                    T0[0] = st.old_T0;
                    T0_frac[0] = 0;
                }
                Pred_lt_3or6_40(st.exc, st.excIndex, T0[0], T0_frac[0], 0);
            }

            /*
             * (MR122 only: Decode pitch gain.) Decode innovative codebook. set pitch sharpening factor
             */
            /* MR475, MR515 */
            if ((mode == MR475) || (mode == MR515))
            {
                /* index of position */
                index = parm[parmOffset++];

                /* signs */
                i = parm[parmOffset++];
                decode_2i40_9bits(subfrNr, i, index, code, 0);
                pit_sharp = st.sharp << 1;
            } /* MR59 */
            else if (mode == MR59)
            {
                /* index of position */
                index = parm[parmOffset++];

                /* signs */
                i = parm[parmOffset++];
                decode_2i40_11bits(i, index, code, 0);
                // printCode(code);
                pit_sharp = st.sharp << 1;
            } /* MR67 */
            else if (mode == MR67)
            {
                /* index of position */
                index = parm[parmOffset++];

                /* signs */
                i = parm[parmOffset++];
                decode_3i40_14bits(i, index, code, 0);
                pit_sharp = st.sharp << 1;
            } /* MR74, MR795 */
            else if (mode <= MR795)
            {
                /* index of position */
                index = parm[parmOffset++];

                /* signs */
                i = parm[parmOffset++];
                decode_4i40_17bits(i, index, code, 0);
                pit_sharp = st.sharp << 1;
            } /* MR102 */
            else if (mode == MR102)
            {
                decode_8i40_31bits(parm, parmOffset, code, 0);
                parmOffset += 7;
                pit_sharp = st.sharp << 1;
            } /* MR122 */
            else
            {
                index = parm[parmOffset++];

                if (bfi != 0)
                {
                    ec_gain_pitch(st.ec_gain_p_st, st.state, gain_pit, 0);
                }
                else
                {
                    gain_pit[0] = d_gain_pitch(mode, index);
                }
                ec_gain_pitch_update(st.ec_gain_p_st, bfi, st.prev_bf, gain_pit, 0);
                decode_10i40_35bits(parm, parmOffset, code, 0);
                parmOffset += 10;

                /*
                 * pit_sharp = gain_pit; if (pit_sharp > 1.0) pit_sharp = 1.0;
                 */
                pit_sharp = gain_pit[0];

                if (pit_sharp > 16383)
                {
                    pit_sharp = 32767;
                }
                else
                {
                    pit_sharp *= 2;
                }
            }

            /*
             * Add the pitch contribution to code[].
             */
            for (i = T0[0]; i < L_SUBFR; i++)
            {
                temp[0] = (code[i - T0[0]] * pit_sharp) >> 15;
                code[i] = code[i] + temp[0];
            }
            // printCode(code);

            /*
             * Decode codebook gain (MR122) or both pitch gain and codebook gain (all others) Update pitch sharpening
             * "sharp" with quantized gain_pit
             */
            if (mode == MR475)
            {
                /* read and decode pitch and code gain */
                if (evenSubfr != 0)
                {
                    /* index of gain(s) */
                    index_mr475 = parm[parmOffset++];
                }

                if (bfi == 0)
                {
                    Dec_gain(st.pred_state, mode, index_mr475, code, 0, evenSubfr, gain_pit, 0, gain_code, 0);
                }
                else
                {
                    ec_gain_pitch(st.ec_gain_p_st, st.state, gain_pit, 0);
                    ec_gain_code(st.ec_gain_c_st, st.pred_state, st.state, gain_code, 0);
                }
                ec_gain_pitch_update(st.ec_gain_p_st, bfi, st.prev_bf, gain_pit, 0);
                ec_gain_code_update(st.ec_gain_c_st, bfi, st.prev_bf, gain_code, 0);
                pit_sharp = gain_pit[0];

                if (pit_sharp > SHARPMAX)
                {
                    pit_sharp = SHARPMAX;
                }
            }
            else if ((mode <= MR74) || (mode == MR102))
            {
                /* read and decode pitch and code gain */
                /* index of gain(s) */
                index = parm[parmOffset++];

                if (bfi == 0)
                {
                    Dec_gain(st.pred_state, mode, index, code, 0, evenSubfr, gain_pit, 0, gain_code, 0);
                }
                else
                {
                    ec_gain_pitch(st.ec_gain_p_st, st.state, gain_pit, 0);
                    ec_gain_code(st.ec_gain_c_st, st.pred_state, st.state, gain_code, 0);
                }
                ec_gain_pitch_update(st.ec_gain_p_st, bfi, st.prev_bf, gain_pit, 0);
                ec_gain_code_update(st.ec_gain_c_st, bfi, st.prev_bf, gain_code, 0);
                pit_sharp = gain_pit[0];

                if (pit_sharp > SHARPMAX)
                {
                    pit_sharp = SHARPMAX;
                }

                if (mode == MR102)
                {
                    if (st.old_T0 > (L_SUBFR + 5))
                    {
                        pit_sharp = pit_sharp >> 2;
                    }
                }
            }
            else
            {
                /* read and decode pitch gain */
                /* index of gain(s) */
                index = parm[parmOffset++];

                if (mode == MR795)
                {
                    /* decode pitch gain */
                    if (bfi != 0)
                    {
                        ec_gain_pitch(st.ec_gain_p_st, st.state, gain_pit, 0);
                    }
                    else
                    {
                        gain_pit[0] = d_gain_pitch(mode, index);
                    }
                    ec_gain_pitch_update(st.ec_gain_p_st, bfi, st.prev_bf, gain_pit, 0);

                    /* read and decode code gain */
                    index = parm[parmOffset++];

                    if (bfi == 0)
                    {
                        d_gain_code(st.pred_state, mode, index, code, 0, gain_code, 0);
                    }
                    else
                    {
                        ec_gain_code(st.ec_gain_c_st, st.pred_state, st.state, gain_code, 0);
                    }
                    ec_gain_code_update(st.ec_gain_c_st, bfi, st.prev_bf, gain_code, 0);
                    pit_sharp = gain_pit[0];

                    if (pit_sharp > SHARPMAX)
                    {
                        pit_sharp = SHARPMAX;
                    }
                }
                else
                { /* MR122 */

                    if (bfi == 0)
                    {
                        d_gain_code(st.pred_state, mode, index, code, 0, gain_code, 0);
                    }
                    else
                    {
                        ec_gain_code(st.ec_gain_c_st, st.pred_state, st.state, gain_code, 0);
                    }
                    ec_gain_code_update(st.ec_gain_c_st, bfi, st.prev_bf, gain_code, 0);
                    pit_sharp = gain_pit[0];
                }
            }

            /*
             * store pitch sharpening for next subframe (for modes which use the previous pitch gain for pitch
             * sharpening in the search phase) do not update sharpening in even subframes for MR475
             */
            if ((mode != MR475) || evenSubfr == 0)
            {
                st.sharp = gain_pit[0];

                if (st.sharp > SHARPMAX)
                {
                    st.sharp = SHARPMAX;
                }
            }

            if (pit_sharp > 16383)
            {
                pit_sharp = 32767;
            }
            else
            {
                pit_sharp *= 2;
            }

            if (pit_sharp > 16384)
            {
                for (i = 0; i < L_SUBFR; i++)
                {
                    temp[0] = (st.exc[i + st.excIndex] * pit_sharp) >> 15;
                    temp2 = (temp[0] * gain_pit[0]) << 1;

                    if (mode == MR122)
                    {
                        temp2 = (temp2 >> 1);
                    }
                    excp[i] = (temp2 + 0x00008000) >> 16;
                }
            }

            /*
             * Store list of LTP gains needed in the source characteristic detector (SCD)
             */
            if (bfi == 0)
            {
                for (i = 0; i < 8; i++)
                {
                    st.ltpGainHistory[i] = st.ltpGainHistory[i + 1];
                }
                st.ltpGainHistory[8] = gain_pit[0];
            }

            /*
             * Limit gain_pit if in background noise and BFI for MR475, MR515, MR59
             */
            if ((st.prev_bf != 0 || bfi != 0) & (st.inBackgroundNoise != 0) & ((mode == MR475) || (mode == MR515) || (mode == MR59)))
            {
                /* if (gain_pit > 0.75) in Q14 */
                if (gain_pit[0] > 12288) /* gain_pit = (gain_pit-0.75)/2.0 + 0.75; */
                {
                    gain_pit[0] = ((gain_pit[0] - 12288) >> 1) + 12288;
                }

                /* if (gain_pit > 0.90) in Q14 */
                if (gain_pit[0] > 14745)
                {
                    gain_pit[0] = 14745;
                }
            }

            /*
             * Calculate CB mixed gain
             */
            Int_lsf(prev_lsf, 0, st.lsfState.past_lsf_q, 0, i_subfr, lsf_i, 0);
            gain_code_mix = Cb_gain_average(st.Cb_gain_averState, mode, gain_code[0], lsf_i, 0, st.lsp_avg_st.lsp_meanSave, 0, bfi, st.prev_bf, pdfi,
                st.prev_pdf, st.inBackgroundNoise, st.voicedHangover);

            /* make sure that MR74, MR795, MR122 have original codeGain */
            /* MR74, MR795, MR122 */
            if ((mode > MR67) & (mode != MR102))
            {
                gain_code_mix = gain_code[0];
            }

            /*
             * Find the total excitation. Find synthesis speech corresponding to st.exc[].
             */
            /* MR475, MR515, MR59, MR67, MR74, MR795, MR102 */
            if (mode <= MR102)
            {
                pitch_fac = gain_pit[0];
                tmp_shift = 1;
            } /* MR122 */
            else
            {
                pitch_fac = gain_pit[0] >> 1;
                tmp_shift = 2;
            }

            /*
             * copy unscaled LTP excitation to exc_enhanced (used in phase dispersion below) and compute total
             * excitation for LTP feedback
             */
            memcpy(exc_enhanced, 0, st.exc, st.excIndex, L_SUBFR);

            for (i = 0; i < L_SUBFR; i++)
            {
                /* st.exc[i] = gain_pit*st.exc[i] + gain_code*code[i]; */
                temp[0] = (st.exc[i + st.excIndex] * pitch_fac) + (code[i] * gain_code[0]);
                temp2 = (temp[0] << tmp_shift);
                if ((((temp2 >> 1) ^ temp2) & 0x40000000) != 0)
                {
                    if (((temp[0] ^ temp2) & 0x80000000) != 0)
                    {
                        temp2 = ((temp[0] & 0x80000000) != 0) ? (-1073741824) : 1073725439;
                    }
                    else
                    {
                        temp2 = ((temp2 & 0x80000000) != 0) ? (-1073741824) : 1073725439;
                    }
                }
                st.exc[i + st.excIndex] = (temp2 + 0x00004000) >> 15;
            }
            /*
             * Adaptive phase dispersion
             */

            /* free phase dispersion adaption */
            st.ph_disp_st.lockFull = 0;

            if (((mode == MR475) || (mode == MR515) || (mode == MR59)) & (st.voicedHangover > 3) & (st.inBackgroundNoise != 0) & (bfi != 0))
            {
                /*
                 * Always Use full Phase Disp. if error in bg noise
                 */
                st.ph_disp_st.lockFull = 1;
            }

            /*
             * apply phase dispersion to innovation (if enabled) and compute total excitation for synthesis part
             */
            ph_disp(st.ph_disp_st, mode, exc_enhanced, 0, gain_code_mix, gain_pit[0], code, 0, pitch_fac, tmp_shift);

            // printCode(code);
            /*
             * The Excitation control module are active during BFI. Conceal drops in signal energy if in bg noise.
             */
            temp2 = 0;

            for (i = 0; i < L_SUBFR; i++)
            {
                temp2 += (exc_enhanced[i] * exc_enhanced[i]);
            }

            if (temp2 > 0x3FFFFFFF)
            {
                excEnergy = 11584;
            }
            else
            {
                temp2 = sqrt_l_exp(temp2, temp, 0);
                temp2 = (temp2 >> ((temp[0] >> 1) + 15));
                excEnergy = temp2 >> 2;
            }

            if (((mode == MR475) || (mode == MR515) || (mode == MR59)) & (st.voicedHangover > 5) & (st.inBackgroundNoise != 0) & (st.state < 4)
                    & (((pdfi != 0) & (st.prev_pdf != 0)) || bfi != 0 || st.prev_bf != 0))
            {
                carefulFlag = 0;

                if ((pdfi != 0) & (bfi == 0))
                {
                    carefulFlag = 1;
                }
                Ex_ctrl(exc_enhanced, 0, excEnergy, st.excEnergyHist, 0, st.voicedHangover, st.prev_bf, carefulFlag);
                // printCode(exc_enhanced);
            }

            if ((st.inBackgroundNoise != 0) & (bfi != 0 || st.prev_bf != 0) & (st.state < 4))
            {
                ; /* do nothing! */
            }
            else
            {
                /* Update energy history for all modes */
                for (i = 0; i < 8; i++)
                {
                    st.excEnergyHist[i] = st.excEnergyHist[i + 1];
                }
                st.excEnergyHist[8] = excEnergy;
            }

            /*
             * Excitation control module end.
             */
            if (pit_sharp > 16384)
            {
                for (i = 0; i < L_SUBFR; i++)
                {
                    excp[i] = excp[i] + exc_enhanced[i];
                    if (Math.abs(excp[i]) > 32767)
                    {
                        excp[i] = ((excp[i] & 0x80000000) != 0) ? -32768 : 32767;
                    }
                }
                agc2(exc_enhanced, 0, excp, 0);
                overflow = Syn_filt(Az, AzIndex, excp, 0, synth, i_subfr + synthOffset, L_SUBFR, st.mem_syn, 0, 0);
            }
            else
            {
                overflow = Syn_filt(Az, AzIndex, exc_enhanced, 0, synth, i_subfr + synthOffset, L_SUBFR, st.mem_syn, 0, 0);
            }

            if (overflow != 0)
            {
                for (i = 0; i < PIT_MAX + L_INTERPOL + L_SUBFR; i++)
                {
                    st.old_exc[i] = st.old_exc[i] >> 2;
                }

                for (i = 0; i < L_SUBFR; i++)
                {
                    exc_enhanced[i] = exc_enhanced[i] >> 2;
                }
                Syn_filt_overflow(Az, AzIndex, exc_enhanced, 0, synth, i_subfr + synthOffset, L_SUBFR, st.mem_syn, 0, 1);
            }
            else
            {
                memcpy(st.mem_syn, 0, synth, i_subfr + 30 + synthOffset, 10);
            }

            /*
             * Update signal for next frame. . shift to the left by L_SUBFR st.exc[]
             */
            memcpy(st.old_exc, 0, st.old_exc, L_SUBFR, (PIT_MAX + L_INTERPOL));

            /* interpolated LPC parameters for next subframe */
            AzIndex += MP1;

            /* store T0 for next subframe */
            st.old_T0 = T0[0];
        }

        /*
         * Call the Source Characteristic Detector which updates st.inBackgroundNoise and st.voicedHangover.
         */

        int[] tempVoicedHangover = new int[]
        { st.voicedHangover };
        st.inBackgroundNoise = Bgn_scd(st.background_state, st.ltpGainHistory, 0, synth, synthOffset, tempVoicedHangover, 0);
        st.voicedHangover = tempVoicedHangover[0];
        dtx_dec_activity_update(st.dtxDecoderState, st.lsfState.past_lsf_q, 0, synth, synthOffset);

        /* store bfi for next subframe */
        st.prev_bf = bfi;
        st.prev_pdf = pdfi;

        /*
         * Calculate the LSF averages on the eight previous frames
         */
        // System.out.println("Calculate lsp_avg");
        lsp_avg(st.lsp_avg_st, st.lsfState.past_lsf_q, 0);
        // System.out.println("Return from Decoder_amr");
        st.dtxDecoderState.dtxGlobalState = newDTXState;
        return;
    }

    /*
     * static void printSynth(int[] synth){ int sumSynth = 0; for(int i = 0;i < 160;i ++){ sumSynth += synth[i]; }
     * System.out.println("sumSynth is "+sumSynth); }
     */

    /*
     * Residu40
     * 
     * 
     * Parameters: a I: prediction coefficients x I: speech signal y O: residual signal
     * 
     * Function: The LP residual is computed by filtering the input speech through the LP inverse filter a(z)
     * 
     * Returns: void
     */
    static void Residu40(int a[], int aOffset, int x[], int xOffset, int y[], int yOffset)
    {
        int s, i, j;

        for (i = 0; i < 40; i++)
        {
            s = a[0 + aOffset] * x[i + xOffset] + a[1 + aOffset] * x[i - 1 + xOffset] + a[2 + aOffset] * x[i - 2 + xOffset] + a[3 + aOffset]
                    * x[i - 3 + xOffset];
            s += a[4 + aOffset] * x[i - 4 + xOffset] + a[5 + aOffset] * x[i - 5 + xOffset] + a[6 + aOffset] * x[i - 6 + xOffset] + a[7 + aOffset]
                    * x[i - 7 + xOffset];
            s += a[8 + aOffset] * x[i - 8 + xOffset] + a[9 + aOffset] * x[i - 9 + xOffset] + a[10 + aOffset] * x[i - 10 + xOffset];
            y[i + yOffset] = (s + 0x800) >> 12;
            if (Math.abs(y[i + yOffset]) > 32767)
            {
                /* go to safe mode */
                for (i = 0; i < 40; i++)
                {
                    s = a[0 + aOffset] * x[i + xOffset];
                    for (j = 1; j <= 10; j++)
                    {
                        s += a[j + aOffset] * x[i - j + xOffset];
                        if (s > 1073741823)
                        {
                            s = 1073741823;
                        }
                        else if (s < -1073741824)
                        {
                            s = -1073741824;
                        }
                    }
                    y[i + yOffset] = (s + 0x800) >> 12;
                    if (Math.abs(y[i + yOffset]) > 32767)
                    {
                        y[i + yOffset] = ((y[i + yOffset] & 0x80000000) != 0) ? -32768 : 32767;
                    }
                }
                return;
            }

        }
        return;
    }

    /*
     * agc
     * 
     * 
     * Parameters: st.past_gain B: gain memory sig_in I: Post_Filter input signal sig_out B: Post_Filter output signal
     * agc_fac I: AGC factor
     * 
     * Function: Scales the Post_Filter output on a subframe basis
     * 
     * Returns: void
     */
    static void agc(AgcState st, int[] sig_in, int sig_inOffset, int[] sig_out, int sig_outOffset, short agc_fac)
    {
        int s, gain_in, gain_out, g0, gain;
        int exp, i;

        /* calculate gain_out with exponent */
        s = energy_new(sig_out, sig_outOffset);

        if (s == 0)
        {
            st.past_gain = 0;
            return;
        }
        exp = 0;
        i = s;
        while ((i & 0x40000000) == 0)
        {
            exp++;
            i = i << 1;
        }
        exp -= 1;
        if ((exp & 0x80000000) != 0)
        {
            s >>= 1;
        }
        else
        {
            s <<= exp;
        }
        gain_out = (s + 0x00008000) >> 16;

        /* calculate gain_in with exponent */
        s = energy_new(sig_in, sig_inOffset);

        if (s == 0)
        {
            g0 = 0;
        }
        else
        {
            i = 0;
            while ((s & 0x40000000) == 0)
            {
                i++;
                s = s << 1;
            }
            s = s + 0x00008000;

            if (s >= 0)
            {
                gain_in = s >> 16;
            }
            else
            {
                gain_in = 32767;
            }
            exp = (exp - i);

            /*
             * g0 = (1-agc_fac) * sqrt(gain_in/gain_out);
             */
            /* s = gain_out / gain_in */
            s = (gain_out << 15) / gain_in;
            exp = 7 - exp;

            if (exp > 0)
            {
                if (exp > 31)
                {
                    if (s != 0)
                    {
                        s = 2147483647;
                    }
                }
                else
                {
                    s = s << exp;
                }
            }
            else
            {
                s = (s >> (-exp));
            }
            if (s < 0)
            {
                s = 2147483647;
            }
            s = Inv_sqrt(s);
            i = ((s << 9) + 0x00008000) >> 16;
            if ((i & 0xFFFF8000) != 0)
            {
                i = 32767;
            }

            /* g0 = i * (1-agc_fac) */
            g0 = (i * (32767 - agc_fac)) >> 15;
        }

        /*
         * compute gain[n] = agc_fac * gain[n-1] + (1-agc_fac) * sqrt(gain_in/gain_out) sig_out[n] = gain[n] *
         * sig_out[n]
         */
        gain = st.past_gain;

        for (i = 0; i < L_SUBFR; i++)
        {
            gain = (gain * agc_fac) >> 15;
            gain = gain + g0;
            sig_out[i + sig_outOffset] = (sig_out[i + sig_outOffset] * gain) >> 12;
            if (Math.abs(sig_out[i + sig_outOffset]) > 32767)
            {
                sig_out[i + sig_outOffset] = ((sig_out[i + sig_outOffset] & 0x8000000) != 0) ? -32768 : 32767;
            }
        }
        st.past_gain = gain;
        return;
    }

    /*
     * Post_Filter
     * 
     * 
     * Parameters: st B: post filter states mode I: AMR mode syn B: synthesis speech Az_4 I: interpolated LPC parameters
     * in all subfr.
     * 
     * Function: Post_Filtering of synthesis speech.
     * 
     * inverse filtering of syn[] through A(z/0.7) to get res2[] tilt compensation filtering; 1 - MU*k*z^-1 synthesis
     * filtering through 1/A(z/0.75) adaptive gain control
     * 
     * Returns: void
     */
    static void Post_Filter(Post_FilterState st, int mode, int[] syn, int synOffset, int[] Az_4, int Az_4Offset)
    {
        int[] h = new int[22], Ap3 = new int[MP1], Ap4 = new int[MP1]; /* bandwidth expanded LP parameters */
        int tmp, i_subfr, i, temp1, temp2, overflow = 0;
        int[] Az;
        int AzIndex;
        int[] p1;
        int p1Index;
        int[] p2;
        int p2Index;
        int[] syn_work;
        syn_work = st.synth_buf;
        int syn_workIndex = M;
        int[] pgamma3 = gamma3;
        int pgamma3Index = 0;
        int[] pgamma4 = gamma4_gamma3_MR122;
        int pgamma4Index = 0;

        /*
         * Post filtering
         */
        memcpy(syn_work, syn_workIndex, syn, synOffset, L_FRAME);
        Az = Az_4;
        AzIndex = Az_4Offset;

        if ((mode == MR122) || (mode == MR102))
        {
            pgamma3 = gamma4_gamma3_MR122;
            pgamma3Index = 0;
            pgamma4 = gamma4_MR122;
            pgamma4Index = 0;
        }

        for (i_subfr = 0; i_subfr < L_FRAME; i_subfr += L_SUBFR)
        {
            /* Find weighted filter coefficients Ap3[] and Ap[4] */
            Ap3[0] = Az[0];
            Ap4[0] = Az[0];

            for (i = 1; i <= 10; i++)
            {
                Ap3[i] = (Az[i] * pgamma3[i - 1 + pgamma3Index] + 0x4000) >> 15;
                Ap4[i] = (Az[i] * pgamma4[i - 1 + pgamma4Index] + 0x4000) >> 15;
            }

            /* filtering of synthesis speech by A(z/0.7) to find res2[] */
            Residu40(Ap3, 0, syn_work, syn_workIndex + i_subfr, st.res2, 0);

            /* tilt compensation filter */
            /* impulse response of A(z/0.7)/A(z/0.75) */
            memcpy(h, 0, Ap3, 0, MP1);
            memset(h, M + 1, 0, 22 - M - 1);
            Syn_filt(Ap4, 0, h, 0, h, 0, 22, h, M + 1, 0);

            /* 1st correlation of h[] */
            tmp = 16777216 + h[1] * h[1];

            for (i = 2; i < 22; i++)
            {
                tmp += h[i] * h[i];
                if (tmp > 0x3FFF8000)
                {
                    break;
                }
            }
            temp1 = tmp >> 15;
            if ((temp1 & 0xFFFF8000) != 0)
            {
                temp1 = 32767;
            }

            tmp = h[0] * h[1];

            for (i = 1; i < 21; i++)
            {
                tmp += h[i] * h[i + 1];
                if (Math.abs(tmp) > 1073741823)
                {
                    tmp = 1073741823;
                }
            }
            temp2 = tmp >> 15;

            if (temp2 <= 0)
            {
                temp2 = 0;
            }
            else
            {
                tmp = temp2 * 26214;
                temp2 = (tmp & 0xffff8000) / temp1;
            }

            /* preemphasis */
            p1 = st.res2;
            p1Index = 39;
            p2 = p1;
            p2Index = p1Index - 1;
            tmp = p1[p1Index];

            do
            {
                p1[p1Index] = p1[p1Index] - ((temp2 * p2[p2Index--]) >> 15);
                if (Math.abs(p1[p1Index]) > 32767)
                {
                    p1[p1Index] = ((p1[p1Index] & 0x80000000) != 0) ? -32768 : 32767;
                }
                p1Index--;
                p1[p1Index] = p1[p1Index] - ((temp2 * p2[p2Index--]) >> 15);
                if (Math.abs(p1[p1Index]) > 32767)
                {
                    p1[p1Index] = ((p1[p1Index] & 0x80000000) != 0) ? -32768 : 32767;
                }
                p1Index--;
                p1[p1Index] = p1[p1Index] - ((temp2 * p2[p2Index--]) >> 15);
                if (Math.abs(p1[p1Index]) > 32767)
                {
                    p1[p1Index] = ((p1[p1Index] & 0x80000000) != 0) ? -32768 : 32767;
                }
                p1Index--;
            } while (p1Index > 0);
            p1[p1Index] = p1[p1Index] - ((temp2 * st.preemph_state_mem_pre) >> 15);
            if (Math.abs(p1[p1Index]) > 32767)
            {
                p1[p1Index] = ((p1[p1Index] & 0x80000000) != 0) ? -32768 : 32767;
            }
            st.preemph_state_mem_pre = tmp;

            /* filtering through 1/A(z/0.75) */
            overflow = Syn_filt(Ap4, 0, st.res2, 0, syn, synOffset + i_subfr, L_SUBFR, st.mem_syn_pst, 0, 0);
            if (overflow != 0)
            {
                Syn_filt_overflow(Ap4, 0, st.res2, 0, syn, synOffset + i_subfr, L_SUBFR, st.mem_syn_pst, 0, 1);
                overflow = 0;
            }
            else
            {
                memcpy(st.mem_syn_pst, 0, syn, synOffset + i_subfr + 30, 10);
            }

            /* scale output to input */
            agc(st.agc_state, syn_work, syn_workIndex + i_subfr, syn, synOffset + i_subfr, (short) AGC_FAC);
            AzIndex += MP1;
        }

        /* update syn_work[] buffer */
        memcpy(syn_work, syn_workIndex - M, syn_work, syn_workIndex + L_FRAME - M, M);
        return;
    }

    /*
     * c Post_Process
     * 
     * 
     * Parameters: st B: post filter states signal B: signal
     * 
     * Function: Postprocessing of input speech.
     * 
     * 2nd order high pass filtering with cut off frequency at 60 Hz. Multiplication of output by two.
     * 
     * 
     * Returns: void
     */
    static void Post_Process(Post_ProcessState st, int signal[], int signalOffset)
    {
        int x2, tmp, i = 0;
        int mask = 0x40000000;

        do
        {
            x2 = st.x1;
            st.x1 = st.x0;
            st.x0 = signal[i + signalOffset];

            /*
             * y[i] = b[0]*x[i]*2 + b[1]*x[i-1]*2 + b140[2]*x[i-2]/2 + a[1]*y[i-1] + a[2] * y[i-2];
             */
            tmp = (st.y1_hi * 15836) + (((st.y1_lo * 15836) & (int) 0xffff8000) >> 15);
            tmp += (st.y2_hi * -7667) + (((st.y2_lo * (-7667)) & (int) 0xffff8000) >> 15);
            tmp += st.x0 * 7699;
            tmp += st.x1 * -15398;
            if ((((tmp >> 1) ^ tmp) & mask) != 0)
            {
                tmp = ((tmp & 0x80000000) != 0) ? -1073741824 : 1073741823;
            }

            tmp += x2 * 7699;
            if ((((tmp >> 1) ^ tmp) & mask) != 0)
            {
                tmp = ((tmp & 0x80000000) != 0) ? -1073741824 : 1073741823;
            }

            tmp = tmp << 1;
            if ((((tmp >> 1) ^ tmp) & mask) != 0)
            {
                tmp = ((tmp & 0x80000000) != 0) ? -1073741824 : 1073741823;
            }

            tmp = tmp << 1;
            if ((((tmp >> 1) ^ tmp) & mask) != 0)
            {
                tmp = ((tmp & 0x80000000) != 0) ? -1073741824 : 1073741823;
            }

            if (Math.abs(tmp) < 536862720)
            {
                signal[signalOffset + i++] = (tmp + 0x00002000) >> 14;
            }
            else if (tmp > 0)
            {
                signal[signalOffset + i++] = 32767;
            }
            else
            {
                signal[signalOffset + i++] = -32768;
            }
            st.y2_hi = st.y1_hi;
            st.y2_lo = st.y1_lo;
            st.y1_hi = tmp >> 15;
            st.y1_lo = ((tmp << 1) - (st.y1_hi << 16)) >> 1;
        } while (i < 160);
        return;
    }

    /*
     * Speech_Decode_Frame
     * 
     * 
     * Parameters: st B: decoder memory mode I: AMR mode parm I: speech parameters frame_type I: Frame type synth O:
     * synthesis speech
     * 
     * Function: Decode one frame
     * 
     * Returns: void
     */
    static void Speech_Decode_Frame(Speech_Decode_FrameState st, int mode, short[] parm, int parmOffset, int frame_type, short[] synth,
            int synthOffset)
    {
        int[] Az_dec = new int[AZ_SIZE]; /* Decoded Az for post-filter in 4 subframes */
        int[] synth_speech = new int[L_FRAME];
        int i;

        /* Synthesis */
        Decoder_amr(st.decoder_amrState, mode, parm, parmOffset, frame_type, synth_speech, 0, Az_dec, 0);
        /*
         * for (i = 0; i < L_FRAME; i++) { System.out.printf("%d,", synth_speech[i]); }
         */
        Post_Filter(st.post_state, mode, synth_speech, 0, Az_dec, 0);

        /* post HP filter, and 15.16 bits */
        Post_Process(st.postHP_state, synth_speech, 0);

        for (i = 0; i < L_FRAME; i++)
        {
            synth[i + synthOffset] = (short) (synth_speech[i] & 0xfff8);
        }
        return;
    }

    /*
     * Decoder_amr_exit
     * 
     * 
     * Parameters: state I: state structure
     * 
     * Function: The memory used for state memory is freed
     * 
     * Returns: Void
     */
    static void Decoder_amr_exit(Decoder_amrState state)
    {
        return;
    }

    /*
     * Post_Filter_exit
     * 
     * 
     * Parameters: state I: state structure
     * 
     * Function: The memory used for state memory is freed
     * 
     * Returns: Void
     */
    static void Post_Filter_exit(Post_FilterState state)
    {
        return;
    }

    /*
     * Post_Process_reset
     * 
     * 
     * Parameters: state B: state structure
     * 
     * Function: Resets state memory
     * 
     * Returns: -1 failure
     */
    static int Post_Process_reset(Post_ProcessState state)
    {
        if (state == null)
        {
            return -1;
        }
        state.y2_hi = 0;
        state.y2_lo = 0;
        state.y1_hi = 0;
        state.y1_lo = 0;
        state.x0 = 0;
        state.x1 = 0;
        return 0;
    }

    /*
     * Post_Process_exit
     * 
     * 
     * Parameters: state I: state structure
     * 
     * Function: The memory used for state memory is freed
     * 
     * Returns: Void
     */
    static void Post_Process_exit(Post_ProcessState state)
    {
        return;
    }

    /*
     * Decoder_amr_init
     * 
     * 
     * Parameters: state O: state structure
     * 
     * Function: Allocates state memory and initializes state memory
     * 
     * Returns: success = 0
     */
    static int Decoder_amr_init(Decoder_amrState state)
    {
        memset(state.Cb_gain_averState.cbGainHistory, 0, 0, L_CBGAINHIST);

        /* Initialize hangover handling */
        state.Cb_gain_averState.hangVar = 0;
        state.Cb_gain_averState.hangCount = 0;

        Decoder_amr_reset(state, 0);
        return 0;
    }

    /*
     * Post_Filter_reset
     * 
     * 
     * Parameters: state B: state structure
     * 
     * Function: Resets state memory
     * 
     * Returns: -1 failure
     */
    static int Post_Filter_reset(Post_FilterState state)
    {
        if (state == null)
        {
            return -1;
        }
        state.preemph_state_mem_pre = 0;
        state.agc_state.past_gain = 4096;
        memset(state.mem_syn_pst, 0, 0, M);
        memset(state.res2, 0, 0, L_SUBFR);
        memset(state.synth_buf, 0, 0, L_FRAME + M);
        return 0;
    }

    /*
     * Post_Filter_init
     * 
     * 
     * Parameters: state O: state structure
     * 
     * Function: Allocates state memory and initializes state memory
     * 
     * Returns: success = 0
     */
    static int Post_Filter_init(Post_FilterState state)
    {
        state.agc_state = new AgcState();
        Post_Filter_reset(state);
        return 0;
    }

    /*
     * Post_Process_init
     * 
     * 
     * Parameters: state O: state structure
     * 
     * Function: Allocates state memory and initializes state memory
     * 
     * Returns: success = 0
     */
    static int Post_Process_init(Post_ProcessState state)
    {
        Post_Process_reset(state);
        return 0;
    }

    /*
     * Speech_Decode_Frame_exit
     * 
     * 
     * Parameters: state I: state structure
     * 
     * Function: The memory used for state memory is freed
     * 
     * Returns: Void
     */
    static void Speech_Decode_Frame_exit(Speech_Decode_FrameState st)
    {
        Decoder_amr_exit(st.decoder_amrState);
        Post_Filter_exit(st.post_state);
        Post_Process_exit(st.postHP_state);

        return;
    }

    /*
     * Speech_Decode_Frame_reset
     * 
     * 
     * Parameters: state B: state structure
     * 
     * Function: Resets state memory
     * 
     * Returns: -1 = failure
     */
    static int Speech_Decode_Frame_reset(Speech_Decode_FrameState st)
    {
        Decoder_amr_reset(st.decoder_amrState, 0);
        Post_Filter_reset(st.post_state);
        Post_Process_reset(st.postHP_state);
        return 0;
    }

    /*
     * Speech_Decode_Frame_init
     * 
     * 
     * Parameters: state O: state structure
     * 
     * Function: Allocates state memory and initializes state memory
     * 
     * Returns: success = 0
     */
    static Speech_Decode_FrameState Speech_Decode_Frame_init()
    {
        Speech_Decode_FrameState s = new Speech_Decode_FrameState();

        Decoder_amr_init(s.decoder_amrState);
        Post_Filter_init(s.post_state);
        Post_Process_init(s.postHP_state);
        return s;
    }
}
