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
import org.openflexo.connie.DataBinding.CachingStrategy;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.AbstractActionSchemeActionFactory;
import org.openflexo.pamela.annotations.*;

import java.lang.reflect.InvocationTargetException;

/**
 * Abstract behaviour being called on an existing {@link VirtualModelInstance}
 *
 * @author sylvain
 *
 */
@ModelEntity(isAbstract = true)
@ImplementationClass(AbstractActionScheme.AbstractActionSchemeImpl.class)
public abstract interface AbstractActionScheme extends FlexoBehaviour {

    @PropertyIdentifier(type = DataBinding.class)
    public static final String CONDITIONAL_KEY = "conditional";

    // Not sure this is a good idea in the language
    @Deprecated
    @Getter(value = CONDITIONAL_KEY)
    @XMLAttribute
    public DataBinding<Boolean> getConditional();

    @Deprecated
    @Setter(CONDITIONAL_KEY)
    public void setConditional(DataBinding<Boolean> conditional);

    public boolean evaluateCondition(FlexoConceptInstance flexoConceptInstance);

    AbstractActionSchemeActionFactory<?, ?, ?> getActionFactory(FlexoConceptInstance fci);

    public static abstract class AbstractActionSchemeImpl extends FlexoBehaviourImpl implements AbstractActionScheme {

        private DataBinding<Boolean> conditional;

        public AbstractActionSchemeImpl() {
            super();
        }

        // TODO: we had to set caching strategy to NO_CACHING in dynamic instantiation context (CTA project)
        @Override
        @Deprecated
        public DataBinding<Boolean> getConditional() {
            if (conditional == null) {
                conditional = new DataBinding<>(this, Boolean.class, DataBinding.BindingDefinitionType.GET);
                conditional.setCachingStrategy(CachingStrategy.NO_CACHING);
                conditional.setBindingName("conditional");
            }
            return conditional;
        }

        // TODO: we had to set caching strategy to NO_CACHING in dynamic instantiation context (CTA project)
        @Override
        @Deprecated
        public void setConditional(DataBinding<Boolean> conditional) {
            if (conditional != null) {
                conditional.setOwner(this);
                conditional.setDeclaredType(Boolean.class);
                conditional.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
                conditional.setCachingStrategy(CachingStrategy.NO_CACHING);
                conditional.setBindingName("conditional");
            }
            this.conditional = conditional;
        }

        @Override
        public boolean evaluateCondition(FlexoConceptInstance flexoConceptInstance) {
            if (getConditional().isSet() && getConditional().isValid()) {
                try {
                    Boolean returned = getConditional().getBindingValue(flexoConceptInstance);
                    if (returned != null) {
                        return returned;
                    }
                    return true;
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
            return true;
        }

        @Override
        public AbstractActionSchemeActionFactory<?, ?, ?> getActionFactory(FlexoConceptInstance fci) {
            return null;
        }

        @Override
        public void revalidateBindings() {
            super.revalidateBindings();
            getConditional().rebuild();
        }

    }
}
