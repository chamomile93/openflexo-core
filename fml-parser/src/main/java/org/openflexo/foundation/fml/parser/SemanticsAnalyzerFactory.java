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

import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.fml.*;
import org.openflexo.foundation.fml.parser.node.Node;
import org.openflexo.p2pp.RawSource.RawSourceFragment;
import org.openflexo.toolbox.ChainedCollection;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Handle {@link FlexoProperty} in the FML parser<br>
 *
 * @author sylvain
 *
 */
public abstract class SemanticsAnalyzerFactory {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(SemanticsAnalyzerFactory.class.getPackage().getName());

    private FMLCompilationUnitSemanticsAnalyzer analyzer;

    public SemanticsAnalyzerFactory(FMLCompilationUnitSemanticsAnalyzer analyzer) {
        super();
        this.analyzer = analyzer;
    }

    public FMLCompilationUnitSemanticsAnalyzer getAnalyzer() {
        return analyzer;
    }

    public FMLModelFactory getFactory() {
        return getAnalyzer().getModelFactory();
    }

    public FragmentManager getFragmentManager() {
        return getAnalyzer().getFragmentManager();
    }

	/*public TypeFactory getTypeFactory() {
		return getAnalyzer().getTypeFactory();
	}*/

    public FlexoServiceManager getServiceManager() {
        return getAnalyzer().getServiceManager();
    }

    public FMLCompilationUnit getCompilationUnit() {
        return getAnalyzer().getCompilationUnit();
    }

    public VirtualModel getVirtualModel() {
        return getAnalyzer().getCompilationUnit().getVirtualModel();
    }

    public FMLTechnologyAdapter getFMLTechnologyAdapter() {
        if (getServiceManager() != null) {
            return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(FMLTechnologyAdapter.class);
        }
        return null;
    }

    /**
     * Return fragment matching supplied node in AST
     *
     * @param token
     * @return
     */
    public RawSourceFragment getFragment(Node node) {
        return analyzer.getFragmentManager().retrieveFragment(node);
    }

    /**
     * Return fragment matching supplied nodes in AST
     *
     * @param token
     * @return
     */
    public RawSourceFragment getFragment(Node node, Node otherNode) {
        return getFragment(node, Collections.singletonList(otherNode));
    }

    /**
     * Return fragment matching supplied nodes in AST
     *
     * @param token
     * @return
     */
    public RawSourceFragment getFragment(Node node, List<? extends Node> otherNodes) {
        ChainedCollection<Node> collection = new ChainedCollection<>();
        collection.add(node);
        collection.add(otherNodes);
        return analyzer.getFragmentManager().getFragment(collection);
    }

    public String getText(Node node) {
        return getFragment(node).getRawText();
    }

}
