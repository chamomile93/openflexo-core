/**
 *
 * Copyright (c) 2014, Openflexo
 * <p>
 * This file is part of Flexodiagram, a component of the software infrastructure
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

package org.openflexo.foundation.doc.nature;

import org.openflexo.foundation.doc.fml.FlexoDocumentModelSlot;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModelNature;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Define the "controlled-document" nature of a {@link VirtualModel} as a container of one or more {@link VirtualModel} that have the
 * {@link FMLControlledDocumentVirtualModelNature}<br>
 *
 * @author sylvain
 *
 */
public abstract class FMLControlledDocumentContainerNature<MS extends FlexoDocumentModelSlot<?, ?, ?>> implements VirtualModelNature {

    // Prevent external instantiation
    protected FMLControlledDocumentContainerNature() {
    }

    /**
     * Return boolean indicating if supplied {@link FMLRTVirtualModelInstance} might be interpreted as a FML-Controlled document
     */
    public boolean hasNature(VirtualModel container, FMLControlledDocumentVirtualModelNature<MS> vmNature) {
        for (VirtualModel vm : container.getVirtualModels(true)) {
            if (vm.hasNature(vmNature)) {
                return true;
            }
        }
        return false;
    }

    protected List<VirtualModel> _getControlledDocumentVirtualModels(VirtualModel container,
                                                                     FMLControlledDocumentVirtualModelNature<MS> vmNature) {
        List<VirtualModel> returned = new ArrayList<>();
        for (VirtualModel vm : container.getVirtualModels(true)) {
            if (vm.hasNature(vmNature)) {
                returned.add(vm);
            }
        }
        return returned;
    }

}
