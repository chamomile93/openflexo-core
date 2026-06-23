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

import org.openflexo.foundation.fml.parser.analysis.DepthFirstAdapter;
import org.openflexo.foundation.fml.parser.node.Node;
import org.openflexo.foundation.fml.parser.node.Token;
import org.openflexo.p2pp.FragmentRetriever;
import org.openflexo.p2pp.RawSource;
import org.openflexo.p2pp.RawSource.RawSourceFragment;
import org.openflexo.p2pp.RawSource.RawSourcePosition;

import java.util.Collection;

/**
 *
 *
 * @author sylvain
 *
 */
public class FragmentManager extends DepthFirstAdapter implements FragmentRetriever<Node> {

    // Raw source as when this analyzer was last parsed
    private RawSource rawSource;

    private RawSourcePosition startPosition;
    private RawSourcePosition endPosition;

    public FragmentManager(RawSource rawSource) {
        this.rawSource = rawSource;
    }

    public RawSource getRawSource() {
        return rawSource;
    }

    @Override
    public RawSourceFragment retrieveFragment(Node node) {
        if (node == null) {
            return null;
        }
        if (getRawSource() == null) {
            return null;
        }
        startPosition = null;
        endPosition = null;
        node.apply(this);
        return getRawSource().makeFragment(startPosition, endPosition);
    }

    public RawSourceFragment getFragment(Collection<? extends Node> nodes) {
        startPosition = null;
        endPosition = null;
        for (Node node : nodes) {
            node.apply(this);
        }
        return getRawSource().makeFragment(startPosition, endPosition);
    }

    @Override
    public void defaultCase(Node node) {
        super.defaultCase(node);
        if (node instanceof Token) {
            handleToken((Token) node);
        }
    }

    private void handleToken(Token token) {

        if (getRawSource() == null) {
            return;
        }

        // System.out.println("Receiving Token " + token.getLine() + ":" + token.getPos() + ":" + token.getText() + " tokenEnd=" + tokenEnd
        // + " endPosition=" + endPosition);

        RawSourcePosition tokenStart = getRawSource().makePositionBeforeChar(token.getLine(), token.getPos());
        RawSourcePosition tokenEnd = getRawSource().makePositionBeforeChar(token.getLine(), token.getPos() + token.getText().length());

        if (startPosition == null || tokenStart.compareTo(startPosition) < 0) {
            startPosition = tokenStart;
        }
        if (endPosition == null || tokenEnd.compareTo(endPosition) > 0) {
            endPosition = tokenEnd;
        }
    }

}
