package testCases;

/**
 * <h2>ManualTestGuide — Step-by-Step GUI Tests for the Student Discussion System</h2>
 *
 * <p>This class documents every manual test that a tester needs to walk through
 * in the running JavaFX application. It pairs with {@link TestCases}: the
 * semi-automated tests in that class verify that the <em>logic</em> (storage
 * and entity layers) is correct; these manual tests verify that the GUI
 * <em>correctly calls</em> that logic, shows the right feedback, and updates
 * the screen in response.</p>
 *
 * <p><b>Why manual?</b> The controllers in {@code guiDiscussion} pop up JavaFX
 * {@code Alert} dialogs using {@code showAndWait()}, which can only run on the
 * JavaFX Application Thread. That thread isn't available from a plain
 * {@code main()} runner, so those code paths need a human tester clicking
 * through the live app. The non-JavaFX parts of the same controllers are
 * covered by the Controller Logic Tests in {@link TestCases}.</p>
 *
 * <p><b>How to run the tests:</b></p>
 * <ol>
 *   <li>Build and launch the app via {@code applicationMain.FoundationsMain}.</li>
 *   <li>Make sure at least one student (Role 1) account exists. If not, go
 *       through the first-run admin flow to create one.</li>
 *   <li>Unless a test says "keep state from the previous test", restart the
 *       app before each test so you're starting with empty in-memory
 *       storage.</li>
 *   <li>Follow each test's numbered steps exactly as written.</li>
 *   <li>Compare what you see to the expected results and record Pass or Fail.
 *       If something fails, note the error/deviation for the project log.</li>
 * </ol>
 *
 * <p><b>Notation.</b> GUI control names are written in <b>bold</b>. Literal
 * values to type are shown in {@code code font}.</p>
 *
 * <hr>
 * <h3>Manual Tests at a Glance</h3>
 *
 * <table border="1" cellpadding="4" cellspacing="0"
 *        summary="Manual tests cross-referenced to requirements">
 *   <tr><th>Test</th><th>What it verifies</th>
 *       <th>User Story requirements</th><th>Related semi-auto tests</th></tr>
 *
 *   <tr><td>{@link MT01_CreatePostSuccess}</td>
 *       <td>Create a post; see it in the list with default thread</td>
 *       <td>POST-US1a, US1b, US1c, US2a, US2b</td>
 *       <td>postTest1, postTest8</td></tr>
 *
 *   <tr><td>{@link MT02_CreatePostEmptyTitle}</td>
 *       <td>Blank title is blocked with an error dialog</td>
 *       <td>POST-US1d</td>
 *       <td>postTest2</td></tr>
 *
 *   <tr><td>{@link MT03_CreatePostEmptyBody}</td>
 *       <td>Blank body is blocked with an error dialog</td>
 *       <td>POST-US1e</td>
 *       <td>postTest6</td></tr>
 *
 *   <tr><td>{@link MT04_ViewAllPosts}</td>
 *       <td>All active posts are listed with all four required fields</td>
 *       <td>POST-US2a, US2b</td>
 *       <td>postTest3, storageTest1</td></tr>
 *
 *   <tr><td>{@link MT05_SearchByKeyword}</td>
 *       <td>Keyword search hits title and body, case-insensitive</td>
 *       <td>POST-US3a, US3b</td>
 *       <td>postTest15, postTest16</td></tr>
 *
 *   <tr><td>{@link MT06_SearchByThread}</td>
 *       <td>Thread filter narrows results to one thread</td>
 *       <td>POST-US3c</td>
 *       <td>postTest17</td></tr>
 *
 *   <tr><td>{@link MT07_SearchEmptyKeyword}</td>
 *       <td>Blank keyword doesn't crash or show an error</td>
 *       <td>POST-US3d</td>
 *       <td>postTest18</td></tr>
 *
 *   <tr><td>{@link MT08_UpdatePostAsAuthor}</td>
 *       <td>Author edits their post and sees the change immediately</td>
 *       <td>POST-US4a</td>
 *       <td>postTest4</td></tr>
 *
 *   <tr><td>{@link MT09_UpdatePostNonAuthor}</td>
 *       <td>Non-author can't edit someone else's post</td>
 *       <td>POST-US4b, CTRL-3</td>
 *       <td>postTest11, ctrlTest3</td></tr>
 *
 *   <tr><td>{@link MT10_DeletePostAsAuthor}</td>
 *       <td>Author deletes their post; it disappears; replies survive</td>
 *       <td>POST-US5a, US2c, US5d</td>
 *       <td>postTest5, storageTest5</td></tr>
 *
 *   <tr><td>{@link MT11_DeletePostNonAuthor}</td>
 *       <td>Non-author can't delete someone else's post</td>
 *       <td>POST-US5b, CTRL-3</td>
 *       <td>postTest12, ctrlTest3</td></tr>
 *
 *   <tr><td>{@link MT12_CreateReplySuccess}</td>
 *       <td>Student replies to a post; reply appears with count update</td>
 *       <td>REPLY-US1a, US1b, US2a, US2b</td>
 *       <td>replyTest1</td></tr>
 *
 *   <tr><td>{@link MT13_CreateReplyEmptyBody}</td>
 *       <td>Blank reply body is blocked with an error dialog</td>
 *       <td>REPLY-US1c</td>
 *       <td>replyTest2</td></tr>
 *
 *   <tr><td>{@link MT14_ViewRepliesOrder}</td>
 *       <td>Replies show in creation order with author and body visible</td>
 *       <td>REPLY-US2a, US2b</td>
 *       <td>replyTest3, storageTest3</td></tr>
 *
 *   <tr><td>{@link MT15_UpdateReplyAsAuthor}</td>
 *       <td>Reply author edits their reply and sees the change immediately</td>
 *       <td>REPLY-US4a</td>
 *       <td>replyTest4</td></tr>
 *
 *   <tr><td>{@link MT16_UpdateReplyNonAuthor}</td>
 *       <td>Non-author can't edit someone else's reply</td>
 *       <td>REPLY-US4b, CTRL-6</td>
 *       <td>replyTest7, ctrlTest6</td></tr>
 *
 *   <tr><td>{@link MT17_DeleteReplyAsAuthor}</td>
 *       <td>Reply author deletes their reply; it's fully removed</td>
 *       <td>REPLY-US5a</td>
 *       <td>replyTest6</td></tr>
 *
 *   <tr><td>{@link MT18_DeleteReplyNonAuthor}</td>
 *       <td>Non-author can't delete someone else's reply</td>
 *       <td>REPLY-US5b, CTRL-6</td>
 *       <td>replyTest5, ctrlTest6</td></tr>
 *
 *   <tr><td>{@link MT19_DeletedPostIndicator}</td>
 *       <td>UI indicates a post is deleted; reply is still accessible</td>
 *       <td>POST-US5d</td>
 *       <td>postTest5, storageTest5, replyTest10</td></tr>
 *
 *   <tr><td>{@link MT20_AdminCanEditAnyPost}</td>
 *       <td>Admin can edit and delete any post, not just their own</td>
 *       <td>CTRL-2</td>
 *       <td>ctrlTest2</td></tr>
 * </table>
 *
 * @author  TP2 Team
 * @version 1.00   2026-03-23  20 manual GUI tests covering all Student User Stories
 * @see TestCases
 * @see guiDiscussion.ViewDiscussionList
 * @see guiDiscussion.ViewCreatePost
 * @see guiDiscussion.ViewPostDetail
 * @see guiDiscussion.ViewCreateReply
 * @see guiDiscussion.ViewUpdatePost
 * @see guiDiscussion.ViewUpdateReply
 * @see guiDiscussion.ControllerCreatePost
 * @see guiDiscussion.ControllerPostDetail
 */
