package studentPostsModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import applicationMain.FoundationsMain;
import entityClasses.Post;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model for the student posts screen.
 */
public class StudentPostsModel {

	private final String currentUser;
	private final ObservableList<Post> visiblePosts;
	private final ObservableList<String> threadOptions;

	private Post selectedPost;
	private String searchText;
	private String currentThread;
	private String statusMessage;

	/**
	 * Creates the model for the current user.
	 *
	 * @param currentUser signed-in username
	 */
	public StudentPostsModel(String currentUser) {
		this.currentUser = currentUser;
		this.visiblePosts = FXCollections.observableArrayList();
		this.threadOptions = FXCollections.observableArrayList();
		this.searchText = "";
		this.currentThread = "<All Threads>";
		this.statusMessage = "View, search, create, update, or delete posts.";
		refreshData();
	}

	/**
	 * Rebuilds the list and thread filter values from storage.
	 */
	public void refreshData() {
		List<Post> allPosts = FoundationsMain.postStorage.getAllPosts();
		updateThreadOptions(allPosts);

		Post refreshedSelection = null;
		if (selectedPost != null) {
			refreshedSelection = FoundationsMain.postStorage.getPostByID(selectedPost.getPostID());
			if (refreshedSelection != null && refreshedSelection.isDeleted()) {
				refreshedSelection = null;
			}
		}
		selectedPost = refreshedSelection;

		visiblePosts.clear();
		for (Post post : allPosts) {
			if (matchesSearch(post) && matchesThread(post)) {
				visiblePosts.add(post);
			}
		}

		if (selectedPost != null && !visiblePosts.contains(selectedPost)) {
			selectedPost = null;
		}
	}

	private void updateThreadOptions(List<Post> allPosts) {
		Set<String> threads = new LinkedHashSet<String>();
		threads.add("<All Threads>");
		threads.add("General");
		for (Post post : allPosts) {
			if (post.getThread() != null && !post.getThread().trim().isEmpty()) {
				threads.add(post.getThread());
			}
		}

		threadOptions.setAll(new ArrayList<String>(threads));
		if (!threadOptions.contains(currentThread)) {
			currentThread = "<All Threads>";
		}
	}

	private boolean matchesSearch(Post post) {
		if (searchText == null || searchText.trim().isEmpty()) {
			return true;
		}

		String keyword = searchText.trim().toLowerCase();
		String title = post.getTitle() == null ? "" : post.getTitle().toLowerCase();
		String body = post.getBody() == null ? "" : post.getBody().toLowerCase();
		return title.contains(keyword) || body.contains(keyword);
	}

	private boolean matchesThread(Post post) {
		if (currentThread == null || currentThread.trim().isEmpty() || "<All Threads>".equals(currentThread)) {
			return true;
		}
		return currentThread.equals(post.getThread());
	}

	/**
	 * Tries to create a post.
	 */
	public boolean createPost(String title, String body, String thread) {
		Post post = new Post(title == null ? null : title.trim(),
				body == null ? null : body.trim(),
				thread == null ? "" : thread.trim(),
				currentUser);

		String error = FoundationsMain.postStorage.addPost(post);
		if (error != null) {
			statusMessage = error;
			return false;
		}

		statusMessage = "Post created.";
		refreshData();
		selectedPost = post;
		return true;
	}

	/**
	 * Tries to update the selected post.
	 */
	public boolean updateSelectedPost(String title, String body, String thread) {
		if (selectedPost == null) {
			statusMessage = "Select a post before updating it.";
			return false;
		}

		Post storedPost = FoundationsMain.postStorage.getPostByID(selectedPost.getPostID());
		if (storedPost == null || storedPost.isDeleted()) {
			statusMessage = "The selected post is no longer available.";
			refreshData();
			return false;
		}

		String cleanThread = thread == null ? "" : thread.trim();
		if (cleanThread.isEmpty()) {
			cleanThread = "General";
		}

		String error = FoundationsMain.postStorage.updatePost(
				storedPost.getPostID(),
				title == null ? null : title.trim(),
				body == null ? null : body.trim(),
				currentUser);
		if (error != null) {
			statusMessage = error;
			return false;
		}

		storedPost.setThread(cleanThread);
		statusMessage = "Post updated.";
		selectedPost = storedPost;
		refreshData();
		return true;
	}

	/**
	 * Tries to soft delete the selected post.
	 */
	public boolean deleteSelectedPost() {
		if (selectedPost == null) {
			statusMessage = "Select a post before deleting it.";
			return false;
		}

		String error = FoundationsMain.postStorage.deletePost(selectedPost.getPostID(), currentUser);
		if (error != null) {
			statusMessage = error;
			return false;
		}

		statusMessage = "Post deleted.";
		selectedPost = null;
		refreshData();
		return true;
	}

	/**
	 * Selects a post by id.
	 */
	public void selectPostByID(String postID) {
		if (postID == null) {
			selectedPost = null;
			return;
		}
		selectedPost = FoundationsMain.postStorage.getPostByID(postID);
		if (selectedPost != null && selectedPost.isDeleted()) {
			selectedPost = null;
		}
	}

	public ObservableList<Post> getVisiblePosts() {
		return visiblePosts;
	}

	public ObservableList<String> getThreadOptions() {
		return threadOptions;
	}

	public Post getSelectedPost() {
		return selectedPost;
	}

	public void setSelectedPost(Post selectedPost) {
		this.selectedPost = selectedPost;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText == null ? "" : searchText;
	}

	public String getCurrentThread() {
		return currentThread;
	}

	public void setCurrentThread(String currentThread) {
		this.currentThread = currentThread == null ? "<All Threads>" : currentThread;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public String getCurrentUser() {
		return currentUser;
	}
}
