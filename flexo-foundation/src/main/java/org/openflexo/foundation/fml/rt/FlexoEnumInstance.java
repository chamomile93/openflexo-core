/**
 *
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.foundation.fml.rt;

import org.openflexo.foundation.fml.FlexoConceptInstanceRole;
import org.openflexo.foundation.fml.FlexoEnum;
import org.openflexo.foundation.fml.FlexoEnumValue;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.XMLElement;

import java.util.logging.Logger;

/**
 * A {@link FlexoEnumInstance} is the run-time concept (instance) of an {@link FlexoFlexoEnum}.<br>
 *
 * This is the run-time concept of a {@link FlexoEnumValue}
 *
 * Take care that resource or owner {@link VirtualModelInstance} is here null
 *
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(FlexoEnumInstance.FlexoEnumInstanceImpl.class)
@XMLElement
public interface FlexoEnumInstance extends FlexoConceptInstance {

    @Override
    public FlexoEnumValue getFlexoConcept();

    public FlexoEnum getFlexoEnum();

    public FlexoEnumValue getValue();

    public void setValue(FlexoEnumValue value);

    public static abstract class FlexoEnumInstanceImpl extends FlexoConceptInstanceImpl implements FlexoEnumInstance {

        private static final Logger logger = FlexoLogger.getLogger(FlexoEnumInstance.class.getPackage().toString());

        private FlexoEnumValue value;

        @Override
        public FlexoEnum getFlexoEnum() {
            return getFlexoConcept().getFlexoEnum();
        }

        @Override
        public FlexoEnumValue getFlexoConcept() {
            return getValue();
        }

        @Override
        public FlexoEnumValue getValue() {
            if (value == null && getFlexoEnum() != null && getFlexoEnum().getValues().size() > 0) {
                return getFlexoEnum().getValues().get(0);
            }
            return value;
        }

        @Override
        public void setValue(FlexoEnumValue value) {
            if ((value == null && this.value != null) || (value != null && !value.equals(this.value))) {
                FlexoEnumValue oldValue = this.value;
                this.value = value;
                getPropertyChangeSupport().firePropertyChange("value", oldValue, value);
            }
        }

        @Override
        public String toString() {
            return getValue().getName();
        }

        /**
         * Instanciate run-time-level object encoding reference to this {@link FlexoConceptInstance} object
         *
         * @param role
         *            the {@link FlexoConceptInstanceRole} defining access to supplied object
         * @param fci
         *            the {@link FlexoConceptInstance} where this reference should be built
         *
         */
        @Override
        public ActorReference<? extends FlexoConceptInstance> makeActorReference(FlexoConceptInstanceRole role, FlexoConceptInstance fci) {

            AbstractVirtualModelInstanceModelFactory<?> factory = fci.getFactory();
            FlexoEnumValueActorReference returned = factory.newInstance(FlexoEnumValueActorReference.class);
            returned.setFlexoRole(role);
            returned.setFlexoConceptInstance(fci);
            returned.setModellingElement(this);
            return returned;
        }

    }
}
