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

import org.openflexo.connie.BindingModel;
import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.fml.FlexoConceptObject.FlexoConceptObjectImpl;
import org.openflexo.pamela.annotations.*;

import java.util.logging.Logger;

@ModelEntity
@ImplementationClass(RolePropertyValue.RolePropertyValueImpl.class)
@Deprecated
public interface RolePropertyValue<T extends FlexoRole<?>> {

    @PropertyIdentifier(type = FlexoRole.class)
    public static final String OWNER_KEY = "owner";

    @PropertyIdentifier(type = String.class)
    public static final String PARAMETER_NAME_KEY = "parameterName";
    @PropertyIdentifier(type = DataBinding.class)
    public static final String VALUE_KEY = "value";

    @Getter(value = PARAMETER_NAME_KEY)
    @XMLAttribute
    public String getParameterName();

    @Setter(PARAMETER_NAME_KEY)
    public void setParameterName(String paramName);

    @Getter(value = VALUE_KEY)
    @XMLAttribute
    public DataBinding<Object> getValue();

    @Setter(VALUE_KEY)
    public void setValue(DataBinding<Object> value);

    @Getter(value = OWNER_KEY)
    public T getOwner();

    @Setter(OWNER_KEY)
    public void setOwner(T owner);

    public static abstract class RolePropertyValueImpl<T extends FlexoRole<?>> extends FlexoConceptObjectImpl
            implements RolePropertyValue<T> {

        static final Logger logger = Logger.getLogger(RolePropertyValue.class.getPackage().getName());

        private DataBinding<Object> value;

        @Override
        public FlexoConcept getFlexoConcept() {
            if (getOwner() != null) {
                return getOwner().getFlexoConcept();
            }
            return null;
        }

        @Override
        public DataBinding<Object> getValue() {
            if (value == null) {
                value = new DataBinding<>(this, Object.class, DataBinding.BindingDefinitionType.GET);
                value.setBindingName(getParameterName());
            }
            return value;
        }

        @Override
        public void setValue(DataBinding<Object> value) {
            if (value != null) {
                value.setOwner(this);
                value.setBindingName(getParameterName());
                value.setDeclaredType(Object.class);
                value.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
            }
            this.value = value;
        }

        @Override
        public BindingModel getBindingModel() {
            if (getOwner() != null) {
                return getOwner().getBindingModel();
            }
            return null;
        }

    }

}
