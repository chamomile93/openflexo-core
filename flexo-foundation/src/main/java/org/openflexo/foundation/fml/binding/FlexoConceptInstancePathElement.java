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

package org.openflexo.foundation.fml.binding;

import org.openflexo.connie.Bindable;
import org.openflexo.connie.BindingEvaluationContext;
import org.openflexo.connie.binding.IBindingPathElement;
import org.openflexo.connie.binding.SimplePathElementImpl;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.connie.expr.BindingPath;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceType;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Type;
import java.util.logging.Logger;

/**
 * A path element which represents a {@link FlexoConceptInstance} accessible at run-time when a behaviour is called<br>
 * The {@link FlexoConceptInstance} is typed as {@link FlexoConceptInstanceType}
 *
 * @author sylvain
 *
 */
@Deprecated
// Should be refactored
public class FlexoConceptInstancePathElement extends SimplePathElementImpl implements PropertyChangeListener {

    private static final Logger logger = Logger.getLogger(FlexoConceptInstancePathElement.class.getPackage().getName());

    private final FlexoConcept flexoConcept;
    private boolean isNotifyingBindingPathChanged = false;

    public FlexoConceptInstancePathElement(IBindingPathElement parent, String pathElementName, FlexoConcept flexoConcept,
                                           Bindable bindable) {
        super(parent, pathElementName, FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept), bindable);
        this.flexoConcept = flexoConcept;
    }

    @Override
    public void activate(BindingPath bindingPath) {
        super.activate(bindingPath);
        if (flexoConcept != null && flexoConcept.getPropertyChangeSupport() != null) {
            flexoConcept.getPropertyChangeSupport().addPropertyChangeListener(this);
        }
    }

    @Override
    public void desactivate() {
        if (flexoConcept != null && flexoConcept.getPropertyChangeSupport() != null) {
            flexoConcept.getPropertyChangeSupport().removePropertyChangeListener(this);
        }
        super.desactivate();
    }

    public FlexoConcept getFlexoConcept() {
        return flexoConcept;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof FlexoConcept) {
            if (evt.getPropertyName().equals(FlexoConcept.FLEXO_PROPERTIES_KEY)
                    || evt.getPropertyName().equals(FlexoConcept.FLEXO_BEHAVIOURS_KEY)) {
                isNotifyingBindingPathChanged = true;
                getPropertyChangeSupport().firePropertyChange(BINDING_PATH_CHANGED, false, true);
                isNotifyingBindingPathChanged = false;
            }
        }
    }

    @Override
    public boolean isNotifyingBindingPathChanged() {
        return isNotifyingBindingPathChanged;
    }

    @Override
    public Type getType() {
        return FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept);
    }

    @Override
    public String getLabel() {
        return getPropertyName();
    }

    @Override
    public String getTooltipText(Type resultingType) {
        return flexoConcept.getDescription();
    }

    @Override
    public Object getBindingValue(Object target, BindingEvaluationContext context) throws TypeMismatchException, NullReferenceException {
        if (target instanceof FlexoBehaviourAction) {
            return ((FlexoBehaviourAction<?, ?, ?>) target).getFlexoConceptInstance();
        }
        logger.warning("Please implement me, target=" + target + " context=" + context);
        return null;
    }

    @Override
    public void setBindingValue(Object value, Object target, BindingEvaluationContext context)
            throws TypeMismatchException, NullReferenceException {
        logger.warning("Please implement me, target=" + target + " context=" + context);
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
