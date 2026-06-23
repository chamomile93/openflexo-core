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

package org.openflexo.foundation.fml.editionaction;

import org.openflexo.connie.BindingEvaluationContext;
import org.openflexo.connie.BindingVariable;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.connie.expr.ExpressionEvaluator;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptObject;
import org.openflexo.foundation.fml.binding.FetchRequestConditionBindingModel;
import org.openflexo.foundation.fml.expr.FMLExpressionEvaluator;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.fml.validation.BindingIsRequiredAndMustBeValid;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.pamela.annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

/**
 * An AbstractInvariant represents a structural constraint attached to an FlexoConcept
 *
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(FetchRequestCondition.FetchRequestConditionImpl.class)
@XMLElement(xmlTag = "Condition")
public interface FetchRequestCondition extends FlexoConceptObject {

    public static final String SELECTED = "selected";

    @PropertyIdentifier(type = AbstractFetchRequest.class)
    public static final String ACTION_KEY = "action";

    @PropertyIdentifier(type = DataBinding.class)
    public static final String CONDITION_KEY = "condition";

    @Getter(value = ACTION_KEY /*, inverse = FetchRequest.CONDITIONS_KEY*/)
    public AbstractFetchRequest<?, ?, ?, ?> getAction();

    @Setter(ACTION_KEY)
    public void setAction(AbstractFetchRequest<?, ?, ?, ?> action);

    @Getter(value = CONDITION_KEY)
    @XMLAttribute
    public DataBinding<Boolean> getCondition();

    @Setter(CONDITION_KEY)
    public void setCondition(DataBinding<Boolean> condition);

    public boolean evaluateCondition(final Object proposedFetchResult, final RunTimeEvaluationContext evaluationContext);

    public static abstract class FetchRequestConditionImpl extends FlexoConceptObjectImpl implements FetchRequestCondition {

        protected static final Logger logger = FlexoLogger.getLogger(FetchRequestCondition.class.getPackage().getName());

        // private FetchRequest fetchRequest;
        private DataBinding<Boolean> condition;
        private FetchRequestConditionBindingModel bindingModel;

        public FetchRequestConditionImpl() {
            super();
        }

        @Override
        public FlexoConcept getFlexoConcept() {
            if (getAction() != null) {
                return getAction().getFlexoConcept();
            }
            return null;
        }

        @Override
        public FetchRequestConditionBindingModel getBindingModel() {
            if (bindingModel == null) {
                bindingModel = new FetchRequestConditionBindingModel(this);
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
        public boolean evaluateCondition(final Object proposedFetchResult, final RunTimeEvaluationContext evaluationContext) {
            Boolean returned = null;
            try {
                returned = condition.getBindingValue(new BindingEvaluationContext() {
                    @Override
                    public Object getValue(BindingVariable variable) {
                        if (variable.getVariableName().equals(SELECTED)) {
                            return proposedFetchResult;
                        }
                        return evaluationContext.getValue(variable);
                    }

                    @Override
                    public ExpressionEvaluator getEvaluator() {
                        return new FMLExpressionEvaluator(this);
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
    public static class ConditionBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<FetchRequestCondition> {
        public ConditionBindingIsRequiredAndMustBeValid() {
            super("'condition'_binding_is_not_valid", FetchRequestCondition.class);
        }

        @Override
        public DataBinding<Boolean> getBinding(FetchRequestCondition object) {
            return object.getCondition();
        }
    }

}
