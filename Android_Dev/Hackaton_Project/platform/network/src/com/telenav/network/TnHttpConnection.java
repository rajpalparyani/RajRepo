/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnHttpConnection.java
 *
 */
package com.telenav.network;

import java.io.IOException;

/**
 * This interface defines the necessary methods and constants for an HTTP connection. <br />
 * HTTP is a request-response protocol in which the parameters of request must be set before the request is sent. The
 * connection exists in one of three states: <br />
 * <br />
 * <b>Setup,</b> in which the request parameters can be set <br />
 * <b>Connected,</b> in which request parameters have been sent and the response is expected <br />
 * <b>Closed,</b> the final state, in which the HTTP connection as been terminated
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 10, 2010
 */
public interface TnHttpConnection extends TnContentConnection
{
    /**
     * HTTP Get method.
     */
    public static final String GET = "GET";

    /**
     * HTTP Post method.
     */
    public static final String POST = "POST";

    /**
     * HTTP Head method.
     */
    public static final String HEAD = "HEAD";

    /* 2XX: generally "OK" */
    /**
     * HTTP Status-Code 200: OK.
     */
    public static final int HTTP_OK = 200;

    /**
     * HTTP Status-Code 201: Created.
     */
    public static final int HTTP_CREATED = 201;

    /**
     * HTTP Status-Code 202: Accepted.
     */
    public static final int HTTP_ACCEPTED = 202;

    /**
     * HTTP Status-Code 203: Non-Authoritative Information.
     */
    public static final int HTTP_NOT_AUTHORITATIVE = 203;

    /**
     * HTTP Status-Code 204: No Content.
     */
    public static final int HTTP_NO_CONTENT = 204;

    /**
     * HTTP Status-Code 205: Reset Content.
     */
    public static final int HTTP_RESET = 205;

    /**
     * HTTP Status-Code 206: Partial Content.
     */
    public static final int HTTP_PARTIAL = 206;

    /* 3XX: relocation/redirect */

    /**
     * HTTP Status-Code 300: Multiple Choices.
     */
    public static final int HTTP_MULT_CHOICE = 300;

    /**
     * HTTP Status-Code 301: Moved Permanently.
     */
    public static final int HTTP_MOVED_PERM = 301;

    /**
     * HTTP Status-Code 302: Temporary Redirect.
     */
    public static final int HTTP_MOVED_TEMP = 302;

    /**
     * HTTP Status-Code 303: See Other.
     */
    public static final int HTTP_SEE_OTHER = 303;

    /**
     * HTTP Status-Code 304: Not Modified.
     */
    public static final int HTTP_NOT_MODIFIED = 304;

    /**
     * HTTP Status-Code 305: Use Proxy.
     */
    public static final int HTTP_USE_PROXY = 305;

    /* 4XX: client error */

    /**
     * HTTP Status-Code 400: Bad Request.
     */
    public static final int HTTP_BAD_REQUEST = 400;

    /**
     * HTTP Status-Code 401: Unauthorized.
     */
    public static final int HTTP_UNAUTHORIZED = 401;

    /**
     * HTTP Status-Code 402: Payment Required.
     */
    public static final int HTTP_PAYMENT_REQUIRED = 402;

    /**
     * HTTP Status-Code 403: Forbidden.
     */
    public static final int HTTP_FORBIDDEN = 403;

    /**
     * HTTP Status-Code 404: Not Found.
     */
    public static final int HTTP_NOT_FOUND = 404;

    /**
     * HTTP Status-Code 405: Method Not Allowed.
     */
    public static final int HTTP_BAD_METHOD = 405;

    /**
     * HTTP Status-Code 406: Not Acceptable.
     */
    public static final int HTTP_NOT_ACCEPTABLE = 406;

    /**
     * HTTP Status-Code 407: Proxy Authentication Required.
     */
    public static final int HTTP_PROXY_AUTH = 407;

    /**
     * HTTP Status-Code 408: Request Time-Out.
     */
    public static final int HTTP_CLIENT_TIMEOUT = 408;

    /**
     * HTTP Status-Code 409: Conflict.
     */
    public static final int HTTP_CONFLICT = 409;

    /**
     * HTTP Status-Code 410: Gone.
     */
    public static final int HTTP_GONE = 410;

