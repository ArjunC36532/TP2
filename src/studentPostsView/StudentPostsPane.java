package studentPostsView;

import entityClasses.Post;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Main pane for the student posts screen.
 */
public class StudentPostsPane extends BorderPane {

	private final TextField searchField;
	private final ComboBox<String> threadFilter;
	private final Button searchButton;
	private final Button clearSearchButton;
	private final ListView<Post> postList;
	private final CreatePostPane createPane;
	private final PostDetailPane detailPane;
	private final Label statusLabel;

	/**
	 * Builds the main posts pane.
	 */
	public StudentPostsPane() {
		setPadding(new Insets(10));

		Label titleLabel = new Label("Student Posts");
		titleLabel.setStyle("-fx-font-size: 18px;");

		searchField = new TextField();
		searchField.setPromptText("Search posts");

		threadFilter = new ComboBox<String>();
		threadFilter.setPrefWidth(170);

		searchButton = new Button("Search");
		clearSearchButton = new Button("Clear");

		HBox controls = new HBox(8, new Label("Keyword"), searchField,
				new Label("Thread"), threadFilter, searchButton, clearSearchButton);
		controls.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(searchField, Priority.ALWAYS);

		VBox topBox = new VBox(8, titleLabel, controls);
		setTop(topBox);

		postList = new ListView<Post>();
		postList.setPrefWidth(380);
		postList.setCellFactory(listView -> new ListCell<Post>() {
			@Override
			protected void updateItem(Post item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item.getTitle() + " | " + item.getThread() + " | " + item.getAuthor());
				}
			}
		});

		createPane = new CreatePostPane();
		detailPane = new PostDetailPane();
		VBox rightPane = new VBox(10, createPane, detailPane);

		HBox centerBox = new HBox(10, postList, rightPane);
		HBox.setHgrow(postList, Priority.ALWAYS);
		setCenter(centerBox);

		statusLabel = new Label("Ready.");
		setBottom(statusLabel);
		BorderPane.setMargin(statusLabel, new Insets(10, 0, 0, 0));
	}

	public TextField getSearchField() {
		return searchField;
	}

	public ComboBox<String> getThreadFilter() {
		return threadFilter;
	}

	public Button getSearchButton() {
		return searchButton;
	}

	public Button getClearSearchButton() {
		return clearSearchButton;
	}

	public ListView<Post> getPostList() {
		return postList;
	}

	public CreatePostPane getCreatePane() {
		return createPane;
	}

	public PostDetailPane getDetailPane() {
		return detailPane;
	}

	public Label getStatusLabel() {
		return statusLabel;
	}
}
