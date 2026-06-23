/**
 *
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
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

package org.openflexo.foundation.action;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;

import java.util.Vector;
import java.util.logging.Logger;

/**
 * @author gpolet
 *
 */
public class SetPropertyAction extends FlexoAction<SetPropertyAction, FlexoObject, FlexoObject> {

    public static final SetPropertyActionType actionType = new SetPropertyActionType("set_property");
    private static final Logger logger = Logger.getLogger(SetPropertyAction.class.getPackage().getName());

    static {
        FlexoObjectImpl.addActionForClass(actionType, FlexoObject.class);
    }

    private String key;

    private Object value;

    private Object previousValue;

    private String localizedPropertyName;

    private boolean performValidate = true;

    /**
     * @param actionType
     * @param focusedObject
     * @param globalSelection
     * @param editor
     */
    protected SetPropertyAction(FlexoObject focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
        super(actionType, focusedObject, globalSelection, editor);
    }

    public static SetPropertyAction makeAction(FlexoObject object, String key, Object value, FlexoEditor editor) {
        SetPropertyAction returned = actionType.makeNewAction(object, null, editor);
        returned.setKey(key);
        returned.setValue(value);
        return returned;
    }

    /**
     * Overrides doAction
     *
     * @see org.openflexo.foundation.action.FlexoAction#doAction(java.lang.Object)
     */
    @Override
    protected void doAction(Object context) throws FlexoException {
        previousValue = getFocusedObject().objectForKey(getKey());
		/*if (getFocusedObject() instanceof PortRegistery && getKey().equals("isVisible")) {
			if (previousValue != null && !previousValue.equals(getValue())) {
				OpenPortRegistery.actionType.makeNewEmbeddedAction(((PortRegistery) getFocusedObject()).getProcess(), null, this)
						.doAction();
			}
			return;
		}*/
        try {
            getFocusedObject().setObjectForKey(getValue(), getKey());
        } catch (Exception exception) {
            if (exception.getCause() instanceof FlexoException) {
                throw (FlexoException) exception.getCause();
            }
            logger.warning("Unexpected exception: see log for details");
            exception.printStackTrace();
            if (exception.getCause() instanceof Exception) {
                throw new UnexpectedException((Exception) exception.getCause());
            }
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getLocalizedPropertyName() {
        return localizedPropertyName;
    }

    public void setLocalizedPropertyName(String localizedPropertyName) {
        this.localizedPropertyName = localizedPropertyName;
    }

    public Object getPreviousValue() {
        return previousValue;
    }

    public boolean getPerformValidate() {
        return performValidate;
    }

    public void setPerformValidate(boolean b) {
        this.performValidate = b;
    }

    public static class SetPropertyActionType extends FlexoActionFactory<SetPropertyAction, FlexoObject, FlexoObject> {

        protected SetPropertyActionType(String actionName) {
            super(actionName);
        }

        @Override
        public boolean isEnabledForSelection(FlexoObject object, Vector<FlexoObject> globalSelection) {
            return true;
        }

        @Override
        public boolean isVisibleForSelection(FlexoObject object, Vector<FlexoObject> globalSelection) {
            return false;
        }

        @Override
        public SetPropertyAction makeNewAction(FlexoObject focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
            return new SetPropertyAction(focusedObject, globalSelection, editor);
        }

        /**
         * Overrides getPersistentProperties
         *
         * @see org.openflexo.foundation.action.FlexoActionFactory#getPersistentProperties()
         */
        @Override
        protected String[] getPersistentProperties() {
            return new String[]{"key", "value"};
        }

    }

}
