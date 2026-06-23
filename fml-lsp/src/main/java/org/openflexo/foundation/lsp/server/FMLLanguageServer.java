package org.openflexo.foundation.lsp.server;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.*;

import java.util.concurrent.CompletableFuture;

/**
 * Main entry point for the FML Language Server implementation.
 * Acts as a coordinator between the language client and the underlying
 * text document and workspace services.
 * <p>
 * This class declares the server capabilities and delegates most
 * of the language logic to {@link FMLTextDocumentService} and {@link FMLWorkspaceService}.
 */

public class FMLLanguageServer implements LanguageServer, LanguageClientAware {

    private TextDocumentService textDocumentService;
    private WorkspaceService workspaceService;
    private LanguageClient client;
    private int errorCode = 1;

    public FMLLanguageServer() {
        this.textDocumentService = new FMLTextDocumentService();
        this.workspaceService = new FMLWorkspaceService();
    }

    /**
     * Establishes the connection with the LSP client.
     *
     * @param client the connected client (editor or IDE)
     */
    @Override
    public void connect(LanguageClient client) {
        this.client = client;
        ((FMLTextDocumentService) textDocumentService).connect(this.client);
    }

    /**
     * Initializes the server with supported capabilities (completion, hover, rename, etc.).
     *
     * @param params initialization parameters received from the client
     * @return the server capabilities
     */
    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
        ServerCapabilities capabilities = new ServerCapabilities();

        capabilities.setTextDocumentSync(TextDocumentSyncKind.Full); // Full document synchronization

        capabilities.setCompletionProvider(new CompletionOptions()); // Completion support

        capabilities.setHoverProvider(true);                         // Hover support

        capabilities.setRenameProvider(true);                        // Rename support

        return CompletableFuture.completedFuture(new InitializeResult(capabilities));
    }

    /**
     * Handles server shutdown.
     */
    @Override
    public CompletableFuture<Object> shutdown() {
        errorCode = 0;
        return null;
    }

    @Override
    public void exit() {
        System.exit(errorCode);
    }


    @Override
    public TextDocumentService getTextDocumentService() {
        return ((FMLTextDocumentService) textDocumentService);
    }


    @Override
    public WorkspaceService getWorkspaceService() {
        return workspaceService;
    }

}
