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

import org.openflexo.foundation.fml.md.FMLMetaData;
import org.openflexo.foundation.fml.md.ListMetaData;
import org.openflexo.foundation.fml.parser.FMLCompilationUnitSemanticsAnalyzer;
import org.openflexo.foundation.fml.parser.node.AListAnnotationAnnotation;
import org.openflexo.p2pp.PrettyPrintContext.Indentation;
import org.openflexo.p2pp.RawSource.RawSourceFragment;

/**
 * @author sylvain
 *
 */
public class ListMetaDataNode extends AbstractMetaDataNode<AListAnnotationAnnotation, ListMetaData, FMLCompilationUnitSemanticsAnalyzer> {

    public ListMetaDataNode(AListAnnotationAnnotation astNode, FMLCompilationUnitSemanticsAnalyzer analyzer) {
        super(astNode, analyzer);
    }

    public ListMetaDataNode(ListMetaData metaData, FMLCompilationUnitSemanticsAnalyzer analyzer) {
        super(metaData, analyzer);
    }

    @Override
    public ListMetaData buildModelObjectFromAST(AListAnnotationAnnotation astNode) {
        String key = makeFullQualifiedIdentifier(astNode.getTag());

        ListMetaData returned = getFactory().newListMetaData(key);
        return returned;
    }

    @Override
    public void preparePrettyPrint(boolean hasParsedVersion) {
        super.preparePrettyPrint(hasParsedVersion);

        append(staticContents("@"), getAtFragment());
        append(dynamicContents(() -> getModelObject().getKey()), getKeyFragment());
        append(staticContents("("), getLParFragment());
        append(childrenContents(LINE_SEPARATOR, "", () -> getModelObject().getMetaDataList(), LINE_SEPARATOR, LINE_SEPARATOR,
                Indentation.Indent, FMLMetaData.class));
        append(staticContents(")"), getRParFragment());
    }

    private RawSourceFragment getKeyFragment() {
        if (getASTNode() != null) {
            return getFragment(getASTNode().getTag());
        }
        return null;
    }

    private RawSourceFragment getAtFragment() {
        if (getASTNode() != null) {
            return getFragment(getASTNode().getAt());
        }
        return null;
    }

    protected RawSourceFragment getLParFragment() {
        if (getASTNode() != null) {
            return getFragment(getASTNode().getLPar());
        }
        return null;
    }

    protected RawSourceFragment getRParFragment() {
        if (getASTNode() != null) {
            return getFragment(getASTNode().getRPar());
        }
        return null;
    }

}
