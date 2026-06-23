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

package org.openflexo.foundation.fml.parser;

import org.openflexo.foundation.fml.FlexoBehaviour;
import org.openflexo.foundation.fml.parser.fmlnodes.*;
import org.openflexo.foundation.fml.parser.node.*;

import java.util.logging.Logger;

/**
 * Handle {@link FlexoBehaviour} in the FML parser<br>
 *
 * @author sylvain
 *
 */
public class FlexoBehaviourFactory extends SemanticsAnalyzerFactory {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(FlexoBehaviourFactory.class.getPackage().getName());

    public FlexoBehaviourFactory(FMLCompilationUnitSemanticsAnalyzer analyzer) {
        super(analyzer);
    }

    FlexoBehaviourNode<?, ?> makeBehaviourNode(PBehaviourDecl node) {
        if (node instanceof AAnonymousConstructorBehaviourDecl) {
            return new CreationSchemeNode(node, getAnalyzer());
        } else if (node instanceof ANamedConstructorBehaviourDecl) {
            return new CreationSchemeNode(node, getAnalyzer());
        } else if (node instanceof AAnonymousDestructorBehaviourDecl) {
            return new DeletionSchemeNode(node, getAnalyzer());
        } else if (node instanceof ANamedDestructorBehaviourDecl) {
            return new DeletionSchemeNode(node, getAnalyzer());
        } else if (node instanceof AFmlBehaviourDecl) { // fml.sablecc l 411/412
            return new FMLBehaviourNode(node, getAnalyzer());
        } else if (node instanceof AFmlFullyQualifiedBehaviourDecl) {
            return new FMLBehaviourNode(node, getAnalyzer());
        } else if (node instanceof AListenerBehaviourDecl) {
            return new EventListenerNode((AListenerBehaviourDecl) node, getAnalyzer());
        } else if (node instanceof AMethodBehaviourDecl) {
            return new ActionSchemeNode((AMethodBehaviourDecl) node, getAnalyzer());
        }
        logger.warning("Unexpected node: " + node + " of " + node.getClass());
        Thread.dumpStack();
        return null;
    }

}
