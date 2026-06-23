package org.openflexo.foundation.lsp.server.providers;


import org.eclipse.lsp4j.*;
import org.openflexo.foundation.fml.FMLKeywords;
import org.openflexo.foundation.lsp.languageServer.utils.TextUtils;

import java.util.Map;
import java.util.concurrent.CompletableFuture;


/**
 * Provides hover information for FML language elements.
 * <p>
 * Generates contextual documentation or details displayed when
 * the user hovers over language keywords or symbols in the editor.
 * <p>
 * This class is responsible for identifying relevant text under the cursor
 * and returning appropriate hover content to assist the user.
 */
public class HoverProvider {

    public HoverProvider() {

    }


    public CompletableFuture<Hover> provide(HoverParams params, Map<String, String> documents) {
        String uri = params.getTextDocument().getUri();
        String text = documents.get(uri);

        if (text == null) {
            return CompletableFuture.completedFuture(null);
        }

        Position pos = params.getPosition();
        String word = TextUtils.extractWordAt(text, pos.getLine(), pos.getCharacter());

        if (TextUtils.isBlank(word)) {
            return CompletableFuture.completedFuture(null);
        }

        if (FMLKeywords.isKeyword(word)) {
            MarkupContent content = new MarkupContent();
            content.setKind(MarkupKind.MARKDOWN);
            content.setValue("Mot-clé FML\n" +
                    "`" + word + "`\n" +
                    "_Ceci est un mot-clé du langage FML._");

            Hover hover = new Hover(content);
            return CompletableFuture.completedFuture(hover);
        }

        return CompletableFuture.completedFuture(null);
    }


}
