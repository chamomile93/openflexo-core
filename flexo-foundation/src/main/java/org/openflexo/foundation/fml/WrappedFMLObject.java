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
import org.openflexo.foundation.fml.FMLModelContext.FMLEntity;
import org.openflexo.foundation.fml.FMLModelContext.FMLProperty;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.pamela.annotations.*;

import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(WrappedFMLObject.WrappedFMLObjectImpl.class)
public interface WrappedFMLObject<O extends FMLObject> extends FMLPrettyPrintable {

    @PropertyIdentifier(type = FMLObject.class)
    public static final String OBJECT_KEY = "object";

    @Getter(value = OBJECT_KEY, ignoreType = true)
    public O getObject();

    @Setter(OBJECT_KEY)
    public void setObject(O anObject);

    public static abstract class WrappedFMLObjectImpl<O extends FMLObject> extends FMLObjectImpl implements WrappedFMLObject<O> {

        protected static final Logger logger = FlexoLogger.getLogger(WrappedFMLObject.class.getPackage().getName());

        private O object;

        @Override
        public O getObject() {
            return object;
        }

        @Override
        public void setObject(O object) {
            if ((object == null && this.object != null) || (object != null && !object.equals(this.object))) {
                O oldValue = this.object;
                this.object = object;
                getPropertyChangeSupport().firePropertyChange("object", oldValue, object);
            }
        }

        @Override
        public FMLCompilationUnit getResourceData() {
            if (getObject() != null) {
                return getObject().getResourceData();
            }
            return null;
        }

        @Override
        public BindingModel getBindingModel() {
            if (getObject() != null) {
                return getObject().getBindingModel();
            }
            return null;
        }

        @Override
        protected FMLEntity<?> getFMLEntity(FMLModelFactory modelFactory) {
            if (getObject() != null) {
                return FMLModelContext.getFMLEntity((Class) getObject().getImplementedInterface(modelFactory), modelFactory);
            }
            return null;
        }

        @Override
        public final List<FMLPropertyValue<?, ?>> getFMLPropertyValues() {
            if (getObject() != null) {
                return getObject().getFMLPropertyValues();
            }
            return null;
        }

        @Override
        public List<FMLPropertyValue<?, ?>> getFMLPropertyValues(FMLModelFactory modelFactory) {
            if (getObject() != null) {
                return getObject().getFMLPropertyValues(modelFactory);
            }
            return null;
        }

        @Override
        public FMLProperty getFMLProperty(String propertyName, FMLModelFactory modelFactory) {
            if (getObject() != null) {
                return getObject().getFMLProperty(propertyName, modelFactory);
            }
            return null;
        }

        @Override
        public String toString() {
            return "WrappedFMLObjectImpl@" + Integer.toHexString(hashCode()) + "[" + getObject() + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((object == null) ? 0 : object.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            WrappedFMLObjectImpl other = (WrappedFMLObjectImpl) obj;
            if (object == null) {
                if (other.object != null)
                    return false;
            } else if (!object.equals(other.object))
                return false;
            return true;
        }

    }
}
