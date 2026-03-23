package testCases;

import entityClasses.Post;
import entityClasses.Reply;
import guiDiscussion.ControllerPostDetail;
import storage.PostListStorage;
import storage.ReplyListStorage;

import java.util.List;

/**
 * <h2>TestCases — Semi-Automated Tests for the Student Discussion System</h2>
 *
 * <p>This is the main test class for TP2. It verifies that {@link entityClasses.Post},
 * {@link entityClasses.Reply}, their storage layers, and the non-GUI portions of the
 * MVC controllers all behave the way the Student User Stories require.</p>
 *
 * <p><b>Why three layers?</b> The GUI controllers ({@code ControllerCreatePost},
 * {@code ControllerUpdatePost}, etc.) validate input then delegate to storage.
 * They also pop up JavaFX {@code Alert} dialogs via {@code showAndWait()}, which
 * can't run without a live JavaFX Application Thread. So we test storage and entity
 * layers directly — those are exactly what the controllers delegate to — plus the
 * few controller helpers ({@code canEditPost}, {@code canEditReply}) that contain
 * no JavaFX at all. The companion {@link ManualTestGuide} covers everything that
 * genuinely needs a human clicking through the running app.</p>
 *
 * <p><b>Running the tests.</b> Just launch {@link #main(String[])} from Eclipse
 * (right-click → Run As → Java Application). No database, no network, no JavaFX
 * toolkit needed. Each test creates its own fresh in-memory storage so order
 * doesn't matter and tests can't pollute each other.</p>
 *
 * <p><b>Reading the output.</b> Every line looks like:</p>
 * <pre>
 *   Post  1  valid input, default thread   : PASS
 *   Post  2  empty title rejected          : FAIL - Expected an error but got null
 * </pre>
 * <p>PASS = every assertion in that test held. FAIL includes a short English
 * explanation of what went wrong. Fix the code, re-run, repeat until all green.</p>
 *
 * <p><b>Return convention.</b> Every test method returns {@code null} on success
 * or a plain English failure string. No third-party testing framework required.</p>
 *
 * <hr>
 * <h3>Requirements-to-Tests Mapping</h3>
 *
 * <p>The table below maps each Student User Story requirement to the test(s) that
 * verify it. Full coverage requires all 24 tests to pass.</p>
 *
 * <table border="1" cellpadding="4" cellspacing="0"
 *        summary="Requirements to test method mapping">
 *   <tr><th>Requirement</th><th>Description</th><th>Test(s)</th></tr>
 *
 *   <tr><td>POST-US1a</td>
 *       <td>Student can create a post with a valid title, body, and optional thread</td>
 *       <td>{@link #postTest1_CreatePostValid}</td></tr>
 *
 *   <tr><td>POST-US1b</td>
 *       <td>Every new post gets a unique Post ID (UUID)</td>
 *       <td>{@link #postTest1_CreatePostValid}</td></tr>
 *
 *   <tr><td>POST-US1c</td>
 *       <td>Blank or missing thread defaults to "General"</td>
 *       <td>{@link #postTest1_CreatePostValid}</td></tr>
 *
 *   <tr><td>POST-US1d</td>
 *       <td>Empty or whitespace-only title is rejected</td>
 *       <td>{@link #postTest2_EmptyTitleRejected}</td></tr>
 *
 *   <tr><td>POST-US1e</td>
 *       <td>Empty or whitespace-only body is rejected</td>
 *       <td>{@link #postTest3_EmptyBodyRejected}</td></tr>
 *
 *   <tr><td>POST-US1f</td>
 *       <td>Null author is rejected</td>
 *       <td>{@link #postTest4_NullAuthorRejected}</td></tr>
 *
 *   <tr><td>POST-US2a</td>
 *       <td>Student can see a list of all active (non-deleted) posts</td>
 *       <td>{@link #postTest5_ViewAllPosts}, {@link #storageTest1_StoreManyPosts}</td></tr>
 *
 *   <tr><td>POST-US2b</td>
 *       <td>Each post shows title, author, thread, and reply count</td>
 *       <td>{@link #postTest5_ViewAllPosts}</td></tr>
 *
 *   <tr><td>POST-US2c</td>
 *       <td>Deleted posts don't appear in the public list</td>
 *       <td>{@link #postTest7_UpdateDeleteAuthorAndAuth},
 *           {@link #storageTest5_DeletedPostKeepsReplies}</td></tr>
 *
 *   <tr><td>POST-US3a</td>
 *       <td>Keyword search matches the title, case-insensitive</td>
 *       <td>{@link #postTest6_SearchPosts}</td></tr>
 *
 *   <tr><td>POST-US3b</td>
 *       <td>Keyword search also matches the body, case-insensitive</td>
 *       <td>{@link #postTest6_SearchPosts}</td></tr>
 *
 *   <tr><td>POST-US3c</td>
 *       <td>Search results can be narrowed to a single thread</td>
 *       <td>{@link #postTest6_SearchPosts}</td></tr>
 *
 *   <tr><td>POST-US3d</td>
 *       <td>Blank keyword returns empty list without crashing</td>
 *       <td>{@link #postTest6_SearchPosts}</td></tr>
 *
 *   <tr><td>POST-US4a</td>
 *       <td>A post's author can update its title and body</td>
 *       <td>{@link #postTest7_UpdateDeleteAuthorAndAuth}</td></tr>
 *
 *   <tr><td>POST-US4b</td>
 *       <td>Non-author gets an authorization error on update</td>
 *       <td>{@link #postTest7_UpdateDeleteAuthorAndAuth},
 *           {@link #ctrlTest3_CanEditPost_NonAuthor}</td></tr>
 *
 *   <tr><td>POST-US5a</td>
 *       <td>Post author can soft-delete their post</td>
 *       <td>{@link #postTest7_UpdateDeleteAuthorAndAuth}</td></tr>
 *
 *   <tr><td>POST-US5b</td>
 *       <td>Non-author gets an authorization error on delete</td>
 *       <td>{@link #postTest7_UpdateDeleteAuthorAndAuth},
 *           {@link #ctrlTest3_CanEditPost_NonAuthor}</td></tr>
 *
 *   <tr><td>POST-US5d</td>
 *       <td>Replies to a deleted post are preserved</td>
 *       <td>{@link #postTest7_UpdateDeleteAuthorAndAuth},
 *           {@link #storageTest5_DeletedPostKeepsReplies}</td></tr>
 *
 *   <tr><td>REPLY-US1a</td>
 *       <td>Student can reply to an active post</td>
 *       <td>{@link #replyTest1_CreateReplyValid}</td></tr>
 *
 *   <tr><td>REPLY-US1b</td>
 *       <td>Every new reply gets a unique Reply ID (UUID)</td>
 *       <td>{@link #replyTest1_CreateReplyValid}</td></tr>
 *
 *   <tr><td>REPLY-US1c</td>
 *       <td>Empty or whitespace-only reply body is rejected</td>
 *       <td>{@link #replyTest2_EmptyBodyRejected}</td></tr>
 *
 *   <tr><td>REPLY-US1d</td>
 *       <td>Reply to a non-existent post is blocked (orphan prevention)</td>
 *       <td>{@link #replyTest3_OrphanReplyBlocked}</td></tr>
 *
 *   <tr><td>REPLY-US2a</td>
 *       <td>Replies come back in the order they were posted</td>
 *       <td>{@link #replyTest4_ViewRepliesInOrder}</td></tr>
 *
 *   <tr><td>REPLY-US2b</td>
 *       <td>Each reply shows its author and body text</td>
 *       <td>{@link #replyTest4_ViewRepliesInOrder}</td></tr>
 *
 *   <tr><td>REPLY-US4a</td>
 *       <td>A reply's author can update its body</td>
 *       <td>{@link #replyTest5_UpdateReplyByAuthor}</td></tr>
 *
 *   <tr><td>REPLY-US4b</td>
 *       <td>Non-author gets an authorization error on reply update</td>
 *       <td>{@link #replyTest6_AuthBlockedFromReplyEditDelete},
 *           {@link #ctrlTest5_CanEditReply_NonAuthor}</td></tr>
 *
 *   <tr><td>REPLY-US5a</td>
 *       <td>A reply's author can delete it (hard delete)</td>
 *       <td>{@link #replyTest7_DeleteReplyByAuthor}</td></tr>
 *
 *   <tr><td>REPLY-US5b</td>
 *       <td>Non-author gets an authorization error on reply delete</td>
 *       <td>{@link #replyTest6_AuthBlockedFromReplyEditDelete},
 *           {@link #ctrlTest5_CanEditReply_NonAuthor}</td></tr>
 *
 *   <tr><td>STORAGE-1</td>
 *       <td>Storage has no hard cap — at least 50 posts can be stored and fetched</td>
 *       <td>{@link #storageTest1_StoreManyPosts}</td></tr>
 *
 *   <tr><td>STORAGE-2</td>
 *       <td>Fresh storage returns an empty (non-null) list</td>
 *       <td>{@link #storageTest2_FreshStorageEmpty}</td></tr>
 *
 *   <tr><td>STORAGE-3</td>
 *       <td>Reply count stays accurate as replies are added and deleted</td>
 *       <td>{@link #storageTest3_ReplyCountTracking}</td></tr>
 *
 *   <tr><td>STORAGE-4</td>
 *       <td>Multiple posts keep their own independent reply lists</td>
 *       <td>{@link #storageTest4_TwoPostsIndependentReplies}</td></tr>
 *
 *   <tr><td>CTRL-1</td>
 *       <td>Controller grants edit permission to the post's author</td>
 *       <td>{@link #ctrlTest1_CanEditPost_Author}</td></tr>
 *
 *   <tr><td>CTRL-2</td>
 *       <td>Admin users can edit or delete any post</td>
 *       <td>{@link #ctrlTest2_CanEditPost_Admin}</td></tr>
 *
 *   <tr><td>CTRL-3</td>
 *       <td>Non-author, non-admin users are denied edit/delete on posts</td>
 *       <td>{@link #ctrlTest3_CanEditPost_NonAuthor}</td></tr>
 *
 *   <tr><td>CTRL-5</td>
 *       <td>Controller grants edit permission to the reply's author</td>
 *       <td>{@link #ctrlTest4_CanEditReply_Author}</td></tr>
 *
 *   <tr><td>CTRL-6</td>
 *       <td>Non-author, non-admin users are denied edit/delete on replies</td>
 *       <td>{@link #ctrlTest5_CanEditReply_NonAuthor}</td></tr>
 * </table>
 *
 * @author  TP2 Team (Sid, Sai, Jeremy, Arjun, Anannay)
 * @version 2.00   2026-03-23  24 tests across 4 groups covering all TP2 Student User Stories
 * @see ManualTestGuide
 * @see entityClasses.Post
 * @see entityClasses.Reply
 * @see storage.PostListStorage
 * @see storage.ReplyListStorage
 * @see guiDiscussion.ControllerPostDetail
 */
