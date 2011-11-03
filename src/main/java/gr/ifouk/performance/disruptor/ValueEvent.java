package gr.ifouk.performance.disruptor;

import com.lmax.disruptor.EventFactory;

public class ValueEvent {
	private boolean increase;

	public boolean getIncrease() {
		return increase;
	}

	public void setIncrease(boolean increase) {
		this.increase = increase;
	}
	

    public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>()
    {

		public ValueEvent newInstance() {
            return new ValueEvent();

		}
    };


}
