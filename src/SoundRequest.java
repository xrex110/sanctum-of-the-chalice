public class SoundRequest {
	public String label;
	public volatile boolean notStopped;

	public SoundRequest(String label) {
		this.label = label;
		this.notStopped = true;
	}
}
