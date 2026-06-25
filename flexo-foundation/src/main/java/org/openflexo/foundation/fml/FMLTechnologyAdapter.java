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

package org.openflexo.foundation.fml;

import org.openflexo.connie.annotations.NotificationUnsafe;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.fml.FlexoConceptInstanceType.DefaultFlexoConceptInstanceTypeFactory;
import org.openflexo.foundation.fml.VirtualModelInstanceType.DefaultVirtualModelInstanceTypeFactory;
import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareResourceFactories;
import org.openflexo.foundation.fml.annotations.DeclareTechnologySpecificTypes;
import org.openflexo.foundation.fml.rm.CompilationUnitResource;
import org.openflexo.foundation.fml.rm.CompilationUnitResourceFactory;
import org.openflexo.foundation.fml.ta.FMLModelSlot;
import org.openflexo.foundation.fml.ta.FMLTechnologyContextManager;
import org.openflexo.foundation.fml.ta.FlexoConceptType;
import org.openflexo.foundation.fml.ta.FlexoConceptType.DefaultFlexoConceptTypeFactory;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.FlexoResourceType;
import org.openflexo.foundation.resource.FlexoResourceType.FlexoResourceTypeFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterService;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines and implements the FML technology adapter (Flexo Modelling Language)
 *
 * @author sylvain
 *
 */
@DeclareModelSlots({FMLModelSlot.class})
@DeclareResourceFactories({CompilationUnitResourceFactory.class})
@DeclareTechnologySpecificTypes({FlexoConceptType.class})
public class FMLTechnologyAdapter extends TechnologyAdapter<FMLTechnologyAdapter> {
    private FlexoResourceTypeFactory resourceTypeFactory;
    private DefaultFlexoConceptInstanceTypeFactory fciFactory;
    // private FlexoEnumTypeFactory enumFactory;
    private DefaultVirtualModelInstanceTypeFactory vmiFactory;
    private DefaultFlexoConceptTypeFactory fmlTypeFactory;

    public FMLTechnologyAdapter() throws TechnologyAdapterInitializationException {
    }

    @Override
    protected void initResourceFactories() {
        super.initResourceFactories();
        getAvailableResourceTypes().add(CompilationUnitResource.class);
    }

    @Override
    public String getName() {
        return "FML technology adapter";
    }

    @Override
    protected String getLocalizationDirectory() {
        return "FlexoLocalization/FMLTechnologyAdapter";
    }

    @Override
    public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
        // no specific binding factory for this technology
        return null;
    }

    @Override
    public FMLTechnologyContextManager createTechnologyContextManager(final FlexoResourceCenterService service) {
        return new FMLTechnologyContextManager(this, service);
    }

    @Override
    public FMLTechnologyContextManager getTechnologyContextManager() {
        return (FMLTechnologyContextManager) super.getTechnologyContextManager();
    }

    @Override
    public FlexoServiceManager getServiceManager() {
        return this.getTechnologyAdapterService().getServiceManager();
    }

    @Override
    public void initTechnologySpecificTypes(TechnologyAdapterService taService) {
        taService.registerTypeClass(FlexoResourceType.class, getFlexoResourceTypeFactory());
        taService.registerTypeClass(FlexoConceptInstanceType.class, getFlexoConceptInstanceTypeFactory());
        // taService.registerTypeClass(FlexoEnumType.class, getFlexoEnumTypeFactory());
        taService.registerTypeClass(VirtualModelInstanceType.class, getVirtualModelInstanceTypeFactory());
        taService.registerTypeClass(FlexoConceptType.class, getFlexoConceptTypeFactory());
    }

    public FlexoResourceTypeFactory getFlexoResourceTypeFactory() {
        if (resourceTypeFactory == null) {
            resourceTypeFactory = new FlexoResourceTypeFactory(this);
        }
        return resourceTypeFactory;
    }

	/*public FlexoEnumTypeFactory getFlexoEnumTypeFactory() {
		if (enumFactory == null) {
			enumFactory = new FlexoEnumTypeFactory(this);
		}
		return enumFactory;
	}*/

    public DefaultFlexoConceptInstanceTypeFactory getFlexoConceptInstanceTypeFactory() {
        if (fciFactory == null) {
            fciFactory = new DefaultFlexoConceptInstanceTypeFactory(this);
        }
        return fciFactory;
    }

    public DefaultVirtualModelInstanceTypeFactory getVirtualModelInstanceTypeFactory() {
        if (vmiFactory == null) {
            vmiFactory = new DefaultVirtualModelInstanceTypeFactory(this);
        }
        return vmiFactory;
    }

    public DefaultFlexoConceptTypeFactory getFlexoConceptTypeFactory() {
        if (fmlTypeFactory == null) {
            fmlTypeFactory = new DefaultFlexoConceptTypeFactory(this);
        }
        return fmlTypeFactory;
    }

    public VirtualModelLibrary getVirtualModelLibrary() {
        return this.getServiceManager().getVirtualModelLibrary();
    }

    public <I> CompilationUnitResourceRepository<I> getVirtualModelRepository(FlexoResourceCenter<I> resourceCenter) {
        CompilationUnitResourceRepository<I> returned = resourceCenter.retrieveRepository(CompilationUnitResourceRepository.class, this);
        if (returned == null) {
            returned = CompilationUnitResourceRepository.instanciateNewRepository(this, resourceCenter);
            resourceCenter.registerRepository(returned, CompilationUnitResourceRepository.class, this);
        }
        return returned;
    }

    @Override
    public String getIdentifier() {
        return "FML";
    }

    public CompilationUnitResourceFactory getCompilationUnitResourceFactory() {
        return getResourceFactory(CompilationUnitResourceFactory.class);
    }

    @NotificationUnsafe
    public List<CompilationUnitResourceRepository<?>> getVirtualModelRepositories() {
        List<CompilationUnitResourceRepository<?>> returned = new ArrayList<>();
        for (FlexoResourceCenter<?> rc : getServiceManager().getResourceCenterService().getResourceCenters()) {
            if (!rc.isDeleted()) {
                returned.add(getVirtualModelRepository(rc));
            }
        }
        return returned;
    }

    @Override
    public void notifyRepositoryStructureChanged() {
        super.notifyRepositoryStructureChanged();
        getPropertyChangeSupport().firePropertyChange("getVirtualModelRepositories()", null, getVirtualModelRepositories());
    }

    @Override
    public void ensureAllRepositoriesAreCreated(FlexoResourceCenter<?> rc) {
        super.ensureAllRepositoriesAreCreated(rc);
        getVirtualModelRepository(rc);
    }

    @Override
    public <I> boolean isIgnorable(final FlexoResourceCenter<I> resourceCenter, final I contents) {
        if (resourceCenter.isIgnorable(contents, this)) {
            return true;
        }
        // This allows to ignore all contained VirtualModel, that will be explored from their container resource
        if (resourceCenter.isDirectory(contents)) {
            if (FlexoResourceCenter.isContainedInDirectoryWithSuffix(resourceCenter, contents, CompilationUnitResourceFactory.FML_SUFFIX)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected <I> boolean isFolderIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
        if (resourceCenter.isDirectory(contents)) {
            if (FlexoResourceCenter.isContainedInDirectoryWithSuffix(resourceCenter, contents, CompilationUnitResourceFactory.FML_SUFFIX)) {
                return true;
            }
        }
        return false;
    }

}
