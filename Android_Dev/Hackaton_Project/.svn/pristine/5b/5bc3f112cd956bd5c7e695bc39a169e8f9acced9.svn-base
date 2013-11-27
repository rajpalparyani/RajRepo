/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IComparable.java
 *
 */
package com.telenav.sort;

/**
 * This interface should be implemented by all classes that wish to define a natural order of their instances.
 * 
 * The order rule must be both transitive (if x.compareTo(y) < 0 and y.compareTo(z) < 0, then x.compareTo(z) < 0 must
 * hold) and invertible (the sign of the result of x.compareTo(y) must be equal to the negation of the sign of the
 * result of y.compareTo(x) for all combinations of x and y).
 * 
 * In addition, it is recommended (but not required) that if and only if the result of x.compareTo(y) is zero, then the
 * result of x.equals(y) should be true.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Sep 13, 2010
 */
public interface IComparable
{
    /**
     * Compares this object to the specified object to determine their relative order.
     * 
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than another; a positive integer if this instance is greater
     *         than another; 0 if this instance has the same order as another.
     */
    public int compareTo(Object another);
}
