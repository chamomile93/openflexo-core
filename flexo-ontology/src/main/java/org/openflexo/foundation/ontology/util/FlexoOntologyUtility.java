/**
 *
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012, THALES SYSTEMES AEROPORTES - All Rights Reserved
 * Copyright (c) 2012-2012, AgileBirds
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

package org.openflexo.foundation.ontology.util;

import org.openflexo.foundation.ontology.*;
import org.openflexo.foundation.ontology.util.visitor.ToStringVisitor;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for FlexoOntology.
 *
 * @author gbesancon
 */
public class FlexoOntologyUtility {

    /**
     * To String.
     *
     * @param flexoOntology
     * @return
     */
    public static <TA extends TechnologyAdapter<TA>> String toString(IFlexoOntology<TA> flexoOntology) {
        StringBuilder builder = new StringBuilder();
        try {
            builder.append("Ontology : ");
            builder.append(flexoOntology.getName());
            builder.append(" (");
            builder.append(flexoOntology.getURI());
            builder.append(")\n");
            for (IFlexoOntologyConcept<TA> concept : flexoOntology.getConcepts()) {
                builder.append(concept.accept(new ToStringVisitor()));
                builder.append("\n");
            }
            for (IFlexoOntologyDataType<TA> dataType : flexoOntology.getDataTypes()) {
                builder.append(dataType.getName());
                builder.append(" (");
                builder.append(dataType.getURI());
                builder.append(")\n");
            }
            for (IFlexoOntologyContainer<TA> subContainer : flexoOntology.getSubContainers()) {
                builder.append(toString(subContainer));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    /**
     * To String.
     *
     * @param container
     * @return
     */
    protected static <TA extends TechnologyAdapter<TA>> String toString(IFlexoOntologyContainer<TA> container) {
        StringBuilder builder = new StringBuilder();
        builder.append("Container : ");
        builder.append(container.getName());
        builder.append("\n");
        for (IFlexoOntologyConcept<TA> concept : container.getConcepts()) {
            builder.append(concept.accept(new ToStringVisitor()));
            builder.append("\n");
        }
        for (IFlexoOntologyDataType<TA> dataType : container.getDataTypes()) {
            builder.append(dataType.getName());
            builder.append(" (");
            builder.append(dataType.getURI());
            builder.append(")\n");
        }
        for (IFlexoOntologyContainer<TA> subContainer : container.getSubContainers()) {
            builder.append(toString(subContainer));
        }
        return builder.toString();
    }

    /**
     * Get All Concepts.
     *
     * @param container
     * @return
     */
    public static <TA extends TechnologyAdapter<TA>> List<IFlexoOntologyConcept<TA>> getAllConcepts(
            IFlexoOntologyConceptContainer<TA> container) {
        List<IFlexoOntologyConcept<TA>> result = new ArrayList<>();
        result.addAll(container.getConcepts());
        for (IFlexoOntologyConceptContainer<TA> subContainer : container.getSubContainers()) {
            result.addAll(getAllConcepts(subContainer));
        }
        return result;
    }

    /**
     * Get Class from uri.
     *
     * @param flexoOntology
     * @param uri
     * @return
     */
    public static <TA extends TechnologyAdapter<TA>> IFlexoOntologyClass<TA> getClass(IFlexoOntology<TA> flexoOntology, String uri) {
        IFlexoOntologyClass<TA> result = null;
        for (IFlexoOntologyConcept<TA> concept : getAllConcepts(flexoOntology)) {
            if (concept instanceof IFlexoOntologyClass) {
                if (concept.getURI().equalsIgnoreCase(uri)) {
                    result = (IFlexoOntologyClass<TA>) concept;
                }
            }
        }
        return result;
    }

    /**
     * Get Individual from uri.
     *
     * @param flexoOntology
     * @param uri
     * @return
     */
    public static <TA extends TechnologyAdapter<TA>> IFlexoOntologyIndividual<TA> getIndividual(IFlexoOntology<TA> flexoOntology,
                                                                                                String uri) {
        IFlexoOntologyIndividual<TA> result = null;
        for (IFlexoOntologyConcept<TA> concept : getAllConcepts(flexoOntology)) {
            if (concept instanceof IFlexoOntologyIndividual) {
                if (concept.getURI().equalsIgnoreCase(uri)) {
                    result = (IFlexoOntologyIndividual<TA>) concept;
                }
            }
        }
        return result;
    }

    /**
     * Get Individual of type.
     *
     * @param flexoOntology
     * @param uri
     * @return
     */
    public static <TA extends TechnologyAdapter<TA>> List<IFlexoOntologyIndividual<TA>> getIndividualOfType(
            IFlexoOntology<TA> flexoOntology, IFlexoOntologyClass<TA> emfClass) {
        List<IFlexoOntologyIndividual<TA>> result = new ArrayList<>();
        for (IFlexoOntologyConcept<TA> concept : getAllConcepts(flexoOntology)) {
            if (concept instanceof IFlexoOntologyIndividual) {
                if (((IFlexoOntologyIndividual<TA>) concept).isIndividualOf(emfClass)) {
                    result.add((IFlexoOntologyIndividual<TA>) concept);
                }
            }
        }
        return result;
    }

    /**
     * Get Feature from uri.
     *
     * @param flexoOntology
     * @param uri
     * @return
     */
    public static <TA extends TechnologyAdapter<TA>> IFlexoOntologyFeature<TA> getFeature(IFlexoOntology<TA> flexoOntology, String uri) {
        IFlexoOntologyFeature<TA> result = null;
        for (IFlexoOntologyConcept<TA> concept : getAllConcepts(flexoOntology)) {
            if (concept instanceof IFlexoOntologyFeature) {
                if (concept.getURI().equalsIgnoreCase(uri)) {
                    result = (IFlexoOntologyFeature<TA>) concept;
                }
            }
        }
        return result;
    }
}