public class ManualTestGuide {

    /*
     * All 20 manual tests live below as empty static inner classes.
     * The class bodies are intentionally blank — the Javadoc on each
     * class IS the deliverable.
     */

    /**
     * <h2>MT-01 — Create a post and see it in the list</h2>
     *
     * <p><b>User stories:</b> POST-US1a, POST-US1b, POST-US1c, POST-US2a, POST-US2b<br>
     * <b>Related semi-auto tests:</b> postTest1, postTest8</p>
     *
     * <p><b>Before you start:</b> Log in as Sid's student account. App freshly
     * launched with no posts.</p>
     *
     * <p><b>Steps:</b></p>
     * <ol>
     *   <li>Go to the Discussion Board.</li>
     *   <li>Click <b>Create Post</b>.</li>
     *   <li>Type {@code "JavaFX question"} in the <b>Title</b> field.</li>
     *   <li>Type {@code "How do I set a scene?"} in the <b>Body</b> field.</li>
     *   <li>Leave <b>Thread</b> completely blank.</li>
     *   <li>Click <b>Submit</b> and dismiss the success alert.</li>
     * </ol>
     *
     * <p>You should see a "Post Created" alert right away. After dismissing it,
     * Sid's new post row should appear in the discussion list showing the title
     * "JavaFX question", Sid's username as author, thread "General" (defaulted
     * from the blank field — POST-US1c), and reply count 0. If the thread shows
     * blank or null, or the row doesn't appear at all, mark this as a fail.</p>
     */
    public static class MT01_CreatePostSuccess {}

