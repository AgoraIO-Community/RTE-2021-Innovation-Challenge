import addSettingsPage from './addSettingsPage';
app.initializers.add('goaskme/points-convert', () => {
  console.log('[goaskme/points-convert Hello, admin888888888888!')
  addSettingsPage()
})
