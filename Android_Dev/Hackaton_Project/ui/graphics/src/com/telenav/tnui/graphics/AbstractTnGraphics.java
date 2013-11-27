/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnGraphics.java
 *
 */
package com.telenav.tnui.graphics;



/**
 * Provides a surface on which applications can draw. 
 * <br />
 * <br />
 * A graphics object encapsulates the state information needed for basic rendering operations, making it accessible to applications (for example, the current font, and drawing color).
 * <br />
 * <br /> 
 * <b>The context stack</b>
 * <br /> 
 * Typically your application maintains one graphics object for each screen it must present to the user. Each manager controlled by the screen uses that graphics object to handle the layout and painting of each field the manager contains.
 * <br />  
 * You can use it like below:
 * <br /> 
 * <br /> 
 * g = getGraphics();
 * <br /> 
 * g.pushClip(...);
 * <br /> 
 * g.draw();
 * <br /> 
 * g.popClip();
 * <br /> 
 * <br />
 * <b>The drawing anchor</b>
 * <br />
 * When you use one of this class's methods to draw an object, or text, you must set the drawing anchor.
 * <br />
 * Please see {@link #HCENTER}, {@link #VCENTER}, {@link #LEFT}, {@link #RIGHT}, {@link #TOP}, {@link #BOTTOM}.
 * <br />
 * <br />
 * <b>What happens to clipped objects</b>
 * <br />
 * When you draw an object and the size of that object would have a portion of it fall outside the clipping region, then those portions are clipped off (i.e. the object or text is cropped to fit the clipping region). 
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-2
 */
public abstract class AbstractTnGraphics
{
    /**
     * Drawing position for horizontally centered text.
     */
    public final static int HCENTER = 1;

    /**
     * Drawing position for the vertical center of the text.
     */
    public final static int VCENTER = 2;

    /**
     * Drawing position for left-aligned text.
     */
    public final static int LEFT = 4;

    /**
     * Drawing position for right-aligned text.
     */
    public final static int RIGHT = 8;

    /**
     * Drawing position for the top of the text.
     */
    public final static int TOP = 16;
    
    /**
     * Drawing position for the bottom of the text.
     */
    public final static int BOTTOM = 32;
    
    /**
     * Drawing position for the baseLine of the text.
     */
    public final static int BASE_LINE = 64;
    
    /**
     * Drawing position for the vertical center of the text to be paint.
     */
    public final static int FONT_ABSOLUTE_VCENTER = 128;
    
    /**
     * Drawing position for the vertical center of some specific characters, which will keep the same base line as the text to be draw.
     */
    public final static int FONT_VISUAL_VCENTER = 256;
    

	/**
	 * The stroke ends with the path, and does not project beyond it.
	 */
	public final static int CAP_BUTT = 0;

	/**
	 * The stroke projects out as a semicircle, with the center at the end of
	 * the path.
	 */
	public final static int CAP_ROUND = 1;

	/**
	 * The stroke projects out as a square, with the center at the end of the
	 * path.
	 */
	public final static int CAP_SQUARE = 2;
	
	/**
	 * The outer edges of a join meet at a sharp angle
	 */
	public final static int JOIN_MITER = 0;

	/**
	 * The outer edges of a join meet in a circular arc.
	 */
	public final static int JOIN_ROUND = 1;

	/**
	 * The outer edges of a join meet with a straight line
	 */
	public final static int JOIN_BEVEL = 2;
    
	static AbstractTnGraphics instance;
	
	/**
     * Retrieve the instance of graphics.
     * 
     * @return {@link AbstractTnGraphics}
     */
    public static AbstractTnGraphics getInstance()
    {
        return instance;
    }
    
    public static void init(AbstractTnGraphics graphics)
    {
        instance = graphics;
    }
    
    /**
     * Pushes a clipping region (and optional drawing offset) onto the context stack. 
     * <br />
     * This clipping region is updated by intersecting with the previous clipping region.
     *  
     * @param rect {@link TnRect}
     */
    public abstract void pushClip(TnRect rect);
    
