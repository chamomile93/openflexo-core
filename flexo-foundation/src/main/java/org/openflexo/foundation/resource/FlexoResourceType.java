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

package org.openflexo.foundation.resource;

import org.openflexo.connie.type.CustomTypeFactory;
import org.openflexo.connie.type.JavaCustomType;
import org.openflexo.connie.type.ParameterizedTypeImpl;
import org.openflexo.connie.type.TypeUtils;
import org.openflexo.foundation.fml.AbstractFMLTypingSpace;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.TechnologyAdapterTypeFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterService;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.toolbox.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represent the type of a FlexoResource of a given type
 *
 * @author sylvain
 *
 */
public class FlexoResourceType implements JavaCustomType {
    //TODO should I use any of this defined concept in relation with the ISubject and IAuthenticator ?

    protected static final Logger logger = FlexoLogger.getLogger(FlexoResourceType.class.getPackage().getName());
    public static FlexoResourceType UNDEFINED_RESOURCE_TYPE = new FlexoResourceType((TechnologySpecificFlexoResourceFactory<?, ?, ?>) null);
    protected ITechnologySpecificFlexoResourceFactory<?, ?, ?> resourceFactory;
    protected String resourceDataClassName;
    // factory stored for unresolved types
    // should be nullified as quickly as possible (nullified when resolved)
    protected CustomTypeFactory<?> customTypeFactory;
    private ParameterizedType genericJavaType;

