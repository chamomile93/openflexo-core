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

package org.openflexo.foundation.fml.rt.editionaction;

import org.openflexo.connie.BindingEvaluationContext;
import org.openflexo.connie.BindingVariable;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.connie.expr.ExpressionEvaluator;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptObject;
import org.openflexo.foundation.fml.binding.MatchConditionBindingModel;
import org.openflexo.foundation.fml.expr.FMLExpressionEvaluator;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.fml.validation.BindingIsRequiredAndMustBeValid;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.pamela.annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

/**
 * A condition applied when matching FlexoConceptInstances
 *
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(MatchCondition.MatchConditionImpl.class)
@XMLElement
public interface MatchCondition extends FlexoConceptObject {

    public static final String SELECTED = "selected";

    @PropertyIdentifier(type = InitiateMatching.class)
    public static final String ACTION_KEY = "action";

    @PropertyIdentifier(type = DataBinding.class)
    public static final String CONDITION_KEY = "condition";

    @Getter(value = ACTION_KEY)
    public InitiateMatching getAction();

    @Setter(ACTION_KEY)
    public void setAction(InitiateMatching action);

    @Getter(value = CONDITION_KEY)
    @XMLAttribute
    public DataBinding<Boolean> getCondition();

    @Setter(CONDITION_KEY)
    public void setCondition(DataBinding<Boolean> condition);

    public boolean evaluateCondition(final FlexoConceptInstance proposedFCI, final RunTimeEvaluationContext evaluationContext);

    public static abstract class MatchConditionImpl extends FlexoConceptObjectImpl implements MatchCondition {

        protected static final Logger logger = FlexoLogger.getLogger(MatchCondition.class.getPackage().getName());

        private DataBinding<Boolean> condition;
        private MatchConditionBindingModel bindingModel;

        @Override
        public FlexoConcept getFlexoConcept() {
            if (getAction() != null) {
                return getAction().getFlexoConcept();
            }
            return null;
        }

        @Override
        public MatchConditionBindingModel getBindingModel() {
            if (bindingModel == null) {
                bindingModel = new MatchConditionBindingModel(this);
            }
            return bindingModel;
        }

        @Override
        public DataBinding<Boolean> getCondition() {
            if (condition == null) {
                condition = new DataBinding<>(this, Boolean.class, BindingDefinitionType.GET);
                condition.setBindingName("condition");
            }
            return condition;
        }

        @Override
        public void setCondition(DataBinding<Boolean> condition) {
            if (condition != null) {
                condition.setOwner(this);
                condition.setBindingName("condition");
                condition.setDeclaredType(Boolean.class);
                condition.setBindingDefinitionType(BindingDefinitionType.GET);
            }
            this.condition = condition;
        }

        @Override
        public boolean evaluateCondition(final FlexoConceptInstance proposedFCI, final RunTimeEvaluationContext evaluationContext) {
            Boolean returned = null;
            try {
                returned = condition.getBindingValue(new BindingEvaluationContext() {
                    @Override
                    public ExpressionEvaluator getEvaluator() {
                        return new FMLExpressionEvaluator(this);
                    }

                    @Override
                    public Object getValue(BindingVariable variable) {
                        if (variable.getVariableName().equals(SELECTED)) {
                            return proposedFCI;
                        }
                        return evaluationContext.getValue(variable);
                    }

                });
            } catch (TypeMismatchException e) {
                e.printStackTrace();
            } catch (NullReferenceException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            if (returned == null) {
                return false;
            }
            return returned;
        }

        @Override
        public void revalidateBindings() {
            super.revalidateBindings();
            getCondition().rebuild();
        }

    }

    @DefineValidationRule
    public static class ConditionBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<MatchCondition> {
        public ConditionBindingIsRequiredAndMustBeValid() {
            super("'condition'_binding_is_not_valid", MatchCondition.class);
        }

        @Override
        public DataBinding<Boolean> getBinding(MatchCondition object) {
            return object.getCondition();
        }
    }

}