public class TestCases {

    // =========================================================================
    //  ENTRY POINT
    // =========================================================================

    /**
     * Runs all 24 tests and prints a PASS or FAIL line for each one.
     *
     * <p>Tests are split into four groups:</p>
     * <ol>
     *   <li>Post Tests (7) — Post entity and PostListStorage</li>
     *   <li>Reply Tests (7) — Reply entity and ReplyListStorage</li>
     *   <li>Storage Integration Tests (5) — scenarios that touch both layers</li>
     *   <li>Controller Logic Tests (5) — canEditPost / canEditReply with no JavaFX</li>
     * </ol>
     *
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println("  Student Discussion System — Semi-Automated Tests (TP2)       ");
        System.out.println("================================================================\n");
        runPostTests();
        runReplyTests();
        runStorageTests();
        runControllerLogicTests();
        System.out.println("\n================================================================");
        System.out.println("  Done. Fix any FAILs and re-run until everything is green.");
        System.out.println("================================================================");
    }

    // =========================================================================
    //  GROUP RUNNERS
    // =========================================================================

    /**
     * Runs the 7 post-related tests (POST-US1 through POST-US5).
     * Each test creates its own fresh storage so nothing bleeds across tests.
     */
    private static void runPostTests() {
        System.out.println("--- Post Tests ---");
        p("Post 1  valid input, default thread         ", postTest1_CreatePostValid());
        p("Post 2  empty title rejected                ", postTest2_EmptyTitleRejected());
        p("Post 3  empty body rejected                 ", postTest3_EmptyBodyRejected());
        p("Post 4  null author rejected                ", postTest4_NullAuthorRejected());
        p("Post 5  view all posts, required fields     ", postTest5_ViewAllPosts());
        p("Post 6  search: title, body, thread, blank  ", postTest6_SearchPosts());
        p("Post 7  update+delete: author and auth      ", postTest7_UpdateDeleteAuthorAndAuth());
    }

