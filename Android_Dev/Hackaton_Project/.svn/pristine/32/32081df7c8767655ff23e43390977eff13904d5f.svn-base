/**
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.json.tnme;

import junit.framework.TestCase;

import java.util.Vector;

/***
 * This black box test was written without inspecting the non-free org.json sourcecode.
 */
public class JSONArrayTest extends TestCase {

    public void testEmptyArray() throws JSONException {
        JSONArray array = new JSONArray();
        assertEquals(0, array.length());
        assertEquals("", array.join(" AND "));
        assertNull(array.get(0));
        try {
            array.getBoolean(0);
            fail();
        } catch (NullPointerException e) {
        }

        assertEquals("[]", array.toString());
        assertEquals("[]", array.toString(4));

        // out of bounds is co-opted with defaulting
        assertNull(array.opt(0));
        assertFalse(array.optBoolean(0));
        assertTrue(array.optBoolean(0, true));

        // bogus (but documented) behaviour: returns null rather than an empty object!
        assertNull(array.toJSONObject(new JSONArray()));
    }

    public void testBooleans() throws JSONException {
        JSONArray array = new JSONArray();
        array.put(true);
        array.put(false);
        array.put(2, false);
        array.put(3, false);
        array.put(2, true);
        assertEquals("[true,false,true,false]", array.toString());
        assertEquals(4, array.length());
        assertEquals(Boolean.TRUE, array.get(0));
        assertEquals(Boolean.FALSE, array.get(1));
        assertEquals(Boolean.TRUE, array.get(2));
        assertEquals(Boolean.FALSE, array.get(3));
        assertFalse(array.isNull(0));
        assertFalse(array.isNull(1));
        assertFalse(array.isNull(2));
        assertFalse(array.isNull(3));
        assertEquals(true, array.optBoolean(0));
        assertEquals(false, array.optBoolean(1, true));
        assertEquals(true, array.optBoolean(2, false));
        assertEquals(false, array.optBoolean(3));
        assertEquals("true", array.getString(0));
        assertEquals("false", array.getString(1));
        assertEquals("true", array.optString(2));
        assertEquals("false", array.optString(3, "x"));
        assertEquals("[\n     true,\n     false,\n     true,\n     false\n]", array.toString(5));
        array.put(5);
        try
        {
            array.getBoolean(4);
            fail();
        }
        catch(JSONException e)
        {
            
        }
    }

    public void testNulls() throws JSONException {
        JSONArray array = new JSONArray();
        array.put(3, (Object)null);
        array.put(0, JSONObject.NULL);
        assertEquals(4, array.length());
        assertEquals("[\"null\",\"null\",\"null\",null]", array.toString());

        assertEquals(JSONObject.NULL, array.get(0));
        assertEquals(JSONObject.NULL, array.get(1));
        assertEquals(JSONObject.NULL, array.get(2));
        assertTrue(array.isNull(0));
        assertTrue(array.isNull(1));
        assertTrue(array.isNull(2));
        assertEquals("null", array.optString(0));
    }

    /***
     * Our behaviour is questioned by this bug:
     * http://code.google.com/p/android/issues/detail?id=7257
     */
    public void testParseNullYieldsJSONObjectNull() throws JSONException {
        JSONArray array = new JSONArray("[\"null\",null]");
        array.put((Object)null);
        assertEquals("null", array.get(0));
        assertEquals(JSONObject.NULL, array.get(1));
        assertNull(array.get(2));
        assertEquals("null", array.getString(0));
        assertEquals("null", array.getString(1));
        assertNull(array.getString(2));
    }

