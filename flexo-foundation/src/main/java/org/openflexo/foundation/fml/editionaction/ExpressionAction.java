/**
 *
 * Copyright (c) 2015, Openflexo
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

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.connie.type.UndefinedType;
import org.openflexo.foundation.fml.binding.DataBindingPropertyImplementation;
import org.openflexo.foundation.fml.binding.DataBindingPropertyImplementation.DataBindingProperty;
import org.openflexo.foundation.fml.rt.FMLExecutionException;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.fml.validation.BindingIsRequiredAndMustBeValid;
import org.openflexo.pamela.annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.logging.Logger;

/**
 * An {@link EditionAction} which can be represented as an expression
 *
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(ExpressionAction.ExpressionActionImpl.class)
@XMLElement
public interface ExpressionAction<T> extends AssignableAction<T> {

    @PropertyIdentifier(type = DataBinding.class)
    public static final String EXPRESSION_KEY = "expression";

    @Getter(value = EXPRESSION_KEY)
    @PropertyImplementation(DataBindingPropertyImplementation.class)
    @DataBindingProperty(
            bindingName = EXPRESSION_KEY,
            declaredType = Object.class,
            owner = "this",
            bindingDefinitionType = BindingDefinitionType.GET,
            isMandatory = true)
    @XMLAttribute
    public DataBinding<T> getExpression();

    @Setter(EXPRESSION_KEY)
    public void setExpression(DataBinding<T> expression);

    @Updater(EXPRESSION_KEY)
    public void updateExpression(DataBinding<T> expression);

    @Override
    public Type getAssignableType();

    public static abstract class ExpressionActionImpl<T> extends AssignableActionImpl<T> implements ExpressionAction<T> {

        @SuppressWarnings("unused")
        private static final Logger logger = Logger.getLogger(ExpressionAction.class.getPackage().getName());

        // This is the last assignable type that was computed
        // This value will be used to notify type changes
        private Type lastKnownAssignableType = UndefinedType.INSTANCE;

        // Flag to prevent stack overflow
        private boolean isAnalyzingType = false;
        private boolean forceRevalidated = false;

        @Override
        public void finalizeDeserialization() {
            super.finalizeDeserialization();
            // Attempt to resolve assignable type at the deserialization
            getAssignableType();
        }

        @Override
        public Type getAssignableType() {

            if (isAnalyzingType) {
                return lastKnownAssignableType;
            }

            if (getExpression() != null && getExpression().isSet() && getExpression().isValid()) {
                isAnalyzingType = true;
                Type lastKnown = lastKnownAssignableType;
                lastKnownAssignableType = getExpression().getAnalyzedType();
                notifyAssignableTypeMightHaveChanged(lastKnown, lastKnownAssignableType);
                isAnalyzingType = false;
                return lastKnownAssignableType;
            }

            // TODO : je pense que ceci n'est plus utile et doit etre supprime
            // Gros hack ici: le probleme est que la BindingFactory n'etait pas valide au moment de l'analyse
            // Il faut donc ecouter les modifications de getBindingFactory()
            if (getExpression() != null && !getExpression().isValid()) {
                isAnalyzingType = true;
                getExpression().revalidate();
                isAnalyzingType = false;
                if (getExpression().isValid()) {
                    return getExpression().getAnalyzedType();
                }
            }

            return UndefinedType.INSTANCE;

        }

        @Override
        public void notifiedScopeChanged() {
            super.notifiedScopeChanged();
            notifyAssignableTypeMightHaveChanged(lastKnownAssignableType, getExpression().getAnalyzedType());
        }

        /**
         * Notify assignableType changes
         */
        private void notifyAssignableTypeMightHaveChanged(Type lastKnown, Type newAssignableType) {

            if ((lastKnown == null && newAssignableType != null) || (lastKnown != null && !lastKnown.equals(newAssignableType))) {
                getPropertyChangeSupport().firePropertyChange("assignableType", lastKnown, newAssignableType);
                lastKnownAssignableType = newAssignableType;
                // TODO : virer ces deux lignes qui me semblent inutiles
                getPropertyChangeSupport().firePropertyChange("iteratorType", null, getIteratorType());
                getPropertyChangeSupport().firePropertyChange("isIterable", null, isIterable());
            }
        }

        @Override
        public void notifiedBindingChanged(DataBinding<?> dataBinding) {
            super.notifiedBindingChanged(dataBinding);
            if (dataBinding == getExpression()) {
                notifyAssignableTypeMightHaveChanged(lastKnownAssignableType, getExpression().getAnalyzedType());
            }
        }

        @Override
        public void notifiedBindingDecoded(DataBinding<?> dataBinding) {
            super.notifiedBindingDecoded(dataBinding);
            if (dataBinding == getExpression()) {
                notifyAssignableTypeMightHaveChanged(lastKnownAssignableType, getExpression().getAnalyzedType());
            }
        }

        @Override
        public String getStringRepresentation() {
            return getHeaderContext() + (getExpression() != null ? getExpression().toString() : "");
        }

        @Override
        public T execute(RunTimeEvaluationContext evaluationContext) throws FMLExecutionException {

            // Quick and dirty hack because found invalid binding
            // TODO: i think this is no more required
            if (!getExpression().isValid() && !forceRevalidated) {
                forceRevalidated = true;
                getExpression().revalidate();
            }

            try {
                return getExpression().getBindingValue(evaluationContext);
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof FMLExecutionException) {
                    throw (FMLExecutionException) e.getTargetException();
                }
                throw new FMLExecutionException(e);
            } catch (NullReferenceException e) {
                throw new FMLExecutionException(e);
            } catch (TypeMismatchException e) {
                throw new FMLExecutionException(e);
            } catch (ReflectiveOperationException e) {
                throw new FMLExecutionException(e);
            }
        }

        @Override
        public void revalidateBindings() {
            super.revalidateBindings();
            getExpression().rebuild();
        }
    }

    @DefineValidationRule
    public static class ExpressionBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<ExpressionAction> {
        public ExpressionBindingIsRequiredAndMustBeValid() {
            super("'expression'_binding_is_required_and_must_be_valid", ExpressionAction.class);
        }

        @Override
        public DataBinding<Object> getBinding(ExpressionAction object) {
            return object.getExpression();
        }

    }

}