    /**
     * Runs the 7 reply-related tests (REPLY-US1 through REPLY-US5).
     */
    private static void runReplyTests() {
        System.out.println("\n--- Reply Tests ---");
        p("Reply 1  valid reply created                ", replyTest1_CreateReplyValid());
        p("Reply 2  empty body rejected                ", replyTest2_EmptyBodyRejected());
        p("Reply 3  orphan reply blocked               ", replyTest3_OrphanReplyBlocked());
        p("Reply 4  replies returned in order          ", replyTest4_ViewRepliesInOrder());
        p("Reply 5  author updates own reply           ", replyTest5_UpdateReplyByAuthor());
        p("Reply 6  non-author blocked edit and delete ", replyTest6_AuthBlockedFromReplyEditDelete());
        p("Reply 7  author hard-deletes own reply      ", replyTest7_DeleteReplyByAuthor());
    }

    /**
     * Runs the 5 storage integration tests (STORAGE-1 through STORAGE-4 plus
     * the soft-delete/reply-survival scenario from POST-US5d).
     */
    private static void runStorageTests() {
        System.out.println("\n--- Storage Integration Tests ---");
        p("Storage 1  50 posts stored and retrieved    ", storageTest1_StoreManyPosts());
        p("Storage 2  fresh storage returns empty list ", storageTest2_FreshStorageEmpty());
        p("Storage 3  reply count tracks adds/deletes  ", storageTest3_ReplyCountTracking());
        p("Storage 4  two posts, independent reply lists", storageTest4_TwoPostsIndependentReplies());
        p("Storage 5  deleted post keeps its replies   ", storageTest5_DeletedPostKeepsReplies());
    }

    /**
     * Runs the 5 controller logic tests (CTRL-1 through CTRL-6, subset).
     *
     * <p>These test the pure-Java parts of {@link guiDiscussion.ControllerPostDetail}
     * — specifically {@code canEditPost()} and {@code canEditReply(Reply)} — which
     * decide whether the Edit/Delete buttons should appear. No JavaFX toolkit is needed
     * because those methods don't touch any UI components.</p>
     *
     * <p>Note: {@code ControllerPostDetail.init()} accepts a Stage as its first argument.
     * We pass {@code null} here because the tested methods never use the Stage reference.</p>
     */
    private static void runControllerLogicTests() {
        System.out.println("\n--- Controller Logic Tests (no JavaFX required) ---");
        p("Ctrl 1  canEditPost → true for author       ", ctrlTest1_CanEditPost_Author());
        p("Ctrl 2  canEditPost → true for admin        ", ctrlTest2_CanEditPost_Admin());
        p("Ctrl 3  canEditPost → false for non-author  ", ctrlTest3_CanEditPost_NonAuthor());
        p("Ctrl 4  canEditReply → true for reply author", ctrlTest4_CanEditReply_Author());
        p("Ctrl 5  canEditReply → false for non-author ", ctrlTest5_CanEditReply_NonAuthor());
    }

    /**
     * Prints one test result line in the format {@code <label>: PASS} or
     * {@code <label>: FAIL - <reason>}.
     *
     * @param label  short description of the test being reported
     * @param result {@code null} means passed; anything else is the failure message
     */
    private static void p(String label, String result) {
        System.out.println(label + ": " + (result == null ? "PASS" : "FAIL - " + result));
    }

    // =========================================================================
    //  POST TESTS
    // =========================================================================

    /**
     * <b>Post Test 1 — Happy-path post creation (POST-US1a, POST-US1b, POST-US1c,
     * POST-US2a, POST-US2b).</b>
     *
     * <p>Sid creates a post with a valid title and body but leaves the thread blank.
     * We check that everything a student would need is actually there.</p>
     *
     * <p>Checks:</p>
     * <ul>
     *   <li>addPost returns null — no error means the post was accepted (POST-US1a).</li>
     *   <li>The post gets a non-empty UUID (POST-US1b).</li>
     *   <li>Thread becomes "General" because we left it blank (POST-US1c).</li>
     *   <li>The post shows up in getAllPosts() with the right ID (POST-US2a/b).</li>
     * </ul>
     *
     * @return null on success, or a short description of what failed
     */
    private static String postTest1_CreatePostValid() {
        PostListStorage ps = new PostListStorage();
        new ReplyListStorage(ps);

        // Sid leaves the thread blank — should default to "General" per POST-US1c
        Post post = new Post("My Title", "Body here", "", "Sid");
        String err = ps.addPost(post);

        if (err != null)
            return "addPost returned an unexpected error: " + err;
        if (post.getPostID() == null || post.getPostID().isEmpty())
            return "Post ID wasn't assigned (POST-US1b)";
        if (!"General".equals(post.getThread()))
            return "Expected thread 'General' for blank input, got: " + post.getThread();

        List<Post> all = ps.getAllPosts();
        if (all.isEmpty())
            return "Post not found in getAllPosts() right after creating it (POST-US2a)";
        if (!all.get(0).getPostID().equals(post.getPostID()))
            return "Post in the list has a different ID than the one we just created";

        return null;
    }