    public void testNumbers() throws JSONException {
        JSONArray array = new JSONArray();
        array.put(Double.MIN_VALUE);
        array.put(9223372036854775806L);
        array.put(Double.MAX_VALUE);
        array.put(-0d);
        assertEquals(4, array.length());

        // toString() and getString(int) return different values for -0d
        assertEquals("[4.9E-324,9223372036854775806,1.7976931348623157E308,-0]", array.toString());

        assertEquals(Double.MIN_VALUE, array.get(0));
        assertEquals(9223372036854775806L, array.get(1));
        assertEquals(Double.MAX_VALUE, array.get(2));
        assertEquals(-0d, array.get(3));
        assertEquals(Double.MIN_VALUE, array.getDouble(0));
        assertEquals(9.223372036854776E18, array.getDouble(1));
        assertEquals(Double.MAX_VALUE, array.getDouble(2));
        assertEquals(-0d, array.getDouble(3));
        assertEquals(0, array.getLong(0));
//        assertEquals(9223372036854775806L, array.getLong(1));
        assertEquals(Long.MAX_VALUE, array.getLong(2));
        assertEquals(0, array.getLong(3));
        assertEquals(0, array.getInt(0));
        assertEquals(Integer.MAX_VALUE, array.getInt(1));
        assertEquals(Integer.MAX_VALUE, array.getInt(2));
        assertEquals(0, array.getInt(3));
        assertEquals(Double.MIN_VALUE, array.opt(0));
        assertEquals(Double.MIN_VALUE, array.optDouble(0));
        assertEquals(0, array.optLong(0, 1L));
        assertEquals(0, array.optInt(0, 1));
        assertEquals("4.9E-324", array.getString(0));
        assertEquals("9223372036854775806", array.getString(1));
        assertEquals("1.7976931348623157E308", array.getString(2));
        assertEquals("-0.0", array.getString(3));
    }

    public void testStrings() throws JSONException {
        JSONArray array = new JSONArray();
        array.put("true");
        array.put("5.5");
        array.put("9223372036854775806");
        array.put("null");
        array.put("5\"8' tall");
        assertEquals(5, array.length());
        assertEquals("[\"true\",\"5.5\",\"9223372036854775806\",\"null\",\"5\\\"8' tall\"]",
                array.toString());

        // although the documentation doesn't mention it, join() escapes text and wraps
        // strings in quotes
        assertEquals("\"true\" \"5.5\" \"9223372036854775806\" \"null\" \"5\\\"8' tall\"",
                array.join(" "));

        assertEquals("true", array.get(0));
        assertEquals("null", array.getString(3));
        assertEquals("5\"8' tall", array.getString(4));
        assertEquals("true", array.opt(0));
        assertEquals("5.5", array.optString(1));
        assertEquals("9223372036854775806", array.optString(2, null));
        assertEquals("null", array.optString(3, "-1"));
        assertFalse(array.isNull(0));
        assertFalse(array.isNull(3));

        assertEquals(true, array.getBoolean(0));
        assertEquals(true, array.optBoolean(0));
        assertEquals(true, array.optBoolean(0, false));
        assertEquals(0, array.optInt(0));
        assertEquals(-2, array.optInt(0, -2));

        assertEquals(5.5d, array.getDouble(1));
        assertEquals(5L, array.getLong(1));
        assertEquals(5, array.getInt(1));
        assertEquals(5, array.optInt(1, 3));

        // The last digit of the string is a 6 but getLong returns a 7. It's probably parsing as a
        // double and then converting that to a long. This is consistent with JavaScript.
        assertEquals(9223372036854775807L, array.getLong(2));
        assertEquals(9.223372036854776E18, array.getDouble(2));
        assertEquals(Integer.MAX_VALUE, array.getInt(2));

        assertFalse(array.isNull(3));
        try {
            array.getDouble(3);
            fail();
        } catch (JSONException e) {
        }
        assertEquals(Double.NaN, array.optDouble(3));
        assertEquals(-1.0d, array.optDouble(3, -1.0d));
    }

    public void testJoin() throws JSONException {
        JSONArray array = new JSONArray();
        array.put((Object)null);
        assertEquals("null", array.join(" & "));
        array.put("\"");
        assertEquals("null & \"\\\"\"", array.join(" & "));
        array.put(5);
        assertEquals("null & \"\\\"\" & 5", array.join(" & "));
        array.put(true);
        assertEquals("null & \"\\\"\" & 5 & true", array.join(" & "));
        array.put(new JSONArray("[true,false]"));
        assertEquals("null & \"\\\"\" & 5 & true & [true,false]", array.join(" & "));
        array.put(new JSONObject("{x:6}"));
        assertEquals("null & \"\\\"\" & 5 & true & [true,false] & {\"x\":6}", array.join(" & "));
    }

    public void testJoinWithNull() throws JSONException {
        JSONArray array = new JSONArray("[5,6]");
        assertEquals("5null6", array.join(null));
    }

