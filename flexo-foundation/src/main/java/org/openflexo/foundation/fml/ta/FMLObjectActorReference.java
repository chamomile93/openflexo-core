/**
 *
 * Copyright (c) 2014-2015, Openflexo
 * <p>
 * This file is part of Excelconnector, a component of the software infrastructure
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

package org.openflexo.foundation.fml.ta;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.fml.*;
import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.pamela.annotations.*;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

/**
 * Implements {@link ActorReference} for {@link ExcelObject} as modelling elements.<br>
 *
 * @author sylvain
 *
 * @param <T>
 *            type of referenced object
 */
@ModelEntity
@ImplementationClass(FMLObjectActorReference.FMLObjectActorReferenceImpl.class)
@XMLElement
public interface FMLObjectActorReference<T extends FMLObject> extends ActorReference<T> {

    @PropertyIdentifier(type = String.class)
    public static final String OBJECT_URI_KEY = "objectURI";
    @PropertyIdentifier(type = String.class)
    public static final String VIRTUAL_MODEL_URI_KEY = "virtualModelURI";

    @Getter(value = VIRTUAL_MODEL_URI_KEY)
    @XMLAttribute
    public String getVirtualModelURI();

    @Setter(VIRTUAL_MODEL_URI_KEY)
    public void setVirtualModelURI(String virtualModelInstanceURI);

    @Getter(value = OBJECT_URI_KEY)
    @XMLAttribute
    public String getObjectURI();

    @Setter(OBJECT_URI_KEY)
    public void setObjectURI(String objectURI);

    public abstract static class FMLObjectActorReferenceImpl<T extends FMLObject> extends ActorReferenceImpl<T>
            implements FMLObjectActorReference<T> {

        private static final Logger logger = FlexoLogger.getLogger(FMLObjectActorReference.class.getPackage().toString());

        private T object;
        private String objectURI;

        // Serialization/deserialization only, do not use
        private String virtualModelURI;

        // Serialization/deserialization only, do not use
        @Override
        public String getVirtualModelURI() {
            if (object != null && object.getDeclaringCompilationUnit() != null) {
                return object.getDeclaringCompilationUnit().getVirtualModel().getURI();
            }
            return virtualModelURI;
        }

        // Serialization/deserialization only, do not use
        @Override
        public void setVirtualModelURI(String virtualModelInstanceURI) {
            this.virtualModelURI = virtualModelInstanceURI;
        }

        @Override
        public String getObjectURI() {
            if (object != null) {
                if (object instanceof FlexoConcept) {
                    return ((FlexoConcept) object).getURI();
                }
                if (object instanceof FlexoProperty) {
                    return ((FlexoProperty<?>) object).getURI();
                }
                if (object instanceof FlexoBehaviour) {
                    return ((FlexoBehaviour) object).getURI();
                }
            }
            return objectURI;
        }

        @Override
        public void setObjectURI(String objectURI) {
            this.objectURI = objectURI;
        }

        @Override
        public T getModellingElement(boolean forceLoading) {
            if (object == null && virtualModelURI != null && objectURI != null) {
                // First find the VirtualModel
                if (getFlexoConceptInstance() == null) {
                    return null;
                }
                FlexoServiceManager sm = getFlexoConceptInstance().getServiceManager();
                if (sm == null || sm.getVirtualModelLibrary() == null) {
                    return null;
                }
                VirtualModel virtualModel = null;
                try {
                    virtualModel = sm.getVirtualModelLibrary().getVirtualModel(virtualModelURI);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (ResourceLoadingCancelledException e) {
                    e.printStackTrace();
                } catch (FlexoException e) {
                    e.printStackTrace();
                }
                if (virtualModel != null) {
                    object = (T) virtualModel.getCompilationUnit().getObject(objectURI);
                }
            }
            if (object == null) {
                logger.warning("Could not retrieve object " + objectURI);
            }
            return object;

        }

        @Override
        public void setModellingElement(T object) {
            this.object = object;
            if (object != null) {
                virtualModelURI = object.getDeclaringCompilationUnit().getVirtualModel().getURI();
                objectURI = getObjectURI();
            }
        }

    }

}