    /**
     * <h2>MT-02 — Blank title shows an error and doesn't create a post</h2>
     *
     * <p><b>User story:</b> POST-US1d<br>
     * <b>Related semi-auto test:</b> postTest2</p>
     *
     * <p><b>Steps:</b></p>
     * <ol>
     *   <li>Log in as any student and click <b>Create Post</b>.</li>
     *   <li>Leave <b>Title</b> blank (or type a few spaces).</li>
     *   <li>Type {@code "Some body text"} in <b>Body</b>.</li>
     *   <li>Click <b>Submit</b>.</li>
     * </ol>
     *
     * <p><b>What you should see:</b></p>
     * <ul>
     *   <li>An error dialog with something like "title can't be empty" — no post is created.</li>
     *   <li>The form stays open so the user can correct the mistake.</li>
     * </ul>
     *
     * <p><b>Pass:</b> Error shown; post count unchanged.
     * <b>Fail:</b> No error; post appeared in the list.</p>
     */
    public static class MT02_CreatePostEmptyTitle {}

    /**
     * <h2>MT-03 — Blank body shows an error and doesn't create a post</h2>
     *
     * <p><b>User story:</b> POST-US1e<br>
     * <b>Related semi-auto test:</b> postTest6</p>
     *
     * <p><b>Steps:</b></p>
     * <ol>
     *   <li>Click <b>Create Post</b>.</li>
     *   <li>Type {@code "My Post"} in <b>Title</b> and leave <b>Body</b> blank.</li>
     *   <li>Click <b>Submit</b>.</li>
     * </ol>
     *
     * <p>Expect an error dialog saying the body can't be empty, and no new row
     * in the discussion list. Same idea as MT-02 but for the body field.</p>
     */
    public static class MT03_CreatePostEmptyBody {}

    /**
     * <h2>MT-04 — Discussion list shows all active posts with required fields</h2>
     *
     * <p><b>User stories:</b> POST-US2a, POST-US2b<br>
     * <b>Related semi-auto tests:</b> postTest3, storageTest1</p>
     *
     * <p><b>Before you start:</b> Have at least two posts already created —
     * for example, one by Sai in thread "General" and one by Jeremy in thread
     * "Logistics".</p>
     *
     * <p><b>Steps:</b></p>
     * <ol>
     *   <li>Navigate to the Discussion Board without typing anything in the
     *       search field.</li>
     *   <li>Scan each row in the list.</li>
     * </ol>
     *
     * <p><b>What you should see:</b></p>
     * <ul>
     *   <li>Both posts are listed (POST-US2a).</li>
     *   <li>Every row shows: title, author username, thread name, and reply count —
     *       none of these should be blank or show "null". (POST-US2b)</li>
     * </ul>
     *
     * <p><b>Pass:</b> All posts listed; all four fields present per row.<br>
     * <b>Fail:</b> A post is missing; any field shows blank or "null".</p>
     */
    public static class MT04_ViewAllPosts {}

