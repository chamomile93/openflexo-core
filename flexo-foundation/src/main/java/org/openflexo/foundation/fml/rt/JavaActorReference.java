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

import org.openflexo.connie.type.TypeUtils;
import org.openflexo.foundation.fml.FMLMigration;
import org.openflexo.foundation.fml.JavaRole;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.pamela.annotations.*;
import org.openflexo.pamela.exceptions.InvalidDataException;

import java.util.logging.Logger;





/**
 * Implements {@link ActorReference} for primitive types as modelling elements.<br>
 *
 * @author sylvain
 *
 * @param <T>
 */
@ModelEntity
@ImplementationClass(JavaActorReference.JavaActorReferenceImpl.class)
@XMLElement
@FMLMigration("TODO: rename to FMLVariableActorRefence")
public interface JavaActorReference<T> extends ActorReference<T> {

    @PropertyIdentifier(type = String.class)
    public static final String VALUE_AS_STRING_KEY = "valueAsString";

    @Getter(value = VALUE_AS_STRING_KEY)
    @XMLAttribute
    public String getValueAsString();

    @Setter(VALUE_AS_STRING_KEY)
    public void setValueAsString(String value);

    @Override
    public JavaRole<T> getFlexoRole();

    public static abstract class JavaActorReferenceImpl<T> extends ActorReferenceImpl<T> implements JavaActorReference<T> {

        private static final Logger logger = FlexoLogger.getLogger(JavaActorReference.class.getPackage().toString());

        private T modellingElement = null;

        @Override
        public JavaRole<T> getFlexoRole() {
            return (JavaRole<T>) super.getFlexoRole();
        }

        @Override
        public void setModellingElement(T object) {
            modellingElement = object;
        }

        @Override
        public T getModellingElement(boolean forceLoading) {
            if (modellingElement == null && getValueAsString() != null && getFactory() != null) {
                try {
                    modellingElement = getFactory().getStringEncoder().fromString(getActorClass(), getValueAsString());
                } catch (InvalidDataException e) {
                    e.printStackTrace();
                }
            }
            return modellingElement;
        }

        @Override
        public String getValueAsString() {
            if (modellingElement != null && getFactory() != null) {
                try {
                    return getFactory().getStringEncoder().toString(modellingElement);
                } catch (InvalidDataException e) {
                    e.printStackTrace();
                }
            }
            return (String) performSuperGetter(VALUE_AS_STRING_KEY);
        }

        @Override
        public Class<? extends T> getActorClass() {

            if (getFlexoRole() == null) {
                return null;
            }

            return (Class<? extends T>) TypeUtils.getBaseClass(getFlexoRole().getType());
        }

        @Override
        public String toString() {
            return "JavaActorReference [" + getRoleName() + "] " + Integer.toHexString(hashCode()) + " encodes " + getModellingElement()
                    + "[type: " + (getFlexoRole() != null ? getFlexoRole().getType() : "null") + "]";
        }

    }
}
