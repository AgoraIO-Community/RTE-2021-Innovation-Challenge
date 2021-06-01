using UnityEngine;
using UnityEngine.UI;
using agora_utilities;

public class Logger {
    Text text;
    public Logger(Text text) {
        this.text = text;
    }
    public void Clear(){
        // this.text = Text.Empty;
        // this.text.text =string.Empty;
        Debug.Log("enter here");
    }

    public void UpdateLog(string logMessage) {
        string srcLogMessage = text.text;
        if (srcLogMessage.Length > 1000) {
            srcLogMessage = "";
        }
        srcLogMessage += "\r\n \r\n";
        srcLogMessage += logMessage;
        text.text = srcLogMessage;
    }

    public bool DebugAssert(bool condition, string message) {
        if (!condition) {
            UpdateLog(message);
            return false;
        }
        Debug.Assert(condition, message);
        return true;
    }
}