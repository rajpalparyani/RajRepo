package com.telenav.tnui.opengles;

import com.telenav.tnui.graphics.AbstractTnImage;

/**
 * Provides a set of utility methods for OpenGL ES applications.<br />
 * <br />
 * The methods in this library are GLU methods as well as modified OpenGL ES methods which support BlackBerry specific
 * data types to simplify development.<br />
 * <br />
 * GLU is the OpenGL Utility Library that contains set of functions to create texture mipmaps from a base image or map
 * coordinates between screen and object space.
 * 
 */
public abstract class TnGLUtils
{
    private static TnGLUtils instance;

    private static int initCount;

    protected TnGLUtils()
    {

    }

    /**
     * Retrieve the instance of TnGLUtils.
     * 
     * @return the instance of TnGLUtils.
     */
    public static TnGLUtils getInstance()
    {
        return instance;
    }

    /**
     * init platform's implementation.
     * 
     * @param ins
     */
    public static void init(TnGLUtils ins)
    {
        if (initCount >= 1)
            return;

        instance = ins;
        initCount++;
    }

    /**
     * Loads a texture from the given Bitmap. <br />
     * <br />
     * The texture is loaded into the currently bound GL texture name and active texture unit. The specified format and
     * type need not match the native format of the bitmap since this method will perform any necessary conversions.
     * When this method returns, the input bitmap can be safely discarded (set to null) since the texture data is stored
     * on the server. <br />
     * <br />
     * A GL error may be set if the texture fails to load (for example, if level, format or type are invalid. To check
     * for possible errors, call glGetError(). <br />
     * 
     * @param gl The GL context to load the texture into.
     * @param target
     * @param level Target mipmap level to load the texture into (zero is the base level).
     * @param image image containing the texutre data to load.
     * @param internalFormat
     * @param border
     */
    public abstract void texImage2D(TnGL gl, int target, int level, AbstractTnImage image, int internalFormat, int border);

    /**
     * scale the bitmap.
     * 
     * @param image the source image
     * @param width the width of dest image
     * @param height the height of dest image
     * @param internalFormat image's format
     * @return a image
     */
    public abstract AbstractTnImage scaleBitmap(AbstractTnImage image, int width, int height, int internalFormat);
    
    /**
     * Return x raised to the power of y.
     * 
     * @param x the base value.
     * @param y the exponent.
     * @return
     */
    public abstract double pow(double x, double y);

    /**
     * Returns the arc tangent of the value x.
     * 
     * @param x the value.
     * @return the arc tangent of the argument in radians.
     */
    public abstract double atan(double x);

    /**
     * Converts rectangular coordinates (x,y) to polar coordinates (r,theta). This method computes the phase theta by
     * computing the arc tangent of y/x in the range of -pi to pi.
     * 
     * @param y the abscissa coordinate.
     * @param x the ordinate coordinate.
     * @return the theta component of the point (r,theta) in polar coordinates that corresponds to the point (x,y) in
     *         Cartesian coordinates.
     */
    public abstract double atan2(double y, double x);

    /**
     * Returns the arc cosine of the value x.
     * 
     * @param x the value.
     * @return the arc cosine of the argument in radians.
     */
    public abstract double acos(double x);

    /**
     * Return the natural logarithm (base e) of x. Interesting cases: <br />
     * If the argument is less than zero (including negative infinity), the result is NaN.<br />
     * If the argument is zero, the result is negative infinity.<br />
     * If the argument is positive infinity, the result is positive infinity.<br />
     * If the argument is NaN, the result is NaN. <br />
     * 
     * @param x a number greater than zero.
     * @return the natural logarithm (base e) of the argument.
     */
    public abstract double log(double x);

    /**
     * Returns the closest double approximation of the base 10 logarithm of the argument. The returned result is within
     * 1 ulp (unit in the last place) of the real result.
     * 
     * @param x the value whose base 10 log has to be computed.
     * @return the natural logarithm of the argument.
     */
    public abstract double log10(double x);

    /**
     * Return the exponential (base e) of x. Interesting cases: <br />
     * If the argument is negative infinity, the result is zero.<br />
     * If the argument is positive infinity, the result is positive infinity.<br />
     * If the argument is NaN, the result is NaN.<br />
     * If the argument is greater than 7.09e+02, the result is positive infinity.<br />
     * If the argument is less than -7.45e+02, the result is zero. <br />
     * 
     * @param x the power to raise e to.
     * @return raised to the power x.
     */
    public abstract double exp(double x);
}