    public void testJoinWithSpecialCharacters() throws JSONException {
        JSONArray array = new JSONArray("[5,6]");
        assertEquals("5\"6", array.join("\""));
    }

    public void testToJSONObject() throws JSONException {
        JSONArray keys = new JSONArray();
        keys.put("a");
        keys.put("b");

        JSONArray values = new JSONArray();
        values.put(5.5d);
        values.put(false);

        JSONObject object = values.toJSONObject(keys);
        assertEquals(5.5d, object.get("a"));
        assertEquals(false, object.get("b"));

        keys.put(0, "a");
        values.put(0, 11.0d);
        assertEquals(5.5d, object.get("a"));
    }

    public void testToJSONObjectWithNulls() throws JSONException {
        JSONArray keys = new JSONArray();
        keys.put("a");
        keys.put("b");

        JSONArray values = new JSONArray();
        values.put(5.5d);
        values.put((Object)null);

        // null values are stripped!
        JSONObject object = values.toJSONObject(keys);
        assertEquals(1, object.length());
        assertFalse(object.has("b"));
        assertEquals("{\"a\":5.5}", object.toString());
    }

    public void testToJSONObjectMoreNamesThanValues() throws JSONException {
        JSONArray keys = new JSONArray();
        keys.put("a");
        keys.put("b");
        JSONArray values = new JSONArray();
        values.put(5.5d);
        JSONObject object = values.toJSONObject(keys);
        assertEquals(1, object.length());
        assertEquals(5.5d, object.get("a"));
    }

    public void testToJSONObjectMoreValuesThanNames() throws JSONException {
        JSONArray keys = new JSONArray();
        keys.put("a");
        JSONArray values = new JSONArray();
        values.put(5.5d);
        values.put(11.0d);
        JSONObject object = values.toJSONObject(keys);
        assertEquals(1, object.length());
        assertEquals(5.5d, object.get("a"));
    }

    public void testToJSONObjectNullKey() throws JSONException {
        JSONArray keys = new JSONArray();
        keys.put(JSONObject.NULL);
        JSONArray values = new JSONArray();
        values.put(5.5d);
        JSONObject object = values.toJSONObject(keys);
        assertEquals(1, object.length());
        assertEquals(5.5d, object.get("null"));
    }

    public void testPutUnsupportedNumbers() throws JSONException {
        JSONArray array = new JSONArray();

        try {
            array.put(Double.NaN);
            fail();
        } catch (JSONException e) {
        }
        try {
            array.put(0, Double.NEGATIVE_INFINITY);
            fail();
        } catch (JSONException e) {
        }
        try {
            array.put(0, Double.POSITIVE_INFINITY);
            fail();
        } catch (JSONException e) {
        }
    }

    public void testPutUnsupportedNumbersAsObject() throws JSONException {
        JSONArray array = new JSONArray();
        array.put(Double.valueOf(Double.NaN));
        array.put(Double.valueOf(Double.NEGATIVE_INFINITY));
        array.put(Double.valueOf(Double.POSITIVE_INFINITY));
        assertEquals("JSON does not allow non-finite numbers.", array.toString());
    }

    /***
     * Although JSONArray is usually defensive about which numbers it accepts,
     * it doesn't check inputs in its constructor.
     */
    public void testCreateWithUnsupportedNumbers() throws JSONException {
        JSONArray array = new JSONArray("[5.5,"+Double.NaN+"]");
        assertEquals(2, array.length());
        assertEquals(5.5, array.getDouble(0));
        assertEquals(Double.NaN, array.getDouble(1));
    }

    public void testToStringWithUnsupportedNumbers() throws JSONException {
        // when the array contains an unsupported number, toString returns null!
        JSONArray array = new JSONArray("[5.5,"+Double.NaN+"]");
        assertEquals("[5.5,\"NaN\"]", array.toString());
    }
    
    public void testTokenerConstructor() throws JSONException {
        JSONArray object = new JSONArray(new JSONTokener("[false,,]"));
        assertEquals(2, object.length());
        assertEquals(false, object.get(0));
        assertNull(object.get(1));
    }
    
