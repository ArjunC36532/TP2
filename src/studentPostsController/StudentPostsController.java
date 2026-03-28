package studentPostsController;

import entityClasses.Post;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import studentPostsModel.StudentPostsModel;
import studentPostsView.StudentPostsPane;

/**
 * Controller for the student posts screen.
 */
public class StudentPostsController {

	private final StudentPostsModel model;
	private final StudentPostsPane view;
	private final Stage stage;

	/**
	 * Sets up the controller for one posts window.
	 */
	public StudentPostsController(Stage stage, String currentUser, Post selectedPost) {
		this.stage = stage;
		this.model = new StudentPostsModel(currentUser);
		this.view = new StudentPostsPane();

		bindActions();
		refreshView();

		if (selectedPost != null) {
			model.selectPostByID(selectedPost.getPostID());
			refreshSelectionInView();
		}

		Scene scene = new Scene(view, 980, 640);
		stage.setScene(scene);
		stage.setTitle("Student Posts");
	}

	/**
	 * Opens the posts window.
	 */
	public static void display(Stage owner, String currentUser, Post selectedPost, Runnable onCloseRefresh) {
		Stage stage = new Stage();
		stage.initOwner(owner);
		new StudentPostsController(stage, currentUser, selectedPost);
		if (onCloseRefresh != null) {
			stage.setOnHidden(event -> onCloseRefresh.run());
		}
		stage.show();
	}

	private void bindActions() {
		view.getSearchButton().setOnAction(event -> {
			model.setSearchText(view.getSearchField().getText());
			refreshView();
		});

		view.getClearSearchButton().setOnAction(event -> {
			view.getSearchField().clear();
			model.setSearchText("");
			refreshView();
		});

		view.getThreadFilter().setOnAction(event -> {
			model.setCurrentThread(view.getThreadFilter().getValue());
			refreshView();
		});

		view.getPostList().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			model.setSelectedPost(newValue);
			refreshSelectionInView();
		});

		view.getCreatePane().getCreateButton().setOnAction(event -> handleCreate());
		view.getCreatePane().getClearButton().setOnAction(event -> view.getCreatePane().clearFields());

		view.getDetailPane().getUpdateButton().setOnAction(event -> handleUpdate());
		view.getDetailPane().getDeleteButton().setOnAction(event -> handleDelete());
		view.getDetailPane().getCloseButton().setOnAction(event -> stage.close());
	}

	private void handleCreate() {
		boolean created = model.createPost(
				view.getCreatePane().getTitleField().getText(),
				view.getCreatePane().getBodyArea().getText(),
				view.getCreatePane().getThreadField().getText());

		refreshView();
		if (created) {
			view.getCreatePane().clearFields();
			refreshSelectionInView();
		} else {
			showInformation("Cannot Create Post", model.getStatusMessage(), AlertType.WARNING);
		}
	}

	private void handleUpdate() {
		boolean updated = model.updateSelectedPost(
				view.getDetailPane().getTitleField().getText(),
				view.getDetailPane().getBodyArea().getText(),
				view.getDetailPane().getThreadField().getText());

		refreshView();
		refreshSelectionInView();
		if (!updated) {
			showInformation("Cannot Update Post", model.getStatusMessage(), AlertType.WARNING);
		}
	}

	private void handleDelete() {
		Post selectedPost = model.getSelectedPost();
		if (selectedPost == null) {
			showInformation("Cannot Delete Post", "Select a post before deleting it.", AlertType.WARNING);
			return;
		}

		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.initOwner(stage);
		confirm.setTitle("Confirm Delete");
		confirm.setHeaderText("Delete Post");
		confirm.setContentText("Are you sure you want to delete the selected post?");
		if (confirm.showAndWait().isEmpty() || confirm.getResult() != javafx.scene.control.ButtonType.OK) {
			return;
		}

		boolean deleted = model.deleteSelectedPost();
		refreshView();
		refreshSelectionInView();
		if (!deleted) {
			showInformation("Cannot Delete Post", model.getStatusMessage(), AlertType.WARNING);
		}
	}

	private void refreshView() {
		model.refreshData();
		view.getSearchField().setText(model.getSearchText());
		view.getThreadFilter().setItems(model.getThreadOptions());
		view.getThreadFilter().setValue(model.getCurrentThread());
		view.getPostList().setItems(model.getVisiblePosts());
		view.getStatusLabel().setText(model.getStatusMessage());
		refreshSelectionInView();
	}

	private void refreshSelectionInView() {
		Post selectedPost = model.getSelectedPost();
		if (selectedPost == null) {
			view.getPostList().getSelectionModel().clearSelection();
			view.getDetailPane().showPost(null, model.getCurrentUser());
			view.getStatusLabel().setText(model.getStatusMessage());
			return;
		}

		view.getPostList().getSelectionModel().select(selectedPost);
		view.getDetailPane().showPost(selectedPost, model.getCurrentUser());
		view.getStatusLabel().setText(model.getStatusMessage());
	}

	private void showInformation(String header, String message, AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.initOwner(stage);
		alert.setTitle("Student Posts");
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
