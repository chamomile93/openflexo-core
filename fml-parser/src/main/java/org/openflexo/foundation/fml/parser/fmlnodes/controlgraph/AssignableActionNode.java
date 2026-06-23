/**
 *
 * Copyright (c) 2019, Openflexo
 * <p>
 * This file is part of FML-parser, a component of the software infrastructure
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

package org.openflexo.foundation.fml.parser.fmlnodes.controlgraph;

import org.openflexo.foundation.fml.editionaction.AbstractAssignationAction;
import org.openflexo.foundation.fml.editionaction.AssignableAction;
import org.openflexo.foundation.fml.parser.FMLCompilationUnitSemanticsAnalyzer;
import org.openflexo.foundation.fml.parser.node.Node;

import java.util.logging.Logger;

/**
 * @author sylvain
 *
 */
public abstract class AssignableActionNode<N extends Node, T extends AssignableAction<?>> extends ControlGraphNode<N, T> {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(AssignableActionNode.class.getPackage().getName());

    public AssignableActionNode(N astNode, FMLCompilationUnitSemanticsAnalyzer analyzer) {
        super(astNode, analyzer);
    }

    public AssignableActionNode(T property, FMLCompilationUnitSemanticsAnalyzer analyzer) {
        super(property, analyzer);
    }

    /**
     * Return boolean indicating if this node requires a final semi in its pretty-print
     *
     * @return
     */
    protected boolean requiresSemi() {
        // We define here the conditions under which this action requires a final semi

        return !((getParent().getModelObject() instanceof AbstractAssignationAction)
                // If parent id AbstractAssignationAction, the parent will do it
                || ((getParent() instanceof IterationActionNode) && getIndex() == 0)
                || ((getParent() instanceof ExpressionIterationActionNode) && getIndex() == 2));
        // For iteration, only first element should avoid final semi
    }

}