    public void testTokenerConstructorEmpty() throws JSONException
    {
        JSONArray object = new JSONArray(new JSONTokener("[]"));
        assertEquals(0, object.length());
    }
    
    public void testTokenerConstructorUnClosed()
    {
        try
        {
            JSONArray object = new JSONArray(new JSONTokener("[nihao, [wohao,]"));
            fail();
        }
        catch(JSONException e)
        {
            
        }
    }

    public void testTokenerConstructorWithArray() throws JSONException
    {
        String str = "[false,{x:4},[0x11,0011," + (Integer.MAX_VALUE + 10L) + ",nihao]]";
        JSONArray array = new JSONArray(new JSONTokener(str));
        assertEquals(3, array.length());
        assertEquals(false, array.get(0));
        Object obj = array.get(1);
        assertTrue(obj instanceof JSONObject);
        assertEquals(4, ((JSONObject)obj).get("x"));
        obj = array.get(2);
        assertTrue(obj instanceof JSONArray);
        assertEquals(4, ((JSONArray)obj).length());
        assertEquals(0x11, ((JSONArray)obj).getInt(0));
        assertEquals(9, ((JSONArray)obj).getInt(1));
        assertEquals(Integer.MAX_VALUE + 10L, ((JSONArray)obj).getLong(2));
        assertEquals("nihao", ((JSONArray)obj).get(3));
    }
    
    public void testTokenerConstructorWrongType() throws JSONException {
        try {
            new JSONArray(new JSONTokener("{\"foo\": false}"));
            fail();
        } catch (JSONException e) {
        }
    }

    public void testTokenerConstructorNull() throws JSONException {
        try {
            new JSONArray((JSONTokener) null);
            fail();
        } catch (NullPointerException e) {
        }
    }

    public void testTokenerConstructorParseFail() {
        try {
            new JSONArray(new JSONTokener("["));
            fail();
        } catch (JSONException e) {
        } catch (StackOverflowError e) {
            assertTrue(true);
        }
    }

    public void testStringConstructor() throws JSONException {
        JSONArray object = new JSONArray("[false]");
        assertEquals(1, object.length());
        assertEquals(false, object.get(0));
    }

    public void testStringConstructorWrongType() throws JSONException {
        try {
            new JSONArray("{\"foo\": false}");
            fail();
        } catch (JSONException e) {
        }
    }

    public void testStringConstructorNull() throws JSONException {
        try {
            new JSONArray((String) null);
            fail();
        } catch (NullPointerException e) {
        }
    }

    public void testStringConstructorParseFail() {
        try {
            new JSONArray("[");
            fail();
        } catch (JSONException e) {
        } catch (StackOverflowError e) {
            assertTrue(true);
        }
    }
    
    public void testVectorConstructorNull()
    {
        JSONArray array = new JSONArray((Vector)null);
        assertEquals(0, array.length());
    }
    
    public void testVectorConstructor()
    {
        Vector v = new Vector();
        v.addElement("test");
        v.addElement(5);
        v.addElement(false);
        JSONArray array = new JSONArray(v);
        assertEquals(3, array.length());
        try
        {
            assertEquals("test", array.get(0));
        }
        catch (JSONException e)
        {
            fail();
            e.printStackTrace();
        }
        try
        {
            assertEquals(5, array.get(1));
        }
        catch (JSONException e)
        {
            fail();
            e.printStackTrace();
        }
        try
        {
            assertEquals(false, array.get(2));
        }
        catch (JSONException e)
        {
            fail();
            e.printStackTrace();
        }
    }

    public void testCreate() throws JSONException {
        JSONArray array = new JSONArray("[5.5,true]");
        assertEquals(2, array.length());
        assertEquals(5.5, array.getDouble(0));
        assertEquals(true, array.get(1));
        assertEquals("[5.5,true]", array.toString());
    }

    public void testAccessOutOfBounds() throws JSONException {
        JSONArray array = new JSONArray();
        array.put("foo");
        assertEquals(null, array.opt(3));
        assertEquals(null, array.opt(-3));
        assertEquals("", array.optString(3));
        assertEquals("", array.optString(-3));
        assertNull(array.get(3));
        assertNull(array.get(-3));
        assertNull(array.getString(3));
        assertNull(array.getString(-3));
    }
    
