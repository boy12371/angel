package test.com.feinno.util;

import java.util.Arrays;

import org.junit.Test;

import com.feinno.util.Event;
import com.feinno.util.EventHandler;

public class TestEvent {

	public EventHandler newEventHandler(final int index) {
		return new EventHandler<Object>() {
			@Override
			public void run(Object sender, Object e) {
				System.out.print(index + ". ");
				System.out.println(sender + " : " + e);
			}
		};
	}

	@Test
	public void Test() {
		Event<Object> event = new Event<Object>("Feinno");
		EventHandler baseEventHandler = newEventHandler(1);
		event.addListener(baseEventHandler);
		event.addListener(newEventHandler(2));
		event.addListener(newEventHandler(3));
		event.addListener(newEventHandler(4));
		event.fireEvent("Hello.");
		System.out.println("-------------------------------------------------");
		event.removeListener(baseEventHandler);
		event.fireEvent("Hello.");
		System.out.println("-------------------------------------------------");
		System.out.println(Arrays.toString(event.getAllHandlers()));
	}

	public static void main(String args[]) {
		new TestEvent().Test();
	}
}