    /**
     * <h2>MT-05 — Keyword search finds posts by title and body (case-insensitive)</h2>
     *
     * <p><b>User stories:</b> POST-US3a, POST-US3b<br>
     * <b>Related semi-auto tests:</b> postTest15, postTest16</p>
     *
     * <p><b>Before you start:</b> Three posts exist:</p>
     * <ul>
     *   <li>Post A (by Arjun): title "JavaFX Scene Builder", body "Drag and drop nodes"</li>
     *   <li>Post B (by Sai): title "General question", body "Explain recursion please"</li>
     *   <li>Post C (by Anannay): title "Unrelated topic", body "Nothing here"</li>
     * </ul>
     *
     * <p><b>Steps:</b></p>
     * <ol>
     *   <li>Type {@code "recursion"} (lower case) in the search field and hit <b>Search</b>.</li>
     *   <li>Note the results, then clear the field.</li>
     *   <li>Type {@code "javafx"} (lower case) and hit <b>Search</b> again.</li>
     * </ol>
     *
     * <p><b>What you should see:</b></p>
     * <ul>
     *   <li>"recursion" returns only Sai's Post B — matched the body despite different
     *       case. (POST-US3b)</li>
     *   <li>"javafx" returns only Arjun's Post A — matched the title. (POST-US3a)</li>
     *   <li>Anannay's Post C never appears in either search.</li>
     * </ul>
     *
     * <p><b>Pass:</b> One correct result each time, Post C never shows up.<br>
     * <b>Fail:</b> Wrong post returned; case sensitivity causes a miss; Post C appears.</p>
     */
    public static class MT05_SearchByKeyword {}

    /**
     * <h2>MT-06 — Thread filter limits search to one thread</h2>
     *
     * <p><b>User story:</b> POST-US3c<br>
     * <b>Related semi-auto test:</b> postTest17</p>
     *
     * <p><b>Before you start:</b> Two posts both containing the word "exam" —
     * Sid's in thread "General" and Jeremy's in thread "Logistics".</p>
     *
     * <p><b>Steps:</b></p>
     * <ol>
     *   <li>Type {@code "exam"} in the keyword field.</li>
     *   <li>Set the thread filter to {@code "Logistics"}.</li>
     *   <li>Click <b>Search</b>.</li>
     * </ol>
     *
     * <p>Only Jeremy's post should come back. Sid's "General" post should be
     * filtered out even though it also contains "exam". If both appear, the
     * thread filter isn't working.</p>
     */
    public static class MT06_SearchByThread {}

    /**
     * <h2>MT-07 — Blank search field doesn't crash anything</h2>
     *
     * <p><b>User story:</b> POST-US3d<br>
     * <b>Related semi-auto test:</b> postTest18</p>
     *
     * <p>Quick sanity check. With at least one post in the list, leave the
     * search field empty and click <b>Search</b>. The app should not crash or
     * throw an error dialog. Either the full list stays up or an empty result
     * is shown gracefully — either is fine as long as nothing explodes.</p>
     */
    public static class MT07_SearchEmptyKeyword {}

    /**
     * <h2>MT-08 — Author edits their own post and sees the change</h2>
     *
     * <p><b>User story:</b> POST-US4a<br>
     * <b>Related semi-auto test:</b> postTest4</p>
     *
     * <p><b>Before you start:</b> Sai has a post with title "Old Title" and
     * body "Old body". Log in as Sai.</p>
     *
     * <p><b>Steps:</b></p>
     * <ol>
     *   <li>Open Sai's post in the post detail view.</li>
     *   <li>Click <b>Edit Post</b>.</li>
     *   <li>Change the title to {@code "New Title"} and the body to {@code "New body"}.</li>
     *   <li>Click <b>Save</b> and dismiss the success alert.</li>
     * </ol>
     *
     * <p><b>What you should see:</b></p>
     * <ul>
     *   <li>"Post Updated" success alert.</li>
     *   <li>Post detail view shows "New Title" and "New body".</li>
     *   <li>Back in the list, Sai's row also shows "New Title".</li>
     * </ul>
     *
     * <p><b>Pass:</b> All three updated correctly.
     * <b>Fail:</b> Error dialog; old values still showing after save.</p>
     */
    public static class MT08_UpdatePostAsAuthor {}

