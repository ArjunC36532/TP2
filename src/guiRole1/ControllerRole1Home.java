package guiRole1;

import entityClasses.Post;
import studentPostsController.StudentPostsController;

/*******
 * <p> Title: ControllerRole1Home Class. </p>
 * 
 * <p> Description: The Java/FX-based Role 1 Home Page.  This class provides the controller
 * actions for the Student Discussion System and user account. </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * @version 1.00		2025-08-17 Initial version
 * @version 2.00		2026-02-17 Discussion system CRUD actions
 */

public class ControllerRole1Home {

	/**
	 * Default constructor is not used.
	 */
	public ControllerRole1Home() {
	}

	protected static void performUpdate () {
		guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewRole1Home.theStage, ViewRole1Home.theUser);
	}	

	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewRole1Home.theStage);
	}
	
	protected static void performQuit() {
		System.exit(0);
	}

	protected static void performCreatePost() {
		openStudentPostsScreen(null);
	}

	protected static void performViewPost() {
		Post selected = ViewRole1Home.tableView_Posts.getSelectionModel().getSelectedItem();
		openStudentPostsScreen(selected);
	}

	protected static void performSearchPosts() {
		openStudentPostsScreen(ViewRole1Home.tableView_Posts.getSelectionModel().getSelectedItem());
	}

	protected static void performRefresh() {
		openStudentPostsScreen(ViewRole1Home.tableView_Posts.getSelectionModel().getSelectedItem());
	}

	private static void openStudentPostsScreen(Post selectedPost) {
		String username = ViewRole1Home.theUser != null ? ViewRole1Home.theUser.getUserName() : "";
		StudentPostsController.display(
				ViewRole1Home.theStage,
				username,
				selectedPost,
				() -> ModelRole1Home.refreshPostList(null, null));
	}
}
