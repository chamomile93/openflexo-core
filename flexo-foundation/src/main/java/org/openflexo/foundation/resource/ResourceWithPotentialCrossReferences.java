/**
 *
 * Copyright (c) 2013-2015, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
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

import org.openflexo.foundation.FlexoException;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;

import java.io.FileNotFoundException;

/**
 * A FlexoResource which may contain cross-reference dependencies
 *
 * @param <RD>
 *            the type of the resource data reference by this resource
 * @author Sylvain
 *
 */
@ModelEntity(isAbstract = true)
@ImplementationClass(FlexoResourceImpl.class)
public interface ResourceWithPotentialCrossReferences<RD extends ResourceData<RD>> extends FlexoResource<RD> {

    /**
     * Load resource data of this resource as a first pass, {@link #finalizeLoadResourceData()} should be called once all dependencies which
     * are {@link ResourceWithPotentialCrossReferences} are loaded for the first pass
     *
     * @return the resource data.
     * @throws ResourceLoadingCancelledException
     * @throws ResourceDependencyLoopException
     * @throws FileNotFoundException
     * @throws FlexoException
     */
    public RD initializeLoadResourceData() throws ResourceLoadingCancelledException, FileNotFoundException, FlexoException;

    /**
     * Finalize load resource data of this resource as a second pass
     *
     * @throws ResourceLoadingCancelledException
     * @throws FileNotFoundException
     * @throws FlexoException
     */
    public void finalizeLoadResourceData() throws ResourceLoadingCancelledException, FileNotFoundException, FlexoException;

}
