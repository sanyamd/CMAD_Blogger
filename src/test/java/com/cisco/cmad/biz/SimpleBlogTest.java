package com.cisco.cmad.biz;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@RunWith(PowerMockRunner.class)
@PrepareForTest ({Document.class, Filters.class, SimpleBlog.class})
public class SimpleBlogTest {
	SimpleBlog simpleBlog;
	MongoClient mongo;
	MongoDatabase db;
	MongoCollection<Document> blogs;
	MongoCollection<Document> users;

	@Before
	public void setUp() {
		mongo = Mockito.mock(MongoClient.class);
		db = Mockito.mock(MongoDatabase.class);
		blogs = Mockito.mock(MongoCollection.class);
		users = Mockito.mock(MongoCollection.class);
		Mockito.when(mongo.getDatabase("CMAD_Blogger")).thenReturn(db);
		simpleBlog = new SimpleBlog(mongo);
		Mockito.when(db.getCollection("blogs")).thenReturn(blogs);
		Mockito.when(db.getCollection("users")).thenReturn(users);
	}
	
	@After
	public void tearDown() {
		mongo = null; 
		db = null; 
		blogs = users = null;
	}
	
	@Test
	public void testAddUser() {
		//Setup
		PowerMockito.mockStatic(Document.class);
		String userDetailsJson = "{\"email\":\"abc@xyz.com\"}";
		Document userData = Mockito.mock(Document.class);
		PowerMockito.when(Document.parse(userDetailsJson)).thenReturn(userData);
		
		// Method Call
		simpleBlog.addUser(userDetailsJson);
		
		// Verification
		verify(users).insertOne(userData);
	}
	
	@Test
	public void testUpdateUser() throws Exception {
		//Setup
		PowerMockito.mockStatic(Document.class);
		PowerMockito.mockStatic(Filters.class);
		Bson bson = Mockito.mock(Bson.class);
		Document update = Mockito.mock(Document.class);
		String userDetailsJson = "{\"email\":\"abc@xyz.com\"}";
		Document userData = Mockito.mock(Document.class);
		Document result = Mockito.mock(Document.class);
		Mockito.stub(userData.get("email")).toReturn("abc@xyz.com");
		PowerMockito.when(Document.parse(userDetailsJson)).thenReturn(userData);
		String str = "$set";
		PowerMockito.whenNew(Document.class).withArguments(str, userData).thenReturn(update);
		PowerMockito.whenNew(Document.class).withNoArguments().thenReturn(result);
		PowerMockito.when(Filters.eq("email", "abc@xyz.com")).thenReturn(bson);
		
		// Method Call
		simpleBlog.updateUser(userDetailsJson);
		
		// Verification
		verify(users).updateOne(bson, update);
	}
	
	@Test
	public void testGetUserInfo() throws Exception {
		//Setup
		String emailId = "abc@xyz.com";
		PowerMockito.mockStatic(Document.class);
		PowerMockito.mockStatic(Filters.class);
		Document result = Mockito.mock(Document.class);
		PowerMockito.whenNew(Document.class).withNoArguments().thenReturn(result);
		
		Bson bson = Mockito.mock(Bson.class);
		PowerMockito.when(Filters.eq("email", "abc@xyz.com")).thenReturn(bson);
		Mockito.stub(users.count()).toReturn((long)1);
		
		FindIterable iterable = Mockito.mock(FindIterable.class),
					 iterable1 = Mockito.mock(FindIterable.class);
		Mockito.stub(users.find(bson)).toReturn(iterable);
		Mockito.stub(iterable.limit(1)).toReturn(iterable1);
		
		MongoCursor mongoCursor = Mockito.mock(MongoCursor.class);
		Mockito.stub(iterable1.iterator()).toReturn(mongoCursor);
		Mockito.stub(mongoCursor.hasNext()).toReturn(true);
		
		Document doc = Mockito.mock(Document.class);
		Mockito.stub(mongoCursor.next()).toReturn(doc);
		
		// Method Call
		simpleBlog.getUserInfo(emailId);

		// Verification
		verify(users).find(bson);
		verify(iterable).limit(1);
		Assert.assertEquals(mongoCursor.next(), doc);
	}
	
	@Test
	public void testAddPost() throws Exception {
		// Setup
		PowerMockito.mockStatic(Document.class);
		String postJson = "{\"email\":\"abc@xyz.com\"}";
		String resultJson = "{\"blogID\":\"1\", \"blogPost\":\"Sample Blog\", \"blogTitle\":\"Sample\"}";
		Document post = Mockito.mock(Document.class);
		Document result = Mockito.mock(Document.class);
		Mockito.stub(post.get("blogID")).toReturn("1");
		PowerMockito.when(Document.parse(postJson)).thenReturn(post);
		PowerMockito.whenNew(Document.class).withNoArguments().thenReturn(result);
		Mockito.stub(result.toJson()).toReturn(resultJson);
		
		// Method Call
		simpleBlog.addPost(postJson);
		
		// Verification
		Assert.assertEquals(result.toJson(), resultJson);
		verify(blogs).insertOne(post);
	}

