import {
  BrowserWindow,
  app,
  systemPreferences,
  ipcMain,
} from "electron";

const isMac = process.platform === "darwin";
const isWin = process.platform === "win32";

if (isWin) app.setAppUserModelId("ITalking");

function createWindow() {
  const win = new BrowserWindow({
    width: 1280,
    height: 1000,
    minWidth: 1060,
    minHeight: 800,
    frame: true,
    center: true,
    show: false,
    webPreferences: {
      backgroundThrottling: false
    },
  });

  win.loadURL('https://italking.tomotoes.com');
  win.setMenu(null)

  // crashes on mac only in dev
  // systemPreferences.askForMediaAccess("microphone");
  ipcMain.on("request-mic", async (event, _serviceName) => {
    const isAllowed: boolean = await systemPreferences.askForMediaAccess(
      "microphone"
    );
    event.returnValue = isAllowed;
  });
  if (isMac) {
    win.webContents.send("@alerts/permissions", true);
  }

  win.once('ready-to-show', () => {
    win.show()
  })

  win.on("closed", () => {
    win.destroy();
  });
}

app.on('ready', createWindow)

app.on('activate', () => {
  if (BrowserWindow.getAllWindows().length === 0) {
    createWindow()
  }
})

app.on("window-all-closed", () => {
  if (process.platform !== 'darwin') {
    app.quit()
  }
});
