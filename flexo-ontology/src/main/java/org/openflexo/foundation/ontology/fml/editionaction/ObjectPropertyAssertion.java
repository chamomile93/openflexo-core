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

package org.openflexo.foundation.ontology.fml.editionaction;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.fml.validation.BindingIsRequiredAndMustBeValid;
import org.openflexo.foundation.ontology.*;
import org.openflexo.foundation.ontology.nature.FlexoOntologyVirtualModelNature;
import org.openflexo.pamela.annotations.*;
import org.openflexo.pamela.validation.ValidationError;
import org.openflexo.pamela.validation.ValidationIssue;
import org.openflexo.pamela.validation.ValidationRule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.logging.Logger;

@ModelEntity
@ImplementationClass(ObjectPropertyAssertion.ObjectPropertyAssertionImpl.class)
@XMLElement
public interface ObjectPropertyAssertion extends AbstractAssertion {

    @PropertyIdentifier(type = AddIndividual.class)
    public static final String ACTION_KEY = "action";

    @PropertyIdentifier(type = String.class)
    public static final String OBJECT_PROPERTY_URI_KEY = "objectPropertyURI";
    @PropertyIdentifier(type = DataBinding.class)
    public static final String OBJECT_KEY = "object";

    @Override
    @Getter(value = ACTION_KEY, inverse = AddIndividual.OBJECT_ASSERTIONS_KEY)
    public AddIndividual<?, ?, ?, ?> getAction();

    @Override
    @Setter(ACTION_KEY)
    public void setAction(AddIndividual<?, ?, ?, ?> action);

    @Getter(value = OBJECT_PROPERTY_URI_KEY)
    @XMLAttribute
    public String _getObjectPropertyURI();

    @Setter(OBJECT_PROPERTY_URI_KEY)
    public void _setObjectPropertyURI(String objectPropertyURI);

    @Getter(value = OBJECT_KEY)
    @XMLAttribute
    public DataBinding<?> getObject();

    @Setter(OBJECT_KEY)
    public void setObject(DataBinding<?> object);

    public IFlexoOntologyStructuralProperty<?> getOntologyProperty();

    public void setOntologyProperty(IFlexoOntologyStructuralProperty<?> p);

    public Object getValue(RunTimeEvaluationContext evaluationContext);

    public IFlexoOntologyConcept getAssertionObject(RunTimeEvaluationContext evaluationContext);

    public static abstract class ObjectPropertyAssertionImpl extends AbstractAssertionImpl implements ObjectPropertyAssertion {

        private static final Logger logger = Logger.getLogger(ObjectPropertyAssertion.class.getPackage().getName());

        private String objectPropertyURI;
        private DataBinding<?> object;

        public ObjectPropertyAssertionImpl() {
            super();
        }

        @Override
        public void _setObjectPropertyURI(String objectPropertyURI) {
            this.objectPropertyURI = objectPropertyURI;
        }

        @Override
        public String _getObjectPropertyURI() {
            return objectPropertyURI;
        }

        @Override
        public IFlexoOntologyStructuralProperty<?> getOntologyProperty() {
            if (FlexoOntologyVirtualModelNature.INSTANCE.hasNature(getOwningVirtualModel())) {
                return FlexoOntologyVirtualModelNature.getOntologyProperty(_getObjectPropertyURI(), getOwningVirtualModel());
            }
            return null;
        }

        @Override
        public void setOntologyProperty(IFlexoOntologyStructuralProperty<?> p) {
            _setObjectPropertyURI(p != null ? p.getURI() : null);
        }

        public Type getObjectType() {
            if (getOntologyProperty() instanceof IFlexoOntologyObjectProperty
                    && ((IFlexoOntologyObjectProperty) getOntologyProperty()).getRange() instanceof IFlexoOntologyClass) {
                return IndividualOfClass
                        .getIndividualOfClass((IFlexoOntologyClass) ((IFlexoOntologyObjectProperty) getOntologyProperty()).getRange());
            }
            return IFlexoOntologyConcept.class;
        }

        @Override
        public DataBinding<?> getObject() {
            if (object == null) {
                object = new DataBinding<>(this, getObjectType(), BindingDefinitionType.GET);
                object.setBindingName("object");
            }
            return object;
        }

        @Override
        public void setObject(DataBinding<?> object) {
            if (object != null) {
                object.setOwner(this);
                object.setBindingName("object");
                object.setDeclaredType(getObjectType());
                object.setBindingDefinitionType(BindingDefinitionType.GET);
            }
            this.object = object;
        }

        @Override
        public IFlexoOntologyConcept getAssertionObject(RunTimeEvaluationContext evaluationContext) {
            Object value = null;
            try {
                value = getObject().getBindingValue(evaluationContext);
            } catch (TypeMismatchException e) {
                e.printStackTrace();
            } catch (NullReferenceException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            if (value instanceof IFlexoOntologyConcept) {
                return (IFlexoOntologyConcept) value;
            }
            return null;
        }

        @Override
        public Object getValue(RunTimeEvaluationContext evaluationContext) {
            try {
                return getObject().getBindingValue(evaluationContext);
            } catch (TypeMismatchException e) {
                e.printStackTrace();
            } catch (NullReferenceException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    @DefineValidationRule
    public static class ObjectPropertyAssertionMustDefineAnOntologyProperty
            extends ValidationRule<ObjectPropertyAssertionMustDefineAnOntologyProperty, ObjectPropertyAssertion> {
        public ObjectPropertyAssertionMustDefineAnOntologyProperty() {
            super(ObjectPropertyAssertion.class, "object_property_assertion_must_define_an_ontology_property");
        }

        @Override
        public ValidationIssue<ObjectPropertyAssertionMustDefineAnOntologyProperty, ObjectPropertyAssertion> applyValidation(
                ObjectPropertyAssertion assertion) {
            if (assertion.getOntologyProperty() == null) {
                return new ValidationError<>(this, assertion, "object_property_assertion_must_define_an_ontology_property");
            }
            return null;
        }

    }

    @DefineValidationRule
    public static class ObjectBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<ObjectPropertyAssertion> {
        public ObjectBindingIsRequiredAndMustBeValid() {
            super("'object'_binding_is_required_and_must_be_valid", ObjectPropertyAssertion.class);
        }

        @Override
        public DataBinding<?> getBinding(ObjectPropertyAssertion object) {
            return object.getObject();
        }

    }

}