    /**
     * <b>Post Test 2 — Whitespace-only title is rejected (POST-US1d).</b>
     *
     * <p>A title of {@code "   "} is functionally empty. We confirm storage rejects it
     * and doesn't silently save a blank-titled post.</p>
     *
     * <p>Checks:</p>
     * <ul>
     *   <li>addPost returns a non-null error string mentioning "title" and "empty".</li>
     *   <li>getAllPosts() still returns an empty list — nothing was saved.</li>
     * </ul>
     *
     * @return null on success, or a short description of what failed
     */
    private static String postTest2_EmptyTitleRejected() {
        PostListStorage ps = new PostListStorage();
        new ReplyListStorage(ps);

        Post post = new Post("   ", "Valid body", "General", "Sai");
        String err = ps.addPost(post);

        if (err == null)
            return "Expected an error for a whitespace-only title but got null";
        if (!err.toLowerCase().contains("title")
                || (!err.toLowerCase().contains("empty")
                    && !err.toLowerCase().contains("whitespace")))
            return "Error should mention 'title' and 'empty'/'whitespace': " + err;
        if (!ps.getAllPosts().isEmpty())
            return "A post with an empty title should not have been saved";

        return null;
    }

    /**
     * <b>Post Test 3 — Whitespace-only body is rejected (POST-US1e).</b>
     *
     * <p>Same idea as the title test but for the body field. A post with only spaces
     * in the body shouldn't be saved.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String postTest3_EmptyBodyRejected() {
        PostListStorage ps = new PostListStorage();
        new ReplyListStorage(ps);

        Post post = new Post("Valid Title", "   ", "General", "Jeremy");
        String err = ps.addPost(post);

        if (err == null)
            return "Expected an error for a whitespace-only body but got null";
        if (!err.toLowerCase().contains("body")
                || (!err.toLowerCase().contains("empty")
                    && !err.toLowerCase().contains("whitespace")))
            return "Error should mention 'body' and 'empty'/'whitespace': " + err;
        if (!ps.getAllPosts().isEmpty())
            return "A post with an empty body should not have been saved";

        return null;
    }

    /**
     * <b>Post Test 4 — Null author is rejected (POST-US1f).</b>
     *
     * <p>Every post needs an author. Passing {@code null} should produce a clear error,
     * not a NullPointerException somewhere down the line.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String postTest4_NullAuthorRejected() {
        PostListStorage ps = new PostListStorage();
        new ReplyListStorage(ps);

        Post post = new Post("Title", "Body", "General", null);
        String err = ps.addPost(post);

        if (err == null)
            return "Expected an error for a null author but got null";
        if (!err.toLowerCase().contains("author"))
            return "Error message should mention 'author': " + err;
        if (!ps.getAllPosts().isEmpty())
            return "A post with a null author should not have been saved";

        return null;
    }

    /**
     * <b>Post Test 5 — All active posts show required fields (POST-US2a, POST-US2b).</b>
     *
     * <p>Arjun and Anannay each create a post with different threads. We then fetch the
     * full list and confirm both posts are there with all four required fields — title,
     * author, thread, and reply count.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String postTest5_ViewAllPosts() {
        PostListStorage ps = new PostListStorage();
        ReplyListStorage rs = new ReplyListStorage(ps);

        ps.addPost(new Post("Arjun's question", "Body1", "General",   "Arjun"));
        ps.addPost(new Post("Anannay's topic",  "Body2", "Logistics", "Anannay"));

        List<Post> all = ps.getAllPosts();
        if (all.size() != 2)
            return "Expected 2 posts, got " + all.size();

        for (Post post : all) {
            if (post.getTitle()  == null) return "A post is missing its title";
            if (post.getAuthor() == null) return "A post is missing its author";
            if (post.getThread() == null) return "A post is missing its thread";
            // reply count must be a real non-negative number
            if (rs.getReplyCount(post.getPostID()) < 0)
                return "Reply count came back negative for post: " + post.getPostID();
        }
        return null;
    }

    /**
     * <b>Post Test 6 — Search: title match, body match, thread filter, and blank
     * keyword (POST-US3a, POST-US3b, POST-US3c, POST-US3d).</b>
     *
     * <p>We load three posts with distinct content and run four search scenarios in
     * sequence. Combining them here keeps the number of tests manageable while still
     * exercising every branch of the search logic.</p>
     *
     * <p>Setup:</p>
     * <ul>
     *   <li>Post A (Sai, thread "General"): title "JavaFX Tutorial", body "Drag and drop"</li>
     *   <li>Post B (Jeremy, thread "General"): title "Generic Topic", body "This discusses recursion"</li>
     *   <li>Post C (Sid, thread "Logistics"): title "exam schedule", body "See the syllabus"</li>
     * </ul>
     *
     * <p>Scenarios:</p>
     * <ol>
     *   <li>"javafx" (lowercase) should match Post A's title only (POST-US3a).</li>
     *   <li>"recursion" should match Post B's body only (POST-US3b).</li>
     *   <li>"exam" with thread "Logistics" should return only Post C (POST-US3c).</li>
     *   <li>Blank/null keyword should return empty list without crashing (POST-US3d).</li>
     * </ol>
     *
     * @return null on success, or a short description of what failed
     */
    private static String postTest6_SearchPosts() {
        PostListStorage ps = new PostListStorage();
        new ReplyListStorage(ps);

        ps.addPost(new Post("JavaFX Tutorial",  "Drag and drop",           "General",   "Sai"));
        ps.addPost(new Post("Generic Topic",    "This discusses recursion", "General",   "Jeremy"));
        ps.addPost(new Post("exam schedule",    "See the syllabus",         "Logistics", "Sid"));

        // POST-US3a: title match, case-insensitive
        List<Post> r1 = ps.searchPosts("javafx", null);
        if (r1.size() != 1 || !"JavaFX Tutorial".equals(r1.get(0).getTitle()))
            return "Title search for 'javafx' should return exactly Sai's post, got: " + r1.size();

        // POST-US3b: body match, case-insensitive
        List<Post> r2 = ps.searchPosts("recursion", null);
        if (r2.size() != 1 || !"Jeremy".equals(r2.get(0).getAuthor()))
            return "Body search for 'recursion' should return Jeremy's post";

        // POST-US3c: thread filter — both General posts have common words, Logistics should narrow it
        List<Post> r3 = ps.searchPosts("exam", "Logistics");
        if (r3.size() != 1 || !"Sid".equals(r3.get(0).getAuthor()))
            return "Thread-filtered search for 'exam' in Logistics should return only Sid's post";

        // POST-US3d: blank keyword returns empty list, not a crash or all posts
        List<Post> r4 = ps.searchPosts("", null);
        if (!r4.isEmpty())
            return "Empty keyword should return empty list, got " + r4.size();

        List<Post> r5 = ps.searchPosts(null, null);
        if (!r5.isEmpty())
            return "Null keyword should return empty list, got " + r5.size();

        return null;
    }

