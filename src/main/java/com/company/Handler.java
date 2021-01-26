package com.company;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;


public class Handler
{

	//File file = new File("D:\\Projekty\\SSLExample\\src\\main\\java\\com\\company\\database.xml");
	File file = new File("C:\\Users\\Luksor\\IdeaProjects\\SSLExample\\src\\main\\java\\com\\company\\database.xml");
	Unmarshaller jaxbUnmarshaller;
	Users users;
	List<User> userList;
	Marshaller jaxbMarshaller;

	public Handler() {
	}

    public long getPhrase(String line) throws IOException {

		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(
						new HttpHost("localhost", 9200, "http"),
						new HttpHost("localhost", 9201, "http")));


		XContentBuilder builder = XContentFactory.jsonBuilder();
		builder.startObject();
		{
			builder.field("user", "kimchy");
			builder.timeField("postDate", new Date());
			builder.field("message", "baca baca trying try baca baca");
		}
		builder.endObject();
		IndexRequest indexRequest = new IndexRequest("posts")
				.id("13").source(builder);

		client.index(indexRequest, RequestOptions.DEFAULT);

		//SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		//sourceBuilder.query(QueryBuilders.matchAllQuery());
		//SearchRequest searchRequest = new SearchRequest("posts");
		//searchRequest.source(sourceBuilder);

//		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//		SearchHits hits = searchResponse.getHits();
//		SearchHit[] searchHits = hits.getHits();
//		long totalHits = hits.getTotalHits().value;

//		return totalHits;




		//GetRequest getRequest = new GetRequest(
		//		"posts",
		//		"2");

		//getRequest.storedFields("message");
		//GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
		//String message = getResponse.getField("message").getValue();
		return 34;
	/*
		if (getResponse.isExists()) {
			long version = getResponse.getVersion();
			String sourceAsString = getResponse.getSourceAsString();
			Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
			byte[] sourceAsBytes = getResponse.getSourceAsBytes();
		} else {

		}

		if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {

		} else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {

		}
		ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
		if (shardInfo.getTotal() != shardInfo.getSuccessful()) {

		}
		if (shardInfo.getFailed() > 0) {
			for (ReplicationResponse.ShardInfo.Failure failure :
					shardInfo.getFailures()) {
				String reason = failure.reason();
			}
		}
	*/

    }

	public boolean checkUser(String login, String password) {
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			users = (Users) jaxbUnmarshaller.unmarshal(file);
			userList = users.getUsers();
			for (int i = 0; i < userList.size(); i++) {
				if(userList.get(i).login.equals(login) && userList.get(i).password.equals(password)){
					return true;
				}
			}

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean checkLogin(String login) {
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			users = (Users) jaxbUnmarshaller.unmarshal(file);
			userList = users.getUsers();
			for (int i = 0; i < userList.size(); i++) {
				if(userList.get(i).login.equals(login)){
					return true;
				}
			}

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void updatePassword(String login, String password, String newPassword, String newQuestion) {
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			users = (Users) jaxbUnmarshaller.unmarshal(file);
			userList = users.getUsers();
			for (int i = 0; i < userList.size(); i++) {
				if(userList.get(i).login.equals(login) && userList.get(i).password.equals(password)){
					userList.get(i).password = newPassword;
					userList.get(i).question = newQuestion;
				}
			}
			updateDatabase(userList);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private void updateDatabase(List<User> userList) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			users = (Users) jaxbUnmarshaller.unmarshal(file);
			users.setUsers(userList);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.marshal(users, file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void updateLogin(String login, String password, String newLogin) {
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			users = (Users) jaxbUnmarshaller.unmarshal(file);
			userList = users.getUsers();
			for (int i = 0; i < userList.size(); i++) {
				if(userList.get(i).login.equals(login) && userList.get(i).password.equals(password)){
					userList.get(i).login = newLogin;
				}
			}
			updateDatabase(userList);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void createAcc(String line, String password, String question) {
		User newUser = new User();
		newUser.setLogin(line);
		newUser.setPassword(password);
		newUser.setQuestion(question);

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			users = (Users) jaxbUnmarshaller.unmarshal(file);
			userList = users.getUsers();
			userList.add(newUser);
			users.setUsers(userList);
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.marshal(users, file);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public String showQuestion(String login) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			users = (Users) jaxbUnmarshaller.unmarshal(file);
			userList = users.getUsers();
			for (int i = 0; i < userList.size(); i++) {
				if(userList.get(i).login.equals(login)){
					login = userList.get(i).question;
					return login;
				}
			}

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
}