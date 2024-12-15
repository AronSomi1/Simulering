package task2;

// Denna klass definierar signallistan. Om man vill skicka mer information i signalen �n minimum, s� kan
// man skriva ytterligare variante av SendSignal som inneh�ller fler parametrar.

// This class defines the signal list. If one wants to send more information than here,
// one can add the extra information in the Signal class and write an extra sendSignal method 
// with more parameters. 

public class SignalList {
	public Signal list, last;

	public SignalList() {
		this.list = new Signal();
		this.last = new Signal();
		list.next = last;
	}

	public void SendSignal(int type, Proc dest, Proc origin, double arrtime) {
		Signal dummy, predummy;
		Signal newSignal = new Signal();
		newSignal.signalType = type;
		newSignal.destination = dest;
		newSignal.arrivalTime = arrtime;
		newSignal.origin = origin;
		predummy = list;
		dummy = list.next;
		while ((dummy.arrivalTime < newSignal.arrivalTime) & (dummy != last)) {
			predummy = dummy;
			dummy = dummy.next;
		}
		predummy.next = newSignal;
		newSignal.next = dummy;
	}

	public Signal FetchSignal() {
		Signal dummy;
		dummy = list.next;
		list.next = dummy.next;
		dummy.next = null;
		return dummy;
	}

	public void ClearList() {
		list.next = last;
	}
}
