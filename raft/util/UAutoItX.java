package raft.util;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;

public class UAutoItX {

	//public static final String dllpath = "C:\\Program Files\\AutoIt3\\AutoItX\\AutoItX3.dll";
	public static final String dllpath = "AutoItX3.dll";// have loaded AutoItX3.dll to lib path, for example: system32 path
	/*
	 * 	AutoItX Interface For AU3_MouseMove() Function
	 	AU3_API long WINAPI AU3_MouseMove(long nX, long nY, long nSpeed);
	 */
	public UAutoItX GetMyMouseMove(int x, int y, int s) throws NativeException, IllegalAccessException{
		
		JNative nGetMyMouseMove = new JNative(dllpath, "AU3_MouseMove");
		
		nGetMyMouseMove.setParameter(0,x);//The screen x coordinate to move the mouse to.
		nGetMyMouseMove.setParameter(1,y);//The screen y coordinate to move the mouse to.
		nGetMyMouseMove.setParameter(2,s);//The speed s to move the mouse (optional)
		
		nGetMyMouseMove.invoke();
		
		return this;
	}
	
	public UAutoItX GetMyMouseClick(String but, int x, int y) throws IllegalAccessException, NativeException{
		
		JNative nGetMyMouseClick = new JNative(dllpath, "AU3_MouseClick");
		
		nGetMyMouseClick.setParameter(0,but);//click The button to click: "left", "right", "middle"
		nGetMyMouseClick.setParameter(1, x);// mouse position x
		nGetMyMouseClick.setParameter(2, y);// mouse position y
		nGetMyMouseClick.setParameter(3, 1);//click times
		
		nGetMyMouseClick.invoke();
		
		return this;
	}
	
	public UAutoItX GetMyMouseDbClick(String but, int x, int y) throws IllegalAccessException, NativeException{
		
		JNative nGetMyMouseClick = new JNative(dllpath, "AU3_MouseClick");
		
		nGetMyMouseClick.setParameter(0,but);//click The button to click: "left", "right", "middle"
		nGetMyMouseClick.setParameter(1, x);// mouse position x
		nGetMyMouseClick.setParameter(2, y);// mouse position y
		nGetMyMouseClick.setParameter(3, 2);//click times
		
		nGetMyMouseClick.invoke();
		
		return this;
	}

	public UAutoItX GetMyMouseClickDrag(String but, int x1, int y1, int x2, int y2, int s) throws IllegalAccessException, NativeException{
		
		JNative nGetMyMouseClickDrag = new JNative(dllpath, "AU3_MouseClickDrag");
		
		nGetMyMouseClickDrag.setParameter(0,but);//click The button to click: "left", "right", "middle"
		nGetMyMouseClickDrag.setParameter(1, x1);// mouse start position x
		nGetMyMouseClickDrag.setParameter(2, y1);// mouse start position y
		nGetMyMouseClickDrag.setParameter(3, x2);// mouse end position x
		nGetMyMouseClickDrag.setParameter(4, y2);// mouse end position x
		nGetMyMouseClickDrag.setParameter(5, s);// mouse move speed
		
		nGetMyMouseClickDrag.invoke();
		
		return this;
	}

	public int GetMyWinWait(String title, String text, int timeout) throws IllegalAccessException, NativeException{
		
		JNative nGetMyWinWait = new JNative(dllpath, "AU3_WinWait");
		
		nGetMyWinWait.setParameter(0, title);// Title of the window to check
		nGetMyWinWait.setParameter(1, text);// Text of the window, can be null
		nGetMyWinWait.setParameter(2, timeout);// timeout in sec

		nGetMyWinWait.setRetVal(Type.INT);
		
		nGetMyWinWait.invoke();
		
		int ret = nGetMyWinWait.getRetValAsInt();
		
		return ret;
	}

	public int GetMyWinActive(String title, String text) throws IllegalAccessException, NativeException{
		
		JNative nGetMyWinActive = new JNative(dllpath, "AU3_WinActive");
		
		nGetMyWinActive.setParameter(0, title);// Title of the window to active
		nGetMyWinActive.setParameter(1, text);// Text of the window, can be null

		nGetMyWinActive.setRetVal(Type.INT);
		
		nGetMyWinActive.invoke();
		
		int ret = nGetMyWinActive.getRetValAsInt();
		
		return ret;
	}

