package com.redhat.postureKeeper;

import com.redhat.postureKeeper.sarif.Run;
import com.redhat.postureKeeper.sarif.Tool;
import com.redhat.postureKeeper.sarif.ToolComponent;

public class TestDataHelper {
   
    public static final String SIMPLE_SARIF = "{\"version\": \"2.1.0\", \"runs\": []}";
    public static final String VALID_REPO = "https://github.com/valid/repo";
    public static final String TEST_WORKSPACE = "test-workspace";

    public static Run createRunObject(String toolName) {
        ToolComponent component = new ToolComponent();
        Tool tool = new Tool();
        component.setName(toolName);
        tool.setDriver(component);
        Run run = new Run();
        run.setTool(tool);
        return run;
    }
}
