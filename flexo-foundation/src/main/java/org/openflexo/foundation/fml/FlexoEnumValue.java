/**
 *
 * Copyright (c) 2014-2015, Openflexo
 * <p>
 * This file is part of Flexo-foundation, a component of the software infrastructure
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

import org.openflexo.foundation.InvalidNameException;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.pamela.annotations.*;
import org.openflexo.pamela.annotations.CloningStrategy.StrategyType;

import java.util.logging.Logger;

/**
 * A {@link FlexoEnumValue} represent the possible value of a {@link FlexoEnum}
 *
 * A {@link FlexoEnumValue} is identified by its name (see {@link #getName()})
 *
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(FlexoEnumValue.FlexoEnumValueImpl.class)
@XMLElement
public interface FlexoEnumValue extends FlexoConcept {

    @PropertyIdentifier(type = FlexoEnum.class)
    public static final String FLEXO_ENUM_KEY = "flexoEnum";

    @Getter(value = FLEXO_ENUM_KEY)
    @CloningStrategy(StrategyType.IGNORE)
    public FlexoEnum getFlexoEnum();

    @Setter(FLEXO_ENUM_KEY)
    public void setFlexoEnum(FlexoEnum flexoEnum);

    public int getIndex();

    public static abstract class FlexoEnumValueImpl extends FlexoConceptImpl implements FlexoEnumValue {

        protected static final Logger logger = FlexoLogger.getLogger(FlexoEnumValue.class.getPackage().getName());

        @Override
        public int getIndex() {
            if (getFlexoEnum() != null) {
                return getFlexoEnum().getValues().indexOf(this);
            }
            return -1;
        }

        @Override
        public FMLCompilationUnit getResourceData() {
            if (getFlexoEnum() != null) {
                return getFlexoEnum().getResourceData();
            }
            return null;
        }

        @Override
        public VirtualModel getOwningVirtualModel() {
            if (getFlexoEnum() != null) {
                return getFlexoEnum().getOwner();
            }
            return null;
        }

        @Override
        public void setName(String name) throws InvalidNameException {
            // TODO Auto-generated method stub
            super.setName(name);
        }

    }

}
