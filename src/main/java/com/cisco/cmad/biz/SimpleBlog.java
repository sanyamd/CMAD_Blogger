package com.cisco.cmad.biz;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.cisco.cmad.api.Blog;
import com.cisco.cmad.api.BlogException;
import com.cisco.cmad.api.PostNotFoundException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class SimpleBlog implements Blog {

	MongoDatabase db;
	MongoCollection<Document> blogs;
	MongoCollection<Document> users;

	public SimpleBlog() {
		MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://130.211.115.181:27017"));
		db = mongo.getDatabase("CMAD_Blogger");
	}
	
	public SimpleBlog(MongoClient mongo) {
		db = mongo.getDatabase("CMAD_Blogger");
	}

	@Override
	public String addUser(String userDetailsJson) throws BlogException {
		Document userData = Document.parse(userDetailsJson);
		userData.put("avatarImgSrc", "./img/img_avatar"+randomInt(1, 6)+".png");
		String email = (String) userData.get("email");
		boolean writtenToDb = false;

		Document result = new Document();
		result.put("email", email);
		result.put("writtenToDb", writtenToDb);

		users = db.getCollection("users");

		try {
			users.insertOne(userData);
			writtenToDb = true;
		} catch (Exception e) {
			writtenToDb = false;
		}
		result.put("writtenToDb", writtenToDb);
		return result.toJson();
	}
	
	private static int randomInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}

	@Override
	public String updateUser(String userDetailsJson) throws BlogException {
		Document userData = Document.parse(userDetailsJson);
		Document update = new Document("$set", userData);
		String email = (String) userData.get("email");
		boolean writtenToDb = false;

		Document result = new Document();
		result.put("email", email);
		result.put("writtenToDb", writtenToDb);

		users = db.getCollection("users");
		Bson bson = eq("email", email);
//		System.out.println("SimpleBlog.updateUser() : "+bson);
		try {
			users.updateOne(bson, update);
			writtenToDb = true;
		} catch (Exception e) {
			writtenToDb = false;
		}
		result.put("writtenToDb", writtenToDb);
		return result.toJson();
	}

	@Override
	public String getUserInfo(String emailId) throws BlogException {
//		System.out.println("SimpleBlog.getUserInfo()"+emailId);
		Document result = new Document();
		result.put("email", emailId);
		result.put("matchFound", false);
		result.put("userInfo", "{}");

		users = db.getCollection("users");

		if (users.count() > 0) {
			FindIterable<Document> find = users.find(eq("email", emailId)).limit(1);

			if (find != null && find.iterator() != null) {
				MongoCursor<Document> cursor = find.iterator();

				while (cursor.hasNext()) {
					Document doc = cursor.next();

					if (doc != null) {
						result.put("matchFound", true);
						result.put("userInfo", doc);
						return result.toJson();
					}
				}
			}
		}
		return result.toJson();
	}

	@Override
	public String addPost(String postJson) throws BlogException {
		Document post = Document.parse(postJson);
		String blogID = ""+ post.get("blogID");
		post.put("blogID", blogID);
		boolean writtenToDb = false;

		Document result = new Document();
		result.put("blogID", blogID);
		result.put("writtenToDb", writtenToDb);

		blogs = db.getCollection("blogs");

		try {
			blogs.insertOne(post);
			writtenToDb = true;
		} catch (Exception e) {
			writtenToDb = false;
		}
		result.put("writtenToDb", writtenToDb);
		return result.toJson();
	}

	@Override
	public String addComment(String commentJson, int blogId) throws BlogException {
		Document comment = Document.parse(commentJson);
		boolean writtenToDb = false, postFound = false;
		Document result = new Document();
		result.put("blogID", blogId);
		result.put("writtenToDb", writtenToDb);
		result.put("post", "{}");

		blogs = db.getCollection("blogs");

		try {
			Document doc = blogs.findOneAndUpdate(eq("blogID", String.valueOf(blogId)),
					new Document("$push", new Document("blogComments", comment)));
			if (doc != null && !doc.get("blogID").equals("")) {
				writtenToDb = true;
			}
			postFound = findPost(blogId, postFound, result);
			result.put("postFound", postFound);
		} catch (Exception e) {
			writtenToDb = false;
		}
		result.put("writtenToDb", writtenToDb);
		return result.toJson();
	}

	@Override
	public List<String> getPosts() throws BlogException {
		blogs = db.getCollection("blogs");

		List<String> list = new ArrayList<String>();

		FindIterable<Document> findIterable = blogs.find();
		for (Document doc : findIterable){
			doc.remove("_id");
			list.add(doc.toJson());
		}
		return list;
	}

	@Override
	public int getPostsCount() throws BlogException {
		blogs = db.getCollection("blogs");
		int count = (int)blogs.count();
		return count;
	}

	@Override
	public List<String> getPosts(String searchString) throws BlogException {
		blogs = db.getCollection("blogs");
		List<String> list = new ArrayList<String>();
		blogs.dropIndexes();
		blogs.createIndex(new Document("$**", "text"));

		FindIterable<Document> findIterable = blogs.find(Filters.text(searchString));
//		System.out.println("SimpleBlog.getPosts() : findIterable : "+findIterable);
		for (Document doc : findIterable){
//			System.out.println("SimpleBlog.getPosts() : doc : "+doc);
			list.add(doc.toJson());
		}
//		System.out.println(list);
		return list;
	}

	@Override
	public String getPost(int postId) throws PostNotFoundException, BlogException {
		boolean postFound = false;
		Document result = new Document();
		result.put("blogID", postId);
		result.put("post", "{}");
		result.put("postFound", postFound);

		blogs = db.getCollection("blogs");
		postFound = findPost(postId, postFound, result);
		result.put("postFound", postFound);
		System.out.println("SimpleBlog.getPost() : result.toJson() : "+result.toJson());
		return result.toJson();
	}

	private boolean findPost(int postId, boolean postFound, Document result) {
		try {
			FindIterable<Document> find = blogs.find(eq("blogID", String.valueOf(postId)));
			System.out.println("SimpleBlog.findPost() : find : "+find.iterator());
			if (find != null && find.iterator() != null) {
				MongoCursor<Document> cursor = find.iterator();
				System.out.println("SimpleBlog.findPost() : cursor : "+cursor.hasNext());
				
				if (cursor != null && cursor.hasNext()) {
					Document doc = cursor.next();
					System.out.println("SimpleBlog.findPost() : doc : "+doc);
					if (doc != null) {
						postFound = true;
						doc.remove("_id");
						result.put("post", doc);
					}
				}
			}
		} catch (Exception e) {
			postFound = false;
		}
		return postFound;
	}

	@Override
	public String signIn(String loginDataJson) {
		Document userData = Document.parse(loginDataJson);
		String email = (String) userData.get("email");

		Document result = new Document();
		result.put("email", email);
		result.put("matchFound", false);

		users = db.getCollection("users");

		if (users.count() > 0) {
			FindIterable<Document> find = users.find(eq("email", email)).limit(1);

			if (find != null && find.iterator() != null) {
				MongoCursor<Document> cursor = find.iterator();

				while (cursor.hasNext()) {
					Document doc = cursor.next();
					String password = (String) doc.get("password");

					if (password != null && userData.get("password").equals(password)) {
						doc.put("password", "");
						doc.remove("_id");
						result.put("matchFound", true);
						result.put("userInfo", doc);
						return result.toJson();
					}
				}
			}
		}
		return result.toJson();
	}

	public static void main(String... args) {
//		System.out.println(randInt(1, 6));;
//		 System.out.println(new SimpleBlog().getPosts("after"));
//		new SimpleBlog().getPosts("technology");
//		new SimpleBlog().getPost(1);
		/*Document comment = new Document();
		comment.put("commentId", "1");
		comment.put("comment", "Wow gr8");
		comment.put("commentAuthor", "kapil");
		comment.put("avatarImgSrc", "./img/avatar6.png");
		comment.put("commentedOn", "10 Mar, 2017");

		System.out.println(new SimpleBlog().addComment(comment.toJson(), 2));*/
		System.out.println(new SimpleBlog().getPost(11));
	}

}