    /**
     * HTTP Status-Code 411: Length Required.
     */
    public static final int HTTP_LENGTH_REQUIRED = 411;

    /**
     * HTTP Status-Code 412: Precondition Failed.
     */
    public static final int HTTP_PRECON_FAILED = 412;

    /**
     * HTTP Status-Code 413: Request Entity Too Large.
     */
    public static final int HTTP_ENTITY_TOO_LARGE = 413;

    /**
     * HTTP Status-Code 414: Request-URI Too Large.
     */
    public static final int HTTP_REQ_TOO_LONG = 414;

    /**
     * HTTP Status-Code 415: Unsupported Media Type.
     */
    public static final int HTTP_UNSUPPORTED_TYPE = 415;

    /* 5XX: server error */

    /**
     * HTTP Status-Code 500: Internal Server Error.
     */
    public static final int HTTP_INTERNAL_ERROR = 500;

    /**
     * HTTP Status-Code 501: Not Implemented.
     */
    public static final int HTTP_NOT_IMPLEMENTED = 501;

    /**
     * HTTP Status-Code 502: Bad Gateway.
     */
    public static final int HTTP_BAD_GATEWAY = 502;

    /**
     * HTTP Status-Code 503: Service Unavailable.
     */
    public static final int HTTP_UNAVAILABLE = 503;

    /**
     * HTTP Status-Code 504: Gateway Timeout.
     */
    public static final int HTTP_GATEWAY_TIMEOUT = 504;

    /**
     * HTTP Status-Code 505: HTTP Version Not Supported.
     */
    public static final int HTTP_VERSION = 505;

    /**
     * Returns the value of the date header field.
     * 
     * @return the sending date of the resource that the URL references, or 0 if not known. The value returned is the
     *         number of milliseconds since January 1, 1970 GMT.
     * @throws IOException if an error occurred connecting to the server.
     */
    public long getDate() throws IOException;

    /**
     * Returns the value of the expires header field.
     * 
     * @return the expiration date of the resource that this URL references, or 0 if not known. The value is the number
     *         of milliseconds since January 1, 1970 GMT.
     * @throws IOException if an error occurred connecting to the server.
     */
    public long getExpiration() throws IOException;

    /**
     * Returns the file portion of the URL of this HttpConnection.
     * 
     * @return the file portion of the URL of this HttpConnection. null is returned if there is no file.
     */
    public String getFile();

    /**
     * Gets a header field value by index.
     * 
     * @param n the index of the header field.
     * @return the value of the nth header field or null if the array index is out of range. An empty String is returned
     *         if the field does not have a value.
     * @throws IOException if an error occurred connecting to the server.
     */
    public String getHeaderField(int n) throws IOException;

    /**
     * Returns the value of the named header field.
     * 
     * @param name of a header field.
     * @return the value of the named header field, or null if there is no such field in the header.
     * @throws IOException if an error occurred connecting to the server.
     */
    public String getHeaderField(String name) throws IOException;

    /**
     * Returns the value of the named field parsed as date. The result is the number of milliseconds since January 1,
     * 1970 GMT represented by the named field. <br/>
     * <br/>
     * This form of getHeaderField exists because some connection types (e.g., http-ng) have pre-parsed headers. Classes
     * for that connection type can override this method and short-circuit the parsing.
     * 
     * @param name the name of the header field.
     * @param def a default value.
     * @return the value of the field, parsed as a date. The value of the def argument is returned if the field is
     *         missing or malformed.
     * @throws IOException if an error occurred connecting to the server.
     */
    public long getHeaderFieldDate(String name, long def) throws IOException;

    /**
     * Returns the value of the named field parsed as a number. <br />
     * <br />
     * This form of getHeaderField exists because some connection types (e.g., http-ng) have pre-parsed headers. Classes
     * for that connection type can override this method and short-circuit the parsing.
     * 
     * @param name the name of the header field.
     * @param def the default value.
     * @return the value of the named field, parsed as an integer. The def value is returned if the field is missing or
     *         malformed.
     * @throws IOException if an error occurred connecting to the server.
     */
    public int getHeaderFieldInt(String name, int def) throws IOException;

