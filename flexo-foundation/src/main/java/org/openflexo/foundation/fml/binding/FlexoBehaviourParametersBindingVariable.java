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

package org.openflexo.foundation.fml.binding;

import org.openflexo.connie.BindingVariable;
import org.openflexo.connie.binding.javareflect.JavaPropertyPathElement;
import org.openflexo.connie.expr.BindingPath;
import org.openflexo.foundation.fml.FlexoBehaviour;
import org.openflexo.foundation.fml.FlexoBehaviourParametersValuesType;

import java.lang.reflect.Type;
import java.util.logging.Logger;

public class FlexoBehaviourParametersBindingVariable extends BindingVariable {
    static final Logger logger = Logger.getLogger(FlexoBehaviourParametersBindingVariable.class.getPackage().getName());

    private FlexoBehaviour flexoBehaviour;

    public FlexoBehaviourParametersBindingVariable(FlexoBehaviour flexoBehaviour) {
        super(FlexoBehaviourBindingModel.PARAMETERS_PROPERTY, flexoBehaviour.getFlexoBehaviourParametersValuesType(), true);
    }

    @Override
    public String getVariableName() {
        return FlexoBehaviourBindingModel.PARAMETERS_PROPERTY;
    }

    public FlexoBehaviour getFlexoBehaviour() {
        return flexoBehaviour;
    }

    @Override
    public Type getType() {
        if (flexoBehaviour != null) {
            return flexoBehaviour.getFlexoBehaviourParametersValuesType();
        }
        return super.getType();
    }

    @Override
    public String getTooltipText(Type resultingType) {
        return "Parameters" + (flexoBehaviour != null ? " for behaviour " + flexoBehaviour.getName() : "");
    }

    @Deprecated
    // Caused by parameters management: change this !!!
    @Override
    public void hasBeenResolved(BindingPath bindingPath) {
        if (bindingPath.getBindingPathElementAtIndex(0) instanceof JavaPropertyPathElement
                && getType() instanceof FlexoBehaviourParametersValuesType) {
            FlexoBehaviour b = ((FlexoBehaviourParametersValuesType) getType()).getFlexoBehaviour();
            FlexoBehaviourParameterValuePathElement newPathElement = new FlexoBehaviourParameterValuePathElement(this,
                    b.getParameter(((JavaPropertyPathElement) bindingPath.getBindingPathElementAtIndex(0)).getLabel()),
                    bindingPath.getOwner());
            bindingPath.replaceBindingPathElementAtIndex(newPathElement, 0);
        }
    }

}