    /**
     * Draws a text character.
     * <br />
     * Use this method to draw a text character. You specify the position of the drawing anchor, and this method renders the character's glyph using the default font. 
     * <br />
     * If the character to draw is too large for the current clipping region, this method crops to fit.
     * 
     * @param character - Character to draw.
     * @param x - Horizontal position of drawing anchor.
     * @param y - Vertical position of drawing anchor.
     * @param anchor - Combination of constant drawing position.
     */
    public abstract void drawChar(char character, int x, int y, int anchor);

    /**
     * Draws a text character.
     * <br />
     * Use this method to draw a text character. You specify the position of the drawing anchor, and this method renders the character's glyph using the default font. 
     * <br />
     * If the character to draw is too large for the current clipping region, this method crops to fit.
     * 
     * @param character - Character to draw.
     * @param x - Horizontal position of drawing anchor.
     * @param y - Vertical position of drawing anchor.
     * @param anchor - Combination of constant drawing position.
     * @param angle - the angle of character when draw
     */
    public abstract void drawChar(char character, int x, int y, int anchor, int angle);

    /**
     * Draws a image.
     * <br />
     * Use this method to draw a image. You specify the destination region for the bitmap by describing the extent of the region with passed parameters. 
     * <br />
     * You must also specify the extent of the image to draw (the top left corner of this extent defined by the top and left parameters). If a portion of part of the image you want to draw falls outside the size of the destination region, this method crops to fit. 
     * 
     * @param img - image source.
     * @param x - Horizontal position of drawing anchor.
     * @param y - Vertical position of drawing anchor.
     * @param anchor - Combination of constant drawing position.
     */
    public abstract void drawImage(AbstractTnImage img, int x, int y, int anchor);
    
    /**
     * Draw the specified image, scaling/translating automatically to fill the destination rectangle. If the source rectangle is not null, it specifies the subset of the image to draw. 
     * <br />
     * Use this method to draw a scale image. 
     * 
     * @param img - image source.
     * @param src - May be null. The subset of the bitmap to be drawn.
     * @param dst - the rectangle that the bitmap will be scaled/translated to fit into.
     */
    public abstract void drawImage(AbstractTnImage img, TnRect src, TnRect dst);

    /**
     * Draws a line. 
     * <br />
     * Use this method to draw a line; you specify coordinates for the end points of the line.
     * 
     * @param x1 - Horizontal position of the line's starting point.
     * @param y1 - Vertical position of the line's starting point.
     * @param x2 - Horizontal position of the line's ending point.
     * @param y2 - Vertical position of the line's ending point.
     */
    public abstract void drawLine(int x1, int y1, int x2, int y2);
    
    /**
     * Draws a line. 
     * <br />
     * Use this method to draw a line; you specify coordinates for the end points of the line.
     * 
     * @param p1 {@link TnPoint}
     * @param p2 {@link TnPoint}
     */
    public abstract void drawLine(TnPoint p1, TnPoint p2);

    /**
     * Draws a rectangle. 
     * <br />
     * Use this method to draw a rectangle. You specify the top and left edges of the rectangle, and its width and height. This method draws the right edge of the rectangle at (x+width-1),
     * the bottom edge at (y+height-1). The resulting rectangle will thus have an area of (width * height). 
     * <br />
     * If you pass in a zero or negative value for either the height or width, this method draws nothing.
     * 
     * @param x - Left edge of the rectangle.
     * @param y - Top edge of the rectangle.
     * @param width - Width of the rectangle.
     * @param height - Height of the rectangle.
     */
    public abstract void drawRect(int x, int y, int width, int height);

    /**
     * Draws a rectangle. 
     * <br />
     * Use this method to draw a rectangle. You specify the top and left edges of the rectangle, and its width and height. This method draws the right edge of the rectangle at (x+width-1),
     * the bottom edge at (y+height-1). The resulting rectangle will thus have an area of (width * height). 
     * <br />
     * If you pass in a zero or negative value for either the height or width, this method draws nothing.
     *  
     * @param rect {@link TnRect}
     */
    public abstract void drawRect(TnRect rect);
    
