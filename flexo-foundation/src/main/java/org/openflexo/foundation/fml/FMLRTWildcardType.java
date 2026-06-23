/**
 *
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * <p>
 * This file is part of Connie-core, a component of the software infrastructure
 * developed at Openflexo.
 * <p>
 * <p>
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either
 * version 1.1 of the License, or any later version ), which is available at
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * <p>
 * You can redistribute it and/or modify under the terms of either of these licenses
 * <p>
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 * <p>
 * Additional permission under GNU GPL version 3 section 7
 * <p>
 * If you modify this Program, or any covered work, by linking or
 * combining it with software containing parts covered by the terms
 * of EPL 1.0, the licensors of this Program grant you additional permission
 * to convey the resulting work. *
 * <p>
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.
 * <p>
 * See http://www.openflexo.org/license.html for details.
 * <p>
 * <p>
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 *
 */

package org.openflexo.foundation.fml;

import org.openflexo.connie.type.TypingSpace;
import org.openflexo.connie.type.WildcardTypeImpl;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

public class FMLRTWildcardType extends WildcardTypeImpl<FMLRTType> implements FMLRTType {

    public FMLRTWildcardType() {
        super();
    }

    public FMLRTWildcardType(List<FMLRTType> upperBounds, List<FMLRTType> lowerBounds) {
        super(upperBounds, lowerBounds);
    }

    public FMLRTWildcardType(FMLRTType[] upperBounds, FMLRTType[] lowerBounds) {
        super(upperBounds, lowerBounds);
    }

    public static FMLRTWildcardType makeUpperBoundWilcard(FMLRTType upperBound) {
        FMLRTWildcardType returned = new FMLRTWildcardType();
        returned.addUpperBound(upperBound);
        return returned;
    }

    public static FMLRTWildcardType makeLowerBoundWilcard(FMLRTType lowerBound) {
        FMLRTWildcardType returned = new FMLRTWildcardType();
        returned.addLowerBound(lowerBound);
        return returned;
    }

    public static FMLRTWildcardType fromWilcard(WildcardType wt) {
        List<FMLRTType> upper = new ArrayList<>();
        for (Type t : wt.getUpperBounds()) {
            if (t instanceof FMLRTType) {
                upper.add((FMLRTType) t);
            }
        }
        List<FMLRTType> lower = new ArrayList<>();
        for (Type t : wt.getLowerBounds()) {
            if (t instanceof FMLRTType) {
                lower.add((FMLRTType) t);
            }
        }
        return new FMLRTWildcardType(upper, lower);
    }

    @Override
    public Class<FMLRTType> getTypeClass() {
        return FMLRTType.class;
    }

    @Override
    public FMLRTWildcardType translateTo(TypingSpace typingSpace) {
        if (hasConnieTypeArguments()) {
            List<FMLRTType> newUpper = new ArrayList<>();
            for (FMLRTType t : getUpperBounds()) {
                newUpper.add(t.translateTo(typingSpace));
            }
            List<FMLRTType> newLower = new ArrayList<>();
            for (FMLRTType t : getLowerBounds()) {
                newLower.add(t.translateTo(typingSpace));
            }
            return new FMLRTWildcardType(newUpper, newLower);
        }
        return this;
    }
}
