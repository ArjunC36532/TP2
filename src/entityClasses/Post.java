package entityClasses;

import java.util.UUID;

/**
 * <p>Title: Post Class</p>
 *
 * <p>
 * The {@code Post} class is the core domain object representing a single
 * discussion post created by a student in the Student Discussion System.
 * Each post has a unique identifier plus a small set of attributes
 * (title, body, thread, author, deleted) that higher layers (storage,
 * GUI, test cases) use to implement the Post-related Student User Stories.
 * </p>
 *
 * <h2>Attributes and Rationale</h2>
 * <ul>
 *   <li><b>postID</b> – A {@link String} uniquely identifying this post.
 *       It is generated using {@link UUID#randomUUID()} when a student
 *       creates a new post. This satisfies the “unique Post ID” requirement
 *       in User Story 1 (Create Post) and allows storage and replies to
 *       reliably reference the same post over time.</li>
 *   <li><b>title</b> – A short summary of the question or topic. The title
 *       is required when creating a post (User Story 1), is shown in the
 *       list of posts (User Story 2 – View all Posts), and is used when
 *       searching for posts by keyword (User Story 3 – Search Posts).</li>
 *   <li><b>body</b> – The full text content of the post. The body is
 *       required when creating a post (User Story 1), is displayed when
 *       viewing posts (User Story 2), and is searched by keyword (User
 *       Story 3). It can be updated by the original author (User Story 4 –
 *       Update Post).</li>
 *   <li><b>thread</b> – A logical category or thread name (for example
 *       {@code "General"} or {@code "Help"}). If a student does not specify
 *       a thread, the constructor assigns the default value {@code "General"}
 *       so that every post belongs to some thread, as required by
 *       User Story 1. The thread is also displayed in the post list
 *       (User Story 2) and may be used when searching or filtering
 *       (User Story 3).</li>
 *   <li><b>author</b> – The username of the student who created the post.
 *       This is required when creating the post (User Story 1) and is shown
 *       in the post list (User Story 2). Storage and GUI layers use this
 *       value to enforce that only the original author can update or delete
 *       the post (User Story 4 – Update Post, User Story 5 – Delete Post).</li>
 *   <li><b>deleted</b> – A boolean flag used for soft deletion. Instead of
 *       physically removing a post from storage, the system marks it as
 *       deleted. Non-deleted posts appear in the main list (User Story 2).
 *       Deleted posts remain in storage so that replies can still exist and
 *       indicate that the original post has been deleted (User Story 5 –
 *       Delete Post). This design works together with the storage classes
 *       to implement the required behavior.</li>
 * </ul>
 *
 * <h2>Operations Supported</h2>
 * <ul>
 *   <li>Constructors for creating new posts at runtime and reconstructing
 *       existing posts from storage.</li>
 *   <li>Getter methods for each attribute so that the UI, storage, and
 *       test code can read post details when implementing the user stories.</li>
 *   <li>Setter methods for mutable attributes (title, body, thread, author,
 *       deleted). These are used by storage and UI layers after they have
 *       performed validation and authorization checks.</li>
 * </ul>
 *
 * <h2>Relation to Student User Stories (Posts)</h2>
 * <ul>
 *   <li><b>User Story 1 – Create Post</b>: The main constructor generates a
 *       unique {@code postID}, stores title/body/thread/author, and applies
 *       the default thread {@code "General"} when no thread is specified.</li>
 *   <li><b>User Story 2 – View all Posts</b>: The attributes postID, title,
 *       author, thread, and deleted provide everything needed to list all
 *       non-deleted posts and show their details.</li>
 *   <li><b>User Story 3 – Search Posts</b>: The title and body fields hold
 *       the searchable text. This class does not impose any fixed upper
 *       limit on the number of posts or search results.</li>
 *   <li><b>User Story 4 – Update Post</b>: The setters for title and body
 *       allow the storage layer (after verifying the author and validating
 *       new content) to update an existing post.</li>
 *   <li><b>User Story 5 – Delete Post</b>: The {@link #setDeleted(boolean)}
 *       method supports soft deletion so that replies remain available
 *       while the post is marked deleted.</li>
 * </ul>
 */
public class Post {

    /*
     * Private attributes that make up a Post.
     *
     * Mapping to requirements:
     * - postID : required unique identifier (US1, used by storage and replies).
     * - title  : required at creation and shown in lists/search (US1, US2, US3).
     * - body   : required content, viewable and searchable (US1, US2, US3, US4).
     * - thread : category for grouping posts, defaults to "General" (US1, US2).
     * - author : creator of the post, used for authorization (US1, US4, US5).
     * - deleted: soft-delete flag so replies remain while post is hidden (US2, US5).
     */
    private String postID;
    private String title;
    private String body;
    private String thread;
    private String author;
    private boolean deleted;

