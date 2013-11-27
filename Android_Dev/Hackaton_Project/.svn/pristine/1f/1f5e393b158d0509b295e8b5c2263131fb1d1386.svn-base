package com.telenav.data.polygon;

/**
 * 
 * @author <a href="mailto:yongyang@telenav.cn">yongyang</a>
 *
 * @version 1.0 CreateTime:Mar 2, 2012 6:37:01 PM
 */
public class Bounds {
	private int minx;
	private int maxx;
	private int miny;
	private int maxy;

	/**
	 * (x1, y1), (x2, y2)
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 */
	public Bounds(int x1, int x2, int y1, int y2) {
		init(x1, x2, y1, y2);
	}

	
    public int hashCode()
    {
        return super.hashCode();
    }


    public void init(int x1, int x2, int y1, int y2) {
		if (x1 < x2) {
			minx = x1;
			maxx = x2;
		} else {
			minx = x2;
			maxx = x1;
		}
		if (y1 < y2) {
			miny = y1;
			maxy = y2;
		} else {
			miny = y2;
			maxy = y1;
		}
	}

	public boolean isNull() {
		return maxx < minx;
	}

	public int getMinX() {
		return minx;
	}

	public int getMaxX() {
		return maxx;
	}

	public int getMinY() {
		return miny;
	}

	public int getMaxY() {
		return maxy;
	}

	/**
	 * 
	 * @param other
	 * @return does the bounds intersect with other bounding box
	 */
	public boolean intersects(Bounds other) {
		if (isNull() || other.isNull())
			return false;
		else
			return other.minx <= maxx && other.maxx >= minx
					&& other.miny <= maxy && other.maxy >= miny;
	}

	/**
	 * 
	 * @param minX
	 * @param minY
	 * @param maxX
	 * @param maxY
	 * @return does the bounds intersect with other bounding box
	 */
	public boolean intersects(int minX, int minY, int maxX, int maxY) {
		return this.minx <= maxX && this.maxx >= minX && this.miny <= maxY
				&& this.maxy >= minY;
	}

	public boolean equals(Object other) {
		if (!(other instanceof Bounds))
			return false;
		Bounds otherEnvelope = (Bounds) other;
		if (isNull())
			return otherEnvelope.isNull();
		else
			return maxx == otherEnvelope.getMaxX()
					&& maxy == otherEnvelope.getMaxY()
					&& minx == otherEnvelope.getMinX()
					&& miny == otherEnvelope.getMinY();
	}

	public String toString() {
		return "(" + minx + "," + miny + " " + maxx + "," + maxy + ")";
	}
}
