package org.openflexo.foundation.resource;

import org.openflexo.pamela.PamelaMetaModelLibrary;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.pamela.factory.PamelaModelFactory;

import java.io.File;

public class GitIODelegateFactory implements IODelegateFactory<File> {

    @Override
    public FlexoIODelegate<File> makeNewInstance(FlexoResource<?> resource) {
        PamelaModelFactory factory;
        GitIODelegate gitIODelegate = null;
        try {
            factory = new PamelaModelFactory(PamelaMetaModelLibrary.retrieveMetaModel(GitIODelegate.class));
            gitIODelegate = factory.newInstance(GitIODelegate.class);
        } catch (ModelDefinitionException e) {
            e.printStackTrace();
        }
        return gitIODelegate;
    }

}
