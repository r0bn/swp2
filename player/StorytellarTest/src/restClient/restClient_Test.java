package restClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;

import android.os.Environment;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import de.hft_stuttgart.spirit.android.Story;

public class restClient_Test extends InstrumentationTestCase {

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
			JSONArray result = new JSONArray();
			result.put(
					0,
					new JSONObject(
							"{\"id\":\"1\",\"title\":\"Schneewittchen und die sieben Zwerge\",\"description\":\"Die halbwüchsigen kehren zurück\",\"author\":\"Grimm\",\"revision\":\"7\",\"size\":\"10\",\"size_uom\":\"MB\",\"location\":\"48.783375 9.181187\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-01 17:08:31\",\"updated_at\":\"2015-05-01 18:09:37\"}"));
			result.put(
					1,
					new JSONObject(
							"{\"id\":\"2\",\"title\":\"Aschenputtel\",\"description\":\"Das arme Ding\",\"author\":\"Lukas\",\"revision\":\"7\",\"size\":\"20\",\"size_uom\":\"MB\",\"location\":\"48.799353 9.003754\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-02 12:32:58\",\"updated_at\":\"2015-05-02 14:37:44\"}"));
			result.put(
					2,
					new JSONObject(
							"{\"id\":\"3\",\"title\":\"Schneewittchen\",\"description\":\"Das arme Ding\",\"author\":\"Lukas\",\"revision\":\"7\",\"size\":\"30\",\"size_uom\":\"MB\",\"location\":\"53.552647 10.007829\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-03 13:02:19\",\"updated_at\":\"2015-05-03 16:45:02\"}"));

			client = new RESTClient();
			Field allStories = RESTClient.class
					.getDeclaredField("URLallStories");
			allStories.setAccessible(true);
			allStories.set(client, URLallStories);

