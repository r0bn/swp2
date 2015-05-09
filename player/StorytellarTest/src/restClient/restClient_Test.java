package restClient;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.NEW;

import android.test.AndroidTestCase;

public class restClient_Test {

	RESTClient client;
	String URLallStories = "http://api.storytellar.de/temp";
	String URLmediaData = "http://api.storytellar.de/media";

	/**
	 * Tests, if Constructor of RESTClient works.
	 */
	@Test
	public void test_ClientNotNull() {
		client = new RESTClient();
		assertNotNull(client);
	}

	/**
	 * Tests, if readInputMethod returns right string "Test"
	 */
	@Test
	public void test_readInput() {
		client = new RESTClient();
		Method method;
		final String testString = "Test";
		try {
			Class[] cArg = new Class[1];
			cArg[0] = InputStream.class;
			method = RESTClient.class.getDeclaredMethod("readInput", cArg);
			method.setAccessible(true);
			InputStream testStream = new InputStream() {
				int call = 0;
				byte[] result = testString.getBytes();

				@Override
				public int read() throws IOException {
					if (call < result.length) {
						int i = result[call];
						call++;
						return i;
					} else {
						return -1;
					}
				}
			};
			Object[] params = new Object[1];
			params[0] = testStream;
			String result = (String) method.invoke(client, params);
			assertEquals("Result is wrong", testString, result);
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Tests, if readInput method can handle empty String
	 */
	@Test
	public void test_readInput_EmptyStream() {
		client = new RESTClient();
		Method method;
		final String testString = "";
		try {
			Class[] cArg = new Class[1];
			cArg[0] = InputStream.class;
			method = RESTClient.class.getDeclaredMethod("readInput", cArg);
			method.setAccessible(true);
			InputStream testStream = new InputStream() {
				int call = 0;
				byte[] result = testString.getBytes();

				@Override
				public int read() throws IOException {
					if (call < result.length) {
						int i = result[call];
						call++;
						return i;
					} else {
						return -1;
					}
				}
			};
			Object[] params = new Object[1];
			params[0] = testStream;
			String result = (String) method.invoke(client, params);
			assertEquals("Result is wrong", testString, result);
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void test_getAvailableStories() {
		try {
			JSONArray result =  new JSONArray();
			result.put(0, new JSONObject("{\"id\":\"1\",\"title\":\"Schneewittchen und die sieben Zwerge\",\"description\":\"Die halbwüchsigen kehren zurück\",\"author\":\"Grimm\",\"revision\":\"7\",\"size\":\"10\",\"size_uom\":\"MB\",\"location\":\"48.783375 9.181187\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-01 17:08:31\",\"updated_at\":\"2015-05-01 18:09:37\"}"));
			result.put(1, new JSONObject("{\"id\":\"2\",\"title\":\"Aschenputtel\",\"description\":\"Das arme Ding\",\"author\":\"Lukas\",\"revision\":\"7\",\"size\":\"20\",\"size_uom\":\"MB\",\"location\":\"48.799353 9.003754\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-02 12:32:58\",\"updated_at\":\"2015-05-02 14:37:44\"}"));
			result.put(2, new JSONObject("{\"id\":\"3\",\"title\":\"Schneewittchen\",\"description\":\"Das arme Ding\",\"author\":\"Lukas\",\"revision\":\"7\",\"size\":\"30\",\"size_uom\":\"MB\",\"location\":\"53.552647 10.007829\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-03 13:02:19\",\"updated_at\":\"2015-05-03 16:45:02\"}"));
			
			client = new RESTClient();
			Field allStories = RESTClient.class
					.getDeclaredField("URLallStories");
			allStories.setAccessible(true);
			allStories.set(client, URLallStories);
			
			JSONArray toTest = client.getAvailableStories();
			for(int i = 0;i<result.length();i++){
				assertEquals(result.getJSONObject(i).get("id"),toTest.getJSONObject(i).get("id"));
				assertEquals(result.getJSONObject(i).get("title"),toTest.getJSONObject(i).get("title"));
				assertEquals(result.getJSONObject(i).get("description"),toTest.getJSONObject(i).get("description"));
				assertEquals(result.getJSONObject(i).get("author"),toTest.getJSONObject(i).get("author"));
				assertEquals(result.getJSONObject(i).get("revision"),toTest.getJSONObject(i).get("revision"));
				assertEquals(result.getJSONObject(i).get("size"),toTest.getJSONObject(i).get("size"));
				assertEquals(result.getJSONObject(i).get("size_uom"),toTest.getJSONObject(i).get("size_uom"));
				assertEquals(result.getJSONObject(i).get("created_at"),toTest.getJSONObject(i).get("created_at"));
				assertEquals(result.getJSONObject(i).get("location"),toTest.getJSONObject(i).get("location"));
				assertEquals(result.getJSONObject(i).get("radius"),toTest.getJSONObject(i).get("radius"));
				assertEquals(result.getJSONObject(i).get("radius_uom"),toTest.getJSONObject(i).get("radius_uom"));
				assertEquals(result.getJSONObject(i).get("updated_at"),toTest.getJSONObject(i).get("updated_at"));
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