    /**
     * Default constructor.
     * <p>
     * This no-argument constructor is provided for tools or frameworks that
     * require it (for example, some serialization libraries). The Student
     * Discussion System itself always uses one of the parameterized
     * constructors so that the user stories are satisfied.
     * </p>
     */
    public Post() {
        // Intentionally left blank; real posts should use a parameterized constructor.
    }

    /**
     * Constructs a new {@code Post} when a student creates a post.
     * <p>
     * This constructor:
     * </p>
     * <ul>
     *   <li>Generates a unique {@code postID} using {@link UUID#randomUUID()}.</li>
     *   <li>Stores the provided title, body, thread, and author.</li>
     *   <li>If {@code thread} is {@code null} or blank, substitutes the default
     *       thread name {@code "General"}.</li>
     *   <li>Initializes {@code deleted} to {@code false} so the post appears in
     *       the main list of posts.</li>
     * </ul>
     *
     * <p>
     * Higher layers (for example, storage or GUI) are responsible for checking
     * that title/body/thread/author are not null, empty, or whitespace-only,
     * and for displaying any error messages when validation fails.
     * </p>
     *
     * @param title  the title of the post (expected to be non-null/non-empty)
     * @param body   the body text of the post (expected to be non-null/non-empty)
     * @param thread the thread name, or {@code null}/blank to use {@code "General"}
     * @param author the username of the student who created the post
     */
    public Post(String title, String body, String thread, String author) {
        // US1: generate a unique Post ID for every new post.
        this.postID = UUID.randomUUID().toString();

        // US1, US2, US3, US4: core text that will be displayed, searched, and updated.
        this.title = title;
        this.body = body;

        // US1: if no thread is provided, default to "General".
        if (thread == null || thread.trim().isEmpty()) {
            this.thread = "General";
        } else {
            this.thread = thread;
        }

        // US1, US4, US5: stored author used for authorization checks in higher layers.
        this.author = author;

        // US2: new posts start as not deleted so they appear in the main list.
        this.deleted = false;
    }

    /**
     * Constructs a {@code Post} with all attributes explicitly provided.
     * <p>
     * This constructor is typically used by storage code when loading posts
     * that were previously saved. It allows the original {@code postID} and
     * {@code deleted} flag to be restored exactly as they were stored.
     * </p>
     *
     * @param postID  the unique identifier for this post (as stored previously)
     * @param title   the title of the post
     * @param body    the body text of the post
     * @param thread  the thread name for this post
     * @param author  the username of the student who created the post
     * @param deleted {@code true} if the post has been soft-deleted; {@code false} otherwise
     */
    public Post(String postID, String title, String body, String thread, String author, boolean deleted) {
        this.postID = postID;
        this.title = title;
        this.body = body;
        this.thread = thread;
        this.author = author;
        this.deleted = deleted;
    }

    /**
     * Returns the unique identifier for this post.
     *
     * @return the {@link String} post ID
     */
    public String getPostID() {
        return postID;
    }

    /**
     * Returns the current title of this post.
     *
     * @return the title of the post
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets a new title for this post.
     * <p>
     * Higher layers must ensure that only the original author can update
     * the title and that the new title is valid (not null, empty, or
     * whitespace-only).
     * </p>
     *
     * @param title the new title to assign
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the body text of this post.
     *
     * @return the body content
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets new body content for this post.
     * <p>
     * Higher layers must ensure that only the original author can update
     * the body and that the new content is valid.
     * </p>
     *
     * @param body the new body text to assign
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Returns the thread name associated with this post.
     *
     * @return the thread name
     */
    public String getThread() {
        return thread;
    }

    /**
     * Sets the thread name for this post.
     * <p>
     * This method allows higher layers to move a post to a different
     * thread if that behavior is supported. Validation is handled outside
     * this class.
     * </p>
     *
     * @param thread the new thread name
     */
    public void setThread(String thread) {
        this.thread = thread;
    }

    /**
     * Returns the username of the student who created this post.
     *
     * @return the author username
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author username for this post.
     * <p>
     * In the normal flow, the author is assigned once at creation time
     * and does not change. This setter exists mainly for completeness
     * and for potential administrative tools.
     * </p>
     *
     * @param author the new author username
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns whether this post has been soft-deleted.
     * <p>
     * A value of {@code true} means the post should not appear in the
     * main list of posts, but it may still be present in storage so
     * that replies can remain and indicate that the original post was
     * deleted.
     * </p>
     *
     * @return {@code true} if this post is marked as deleted; {@code false} otherwise
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Marks this post as deleted or not deleted.
     * <p>
     * Higher layers call this method only after the student has confirmed
     * deletion and after any authorization checks have passed. The system
     * uses this flag for soft deletion to satisfy the requirement that
     * replies remain even when the post is deleted.
     * </p>
     *
     * @param deleted {@code true} to mark the post as deleted; {@code false} to mark it as not deleted
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}