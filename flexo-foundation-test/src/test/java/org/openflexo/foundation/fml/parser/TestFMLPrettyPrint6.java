/**
 *
 * Copyright (c) 2014, Openflexo
 * <p>
 * This file is part of Cartoeditor, a component of the software infrastructure
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.DefaultFlexoEditor;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.FMLCompilationUnit;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.controlgraph.EmptyControlGraph;
import org.openflexo.foundation.fml.parser.fmlnodes.ActionSchemeNode;
import org.openflexo.foundation.fml.parser.fmlnodes.FMLCompilationUnitNode;
import org.openflexo.foundation.fml.parser.fmlnodes.VirtualModelNode;
import org.openflexo.foundation.test.parser.FMLParserTestCase;
import org.openflexo.p2pp.RawSource;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Parse a FML file, perform some edits and checks that pretty-print is correct
 *
 * @author sylvain
 *
 */
@RunWith(OrderedRunner.class)
public class TestFMLPrettyPrint6 extends FMLParserTestCase {

    static FlexoEditor editor;
    private static FMLCompilationUnit compilationUnit;
    private static VirtualModel virtualModel;
    private static ActionScheme behaviour1;
    private static VirtualModelNode vmNode;
    private static ActionSchemeNode behaviour1Node;

    @Test
    @TestOrder(1)
    public void initServiceManager() throws ParseException, ModelDefinitionException, IOException {
        instanciateTestServiceManager();

        editor = new DefaultFlexoEditor(null, serviceManager);
        assertNotNull(editor);

    }

    @Test
    @TestOrder(2)
    public void loadInitialVersion() throws ParseException, ModelDefinitionException, IOException {
        instanciateTestServiceManager();

        log("Initial version");

        final Resource fmlFile = ResourceLocator.locateResource("TestFMLPrettyPrint6/InitialModel.fml");
        compilationUnit = parseFile(fmlFile);
        assertNotNull(virtualModel = compilationUnit.getVirtualModel());
        assertEquals("TestViewPointA", virtualModel.getName());

        assertNotNull(rootNode = (FMLCompilationUnitNode) compilationUnit.getPrettyPrintDelegate());
        assertNotNull(vmNode = (VirtualModelNode) rootNode.getObjectNode(virtualModel));

        assertNotNull(behaviour1 = (ActionScheme) virtualModel.getFlexoBehaviour("firstBehaviour"));
        assertTrue(behaviour1.getControlGraph() instanceof EmptyControlGraph);
        assertNotNull(behaviour1Node = (ActionSchemeNode) rootNode.getObjectNode(behaviour1));

        System.out.println("FML=\n" + compilationUnit.getFMLPrettyPrint());
        System.out.println("Normalized=\n" + compilationUnit.getNormalizedFML());
        testNormalizedFMLRepresentationEquals(compilationUnit, "TestFMLPrettyPrint6/Step1Normalized.fml");

        System.out.println("FML=\n" + compilationUnit.getFMLPrettyPrint());
        testFMLPrettyPrintEquals(compilationUnit, "TestFMLPrettyPrint6/Step1PrettyPrint.fml");

        RawSource rawSource = rootNode.getRawSource();
        System.out.println(rawSource.debug());
        debug(rootNode, 0);

    }

    @Test
    @TestOrder(3)
    public void changeAbstractBehaviour() throws ParseException, IOException {

        log("changeAbstractBehaviour()");

        behaviour1.setAbstract(true);
        System.out.println("Normalized=\n" + compilationUnit.getNormalizedFML());
        testNormalizedFMLRepresentationEquals(compilationUnit, "TestFMLPrettyPrint6/Step2Normalized.fml");

        System.out.println("DEBUG PP:");
        System.out.println(behaviour1Node.debug());

        System.out.println("FML=\n" + compilationUnit.getFMLPrettyPrint());
        testFMLPrettyPrintEquals(compilationUnit, "TestFMLPrettyPrint6/Step2PrettyPrint.fml");

    }

}
