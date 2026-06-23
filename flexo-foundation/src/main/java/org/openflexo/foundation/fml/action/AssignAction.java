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

package org.openflexo.foundation.fml.action;

import org.openflexo.connie.Bindable;
import org.openflexo.connie.BindingFactory;
import org.openflexo.connie.BindingModel;
import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.fml.editionaction.*;

import java.util.Vector;
import java.util.logging.Logger;

/**
 * Instantiate a new {@link AssignationAction} while embedding focused {@link AssignableAction} the value beeing assigned
 *
 * @author sylvain
 *
 */
public class AssignAction extends FlexoAction<AssignAction, AssignableAction<?>, FMLObject> implements Bindable {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(AssignAction.class.getPackage().getName());

    public static FlexoActionFactory<AssignAction, AssignableAction<?>, FMLObject> actionType = new FlexoActionFactory<AssignAction, AssignableAction<?>, FMLObject>(
            "assign_to", FlexoActionFactory.defaultGroup, FlexoActionFactory.NORMAL_ACTION_TYPE) {

        /**
         * Factory method
         */
        @Override
        public AssignAction makeNewAction(AssignableAction<?> focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
            return new AssignAction(focusedObject, globalSelection, editor);
        }

        @Override
        public boolean isVisibleForSelection(AssignableAction<?> object, Vector<FMLObject> globalSelection) {
            return true;
        }

        @Override
        public boolean isEnabledForSelection(AssignableAction<?> object, Vector<FMLObject> globalSelection) {
            return object != null && !(object instanceof DeclarationAction) && !(object instanceof AssignationAction)
                    && !(object instanceof AddToListAction) && !(object instanceof ReturnStatement);
        }

    };

    static {
        FlexoObjectImpl.addActionForClass(AssignAction.actionType, AssignableAction.class);
    }

    private AssignationAction<?> assignationAction;
    private DataBinding<?> assignation;
    private boolean isNotifying = false;

    protected AssignAction(AssignableAction<?> focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
        super(actionType, focusedObject, globalSelection, editor);
    }

    @Override
    protected void doAction(Object context) throws FlexoException {
        assignationAction = getFocusedObject().assignTo((DataBinding) getAssignation());
    }

    @Override
    public boolean isValid() {
        if (!getAssignation().isValid()) {
            return false;
        }
        return true;
    }

    public DataBinding<?> getAssignation() {
        if (assignation == null) {
            assignation = new DataBinding<>(this, Object.class, DataBinding.BindingDefinitionType.GET_SET);
            assignation.setBindingName("assignation");
            assignation.setMandatory(true);
        }
        return assignation;
    }

    public AssignationAction<?> getDeclarationAction() {
        return assignationAction;
    }

    @Override
    public BindingModel getBindingModel() {
        return getFocusedObject().getBindingModel();
    }

    @Override
    public BindingFactory getBindingFactory() {
        return getFocusedObject().getBindingFactory();
    }

    @Override
    public void notifiedBindingChanged(DataBinding<?> dataBinding) {
        if (isNotifying) {
            return;
        }
        isNotifying = true;
        getPropertyChangeSupport().firePropertyChange("assignation", null, getAssignation());
        isNotifying = false;
    }

    @Override
    public void notifiedBindingDecoded(DataBinding<?> dataBinding) {
    }

}
