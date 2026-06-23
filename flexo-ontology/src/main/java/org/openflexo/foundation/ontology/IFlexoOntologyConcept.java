/**
 *
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012, THALES SYSTEMES AEROPORTES - All Rights Reserved
 * Copyright (c) 2011-2012, AgileBirds
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

package org.openflexo.foundation.ontology;

import org.openflexo.foundation.technologyadapter.TechnologyAdapter;

import java.util.List;

/**
 * Common interface for concepts of Ontology.
 *
 * @author gbesancon
 */
public interface IFlexoOntologyConcept<TA extends TechnologyAdapter<TA>> extends IFlexoOntologyObject<TA> {
    /**
     * Ontology of Concept.
     *
     * @return
     */
    public IFlexoOntology<TA> getOntology();

    /**
     * Annotation upon Concept.
     *
     * @return
     */
    public List<? extends IFlexoOntologyAnnotation> getAnnotations();

    /**
     * Container of Concept.
     *
     * @return
     */
    public IFlexoOntologyConceptContainer<TA> getContainer();

    /**
     * Association with structural features for Concept.
     *
     * @return
     */
    public List<? extends IFlexoOntologyFeatureAssociation<TA>> getStructuralFeatureAssociations();

    /**
     * Association with behavioural features for Concept.
     *
     * @return
     */
    public List<? extends IFlexoOntologyFeatureAssociation<TA>> getBehaviouralFeatureAssociations();

    /**
     *
     * Is this a Super Concept of concept.
     *
     * @return
     */
    public boolean isSuperConceptOf(IFlexoOntologyConcept<TA> concept);

    /**
     *
     * Is this a Sub Concept of concept.
     *
     * @return
     */
    public boolean isSubConceptOf(IFlexoOntologyConcept<TA> concept);

    /**
     * Visitor access.
     *
     * @param visitor
     * @return
     *
     * @pattern visitor
     */
    public <T> T accept(IFlexoOntologyConceptVisitor<T> visitor);

    /**
     * This equals has a particular semantics (differs from {@link #equals(Object)} method) in the way that it returns true only and only if
     * compared objects are representing same concept regarding URI. This does not guarantee that both objects will respond the same way to
     * some methods.<br>
     * This method returns true if and only if objects are same, or if one of both object redefine the other one (with eventual many levels)
     *
     * @param o
     * @return
     */
    public boolean equalsToConcept(IFlexoOntologyConcept<TA> concept);

    /**
     * Return all properties accessible in the scope of this ontology object, where declared domain is this object
     *
     * @return
     */
    //TODO missing a definition here ?

}
