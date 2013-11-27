/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * INavRuleConstants.java
 *
 */
package com.telenav.datatypes.nav;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
public interface INavEngineConstants
{
    // index of deviation counter
    public static final byte DEV_HEAD_COUNT     = 0;
    public static final byte DEV_POS_COUNT      = 1;
    public static final byte DEV_VEL_COUNT      = 2;
    
    //regainer rules
    public static final byte RULE_MIN_SPEED                             = 0;
    public static final byte RULE_HEADING_BITS                          = 1;
    public static final byte RULE_DISTANCE_BITS                         = 2;
    public static final byte RULE_REGAIN_POSITION                       = 3;
    public static final byte RULE_REGAIN_HEADING                        = 4;
    public static final byte RULE_ADI_POSITION                          = 5;
    public static final byte RULE_ADI_HEADING                           = 6;
    public static final byte RULE_ADI_LAST_POSITION                     = 7;
    public static final byte RULE_REGAIN_LAST_ROUTE                     = 8;
    public static final byte RULE_PSEUDO_NAV_ENTRY_THRESHOLD            = 9;
    public static final byte RULE_PSEUDO_NAV_EXIT_THRESHOLD             = 10;
    public static final byte RULE_PSEUDO_NAV_ENTRY_HEADING_THRESHOLD    = 11;
    public static final byte RULE_PSEUDO_NAV_EXIT_HEADING_THRESHOLD     = 12;
    public static final byte RULE_FORCED_ADI_POINT_THRESHOLD            = 13;
    public static final byte RULE_FORCED_ADI_POINT_HEADING_THRESHOLD    = 14;
    public static final byte RULE_LAST_ADI_POINT_WEIGHT                 = 15;
    
    //DEV tuning rules
    public static final byte RULE_USE_HEADING_FOR_RAMPS = 0;
    public static final byte RULE_HEADING_STRICT        = 1;
    public static final byte RULE_HEADING_MEDIUM        = 2;
    public static final byte RULE_HEADING_LOOSE         = 3;
    public static final byte RULE_DISTANCE_STRICT       = 4;
    public static final byte RULE_DISTANCE_MEDIUM       = 5;
    public static final byte RULE_DISTANCE_LOOSE        = 6;
    public static final byte RULE_RANGE_RATIO_NUMERATOR = 7;
    public static final byte RULE_RANGE_RATIO_SUBSTRACT = 8;
    public static final byte RULE_REGAINER_OFF_EDGE     = 9;
    public static final byte RULE_NUMBER_NAV_CYCLES     = 10;
    public static final byte RULE_NUMBER_REGAIN_CYCLES  = 11;
    
    //K-table indices
    public static final byte K11 = 0;
    public static final byte K22 = 1;
    public static final byte K33 = 2;
    public static final byte K44 = 3;
    public static final byte K13 = 4;
    public static final byte K24 = 5;
    public static final byte K31 = 6;
    public static final byte K42 = 7;
    
    //node type
    public static final int TYPE_ZOOM_RULES         = 16;
    public static final int TYPE_K_VALUES           = 20;
    public static final int TYPE_REGAINER_RULES     = 26;
    
    // ----- segment types:
    public static final byte SEGMENT_NORMAL = 0;
    public static final byte SEGMENT_TIGHT_TURN = 1;

}
