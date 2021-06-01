import styled from 'styled-components'

export const Stage = styled.div`
  flex: 0.5;
  font-size: 18px;
  height: 100vh;
  max-height: 100vh;

  @media screen and (max-width: 850px) {
    flex: 0.9;
  }
  @media screen and (max-width: 570px) {
    flex: 1;
  }
`
export const StageInner = styled.div`
  border-radius: 10px;
  background-color: #f5f8fa;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  height: calc(100vh - 70px);
  max-width: 650px;
`

export const StageHeader = styled.div`
  flex-basis: 150px;
  display: flex;
  flex-direction: column;
  padding: 20px;

  margin: 10px;
  border-radius: 10px;
  background: hsla(0, 0%, 100%, 0.6);
  box-shadow: 0 0.625em 3.75em 0 #eaeaea;
  transition: all 0.25s ease 0.2s, transform 0.3s cubic-bezier(0.6, 0.2, 0.1, 1) 0.1s;
  user-select: none;
  background-image: url("https://source.unsplash.com/random");
  background-size: cover;
  background-blend-mode: overlay;
  cursor: pointer;
`

export const StageTitle = styled.div`
  overflow: hidden;
  display: flex;
  justify-content: space-between;
`

export const StageName = styled.div`
  white-space: nowrap;
  text-overflow: ellipsis;
  font-weight: bold;
  font-size: 18px;
  color: #333;
`

export const StageTimer = styled.div`
  color: #fff;
  font-size: 18px;
`

export const StageDetail = styled.div`
  margin: 10px 0px;
  color: #5f5e5e;
  font-size: 14px;
  display: flex;
`

export const StageCreator = styled.div`
  margin-left: 5px;
  font-weight: bold;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
`

export const StageDescription = styled.div`
  font-size: 16px;
  color: #333;
  text-overflow: ellipsis;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 8;
  -webkit-box-orient: vertical;
  max-height: 100px;
`

export const StageBody = styled.div`
  flex: 1;
  padding: 20px 20px 0px;
  display: flex;
  flex-direction: column;

  margin: 10px;
  border-radius: 10px;
  background: hsla(0, 0%, 100%, 0.6);
  box-shadow: 0 0.625em 3.75em 0 #eaeaea;
  user-select: none;
  overflow-y: auto;

  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
  font-size: 18px;

  &::-webkit-scrollbar {
    display: none;
  }
`

export const StageSpeaker = styled.div`

`

export const StageSpectator = styled.div`
`

export const StageCount = styled.div`
  padding-left: 10px;
  padding-right: 10px;
  height: 18px;
  line-height: 18px;
  font-size: 14px;
  background-color: #f6f6f6;
  color: #5f5e5e;
  margin-left: 5px;
  border-radius: 8px;
`

export const StageLabel = styled.div`
  font-size: 18px;
  color: #333;
  font-weight: bold;
  display: flex;
  align-items: center;
`

export const StageUserList = styled.div`
  display: flex;
  flex-wrap: wrap;
  align-content: space-between;
  margin: 20px 0px;
  width: 100%;
`

export const StageUser = styled.div`
  display: flex;
  flex-direction: column;
  height: 90px;
  width: 147.5px;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  cursor: pointer;
  position: relative;
`

export const StageUserAvatar = styled.div`
  height: 60px;
  width: 60px;
  margin-bottom: 10px;
  transition: all 0.25s ease 0.2s, transform 0.3s cubic-bezier(0.6, 0.2, 0.1, 1) 0.1s;

  &:hover {
    opacity: .8;
    transform: translateY(-2px);
  }
`

export const StageUserName = styled.div<{ primary: boolean }>`
  font-size: 14px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  color: ${props => props.primary ? '#fff' : '#333'};
  padding: 5px 10px;
  cursor: pointer;
  background: ${props => !props.primary ? '#f1f1f1' : '#1890ff'};
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.25s ease;

  &:hover {
    background: #1890ff;
    color: #fff
  }
`

export const StageSpeakerStatus = styled.div`
  width: 20px;
  height: 20px;
  position: absolute;
  transform: translate(20px, 7px);
  padding: 2px;
  border: 2px solid hsla(0, 0%, 100%, 0.6);
  background: #f1f1f1;
  box-shadow: 0 0.625em 3.75em 0 #eaeaea;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2;
  box-sizing: border-box;

  svg {
    width: 1em;
    height: 1em;
    display: inline-block;
    font-size: 13px;
    flex-shrink: 0;
    user-select: none;
    transition: transform 0.25s ease 0.2s, transform 0.3s cubic-bezier(0.6, 0.2, 0.1, 1) 0.1s;
  }

  .anticon-audio {
    svg {
      fill: #1890ff;
    }
  }

  .anticon-audio-muted {
    svg {
      fill: #fd4d4d
    }
  }

`

export const StageFooter = styled.div`
  flex-basis: 100px;
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  margin: 10px;
  border-radius: 10px;
  background: hsla(0, 0%, 100%, 0.6);
  transition: all 0.25s ease 0.2s, transform 0.3s cubic-bezier(0.6, 0.2, 0.1, 1) 0.1s;
  user-select: none;
`

export const StageBtn = styled.div<{ disabled?: boolean, primary?: boolean, border?: boolean }>`
  display: flex;
  flex-direction: column;
  align-content: center;
  justify-content: space-around;
  text-align: center;
  width: 100px;
  height: 100px;
  background-color: ${props => props.border ? '#f6f6f6' : '#fff'};;
  border-radius: 10px;
  color: ${props => props.primary ? 'rgb(253 77 77 / 80%)' : props.disabled ? 'rgb(51 51 51 / 70%)' : '#333'};
  padding: 5px;
  font-size: 15px;
  cursor: ${props => props.disabled ? 'not-allowed' : 'pointer'};
  box-shadow: inset 0 0 10px 0px #eaeaea;
  transition: box-shadow 0.25s ease 0.2s, transform 0.3s cubic-bezier(0.6, 0.2, 0.1, 1) 0.1s;

  border-color: #666;
  border-style: solid;
  border-width: ${props => props.border ? '1px' : '0px'};

  svg {
    fill: ${props => props.primary ? 'rgb(253 77 77 / 80%)' : props.disabled ? 'rgb(51 51 51 / 70%)' : '#333'};
    width: 1em;
    height: 1em;
    display: inline-block;
    font-size: 30px;
    flex-shrink: 0;
    user-select: none;
    margin-left: 5px;
    transition: transform 0.25s ease 0.2s, transform 0.3s cubic-bezier(0.6, 0.2, 0.1, 1) 0.1s;
  }

  &:hover {
    box-shadow: inset 0 0 ${props => props.disabled ? '10px' : '15px'} 0px #eaeaea;

    svg {
      transform: scale(${props => props.disabled ? '1' : '1.1'});
    }
  }
`