    /**
     * <h2>MT-09 — Non-author can't edit someone else's post</h2>
     *
     * <p><b>User stories:</b> POST-US4b, CTRL-3<br>
     * <b>Related semi-auto tests:</b> postTest11, ctrlTest3</p>
     *
     * <p>Sid has a post. Log in as Anannay (a regular student, not an admin)
     * and open Sid's post. The <b>Edit Post</b> button should either be hidden
     * entirely, or if it's visible and Anannay clicks it, an authorization
     * error should come up on save. Either way Sid's title and body must be
     * unchanged when you reload. If Anannay can successfully change Sid's post,
     * this is a fail.</p>
     */
    public static class MT09_UpdatePostNonAuthor {}

    /**
     * <h2>MT-10 — Author deletes their post; replies survive</h2>
     *
     * <p><b>User stories:</b> POST-US5a, POST-US2c, POST-US5d<br>
     * <b>Related semi-auto tests:</b> postTest5, storageTest5</p>
     *
     * <p><b>Before you start:</b> Jeremy has a post. Arjun has already added a
     * reply to it. Log in as Jeremy.</p>
     *
     * <p><b>Steps:</b></p>
     * <ol>
     *   <li>Open Jeremy's post in the post detail view.</li>
     *   <li>Click <b>Delete Post</b> and confirm in the dialog.</li>
     *   <li>Dismiss the success alert, then go back to the discussion list.</li>
     * </ol>
     *
     * <p><b>What you should see:</b></p>
     * <ul>
     *   <li>Confirmation dialog before anything is deleted. (POST-US5a)</li>
     *   <li>Jeremy's post is gone from the discussion list. (POST-US2c)</li>
     *   <li>Arjun's reply is still in storage — verified by storageTest5 at the
     *       logic level. (POST-US5d)</li>
     * </ul>
     *
     * <p><b>Pass:</b> Post gone; reply persists; no errors.
     * <b>Fail:</b> Post still visible; confirmation skipped; error alert.</p>
     */
    public static class MT10_DeletePostAsAuthor {}

    /**
     * <h2>MT-11 — Non-author can't delete someone else's post</h2>
     *
     * <p><b>User stories:</b> POST-US5b, CTRL-3<br>
     * <b>Related semi-auto tests:</b> postTest12, ctrlTest3</p>
     *
     * <p>Arjun has a post. Log in as Sai and try to delete it. The
     * <b>Delete Post</b> button should be hidden or blocked. Arjun's post must
     * still be in the list when you're done.</p>
     *
     * <p><b>Pass:</b> Arjun's post still visible; Sai can't delete it.<br>
     * <b>Fail:</b> Post disappears; no restriction enforced.</p>
     */
    public static class MT11_DeletePostNonAuthor {}

    /**
     * <h2>MT-12 — Create a reply and see it under the post</h2>
     *
     * <p><b>User stories:</b> REPLY-US1a, REPLY-US1b, REPLY-US2a, REPLY-US2b<br>
     * <b>Related semi-auto test:</b> replyTest1</p>
     *
     * <p><b>Before you start:</b> An existing post is open. Log in as Anannay.</p>
     *
     * <p><b>Steps:</b></p>
     * <ol>
     *   <li>With the post open in the detail view, click <b>Add Reply</b>.</li>
     *   <li>Type {@code "This is my answer."} in the body field.</li>
     *   <li>Click <b>Submit Reply</b> and dismiss the success alert.</li>
     * </ol>
     *
     * <p><b>What you should see:</b></p>
     * <ul>
     *   <li>"Reply Created" success alert. (REPLY-US1a)</li>
     *   <li>Anannay's reply appears below the post body, showing "This is my answer."
     *       and Anannay's username. (REPLY-US2b)</li>
     *   <li>The reply count on the post row went up by 1. (REPLY-US2a)</li>
     * </ul>
     *
     * <p><b>Pass:</b> Reply visible with correct author and body; count updated.<br>
     * <b>Fail:</b> No alert; reply missing; count didn't change.</p>
     */
    public static class MT12_CreateReplySuccess {}

