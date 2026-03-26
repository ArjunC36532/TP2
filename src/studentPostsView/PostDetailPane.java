package studentPostsView;

import entityClasses.Post;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Shows the selected post and its buttons.
 */
public class PostDetailPane extends VBox {

	private final Label idLabel;
	private final Label authorLabel;
	private final TextField titleField;
	private final TextField threadField;
	private final TextArea bodyArea;
	private final Button updateButton;
	private final Button deleteButton;
	private final Button closeButton;

	/**
	 * Builds the post details area.
	 */
	public PostDetailPane() {
		setSpacing(8);
		setPadding(new Insets(12));
		setPrefWidth(320);

		Label heading = new Label("Selected Post");

		idLabel = new Label("Post ID: none");
		authorLabel = new Label("Author: none");

		titleField = new TextField();
		threadField = new TextField();

		bodyArea = new TextArea();
		bodyArea.setWrapText(true);
		bodyArea.setPrefRowCount(10);

		updateButton = new Button("Update Post");
		deleteButton = new Button("Delete Post");
		closeButton = new Button("Close");

		getChildren().addAll(
				heading,
				idLabel,
				authorLabel,
				new Label("Title"),
				titleField,
				new Label("Thread"),
				threadField,
				new Label("Body"),
				bodyArea,
				updateButton,
				deleteButton,
				closeButton);
	}

	/**
	 * Fills in the selected post.
	 */
	public void showPost(Post post, String currentUser) {
		if (post == null) {
			idLabel.setText("Post ID: none");
			authorLabel.setText("Author: none");
			titleField.clear();
			threadField.clear();
			bodyArea.clear();
			updateButton.setDisable(true);
			deleteButton.setDisable(true);
			return;
		}

		idLabel.setText("Post ID: " + post.getPostID());
		authorLabel.setText("Author: " + post.getAuthor());
		titleField.setText(post.getTitle());
		threadField.setText(post.getThread());
		bodyArea.setText(post.getBody());

		boolean canEdit = currentUser != null && currentUser.equals(post.getAuthor());
		updateButton.setDisable(!canEdit);
		deleteButton.setDisable(!canEdit);
	}

	public Label getIdLabel() {
		return idLabel;
	}

	public Label getAuthorLabel() {
		return authorLabel;
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

	public Button getUpdateButton() {
		return updateButton;
	}

	public Button getDeleteButton() {
		return deleteButton;
	}

	public Button getCloseButton() {
		return closeButton;
	}
}
