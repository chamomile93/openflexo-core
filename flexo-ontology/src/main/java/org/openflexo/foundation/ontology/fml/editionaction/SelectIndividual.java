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

import org.openflexo.foundation.fml.editionaction.FetchRequest;
import org.openflexo.foundation.ontology.IFlexoOntologyIndividual;
import org.openflexo.foundation.technologyadapter.FlexoModel;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;

import java.util.List;

/**
 * A {@link FetchRequest} allowing to retrieve a selection of some individuals matching some conditions and a given type.<br>
 * This action is technology-specific and must be redefined in a given technology
 *
 * @author sylvain
 *
 * @param <M>
 * @param <MM>
 * @param <T>
 */
@ModelEntity(isAbstract = true)
//TODO idf why the annotation was referencing "SelectIndividual.AbstractSelectIndividualImpl.class". Maybe this is resolved in the code ? but the eclipse compiler was complaining.
@ImplementationClass(SelectIndividual.AbstractSelectIndividualImpl.class)
public abstract interface SelectIndividual<MS extends TypeAwareModelSlot<M, ?, ?>, M extends FlexoModel<M, ?> & TechnologyObject<?>, T extends IFlexoOntologyIndividual>
        extends AbstractSelectIndividual<MS, M, T, List<T>>, FetchRequest<MS, M, T> {

}