    /**
     * <h2>MT-13 — Blank reply body shows an error</h2>
     *
     * <p><b>User story:</b> REPLY-US1c<br>
     * <b>Related semi-auto test:</b> replyTest2</p>
     *
     * <p><b>Steps:</b></p>
     * <ol>
     *   <li>Open any post and click <b>Add Reply</b>.</li>
     *   <li>Leave the reply body completely blank and click <b>Submit Reply</b>.</li>
     * </ol>
     *
     * <p>An error dialog should say the body can't be empty. No reply is added
     * and the count stays the same. Same concept as MT-02/03 but for replies.</p>
     */
    public static class MT13_CreateReplyEmptyBody {}

    /**
     * <h2>MT-14 — Replies appear in creation order with both author and body visible</h2>
     *
     * <p><b>User stories:</b> REPLY-US2a, REPLY-US2b<br>
     * <b>Related semi-auto tests:</b> replyTest3, storageTest3</p>
     *
     * <p><b>Before you start:</b> A post has exactly three replies added
     * in this order: Sid replies "First", Jeremy replies "Second",
     * Sai replies "Third".</p>
     *
     * <p><b>Steps:</b></p>
     * <ol>
     *   <li>Open that post in the post detail view.</li>
     *   <li>Look at the reply list from top to bottom.</li>
     * </ol>
     *
     * <p><b>What you should see:</b></p>
     * <ul>
     *   <li>Sid's "First" at the top, then Jeremy's "Second", then Sai's "Third"
     *       — in the exact order they were submitted. (REPLY-US2a)</li>
     *   <li>Each entry shows the reply body text and the author's username.
     *       (REPLY-US2b)</li>
     * </ul>
     *
     * <p><b>Pass:</b> Correct order; author and body both visible per reply.<br>
     * <b>Fail:</b> Wrong order; missing author or body.</p>
     */
    public static class MT14_ViewRepliesOrder {}

    /**
     * <h2>MT-15 — Reply author edits their reply and the change sticks</h2>
     *
     * <p><b>User story:</b> REPLY-US4a<br>
     * <b>Related semi-auto test:</b> replyTest4</p>
     *
     * <p><b>Before you start:</b> Arjun has a reply with body "Original reply".
     * Log in as Arjun.</p>
     *
     * <p><b>Steps:</b></p>
     * <ol>
     *   <li>Open the post containing Arjun's reply.</li>
     *   <li>Click <b>Edit Reply</b> on Arjun's reply.</li>
     *   <li>Change the body to {@code "Corrected reply"} and click <b>Save</b>.</li>
     *   <li>Dismiss the success alert.</li>
     * </ol>
     *
     * <p>The "Reply Updated" alert should appear, and the reply in the detail
     * view should now show "Corrected reply". If the old text is still there
     * or an error dialog came up instead, mark this as a fail.</p>
     */
    public static class MT15_UpdateReplyAsAuthor {}

    /**
     * <h2>MT-16 — Non-author can't edit someone else's reply</h2>
     *
     * <p><b>User stories:</b> REPLY-US4b, CTRL-6<br>
     * <b>Related semi-auto tests:</b> replyTest7, ctrlTest6</p>
     *
     * <p>Sai has a reply. Log in as Jeremy and open the post containing that
     * reply. The <b>Edit Reply</b> button should either be hidden on Sai's
     * reply for Jeremy, or an authorization error should appear if Jeremy
     * tries to save a change. Either route must leave Sai's reply body
     * unchanged.</p>
     *
     * <p><b>Pass:</b> Jeremy can't change Sai's reply by any route.<br>
     * <b>Fail:</b> Reply body changes; no restriction applied.</p>
     */
    public static class MT16_UpdateReplyNonAuthor {}

