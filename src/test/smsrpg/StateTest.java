package test.smsrpg;

import static org.junit.Assert.*;
import log.Log;

import org.junit.BeforeClass;
import org.junit.Test;

import smsrpg.State;

public class StateTest {
	
	private enum TestState {
		TEST1,
		TEST2,
		TEST3;
	}
	
	private enum TestStateWithParams {
		TEST1("test1"),
		TEST2("test2"),
		TEST3("test3");
		
		private String value;
		
		private TestStateWithParams(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	@BeforeClass
	public static void setUp() {
		Log.showAll(true);
	}

	@Test
	public void testState() {
		State<TestState> state = new State<TestState>();
		
		assertTrue("State should be empty", state.isEmpty());
		
		State<TestStateWithParams> stateWithParams = new State<TestStateWithParams>();
		
		assertTrue("State with params should be empty", stateWithParams.isEmpty());
	}

	@Test
	public void testPopAndPush() {
		State<TestState> state = new State<TestState>();
		state.push(TestState.TEST1);
		
		assertFalse("State should not be empty", state.isEmpty());
		assertEquals("State should have size of 1", state.size(), 1);
		assertEquals("First state should be 'Test1'", state.pop(), TestState.TEST1);
		assertTrue("State should be empty", state.isEmpty());
		
		State<TestStateWithParams> stateWithParams = new State<TestStateWithParams>();
		stateWithParams.push(TestStateWithParams.TEST1);
		
		assertFalse("State should not be empty", stateWithParams.isEmpty());
		assertEquals("State should have size of 1", stateWithParams.size(), 1);
		assertEquals("First state should be 'Test1'", stateWithParams.pop(), TestStateWithParams.TEST1);
		assertTrue("State should be empty", stateWithParams.isEmpty());
	}

	@Test
	public void testGetCurrent() {
		State<TestState> state = new State<TestState>();
		state.push(TestState.TEST1);
		
		assertEquals("Current state should be 'Test1'", state.getCurrent(), TestState.TEST1);
		
		State<TestStateWithParams> stateWithParams = new State<TestStateWithParams>();
		stateWithParams.push(TestStateWithParams.TEST1);
		
		assertEquals("Current state should be 'Test1'", stateWithParams.getCurrent(), TestStateWithParams.TEST1);
		assertEquals("Current state should have value of 'test1'", stateWithParams.getCurrent().getValue(), TestStateWithParams.TEST1.getValue());
	}

	@Test
	public void testChange() {
		State<TestState> state = new State<TestState>();
		state.push(TestState.TEST1);
		state.change(TestState.TEST2);
		
		assertEquals("First state should be 'Test2'", state.getCurrent(), TestState.TEST2);
		assertEquals("State should still have size of 1", state.size(), 1);
		
		State<TestStateWithParams> stateWithParams = new State<TestStateWithParams>();
		stateWithParams.push(TestStateWithParams.TEST1);
		stateWithParams.change(TestStateWithParams.TEST2);
		
		assertEquals("First state should be 'Test2'", stateWithParams.getCurrent(), TestStateWithParams.TEST2);
		assertEquals("First state should have value of 'test2'", stateWithParams.getCurrent().getValue(), TestStateWithParams.TEST2.getValue());
		assertEquals("State should still have size of 1", stateWithParams.size(), 1);
	}

	@Test
	public void testParseString() {
		String stateString = "TEST1>TEST2>TEST1>TEST3";
		
		State<TestState> state = new State<TestState>();
		state.push(TestState.TEST1);
		
		state.parseString(stateString, TestState.class);
		
		assertFalse("State should not be empty", state.isEmpty());
		assertEquals("State should have size of 4", state.size(), 4);
		assertEquals("Current state should be 'TEST3;", state.getCurrent(), TestState.TEST3);
		assertEquals("First popped state should be 'TEST3'", state.pop(), TestState.TEST3);
		assertEquals("Next popped state should be 'TEST1'", state.pop(), TestState.TEST1);
		assertEquals("Next popped state should be 'TEST2'", state.pop(), TestState.TEST2);
		assertEquals("Next popped state should be 'TEST1'", state.pop(), TestState.TEST1);
		assertTrue("State should be empty", state.isEmpty());
		
		State<TestStateWithParams> stateWithParams = new State<TestStateWithParams>();
		stateWithParams.push(TestStateWithParams.TEST1);
		
		stateWithParams.parseString(stateString, TestStateWithParams.class);
		
		assertFalse("State should not be empty", stateWithParams.isEmpty());
		assertEquals("State should have size of 4", stateWithParams.size(), 4);
		assertEquals("Current state should be 'TEST3;", stateWithParams.getCurrent(), TestStateWithParams.TEST3);
		assertEquals("First popped state should be 'TEST3'", stateWithParams.pop(), TestStateWithParams.TEST3);
		assertEquals("Next popped state should be 'TEST1'", stateWithParams.pop(), TestStateWithParams.TEST1);
		assertEquals("Next popped state should be 'TEST2'", stateWithParams.pop(), TestStateWithParams.TEST2);
		assertEquals("Next popped state should be 'TEST1'", stateWithParams.pop(), TestStateWithParams.TEST1);
		assertTrue("State should be empty", stateWithParams.isEmpty());
	}

	@Test
	public void testToString() {
		String stateString = "TEST1>TEST2>TEST1>TEST3";
		
		State<TestState> state = new State<TestState>();
		state.parseString(stateString, TestState.class);
		
		assertEquals("State string should be the same as the original state string", state.toString(), stateString);
		
		State<TestStateWithParams> stateWithParams = new State<TestStateWithParams>();
		stateWithParams.parseString(stateString, TestStateWithParams.class);
		
		assertEquals("State string should be the same as the original state string", stateWithParams.toString(), stateString);
	}

}
