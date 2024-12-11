package task1;

class ProcessNode {
    SignalList sl;
    ProcessNode next;

    public ProcessNode() {
    }

    public ProcessNode(SignalList sl) {
        this.sl = sl;
    }
}

public class ProcessList {
    private ProcessNode head;
    private ProcessNode tail;

    public ProcessList() {
        head = new ProcessNode();
        tail = new ProcessNode();
        head.next = tail;
    }

    // Adds a new SignalList into the ProcessList, ordered by earliest signal time
    public void addProcess(SignalList sl) {
        double earliestTime = getEarliestTimeFrom(sl);

        ProcessNode newNode = new ProcessNode(sl);
        ProcessNode pred = head;
        ProcessNode curr = head.next;

        // Insert in order of earliest signal arrival time
        while (curr != tail && getEarliestTimeFrom(curr.sl) < earliestTime) {
            pred = curr;
            curr = curr.next;
        }

        pred.next = newNode;
        newNode.next = curr;
    }

    // Fetch the earliest signal from the earliest SignalList
    public Signal fetchEarliestSignal() {
        // If the list is empty or no processes are present
        if (head.next == tail) {
            return null; // No signals to fetch
        }

        ProcessNode earliestNode = head.next;
        Signal earliestSignal = earliestNode.sl.FetchSignal();

        // We fetched the earliest signal. Now, this SignalList may have changed its
        // earliest time.
        // To maintain order, remove and re-insert it, unless it's now empty.
        head.next = earliestNode.next; // Remove the node from the list (temporarily)

        // If the SignalList still has signals, re-insert it into proper position
        if (getEarliestTimeFrom(earliestNode.sl) != Double.POSITIVE_INFINITY) {
            addProcess(earliestNode.sl);
        }

        return earliestSignal;
    }

    // Helper function: returns the earliest signal time in a given SignalList
    private double getEarliestTimeFrom(SignalList sl) {
        // The earliest event is at sl.list.next, if it exists
        if (sl.list.next == sl.last) {
            // No signals in this list
            return Double.POSITIVE_INFINITY;
        }
        return sl.list.next.arrivalTime;
    }
}