    /**
     * <h2>MT-17 — Reply author fully deletes their reply</h2>
     *
     * <p><b>User story:</b> REPLY-US5a<br>
     * <b>Related semi-auto test:</b> replyTest6</p>
     *
     * <p><b>Before you start:</b> Anannay has a reply on a post. Log in as Anannay.</p>
     *
     * <p><b>Steps:</b></p>
     * <ol>
     *   <li>Open the post containing Anannay's reply.</li>
     *   <li>Click <b>Delete Reply</b> on his reply and confirm.</li>
     *   <li>Dismiss the success alert.</li>
     * </ol>
     *
     * <p><b>What you should see:</b></p>
     * <ul>
     *   <li>Anannay's reply is completely gone from the post detail view —
     *       replies are hard-deleted so there's no placeholder left behind.</li>
     *   <li>The reply count in the discussion list dropped by 1.</li>
     * </ul>
     *
     * <p><b>Pass:</b> Reply gone; count decremented; no error.
     * <b>Fail:</b> Reply still showing; count unchanged; error alert.</p>
     */
    public static class MT17_DeleteReplyAsAuthor {}

    /**
     * <h2>MT-18 — Non-author can't delete someone else's reply</h2>
     *
     * <p><b>User stories:</b> REPLY-US5b, CTRL-6<br>
     * <b>Related semi-auto tests:</b> replyTest5, ctrlTest6</p>
     *
     * <p>Anannay has a reply. Log in as Sid and open the same post. The
     * <b>Delete Reply</b> button should be hidden on Anannay's reply, or if
     * it's somehow visible and Sid clicks it, an authorization error must
     * appear. Anannay's reply and the reply count should be completely
     * unchanged when you're done.</p>
     */
    public static class MT18_DeleteReplyNonAuthor {}

    /**
     * <h2>MT-19 — Replies to a deleted post are still accessible</h2>
     *
     * <p><b>User story:</b> POST-US5d<br>
     * <b>Related semi-auto tests:</b> postTest5, storageTest5, replyTest10</p>
     *
     * <p><b>Before you start:</b> Run MT-10 first — Jeremy's post has been
     * deleted, but Arjun's reply to it is still in storage. You can be
     * logged in as anyone.</p>
     *
     * <p><b>Steps:</b></p>
     * <ol>
     *   <li>If the UI lets you navigate to a deleted post's detail view
     *       (e.g., via a direct link or admin panel), open it.</li>
     *   <li>Look at the page content.</li>
     * </ol>
     *
     * <p>Arjun's reply should still be readable. If the UI renders the deleted
     * post's detail page, it should clearly indicate that the post has been
     * removed — something like "This post has been deleted" near the top —
     * so readers understand why there's no post body. The logic-level guarantee
     * is confirmed by storageTest5 in {@link TestCases}.</p>
     *
     * <p><b>Pass:</b> Arjun's reply visible; UI signals the post is deleted.<br>
     * <b>Fail:</b> Reply missing; no deleted-post indicator shown.</p>
     */
    public static class MT19_DeletedPostIndicator {}

    /**
     * <h2>MT-20 — Admin can edit and delete any post, not just their own</h2>
     *
     * <p><b>Requirement:</b> CTRL-2<br>
     * <b>Related semi-auto test:</b> ctrlTest2</p>
     *
     * <p><b>Before you start:</b> Arjun has a post. Log in as an admin account.</p>
     *
     * <p><b>Steps:</b></p>
     * <ol>
     *   <li>Open Arjun's post in the post detail view.</li>
     *   <li>Check that both <b>Edit Post</b> and <b>Delete Post</b> are visible
     *       even though the admin isn't the author.</li>
     *   <li>Click <b>Edit Post</b>, change the title to
     *       {@code "Admin-edited title"}, and click <b>Save</b>.</li>
     * </ol>
     *
     * <p>Both buttons should show up for the admin — this is what
     * {@code canEditPost()} returning true for admins looks like in the GUI.
     * The update should go through and "Admin-edited title" should appear in
     * the discussion list.</p>
     *
     * <p><b>Pass:</b> Admin sees both controls and the edit succeeds.<br>
     * <b>Fail:</b> Buttons hidden; edit rejected with an auth error.</p>
     */
    public static class MT20_AdminCanEditAnyPost {}
}
