package gr.aegean.container.risk.scanner;

import java.util.List;

public interface ToolRunner {
	public void runTool(List<String> imageNames);
	
	public List<String> getResultFiles();
}
