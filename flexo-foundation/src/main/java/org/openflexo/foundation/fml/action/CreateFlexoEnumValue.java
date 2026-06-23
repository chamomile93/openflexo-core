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

package org.openflexo.foundation.fml.action;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.InvalidNameException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.action.TechnologySpecificFlexoAction;
import org.openflexo.foundation.fml.*;
import org.openflexo.toolbox.StringUtils;

import java.util.Vector;
import java.util.logging.Logger;

public class CreateFlexoEnumValue extends FlexoAction<CreateFlexoEnumValue, FlexoEnum, FMLObject>
        implements TechnologySpecificFlexoAction<FMLTechnologyAdapter> {

    private static final Logger logger = Logger.getLogger(CreateFlexoEnumValue.class.getPackage().getName());

    public static FlexoActionFactory<CreateFlexoEnumValue, FlexoEnum, FMLObject> actionType = new FlexoActionFactory<CreateFlexoEnumValue, FlexoEnum, FMLObject>(
            "create_enum_value", FlexoActionFactory.newMenu, FlexoActionFactory.defaultGroup, FlexoActionFactory.ADD_ACTION_TYPE) {

        /**
         * Factory method
         */
        @Override
        public CreateFlexoEnumValue makeNewAction(FlexoEnum focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
            return new CreateFlexoEnumValue(focusedObject, globalSelection, editor);
        }

        @Override
        public boolean isVisibleForSelection(FlexoEnum object, Vector<FMLObject> globalSelection) {
            return object != null;
        }

        @Override
        public boolean isEnabledForSelection(FlexoEnum object, Vector<FMLObject> globalSelection) {
            return object != null;
        }

    };

    static {
        FlexoObjectImpl.addActionForClass(CreateFlexoEnumValue.actionType, FlexoEnum.class);
    }

    private String valueName;
    private String description;

    private FlexoEnumValue newValue;

    private CreateFlexoEnumValue(FlexoEnum focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
        super(actionType, focusedObject, globalSelection, editor);

    }

    @Override
    public Class<? extends FMLTechnologyAdapter> getTechnologyAdapterClass() {
        return FMLTechnologyAdapter.class;
    }

    public FlexoEnum getFlexoEnum() {
        if (getFocusedObject() != null) {
            return getFocusedObject();
        }
        return null;
    }

    public String getValueName() {
        if (StringUtils.isEmpty(valueName)) {
            return "value";
        }
        return valueName;
    }

    public void setValueName(String parameterName) {
        if ((parameterName == null && this.valueName != null) || (parameterName != null && !parameterName.equals(this.valueName))) {
            String oldValue = this.valueName;
            this.valueName = parameterName;
            getPropertyChangeSupport().firePropertyChange("valueName", oldValue, parameterName);
        }
    }

    @Override
    protected void doAction(Object context) throws NotImplementedException, InvalidNameException {
        logger.info("Add FlexoEnumValue, name=" + getValueName());

        FMLModelFactory factory = getFocusedObject().getFMLModelFactory();
        newValue = factory.newFlexoEnumValue(getFocusedObject());
        newValue.setName(getValueName());
        newValue.setDescription(getDescription());
    }

    public FlexoEnumValue getNewValue() {
        return newValue;
    }

    @Override
    public boolean isValid() {
        if (StringUtils.isEmpty(getValueName())) {
            return false;
        } else if (getFlexoEnum().getValue(getValueName()) != null) {
            return false;
        }
        return true;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if ((description == null && this.description != null) || (description != null && !description.equals(this.description))) {
            String oldValue = this.description;
            this.description = description;
            getPropertyChangeSupport().firePropertyChange("description", oldValue, description);
        }
    }

}