    public FlexoResourceType(ITechnologySpecificFlexoResourceFactory<?, ?, ?> resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    protected FlexoResourceType(String resourceDataClassName) {
        this.resourceDataClassName = resourceDataClassName;
    }

    private static <R extends TechnologyAdapterResource<RD, TA>, RD extends ResourceData<RD> & TechnologyObject<TA>, TA extends TechnologyAdapter<TA>> ITechnologySpecificFlexoResourceFactory<R, RD, TA> getFlexoResourceFactoryForClass(
            Class<? extends RD> resourceOrResourceDataClass, TechnologyAdapterService taService) {
        for (TechnologyAdapter<?> ta : taService.getTechnologyAdapters()) {
            List<ITechnologySpecificFlexoResourceFactory<?, ?, ?>> resourceFactories = ta.getResourceFactories();
            for (ITechnologySpecificFlexoResourceFactory<?, ?, ?> f : resourceFactories) {
                if (f.getResourceClass().equals(resourceOrResourceDataClass)) {
                    return (ITechnologySpecificFlexoResourceFactory<R, RD, TA>) f;
                }
                if (f.getResourceDataClass().equals(resourceOrResourceDataClass)) {
                    return (ITechnologySpecificFlexoResourceFactory<R, RD, TA>) f;
                }
            }
        }
        return null;

    }

	/*@Override
	public FMLTechnologyAdapter getSpecificTechnologyAdapter() {
		if (resourceFactory != null) {
			return resourceFactory.getTechnologyAdapter();
		}
		return null;
	}*/

    public static <R extends TechnologyAdapterResource<RD, TA>, RD extends ResourceData<RD> & TechnologyObject<TA>, TA extends TechnologyAdapter<TA>> FlexoResourceType getFlexoResourceType(
            Class<RD> resourceDataClass, TechnologyAdapterService taService) {
        ITechnologySpecificFlexoResourceFactory<R, RD, TA> resourceFactory = getFlexoResourceFactoryForClass(resourceDataClass, taService);
        return getFlexoResourceType(resourceFactory);
    }

    public static <R extends TechnologyAdapterResource<RD, TA>, RD extends ResourceData<RD> & TechnologyObject<TA>, TA extends TechnologyAdapter<TA>> FlexoResourceType getFlexoResourceType(
            ITechnologySpecificFlexoResourceFactory<R, RD, TA> resourceFactory) {
        if (resourceFactory != null)
            return resourceFactory.getResourceType();
        return UNDEFINED_RESOURCE_TYPE;
    }

    public ITechnologySpecificFlexoResourceFactory<?, ?, ?> getResourceFactory() {
        if (!isResolved() && customTypeFactory != null) {
            resolve(customTypeFactory);
        }
        return resourceFactory;
    }

    @Override
    public Class<?> getBaseClass() {
        if (resourceFactory != null) {
            return resourceFactory.getResourceClass();
        }
        return FlexoResource.class;
    }

    @Override
    public Class<?> getJavaType() {
        return getBaseClass();
    }

    public ParameterizedType getGenericJavaType() {
        if (genericJavaType == null) {
            genericJavaType = new ParameterizedTypeImpl(FlexoResource.class, getResourceDataClass());
        }
        return genericJavaType;
    }

    public Type getResourceDataClass() {
        if (FlexoResource.class.isAssignableFrom(getBaseClass())) {
            Type returned = TypeUtils.getTypeArgument(getBaseClass(), FlexoResource.class, 0);
            return returned;
        }
        return ResourceData.class;
    }

    @Override
    public boolean isTypeAssignableFrom(Type aType, boolean permissive) {
        return TypeUtils.isTypeAssignableFrom(getGenericJavaType(), aType, permissive);
    }

    @Override
    public boolean isOfType(Object object, boolean permissive) {
        return (object instanceof FlexoResource && getBaseClass().isAssignableFrom(object.getClass()));
    }

    @Override
    public String simpleRepresentation() {
        return AbstractFMLTypingSpace.RESOURCE + "<" + TypeUtils.simpleRepresentation(getResourceDataClass()) + ">";
    }

    @Override
    public String fullQualifiedRepresentation() {
        return AbstractFMLTypingSpace.RESOURCE + "<" + TypeUtils.fullQualifiedRepresentation(getResourceDataClass()) + ">";
    }

    @Override
    public String toString() {
        return simpleRepresentation();
    }

    @Override
    public String getSerializationRepresentation() {
        return (resourceFactory != null ? resourceFactory.getResourceDataClass().getName() : resourceDataClassName);
    }

    @Override
    public boolean isResolved() {
        return resourceFactory != null || StringUtils.isEmpty(resourceDataClassName);
    }

    @Override
    public void resolve() {
        if (customTypeFactory != null) {
            resolve(customTypeFactory);
        }
    }

    private void resolve(CustomTypeFactory<?> factory) {
        if (factory instanceof FlexoResourceTypeFactory) {

            try {
                Class resourceClass = Class.forName(resourceDataClassName);
                resourceFactory = getFlexoResourceFactoryForClass(resourceClass,
                        ((FlexoResourceTypeFactory) factory).getTechnologyAdapter().getTechnologyAdapterService());
                if (resourceFactory != null) {
                    this.customTypeFactory = null;
                } else {
                    this.customTypeFactory = factory;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((resourceFactory == null) ? 0 : resourceFactory.hashCode());
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
        FlexoResourceType other = (FlexoResourceType) obj;
        if (resourceFactory == null) {
            if (other.resourceFactory != null)
                return false;
        } else if (!resourceFactory.equals(other.resourceFactory)) {
            return false;
        }
        return true;
    }

    /**
     * Factory for FlexoResourceType instances
     *
     * @author sylvain
     *
     */
    public static class FlexoResourceTypeFactory extends TechnologyAdapterTypeFactory<FlexoResourceType, FMLTechnologyAdapter> {

        private ITechnologySpecificFlexoResourceFactory<?, ?, ?> resourceFactory;

        public FlexoResourceTypeFactory(FMLTechnologyAdapter technologyAdapter) {
            super(technologyAdapter);
        }

        @Override
        public Class<FlexoResourceType> getCustomType() {
            return FlexoResourceType.class;
        }

        @Override
        public FlexoResourceType makeCustomType(String configuration) {

            ITechnologySpecificFlexoResourceFactory<?, ?, ?> resourceFactory = null;

            if (configuration != null) {
                try {
                    Class resourceClass = Class.forName(configuration);
                    resourceFactory = getFlexoResourceFactoryForClass(resourceClass, getTechnologyAdapter().getTechnologyAdapterService());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                resourceFactory = getResourceFactory();
            }

            if (resourceFactory != null) {
                return resourceFactory.getResourceType();
            }
            // We don't return UNDEFINED_FLEXO_CONCEPT_INSTANCE_TYPE because we want here a mutable type
            // if FlexoConcept might be resolved later
            return new FlexoResourceType(configuration);
        }

        public ITechnologySpecificFlexoResourceFactory<?, ?, ?> getResourceFactory() {
            return resourceFactory;
        }

        public void setResourceFactory(ITechnologySpecificFlexoResourceFactory<?, ?, ?> resourceFactory) {
            if ((resourceFactory == null && this.resourceFactory != null)
                    || (resourceFactory != null && !resourceFactory.equals(this.resourceFactory))) {
                ITechnologySpecificFlexoResourceFactory<?, ?, ?> oldValue = this.resourceFactory;
                this.resourceFactory = resourceFactory;
                getPropertyChangeSupport().firePropertyChange("resourceFactory", oldValue, resourceFactory);
            }
        }

        @Override
        public String toString() {
            return "FlexoResourceTypeFactory";
        }

        @Override
        public void configureFactory(FlexoResourceType type) {
            if (type != null) {
                setResourceFactory(type.getResourceFactory());
            }
        }
    }
}
