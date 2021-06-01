import React from 'react'

const SoundEffects: React.FC = () => {
  return (
    <div className="sound-effects">
      <audio
        id="sound-mute"
        preload="none"
        controls={false}
        src="/sound-effects/mute.wav"
      />
      <audio
        id="sound-unmute"
        preload="none"
        controls={false}
        src="/sound-effects/unmute.wav"
      />
      <audio
        id="sound-deafen"
        preload="none"
        controls={false}
        src="/sound-effects/deafen.wav"
      />
      <audio
        id="sound-undeafen"
        preload="none"
        controls={false}
        src="/sound-effects/undeafen.wav"
      />
    </div>
  )
}

export default SoundEffects