    /**
     * <b>Post Test 7 — Author CRUD and authorization (POST-US4a, POST-US4b,
     * POST-US5a, POST-US5b, POST-US2c, POST-US5d).</b>
     *
     * <p>This test walks through the full update/delete lifecycle in one pass:</p>
     * <ol>
     *   <li>Sid creates a post; Jeremy adds a reply.</li>
     *   <li>Sid updates his own post — should succeed (POST-US4a).</li>
     *   <li>Anannay tries to update Sid's post — should be blocked (POST-US4b).</li>
     *   <li>Anannay tries to delete Sid's post — should be blocked (POST-US5b).</li>
     *   <li>Sid deletes his own post — should succeed, post leaves public list (POST-US5a, POST-US2c).</li>
     *   <li>Jeremy's reply must survive the deletion (POST-US5d).</li>
     * </ol>
     *
     * @return null on success, or a short description of what failed
     */
    private static String postTest7_UpdateDeleteAuthorAndAuth() {
        PostListStorage ps = new PostListStorage();
        ReplyListStorage rs = new ReplyListStorage(ps);

        Post post = new Post("Sid's Post", "Original body", "General", "Sid");
        ps.addPost(post);
        String pid = post.getPostID();

        // Jeremy replies before any deletion
        rs.addReply(new Reply("Jeremy's comment", "Jeremy", pid));

        // POST-US4a: Sid updates his own post
        String upd = ps.updatePost(pid, "Updated Title", "Updated body", "Sid");
        if (upd != null)
            return "Sid's update should succeed but got: " + upd;
        Post afterUpdate = ps.getPostByID(pid);
        if (!"Updated Title".equals(afterUpdate.getTitle()))
            return "Title didn't change after Sid's update";

        // POST-US4b: Anannay can't update someone else's post
        String noUpd = ps.updatePost(pid, "Hacked", "Hacked", "Anannay");
        if (noUpd == null)
            return "Anannay should have been blocked from updating Sid's post";

        // POST-US5b: Anannay can't delete someone else's post
        String noDel = ps.deletePost(pid, "Anannay");
        if (noDel == null)
            return "Anannay should have been blocked from deleting Sid's post";
        boolean stillVisible = false;
        for (Post p : ps.getAllPosts())
            if (pid.equals(p.getPostID())) { stillVisible = true; break; }
        if (!stillVisible)
            return "Sid's post disappeared after Anannay's unauthorized delete attempt";

        // POST-US5a: Sid deletes his own post
        String del = ps.deletePost(pid, "Sid");
        if (del != null)
            return "Sid's own delete should succeed but got: " + del;

        // POST-US2c: post no longer in the public list
        for (Post p : ps.getAllPosts())
            if (pid.equals(p.getPostID()))
                return "Deleted post is still showing in getAllPosts()";

        // POST-US5d: Jeremy's reply must still be there
        List<Reply> replies = rs.getRepliesByPostID(pid);
        if (replies.size() != 1)
            return "Jeremy's reply should survive post deletion; expected 1, got " + replies.size();

        return null;
    }

    // =========================================================================
    //  REPLY TESTS
    // =========================================================================

