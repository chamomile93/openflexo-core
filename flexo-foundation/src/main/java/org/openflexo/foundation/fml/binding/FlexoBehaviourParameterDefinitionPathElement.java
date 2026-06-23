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

import org.openflexo.connie.Bindable;
import org.openflexo.connie.BindingEvaluationContext;
import org.openflexo.connie.binding.IBindingPathElement;
import org.openflexo.connie.binding.SimplePathElementImpl;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.FlexoBehaviourParameter;

import java.lang.reflect.Type;
import java.util.logging.Logger;

@Deprecated
// Should be refactored
public class FlexoBehaviourParameterDefinitionPathElement extends SimplePathElementImpl {

    private static final Logger logger = Logger.getLogger(FlexoBehaviourParameterDefinitionPathElement.class.getPackage().getName());
    private static final String PARAMETER_DEFINITION = "definition";
    private FlexoBehaviourParameter parameter;

    public FlexoBehaviourParameterDefinitionPathElement(IBindingPathElement parent, FlexoBehaviourParameter parameter, Bindable bindable) {
        super(parent, PARAMETER_DEFINITION, parameter.getImplementedInterface(), bindable);
        this.parameter = parameter;
    }

    /**
     * Return a flag indicating if this BindingPathElement supports computation with 'null' value as entry (target)
     *
     * Returns true here
     *
     * @return
     */
    @Override
    public boolean supportsNullValues() {
        return true;
    }

    @Override
    public String getLabel() {
        return PARAMETER_DEFINITION;
        // return "prout-" + parameter.getName();
    }

    @Override
    public String getTooltipText(Type resultingType) {
        return "Definition for parameter " + parameter.getDescription();
    }

    @Override
    public Object getBindingValue(Object target, BindingEvaluationContext context) throws TypeMismatchException, NullReferenceException {
        // Inconditionnaly return parameter
        return parameter;
    }

    @Override
    public void setBindingValue(Object value, Object target, BindingEvaluationContext context)
            throws TypeMismatchException, NullReferenceException {
        logger.warning("Operation not allowed");
    }

    @Override
    public boolean isResolved() {
        return true;
    }

    @Override
    public void resolve() {
        // Not applicable
    }

}
