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

package org.openflexo.foundation.fml;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.fml.rt.action.NavigationSchemeActionFactory;
import org.openflexo.foundation.fml.validation.BindingIsRequiredAndMustBeValid;
import org.openflexo.foundation.nature.FlexoNature;
import org.openflexo.pamela.annotations.*;

import java.lang.reflect.InvocationTargetException;

@ModelEntity
@ImplementationClass(NavigationScheme.NavigationSchemeImpl.class)
@XMLElement
public interface NavigationScheme extends AbstractActionScheme {

    @PropertyIdentifier(type = DataBinding.class)
    public static final String TARGET_OBJECT_KEY = "targetObject";

    @Getter(value = TARGET_OBJECT_KEY)
    @XMLAttribute
    public DataBinding<?> getTargetObject();

    @Setter(TARGET_OBJECT_KEY)
    public void setTargetObject(DataBinding<?> targetObject);

    public FlexoObject evaluateTargetObject(RunTimeEvaluationContext evaluationContext);

    /**
     * Returns nature which we will try to use to display target object
     *
     * @return
     */
    public <O extends FlexoObject> FlexoNature<O> getDisplayNature(O targetObject);

    public static abstract class NavigationSchemeImpl extends AbstractActionSchemeImpl implements NavigationScheme {

        private DataBinding<?> targetObject;

        public NavigationSchemeImpl() {
            super();
        }

        @Override
        public DataBinding<?> getTargetObject() {
            if (targetObject == null) {
                targetObject = new DataBinding<>(this, FlexoObject.class, BindingDefinitionType.GET);
                targetObject.setBindingName("targetObject");
            }
            return targetObject;
        }

        @Override
        public void setTargetObject(DataBinding<?> targetObject) {
            if (targetObject != null) {
                targetObject.setOwner(this);
                targetObject.setBindingName("targetObject");
                targetObject.setDeclaredType(FlexoObject.class);
                targetObject.setBindingDefinitionType(BindingDefinitionType.GET);
            }
            this.targetObject = targetObject;
        }

        @Override
        public FlexoObject evaluateTargetObject(RunTimeEvaluationContext evaluationContext) {
            if (getTargetObject().isValid()) {
                try {
                    return (FlexoObject) getTargetObject().getBindingValue(evaluationContext);
                } catch (TypeMismatchException e) {
                    e.printStackTrace();
                } catch (NullReferenceException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public NavigationSchemeActionFactory getActionFactory(FlexoConceptInstance fci) {
            return new NavigationSchemeActionFactory(this, fci);
        }

        /**
         * Returns nature which we will try to use to display target object
         *
         * @return
         */
        @Override
        public <O extends FlexoObject> FlexoNature<O> getDisplayNature(O targetObject) {
            return null;
        }

        @Override
        public void revalidateBindings() {
            super.revalidateBindings();
            getTargetObject().rebuild();
        }

    }

    @DefineValidationRule
    public static class TargetObjectIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<NavigationScheme> {
        public TargetObjectIsRequiredAndMustBeValid() {
            super("'target_object'_binding_is_not_valid", NavigationScheme.class);
        }

        @Override
        public DataBinding<?> getBinding(NavigationScheme object) {
            return object.getTargetObject();
        }
    }

}
