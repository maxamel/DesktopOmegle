package test.java.gen;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import main.java.client.OmegleClient;
import main.java.server.OmegleService;
import main.java.server.ServerConstants;

import org.junit.Test;
import org.mockito.Mockito;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import static org.mockito.Mockito.*;

public class ServiceTester {
	
	@Test
	public void testOmegleServiceSingleton() {
		OmegleService service1 = OmegleService.getInstance();
		OmegleService service2 = OmegleService.getInstance();
		assertEquals(service1, service2);
	}

	@Test
	@SuppressFBWarnings(value="REC_CATCH_EXCEPTION")
	public void testParseComplexConnected() {
		OmegleService service = OmegleService.getInstance();
		
		String jsonAnswer = "{\"events\":[[\"waiting\"],[\"connected\"],[\"commonLikes\",[\"israel\"] ],[\"gotMessage\",\"hi\"]] }";
		try {
			Class[] cArg = new Class[1];
	        cArg[0] = String.class;
			Method method = service.getClass().getDeclaredMethod("processJson",cArg);
			method.setAccessible(true);
			method.invoke(service, jsonAnswer);
			assertEquals(service.getLikes(),"israel");
			assertTrue(service.getMsgs().contains("hi"));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unreachable state! " + e.getMessage());
		} 
	}
	@Test
	@SuppressFBWarnings(value="REC_CATCH_EXCEPTION")
	public void testParseSimpleMessage() {
		OmegleService service = OmegleService.getInstance();
		
		String jsonAnswer = "[[\"gotMessage\",\"hi\"]] ";
		try {
			Class[] cArg = new Class[1];
	        cArg[0] = String.class;
			Method method = service.getClass().getDeclaredMethod("processJson",cArg);
			method.setAccessible(true);
			method.invoke(service, jsonAnswer);
			assertTrue(service.getMsgs().contains("hi"));
		} catch (Exception e) {
			System.out.println("Unreachable state!");
		} 
	}
	
	@Test
	@SuppressFBWarnings(value="REC_CATCH_EXCEPTION")
	public void testParseManyLikes() {
		OmegleService service = OmegleService.getInstance();
		
		String jsonAnswer =  "[[\"connected\"],[\"commonLikes\",[\"Israel,Russia,Afghanistan\"] ],[\"gotMessage\",\"hi\"]] }";
		try {
			Class[] cArg = new Class[1];
	        cArg[0] = String.class;
			Method method = service.getClass().getDeclaredMethod("processJson",cArg);
			method.setAccessible(true);
			method.invoke(service, jsonAnswer);
			assertTrue(service.getMsgs().contains("hi"));
			assertTrue(service.getLikes().contains("Israel")&&service.getLikes().contains("Russia")&&service.getLikes().contains("Afghanistan"));
		} catch (Exception e) {
			System.out.println("Unreachable state!");
		} 
	}
	
	@Test
	@SuppressFBWarnings(value="REC_CATCH_EXCEPTION")
	public void testParseComplexDisconnection() {
		OmegleService service = OmegleService.getInstance();
		
		String jsonAnswer = "[[connected],[\"gotMessage\",\"hi\"],[\"strangerDisconnected\"]]";
		try {
			Class[] cArg = new Class[1];
	        cArg[0] = String.class;
			Method method = service.getClass().getDeclaredMethod("processJson",cArg);
			method.setAccessible(true);
			method.invoke(service, jsonAnswer);
			assertTrue(service.getStatus().equals(ServerConstants.STATUS_OFFLINE));
			assertTrue(service.getMsgs().contains("hi"));
		} catch (Exception e) {
			System.out.println("Unreachable state!");
		} 
	}
	
	@Test
	@SuppressFBWarnings(value="REC_CATCH_EXCEPTION")
	public void testParseSimpleConnected() {
		OmegleService service = OmegleService.getInstance();
		
		String jsonAnswer = "[[\"connected\"]]";
		try {
			Class[] cArg = new Class[1];
	        cArg[0] = String.class;
			Method method = service.getClass().getDeclaredMethod("processJson",cArg);
			method.setAccessible(true);
			method.invoke(service, jsonAnswer);
			assertTrue(service.getStatus().equals(ServerConstants.STATUS_ONLINE));
		} catch (Exception e) {
			System.out.println("Unreachable state!");
		} 
	}
}