	@Test
	public void testAddComment() throws Exception {
		// Setup
		PowerMockito.mockStatic(Document.class);
		PowerMockito.mockStatic(Filters.class);
		Bson bson = Mockito.mock(Bson.class);
		String commentJson = "{\"email\":\"abc@xyz.com\"}";
		String resultJson = "{\"blogID\":\"1\", \"writtenToDB\":\"true\", \"post\":\"{}\"}";
		Document comment = Mockito.mock(Document.class);
		Document result = Mockito.mock(Document.class);
		Mockito.stub(comment.get("blogID")).toReturn("1");
		PowerMockito.when(Document.parse(commentJson)).thenReturn(comment);
		PowerMockito.whenNew(Document.class).withNoArguments().thenReturn(result);
		Mockito.stub(result.toJson()).toReturn(resultJson);
		Document outerDocument = mockPushingComment(comment, result, bson);

		// Method Call
		simpleBlog.addComment(commentJson, 1);
		
		// Verification
		Assert.assertEquals(result.toJson(), resultJson);
		verify(blogs).findOneAndUpdate(bson, outerDocument);
	}
	
	@Test
	public void testGetPosts() throws Exception {
		// Setup
		PowerMockito.mockStatic(ArrayList.class);
		PowerMockito.mockStatic(Document.class);
		ArrayList list = Mockito.mock(ArrayList.class);
		FindIterable iterable = Mockito.mock(FindIterable.class);
		Document doc = Mockito.mock(Document.class);
		MongoCursor cursor = Mockito.mock(MongoCursor.class);
		Mockito.stub(blogs.find()).toReturn(iterable);
		PowerMockito.whenNew(ArrayList.class).withNoArguments().thenReturn(list);
		Mockito.stub(iterable.iterator()).toReturn(cursor);
		Mockito.stub(cursor.next()).toReturn(doc);

		// Method call
		simpleBlog.getPosts();

		// Verification
		verify(blogs).find();
	}
	
	@Test
	public void testGetPostsCount() {
		// Setup
		Mockito.stub(blogs.count()).toReturn((long)5);
		
		// Method call and Verification
		Assert.assertEquals(simpleBlog.getPostsCount(), 5);
	}
	
	@Test
	public void testGetPostsSearch() throws Exception {
		// Setup
		String searchString = "Sports";
		PowerMockito.mockStatic(ArrayList.class);
		PowerMockito.mockStatic(Document.class);
		PowerMockito.mockStatic(Filters.class);
		ArrayList list = Mockito.mock(ArrayList.class);
		FindIterable iterable = Mockito.mock(FindIterable.class);
		Document doc = Mockito.mock(Document.class);
		PowerMockito.whenNew(ArrayList.class).withNoArguments().thenReturn(list);
		PowerMockito.whenNew(Document.class).withArguments("$**", "text").thenReturn(doc);
		Bson bson = Mockito.mock(Bson.class);
		MongoCursor cursor = Mockito.mock(MongoCursor.class);
		Mockito.stub(Filters.text(searchString)).toReturn(bson);
		Mockito.stub(blogs.find(bson)).toReturn(iterable);
		Mockito.stub(iterable.iterator()).toReturn(cursor);
		Mockito.stub(cursor.next()).toReturn(doc);

		// Method call
		simpleBlog.getPosts(searchString);
		
		// Verification
		verify(blogs).dropIndexes();
		verify(blogs).createIndex(doc);
	}
	
	
	@Test
	public void testSignIn() throws Exception {
		// Setup
		PowerMockito.mockStatic(Document.class);
		PowerMockito.mockStatic(Filters.class);
		String userDetailsJson = "{\"email\":\"abc@xyz.com\"}";
		Document userData = Mockito.mock(Document.class);
		PowerMockito.when(Document.parse(userDetailsJson)).thenReturn(userData);
		Mockito.stub(userData.get("email")).toReturn("abc@xyz.com");
		Document result = Mockito.mock(Document.class);
		PowerMockito.whenNew(Document.class).withNoArguments().thenReturn(result);

		Bson bson = Mockito.mock(Bson.class);
		PowerMockito.when(Filters.eq("email", "abc@xyz.com")).thenReturn(bson);
		Mockito.stub(users.count()).toReturn((long)1);
		
		FindIterable iterable = Mockito.mock(FindIterable.class),
					 iterable1 = Mockito.mock(FindIterable.class);
		Mockito.stub(users.find(bson)).toReturn(iterable);
		Mockito.stub(iterable.limit(1)).toReturn(iterable1);

		MongoCursor mongoCursor = Mockito.mock(MongoCursor.class);
		Mockito.stub(iterable1.iterator()).toReturn(mongoCursor);
		Mockito.stub(mongoCursor.hasNext()).toReturn(true);
		
		Document doc1 = Mockito.mock(Document.class);
		Mockito.stub(mongoCursor.next()).toReturn(doc1);
		Mockito.stub(doc1.get("password")).toReturn("password");
		Mockito.stub(userData.get("password")).toReturn("password");
		
		// Method call
		simpleBlog.signIn(userDetailsJson);
		
		// Verification
		verify(result).put("matchFound", true);
	}

	private Document mockPushingComment(Document comment, Document result, Bson bson) throws Exception {
		PowerMockito.when(Filters.eq("blogID", "1")).thenReturn(bson);
		Mockito.stub(users.count()).toReturn((long)1);
		Document pushingDoc = Mockito.mock(Document.class);
		Document outerDoc = Mockito.mock(Document.class);
		PowerMockito.whenNew(Document.class).withArguments("blogComments", comment).thenReturn(pushingDoc);
		PowerMockito.whenNew(Document.class).withArguments("$push", pushingDoc).thenReturn(outerDoc);
		return outerDoc;
	}
}