    /**
     * <b>Reply Test 1 — Happy-path reply creation (REPLY-US1a, REPLY-US1b, REPLY-US2a).</b>
     *
     * <p>Anannay replies to Arjun's post. We verify the reply was accepted, got a
     * unique ID, shows up under the post, and bumped the reply count.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String replyTest1_CreateReplyValid() {
        PostListStorage ps = new PostListStorage();
        ReplyListStorage rs = new ReplyListStorage(ps);

        Post post = new Post("Arjun's topic", "Body", "General", "Arjun");
        ps.addPost(post);
        int countBefore = rs.getReplyCount(post.getPostID());

        Reply reply = new Reply("Anannay's answer", "Anannay", post.getPostID());
        String result = rs.addReply(reply);

        // null is clean success; a "Warning:" prefix is also acceptable (soft-deleted post edge case)
        if (result != null && result.startsWith("Error:"))
            return "addReply returned an error: " + result;
        if (reply.getReplyID() == null || reply.getReplyID().isEmpty())
            return "Reply ID wasn't assigned (REPLY-US1b)";
        if (rs.getRepliesByPostID(post.getPostID()).isEmpty())
            return "Reply doesn't show up under the post (REPLY-US1a)";
        if (rs.getReplyCount(post.getPostID()) != countBefore + 1)
            return "Reply count should have gone up by 1; expected "
                    + (countBefore + 1) + " but got " + rs.getReplyCount(post.getPostID());

        return null;
    }

    /**
     * <b>Reply Test 2 — Whitespace-only reply body is rejected (REPLY-US1c).</b>
     *
     * <p>Just like posts, a reply with nothing but spaces isn't useful and should
     * be rejected before it's stored.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String replyTest2_EmptyBodyRejected() {
        PostListStorage ps = new PostListStorage();
        ReplyListStorage rs = new ReplyListStorage(ps);

        Post post = new Post("Topic", "Body", "General", "Sai");
        ps.addPost(post);

        Reply reply = new Reply("   ", "Jeremy", post.getPostID());
        String err = rs.addReply(reply);

        if (err == null)
            return "Expected an error for a whitespace-only reply body but got null";
        if (!err.toLowerCase().contains("body")
                && !err.toLowerCase().contains("empty")
                && !err.toLowerCase().contains("whitespace"))
            return "Error should mention body/empty/whitespace: " + err;
        if (!rs.getRepliesByPostID(post.getPostID()).isEmpty())
            return "An empty reply should not have been saved";

        return null;
    }

    /**
     * <b>Reply Test 3 — Replying to a non-existent post is blocked (REPLY-US1d).</b>
     *
     * <p>If we allowed replies to posts that don't exist, we'd end up with orphaned
     * data that has nowhere to display. The storage should catch this immediately.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String replyTest3_OrphanReplyBlocked() {
        PostListStorage ps = new PostListStorage();
        ReplyListStorage rs = new ReplyListStorage(ps);

        // this post ID was never stored anywhere
        Reply reply = new Reply("Hi there", "Sid", "made-up-post-id-that-doesnt-exist");
        String err = rs.addReply(reply);

        if (err == null)
            return "Should have gotten an error for replying to a non-existent post";
        if (!err.toLowerCase().contains("exist") && !err.toLowerCase().contains("associated"))
            return "Error should mention that the post doesn't exist: " + err;

        return null;
    }

    /**
     * <b>Reply Test 4 — Replies come back in creation order with all required
     * fields (REPLY-US2a, REPLY-US2b).</b>
     *
     * <p>Sid, Jeremy, and Sai each reply in order. We verify the list comes back
     * in the same sequence with both author and body present for each entry.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String replyTest4_ViewRepliesInOrder() {
        PostListStorage ps = new PostListStorage();
        ReplyListStorage rs = new ReplyListStorage(ps);

        Post post = new Post("Topic", "Body", "General", "Arjun");
        ps.addPost(post);
        String pid = post.getPostID();

        rs.addReply(new Reply("First reply",  "Sid",    pid));
        rs.addReply(new Reply("Second reply", "Jeremy", pid));
        rs.addReply(new Reply("Third reply",  "Sai",    pid));

        List<Reply> replies = rs.getRepliesByPostID(pid);

        if (replies.size() != 3) return "Expected 3 replies, got " + replies.size();
        for (Reply r : replies) {
            if (r.getAuthor() == null) return "A reply is missing its author";
            if (r.getBody()   == null) return "A reply is missing its body";
        }
        // order check
        if (!"First reply" .equals(replies.get(0).getBody())) return "Wrong order at position 0";
        if (!"Second reply".equals(replies.get(1).getBody())) return "Wrong order at position 1";
        if (!"Third reply" .equals(replies.get(2).getBody())) return "Wrong order at position 2";

        return null;
    }

    /**
     * <b>Reply Test 5 — Author can update their own reply (REPLY-US4a).</b>
     *
     * <p>Anannay created the reply, so he should be able to fix a typo. We check
     * that the change actually sticks in storage, not just that no error was thrown.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String replyTest5_UpdateReplyByAuthor() {
        PostListStorage ps = new PostListStorage();
        ReplyListStorage rs = new ReplyListStorage(ps);

        Post post = new Post("Topic", "Body", "General", "Sai");
        ps.addPost(post);

        Reply reply = new Reply("Original text", "Anannay", post.getPostID());
        rs.addReply(reply);

        String err = rs.updateReply(reply.getReplyID(), "Corrected text", "Anannay");
        if (err != null) return "Anannay's own update failed: " + err;

        Reply updated = rs.getReplyByID(reply.getReplyID());
        if (updated == null) return "Can't find the reply after updating it";
        if (!"Corrected text".equals(updated.getBody()))
            return "Body didn't update — still showing: " + updated.getBody();

        return null;
    }

    /**
     * <b>Reply Test 6 — Non-author blocked from both update and delete
     * (REPLY-US4b, REPLY-US5b).</b>
     *
     * <p>Sai wrote a reply. We first confirm Jeremy can't update it, then that
     * Jeremy can't delete it either. The reply text must be intact after both
     * blocked attempts.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String replyTest6_AuthBlockedFromReplyEditDelete() {
        PostListStorage ps = new PostListStorage();
        ReplyListStorage rs = new ReplyListStorage(ps);

        Post post = new Post("Topic", "Body", "General", "Arjun");
        ps.addPost(post);

        Reply reply = new Reply("Sai's reply", "Sai", post.getPostID());
        rs.addReply(reply);

        // REPLY-US4b: Jeremy tries to update Sai's reply
        String noUpd = rs.updateReply(reply.getReplyID(), "Jeremy was here", "Jeremy");
        if (noUpd == null)
            return "Jeremy should have been blocked from updating Sai's reply";
        String body = rs.getReplyByID(reply.getReplyID()).getBody();
        if (!"Sai's reply".equals(body))
            return "Sai's reply body was changed even though Jeremy wasn't authorized";

        // REPLY-US5b: Jeremy tries to delete Sai's reply
        String noDel = rs.deleteReply(reply.getReplyID(), "Jeremy");
        if (noDel == null)
            return "Jeremy should have been blocked from deleting Sai's reply";
        if (rs.getRepliesByPostID(post.getPostID()).isEmpty())
            return "Sai's reply was deleted even though Jeremy wasn't authorized";

        return null;
    }

    /**
     * <b>Reply Test 7 — Author hard-deletes their own reply (REPLY-US5a).</b>
     *
     * <p>Unlike posts, reply deletion is a hard delete — the record is fully removed.
     * After Jeremy deletes his reply it should be gone from both {@code getReplyByID}
     * and the post's reply list.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String replyTest7_DeleteReplyByAuthor() {
        PostListStorage ps = new PostListStorage();
        ReplyListStorage rs = new ReplyListStorage(ps);

        Post post = new Post("Topic", "Body", "General", "Sid");
        ps.addPost(post);

        Reply reply = new Reply("Jeremy's reply", "Jeremy", post.getPostID());
        rs.addReply(reply);
        String rid = reply.getReplyID();

        String err = rs.deleteReply(rid, "Jeremy");
        if (err != null) return "Jeremy's delete should succeed but got: " + err;
        if (rs.getReplyByID(rid) != null)
            return "Reply should be completely gone (hard delete) but getReplyByID still finds it";
        if (!rs.getRepliesByPostID(post.getPostID()).isEmpty())
            return "Reply list should be empty after deleting the only reply";

        return null;
    }

    // =========================================================================
    //  STORAGE INTEGRATION TESTS
    // =========================================================================

    /**
     * <b>Storage Test 1 — Can store and retrieve at least 50 posts (STORAGE-1).</b>
     *
     * <p>There's no explicit cap in the spec, but we want confidence that storage won't
     * choke in a real course with many posts. 50 is a reasonable lower bound.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String storageTest1_StoreManyPosts() {
        PostListStorage ps = new PostListStorage();
        new ReplyListStorage(ps);
        int target = 50;

        for (int i = 0; i < target; i++) {
            Post p = new Post("Title " + i, "Body " + i, "General", "user");
            String err = ps.addPost(p);
            if (err != null) return "Adding post " + i + " failed: " + err;
        }

        List<Post> all = ps.getAllPosts();
        if (all.size() != target)
            return "Expected " + target + " posts, got " + all.size();

        for (Post p : all)
            if (ps.getPostByID(p.getPostID()) == null)
                return "getPostByID returned null for a post that should be there: " + p.getPostID();

        return null;
    }

    /**
     * <b>Storage Test 2 — Fresh storage returns an empty list, not null (STORAGE-2).</b>
     *
     * <p>A null return from {@code getAllPosts()} would crash callers that iterate
     * without a null check. Confirming the no-posts case is handled gracefully.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String storageTest2_FreshStorageEmpty() {
        PostListStorage ps = new PostListStorage();
        new ReplyListStorage(ps);

        List<Post> all = ps.getAllPosts();
        if (all == null)    return "getAllPosts() returned null — should return an empty list";
        if (!all.isEmpty()) return "Expected an empty list on fresh storage, got " + all.size();
        return null;
    }

    /**
     * <b>Storage Test 3 — Reply count stays accurate through adds and deletes
     * (STORAGE-3).</b>
     *
     * <p>We track the count at three moments: zero before any replies, three after
     * adding three, and two after deleting one.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String storageTest3_ReplyCountTracking() {
        PostListStorage ps = new PostListStorage();
        ReplyListStorage rs = new ReplyListStorage(ps);

        Post post = new Post("Topic", "Body", "General", "Sid");
        ps.addPost(post);
        String pid = post.getPostID();

        if (rs.getReplyCount(pid) != 0)
            return "Fresh post should have reply count 0, got " + rs.getReplyCount(pid);

        Reply r1 = new Reply("R1", "Sai",    pid);
        Reply r2 = new Reply("R2", "Jeremy", pid);
        Reply r3 = new Reply("R3", "Arjun",  pid);
        rs.addReply(r1); rs.addReply(r2); rs.addReply(r3);

        if (rs.getReplyCount(pid) != 3)
            return "After 3 adds, count should be 3, got " + rs.getReplyCount(pid);

        rs.deleteReply(r2.getReplyID(), "Jeremy");
        if (rs.getReplyCount(pid) != 2)
            return "After deleting 1, count should be 2, got " + rs.getReplyCount(pid);

        return null;
    }

    /**
     * <b>Storage Test 4 — Two posts keep independent reply lists; replies don't
     * bleed between posts (STORAGE-4, REPLY-US3a).</b>
     *
     * <p>Arjun and Anannay each post. Sai and Jeremy reply only to Arjun's post.
     * We check Arjun's post has 2 replies, Anannay's has 0, and none of Arjun's
     * replies accidentally show up under Anannay's post.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String storageTest4_TwoPostsIndependentReplies() {
        PostListStorage ps = new PostListStorage();
        ReplyListStorage rs = new ReplyListStorage(ps);

        Post postA = new Post("Arjun's post",   "Body A", "General", "Arjun");
        Post postB = new Post("Anannay's post",  "Body B", "General", "Anannay");
        ps.addPost(postA);
        ps.addPost(postB);

        rs.addReply(new Reply("Sai's reply",    "Sai",    postA.getPostID()));
        rs.addReply(new Reply("Jeremy's reply", "Jeremy", postA.getPostID()));

        if (rs.getReplyCount(postA.getPostID()) != 2)
            return "Arjun's post should have 2 replies, got " + rs.getReplyCount(postA.getPostID());

        // Anannay's post has no replies — list should be empty, not null
        List<Reply> forB = rs.getRepliesByPostID(postB.getPostID());
        if (forB == null)
            return "getRepliesByPostID should return empty list, not null";
        if (!forB.isEmpty())
            return "Anannay's post should have 0 replies but got " + forB.size();

        // sanity check that Arjun's replies reference Arjun's post ID
        for (Reply r : rs.getRepliesByPostID(postA.getPostID()))
            if (!postA.getPostID().equals(r.getPostID()))
                return "A reply in Arjun's list has the wrong post ID";

        return null;
    }

    /**
     * <b>Storage Test 5 — Replies survive after their parent post is soft-deleted
     * (POST-US5d, POST-US2c).</b>
     *
     * <p>Integration-level counterpart to the deletion check in postTest7. Here we
     * confirm three things at the storage layer: replies are still there after the
     * post is deleted, the post object still exists (soft delete), and the post no
     * longer shows up in the public list.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String storageTest5_DeletedPostKeepsReplies() {
        PostListStorage ps = new PostListStorage();
        ReplyListStorage rs = new ReplyListStorage(ps);

        Post post = new Post("Topic", "Body", "General", "Sid");
        ps.addPost(post);
        String pid = post.getPostID();

        rs.addReply(new Reply("Sai's reply",    "Sai",    pid));
        rs.addReply(new Reply("Jeremy's reply", "Jeremy", pid));

        ps.deletePost(pid, "Sid");

        // both replies should still be there
        List<Reply> replies = rs.getRepliesByPostID(pid);
        if (replies.size() != 2)
            return "Expected 2 replies to survive post deletion, got " + replies.size();

        // post exists in storage but is marked deleted (soft delete)
        Post deleted = ps.getPostByID(pid);
        if (deleted == null)      return "Post record should still exist after soft delete";
        if (!deleted.isDeleted()) return "isDeleted() should be true after deletion";

        // post should not be in the public list
        for (Post p : ps.getAllPosts())
            if (pid.equals(p.getPostID()))
                return "Deleted post should not appear in getAllPosts()";

        return null;
    }

    // =========================================================================
    //  CONTROLLER LOGIC TESTS
    //
    //  These test ControllerPostDetail.canEditPost() and canEditReply(Reply).
    //  Neither method uses JavaFX, so no toolkit setup is needed.
    //  We pass null for the Stage parameter because these methods never touch it.
    // =========================================================================

    /**
     * <b>Controller Logic Test 1 — canEditPost returns true when the logged-in
     * user is the post's author (CTRL-1).</b>
     *
     * <p>This is what makes the Edit and Delete buttons appear for Sid when he's
     * looking at his own post in the detail view.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String ctrlTest1_CanEditPost_Author() {
        Post post = new Post("Title", "Body", "General", "Sid");
        ControllerPostDetail.init(null, post, "Sid", false, null);
        if (!ControllerPostDetail.canEditPost())
            return "canEditPost should return true when the logged-in user is the author";
        return null;
    }

    /**
     * <b>Controller Logic Test 2 — canEditPost returns true for any admin user
     * (CTRL-2).</b>
     *
     * <p>Admins need to moderate content regardless of who posted it, so the admin
     * flag overrides the author check entirely.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String ctrlTest2_CanEditPost_Admin() {
        // post belongs to Sai, but an admin is viewing it
        Post post = new Post("Title", "Body", "General", "Sai");
        ControllerPostDetail.init(null, post, "adminUser", true, null);
        if (!ControllerPostDetail.canEditPost())
            return "canEditPost should return true for an admin, even if they're not the author";
        return null;
    }

    /**
     * <b>Controller Logic Test 3 — canEditPost returns false for a regular user
     * who isn't the author (CTRL-3, POST-US4b, POST-US5b).</b>
     *
     * <p>Jeremy shouldn't see Edit/Delete buttons on Arjun's post. This is the same
     * authorization rule storage enforces, verified here at the controller level.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String ctrlTest3_CanEditPost_NonAuthor() {
        Post post = new Post("Title", "Body", "General", "Arjun");
        ControllerPostDetail.init(null, post, "Jeremy", false, null);
        if (ControllerPostDetail.canEditPost())
            return "canEditPost should return false when the user isn't the author and isn't an admin";
        return null;
    }

    /**
     * <b>Controller Logic Test 4 — canEditReply returns true when the logged-in
     * user is the reply's author (CTRL-5).</b>
     *
     * <p>Anannay wrote the reply, so he should see Edit/Delete buttons on it when
     * viewing the post detail.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String ctrlTest4_CanEditReply_Author() {
        Post post   = new Post("Title", "Body", "General", "Sid");
        Reply reply = new Reply("Anannay's reply", "Anannay", post.getPostID());
        ControllerPostDetail.init(null, post, "Anannay", false, null);
        if (!ControllerPostDetail.canEditReply(reply))
            return "canEditReply should return true when the user is the reply's author";
        return null;
    }

    /**
     * <b>Controller Logic Test 5 — canEditReply returns false for a user who
     * didn't write the reply (CTRL-6, REPLY-US4b, REPLY-US5b).</b>
     *
     * <p>Sai shouldn't see Edit/Delete buttons on Anannay's reply. Neither author
     * nor admin — should be denied.</p>
     *
     * @return null on success, or a short description of what failed
     */
    private static String ctrlTest5_CanEditReply_NonAuthor() {
        Post post   = new Post("Title", "Body", "General", "Sid");
        Reply reply = new Reply("Anannay's reply", "Anannay", post.getPostID());
        // logged in as Sai, who is neither the reply author nor an admin
        ControllerPostDetail.init(null, post, "Sai", false, null);
        if (ControllerPostDetail.canEditReply(reply))
            return "canEditReply should return false when the user isn't the reply author and isn't an admin";
        return null;
    }
}

