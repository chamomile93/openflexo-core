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

package org.openflexo.foundation.fml.ta;

import org.openflexo.foundation.fml.FMLCompilationUnit;
import org.openflexo.foundation.fml.rm.CompilationUnitResource;
import org.openflexo.foundation.fml.rt.AbstractFMLRTModelSlot;
import org.openflexo.foundation.fml.rt.ResourceBasedModelSlotInstance;
import org.openflexo.pamela.annotations.*;
import org.openflexo.toolbox.StringUtils;

import java.util.logging.Logger;

/**
 *
 * Concretize the binding of a {@link FMLModelSlot} to a concrete {@link FMLCompilationUnit}
 *
 * @author Sylvain Guerin
 *
 * @see AbstractFMLRTModelSlot
 *
 */
@ModelEntity
@ImplementationClass(FMLModelSlotInstance.FMLModelSlotInstanceImpl.class)
@XMLElement
public interface FMLModelSlotInstance extends ResourceBasedModelSlotInstance<FMLModelSlot, CompilationUnitResource, FMLCompilationUnit> {

    @PropertyIdentifier(type = String.class)
    public static final String VIRTUAL_MODEL_URI_KEY = "virtualModelURI";

    @Getter(value = VIRTUAL_MODEL_URI_KEY)
    @XMLAttribute
    public String getVirtualModelURI();

    @Setter(VIRTUAL_MODEL_URI_KEY)
    public void setVirtualModelURI(String virtualModelInstanceURI);

    public static abstract class FMLModelSlotInstanceImpl extends
            ResourceBasedModelSlotInstanceImpl<FMLModelSlot, CompilationUnitResource, FMLCompilationUnit> implements FMLModelSlotInstance {

        private static final Logger logger = Logger.getLogger(FMLModelSlotInstance.class.getPackage().getName());

        // Serialization/deserialization only, do not use
        private String virtualModelURI;

        @Override
        protected boolean isResourceRetrievable() {
            return StringUtils.isNotEmpty(virtualModelURI) && getServiceManager() != null
                    && getServiceManager().getResourceManager() != null;
        }

        @Override
        protected CompilationUnitResource retrieveResource() {
            CompilationUnitResource returned = (CompilationUnitResource) getServiceManager().getResourceManager()
                    .getResource(virtualModelURI);
            if (returned == null) {
                logger.warning("Cannot find virtual model " + virtualModelURI);
				/*for (FlexoResourceCenter<?> rc : getServiceManager().getResourceCenterService().getResourceCenters()) {
				System.out.println("--------------- RC: " + rc);
				for (FlexoResource<?> resource : rc.getAllResources()) {
				System.out.println(" > " + resource.getURI());
				}
				}*/
            }
            return returned;
        }

        // Serialization/deserialization only, do not use
        @Override
        public String getVirtualModelURI() {
            if (getResource() != null) {
                return getResource().getURI();
            }
            return virtualModelURI;
        }

        // Serialization/deserialization only, do not use
        @Override
        public void setVirtualModelURI(String virtualModelInstanceURI) {
            this.virtualModelURI = virtualModelInstanceURI;
        }

        @Override
        public String getBindingDescription() {
            return "Bound to " + getVirtualModelURI();
        }

    }
}
