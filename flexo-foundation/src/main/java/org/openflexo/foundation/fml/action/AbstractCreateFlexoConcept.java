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

package org.openflexo.foundation.fml.action;

import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.LongRunningAction;
import org.openflexo.foundation.fml.*;
import org.openflexo.foundation.fml.FlexoBehaviourParameter.FlexoBehaviourParameterImpl;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour.BehaviourParameterEntry;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.fml.editionaction.ExpressionAction;
import org.openflexo.foundation.fml.inspector.FlexoConceptInspector;
import org.openflexo.foundation.task.Progress;
import org.openflexo.toolbox.PropertyChangedSupportDefaultImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Abstract action creating a {@link FlexoConcept} or any of its subclass
 *
 * @author sylvain
 *
 */
public abstract class AbstractCreateFlexoConcept<A extends FlexoAction<A, T1, T2>, T1 extends FlexoObject, T2 extends FMLObject>
        extends FlexoAction<A, T1, T2> implements LongRunningAction {

    public static final String PARENT_FLEXO_CONCEPT_ENTRIES = "parentFlexoConceptEntries";
    public static final String PROPERTIES_ENTRIES = "propertiesEntries";
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(AbstractCreateFlexoConcept.class.getPackage().getName());
    private final List<ParentFlexoConceptEntry> parentFlexoConceptEntries;

    private final List<PropertyEntry<?>> propertiesEntries;
    private boolean isAbstract = false;
    private Visibility visibility = Visibility.Public;
    private List<PropertyEntry<?>> propertiesUsedForCreationScheme;
    private List<PropertyEntry<?>> propertiesUsedForInspector;
    private boolean defineInspector = false;
    private boolean defineSomeBehaviours = false;
    private boolean defineDefaultCreationScheme = true;
    private boolean defineDefaultDeletionScheme = true;
    private boolean defineSynchronizationScheme = false;
    private boolean defineCloningScheme = false;

    protected AbstractCreateFlexoConcept(FlexoActionFactory<A, T1, T2> actionType, T1 focusedObject, Vector<T2> globalSelection,
                                         FlexoEditor editor) {
        super(actionType, focusedObject, globalSelection, editor);
        parentFlexoConceptEntries = new ArrayList<>();
        propertiesEntries = new ArrayList<>();
    }

    private static String paramNameForEntry(PropertyEntry<?> entry) {
        String capitalizedName = entry.getName().substring(0, 1).toUpperCase() + entry.getName().substring(1);
        if (capitalizedName.startsWith("A") || capitalizedName.startsWith("E") || capitalizedName.startsWith("I")
                || capitalizedName.startsWith("O") || capitalizedName.startsWith("U")) {
            return "an" + capitalizedName;
        }
        return "a" + capitalizedName;
    }

    public abstract FlexoConcept getNewFlexoConcept();

    public boolean getIsAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(boolean isAbstract) {
        boolean wasValid = isValid();
        this.isAbstract = isAbstract;
        getPropertyChangeSupport().firePropertyChange("isAbstract", null, isAbstract);
        getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        boolean wasValid = isValid();
        this.visibility = visibility;
        getPropertyChangeSupport().firePropertyChange("visibility", null, visibility);
        getPropertyChangeSupport().firePropertyChange("isValid", wasValid, isValid());
    }

    public List<ParentFlexoConceptEntry> getParentFlexoConceptEntries() {
        return parentFlexoConceptEntries;
    }

    public ParentFlexoConceptEntry newParentFlexoConceptEntry() {
        ParentFlexoConceptEntry returned = new ParentFlexoConceptEntry();
        parentFlexoConceptEntries.add(returned);
        getPropertyChangeSupport().firePropertyChange(PARENT_FLEXO_CONCEPT_ENTRIES, null, returned);
        return returned;
    }

    public void deleteParentFlexoConceptEntry(ParentFlexoConceptEntry parentFlexoConceptEntryToDelete) {
        parentFlexoConceptEntries.remove(parentFlexoConceptEntryToDelete);
        parentFlexoConceptEntryToDelete.delete();
        getPropertyChangeSupport().firePropertyChange(PARENT_FLEXO_CONCEPT_ENTRIES, parentFlexoConceptEntryToDelete, null);
    }

    public ParentFlexoConceptEntry addToParentConcepts(FlexoConcept parentFlexoConcept) {
        ParentFlexoConceptEntry newParentFlexoConceptEntry = new ParentFlexoConceptEntry(parentFlexoConcept);
        parentFlexoConceptEntries.add(newParentFlexoConceptEntry);
        getPropertyChangeSupport().firePropertyChange(PARENT_FLEXO_CONCEPT_ENTRIES, null, newParentFlexoConceptEntry);
        return newParentFlexoConceptEntry;
    }

    protected void performSetParentConcepts() throws InconsistentFlexoConceptHierarchyException {
        for (ParentFlexoConceptEntry entry : getParentFlexoConceptEntries()) {
            getNewFlexoConcept().addToParentFlexoConcepts(entry.getParentConcept());
        }
    }

    public List<PropertyEntry<?>> getPropertiesEntries() {
        return propertiesEntries;
    }

    public List<PropertyEntry<?>> getPropertiesUsedForCreationScheme() {
        if (propertiesUsedForCreationScheme == null) {
            propertiesUsedForCreationScheme = new ArrayList<>();
            propertiesUsedForCreationScheme.addAll(getPropertiesEntries());
        }
        return propertiesUsedForCreationScheme;
    }

    public List<PropertyEntry<?>> getPropertiesUsedForInspector() {
        if (propertiesUsedForInspector == null) {
            propertiesUsedForInspector = new ArrayList<>();
            propertiesUsedForInspector.addAll(getPropertiesEntries());
        }
        return propertiesUsedForInspector;
    }

    public PropertyEntry<?> newPropertyEntry() {
        PropertyEntry<?> returned = new PropertyEntry<>("property" + (getPropertiesEntries().size() + 1), getLocales(),
                getFocusedObject() instanceof FlexoConceptObject ? (FlexoConceptObject) getFocusedObject() : null);
        returned.setType(String.class);
        propertiesEntries.add(returned);
        getPropertyChangeSupport().firePropertyChange(PROPERTIES_ENTRIES, null, returned);
        return returned;
    }

    public void deletePropertyEntry(PropertyEntry<?> propertyEntryToDelete) {
        propertiesEntries.remove(propertyEntryToDelete);
        propertyEntryToDelete.delete();
        getPropertyChangeSupport().firePropertyChange(PROPERTIES_ENTRIES, propertyEntryToDelete, null);
    }

    public void propertyFirst(PropertyEntry<?> p) {
        getPropertiesEntries().remove(p);
        getPropertiesEntries().add(0, p);
        getPropertyChangeSupport().firePropertyChange(PROPERTIES_ENTRIES, null, getPropertiesEntries());
    }

    public void propertyUp(PropertyEntry<?> p) {
        int index = getPropertiesEntries().indexOf(p);
        if (index > 0) {
            getPropertiesEntries().remove(p);
            getPropertiesEntries().add(index - 1, p);
            getPropertyChangeSupport().firePropertyChange(PROPERTIES_ENTRIES, null, getPropertiesEntries());
        }
    }

    public void propertyDown(PropertyEntry<?> p) {
        int index = getPropertiesEntries().indexOf(p);
        if (index > -1) {
            getPropertiesEntries().remove(p);
            getPropertiesEntries().add(index + 1, p);
            getPropertyChangeSupport().firePropertyChange(PROPERTIES_ENTRIES, null, getPropertiesEntries());
        }
    }

    public void propertyLast(PropertyEntry<?> p) {
        getPropertiesEntries().remove(p);
        getPropertiesEntries().add(p);
        getPropertyChangeSupport().firePropertyChange(PROPERTIES_ENTRIES, null, getPropertiesEntries());
    }

    protected void performCreateProperties() {
        for (PropertyEntry<?> entry : getPropertiesEntries()) {
            entry.performCreateProperty(getNewFlexoConcept(), this);
        }
    }

    protected void performCreateBehaviours() {
        if (getDefineSomeBehaviours()) {
            if (getDefineDefaultCreationScheme()) {
                CreateFlexoBehaviour createCreationScheme = CreateFlexoBehaviour.actionType.makeNewEmbeddedAction(getNewFlexoConcept(),
                        null, this);
                createCreationScheme.setFlexoBehaviourName("create");
                createCreationScheme.setFlexoBehaviourClass(CreationScheme.class);
                for (PropertyEntry<?> entry : getPropertiesUsedForCreationScheme()) {
                    BehaviourParameterEntry newEntry = createCreationScheme.newParameterEntry();
                    newEntry.setParameterName(paramNameForEntry(entry));
                    newEntry.setParameterType(entry.getType());
                    newEntry.setContainer(entry.getContainer());
                    newEntry.setDefaultValue(entry.getDefaultValue());
                    newEntry.setParameterDescription(entry.getDescription());
                }
                System.out.println("action valide = " + createCreationScheme.isValid());
                createCreationScheme.doAction();
                CreationScheme creationScheme = (CreationScheme) createCreationScheme.getNewFlexoBehaviour();
                for (PropertyEntry<?> entry : getPropertiesUsedForCreationScheme()) {
                    CreateEditionAction assignAction = CreateEditionAction.actionType
                            .makeNewEmbeddedAction(creationScheme.getControlGraph(), null, this);
                    assignAction.setEditionActionClass(ExpressionAction.class);
                    assignAction.setAssignation(new DataBinding<>(entry.getName()));
                    assignAction.doAction();
                    AssignationAction<?> createRightMember = (AssignationAction<?>) assignAction.getNewEditionAction();
                    ((ExpressionAction<?>) createRightMember.getAssignableAction())
                            .setExpression(new DataBinding<>("parameters." + paramNameForEntry(entry)));
                }

            }

            if (getDefineDefaultDeletionScheme()) {
                getNewFlexoConcept().generateDefaultDeletionScheme();
            }

            if (getDefineSynchronizationScheme()) {
                CreateFlexoBehaviour createSynchronizationScheme = CreateFlexoBehaviour.actionType
                        .makeNewEmbeddedAction(getNewFlexoConcept(), null, this);
                createSynchronizationScheme.setFlexoBehaviourName("synchronize");
                createSynchronizationScheme.setFlexoBehaviourClass(SynchronizationScheme.class);
                createSynchronizationScheme.doAction();
            }

            if (getDefineCloningScheme()) {
                CreateFlexoBehaviour createCloningScheme = CreateFlexoBehaviour.actionType.makeNewEmbeddedAction(getNewFlexoConcept(), null,
                        this);
                createCloningScheme.setFlexoBehaviourName("clone");
                createCloningScheme.setFlexoBehaviourClass(CloningScheme.class);
                createCloningScheme.doAction();
            }
        }
    }

    public void performCreateInspectors() {

        if (getDefineInspector()) {

            FlexoConceptInspector inspector = getNewFlexoConcept().getInspector();
            System.out.println("Creating inspector " + inspector);

            System.out.println("getPropertiesUsedForInspector()=" + getPropertiesUsedForInspector());

            for (PropertyEntry<?> entry : getPropertiesUsedForInspector()) {
                performCreateInspectorEntry(entry, inspector);
            }
        }
    }

    private void performCreateInspectorEntry(PropertyEntry<?> entry, FlexoConceptInspector inspector) {
        Progress.progress(getLocales().localizedForKey("create_inspector_entry") + " " + entry.getName());

        System.out.println("Create inspector entry " + entry.getName() + " type=" + entry.getType());

        CreateInspectorEntry action = CreateInspectorEntry.actionType.makeNewEmbeddedAction(inspector, null, this);
        action.setEntryName(entry.getName());
        action.setEntryType(entry.getType());
        action.setWidgetType(FlexoBehaviourParameterImpl.getAvailableWidgetTypes(entry.getType()).get(0));
        action.setContainer(entry.getContainer());
        action.setData(new DataBinding<>(entry.getName()));
        // action.setList(entry.getList());
        action.setIsReadOnly(false);
        action.setDescription(entry.getDescription());
        action.doAction();
    }

    public boolean getDefineInspector() {
        return defineInspector;
    }

    public void setDefineInspector(boolean defineInspector) {
        if (defineInspector != this.defineInspector) {
            this.defineInspector = defineInspector;
            getPropertyChangeSupport().firePropertyChange("defineInspector", !defineInspector, defineInspector);
        }
    }

    public boolean getDefineSomeBehaviours() {
        return defineSomeBehaviours;
    }

    public void setDefineSomeBehaviours(boolean defineSomeBehaviours) {
        if (defineSomeBehaviours != this.defineSomeBehaviours) {
            this.defineSomeBehaviours = defineSomeBehaviours;
            getPropertyChangeSupport().firePropertyChange("defineSomeBehaviours", !defineSomeBehaviours, defineSomeBehaviours);
        }
    }

    public boolean getDefineDefaultCreationScheme() {
        return defineDefaultCreationScheme;
    }

    public void setDefineDefaultCreationScheme(boolean defineDefaultCreationScheme) {
        if (defineDefaultCreationScheme != this.defineDefaultCreationScheme) {
            this.defineDefaultCreationScheme = defineDefaultCreationScheme;
            getPropertyChangeSupport().firePropertyChange("defineDefaultCreationScheme", !defineDefaultCreationScheme,
                    defineDefaultCreationScheme);
        }
    }

    public boolean getDefineDefaultDeletionScheme() {
        return defineDefaultDeletionScheme;
    }

    public void setDefineDefaultDeletionScheme(boolean defineDefaultDeletionScheme) {
        if (defineDefaultDeletionScheme != this.defineDefaultDeletionScheme) {
            this.defineDefaultDeletionScheme = defineDefaultDeletionScheme;
            getPropertyChangeSupport().firePropertyChange("defineDefaultDeletionScheme", !defineDefaultDeletionScheme,
                    defineDefaultDeletionScheme);
        }
    }

    public boolean getDefineSynchronizationScheme() {
        return defineSynchronizationScheme;
    }

    public void setDefineSynchronizationScheme(boolean defineSynchronizationScheme) {
        if (defineSynchronizationScheme != this.defineSynchronizationScheme) {
            this.defineSynchronizationScheme = defineSynchronizationScheme;
            getPropertyChangeSupport().firePropertyChange("defineSynchronizationScheme", !defineSynchronizationScheme,
                    defineSynchronizationScheme);
        }
    }

    public boolean getDefineCloningScheme() {
        return defineCloningScheme;
    }

    public void setDefineCloningScheme(boolean defineCloningScheme) {
        if (defineCloningScheme != this.defineCloningScheme) {
            this.defineCloningScheme = defineCloningScheme;
            getPropertyChangeSupport().firePropertyChange("defineCloningScheme", !defineCloningScheme, defineCloningScheme);
        }
    }

    public static class ParentFlexoConceptEntry extends PropertyChangedSupportDefaultImplementation {

        private FlexoConcept parentConcept;

        public ParentFlexoConceptEntry() {
            super();
        }

        public ParentFlexoConceptEntry(FlexoConcept parentConcept) {
            super();
            this.parentConcept = parentConcept;
        }

        public void delete() {
            parentConcept = null;
        }

        public FlexoConcept getParentConcept() {
            return parentConcept;
        }

        public void setParentConcept(FlexoConcept parentConcept) {
            if ((parentConcept == null && this.parentConcept != null)
                    || (parentConcept != null && !parentConcept.equals(this.parentConcept))) {
                FlexoConcept oldValue = this.parentConcept;
                this.parentConcept = parentConcept;
                getPropertyChangeSupport().firePropertyChange("parentConcept", oldValue, parentConcept);
            }
        }
    }

}
