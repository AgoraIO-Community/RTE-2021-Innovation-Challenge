import App from 'constants/app'

export enum SoundEffect {
  Mute = 'sound-mute', Unmute = 'sound-unmute', Deafen = 'sound-deafen', Undeafen = 'sound-undeafen'
}

type PlaySound = (effectType: SoundEffect) => void

const playSound: PlaySound = effect => {
  const soundEl: HTMLAudioElement | null = document.getElementById(effect) as HTMLAudioElement
  if (!soundEl) {
    return
  }
  soundEl.volume = App.SoundEffectVolume
  soundEl.play()
}

export default playSound
