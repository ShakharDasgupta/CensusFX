/*
 * Copyright (C) 2017 Shakhar Dasgupta
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.shakhar.census;

/**
 * US state or territory recognized by the US Census Bureau.
 *
 * @author Shakhar Dasgupta
 */
public class State {

    private final String fipsCode;
    private final String uspsCode;
    private final String name;

    /**
     * Constructs a <tt>State</tt> from the specified <tt>fipsCode</tt>,
     * <tt>uspsCode</tt> and <tt>name</tt>.
     *
     * @param fipsCode the FIPS Code which uniquely identifies this
     * <tt>State</tt>
     * @param uspsCode the postal abbreviation used by USPS to identify the
     * <tt>State</tt>
     * @param name the name of this <tt>State</tt>
     */
    public State(String fipsCode, String uspsCode, String name) {
        this.fipsCode = fipsCode;
        this.uspsCode = uspsCode;
        this.name = name;
    }

    /**
     * Returns the FIPS Code which uniquely identifies this <tt>State</tt>.
     *
     * @return the FIPS Code which uniquely identifies this <tt>State</tt>
     */
    public String getFipsCode() {
        return fipsCode;
    }

    /**
     * Returns the postal abbreviation used by USPS to identify the
     * <tt>State</tt>.
     *
     * @return the postal abbreviation used by USPS to identify the
     * <tt>State</tt>
     */
    public String getUspsCode() {
        return uspsCode;
    }

    /**
     * Returns the name of this <tt>State</tt>.
     *
     * @return the name of this <tt>State</tt>
     */
    public String getName() {
        return name;
    }
}
