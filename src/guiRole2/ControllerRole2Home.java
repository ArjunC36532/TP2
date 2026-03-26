package guiRole2;

import entityClasses.Post;
import studentPostsController.StudentPostsController;

/*******
 * <p> Title: ControllerRole2Home Class. </p>
 * 
 * <p> Description: The Java/FX-based Role 2 Home Page. Controller actions for the Student Discussion System. </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * @version 2.00		2026-02-17 Discussion system CRUD actions
 */

public class ControllerRole2Home {

	public ControllerRole2Home() {
	}

	protected static void performUpdate () {
		guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewRole2Home.theStage, ViewRole2Home.theUser);
	}	

	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewRole2Home.theStage);
	}
	
	protected static void performQuit() {
		System.exit(0);
	}

	protected static void performCreatePost() {
		openStudentPostsScreen(null);
	}

	protected static void performViewPost() {
		Post selected = ViewRole2Home.tableView_Posts.getSelectionModel().getSelectedItem();
		openStudentPostsScreen(selected);
	}

	protected static void performSearchPosts() {
		openStudentPostsScreen(ViewRole2Home.tableView_Posts.getSelectionModel().getSelectedItem());
	}

	protected static void performRefresh() {
		openStudentPostsScreen(ViewRole2Home.tableView_Posts.getSelectionModel().getSelectedItem());
	}

	private static void openStudentPostsScreen(Post selectedPost) {
		String username = ViewRole2Home.theUser != null ? ViewRole2Home.theUser.getUserName() : "";
		StudentPostsController.display(
				ViewRole2Home.theStage,
				username,
				selectedPost,
				() -> ModelRole2Home.refreshPostList(null, null));
	}
}
