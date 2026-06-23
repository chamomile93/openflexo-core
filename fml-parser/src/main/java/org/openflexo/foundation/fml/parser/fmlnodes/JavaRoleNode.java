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

package org.openflexo.foundation.fml.parser.fmlnodes;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.foundation.InvalidNameException;
import org.openflexo.foundation.fml.JavaRole;
import org.openflexo.foundation.fml.parser.ExpressionFactory;
import org.openflexo.foundation.fml.parser.FMLCompilationUnitSemanticsAnalyzer;
import org.openflexo.foundation.fml.parser.TypeFactory;
import org.openflexo.foundation.fml.parser.node.AJavaInnerConceptDecl;
import org.openflexo.foundation.fml.parser.node.PExpression;

import java.util.logging.Logger;

/**
 * @author sylvain
 *
 */
public class JavaRoleNode extends BasicPropertyNode<JavaRole<?>> {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(JavaRoleNode.class.getPackage().getName());

    public JavaRoleNode(AJavaInnerConceptDecl astNode, FMLCompilationUnitSemanticsAnalyzer analyzer) {
        super(astNode, analyzer);
    }

    public JavaRoleNode(JavaRole<?> property, FMLCompilationUnitSemanticsAnalyzer analyzer) {
        super(property, analyzer);
    }

    @Override
    public void preparePrettyPrint(boolean hasParsedVersion) {
        super.preparePrettyPrint(hasParsedVersion);

        append(dynamicContents(() -> getVisibilityAsString(getModelObject().getVisibility()), SPACE), getVisibilityFragment());
        append(dynamicContents(() -> serializeType(getModelObject().getType())), getTypeFragment());
        append(dynamicContents(SPACE, () -> getModelObject().getName()), getNameFragment());
        when(() -> getModelObject().getDefaultValue().isSet()).thenAppend(staticContents(SPACE, "=", SPACE), getAssignFragment())
                .thenAppend(dynamicContents(() -> getModelObject().getDefaultValue().toString()), getDefaultValueFragment());
        append(staticContents(";"), getSemiFragment());
    }

    @Override
    public JavaRole<?> buildModelObjectFromAST(AJavaInnerConceptDecl astNode) {
        JavaRole<?> returned = getFactory().newJavaRole();
        returned.setVisibility(getVisibility(astNode.getVisibility()));
        try {
            returned.setName(getName(astNode.getVariableDeclarator()).getText());
        } catch (InvalidNameException e) {
            throwIssue("Invalid name: " + getName(astNode.getVariableDeclarator()).getText());
        }
        returned.setType(TypeFactory.makeType(astNode.getType(), getSemanticsAnalyzer().getTypingSpace()));

        PExpression initializerExpression = getInitializerExpression(astNode.getVariableDeclarator());
        if (initializerExpression != null) {
            DataBinding defaultValueExpression = ExpressionFactory.makeDataBinding(initializerExpression, returned,
                    BindingDefinitionType.GET, Object.class, getSemanticsAnalyzer(), this);
            returned.setDefaultValue(defaultValueExpression);
        }

        return returned;
    }

}