    /**
     * Draws raw RGB data from an int array.
     * <br />
     * 
     * @param rgbData- Color data to use, of the form 0xAARRGGBB.
     * @param offset- Offset into the data to start drawing from.
     * @param scanlength - Width of a scanline within the data.
     * @param x - Left edge of rectangle.
     * @param y- Top edge of the rectangle.
     * @param width- Width of the rectangle.
     * @param height - Height of the rectangle.
     * @param hasAlpha - True if the alpha channel of the colors contains valid values. If false, the alpha byte is ignored (assumed to
     * be 0xFF for every pixel).
     */
    public abstract void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean hasAlpha);

    /**
     * Draws a text.
     * <br />
     * Use this method to draw a text. You specify the position of the drawing anchor, and this method renders the text using the default font. 
     * <br />
     * If the character to draw is too large for the current clipping region, this method crops to fit.
     * 
     * @param str - string to draw.
     * @param x - Horizontal position of drawing anchor.
     * @param y - Vertical position of drawing anchor.
     * @param anchor - Combination of constant drawing position.
     */
    public abstract void drawString(String str, int x, int y, int anchor);

    /**
     * Draw the specified arc, which will be scaled to fit inside the
     * specified oval. If the sweep angle is >= 360, then the oval is drawn
     * completely.
     * 
     * @param xC center X of circle
     * @param yC center Y of circle
     * @param d radius of circle
     * @param startAngle Starting angle (in degrees) where the arc begins
     * @param sweepAngle Sweep angle (in degrees) measured clockwise
     */
    public abstract void fillArcs(int xC, int yC, int d, float startAngle, float sweepAngle);
    
    /**
     * Fills a rectangle. 
     * <br />
     * Use this method to fill a rectangle. You specify the top and left edges of the rectangle, and its width and height. 
     * This method paints the right edge of the rect at (x+width), the bottom edge at (y+height).
     * <br />
     * The resulting rectangle will thus have an area of width * height. 
     * 
     * @param x - Left edge of the rectangle.
     * @param y - Top edge of the rectangle.
     * @param width - Width of the rectangle.
     * @param height - Height of the rectangle.
     */
    public abstract void fillRect(int x, int y, int width, int height);
    
    /**
     * Fills a rectangle. 
     * <br />
     * Use this method to fill a rectangle. You specify the top and left edges of the rectangle, and its width and height. 
     * This method paints the right edge of the rect at (x+width), the bottom edge at (y+height).
     * <br />
     * The resulting rectangle will thus have an area of width * height. 
     * 
     * @param rect {@link TnRect}
     */
    public abstract void fillRect(TnRect rect);

    /**
     * Fills a triangle. 
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public abstract void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3);

    /**
     * Fills a circular covering a specified rectangle. 
     * The center of the filled arc is the center of a rectangle.
     * 
     * @param xC - center X of circle
     * @param yC - center Y of circle
     * @param d - radius of circle
     * @param isClean - whether should clear Xfermode
     */
    public abstract void fillCircle(int xC, int yC, int d, boolean isClear);

    /**
     * draws a circular covering a specified rectangle. 
     * The center of the filled arc is the center of a rectangle.
     * 
     * @param xC - center X of circle
     * @param yC - center Y of circle
     * @param d - radius of circle
     */
    public abstract void drawCircle(int xC, int yC, int d);

    /**
     * Retrieves the current foreground drawing color.
     * 
     * @return 0xAARRGGBB
     */
    public abstract int getColor();

    /**
     * Retrieves the current font for the current graphics context.
     * 
     * @return {@link AbstractTnFont}
     */
    public abstract AbstractTnFont getFont();

    /**
     * Retrieves the stroke's width.
     * 
     * @return int
     */
    public abstract int getStrokeWidth();

    /**
     * Retrieves the horizontal component of the current drawing offset.
     * 
     * @return int
     */
    public abstract int getTranslateX();
    
    /**
     * Retrieves the vertical component of the current drawing offset.
     * 
     * @return int
     */
    public abstract int getTranslateY();

    /**
     * Pushes a clipping region (and optional drawing offset) onto the context stack. 
     * <br />
     * This clipping region is updated by intersecting with the previous clipping region.
     * 
     * @param x - Left edge of clipping region.
     * @param y- Top edge of clipping region.
     * @param width - Width in pixels of clipping region.
     * @param height - Height in pixels of clipping region.
     */
    public abstract void pushClip(int x, int y, int width, int height);

    /**
     * Pops a drawing context off the stack. 
     * <br />
     * Use this method to pop off the context stack the last context pushed.
     */
    public abstract void popClip();
    
    /**
     * Sets the current color.
     * <br /> 
     * All subsequent rendering will be done in this color (more precisely, rendering will be done in a display color that is nearest to the color you specify).
     * 
     * @param RGB 0x00RRGGBB.
     */
    public abstract void setColor(int RGB);

    /**
     * Sets the current color.
     * <br /> 
     * All subsequent rendering will be done in this color (more precisely, rendering will be done in a display color that is nearest to the color you specify).
     * 
     * @param alpha
     * @param red
     * @param green
     * @param blue
     */
    public abstract void setColor(int alpha, int red, int green, int blue);

    /**
     * Sets the current font for the current graphics context. 
     * <br />
     * Subsequent text rendering uses this font.
     * 
     * @param font
     */
    public abstract void setFont(AbstractTnFont font);
    
    /**
     * set the stroke's width.
     * 
     * @param width
     */
    public abstract void setStrokeWidth(int width);

    /**
     * Apply a translation to the current drawing offset. 
     * <br />
     * The drawing offset is updated by adding the x and y offsets.
     * 
     * @param x Horizontal offset to add to the current x drawing offset.
     * @param y Vertical offset to add to the current y drawing offset.
     */
    public abstract void translate(int x, int y);
    
    /**
     * Apply a rotation to the current drawing offset
     * <br />
     * the drawing offset is updated by the rotating degree at original point
     * @param degree The amount to rotate, in degrees
     */
    public abstract void rotate(int degree);
    
    /**
     * Apply a rotation to the current drawing offset
     * <br />
     * the drawing offset is updated by the rotating degree at pivot point (x, y)
     * @param degree degree The amount to rotate, in degrees
     * @param x The x-coord for the pivot point (unchanged by the rotation)
     * @param y The y-coord for the pivot point (unchanged by the rotation)
     */
    public abstract void rotate(int degree, int x, int y);

    /**
     * check if this rectangle is intersect with the clip.
     * 
     * @param clipBounds
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public abstract boolean isIntersectWithClip(TnRect clipBounds, int x, int y, int width, int height);
    
    /**
     * check if this rectangle is intersect with the clip.
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public abstract boolean isIntersectWithClip(int x, int y, int width, int height);
    
    /**
     * Retrieve the clip bounds of graphics.
     * 
     * @param clipBounds Return the clip bounds here.
     */
    public abstract void getClipBounds(TnRect clipBounds);

    /**
     * Draw a set of path outlines. 
     * <br />
     * Use this method to draw a set of path outlines that contains line segments and curves.
     * The xPts and yPts arrays keep track of each vertex in the path. Each value in the xPts array must have corresponding values 
     * at the same index in the yPts array and the pointTypes array.
     * <br />
     * The offsets array keeps track of the locations of each path in the set of paths. If offsets is null, the xPts and yPts data will be treated as a single path, and these arrays must have the same length. 
     * <br />
     * Note that each non-null array parameter must be a different array.
     * 
     * @param xx - Ordered list of x values for each vertex in the paths.
     * @param yy - Ordered list of y values for each vertex in the paths.
     */
    public abstract void drawPathOutline(int[] xx, int[] yy);

    /**
     * Draw a set of path outlines. 
     * <br />
     * Use this method to draw a set of path outlines that contains line segments and curves.
     * The xPts and yPts arrays keep track of each vertex in the path. Each value in the xPts array must have corresponding values 
     * at the same index in the yPts array and the pointTypes array.
     * <br />
     * The offsets array keeps track of the locations of each path in the set of paths. If offsets is null, the xPts and yPts data will be treated as a single path, and these arrays must have the same length. 
     * <br />
     * Note that each non-null array parameter must be a different array.
     * 
     * @param points - Ordered list of x/y values for each vertex in the paths.
     * @param qFactor - the shift of the x/y values
     */
    public abstract void drawPathOutline(int[] points, int numPoints, int qFactor);
    
    /**
     * Draws a set of filled paths. 
     * <br />
     * Use this method to draw one or more filled non-intersecting paths that contain line segments or curves. You specify the x and y coordinates and point types of the paths desired. 
     * <br />
     * The xPts and yPts arrays keep track of each vertex in the polygon and each value in the xPts array must have a corresponding value at the same index in the yPts array, and a corresponding value at the same index in the pointTypes array. 
     * <br />
     * None of the edges in a path may cross any other edge in any of the paths (including itself). If edges do cross, the drawing behaviour is undefined. A path may be fully contained within another path, e.g. a "donut" shape. 
     * <br />
     * The paths are filled by the "even-odd" rule. Thus if a ray is drawn from any point, the area containing the point will be filled if the ray passes through an odd number of edges and will not be filled if is passes through an even number of edges. 
     * 
     * @param xx - Ordered list of x values for each vertex in the paths.
     * @param yy - Ordered list of y values for each vertex in the paths.
     */
    public abstract void drawFilledPath(int[] xx, int[] yy);
    
    /**
     * Draws a set of filled paths. 
     * <br />
     * Use this method to draw one or more filled non-intersecting paths that contain line segments or curves. You specify the x and y coordinates and point types of the paths desired. 
     * <br />
     * The xPts and yPts arrays keep track of each vertex in the polygon and each value in the xPts array must have a corresponding value at the same index in the yPts array, and a corresponding value at the same index in the pointTypes array. 
     * <br />
     * None of the edges in a path may cross any other edge in any of the paths (including itself). If edges do cross, the drawing behaviour is undefined. A path may be fully contained within another path, e.g. a "donut" shape. 
     * <br />
     * The paths are filled by the "even-odd" rule. Thus if a ray is drawn from any point, the area containing the point will be filled if the ray passes through an odd number of edges and will not be filled if is passes through an even number of edges. 
     * 
     * @param points - Ordered list of x/y values for each vertex in the paths.\
     * @param qFactor - the shift of x/y values
     */
    public abstract void drawFilledPath(int[] points, int numPoints, int qFactor);
    
    /**
     * Draws a point. 
     * <br />
     * This method draws the pixel located at a coordinate you specify.
     * 
     * @param x0 - Horizontal position of the point to draw.
     * @param y0 - Vertical position of the point to draw.
     */
    public abstract void drawPoint(int x0, int y0);

    /**
     * Clears the entire graphics area to the current background with special color.
     * 
     * @param color
     * @param w
     * @param h
     */
    public void clear(int color, int w, int h)
    {
        setColor(color);
        fillRect(0, 0, w, h);
    }

    /**
     * Fills a rectangle with rounded edges. 
     * <br />
     * Use this method to fill a rectangle with rounded edges. You specify the top and left edges of the rectangle, and its width and height. 
     * 
     * @param x - Left edge of the rectangle.
     * @param y - Top edge of the rectangle.
     * @param width - Width of the rectangle.
     * @param height - Height of the rectangle.
     * @param arcWidth - Width of arc used to round the four corners.
     * @param arcHeight - Height of arc used to round the four corners.
     */
    public abstract void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);

    /**
     * Draws a rectangle with rounded edges. 
     * <br />
     * Use this method to draw a rectangle with rounded edges. You specify the top and left edges of the rectangle, and its width and height. 
     * 
     * @param x - Left edge of the rectangle.
     * @param y - Top edge of the rectangle.
     * @param width - Width of the rectangle.
     * @param height - Height of the rectangle.
     * @param arcWidth - Width of arc used to round the four corners.
     * @param arcHeight - Height of arc used to round the four corners.
     */
    public abstract void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);

    /**
     * Draws a triangle with rounded edges. 
     * <br />
     * Use this method to draw a triangle with rounded edges. You specify 3 points of the triangle. 
     * 
     * @param x1 - Left edge of the first point.
     * @param y1 - Top edge of the first point.
     * @param x2 - Left edge of the second point.
     * @param y2 - Top edge of the second point.
     * @param x3 - Left edge of the third point.
     * @param y3 - Top edge of the third point.
     * @param radius Amount to round sharp angles between line segments.
     */
    public abstract void drawRoundTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int radius);

    /**
     * Fills a triangle with rounded edges. 
     * <br />
     * Use this method to draw a triangle with rounded edges. You specify 3 points of the triangle. 
     * 
     * @param x1 - Left edge of the first point.
     * @param y1 - Top edge of the first point.
     * @param x2 - Left edge of the second point.
     * @param y2 - Top edge of the second point.
     * @param x3 - Left edge of the third point.
     * @param y3 - Top edge of the third point.
     * @param radius Amount to round sharp angles between line segments.
     */
    public abstract void fillRoundTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int radius);
    
    /**
     * Draws a polyline with points.
     * 
     * @param points - the points of line.
     * @param numPoints - the count of points.
     * @param w - the width of line.
     */
    public abstract void drawPolyline(int[] points, int numPoints, int w);

    /**
     * Set the paint's Cap. The Cap specifies the treatment for the beginning and ending of
     * stroked lines and paths. The default is CAP_BUTT.
     *
     * @param cap {@link #CAP_BUTT}, {@link #CAP_ROUND}, {@link #CAP_SQUARE}
     */
    public abstract void setStrokeCap(int strokeCap);
    
    /**
     * Return the graphics Cap, controlling how the start and end of stroked
     * lines and paths are treated.
     *
     * @return the line cap style for the graphics.
     */
    public abstract int getStrokeCap();
    
    /**
     * Set the paint's Join. The Join specifies the treatment where lines and curve segments
     * join on a stroked path. The default is JOIN_MITER.
     * 
     * @param strokeJoin {@link #JOIN_BEVEL}, {@link #JOIN_MITER}, {@link #JOIN_ROUND}
     */
    public abstract void setStrokeJoin(int strokeJoin);
    
    /**
     * Return the graphics stroke join type.
     *
     * @return the graphics Join.
     */
    public abstract int getStrokeJoin();
    
    /**
     * It's for anti-aliased rendering. Smooth out the edges of what is being drawn, but is has no impact on the
     * interior of the shape.
     * 
     * @param isAntiAlias true if the antialias bit is set in the paint's flags.
     */
    public abstract void setAntiAlias(boolean isAntiAlias);
    
    /**
     * It's for anti-aliased rendering. Smooth out the edges of what is being drawn, but is has no impact on the
     * interior of the shape.
     * 
     * @return true if the antialias bit is set in the paint's flags.
     */
    public abstract boolean isAntiAlias();
    
    /**
     * Retrieve the native graphics object.
     * <br />
     * For example: At Android platform, will be Canvas, and at RIM platform, will be Graphics.
     * 
     * @return Object
     */
    public abstract Object getNativeGraphics();
    
    /**
     * set the native graphics.
     * 
     * @param graphics At Android platform, will be Canvas, and at RIM platform, will be Graphics.
     */
    public abstract void setGraphics(Object graphics);
}
