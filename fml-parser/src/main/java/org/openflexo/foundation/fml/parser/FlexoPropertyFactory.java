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

import org.openflexo.connie.type.TypeUtils;
import org.openflexo.connie.type.UnresolvedType;
import org.openflexo.foundation.fml.FlexoConceptInstanceType;
import org.openflexo.foundation.fml.FlexoProperty;
import org.openflexo.foundation.fml.VirtualModelInstanceType;
import org.openflexo.foundation.fml.parser.fmlnodes.*;
import org.openflexo.foundation.fml.parser.node.*;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Handle {@link FlexoProperty} in the FML parser<br>
 *
 * @author sylvain
 *
 */
public class FlexoPropertyFactory extends SemanticsAnalyzerFactory {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(FlexoPropertyFactory.class.getPackage().getName());

    public FlexoPropertyFactory(FMLCompilationUnitSemanticsAnalyzer analyzer) {
        super(analyzer);
    }

    AbstractPropertyNode makeAbstractPropertyNode(AAbstractPropertyInnerConceptDecl node) {
        return new AbstractPropertyNode(node, getAnalyzer());
    }

    FlexoPropertyNode<?, ?> makeBasicPropertyNode(AJavaInnerConceptDecl node) {
        Type type = TypeFactory.makeType(node.getType(), getAnalyzer().getTypingSpace());

        if (type == null) {
            logger.warning("Cannot find type for " + node + " of " + node.getClass());
            type = new UnresolvedType(node.toString());
        }

        if (type instanceof VirtualModelInstanceType) {
            return new ModelSlotPropertyNode(node, getAnalyzer());
        } else if (type instanceof FlexoConceptInstanceType) {
            return new FlexoRolePropertyNode(node, getAnalyzer());
        }

        if (TypeUtils.isPrimitive(type) || type.equals(String.class) || type.equals(Date.class)) {
            return new PrimitiveRoleNode(node, getAnalyzer());
        } else {
            return new JavaRoleNode(node, getAnalyzer());
        }
    }

    ModelSlotPropertyNode<?, ?> makeModelSlotPropertyNode(AFmlFullyQualifiedInnerConceptDecl node) {
        return new ModelSlotPropertyNode(node, getAnalyzer());
    }

    ModelSlotPropertyNode<?, ?> makeModelSlotPropertyNode(AFmlInnerConceptDecl node) {
        return new ModelSlotPropertyNode(node, getAnalyzer());
    }

    FlexoRolePropertyNode<?, ?> makeFlexoRolePropertyNode(AFmlFullyQualifiedInnerConceptDecl node) {
        return new FlexoRolePropertyNode(node, getAnalyzer());
    }

    FlexoRolePropertyNode<?, ?> makeFlexoRolePropertyNode(AFmlInnerConceptDecl node) {
        return new FlexoRolePropertyNode(node, getAnalyzer());
    }

    ExpressionPropertyNode makeExpressionPropertyNode(AExpressionPropertyInnerConceptDecl node) {
        return new ExpressionPropertyNode(node, getAnalyzer());
    }

    GetSetPropertyNode makeGetSetPropertyNode(AGetSetPropertyInnerConceptDecl node) {
        return new GetSetPropertyNode(node, getAnalyzer());
    }

	/*FlexoPropertyNode<?, ?> makePropertyNode(PInnerConceptDecl node) {
		if (node instanceof ABasicPropertyPropertyDeclaration) {
			PBasicRoleDeclaration basicRoleDeclaration = ((ABasicPropertyPropertyDeclaration) node).getBasicRoleDeclaration();
			if (basicRoleDeclaration instanceof AJavaBasicRoleDeclaration) {
				Type type = getTypeFactory().makeType(((AJavaBasicRoleDeclaration) basicRoleDeclaration).getType());
				// System.out.println("Tiens une basic property declaration java: " + node + " type=" + type);
				if (getTypeFactory().getPrimitiveType(type) != null) {
					return new PrimitiveRoleNode((AJavaBasicRoleDeclaration) basicRoleDeclaration, getAnalyzer());
				}
				else {
					return new JavaRoleNode((AJavaBasicRoleDeclaration) basicRoleDeclaration, getAnalyzer());
				}
			}
			else if (basicRoleDeclaration instanceof AFmlBasicRoleDeclaration) {
				// System.out.println("Tiens une basic property declaration FML: " + node);
			}
			else if (basicRoleDeclaration instanceof AFmlFullyQualifiedBasicRoleDeclaration) {
				// System.out.println("Tiens une basic property declaration FML fully-qualified: " + node);
			}
			return new JavaRoleNode((AJavaInnerConceptDecl) node, (FMLSemanticsAnalyzer) this);
		}
		else if (node instanceof AFmlInnerConceptDecl) {
			// System.out.println("Tiens une basic property declaration FML: " + node);
		}
		else if (node instanceof AFmlFullyQualifiedInnerConceptDecl) {
			// System.out.println("Tiens une basic property declaration FML fully-qualified: " + node);
		}
		else if (node instanceof AExpressionPropertyPropertyDeclaration) {
			PExpressionPropertyDeclaration expressionPropertyDeclaration = ((AExpressionPropertyPropertyDeclaration) node)
					.getExpressionPropertyDeclaration();
			return new ExpressionPropertyNode(expressionPropertyDeclaration, getAnalyzer());
	
		}
		else if (node instanceof AGetSetPropertyPropertyDeclaration) {
			AGetSetPropertyDeclaration getSetPropertyDeclaration = (AGetSetPropertyDeclaration) ((AGetSetPropertyPropertyDeclaration) node)
					.getGetSetPropertyDeclaration();
			return new GetSetPropertyNode(getSetPropertyDeclaration, getAnalyzer());
	
		}
		logger.warning("Unexpected node: " + node + " of " + node.getClass());
		Thread.dumpStack();
		return null;
	}*/
}
