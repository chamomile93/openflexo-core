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

import org.openflexo.foundation.fml.FMLInstancePropertyValue;
import org.openflexo.foundation.fml.FMLModelContext.FMLProperty;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.fml.parser.FMLCompilationUnitSemanticsAnalyzer;
import org.openflexo.foundation.fml.parser.node.AInstanceQualifiedArgument;
import org.openflexo.p2pp.PrettyPrintContext.Indentation;
import org.openflexo.p2pp.RawSource.RawSourceFragment;

import java.util.logging.Logger;

/**
 *
 * <pre>
 *     {instance}       [arg_name]:identifier assign qualified_instance
 * </pre>
 *
 * @author sylvain
 *
 */
public class FMLInstancePropertyValueNode<M extends FMLObject, T extends FMLObject>
        extends AbstractFMLPropertyValueNode<AInstanceQualifiedArgument, FMLInstancePropertyValue<M, T>, M, T> {

    private static final Logger logger = Logger.getLogger(FMLInstancePropertyValueNode.class.getPackage().getName());

    public FMLInstancePropertyValueNode(AInstanceQualifiedArgument astNode, FMLCompilationUnitSemanticsAnalyzer analyzer) {
        super(astNode, analyzer);
    }

    public FMLInstancePropertyValueNode(FMLInstancePropertyValue<M, T> propertyValue, FMLCompilationUnitSemanticsAnalyzer analyzer) {
        super(propertyValue, analyzer);
    }

    @Override
    public FMLInstancePropertyValueNode<M, T> deserialize() {

        String propertyName = getASTNode().getArgName().getText();

        FMLProperty fmlProperty = ((FMLObject) getParent().getModelObject()).getFMLProperty(propertyName, getFactory());
        if (fmlProperty == null) {
            getModelObject().setUnresolvedPropertyName(propertyName);
            logger.warning("Cannot find FML property " + propertyName + " in " + getParent().getModelObject());
        } else {
            getModelObject().setProperty(fmlProperty);
        }
        return (FMLInstancePropertyValueNode<M, T>) super.deserialize();
    }

    @Override
    public FMLInstancePropertyValue<M, T> buildModelObjectFromAST(AInstanceQualifiedArgument astNode) {
        return (FMLInstancePropertyValue<M, T>) getFactory().newInstancePropertyValue();
    }

    @Override
    public void preparePrettyPrint(boolean hasParsedVersion) {
        super.preparePrettyPrint(hasParsedVersion);

        append(dynamicContents(() -> getModelObject().getPropertyName()), getArgNameFragment());
        append(staticContents("="), getAssignFragment());
        append(childContents("", () -> getModelObject().getInstance(), "", Indentation.DoNotIndent));
    }

    private RawSourceFragment getArgNameFragment() {
        if (getASTNode() != null) {
            return getFragment(getASTNode().getArgName());
        }
        return null;
    }

    private RawSourceFragment getAssignFragment() {
        if (getASTNode() != null) {
            return getFragment(getASTNode().getAssign());
        }
        return null;
    }
}
