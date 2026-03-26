package studentPostsView;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Simple form for making a post.
 */
public class CreatePostPane extends VBox {

	private final TextField titleField;
	private final TextField threadField;
	private final TextArea bodyArea;
	private final Button createButton;
	private final Button clearButton;

	/**
	 * Builds the create form.
	 */
	public CreatePostPane() {
		setSpacing(8);
		setPadding(new Insets(12));

		Label heading = new Label("Create Post");

		titleField = new TextField();
		titleField.setPromptText("Post title");

		threadField = new TextField("General");
		threadField.setPromptText("Thread");

		bodyArea = new TextArea();
		bodyArea.setPromptText("Write your post here");
		bodyArea.setWrapText(true);
		bodyArea.setPrefRowCount(8);

		createButton = new Button("Create Post");
		clearButton = new Button("Clear");

		getChildren().addAll(
				heading,
				new Label("Title"),
				titleField,
				new Label("Thread"),
				threadField,
				new Label("Body"),
				bodyArea,
				createButton,
				clearButton);
	}

	/**
	 * Clears the fields.
	 */
	public void clearFields() {
		titleField.clear();
		threadField.setText("General");
		bodyArea.clear();
	}

	public TextField getTitleField() {
		return titleField;
	}

	public TextField getThreadField() {
		return threadField;
	}

	public TextArea getBodyArea() {
		return bodyArea;
	}

	public Button getCreateButton() {
		return createButton;
	}

	public Button getClearButton() {
		return clearButton;
	}
}
