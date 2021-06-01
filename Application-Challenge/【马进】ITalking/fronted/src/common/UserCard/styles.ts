import styled from 'styled-components'

export const UserCard = styled.div`
  padding: 15px 20px;
  border-radius: 8px;
  background-color: #f5f8fa;
  margin: 20px 0px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  cursor: pointer;
`

export const UserAvatar = styled.div`
  height: 80px;
  width: 80px;
  border-radius: 50%;
  cursor: pointer;
  box-shadow: 0 0 2px 3px hsl(0deg 0% 100% / 70%), 0 0 30px 8px #f6f6f6;
  transition: all 200ms cubic-bezier(0.4, 0, 0.2, 1) 0ms;

  &:hover {
    transform: scale(1.05);
    box-shadow: 0 0 2px 3px hsl(0deg 0% 100% / 70%), 0 0 30px 8px #f1f1f1;
  }
`

export const UserName = styled.div`
  margin-top: 8px;
  font-weight: 700;
  cursor: pointer;
  transition: all .2s;
  
  &:hover {
    opacity: .8;
  }
`

export const UserDescription = styled.div`
  margin-top: 10px;
  color: rgba(117, 117, 117, 0.72);
  cursor: pointer;
  width: 100%;
  text-align: center;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  transition: all .2s;
  
  &:hover {
    opacity: .8;
  }
`

export const UserFollowDetail = styled.div`
  margin-top: 10px;
  display: flex;
  cursor: default;
  color: #505050;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  width: 100%;
  justify-content: center;
`

export const UserFollowLabel = styled.span`
  font-weight: bold;
  margin-right: 3px;
`

export const UserFollowBlock = styled.div`
  margin: 0px 5px;
  padding: 5px 10px;
  cursor: pointer;
  background-color: #ecf0f3;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all .2s;
  
  &:hover {
    background-color: #d1d9e6;
  }
`