    public void testGetJSONArray() throws JSONException
    {
        String str = "[5,[A,0x60]]";
        JSONArray parentArray = new JSONArray(str);
        assertEquals(2, parentArray.length());
        JSONArray childArray = parentArray.getJSONArray(1);
        assertEquals(2, childArray.length());
        
        try
        {
            parentArray.getJSONArray(0);
            fail();
        }
        catch(JSONException e)
        {
            
        }
    }
    
    public void testGetJSONObject() throws JSONException
    {
        String str = "[6,{p:0x68}]";
        JSONArray parentArray = new JSONArray(str);
        assertEquals(2, parentArray.length());
        try
        {
            parentArray.getJSONObject(0);
            fail();
        }
        catch(JSONException e)
        {
            
        }
        JSONObject object = parentArray.getJSONObject(1);
        assertEquals(0x68, object.getInt("p"));
    }
    
    public void testOptJSONArray() throws JSONException
    {
        String str = "[5,[A,0x60]]";
        JSONArray parentArray = new JSONArray(str);
        assertEquals(2, parentArray.length());
        JSONArray childArray = parentArray.optJSONArray(1);
        assertEquals(2, childArray.length());
        assertNull(parentArray.optJSONArray(0));
        assertNull(parentArray.optJSONArray(100));
    }
    
    public void testOptJSONObject() throws JSONException
    {
        String str = "[6,{p:0x68}]";
        JSONArray parentArray = new JSONArray(str);
        assertEquals(2, parentArray.length());
        assertNull(parentArray.optJSONObject(0));
        JSONObject object = parentArray.optJSONObject(1);
        assertEquals(0x68, object.getInt("p"));
        assertNull(parentArray.optJSONObject(10));
    }
    
    public void testOptLong() throws JSONException
    {
        String str = "[false,"+ (Long.MIN_VALUE) +"]";
        JSONArray array = new JSONArray(str);
        assertEquals(Long.MIN_VALUE, array.optLong(1));
        assertEquals(1024, array.optLong(0, 1024));
    }
    
    public void testPutVector() throws JSONException
    {
        Vector v = new Vector();
        v.addElement("ABCDE");
        v.addElement(1024);
        v.addElement(false);

        JSONArray array = new JSONArray();
        assertEquals(0, array.length());
        array.put(v);
        assertEquals(1, array.length());
        JSONArray childArray = array.getJSONArray(0);
        assertEquals("ABCDE", childArray.get(0));
        assertEquals(1024, childArray.get(1));
        assertEquals(false, childArray.get(2));

        JSONArray other = new JSONArray();
        other.put(true);
        other.put(0, v);

        assertEquals(1, other.length());
        childArray = array.getJSONArray(0);
        assertEquals("ABCDE", childArray.get(0));
        assertEquals(1024, childArray.get(1));
        assertEquals(false, childArray.get(2));
    }
    
    public void testPut() throws JSONException
    {
        JSONArray array = new JSONArray();
        array.put(0, 1024);
        array.put(0, 2048);
        assertEquals(2048, array.getInt(0));
        
        array.put(1, Long.MIN_VALUE);
        array.put(1, Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, array.getLong(1));
        
        Object obj = new Object();
        try
        {
            array.put(-1, obj);
            fail();
        }
        catch(JSONException e)
        {
        }
    }
    
    public void testToString() throws JSONException
    {
        JSONArray array = new JSONArray();
        array.put(false);
        array.put(true);
        assertEquals("[false,true]", array.toString());
        
        array = new JSONArray();
        array.put(false);
        assertEquals("[false]",array.toString(4));
        
        array.put("test");
        array.put(123);
        assertEquals("[\n    false,\n    \"test\",\n    123\n]", array.toString(4));
    }
    
    public void testWrite() throws JSONException
    {
        JSONArray array = new JSONArray();
        array.put(true);
        array.put("test");
        
        JSONArray child = new JSONArray();
        child.put("child");
        child.put(1234);
        
        JSONObject obj = new JSONObject();
        obj.put("key1", 1024);
        
        array.put(child);
        array.put(obj);
        
        java.io.StringWriter writer = new java.io.StringWriter();
        array.write(writer);
        
        assertEquals("[true,\"test\",[\"child\",1234],{\"key1\":1024}]", writer.toString());
    }
}
