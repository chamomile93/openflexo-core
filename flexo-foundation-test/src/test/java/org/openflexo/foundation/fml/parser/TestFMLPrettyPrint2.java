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
import org.openflexo.foundation.fml.FMLCompilationUnit;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.CreateFlexoConcept;
import org.openflexo.foundation.test.parser.FMLParserTestCase;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Parse a FML file, perform some edits and checks that pretty-print is correct
 *
 * @author sylvain
 *
 */
@RunWith(OrderedRunner.class)
public class TestFMLPrettyPrint2 extends FMLParserTestCase {

    static FlexoEditor editor;
    private static FMLCompilationUnit compilationUnit;
    private static VirtualModel virtualModel;
    private static FlexoConcept conceptA;

    @Test
    @TestOrder(1)
    public void initServiceManager() throws ParseException, ModelDefinitionException, IOException {
        instanciateTestServiceManager();

        editor = new DefaultFlexoEditor(null, serviceManager);
        assertNotNull(editor);

        final Resource fmlFile = ResourceLocator.locateResource("TestFMLPrettyPrint2/TestBasicTypesWithComments.fml");
        compilationUnit = parseFile(fmlFile);
        assertNotNull(virtualModel = compilationUnit.getVirtualModel());
        assertEquals("MyModel", virtualModel.getName());
        // assertEquals(2, virtualModel.getFlexoConcepts().size());
        // conceptA = virtualModel.getFlexoConcepts().get(0);
        // assertEquals("ConceptA", conceptA.getName());

        System.out.println("Normalized:");
        System.out.println(compilationUnit.getPrettyPrintDelegate()
                .getNormalizedRepresentation(compilationUnit.getPrettyPrintDelegate().makePrettyPrintContext()));

        System.out.println("Current FML");
        System.out.println(">>>>>>>>>>>>>>>>" + compilationUnit.getPrettyPrintDelegate()
                .getRepresentation(compilationUnit.getPrettyPrintDelegate().makePrettyPrintContext()) + "<<<<<<<<<<<<<<");
    }

	/*@Test
	@TestOrder(2)
	public void changeVirtualModelName() {
		log("Change name to AnOtherName");
		assertEquals("MyModel", virtualModel.getName());
		virtualModel.setName("AnOtherName");
		System.out.println("FML=\n" + compilationUnit.getFMLPrettyPrint());
	}*/

    @Test
    @TestOrder(3)
    public void addFlexoConcept() {
        log("addFlexoConcept");

        CreateFlexoConcept addConceptC = CreateFlexoConcept.actionType.makeNewAction(virtualModel, null, editor);
        addConceptC.setNewFlexoConceptName("FlexoConceptC");
        addConceptC.doAction();

        FlexoConcept conceptC = addConceptC.getNewFlexoConcept();

        System.out.println("Normalized:");
        System.out.println(compilationUnit.getPrettyPrintDelegate()
                .getNormalizedRepresentation(compilationUnit.getPrettyPrintDelegate().makePrettyPrintContext()));

        System.out.println("Current FML");
        System.out.println(">>>>>>>>>>>>>>>>" + compilationUnit.getPrettyPrintDelegate()
                .getRepresentation(compilationUnit.getPrettyPrintDelegate().makePrettyPrintContext()) + "<<<<<<<<<<<<<<");
    }
}
