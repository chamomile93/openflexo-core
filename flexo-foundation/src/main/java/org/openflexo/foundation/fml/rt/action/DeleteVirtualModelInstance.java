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

package org.openflexo.foundation.fml.rt.action;

import org.openflexo.connie.type.TypeUtils;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.TechnologySpecificFlexoAction;
import org.openflexo.foundation.fml.rt.FMLRTTechnologyAdapter;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;

import java.util.Vector;
import java.util.logging.Logger;

public class DeleteVirtualModelInstance extends FlexoAction<DeleteVirtualModelInstance, FMLRTVirtualModelInstance, FlexoObject>
        implements TechnologySpecificFlexoAction<FMLRTTechnologyAdapter> {

    private static final Logger logger = Logger.getLogger(DeleteVirtualModelInstance.class.getPackage().getName());

    public static FlexoActionFactory<DeleteVirtualModelInstance, FMLRTVirtualModelInstance, FlexoObject> actionType = new FlexoActionFactory<DeleteVirtualModelInstance, FMLRTVirtualModelInstance, FlexoObject>(
            "delete_virtual_model_instance", FlexoActionFactory.editGroup, FlexoActionFactory.DELETE_ACTION_TYPE) {

        /**
         * Factory method
         */
        @Override
        public DeleteVirtualModelInstance makeNewAction(FMLRTVirtualModelInstance focusedObject, Vector<FlexoObject> globalSelection,
                                                        FlexoEditor editor) {
            return new DeleteVirtualModelInstance(focusedObject, globalSelection, editor);
        }

        @Override
        public boolean isVisibleForSelection(FMLRTVirtualModelInstance vmi, Vector<FlexoObject> globalSelection) {
            return vmi != null && TypeUtils.isAssignableTo(vmi, FMLRTVirtualModelInstance.class);
        }

        @Override
        public boolean isEnabledForSelection(FMLRTVirtualModelInstance vmi, Vector<FlexoObject> globalSelection) {
            return isVisibleForSelection(vmi, globalSelection);
        }

    };

    static {
        FlexoObjectImpl.addActionForClass(DeleteVirtualModelInstance.actionType, FMLRTVirtualModelInstance.class);
    }

    private DeleteVirtualModelInstance(FMLRTVirtualModelInstance focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
        super(actionType, focusedObject, globalSelection, editor);
    }

    @Override
    public Class<? extends FMLRTTechnologyAdapter> getTechnologyAdapterClass() {
        return FMLRTTechnologyAdapter.class;
    }

    @Override
    protected void doAction(Object context) {
        logger.info("Delete virtual model instance");

        if (getFocusedObject().getResource() != null) {
            getFocusedObject().getResource().delete();
        }
    }
}