			JSONArray toTest = client.getAvailableStories();
			if (toTest.length() != 3) {
				fail("Number of Stories is wrong! expected: " + 3
						+ ", actual: " + toTest.length());
			} else {
				for (int i = 0; i < result.length(); i++) {
					assertEquals(result.getJSONObject(i).get("id"), toTest
							.getJSONObject(i).get("id"));
					assertEquals(result.getJSONObject(i).get("title"), toTest
							.getJSONObject(i).get("title"));
					assertEquals(result.getJSONObject(i).get("description"),
							toTest.getJSONObject(i).get("description"));
					assertEquals(result.getJSONObject(i).get("author"), toTest
							.getJSONObject(i).get("author"));
					assertEquals(result.getJSONObject(i).get("revision"),
							toTest.getJSONObject(i).get("revision"));
					assertEquals(result.getJSONObject(i).get("size"), toTest
							.getJSONObject(i).get("size"));
					assertEquals(result.getJSONObject(i).get("size_uom"),
							toTest.getJSONObject(i).get("size_uom"));
					assertEquals(result.getJSONObject(i).get("created_at"),
							toTest.getJSONObject(i).get("created_at"));
					assertEquals(result.getJSONObject(i).get("location"),
							toTest.getJSONObject(i).get("location"));
					assertEquals(result.getJSONObject(i).get("radius"), toTest
							.getJSONObject(i).get("radius"));
					assertEquals(result.getJSONObject(i).get("radius_uom"),
							toTest.getJSONObject(i).get("radius_uom"));
					assertEquals(result.getJSONObject(i).get("updated_at"),
							toTest.getJSONObject(i).get("updated_at"));
				}
			}
		} catch (JSONException e) {
			fail("test is wrong!");
		} catch (IllegalAccessException e) {
			fail("test is wrong!");
		} catch (IllegalArgumentException e) {
			fail("test is wrong!");
		} catch (NoSuchFieldException e) {
			fail("test is wrong!");
		} catch (Exception e) {
			fail("client throwed an exception: " + e.getMessage());
		}

	}

	@Test
	public void test_getAvailableStoriesWithParamenter_testID() {
		client = new RESTClient();
		String url = URLallStories + "?id=1";
		try {
			JSONObject result = new JSONObject(
					"{\"id\":\"1\",\"title\":\"Schneewittchen und die sieben Zwerge\",\"description\":\"Die halbwüchsigen kehren zurück\",\"author\":\"Grimm\",\"revision\":\"7\",\"size\":\"10\",\"size_uom\":\"MB\",\"location\":\"48.783375 9.181187\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-01 17:08:31\",\"updated_at\":\"2015-05-01 18:09:37\"}");
			JSONArray toTest = client.getAvailableStoriesWithParamenter(url);
			if (toTest.length() != 1) {
				fail("Number of Stories is wrong! expected: " + 1
						+ ", actual: " + toTest.length());
			} else {
				assertEquals(result.get("id"), toTest.getJSONObject(0)
						.get("id"));
				assertEquals(result.get("title"),
						toTest.getJSONObject(0).get("title"));
				assertEquals(result.get("description"), toTest.getJSONObject(0)
						.get("description"));
				assertEquals(result.get("author"),
						toTest.getJSONObject(0).get("author"));
				assertEquals(result.get("revision"), toTest.getJSONObject(0)
						.get("revision"));
				assertEquals(result.get("size"),
						toTest.getJSONObject(0).get("size"));
				assertEquals(result.get("size_uom"), toTest.getJSONObject(0)
						.get("size_uom"));
				assertEquals(result.get("created_at"), toTest.getJSONObject(0)
						.get("created_at"));
				assertEquals(result.get("location"), toTest.getJSONObject(0)
						.get("location"));
				assertEquals(result.get("radius"),
						toTest.getJSONObject(0).get("radius"));
				assertEquals(result.get("radius_uom"), toTest.getJSONObject(0)
						.get("radius_uom"));
				assertEquals(result.get("updated_at"), toTest.getJSONObject(0)
						.get("updated_at"));
			}
		} catch (JSONException e) {
			fail("test is wrong!");
		} catch (Exception e) {
			fail("client throwed an exception: " + e.getMessage());
		}

	}

	@Test
	public void test_getAvailableStoriesWithParamenter_testTitle() {
		client = new RESTClient();
		String url = URLallStories + "?title=Aschenputtel";
		try {
			JSONObject result = new JSONObject(
					"{\"id\":\"2\",\"title\":\"Aschenputtel\",\"description\":\"Das arme Ding\",\"author\":\"Lukas\",\"revision\":\"7\",\"size\":\"20\",\"size_uom\":\"MB\",\"location\":\"48.799353 9.003754\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-02 12:32:58\",\"updated_at\":\"2015-05-02 14:37:44\"}");
			JSONArray toTest = client.getAvailableStoriesWithParamenter(url);
			if (toTest.length() != 1) {
				fail("Number of Stories is wrong! expected: " + 1
						+ ", actual: " + toTest.length());
			} else {
				assertEquals(result.get("id"), toTest.getJSONObject(0)
						.get("id"));
				assertEquals(result.get("title"),
						toTest.getJSONObject(0).get("title"));
				assertEquals(result.get("description"), toTest.getJSONObject(0)
						.get("description"));
				assertEquals(result.get("author"),
						toTest.getJSONObject(0).get("author"));
				assertEquals(result.get("revision"), toTest.getJSONObject(0)
						.get("revision"));
				assertEquals(result.get("size"),
						toTest.getJSONObject(0).get("size"));
				assertEquals(result.get("size_uom"), toTest.getJSONObject(0)
						.get("size_uom"));
				assertEquals(result.get("created_at"), toTest.getJSONObject(0)
						.get("created_at"));
				assertEquals(result.get("location"), toTest.getJSONObject(0)
						.get("location"));
				assertEquals(result.get("radius"),
						toTest.getJSONObject(0).get("radius"));
				assertEquals(result.get("radius_uom"), toTest.getJSONObject(0)
						.get("radius_uom"));
				assertEquals(result.get("updated_at"), toTest.getJSONObject(0)
						.get("updated_at"));
			}
		} catch (JSONException e) {
			fail("test is wrong!");
		} catch (Exception e) {
			fail("client throwed an exception: " + e.getMessage());
		}
	}

	@Test
	public void test_getAvailableStoriesWithParamenter_testDescription() {
		client = new RESTClient();
		String url = URLallStories + "?description=Das+arme+Ding";
		try {
			JSONArray result = new JSONArray();
			result.put(
					0,
					new JSONObject(
							"{\"id\":\"2\",\"title\":\"Aschenputtel\",\"description\":\"Das arme Ding\",\"author\":\"Lukas\",\"revision\":\"7\",\"size\":\"20\",\"size_uom\":\"MB\",\"location\":\"48.799353 9.003754\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-02 12:32:58\",\"updated_at\":\"2015-05-02 14:37:44\"}"));
			result.put(
					1,
					new JSONObject(
							"{\"id\":\"3\",\"title\":\"Schneewittchen\",\"description\":\"Das arme Ding\",\"author\":\"Lukas\",\"revision\":\"7\",\"size\":\"30\",\"size_uom\":\"MB\",\"location\":\"53.552647 10.007829\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-03 13:02:19\",\"updated_at\":\"2015-05-03 16:45:02\"}"));
			JSONArray toTest = client.getAvailableStoriesWithParamenter(url);
			if (toTest.length() != 2) {
				fail("Number of Stories is wrong! expected: " + 2
						+ ", actual: " + toTest.length());
			} else {
				for (int i = 0; i < result.length(); i++) {
					assertEquals(result.getJSONObject(i).get("id"), toTest
							.getJSONObject(i).get("id"));
					assertEquals(result.getJSONObject(i).get("title"), toTest
							.getJSONObject(i).get("title"));
					assertEquals(result.getJSONObject(i).get("description"),
							toTest.getJSONObject(i).get("description"));
					assertEquals(result.getJSONObject(i).get("author"), toTest
							.getJSONObject(i).get("author"));
					assertEquals(result.getJSONObject(i).get("revision"),
							toTest.getJSONObject(i).get("revision"));
					assertEquals(result.getJSONObject(i).get("size"), toTest
							.getJSONObject(i).get("size"));
					assertEquals(result.getJSONObject(i).get("size_uom"),
							toTest.getJSONObject(i).get("size_uom"));
					assertEquals(result.getJSONObject(i).get("created_at"),
							toTest.getJSONObject(i).get("created_at"));
					assertEquals(result.getJSONObject(i).get("location"),
							toTest.getJSONObject(i).get("location"));
					assertEquals(result.getJSONObject(i).get("radius"), toTest
							.getJSONObject(i).get("radius"));
					assertEquals(result.getJSONObject(i).get("radius_uom"),
							toTest.getJSONObject(i).get("radius_uom"));
					assertEquals(result.getJSONObject(i).get("updated_at"),
							toTest.getJSONObject(i).get("updated_at"));
				}
			}
		} catch (JSONException e) {
			fail("test is wrong!");
		} catch (Exception e) {
			fail("client throwed an exception: " + e.getMessage());
		}

	}

	@Test
	public void test_getAvailableStoriesWithParamenter_testAuthor() {
		client = new RESTClient();
		String url = URLallStories + "?author=Grimm";
		try {
			JSONObject result = new JSONObject(
					"{\"id\":\"1\",\"title\":\"Schneewittchen und die sieben Zwerge\",\"description\":\"Die halbwüchsigen kehren zurück\",\"author\":\"Grimm\",\"revision\":\"7\",\"size\":\"10\",\"size_uom\":\"MB\",\"location\":\"48.783375 9.181187\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-01 17:08:31\",\"updated_at\":\"2015-05-01 18:09:37\"}");
			JSONArray toTest = client.getAvailableStoriesWithParamenter(url);
			if (toTest.length() != 1) {
				fail("Number of Stories is wrong! expected: " + 1
						+ ", actual: " + toTest.length());
			} else {
				assertEquals(result.get("id"), toTest.getJSONObject(0)
						.get("id"));
				assertEquals(result.get("title"),
						toTest.getJSONObject(0).get("title"));
				assertEquals(result.get("description"), toTest.getJSONObject(0)
						.get("description"));
				assertEquals(result.get("author"),
						toTest.getJSONObject(0).get("author"));
				assertEquals(result.get("revision"), toTest.getJSONObject(0)
						.get("revision"));
				assertEquals(result.get("size"),
						toTest.getJSONObject(0).get("size"));
				assertEquals(result.get("size_uom"), toTest.getJSONObject(0)
						.get("size_uom"));
				assertEquals(result.get("created_at"), toTest.getJSONObject(0)
						.get("created_at"));
				assertEquals(result.get("location"), toTest.getJSONObject(0)
						.get("location"));
				assertEquals(result.get("radius"),
						toTest.getJSONObject(0).get("radius"));
				assertEquals(result.get("radius_uom"), toTest.getJSONObject(0)
						.get("radius_uom"));
				assertEquals(result.get("updated_at"), toTest.getJSONObject(0)
						.get("updated_at"));
			}

			JSONArray result2 = new JSONArray();
			result2.put(
					0,
					new JSONObject(
							"{\"id\":\"2\",\"title\":\"Aschenputtel\",\"description\":\"Das arme Ding\",\"author\":\"Lukas\",\"revision\":\"7\",\"size\":\"20\",\"size_uom\":\"MB\",\"location\":\"48.799353 9.003754\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-02 12:32:58\",\"updated_at\":\"2015-05-02 14:37:44\"}"));
			result2.put(
					1,
					new JSONObject(
							"{\"id\":\"3\",\"title\":\"Schneewittchen\",\"description\":\"Das arme Ding\",\"author\":\"Lukas\",\"revision\":\"7\",\"size\":\"30\",\"size_uom\":\"MB\",\"location\":\"53.552647 10.007829\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-03 13:02:19\",\"updated_at\":\"2015-05-03 16:45:02\"}"));
			url = URLallStories + "?author=Lukas";
			toTest = client.getAvailableStoriesWithParamenter(url);
			if (toTest.length() != 2) {
				fail("Number of Stories is wrong! expected: " + 2
						+ ", actual: " + toTest.length());
			} else {
				for (int i = 0; i < result2.length(); i++) {
					assertEquals(result2.getJSONObject(i).get("id"), toTest
							.getJSONObject(i).get("id"));
					assertEquals(result2.getJSONObject(i).get("title"), toTest
							.getJSONObject(i).get("title"));
					assertEquals(result2.getJSONObject(i).get("description"),
							toTest.getJSONObject(i).get("description"));
					assertEquals(result2.getJSONObject(i).get("author"), toTest
							.getJSONObject(i).get("author"));
					assertEquals(result2.getJSONObject(i).get("revision"),
							toTest.getJSONObject(i).get("revision"));
					assertEquals(result2.getJSONObject(i).get("size"), toTest
							.getJSONObject(i).get("size"));
					assertEquals(result2.getJSONObject(i).get("size_uom"),
							toTest.getJSONObject(i).get("size_uom"));
					assertEquals(result2.getJSONObject(i).get("created_at"),
							toTest.getJSONObject(i).get("created_at"));
					assertEquals(result2.getJSONObject(i).get("location"),
							toTest.getJSONObject(i).get("location"));
					assertEquals(result2.getJSONObject(i).get("radius"), toTest
							.getJSONObject(i).get("radius"));
					assertEquals(result2.getJSONObject(i).get("radius_uom"),
							toTest.getJSONObject(i).get("radius_uom"));
					assertEquals(result2.getJSONObject(i).get("updated_at"),
							toTest.getJSONObject(i).get("updated_at"));
				}
			}

		} catch (JSONException e) {
			fail("test is wrong!");
		} catch (Exception e) {
			fail("client throwed an exception: " + e.getMessage());
		}

	}

	@Test
	public void test_getAvailableStoriesWithParamenter_testSize_Max() {
		client = new RESTClient();
		String url = URLallStories + "?size_max=10";
		try {
			JSONObject result = new JSONObject(
					"{\"id\":\"1\",\"title\":\"Schneewittchen und die sieben Zwerge\",\"description\":\"Die halbwüchsigen kehren zurück\",\"author\":\"Grimm\",\"revision\":\"7\",\"size\":\"10\",\"size_uom\":\"MB\",\"location\":\"48.783375 9.181187\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-01 17:08:31\",\"updated_at\":\"2015-05-01 18:09:37\"}");
			JSONArray toTest = client.getAvailableStoriesWithParamenter(url);
			if (toTest.length() != 1) {
				fail("Number of Stories is wrong! expected: " + 1
						+ ", actual: " + toTest.length());
			} else {
				assertEquals(result.get("id"), toTest.getJSONObject(0)
						.get("id"));
				assertEquals(result.get("title"),
						toTest.getJSONObject(0).get("title"));
				assertEquals(result.get("description"), toTest.getJSONObject(0)
						.get("description"));
				assertEquals(result.get("author"),
						toTest.getJSONObject(0).get("author"));
				assertEquals(result.get("revision"), toTest.getJSONObject(0)
						.get("revision"));
				assertEquals(result.get("size"),
						toTest.getJSONObject(0).get("size"));
				assertEquals(result.get("size_uom"), toTest.getJSONObject(0)
						.get("size_uom"));
				assertEquals(result.get("created_at"), toTest.getJSONObject(0)
						.get("created_at"));
				assertEquals(result.get("location"), toTest.getJSONObject(0)
						.get("location"));
				assertEquals(result.get("radius"),
						toTest.getJSONObject(0).get("radius"));
				assertEquals(result.get("radius_uom"), toTest.getJSONObject(0)
						.get("radius_uom"));
				assertEquals(result.get("updated_at"), toTest.getJSONObject(0)
						.get("updated_at"));
			}

			JSONArray result2 = new JSONArray();
			result2.put(
					0,
					new JSONObject(
							"{\"id\":\"1\",\"title\":\"Schneewittchen und die sieben Zwerge\",\"description\":\"Die halbwüchsigen kehren zurück\",\"author\":\"Grimm\",\"revision\":\"7\",\"size\":\"10\",\"size_uom\":\"MB\",\"location\":\"48.783375 9.181187\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-01 17:08:31\",\"updated_at\":\"2015-05-01 18:09:37\"}"));
			result2.put(
					1,
					new JSONObject(
							"{\"id\":\"2\",\"title\":\"Aschenputtel\",\"description\":\"Das arme Ding\",\"author\":\"Lukas\",\"revision\":\"7\",\"size\":\"20\",\"size_uom\":\"MB\",\"location\":\"48.799353 9.003754\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-02 12:32:58\",\"updated_at\":\"2015-05-02 14:37:44\"}"));
			url = URLallStories + "?size_max=20";
			toTest = client.getAvailableStoriesWithParamenter(url);
			if (toTest.length() != 2) {
				fail("Number of Stories is wrong! expected: " + 2
						+ ", actual: " + toTest.length());
			} else {
				for (int i = 0; i < result2.length(); i++) {
					assertEquals(result2.getJSONObject(i).get("id"), toTest
							.getJSONObject(i).get("id"));
					assertEquals(result2.getJSONObject(i).get("title"), toTest
							.getJSONObject(i).get("title"));
					assertEquals(result2.getJSONObject(i).get("description"),
							toTest.getJSONObject(i).get("description"));
					assertEquals(result2.getJSONObject(i).get("author"), toTest
							.getJSONObject(i).get("author"));
					assertEquals(result2.getJSONObject(i).get("revision"),
							toTest.getJSONObject(i).get("revision"));
					assertEquals(result2.getJSONObject(i).get("size"), toTest
							.getJSONObject(i).get("size"));
					assertEquals(result2.getJSONObject(i).get("size_uom"),
							toTest.getJSONObject(i).get("size_uom"));
					assertEquals(result2.getJSONObject(i).get("created_at"),
							toTest.getJSONObject(i).get("created_at"));
					assertEquals(result2.getJSONObject(i).get("location"),
							toTest.getJSONObject(i).get("location"));
					assertEquals(result2.getJSONObject(i).get("radius"), toTest
							.getJSONObject(i).get("radius"));
					assertEquals(result2.getJSONObject(i).get("radius_uom"),
							toTest.getJSONObject(i).get("radius_uom"));
					assertEquals(result2.getJSONObject(i).get("updated_at"),
							toTest.getJSONObject(i).get("updated_at"));
				}
			}

		} catch (JSONException e) {
			fail("test is wrong!");
		} catch (Exception e) {
			fail("client throwed an exception: " + e.getMessage());
		}

	}

	@Test
	public void test_getAvailableStoriesWithParamenter_testLocationAndRadius() {
		client = new RESTClient();
		String url = URLallStories + "?gps_point=48.783375+9.181187&gps_point_radius=1";
		try {
			JSONObject result = new JSONObject(
					"{\"id\":\"1\",\"title\":\"Schneewittchen und die sieben Zwerge\",\"description\":\"Die halbwüchsigen kehren zurück\",\"author\":\"Grimm\",\"revision\":\"7\",\"size\":\"10\",\"size_uom\":\"MB\",\"location\":\"48.783375 9.181187\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-01 17:08:31\",\"updated_at\":\"2015-05-01 18:09:37\"}");
			JSONArray toTest = client.getAvailableStoriesWithParamenter(url);
			if (toTest.length() != 1) {
				fail("Number of Stories is wrong! expected: " + 1
						+ ", actual: " + toTest.length());
			} else {
				assertEquals(result.get("id"), toTest.getJSONObject(0)
						.get("id"));
				assertEquals(result.get("title"),
						toTest.getJSONObject(0).get("title"));
				assertEquals(result.get("description"), toTest.getJSONObject(0)
						.get("description"));
				assertEquals(result.get("author"),
						toTest.getJSONObject(0).get("author"));
				assertEquals(result.get("revision"), toTest.getJSONObject(0)
						.get("revision"));
				assertEquals(result.get("size"),
						toTest.getJSONObject(0).get("size"));
				assertEquals(result.get("size_uom"), toTest.getJSONObject(0)
						.get("size_uom"));
				assertEquals(result.get("created_at"), toTest.getJSONObject(0)
						.get("created_at"));
				assertEquals(result.get("location"), toTest.getJSONObject(0)
						.get("location"));
				assertEquals(result.get("radius"),
						toTest.getJSONObject(0).get("radius"));
				assertEquals(result.get("radius_uom"), toTest.getJSONObject(0)
						.get("radius_uom"));
				assertEquals(result.get("updated_at"), toTest.getJSONObject(0)
						.get("updated_at"));
			}

			JSONArray result2 = new JSONArray();
			result2.put(
					0,
					new JSONObject(
							"{\"id\":\"1\",\"title\":\"Schneewittchen und die sieben Zwerge\",\"description\":\"Die halbwüchsigen kehren zurück\",\"author\":\"Grimm\",\"revision\":\"7\",\"size\":\"10\",\"size_uom\":\"MB\",\"location\":\"48.783375 9.181187\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-01 17:08:31\",\"updated_at\":\"2015-05-01 18:09:37\"}"));
			result2.put(
					1,
					new JSONObject(
							"{\"id\":\"2\",\"title\":\"Aschenputtel\",\"description\":\"Das arme Ding\",\"author\":\"Lukas\",\"revision\":\"7\",\"size\":\"20\",\"size_uom\":\"MB\",\"location\":\"48.799353 9.003754\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-02 12:32:58\",\"updated_at\":\"2015-05-02 14:37:44\"}"));
			url = URLallStories + "?gps_point=48.783375+9.181187&gps_point_radius=100";
			toTest = client.getAvailableStoriesWithParamenter(url);
			if (toTest.length() != 2) {
				fail("Number of Stories is wrong! expected: " + 2
						+ ", actual: " + toTest.length());
			} else {
				for (int i = 0; i < result2.length(); i++) {
					assertEquals(result2.getJSONObject(i).get("id"), toTest
							.getJSONObject(i).get("id"));
					assertEquals(result2.getJSONObject(i).get("title"), toTest
							.getJSONObject(i).get("title"));
					assertEquals(result2.getJSONObject(i).get("description"),
							toTest.getJSONObject(i).get("description"));
					assertEquals(result2.getJSONObject(i).get("author"), toTest
							.getJSONObject(i).get("author"));
					assertEquals(result2.getJSONObject(i).get("revision"),
							toTest.getJSONObject(i).get("revision"));
					assertEquals(result2.getJSONObject(i).get("size"), toTest
							.getJSONObject(i).get("size"));
					assertEquals(result2.getJSONObject(i).get("size_uom"),
							toTest.getJSONObject(i).get("size_uom"));
					assertEquals(result2.getJSONObject(i).get("created_at"),
							toTest.getJSONObject(i).get("created_at"));
					assertEquals(result2.getJSONObject(i).get("location"),
							toTest.getJSONObject(i).get("location"));
					assertEquals(result2.getJSONObject(i).get("radius"), toTest
							.getJSONObject(i).get("radius"));
					assertEquals(result2.getJSONObject(i).get("radius_uom"),
							toTest.getJSONObject(i).get("radius_uom"));
					assertEquals(result2.getJSONObject(i).get("updated_at"),
							toTest.getJSONObject(i).get("updated_at"));
				}
			}

		} catch (JSONException e) {
			fail("test is wrong!");
		} catch (Exception e) {
			fail("client throwed an exception: " + e.getMessage());
		}
	}

	@Test
	public void test_getAvailableStoriesWithParamenter_testCreated_at() {
		client = new RESTClient();
		String url = URLallStories + "?creation_date_max=2015-05-02";
		try {
			JSONArray result = new JSONArray();
			result.put(
					0,
					new JSONObject(
							"{\"id\":\"1\",\"title\":\"Schneewittchen und die sieben Zwerge\",\"description\":\"Die halbwüchsigen kehren zurück\",\"author\":\"Grimm\",\"revision\":\"7\",\"size\":\"10\",\"size_uom\":\"MB\",\"location\":\"48.783375 9.181187\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-01 17:08:31\",\"updated_at\":\"2015-05-01 18:09:37\"}"));
			result.put(
					1,
					new JSONObject(
							"{\"id\":\"2\",\"title\":\"Aschenputtel\",\"description\":\"Das arme Ding\",\"author\":\"Lukas\",\"revision\":\"7\",\"size\":\"20\",\"size_uom\":\"MB\",\"location\":\"48.799353 9.003754\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-02 12:32:58\",\"updated_at\":\"2015-05-02 14:37:44\"}"));
			
			JSONArray toTest = client.getAvailableStoriesWithParamenter(url);
			if (toTest.length() != 2) {
				fail("Number of Stories is wrong! expected: " + 2
						+ ", actual: " + toTest.length());
			} else {
				for (int i = 0; i < result.length(); i++) {
					assertEquals(result.getJSONObject(i).get("id"), toTest
							.getJSONObject(i).get("id"));
					assertEquals(result.getJSONObject(i).get("title"), toTest
							.getJSONObject(i).get("title"));
					assertEquals(result.getJSONObject(i).get("description"),
							toTest.getJSONObject(i).get("description"));
					assertEquals(result.getJSONObject(i).get("author"), toTest
							.getJSONObject(i).get("author"));
					assertEquals(result.getJSONObject(i).get("revision"),
							toTest.getJSONObject(i).get("revision"));
					assertEquals(result.getJSONObject(i).get("size"), toTest
							.getJSONObject(i).get("size"));
					assertEquals(result.getJSONObject(i).get("size_uom"),
							toTest.getJSONObject(i).get("size_uom"));
					assertEquals(result.getJSONObject(i).get("created_at"),
							toTest.getJSONObject(i).get("created_at"));
					assertEquals(result.getJSONObject(i).get("location"),
							toTest.getJSONObject(i).get("location"));
					assertEquals(result.getJSONObject(i).get("radius"), toTest
							.getJSONObject(i).get("radius"));
					assertEquals(result.getJSONObject(i).get("radius_uom"),
							toTest.getJSONObject(i).get("radius_uom"));
					assertEquals(result.getJSONObject(i).get("updated_at"),
							toTest.getJSONObject(i).get("updated_at"));
				}
			}

			JSONArray result2 = new JSONArray();
			result2.put(
					0,
					new JSONObject(
							"{\"id\":\"2\",\"title\":\"Aschenputtel\",\"description\":\"Das arme Ding\",\"author\":\"Lukas\",\"revision\":\"7\",\"size\":\"20\",\"size_uom\":\"MB\",\"location\":\"48.799353 9.003754\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-02 12:32:58\",\"updated_at\":\"2015-05-02 14:37:44\"}"));
			result2.put(
					2,
					new JSONObject(
							"{\"id\":\"3\",\"title\":\"Schneewittchen\",\"description\":\"Das arme Ding\",\"author\":\"Lukas\",\"revision\":\"7\",\"size\":\"30\",\"size_uom\":\"MB\",\"location\":\"53.552647 10.007829\",\"radius\":\"2\",\"radius_uom\":\"km\",\"created_at\":\"2015-05-03 13:02:19\",\"updated_at\":\"2015-05-03 16:45:02\"}"));
			url = URLallStories + "?location=48.783375+9.181187&radius=100";
			toTest = client.getAvailableStoriesWithParamenter(url);
			if (toTest.length() != 2) {
				fail("Number of Stories is wrong! expected: " + 2
						+ ", actual: " + toTest.length());
			} else {
				for (int i = 0; i < result2.length(); i++) {
					assertEquals(result2.getJSONObject(i).get("id"), toTest
							.getJSONObject(i).get("id"));
					assertEquals(result2.getJSONObject(i).get("title"), toTest
							.getJSONObject(i).get("title"));
					assertEquals(result2.getJSONObject(i).get("description"),
							toTest.getJSONObject(i).get("description"));
					assertEquals(result2.getJSONObject(i).get("author"), toTest
							.getJSONObject(i).get("author"));
					assertEquals(result2.getJSONObject(i).get("revision"),
							toTest.getJSONObject(i).get("revision"));
					assertEquals(result2.getJSONObject(i).get("size"), toTest
							.getJSONObject(i).get("size"));
					assertEquals(result2.getJSONObject(i).get("size_uom"),
							toTest.getJSONObject(i).get("size_uom"));
					assertEquals(result2.getJSONObject(i).get("created_at"),
							toTest.getJSONObject(i).get("created_at"));
					assertEquals(result2.getJSONObject(i).get("location"),
							toTest.getJSONObject(i).get("location"));
					assertEquals(result2.getJSONObject(i).get("radius"), toTest
							.getJSONObject(i).get("radius"));
					assertEquals(result2.getJSONObject(i).get("radius_uom"),
							toTest.getJSONObject(i).get("radius_uom"));
					assertEquals(result2.getJSONObject(i).get("updated_at"),
							toTest.getJSONObject(i).get("updated_at"));
				}
			}

		} catch (JSONException e) {
			fail("test is wrong!");
		} catch (Exception e) {
			fail("client throwed an exception: " + e.getMessage());
		}
	}
	
	@Test
	public void test_downloadMediaFiles() {
		client = new RESTClient();
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		File dir = new File("/storage/emulated/0/StorytellAR/Content/-1");
		dir.mkdirs();
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("Studentengespraech.mp4", "Studentengespraech.mp4");
		Story story = Mockito.mock(Story.class);
		Mockito.when(story.getId()).thenReturn(1,-1);
		client.downloadMediaFiles(map, story);
		File toTest = new File("/storage/emulated/0/StorytellAR/Content/-1/Studentengespraech.mp4");
		if(!toTest.exists()){
			fail("File was not downloaded");
		}
		toTest.delete();
		dir.delete();		
	}
}