	public UAutoItX GetMyWinActivate(String title, String text) throws IllegalAccessException, NativeException{
		
		JNative nGetMyWinActivate = new JNative(dllpath, "AU3_WinActivate");
		
		nGetMyWinActivate.setParameter(0, title);// Title of the window to active
		nGetMyWinActivate.setParameter(1, text);// Text of the window, can be null

		nGetMyWinActivate.invoke();
		
		return this;
	}

	public UAutoItX GetMySendKey(String key) throws IllegalAccessException, NativeException, InterruptedException{
		
		JNative nGetMySendKey = new JNative(dllpath, "AU3_Send");
		
		nGetMySendKey.setParameter(0, key);// the sequence of keys to send
		nGetMySendKey.setParameter(1, 0);// how keys is processed, 0 by default

		nGetMySendKey.invoke();
		
		Thread.sleep(200);
		return this;
	}

	public int GetMyWinExists(String title, String text) throws IllegalAccessException, NativeException{
		
		JNative nGetMyWinExists = new JNative(dllpath, "AU3_WinExists");
		
		nGetMyWinExists.setParameter(0, title);// the title of the window to check
		nGetMyWinExists.setParameter(1, text);// the text of the window to check , optional

		nGetMyWinExists.setRetVal(Type.INT);
		
		nGetMyWinExists.invoke();
		
		int ret = nGetMyWinExists.getRetValAsInt();
		
		return ret;
	}

	public UAutoItX GetMyWinText(String title, String text) throws IllegalAccessException, NativeException{
		
		JNative nGetMyWinGetText = new JNative(dllpath, "AU3_WinGetText");
		
		nGetMyWinGetText.setParameter(0, title);// the title of the window to read
		nGetMyWinGetText.setParameter(1, text);// the text of the window to read , optional

		nGetMyWinGetText.invoke();
		
		return this;
	}
	
	public UAutoItX GetMyWinTitle(String title, String text) throws IllegalAccessException, NativeException{
		
		JNative nGetMyWinTitle = new JNative(dllpath, "AU3_WinGetText");
		
		nGetMyWinTitle.setParameter(0, title);// the title of the window to read
		nGetMyWinTitle.setParameter(1, text);// the text of the window to read , optional
		
		nGetMyWinTitle.invoke();

		return this;
	}
	
	public int GetMyWinSetState(String title, String text, int flag) throws IllegalAccessException, NativeException{
		
		JNative nGetMyWinSetState = new JNative(dllpath, "AU3_WinSetState");
		
		nGetMyWinSetState.setParameter(0, title);// the title of the window to set
		nGetMyWinSetState.setParameter(1, text);// the text of the window to set , optional
		nGetMyWinSetState.setParameter(2, flag);// the "show" flag of the executed program
		
		nGetMyWinSetState.setRetVal(Type.INT);
		
		nGetMyWinSetState.invoke();
		
		int ret = nGetMyWinSetState.getRetValAsInt();
		
		return ret;
	}

	public UAutoItX GetMyWinClose(String title, String text) throws IllegalAccessException, NativeException{
		
		JNative nGetMyWinClose = new JNative(dllpath, "AU3_WinClose");
		
		nGetMyWinClose.setParameter(0, title);// the title of the window to close
		nGetMyWinClose.setParameter(1, text);// the text of the window to close , optional
		
		nGetMyWinClose.invoke();
		
		return this;
	}

	public int GetMyProgramRun(String path) throws IllegalAccessException, NativeException{
		
		JNative nGetMyProgramRun = new JNative(dllpath, "AU3_Run");
		
		nGetMyProgramRun.setParameter(0, path);//the full path of the program to be executed
		
		nGetMyProgramRun.setRetVal(Type.INT);
		
		nGetMyProgramRun.invoke();
		
		int ret = nGetMyProgramRun.getRetValAsInt();
		
		return ret;
	}

	public int GetMyProgramRunWait(String path) throws IllegalAccessException, NativeException{
		
		JNative nGetMyProgramRunWait = new JNative(dllpath, "AU3_RunWait");
		
		nGetMyProgramRunWait.setParameter(0, path);//the full path of the program to be executed
		
		nGetMyProgramRunWait.setRetVal(Type.INT);
		
		nGetMyProgramRunWait.invoke();
		
		int ret = nGetMyProgramRunWait.getRetValAsInt();
		
		return ret;
	}

}
