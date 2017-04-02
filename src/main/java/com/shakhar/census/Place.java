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

import java.io.Serializable;
import java.util.Objects;

/**
 * Place defined by the US Census Bureau.
 *
 * @author Shakhar Dasgupta
 */
public class Place implements Serializable {

    private final State state;
    private final String fipsCode;
    private final String name;
    private final String county;

    /**
     * Constructs a <tt>Place</tt> from the specified <tt>state</tt>,
     * <tt>fipsCode</tt>, <tt>placeName</tt> and <tt>county</tt>.
     *
     * @param state the <tt>State</tt> which geographically contains this
     * <tt>Place</tt>
     * @param fipsCode the FIPS Code which uniquely identifies this
     * <tt>Place</tt> within its <tt>State</tt>
     * @param placeName the name of this <tt>Place</tt>
     * @param county the name of the county (or counties separated by commas)
     * associated with this <tt>Place</tt>
     */
    public Place(State state, String fipsCode, String placeName, String county) {
        this.state = state;
        this.fipsCode = fipsCode;
        this.name = placeName;
        this.county = county;
    }

    /**
     * Returns the <tt>State</tt> which geographically contains this
     * <tt>Place</tt>.
     *
     * @return the <tt>State</tt> which geographically contains this
     * <tt>Place</tt>
     */
    public State getState() {
        return state;
    }

    /**
     * Returns the FIPS Code which uniquely identifies this <tt>Place</tt>
     * within its <tt>State</tt>.
     *
     * @return the FIPS Code which uniquely identifies this <tt>Place</tt>
     * within its <tt>State</tt>
     */
    public String getFipsCode() {
        return fipsCode;
    }

    /**
     * Returns the name of this <tt>Place</tt>.
     *
     * @return the name of this <tt>Place</tt>
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name of the county (or counties separated by commas)
     * associated with this <tt>Place</tt>
     *
     * @return the name of the county (or counties separated by commas)
     * associated with this <tt>Place</tt>
     */
    public String getCounty() {
        return county;
    }

    /**
     * Returns the <tt>String</tt> representation of this <tt>Place</tt>. The
     * <tt>String</tt> representation contains the name of the place followed by
     * the <tt>State</tt> USPS code.
     *
     * @return the <tt>String</tt> representation of this <tt>Place</tt>
     */
    @Override
    public String toString() {
        return name + ", " + state.getUspsCode();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.state);
        hash = 67 * hash + Objects.hashCode(this.fipsCode);
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.county);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Place other = (Place) obj;
        if (!Objects.equals(this.fipsCode, other.fipsCode)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.county, other.county)) {
            return false;
        }
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        return true;
    }

}
