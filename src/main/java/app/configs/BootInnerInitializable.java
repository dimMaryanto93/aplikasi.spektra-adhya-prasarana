package app.configs;

import java.io.IOException;

import javafx.scene.Node;

public interface BootInnerInitializable extends BootInitializable {

	public Node initNode() throws IOException;

}
