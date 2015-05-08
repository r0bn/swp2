package restClient;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.junit.Test;

import android.test.AndroidTestCase;

public class restClient_Test extends AndroidTestCase {

	RESTClient client;
	
	@Test
	public void test_ClientNotNull() {
		client = new RESTClient();
		assertNotNull(client);
	}
	
	@Test
	public void test_readInput() {
		client = new RESTClient();
		Method method;
		final String testString = "Test";
		try {
			Class[] cArg = new Class[1];
			cArg[0] = InputStream.class;
			method = RESTClient.class.getDeclaredMethod("readInput",cArg);
			method.setAccessible(true);
			InputStream testStream = new InputStream() {
				int call = 0;
				byte[] result = testString.getBytes();
				@Override
				public int read() throws IOException {
					if(call < result.length){
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
			String result = (String)method.invoke(client, params);
			assertEquals("Result is wrong", testString,result);
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_readInput_EmptyStream() {
		client = new RESTClient();
		Method method;
		final String testString = "";
		try {
			Class[] cArg = new Class[1];
			cArg[0] = InputStream.class;
			method = RESTClient.class.getDeclaredMethod("readInput",cArg);
			method.setAccessible(true);
			InputStream testStream = new InputStream() {
				int call = 0;
				byte[] result = testString.getBytes();
				@Override
				public int read() throws IOException {
					if(call < result.length){
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
			String result = (String)method.invoke(client, params);
			assertEquals("Result is wrong", testString,result);
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}
	

}