    /**
     * Gets a header field key by index.
     * 
     * @param n the index of the header field.
     * @return the key of the nth header field or null if the array index is out of range.
     * @throws IOException if an error occurred connecting to the server.
     */
    public String getHeaderFieldKey(int n) throws IOException;

    /**
     * Returns the host information of the URL of this Http Connection. e.g. host name or IPv4 address.
     * 
     * @return he host information of the URL of this Http Connection.
     */
    public String getHost();

    /**
     * Returns the value of the last-modified header field. The result is the number of milliseconds since January 1,
     * 1970 GMT.
     * 
     * @return the date the resource referenced by this HttpConnection was last modified, or 0 if not known.
     * @throws IOException if an error occurred connecting to the server.
     */
    public long getLastModified() throws IOException;

    /**
     * Returns the network port number of the URL for this Http Connection.
     * 
     * @return the network port number of the URL for this HttpConnection. The default HTTP port number (80) is returned
     *         if there was no port number in the string passed to Connector.open.
     */
    public int getPort();

    /**
     * Returns the protocol name of the URL of this HttpConnection. e.g., http or https
     * 
     * @return the protocol of the URL of this HttpConnection.
     */
    public String getProtocol();

    /**
     * Returns the query portion of the URL of this HttpConnection. RFC2396 defines the query component as the text
     * after the first question-mark (?) character in the URL.
     * 
     * @return the query portion of the URL of this HttpConnection. null is returned if there is no value.
     */
    public String getQuery();

    /**
     * Returns the ref portion of the URL of this HttpConnection. RFC2396 specifies the optional fragment identifier as
     * the the text after the crosshatch (#) character in the URL. This information may be used by the user agent as
     * additional reference information after the resource is successfully retrieved. The format and interpretation of
     * the fragment identifier is dependent on the media type[RFC2046] of the retrieved information.
     * 
     * @return the ref portion of the URL of this HttpConnection. null is returned if there is no value.
     */
    public String getRef();

    /**
     * Get the current request method. e.g. HEAD, GET, POST The default value is GET.
     * 
     * @return the HTTP request method.
     */
    public String getRequestMethod();

    /**
     * Returns the value of the named general request property for this connection.
     * 
     * @param key the keyword by which the request property is known (e.g., "accept").
     * @return the value of the named general request property for this connection. If there is no key with the
     *         specified name then null is returned.
     */
    public String getRequestProperty(String key);

    /**
     * Returns the HTTP response status code. It parses responses like: <br />
     * HTTP/1.0 200 OK <br />
     * HTTP/1.0 401 Unauthorized <br />
     * and extracts the ints 200 and 401 respectively. from the response (i.e., the response is not valid HTTP).
     * 
     * @return the HTTP Status-Code or -1 if no status code can be discerned.
     * @throws IOException if an error occurred connecting to the server.
     */
    public int getResponseCode() throws IOException;

    /**
     * Gets the HTTP response message, if any, returned along with the response code from a server. From responses like: <br />
     * HTTP/1.0 200 OK <br />
     * HTTP/1.0 401 Unauthorized <br />
     * Extracts the Strings "OK" and "Not Found" respectively. Returns null if none could be discerned from the
     * responses (the result was not valid HTTP).
     * 
     * @return the HTTP response message, or null
     * @throws IOException if an error occurred connecting to the server.
     */
    public String getResponseMessage() throws IOException;

    /**
     * Return a string representation of the URL for this connection.
     * 
     * @return the string representation of the URL for this connection.
     */
    public String getURL();

    /**
     * Set the method for the URL request, one of: <br /> {@link #GET}, {@link #POST}, {@link #HEAD} br /> are legal, subject
     * to protocol restrictions. The default method is GET.
     * 
     * @param method
     * @throws IOException
     */
    public void setRequestMethod(String method) throws IOException;

    /**
     * Sets the general request property. If a property with the key already exists, overwrite its value with the new
     * value. <br />
     * <b>Note:</b> HTTP requires all request properties which can legally have multiple instances with the same key to
     * use a comma-separated list syntax which enables multiple properties to be appended into a single property.
     * 
     * @param key the keyword by which the request is known (e.g., "accept").
     * @param value the value associated with it.
     * @throws IOException is thrown if the connection is in the connected state.
     */
    public void setRequestProperty(String key, String value) throws IOException;
}
