package com.telenav.datatypes.audio;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class RuleNode
{
    private static final int INT_NULL = Integer.MIN_VALUE ;
    private static final int TYPE_MSG_AUDIO = 33 ;
    
    // this is the reference to the root node.  This is 1 because
    // the first byte in the array is the number of variables in the rule
    private static final int ROOT_NODE =0 ;
    
    // Shift values to retrive specific fields from a node. These values
    // can be used to derive the index of sepcific fields given the 
    // reference of the current node
    private static final int ID_FIELD = 0 ;
    private static final int VALUE_FIELD = 1 ;
    private static final int NUM_CHILDREN_FIELD = 2 ;
    private static final int CHILDREN_FIELD = 3 ;
    
    // Note make sure to update these constants with the values in
    // RuleTreeConstants if anything changes in the parser.
    private static final int JJTROOT = 0;
    private static final int JJTIFBLOCK = 1;
    private static final int JJTVOID = 2;
    private static final int JJTSTATEMENTS = 3;
    private static final int JJTAND = 4;
    private static final int JJTOR = 5;
    private static final int JJTNOT = 6;
    private static final int JJTGT = 7;
    private static final int JJTLT = 8;
    private static final int JJTEQ = 9;
    private static final int JJTNE = 10;
    private static final int JJTGTEQ = 11;
    private static final int JJTLTEQ = 12;
    private static final int JJTDEFINED = 13;
    private static final int JJTRETURNTERMS = 14;
    private static final int JJTADDTERMS = 15;
    private static final int JJTADD = 16;
    private static final int JJTSUBTRACT = 17;
    private static final int JJTMULT = 18;
    private static final int JJTDIV = 19;
    private static final int JJTMOD = 20;
    private static final int JJTASSIGNMENT = 21;
    private static final int JJTNODEARGINDEX = 22;
    private static final int JJTINTARGINDEX = 23;
    private static final int JJTINTLITERAL = 24;
    private static final int JJTFINALIDENTIFIER = 25;
    private static final int JJTIDENTIFIER = 26;

    protected int varCount ;
    protected int[] variables ;
    protected int[] rule ;
    
    protected RuleNode(InputStream is)
                                        throws Exception
    {
        if (!initRule(is))
            throw new Exception("Rule header is corrupted, unable to initialize.") ;
        
        // first convert the ruleBinary to integer byte codes
        populateRuleArray(is) ;
    }
    
    int MAX_REASONABLE_RULE_LENGTH = 100000;
    
    private boolean initRule(InputStream is)
                                throws IOException
    {
        // Read header info
        byte[] intBuf = new byte[4] ;
        
        // make sure that the head is intact
        // header if the first 8 bytes
        if (is.available() >= 8)
        {
            // ok, read header
            // first the rule length
            is.read(intBuf) ;
            int ruleLength = bytes2int(intBuf) ;
            if (ruleLength > MAX_REASONABLE_RULE_LENGTH)
            {
                System.err.println("Wrong rule header.") ;
                return false;
            }
                
            // init rule array
            rule = new int[ruleLength];
            
            // next the number of variables
            is.read(intBuf);
            varCount = bytes2int(intBuf);
            
            return true ;
        }
        else
        {
            System.err.println("Couldn't read rule header.") ;
            return false ;
        }
    }
    
    private static int bytes2int(byte[] buffer)
    {
        int ch1 = buffer[0] & 0xff;
        int ch2 = buffer[1] & 0xff;
        int ch3 = buffer[2] & 0xff;
        int ch4 = buffer[3] & 0xff;
        return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0);
    }

    private void populateRuleArray(InputStream is)
                                throws Exception
    {
        byte[] intBuf = new byte[4] ;
        
        for (int i=0; i<rule.length; i++)
        {
            // There should be at exactly this number of
            // int elements in the input stream otherwise,
            // the rule has been corrupted
            if (is.available() >= intBuf.length)
            {
                is.read(intBuf) ;
                rule[i] = bytes2int(intBuf) ;
            }
            else
                throw new Exception(
                     "Unable to load rule.  Data appears to be corrupted.") ;
        }
    }

    public AudioDataNode eval(int[] intArgs,
            AudioDataNode[] nodeArgs)
    {
        
        AudioDataNode terms = AudioDataFactory.getInstance().createAudioDataNode(null);

        // initialize variable store
        variables = new int[varCount] ;
        for (int i=0; i<variables.length; i++)
            variables[i] =INT_NULL ;
        
        // start traversal with the root reference
        EvalResult r = traverse(ROOT_NODE,intArgs,nodeArgs);

        if (r != null && r.nodes != null)
            for (int j = 0; j < r.nodes.size(); j++)
                terms.addChild((AudioDataNode) r.nodes.elementAt(j));

        return terms ;

    }
    
    private int getChildNode(int nodeRef, int childPos)
    {
        return rule[nodeRef + CHILDREN_FIELD + childPos] ;
    }
    
    private int getId(int nodeRef)
    {
        return rule[nodeRef + ID_FIELD] ;
    }
    
    private int getValue(int nodeRef)
    {
        return rule[nodeRef + VALUE_FIELD] ;
    }
    
    private int getNumChildren(int nodeRef)
    {
        return rule[nodeRef + NUM_CHILDREN_FIELD] ;
    }

    
    private EvalResult traverse(int node,
                               int[] intArgs,
                               AudioDataNode[] nodeArgs)
                                            throws RuntimeException
    {
        EvalResult result = null ;
        
        int id = getId(node) ;
        int value = getValue(node) ;
        int numChildren = getNumChildren(node) ;
        
        switch (id)
        {
            case JJTROOT:
                // ROOT node will just have one child.  Just traverse
                // that child, if any, and return the result
                if (numChildren == 1)
                    return traverse(getChildNode(node, 0),
                                                intArgs, nodeArgs) ;
                else
                    return null ;
                
            case JJTIFBLOCK:
                // An ifblock has two mandatory nodes
                //      0. logical expression
                //      1. Statments (in the {} block)
                // It may also have a 3rd node, which would be another ifNode.  If the condition evaluates to true
                // skip the if node, otherwise execute it.
                int conditionNode = getChildNode(node, 0);
                EvalResult conditionResult = traverse(conditionNode, intArgs, nodeArgs);
                
                // if the condition is true, execute the statments ;
                if (conditionResult.booleanResult)
                {
                    int statementsNode = getChildNode(node, 1); ;
                    return traverse(statementsNode, intArgs, nodeArgs) ;
                }
                else if (numChildren == 3)
                {
                    int elseNode = getChildNode(node, 2) ;
                    return traverse(elseNode,intArgs,nodeArgs) ;
                }
                
                return null ;
                
            case JJTSTATEMENTS:
                if (numChildren > 0)
                {
                    result = new EvalResult() ;
                    result.nodes = new Vector() ;
                    
                    for (int i = 0; i < numChildren; ++i)
                    {
                        int statementNode = getChildNode(node, i) ;
                        EvalResult statementResult
                                    = traverse(statementNode, intArgs, nodeArgs);
                        
                        if (statementResult != null && statementResult.nodes != null)
                        {
                            for (int j=0; j<statementResult.nodes.size(); j++)
                                result.nodes.addElement(statementResult.nodes.elementAt(j)) ;
                    
                            // stop evaluating if the result says stop
                            if (statementResult.stop)
                            {
                                result.stop = true ;
                                break ;
                            }
                        }
                    }
                
                    return result ;
                }
                else
                    return null ;

            // HANDLE NOT EXPRESSION
            case JJTNOT:
                // evaluate child logical expression
                result = traverse(getChildNode(node, 0), intArgs, nodeArgs);
                
                // Reverse the boolean result
                result.booleanResult = ! result.booleanResult ;

                return result;
            
            case JJTADDTERMS:
                return addTerms(node, numChildren, intArgs, nodeArgs) ;
                
            case JJTRETURNTERMS:
                result = addTerms(node, numChildren, intArgs, nodeArgs) ;
                result.stop = true ;
                return result ;
                
            case JJTINTLITERAL:
                result = new EvalResult();
                result.intResult = value;
                return result;
                
            case JJTINTARGINDEX :
                // The value for the argument is the index in
                // in the arguments array.
                result = new EvalResult() ;
                if (value >= intArgs.length)
                    // no such argument exists
                    throw new RuntimeException(
                            "No argument specified for index: "+value) ;
                
                result.intResult = intArgs[value] ;
                return result ;
                
            case JJTNODEARGINDEX:
                // The value for the argument is the index in
                // in the arguments array.
                result = new EvalResult() ;
                if (value >= nodeArgs.length)
                    // no such argument exists
                    throw new RuntimeException(
                            "No argument specified for index: "+value) ;
                    
                result.nodeResult = nodeArgs[value] ;
                return result ;
                
            case JJTIDENTIFIER :
                // an identifier node should only be evaluated as
                // part of an expression.  The identifier node
                // that is part of an assignment expression
                // will be consumed by the assignment evaluation
                // code.
                result = new EvalResult() ;
                result.intResult = variables[value];
                
                // if we are unable to resolve the variable, then
                // throw a runtime exception
                if (result.intResult == INT_NULL)
                    throw new NullPointerException("Referenced variable '"+value+"' is null") ;
                
                return result;
                
            // LOGICAL OPERATORS
            case JJTAND :
                int lhs = getChildNode(node, 0);
                int rhs = getChildNode(node, 1);
                result = new EvalResult() ;
                result.booleanResult = traverse(lhs, intArgs, nodeArgs).booleanResult
                        && traverse(rhs, intArgs, nodeArgs).booleanResult;
                return result;
                
            case JJTOR :
                lhs = getChildNode(node, 0);
                rhs = getChildNode(node, 1);
                result = new EvalResult() ;
                result.booleanResult = traverse(lhs, intArgs, nodeArgs).booleanResult
                        || traverse(rhs, intArgs, nodeArgs).booleanResult;
                return result;

            // COMPARISON OPERATORS
            case JJTGT :
                lhs = getChildNode(node, 0);
                rhs = getChildNode(node, 1);
                result = new EvalResult() ;
                result.booleanResult = traverse(lhs, intArgs, nodeArgs).intResult
                        > traverse(rhs, intArgs, nodeArgs).intResult;
                return result;
                        
            case JJTLT :
                lhs = getChildNode(node, 0);
                rhs = getChildNode(node, 1);
                result = new EvalResult() ;
                result.booleanResult = traverse(lhs, intArgs, nodeArgs).intResult
                        < traverse(rhs, intArgs, nodeArgs).intResult;
                return result;
                
            case JJTEQ :
                lhs = getChildNode(node, 0);
                rhs = getChildNode(node, 1);
                result = new EvalResult() ;
                result.booleanResult = traverse(lhs, intArgs, nodeArgs).intResult
                        == traverse(rhs, intArgs, nodeArgs).intResult;
                return result;
                
            case JJTNE :
                lhs = getChildNode(node, 0);
                rhs = getChildNode(node, 1);
                result = new EvalResult() ;
                result.booleanResult = traverse(lhs, intArgs, nodeArgs).intResult
                        != traverse(rhs, intArgs, nodeArgs).intResult;
                return result;
                
            case JJTGTEQ :
                lhs = getChildNode(node, 0);
                rhs = getChildNode(node, 1);
                result = new EvalResult() ;
                result.booleanResult = traverse(lhs, intArgs, nodeArgs).intResult
                        >= traverse(rhs, intArgs, nodeArgs).intResult;
                return result;
                
            case JJTLTEQ :
                lhs = getChildNode(node, 0);
                rhs = getChildNode(node, 1);
                result = new EvalResult() ;
                result.booleanResult = traverse(lhs, intArgs, nodeArgs).intResult
                        <= traverse(rhs, intArgs, nodeArgs).intResult;
                return result;
                
            case JJTDEFINED :
                int argNode = getChildNode(node,0);
                int argNodeId = getId(argNode) ;
                int argNodeValue = getValue(argNode) ;
                
                switch (argNodeId)
                {
                    case JJTINTARGINDEX :
                        result = new EvalResult() ;
                        result.booleanResult = (argNodeValue < intArgs.length) ;
                        return result ;
                    
                    case JJTNODEARGINDEX :
                        result = new EvalResult() ;
                        result.booleanResult =
                            (argNodeValue < nodeArgs.length 
                                    && nodeArgs[argNodeValue] != null) ;
                        return result ;
                        
                    default :
                        return null;
                }
                
            // ASSIGNMENT OPERATOR
            case JJTASSIGNMENT :
                lhs = getChildNode(node, 0);
                rhs = getChildNode(node, 1);
                // evaluate right handside
                
                int rhsValue = traverse(rhs, intArgs, nodeArgs).intResult ;
                
                // assign variable
                variables[getValue(lhs)] = rhsValue ;
                return null ;

            // ARITHMETIC OPERATORS
            case JJTADD :
                lhs = getChildNode(node, 0);
                rhs = getChildNode(node, 1);
                result = new EvalResult() ;
                result.intResult = traverse(lhs, intArgs, nodeArgs).intResult
                        + traverse(rhs, intArgs, nodeArgs).intResult;
                return result;
                
            case JJTSUBTRACT :
                lhs = getChildNode(node, 0);
                rhs = getChildNode(node, 1);
                result = new EvalResult() ;
                result.intResult = traverse(lhs, intArgs, nodeArgs).intResult
                        - traverse(rhs, intArgs, nodeArgs).intResult;
                return result;
                
            case JJTMULT :
                lhs = getChildNode(node, 0);
                rhs = getChildNode(node, 1);
                result = new EvalResult() ;
                result.intResult = traverse(lhs, intArgs, nodeArgs).intResult
                        * traverse(rhs, intArgs, nodeArgs).intResult;
                return result;
                
            case JJTDIV :
                lhs = getChildNode(node, 0);
                rhs = getChildNode(node, 1);
                result = new EvalResult() ;
                result.intResult = traverse(lhs, intArgs, nodeArgs).intResult
                        / traverse(rhs, intArgs, nodeArgs).intResult;
                return result;
                
            case JJTMOD :
                lhs = getChildNode(node, 0);
                rhs = getChildNode(node, 1);
                result = new EvalResult() ;
                result.intResult = traverse(lhs, intArgs, nodeArgs).intResult
                        % traverse(rhs, intArgs, nodeArgs).intResult;
                return result;

            default :

                throw new java.lang.IllegalArgumentException(
                        "eval(): invalid operator! " + id);
        }
    }
    
    
    private EvalResult addTerms(int node,
                                int numChildren,
                                int[] intArgs,
                                AudioDataNode[] nodeArgs)
    {
        
        Vector terms = new Vector() ;
        for (int i = 0; i < numChildren; i++)
        {
            int child = getChildNode(node, i);
            EvalResult result = traverse(child, intArgs, nodeArgs);
            
            if (result.nodeResult != null)
                terms.addElement(result.nodeResult) ;
            else
            {
                AudioDataNode term = AudioDataFactory.getInstance().createAudioDataNode((AudioDataFactory.getInstance().createAudioData((int) result.intResult)));
                terms.addElement(term);
            }
        }
        
        EvalResult result = new EvalResult() ;
        result.nodes = terms ;
        
        return result ;
    }

    public static class EvalResult
    {
        public boolean booleanResult;
        public int intResult;
        public AudioDataNode nodeResult ;
        public boolean stop ;
        public Vector nodes;
    }


}
